/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2018 by Andre Winkler. All
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
import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.betoffice.database.data.MySqlDatabasedTestSupport.DataLoader;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.GameTipp;
import de.winkler.betoffice.storage.Group;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.UserResult;
import de.winkler.betoffice.util.LoggerFactory;

/**
 * Test of the finder methods of {@link SeasonManagerService}.
 *
 * @author by Andre Winkler
 */
public class CalculateUserRankingServiceFinderTest extends AbstractServiceTest {

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
        dsuatd.tearDown();
        dsuatd.setUp(DataLoader.FULL);
    }

    @After
    public void tearDown() throws SQLException {
        dsuatd.tearDown();
    }

    // ------------------------------------------------------------------------

    @Test
    public void testFindTippsWm2006() {
        Optional<Season> wm2006 = seasonManagerService
                .findSeasonByName("WM Deutschland", "2006");

        List<GameList> rounds = seasonManagerService.findRounds(wm2006.get());
        GameList finale = rounds.get(24);

        List<GameTipp> tipps = tippService.findTippsByMatch(finale.get(0));
        assertThat(tipps).hasSize(8);

        String[] nicknames = new String[] { "Roenne", "Jogi", "Goddard",
                "Peter", "mrTipp", "chris", "Frosch", "Hattwig" };

        for (int index = 0; index < nicknames.length; index++) {
            assertThat(tipps.get(index).getUser().getNickName())
                    .isEqualTo(nicknames[index]);
        }
    }

    @Test
    public void testFindTippByFroschWm2006() {
        Optional<Season> wm2006 = seasonManagerService
                .findSeasonByName("WM Deutschland", "2006");
        Optional<User> frosch = masterDataManagerService
                .findUserByNickname("Frosch");
        Optional<User> mrTipp = masterDataManagerService
                .findUserByNickname("mrTipp");
        Optional<User> peter = masterDataManagerService
                .findUserByNickname("Peter");

        List<User> users = seasonManagerService
                .findActivatedUsers(wm2006.get());
        assertEquals(11, users.size());

        assertEquals("Frosch", frosch.get().getNickName());
        assertEquals("mrTipp", mrTipp.get().getNickName());
        assertEquals("Peter", peter.get().getNickName());

        List<GameList> rounds = seasonManagerService.findRounds(wm2006.get());
        GameList finale = rounds.get(24);
        List<GameTipp> finalRoundTipps = tippService
                .findTippsByRoundAndUser(finale, frosch.get());

        assertEquals(frosch.get(), finalRoundTipps.get(0).getUser());
        assertEquals(0, finalRoundTipps.get(0).getTipp().getHomeGoals());
        assertEquals(2, finalRoundTipps.get(0).getTipp().getGuestGoals());

        GameList froschTipps = tippService.findTipp(finale, frosch.get());
        // 7 Tipps von allen Teilnehmern. Nur einer ist nicht 'null'.
        // TODO: Nach der Umstellung auf JPA Annotationen wird nur noch
        // ein Datensatz geliefert: Der fuer Frosch.
        assertEquals(1, froschTipps.get(0).getTipps().size());
        assertEquals(0, froschTipps.get(0).getGameTipp(frosch.get()).getTipp()
                .getHomeGoals());
        assertEquals(2, froschTipps.get(0).getGameTipp(frosch.get()).getTipp()
                .getGuestGoals());

        GameList mrTippTipps = tippService.findTipp(finale, mrTipp.get());
        assertEquals(0, mrTippTipps.get(0).getGameTipp(mrTipp.get()).getTipp()
                .getHomeGoals());
        assertEquals(1, mrTippTipps.get(0).getGameTipp(mrTipp.get()).getTipp()
                .getGuestGoals());

        GameList peterTipps = tippService.findTipp(finale, peter.get());
        assertEquals(1, peterTipps.get(0).getGameTipp(peter.get()).getTipp()
                .getHomeGoals());
        assertEquals(2, peterTipps.get(0).getGameTipp(peter.get()).getTipp()
                .getGuestGoals());
    }

    @Test
    public void testFindTippByXtianWm2006() {
        Optional<Season> wm2006 = seasonManagerService
                .findSeasonByName("WM Deutschland", "2006");
        Optional<User> user = masterDataManagerService
                .findUserByNickname("Xtian");
        List<GameList> rounds = seasonManagerService.findRounds(wm2006.get());
        GameList finale = rounds.get(24);
        List<GameTipp> finalRoundTipps = tippService
                .findTippsByRoundAndUser(finale, user.get());

        assertEquals(finalRoundTipps.size(), 0);

        // assertEquals(user, finalRoundTipps.get(0).getUser());
        // assertEquals(0, finalRoundTipps.get(0).getTipp().getHomeGoals());
        // assertEquals(2, finalRoundTipps.get(0).getTipp().getGuestGoals());
    }

    @Test
    public void testCalculateUserRankingWm2006() {
        Optional<Season> wm2006 = seasonManagerService
                .findSeasonByName("WM Deutschland", "2006");

        List<UserResult> userResults = seasonManagerService
                .calculateUserRanking(wm2006.get());
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
        Optional<Season> buli = seasonManagerService
                .findSeasonByName("Fussball Bundesliga", "2006/2007");

        Optional<GameList> round = seasonManagerService.findRound(buli.get(),
                0);
        Group bundesliga = round.get().getGroup();
        assertEquals(9, round.get().toList(bundesliga).size());

        List<UserResult> userResults = seasonManagerService
                .calculateUserRanking(round.get());

        validateUserResult(userResults, 0, "Peter", 50, 0, 5, 1);
        validateUserResult(userResults, 1, "Steffen", 46, 2, 2, 2);
        validateUserResult(userResults, 2, "Frosch", 43, 1, 3, 3);
        validateUserResult(userResults, 3, "Roenne", 43, 1, 3, 4);

        List<UserResult> userResultsRange = seasonManagerService
                .calculateUserRanking(buli.get(), 0, 1);

        validateUserResult(userResultsRange, 0, "Peter", 119, 3, 8, 1);
        validateUserResult(userResultsRange, 1, "chris", 106, 2, 8, 2);
        validateUserResult(userResultsRange, 2, "Persistenz", 106, 2, 8, 3);
        validateUserResult(userResultsRange, 3, "Frosch", 103, 1, 9, 4);
    }

    private void validateUserResult(final List<UserResult> userResults,
            final int index, final String nickname,
            final int points, final int win, final int totoWin, int tabPos) {

        assertEquals(nickname, userResults.get(index).getUser().getNickName());
        assertEquals(points, userResults.get(index).getPoints());
        assertEquals(win, userResults.get(index).getUserWin());
        assertEquals(totoWin, userResults.get(index).getUserTotoWin());
        assertEquals(tabPos, userResults.get(index).getTabPos());

        if (log.isDebugEnabled()) {
            log.debug("Name: " + userResults.get(index).getUser().getNickName()
                    + ", PTS: "
                    + userResults.get(index).getPoints());
        }
    }

}
