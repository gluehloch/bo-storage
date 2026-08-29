/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2024 by Andre Winkler. All
 * rights reserved.
 * ============================================================================
 * GNU GENERAL PUBLIC LICENSE TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND
 * MODIFICATION
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

package de.betoffice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.betoffice.database.data.DatabaseTestData.DataLoader;
import de.betoffice.storage.community.entity.CommunityReference;
import de.betoffice.storage.group.entity.GroupTypeEntity;
import de.betoffice.storage.season.SeasonType;
import de.betoffice.storage.season.entity.GameEntity;
import de.betoffice.storage.season.entity.GameListEntity;
import de.betoffice.storage.season.entity.GameResult;
import de.betoffice.storage.season.entity.GroupEntity;
import de.betoffice.storage.season.entity.SeasonEntity;
import de.betoffice.storage.team.TeamResult;
import de.betoffice.storage.team.entity.TeamEntity;
import de.betoffice.storage.user.entity.UserEntity;

/**
 * Test of the finder methods of {@link SeasonManagerService}.
 *
 * @author by Andre Winkler
 */
@Tag("complete-database-ex-tipp")
class SeasonManagerServiceFinderIT extends AbstractServiceTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SeasonManagerService seasonManagerService;

    @Autowired
    private TippService tippService;

    @Autowired
    private MasterDataManagerService masterDataManagerService;

    @Autowired
    private DatabaseMaintenanceService databaseMaintenanceService;

    @Autowired
    private CommunityService communityService;

    private DatabaseSetUpAndTearDown dsuatd;

    @BeforeEach
    void setUp() throws Exception {
        dsuatd = new DatabaseSetUpAndTearDown(dataSource);
        dsuatd.setUp(DataLoader.COMPLETE_EX_TIPP);
    }

    @AfterEach
    void tearDown() throws SQLException {
        dsuatd.tearDown();
    }

    // ------------------------------------------------------------------------

    @Test
    void testNextTippForm() {
        SeasonEntity season = seasonManagerService.findSeasonById(11);
        ZonedDateTime date = ZonedDateTime.of(2008, 5, 6, 1, 0, 0, 0, ZoneId.of("Europe/Berlin"));
        Optional<GameListEntity> findNextTippRound = tippService.findNextTippRound(season.getId(), date);
        assertThat(findNextTippRound).isPresent().map(gl -> gl.getId()).isPresent().get().isEqualTo(321L);
    }

    @Test
    void testSelectAndFind() {
        //
        // select season
        //
        SeasonEntity season = seasonManagerService.findSeasonById(11);
        assertThat(season.getReference().getName()).isEqualTo("Fussball Bundesliga");
        assertThat(season.getReference().getYear()).isEqualTo("2007/2008");
        assertThat(season.getMode()).isEqualTo(SeasonType.LEAGUE);

        SeasonEntity fullSeason = seasonManagerService.findSeasonById(season.getId());
        assertNotNull(fullSeason);

        GroupTypeEntity bundesliga = masterDataManagerService.findGroupType("1. Liga").orElseThrow();
        assertThat(bundesliga.getName()).isEqualTo("1. Liga");
        GroupEntity bundesliga_2008_2009 = seasonManagerService.findGroup(season, bundesliga);

        List<TeamEntity> teams = seasonManagerService.findTeams(bundesliga_2008_2009);
        assertThat(teams).hasSize(18);

        GameListEntity round = seasonManagerService.findRound(season, 0).orElseThrow();
        assertThat(round.size()).isEqualTo(9);

        //
        // find matches
        //
        TeamEntity stuttgart = masterDataManagerService.findTeam("VfB Stuttgart").orElseThrow();
        TeamEntity hsv = masterDataManagerService.findTeam("Hamburger SV").orElseThrow();

        List<GameEntity> matchesHsvStuttgart = seasonManagerService.findMatches(stuttgart, hsv, 20);
        assertThat(matchesHsvStuttgart.size()).isEqualTo(17);

        List<GameEntity> matchesStuttgartHsv = seasonManagerService.findMatches(hsv, stuttgart, 20);
        assertThat(matchesStuttgartHsv.size()).isEqualTo(17);

        List<GameEntity> allMatchesStuttgartHsv = seasonManagerService.findMatches(stuttgart, hsv, true, 100);
        assertThat(allMatchesStuttgartHsv.size()).isEqualTo(34);

        assertThat(seasonManagerService.findMatchesWithHomeTeam(hsv, 10)).isNotEmpty();
        assertThat(seasonManagerService.findMatchesWithGuestTeam(stuttgart, 10)).isNotEmpty();
        assertThat(seasonManagerService.findMatches(hsv, 10)).isNotEmpty();
        //
        // find all season
        //

        assertThat(seasonManagerService.findAllSeasons().size()).isEqualTo(34);

        //
        // find WM 2006
        //
        Optional<SeasonEntity> wm2006 = seasonManagerService.findSeasonByName("WM Deutschland", "2006");
        assertThat(wm2006).isPresent().containsInstanceOf(SeasonEntity.class).hasValueSatisfying(s -> {
            assertThat(s.getReference().getYear()).isEqualTo("2006");
            assertThat(s.getReference().getName()).isEqualTo("WM Deutschland");
        });

        List<GameListEntity> rounds = seasonManagerService.findRounds(wm2006.get());
        assertThat(rounds).hasSize(25);

        //
        // find all users
        //
        CommunityReference tdkb2006 = CommunityReference.of("TDKB 2006");

        Set<UserEntity> members = communityService.findMembers(tdkb2006);
        assertThat(members).hasSize(11);

        assertThat(members).extracting("nickname.nickname").contains("Andi", "Bernd_das_Brot", "chris",
                "Frosch", "Goddard", "Hattwig", "Jogi", "mrTipp", "Peter",
                "Roenne", "Steffen");

        // 
        // find group types
        //
        List<GroupTypeEntity> groupTypes = seasonManagerService.findGroupTypes(wm2006.get());
        assertThat(groupTypes.size()).isEqualTo(13);

        String[] groupTypesWm2006 = new String[] { "Achtelfinale", "Finale",
                "Gruppe A", "Gruppe B", "Gruppe C", "Gruppe D", "Gruppe E",
                "Gruppe F", "Gruppe G", "Gruppe H", "Halbfinale",
                "Spiel um Platz 3", "Viertelfinale" };

        for (int index = 0; index < groupTypesWm2006.length; index++) {
            assertThat(groupTypes.get(index).getName())
                    .isEqualTo(groupTypesWm2006[index]);
        }

        List<GroupEntity> groups = seasonManagerService.findGroups(wm2006.get());
        assertThat(groups.size()).isEqualTo(13);

        //
        // find teams by group type
        //
        GroupTypeEntity finale = masterDataManagerService.findGroupType("Finale").orElseThrow();
        TeamEntity italien = masterDataManagerService.findTeam("Italien").orElseThrow();
        TeamEntity frankreich = masterDataManagerService.findTeam("Frankreich").orElseThrow();

        List<TeamEntity> finalTeams = seasonManagerService.findTeams(wm2006.get(), finale);

        assertThat(finalTeams.size()).isEqualTo(2);
        assertThat(finalTeams.get(0)).isEqualTo(frankreich);
        assertThat(finalTeams.get(1)).isEqualTo(italien);

        List<GroupEntity> groupsWm2006 = seasonManagerService.findGroups(wm2006.get());
        assertThat(groupsWm2006.size()).isEqualTo(13);
        GroupEntity achtelfinale = groupsWm2006.get(0);
        assertThat(achtelfinale.getGroupType().getName()).isEqualTo("Achtelfinale");

        List<TeamEntity> teamsWM2006Achtelfinale = seasonManagerService.findTeams(achtelfinale);
        assertThat(teamsWM2006Achtelfinale.size()).isEqualTo(16);

        //
        // find rounds
        //

        List<GameListEntity> roundsWm2006 = seasonManagerService.findRounds(wm2006.get());
        assertThat(roundsWm2006.size()).isEqualTo(25);

        GameListEntity spieltag_1 = seasonManagerService.findRoundGames(roundsWm2006.get(0).getId()).orElseThrow();
        assertThat(spieltag_1.getGroup().getGroupType().getName()).isEqualTo("Gruppe A");

        GameListEntity spieltag_2 = seasonManagerService.findRoundGames(roundsWm2006.get(1).getId()).orElseThrow();
        assertThat(spieltag_2.getGroup().getGroupType().getName()).isEqualTo("Gruppe B");

        GameListEntity finaleWm2006 = seasonManagerService.findRoundGames(roundsWm2006.get(24).getId()).orElseThrow();
        assertThat(finaleWm2006.getGroup().getGroupType().getName()).isEqualTo("Finale");

        GameEntity finalRoundMatch = finaleWm2006.get(0);

        assertThat(finalRoundMatch.getHomeTeam()).isEqualTo(italien);
        assertThat(finalRoundMatch.getGuestTeam()).isEqualTo(frankreich);

        // Damals noch ohne KO Spiele...
        assertThat(finalRoundMatch.getResult()).isEqualTo(new GameResult(5, 3));

        Optional<TeamEntity> deutschland = masterDataManagerService.findTeam("Deutschland");
        Optional<TeamEntity> portugal = masterDataManagerService.findTeam("Portugal");

        GameListEntity platz3 = roundsWm2006.get(23);
        Optional<GameEntity> platz3Match = seasonManagerService.findMatch(platz3, deutschland.get(), portugal.get());
        assertThat(platz3Match.get().getResult()).isEqualTo(new GameResult(3, 1));

        //
        // Find games of a day
        //

        // TODO https://github.com/gluehloch/bo-storage/issues/4 
        //        List<Game> games = seasonManagerService.findMatches(spieltag_2.getDateTime());
        //        assertThat(games).hasSize(3);
        //        assertThat(games.get(0).getHomeTeam().getName()).isEqualTo("Deutschland");
        //        assertThat(games.get(0).getGuestTeam().getName()).isEqualTo("Costa Rica");
        //        assertThat(games.get(1).getHomeTeam().getName()).isEqualTo("Polen");
        //        assertThat(games.get(1).getGuestTeam().getName()).isEqualTo("Ecuador");
        //
        //        assertThat(games.get(0).getDateTime().toLocalDate()).isEqualTo(spieltag_2.getDateTime().toLocalDate());
        //        assertThat(games.get(1).getDateTime().toLocalDate()).isEqualTo(spieltag_2.getDateTime().toLocalDate());
    }

    @Test
    void testCalculateTeamRanking() {
        Optional<SeasonEntity> buli = seasonManagerService.findSeasonByName("Fussball Bundesliga", "2006/2007");
        Optional<GameListEntity> round = seasonManagerService.findRound(buli.get(), 0);
        GroupEntity bundesliga = round.get().getGroup();
        assertEquals(9, round.get().toList(bundesliga).size());

        List<GroupEntity> groups = seasonManagerService.findGroups(buli.get());
        List<TeamResult> teamResults = seasonManagerService
                .calculateTeamRanking(buli.get(), groups.get(0).getGroupType());

        validateTeamResult(teamResults, 0, "VfB Stuttgart", 61, 37, 70);
        validateTeamResult(teamResults, 1, "FC Schalke 04", 53, 32, 68);
        validateTeamResult(teamResults, 2, "SV Werder Bremen", 76, 40, 66);
        validateTeamResult(teamResults, 17, "Borussia MGladbach", 23, 44, 26);

        teamResults = seasonManagerService.calculateTeamRanking(buli.get(),
                groups.get(0).getGroupType(), 0, 4);
        validateTeamResult(teamResults, 0, "FC Bayern München", 7, 4, 10);
        validateTeamResult(teamResults, 1, "FC Schalke 04", 6, 3, 10);
        validateTeamResult(teamResults, 2, "Hertha BSC Berlin", 8, 2, 9);
        validateTeamResult(teamResults, 3, "1.FC Nürnberg", 6, 2, 9);
        validateTeamResult(teamResults, 4, "Borussia MGladbach", 6, 5, 9);
    }

    @Test
    void testDatabaseMaintenanceService() {
        Object object = databaseMaintenanceService.executeHql("select s from SeasonEntity s");
        assertEquals(34, ((List<?>) object).size());

        Object object2 = databaseMaintenanceService.executeHql(
                """
                        select
                        	s
                        from
                        	SeasonEntity s
                         	left join fetch s.groups as group
                               where
                            s.reference.name = 'WM Deutschland'
                            and s.reference.year = '2006'
                        """);
        assertEquals(1, ((List<?>) object2).size());
    }

    private void validateTeamResult(final List<TeamResult> teamResults,
            final int index, final String teamName, final int positiveGoals,
            final int negativeGoals, final int points) {

        TeamResult teamResult = teamResults.get(index);
        assertThat(teamResult.getTeam().getName()).isEqualTo(teamName);
        assertThat(teamResult.getPosGoals()).isEqualTo(positiveGoals);
        assertThat(teamResult.getNegGoals()).isEqualTo(negativeGoals);
        assertThat(teamResult.getPoints()).isEqualTo(points);
    }

}
