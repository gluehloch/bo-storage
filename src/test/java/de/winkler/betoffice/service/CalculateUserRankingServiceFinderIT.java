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

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.sql.DataSource;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import de.betoffice.database.data.DatabaseTestData.DataLoader;
import de.winkler.betoffice.storage.CommunityReference;
import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.GameTipp;
import de.winkler.betoffice.storage.Group;
import de.winkler.betoffice.storage.Nickname;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.SeasonRange;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.UserResult;
import de.winkler.betoffice.util.LoggerFactory;

/**
 * Test of the finder methods of {@link SeasonManagerService}.
 *
 * @author by Andre Winkler
 */
@Tag("complete-database")
class CalculateUserRankingServiceFinderIT extends AbstractServiceTest {

    private final Logger log = LoggerFactory.make();

    @Autowired
    DataSource dataSource;

    @Autowired
    SeasonManagerService seasonManagerService;

    @Autowired
    CommunityService communityService;

    @Autowired
    CommunityCalculatorService communityCalculatorService;

    @Autowired
    TippService tippService;

    @Autowired
    MasterDataManagerService masterDataManagerService;

    @Autowired
    DatabaseMaintenanceService databaseMaintenanceService;

    private DatabaseSetUpAndTearDown dsuatd;

    @BeforeEach
    public void setUp() throws Exception {
        dsuatd = new DatabaseSetUpAndTearDown(dataSource);
        dsuatd.tearDown();
        dsuatd.setUp(DataLoader.COMPLETE);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        dsuatd.tearDown();
    }

    // ------------------------------------------------------------------------

    @Test
    void testFindTippsWm2006() {
        Season wm2006 = seasonManagerService.findSeasonByName("WM Deutschland", "2006").orElseThrow();
        List<GameList> rounds = seasonManagerService.findRounds(wm2006);

        GameList firstRound = seasonManagerService.findRound(wm2006, 0).orElseThrow();
        assertThat(firstRound.size()).isEqualTo(2);

        Game gameDeutschlandVsCostaRica = firstRound.get(0);
        assertThat(gameDeutschlandVsCostaRica.isKo()).as("KO game").isFalse();

        Game gamePolenVsEcuador = firstRound.get(1);
        assertThat(gamePolenVsEcuador.isKo()).isFalse();
        
        GameList finale = seasonManagerService.findRoundGames(rounds.get(24).getId()).orElseThrow();
        assertThat(finale.size()).isEqualTo(1);
        Game finalGame = finale.get(0);
        assertThat(finalGame.getHomeTeam().getName()).as("Heimmannschaft").isEqualTo("Italien");
        assertThat(finalGame.getGuestTeam().getName()).as("Gastmannschaft").isEqualTo("Frankreich");
        assertThat(finalGame.isKo()).isFalse(); // Damals konnte beoffice noch keine KO Spiele.
        assertThat(finalGame.getResult().getHomeGoals()).isEqualTo(5);
        assertThat(finalGame.getResult().getGuestGoals()).isEqualTo(3);

        List<GameTipp> tipps = tippService.findTipps(finale.get(0));
        assertThat(tipps).hasSize(8);

        String[] nicknames = new String[] { "Roenne", "Jogi", "Goddard",
                "Peter", "mrTipp", "chris", "Frosch", "Hattwig" };

        for (int index = 0; index < nicknames.length; index++) {
            assertThat(tipps.get(index).getUser().getNickname().value())
                    .isEqualTo(nicknames[index]);
        }
        
        // --

        User frosch = communityService.findUser(Nickname.of("Frosch")).orElseThrow();
        User mrTipp = communityService.findUser(Nickname.of("mrTipp")).orElseThrow();
        User peter = communityService.findUser(Nickname.of("Peter")).orElseThrow();

        CommunityReference communityReference = CommunityReference.of("TDKB 2006");

        Set<User> users = communityService.findMembers(communityReference);
        assertEquals(11, users.size());
        assertEquals("Frosch", frosch.getNickname().value());
        assertEquals("mrTipp", mrTipp.getNickname().value());
        assertEquals("Peter", peter.getNickname().value());

        assertThat(rounds.get(24)).isEqualTo(finale);
        List<GameTipp> finalRoundTipps = tippService.findTipps(finale, frosch);

        assertEquals(frosch, finalRoundTipps.get(0).getUser());
        assertEquals(0, finalRoundTipps.get(0).getTipp().getHomeGoals());
        assertEquals(2, finalRoundTipps.get(0).getTipp().getGuestGoals());

        List<GameTipp> froschTipps = tippService.findTipps(finale, frosch);
        // 7 Tipps von allen Teilnehmern. Nur einer ist nicht 'null'.
        // TODO: Nach der Umstellung auf JPA Annotationen wird nur noch
        // ein Datensatz geliefert: Der fuer Frosch.
        assertEquals(1, froschTipps.size());
        assertEquals(0, froschTipps.get(0).getTipp().getHomeGoals());
        assertEquals(2, froschTipps.get(0).getTipp().getGuestGoals());

        List<GameTipp> mrTippTipps = tippService.findTipps(finale, mrTipp);
        assertEquals(0, mrTippTipps.get(0).getTipp().getHomeGoals());
        assertEquals(1, mrTippTipps.get(0).getTipp().getGuestGoals());

        List<GameTipp> peterTipps = tippService.findTipps(finale, peter);
        assertEquals(1, peterTipps.get(0).getTipp().getHomeGoals());
        assertEquals(2, peterTipps.get(0).getTipp().getGuestGoals());
        
        // --

        User user = communityService.findUser(Nickname.of("Xtian")).orElseThrow();
        assertThat(tippService.findTipps(finale, user)).hasSize(0);
    }

    @Test
    public void testCalculateUserRanking() {
        //
        // WM 2006 / Deutschland
        //
        seasonManagerService.findSeasonByName("WM Deutschland", "2006").orElseThrow();

        CommunityReference communityReferenceWm2006 = CommunityReference.of("TDKB 2006");

        List<UserResult> userResultsWm2006 = communityCalculatorService.calculateRanking(communityReferenceWm2006);
        validateUserResult(userResultsWm2006, 0, "Frosch", 483, 11, 34, 1);
        validateUserResult(userResultsWm2006, 1, "Jogi", 477, 9, 36, 2);
        validateUserResult(userResultsWm2006, 2, "Hattwig", 471, 7, 38, 3);
        validateUserResult(userResultsWm2006, 3, "Roenne", 467, 9, 35, 4);
        validateUserResult(userResultsWm2006, 4, "Goddard", 415, 5, 35, 5);
        validateUserResult(userResultsWm2006, 5, "Peter", 408, 6, 33, 6);
        validateUserResult(userResultsWm2006, 6, "chris", 378, 6, 30, 7);
        validateUserResult(userResultsWm2006, 7, "Andi", 328, 6, 25, 8);
        validateUserResult(userResultsWm2006, 8, "Steffen", 325, 5, 26, 9);
        validateUserResult(userResultsWm2006, 9, "Bernd_das_Brot", 239, 3, 20, 10);
        validateUserResult(userResultsWm2006, 10, "mrTipp", 222, 4, 17, 11);

        //
        // Bundesliga 2006 / 2007
        //

        Season bundesliga = seasonManagerService.findSeasonByName("Fussball Bundesliga", "2006/2007").orElseThrow();

        CommunityReference communityReferenceBundesliga2006 = CommunityReference.of("TDKB 2006/2007");
        communityService.find(communityReferenceBundesliga2006).orElseThrow();

        Optional<GameList> round = seasonManagerService.findRound(bundesliga, 0);
        Group bundesligaGroup = round.get().getGroup();
        assertThat(round.get().toList(bundesligaGroup)).hasSize(9);

        List<UserResult> userResultsBundesliga2006 = communityCalculatorService.calculateRanking(communityReferenceBundesliga2006, round.get());

        validateUserResult(userResultsBundesliga2006, 0, "Peter", 50, 0, 5, 1);
        validateUserResult(userResultsBundesliga2006, 1, "Steffen", 46, 2, 2, 2);
        validateUserResult(userResultsBundesliga2006, 2, "Frosch", 43, 1, 3, 3);
        validateUserResult(userResultsBundesliga2006, 3, "Roenne", 43, 1, 3, 4);

        List<UserResult> userResultsRange = communityCalculatorService.calculateRanking(communityReferenceBundesliga2006,
                SeasonRange.of(0, 1));

        validateUserResult(userResultsRange, 0, "Peter", 119, 3, 8, 1);
        validateUserResult(userResultsRange, 1, "chris", 106, 2, 8, 2);
        validateUserResult(userResultsRange, 2, "Persistenz", 106, 2, 8, 3);
        validateUserResult(userResultsRange, 3, "Frosch", 103, 1, 9, 4);
    }

    private void validateUserResult(final List<UserResult> userResults,
            final int index, final String nickname,
            final int points, final int win, final int totoWin, int tabPos) {

        log.debug("Name: "
                + userResults.get(index).getUser().getNickname()
                + ", PTS: "
                + userResults.get(index).getPoints());

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(userResults.get(index).getUser().getNickname().value()).as("Nickname").isEqualTo(nickname);
        softly.assertThat(userResults.get(index).getUserWin()).as("13 points").isEqualTo(win);
        softly.assertThat(userResults.get(index).getUserTotoWin()).as("10 points").isEqualTo(totoWin);
        softly.assertThat(userResults.get(index).getPoints()).as("summary points").isEqualTo(points);
        softly.assertThat(userResults.get(index).getTabPos()).as("tab position").isEqualTo(tabPos);
        softly.assertAll();
    }

}
