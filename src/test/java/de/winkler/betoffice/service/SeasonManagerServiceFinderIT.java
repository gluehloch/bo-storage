/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2022 by Andre Winkler. All
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

package de.winkler.betoffice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import de.winkler.betoffice.storage.Community;
import de.winkler.betoffice.storage.CommunityReference;
import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.GameResult;
import de.winkler.betoffice.storage.Group;
import de.winkler.betoffice.storage.GroupType;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.Team;
import de.winkler.betoffice.storage.TeamResult;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.enums.SeasonType;

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
        GameList findNextTippRound = tippService.findNextTippRound(season.getId(), date);
        assertThat(findNextTippRound.getId()).isEqualTo(321);
    }

    @Test
    void testSelectCompleteSeason() {
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
    }

    @Test
    void testFindMatches() {
        Optional<Team> stuttgart = masterDataManagerService.findTeam("VfB Stuttgart");
        Optional<Team> hsv = masterDataManagerService.findTeam("Hamburger SV");

        List<Game> matchesHsvStuttgart = seasonManagerService.findMatches(stuttgart.get(), hsv.get());
        assertThat(matchesHsvStuttgart.size()).isEqualTo(22);

        List<Game> matchesStuttgartHsv = seasonManagerService.findMatches(hsv.get(), stuttgart.get());
        assertThat(matchesStuttgartHsv.size()).isEqualTo(24);

        List<Game> allMatchesStuttgartHsv = seasonManagerService.findMatches(stuttgart.get(), hsv.get(), true);
        assertThat(allMatchesStuttgartHsv.size()).isEqualTo(46);
    }

    @Test
    void testFindMatchesWithSpinButFalse() {
        Optional<Team> stuttgart = masterDataManagerService
                .findTeam("VfB Stuttgart");
        Optional<Team> hsv = masterDataManagerService.findTeam("Hamburger SV");

        List<Game> matchesHsvStuttgart = seasonManagerService
                .findMatches(stuttgart.get(), hsv.get(), false);
        assertThat(matchesHsvStuttgart.size()).isEqualTo(22);

        List<Game> matchesStuttgartHsv = seasonManagerService
                .findMatches(hsv.get(), stuttgart.get(), false);
        assertThat(matchesStuttgartHsv.size()).isEqualTo(24);

        List<Game> allMatchesStuttgartHsv = seasonManagerService
                .findMatches(stuttgart.get(), hsv.get(), true);

        assertThat(allMatchesStuttgartHsv.size()).isEqualTo(46);
    }

    @Test
    void testFindAllSeasons() {
        assertThat(seasonManagerService.findAllSeasons().size()).isEqualTo(33);
    }

    /**
     * Queries depending on the results of WM 2006 in Germany.
     */
    @Test
    void testFindWm2006() {
        Optional<Season> wm2006 = seasonManagerService.findSeasonByName("WM Deutschland", "2006");
        assertThat(wm2006).isPresent().containsInstanceOf(Season.class).hasValueSatisfying((season) -> {
            assertThat(season.getReference().getYear()).isEqualTo("2006");
            assertThat(season.getReference().getName()).isEqualTo("WM Deutschland");
        });

        List<GameList> rounds = seasonManagerService.findRounds(wm2006.get());
        assertThat(rounds).hasSize(25);
    }

    @Test
    void testFindActiveUsersWm2006() {
        CommunityReference tdkb2006 = CommunityReference.of("TDKB 2006");
        Community community = communityService.find(tdkb2006).orElseThrow();
        Season wm2006 = seasonManagerService.findSeasonByName("WM Deutschland", "2006").orElseThrow();

        Set<User> members = communityService.findMembers(tdkb2006);
        assertThat(members).hasSize(11);

        final String[] usersWm2006 = new String[] { "Andi", "Bernd_das_Brot", "chris",
                "Frosch", "Goddard", "Hattwig", "Jogi", "mrTipp", "Peter",
                "Roenne", "Steffen" };

        assertThat(members).extracting("nickname.value").contains("Andi", "Bernd_das_Brot", "chris",
                "Frosch", "Goddard", "Hattwig", "Jogi", "mrTipp", "Peter",
                "Roenne", "Steffen");
        
//        for (int index = 0; index < usersWm2006.length; index++) {
//            assertThat(members.get(index).getNickname()).isEqualTo(usersWm2006[index]);
//        }
    }

    @Test
    void testFindGroupTypesWm2006() {
        Optional<Season> wm2006 = seasonManagerService.findSeasonByName("WM Deutschland", "2006");

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
    }

    @Test
    void testFindGroupWm2006() {
        Optional<Season> wm2006 = seasonManagerService.findSeasonByName("WM Deutschland", "2006");

        List<Group> groups = seasonManagerService.findGroups(wm2006.get());
        assertThat(groups.size()).isEqualTo(13);
    }

    @Test
    public void testFindTeamsByGroupTypeWm2006() {
        Optional<Season> wm2006 = seasonManagerService.findSeasonByName("WM Deutschland", "2006");

        Optional<GroupType> finale = masterDataManagerService.findGroupType("Finale");
        Optional<Team> italien = masterDataManagerService.findTeam("Italien");
        Optional<Team> frankreich = masterDataManagerService.findTeam("Frankreich");

        List<Team> finalTeams = seasonManagerService.findTeams(wm2006.get(),
                finale.get());

        assertThat(finalTeams.size()).isEqualTo(2);
        assertThat(finalTeams.get(0)).isEqualTo(frankreich.get());
        assertThat(finalTeams.get(1)).isEqualTo(italien.get());
    }

    @Test
    public void testFindTeamsByGroupWm2006() {
        Optional<Season> wm2006 = seasonManagerService.findSeasonByName("WM Deutschland", "2006");
        List<Group> groups = seasonManagerService.findGroups(wm2006.get());
        assertThat(groups.size()).isEqualTo(13);
        Group achtelfinale = groups.get(0);
        assertThat(achtelfinale.getGroupType().getName()).isEqualTo("Achtelfinale");

        List<Team> teams = seasonManagerService.findTeams(achtelfinale);
        assertThat(teams.size()).isEqualTo(16);
    }

    @Test
    void testFindRoundWm2006() {
        Season wm2006 = seasonManagerService.findSeasonByName("WM Deutschland", "2006").orElseThrow();

        List<GameList> rounds = seasonManagerService.findRounds(wm2006);
        assertThat(rounds.size()).isEqualTo(25);

        GameList spieltag_1 = seasonManagerService.findRoundGames(rounds.get(0).getId()).orElseThrow();
        assertThat(spieltag_1.getGroup().getGroupType().getName()).isEqualTo("Gruppe A");

        GameList spieltag_2 = seasonManagerService.findRoundGames(rounds.get(1).getId()).orElseThrow();
        assertThat(spieltag_2.getGroup().getGroupType().getName()).isEqualTo("Gruppe B");

        GameList finale = seasonManagerService.findRoundGames(rounds.get(24).getId()).orElseThrow();
        assertThat(finale.getGroup().getGroupType().getName()).isEqualTo("Finale");

        Optional<Team> italien = masterDataManagerService.findTeam("Italien");
        Optional<Team> frankreich = masterDataManagerService.findTeam("Frankreich");
        Game finalRoundMatch = finale.get(0);

        assertThat(finalRoundMatch.getHomeTeam()).isEqualTo(italien.get());
        assertThat(finalRoundMatch.getGuestTeam()).isEqualTo(frankreich.get());
    }

    @Test
    void testFindMatchWm2006() {
        Optional<Season> wm2006 = seasonManagerService.findSeasonByName("WM Deutschland", "2006");

        List<GameList> rounds = seasonManagerService.findRounds(wm2006.get());
        assertThat(rounds.size()).isEqualTo(25);

        Optional<Team> italien = masterDataManagerService.findTeam("Italien");
        Optional<Team> frankreich = masterDataManagerService.findTeam("Frankreich");

        GameList finale = rounds.get(24);
        Optional<Game> finalMatch = seasonManagerService.findMatch(finale, italien.get(), frankreich.get());
        assertThat(finalMatch.get().getResult()).isEqualTo(new GameResult(5, 3));

        Optional<Team> deutschland = masterDataManagerService.findTeam("Deutschland");
        Optional<Team> portugal = masterDataManagerService.findTeam("Portugal");

        GameList platz3 = rounds.get(23);
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
        Object object = databaseMaintenanceService.executeHql("from Season");
        assertEquals(33, ((List<?>) object).size());

        Object object2 = databaseMaintenanceService.executeHql(
                "select s " + "from Season s left join fetch s.groups as group "
                        + "where s.reference.name = 'WM Deutschland' and s.reference.year = 2006");
        assertEquals(13, ((List<?>) object2).size());
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
