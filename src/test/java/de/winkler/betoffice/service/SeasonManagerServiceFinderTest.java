/*
 * $Id: SeasonManagerServiceFinderTest.java 3850 2013-11-30 18:24:08Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2010 by Andre Winkler. All rights reserved.
 * ============================================================================
 *          GNU GENERAL PUBLIC LICENSE
 *  TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package de.winkler.betoffice.service;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.awtools.basic.LoggerFactory;
import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.GameResult;
import de.winkler.betoffice.storage.GameTipp;
import de.winkler.betoffice.storage.Group;
import de.winkler.betoffice.storage.GroupType;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.Team;
import de.winkler.betoffice.storage.TeamResult;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.UserResult;
import de.winkler.betoffice.storage.enums.SeasonType;
import de.winkler.betoffice.test.database.MySqlDatabasedTestSupport.DataLoader;

/**
 * Test of the finder methods of {@link SeasonManagerService}.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3850 $ $LastChangedDate: 2013-11-30 19:24:08 +0100 (Sa, 30 Nov 2013) $
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/betoffice-datasource.xml",
        "/betoffice-persistence.xml", "/test-mysql-piratestest.xml" })
public class SeasonManagerServiceFinderTest {

    private final Logger log = LoggerFactory.make();

    @Autowired
    protected DataSource dataSource;

    @Autowired
    protected SeasonManagerService seasonManagerService;

    @Autowired
    protected TippService tippService;

    @Autowired
    protected MasterDataManagerService masterDataManagerService;

    @Autowired
    protected DatabaseMaintenanceService databaseMaintenanceService;

    private DatabaseSetUpAndTearDown dsuatd;

    @Before
    public void setUp() throws Exception {
        dsuatd = new DatabaseSetUpAndTearDown(dataSource);
        dsuatd.setUp(DataLoader.FULL);
    }

    @After
    public void tearDown() throws SQLException {
        dsuatd.tearDown();
    }

    // ------------------------------------------------------------------------

    @Test
    public void testNextTippForm() {
        Season season = seasonManagerService.findSeasonById(11);
        DateTime date = new DateTime(2008, 5, 6, 1, 0, 0);
        GameList findNextTippRound = tippService.findNextTippRound(season, date);
        assertThat(findNextTippRound.getId()).isEqualTo(321);
    }

    @Test
    public void testSelectCompleteSeason() {
        Season season = seasonManagerService.findSeasonById(11);
        assertThat(season.getName()).isEqualTo("Fussball Bundesliga");
        assertThat(season.getMode()).isEqualTo(SeasonType.LEAGUE);

        Season fullSeason = seasonManagerService
                .findRoundGroupTeamUserTippRelations(season);
        assertNotNull(fullSeason);
    }

    @Test
    public void testFindMatches() {
        Team stuttgart = masterDataManagerService.findTeam("VfB Stuttgart");
        Team hsv = masterDataManagerService.findTeam("Hamburger SV");

        List<Game> matchesHsvStuttgart = seasonManagerService.findMatches(
                stuttgart, hsv);
        assertThat(matchesHsvStuttgart.size()).isEqualTo(13);

        List<Game> matchesStuttgartHsv = seasonManagerService.findMatches(hsv,
                stuttgart);
        assertThat(matchesStuttgartHsv.size()).isEqualTo(14);

        List<Game> allMatchesStuttgartHsv = seasonManagerService.findMatches(
                stuttgart, hsv, true);

        assertThat(allMatchesStuttgartHsv.size()).isEqualTo(27);
    }

    @Test
    public void testFindMatchesWithSpinButFalse() {
        Team stuttgart = masterDataManagerService.findTeam("VfB Stuttgart");
        Team hsv = masterDataManagerService.findTeam("Hamburger SV");

        List<Game> matchesHsvStuttgart = seasonManagerService.findMatches(
                stuttgart, hsv, false);
        assertThat(matchesHsvStuttgart.size()).isEqualTo(13);

        List<Game> matchesStuttgartHsv = seasonManagerService.findMatches(hsv,
                stuttgart, false);
        assertThat(matchesStuttgartHsv.size()).isEqualTo(14);

        List<Game> allMatchesStuttgartHsv = seasonManagerService.findMatches(
                stuttgart, hsv, true);

        assertThat(allMatchesStuttgartHsv.size()).isEqualTo(27);
    }

    @Test
    public void testFindAllSeasons() {
        assertThat(seasonManagerService.findAllSeasons().size()).isEqualTo(20);
    }

    /**
     * Queries depending on the results of WM 2006 in Germany.
     */
    @Test
    public void testFindWm2006() {
        Season wm2006 = seasonManagerService.findSeasonByName("WM Deutschland",
                "2006");
        assertThat(wm2006).isNotNull();
        assertThat(wm2006.getYear()).isEqualTo("2006");
        assertThat(wm2006.getName()).isEqualTo("WM Deutschland");

        //
        // Die Anzahl der Spieltage kann nicht abgefragt werden!
        // assertEquals(25, wm2006.size());
        // Hier droht eine LazyInitializationException Ausnahme!
        //
    }

    @Test
    public void testFindActiveUsersWm2006() {
        Season wm2006 = seasonManagerService.findSeasonByName("WM Deutschland",
                "2006");

        List<User> users = seasonManagerService.findActivatedUsers(wm2006);
        assertThat(users.size()).isEqualTo(11);

        String[] usersWm2006 = new String[] { "Andi", "Bernd_das_Brot",
                "chris", "Frosch", "Goddard", "Hattwig", "Jogi", "mrTipp",
                "Peter", "Roenne", "Steffen" };

        for (int index = 0; index < usersWm2006.length; index++) {
            assertThat(users.get(index).getNickName()).isEqualTo(
                    usersWm2006[index]);
        }
    }

    @Test
    public void testFindGroupTypesWm2006() {
        Season wm2006 = seasonManagerService.findSeasonByName("WM Deutschland",
                "2006");

        List<GroupType> groupTypes = seasonManagerService
                .findGroupTypesBySeason(wm2006);
        assertThat(groupTypes.size()).isEqualTo(13);

        String[] groupTypesWm2006 = new String[] { "Achtelfinale", "Finale",
                "Gruppe A", "Gruppe B", "Gruppe C", "Gruppe D", "Gruppe E",
                "Gruppe F", "Gruppe G", "Gruppe H", "Halbfinale",
                "Spiel um Platz 3", "Viertelfinale" };

        for (int index = 0; index < groupTypesWm2006.length; index++) {
            assertThat(groupTypes.get(index).getName()).isEqualTo(
                    groupTypesWm2006[index]);
        }
    }

    @Test
    public void testFindGroupWm2006() {
        Season wm2006 = seasonManagerService.findSeasonByName("WM Deutschland",
                "2006");

        List<Group> groups = seasonManagerService.findGroups(wm2006);
        assertThat(groups.size()).isEqualTo(13);
    }

    @Test
    public void testFindTeamsByGroupTypeWm2006() {
        Season wm2006 = seasonManagerService.findSeasonByName("WM Deutschland",
                "2006");

        GroupType finale = masterDataManagerService.findGroupType("Finale");
        Team italien = masterDataManagerService.findTeam("Italien");
        Team frankreich = masterDataManagerService.findTeam("Frankreich");

        List<Team> finalTeams = seasonManagerService.findTeamsByGroupType(
                wm2006, finale);

        assertThat(finalTeams.size()).isEqualTo(2);
        assertThat(finalTeams.get(0)).isEqualTo(frankreich);
        assertThat(finalTeams.get(1)).isEqualTo(italien);
    }

    @Test
    public void testFindTeamsByGroupWm2006() {
        Season wm2006 = seasonManagerService.findSeasonByName("WM Deutschland",
                "2006");
        List<Group> groups = seasonManagerService.findGroups(wm2006);
        assertThat(groups.size()).isEqualTo(13);
        Group achtelfinale = groups.get(0);
        assertThat(achtelfinale.getGroupType().getName()).isEqualTo(
                "Achtelfinale");

        List<Team> teams = seasonManagerService.findTeamsByGroup(achtelfinale);
        assertThat(teams.size()).isEqualTo(16);
    }

    @Test
    public void testFindRoundWm2006() {
        Season wm2006 = seasonManagerService.findSeasonByName("WM Deutschland",
                "2006");
        //
        // This don´t work:
        //
        //     int finalRound = wm2006.size() - 1;
        //
        // You would get a LazyInitializationException!
        //

        List<GameList> rounds = seasonManagerService.findRounds(wm2006);
        assertThat(rounds.size()).isEqualTo(25);

        GameList spieltag_1 = rounds.get(0);
        assertThat(spieltag_1.getGroup().getGroupType().getName()).isEqualTo(
                "Gruppe A");

        GameList spieltag_2 = rounds.get(1);
        assertThat(spieltag_2.getGroup().getGroupType().getName()).isEqualTo(
                "Gruppe B");

        GameList finale = rounds.get(24);
        assertThat(finale.getGroup().getGroupType().getName()).isEqualTo(
                "Finale");

        Team italien = masterDataManagerService.findTeam("Italien");
        Team frankreich = masterDataManagerService.findTeam("Frankreich");
        Game finalRoundMatch = finale.get(0);

        assertThat(finalRoundMatch.getHomeTeam()).isEqualTo(italien);
        assertThat(finalRoundMatch.getGuestTeam()).isEqualTo(frankreich);
    }

    @Test
    public void testFindMatchWm2006() {
        Season wm2006 = seasonManagerService.findSeasonByName("WM Deutschland",
                "2006");

        List<GameList> rounds = seasonManagerService.findRounds(wm2006);
        assertThat(rounds.size()).isEqualTo(25);

        Team italien = masterDataManagerService.findTeam("Italien");
        Team frankreich = masterDataManagerService.findTeam("Frankreich");

        GameList finale = rounds.get(24);
        Game finalMatch = seasonManagerService.findMatch(finale, italien,
                frankreich);
        assertThat(finalMatch.getResult()).isEqualTo(new GameResult(5, 3));

        Team deutschland = masterDataManagerService.findTeam("Deutschland");
        Team portugal = masterDataManagerService.findTeam("Portugal");

        GameList platz3 = rounds.get(23);
        Game platz3Match = seasonManagerService.findMatch(platz3, deutschland,
                portugal);
        assertThat(platz3Match.getResult()).isEqualTo(new GameResult(3, 1));
    }

    @Test
    public void testFindTippsWm2006() {
        Season wm2006 = seasonManagerService.findSeasonByName("WM Deutschland",
                "2006");

        List<GameList> rounds = seasonManagerService.findRounds(wm2006);
        GameList finale = rounds.get(24);

        List<GameTipp> tipps = tippService.findTippsByMatch(finale.get(0));
        assertThat(tipps.size()).isEqualTo(8);

        String[] nicknames = new String[] { "Roenne", "Jogi", "Goddard",
                "Peter", "mrTipp", "chris", "Frosch", "Hattwig" };

        for (int index = 0; index < nicknames.length; index++) {
            assertThat(tipps.get(index).getUser().getNickName()).isEqualTo(
                    nicknames[index]);
        }
    }

    @Test
    public void testFindTippByFroschWm2006() {
        Season wm2006 = seasonManagerService.findSeasonByName("WM Deutschland",
                "2006");
        User user = masterDataManagerService.findUserByNickname("Frosch");
        List<GameList> rounds = seasonManagerService.findRounds(wm2006);
        GameList finale = rounds.get(24);
        List<GameTipp> finalRoundTipps = tippService.findTippsByRoundAndUser(
                finale, user);

        assertEquals(user, finalRoundTipps.get(0).getUser());
        assertEquals(0, finalRoundTipps.get(0).getTipp().getHomeGoals());
        assertEquals(2, finalRoundTipps.get(0).getTipp().getGuestGoals());
    }

    @Test
    public void testCalculateUserRankingWm2006() {
        Season wm2006 = seasonManagerService.findSeasonByName("WM Deutschland",
                "2006");

        List<UserResult> userResults = seasonManagerService
                .calculateUserRanking(wm2006);
        validateUserResult(userResults, 0, "Frosch", 483, 11, 34, 1);
        validateUserResult(userResults, 1, "Jogi", 477, 9, 36, 2);
        validateUserResult(userResults, 2, "Hattwig", 471, 7, 38, 3);
        validateUserResult(userResults, 3, "Roenne", 467, 9, 35, 4);
        validateUserResult(userResults, 4, "Goddard", 415, 5, 35, 5);
        validateUserResult(userResults, 5, "Peter", 408, 6, 33, 6);
        validateUserResult(userResults, 6, "chris", 378, 6, 30, 7);
        validateUserResult(userResults, 7, "Andi", 328, 6, 25, 8);
        validateUserResult(userResults, 8, "Steffen", 325, 5, 26, 9);
        validateUserResult(userResults, 9, "Bernd_das_Brot", 239, 3, 20, 10);
        validateUserResult(userResults, 10, "mrTipp", 222, 4, 17, 11);
    }

    @Test
    public void testCalculateUserRankingBuli2006() {
        Season buli = seasonManagerService.findSeasonByName(
                "Fussball Bundesliga", "2006/2007");

        GameList round = seasonManagerService.findRound(buli, 0);
        Group bundesliga = round.getGroup();
        assertEquals(9, round.getGamesOfGroup(bundesliga).size());

        List<UserResult> userResults = seasonManagerService
                .calculateUserRanking(round);

        validateUserResult(userResults, 0, "Peter", 50, 0, 5, 1);
        validateUserResult(userResults, 1, "Steffen", 46, 2, 2, 2);
        validateUserResult(userResults, 2, "Frosch", 43, 1, 3, 3);
        validateUserResult(userResults, 3, "Roenne", 43, 1, 3, 4);

        List<UserResult> userResultsRange = seasonManagerService
                .calculateUserRanking(buli, 0, 1);

        validateUserResult(userResultsRange, 0, "Peter", 119, 3, 8, 1);
        validateUserResult(userResultsRange, 1, "chris", 106, 2, 8, 2);
        validateUserResult(userResultsRange, 2, "Persistenz", 106, 2, 8, 3);
        validateUserResult(userResultsRange, 3, "Frosch", 103, 1, 9, 4);
    }

    @Test
    public void testCalculateTeamRanking() {
        Season buli = seasonManagerService.findSeasonByName(
                "Fussball Bundesliga", "2006/2007");
        GameList round = seasonManagerService.findRound(buli, 0);
        Group bundesliga = round.getGroup();
        assertEquals(9, round.getGamesOfGroup(bundesliga).size());

        List<Group> groups = seasonManagerService.findGroups(buli);
        List<TeamResult> teamResults = seasonManagerService
                .calculateTeamRanking(buli, groups.get(0));

        validateTeamResult(teamResults, 0, "VfB Stuttgart", 61, 37, 70);
        validateTeamResult(teamResults, 1, "FC Schalke 04", 53, 32, 68);
        validateTeamResult(teamResults, 2, "SV Werder Bremen", 76, 40, 66);
        validateTeamResult(teamResults, 17, "Borussia MGladbach", 23, 44, 26);

        teamResults = seasonManagerService.calculateTeamRanking(buli,
                groups.get(0), 0, 4);
        validateTeamResult(teamResults, 0, "FC Bayern München", 7, 4, 10);
        validateTeamResult(teamResults, 1, "FC Schalke 04", 6, 3, 10);
        validateTeamResult(teamResults, 2, "Hertha BSC Berlin", 8, 2, 9);
        validateTeamResult(teamResults, 3, "1.FC Nürnberg", 6, 2, 9);
        validateTeamResult(teamResults, 4, "Borussia MGladbach", 6, 5, 9);
    }

    @Test
    public void testDatabaseMaintenanceService() {
        Object object = databaseMaintenanceService.executeHql("from Season");
        assertEquals(20, ((List<?>) object).size());

        Object object2 = databaseMaintenanceService.executeHql("select s "
                + "from Season s left join fetch s.groups as group "
                + "where s.name = 'WM Deutschland' and s.year = 2006");
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

    private void validateUserResult(final List<UserResult> userResults,
            final int index, final String nickname, final int points,
            final int win, final int totoWin, int tabPos) {

        assertEquals(nickname, userResults.get(index).getUser().getNickName());
        assertEquals(points, userResults.get(index).getPoints());
        assertEquals(win, userResults.get(index).getUserWin());
        assertEquals(totoWin, userResults.get(index).getUserTotoWin());
        assertEquals(tabPos, userResults.get(index).getTabPos());

        if (log.isDebugEnabled()) {
            log.debug("Name: " + userResults.get(index).getUser().getNickName()
                    + ", PTS: " + userResults.get(index).getPoints());
        }
    }

}
