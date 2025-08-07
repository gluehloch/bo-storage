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
import de.betoffice.storage.group.entity.GroupType;
import de.betoffice.storage.season.SeasonType;
import de.betoffice.storage.season.entity.Game;
import de.betoffice.storage.season.entity.GameList;
import de.betoffice.storage.season.entity.GameResult;
import de.betoffice.storage.season.entity.Group;
import de.betoffice.storage.season.entity.Season;
import de.betoffice.storage.team.TeamResult;
import de.betoffice.storage.team.entity.Team;
import de.betoffice.storage.user.entity.User;

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
        Season season = seasonManagerService.findSeasonById(11);
        ZonedDateTime date = ZonedDateTime.of(2008, 5, 6, 1, 0, 0, 0, ZoneId.of("Europe/Berlin"));
        Optional<GameList> findNextTippRound = tippService.findNextTippRound(season.getId(), date);
        assertThat(findNextTippRound).isPresent().map(gl -> gl.getId()).isPresent().get().isEqualTo(321L);
    }

    @Test
    void testSelectAndFind() {
        //
        // select season
        //
        Season season = seasonManagerService.findSeasonById(11);
        assertThat(season.getReference().getName()).isEqualTo("Fussball Bundesliga");
        assertThat(season.getReference().getYear()).isEqualTo("2007/2008");
        assertThat(season.getMode()).isEqualTo(SeasonType.LEAGUE);

        Season fullSeason = seasonManagerService.findSeasonById(season.getId());
        assertNotNull(fullSeason);

        GroupType bundesliga = masterDataManagerService.findGroupType("1. Liga").orElseThrow();
        assertThat(bundesliga.getName()).isEqualTo("1. Liga");
        Group bundesliga_2008_2009 = seasonManagerService.findGroup(season, bundesliga);

        List<Team> teams = seasonManagerService.findTeams(bundesliga_2008_2009);
        assertThat(teams).hasSize(18);

        GameList round = seasonManagerService.findRound(season, 0).orElseThrow();
        assertThat(round.size()).isEqualTo(9);

        //
        // find matches
        //
        Team stuttgart = masterDataManagerService.findTeam("VfB Stuttgart").orElseThrow();
        Team hsv = masterDataManagerService.findTeam("Hamburger SV").orElseThrow();

        List<Game> matchesHsvStuttgart = seasonManagerService.findMatches(stuttgart, hsv, 20);
        assertThat(matchesHsvStuttgart.size()).isEqualTo(17);

        List<Game> matchesStuttgartHsv = seasonManagerService.findMatches(hsv, stuttgart, 20);
        assertThat(matchesStuttgartHsv.size()).isEqualTo(17);

        List<Game> allMatchesStuttgartHsv = seasonManagerService.findMatches(stuttgart, hsv, true, 100);
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
        Optional<Season> wm2006 = seasonManagerService.findSeasonByName("WM Deutschland", "2006");
        assertThat(wm2006).isPresent().containsInstanceOf(Season.class).hasValueSatisfying(s -> {
            assertThat(s.getReference().getYear()).isEqualTo("2006");
            assertThat(s.getReference().getName()).isEqualTo("WM Deutschland");
        });

        List<GameList> rounds = seasonManagerService.findRounds(wm2006.get());
        assertThat(rounds).hasSize(25);

        //
        // find all users
        //
        CommunityReference tdkb2006 = CommunityReference.of("TDKB 2006");

        Set<User> members = communityService.findMembers(tdkb2006);
        assertThat(members).hasSize(11);

        assertThat(members).extracting("nickname.nickname").contains("Andi", "Bernd_das_Brot", "chris",
                "Frosch", "Goddard", "Hattwig", "Jogi", "mrTipp", "Peter",
                "Roenne", "Steffen");

        // 
        // find group types
        //
        List<GroupType> groupTypes = seasonManagerService.findGroupTypesBySeason(wm2006.get());
        assertThat(groupTypes.size()).isEqualTo(13);

        String[] groupTypesWm2006 = new String[] { "Achtelfinale", "Finale",
                "Gruppe A", "Gruppe B", "Gruppe C", "Gruppe D", "Gruppe E",
                "Gruppe F", "Gruppe G", "Gruppe H", "Halbfinale",
                "Spiel um Platz 3", "Viertelfinale" };

        for (int index = 0; index < groupTypesWm2006.length; index++) {
            assertThat(groupTypes.get(index).getName())
                    .isEqualTo(groupTypesWm2006[index]);
        }

        List<Group> groups = seasonManagerService.findGroups(wm2006.get());
        assertThat(groups.size()).isEqualTo(13);

        //
        // find teams by group type
        //
        GroupType finale = masterDataManagerService.findGroupType("Finale").orElseThrow();
        Team italien = masterDataManagerService.findTeam("Italien").orElseThrow();
        Team frankreich = masterDataManagerService.findTeam("Frankreich").orElseThrow();

        List<Team> finalTeams = seasonManagerService.findTeams(wm2006.get(), finale);

        assertThat(finalTeams.size()).isEqualTo(2);
        assertThat(finalTeams.get(0)).isEqualTo(frankreich);
        assertThat(finalTeams.get(1)).isEqualTo(italien);

        List<Group> groupsWm2006 = seasonManagerService.findGroups(wm2006.get());
        assertThat(groupsWm2006.size()).isEqualTo(13);
        Group achtelfinale = groupsWm2006.get(0);
        assertThat(achtelfinale.getGroupType().getName()).isEqualTo("Achtelfinale");

        List<Team> teamsWM2006Achtelfinale = seasonManagerService.findTeams(achtelfinale);
        assertThat(teamsWM2006Achtelfinale.size()).isEqualTo(16);

        //
        // find rounds
        //

        List<GameList> roundsWm2006 = seasonManagerService.findRounds(wm2006.get());
        assertThat(roundsWm2006.size()).isEqualTo(25);

        GameList spieltag_1 = seasonManagerService.findRoundGames(roundsWm2006.get(0).getId()).orElseThrow();
        assertThat(spieltag_1.getGroup().getGroupType().getName()).isEqualTo("Gruppe A");

        GameList spieltag_2 = seasonManagerService.findRoundGames(roundsWm2006.get(1).getId()).orElseThrow();
        assertThat(spieltag_2.getGroup().getGroupType().getName()).isEqualTo("Gruppe B");

        GameList finaleWm2006 = seasonManagerService.findRoundGames(roundsWm2006.get(24).getId()).orElseThrow();
        assertThat(finaleWm2006.getGroup().getGroupType().getName()).isEqualTo("Finale");

        Game finalRoundMatch = finaleWm2006.get(0);

        assertThat(finalRoundMatch.getHomeTeam()).isEqualTo(italien);
        assertThat(finalRoundMatch.getGuestTeam()).isEqualTo(frankreich);

        // Damals noch ohne KO Spiele...
        assertThat(finalRoundMatch.getResult()).isEqualTo(new GameResult(5, 3));

        Optional<Team> deutschland = masterDataManagerService.findTeam("Deutschland");
        Optional<Team> portugal = masterDataManagerService.findTeam("Portugal");

        GameList platz3 = roundsWm2006.get(23);
        Optional<Game> platz3Match = seasonManagerService.findMatch(platz3, deutschland.get(), portugal.get());
        assertThat(platz3Match.get().getResult()).isEqualTo(new GameResult(3, 1));
    }

    @Test
    void testCalculateTeamRanking() {
        Optional<Season> buli = seasonManagerService.findSeasonByName("Fussball Bundesliga", "2006/2007");
        Optional<GameList> round = seasonManagerService.findRound(buli.get(), 0);
        Group bundesliga = round.get().getGroup();
        assertEquals(9, round.get().toList(bundesliga).size());

        List<Group> groups = seasonManagerService.findGroups(buli.get());
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
        Object object = databaseMaintenanceService.executeHql("select s from Season s");
        assertEquals(34, ((List<?>) object).size());

        Object object2 = databaseMaintenanceService.executeHql(
                """
                select
                	s
                from
                	Season s
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
