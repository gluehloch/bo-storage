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

package de.betoffice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.betoffice.database.data.DatabaseTestData.DataLoader;
import de.betoffice.storage.community.entity.Community;
import de.betoffice.storage.community.entity.CommunityReference;
import de.betoffice.storage.exception.StorageObjectNotFoundException;
import de.betoffice.storage.season.SeasonType;
import de.betoffice.storage.season.entity.Game;
import de.betoffice.storage.season.entity.GameList;
import de.betoffice.storage.season.entity.GameResult;
import de.betoffice.storage.season.entity.Group;
import de.betoffice.storage.season.entity.GroupType;
import de.betoffice.storage.season.entity.Season;
import de.betoffice.storage.season.entity.SeasonReference;
import de.betoffice.storage.team.entity.Team;
import de.betoffice.storage.tip.GameTipp;
import de.betoffice.storage.tip.TippStatusType;
import de.betoffice.storage.tip.TotoResult;
import de.betoffice.storage.user.UserResult;
import de.betoffice.storage.user.entity.Nickname;
import de.betoffice.storage.user.entity.User;
import de.betoffice.test.DateTimeDummyProducer;

/**
 * Testklasse für die Klasse GameTipp.
 * 
 * @author Andre Winkler
 */
class GameTippTest extends AbstractServiceTest {

    private static final String JUNIT_TOKEN = "#JUNIT#";

    @Autowired
    private SeasonManagerService seasonManagerService;

    @Autowired
    private TippService tippService;

    @Autowired
    private MasterDataManagerService masterDataManagerService;
    
    @Autowired
    private CommunityService communityService;

    @Autowired
    protected DataSource dataSource;

    private DatabaseSetUpAndTearDown dsuatd;

    @BeforeEach
    public void setUp() throws Exception {
        dsuatd = new DatabaseSetUpAndTearDown(dataSource);
        dsuatd.setUp(DataLoader.EMPTY);

        createData();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        dsuatd.tearDown();
    }

    private class TestSpec {
        private final GameResult testResult;
        private final TotoResult expectedToto;
        private final User user;

        public TestSpec(GameResult gameResult, TotoResult totoResult, User user) {
            this.testResult = gameResult;
            this.expectedToto = totoResult;
            this.user = user;
        }

        public boolean executeTest() throws StorageObjectNotFoundException {
            getGame().setResult(testResult);
            getGame().setPlayed(true);
            seasonManagerService.updateMatch(getGame());
            
            TotoResult _testResult = tippService.findTipp(getGame(), user).orElseThrow().getTotoResult();

            assertEquals(expectedToto, _testResult);
            return (expectedToto.equals(_testResult));
        }

        public String toString() {
            StringBuffer buf = new StringBuffer();
            buf.append("Ergebnis: ");
            buf.append(testResult);
            buf.append(" Erwarteter Punkte-Wert: ");
            buf.append(expectedToto);
            buf.append(" Von User: ");
            buf.append(user);
            buf.append(" Getippt: ");

            GameTipp tipp = tippService.findTipp(game, user).orElseThrow();
            buf.append(tipp);

            return buf.toString();
        }
    }

    private final List<User> allUsers = new ArrayList<User>();

    private Game game;
    private GameResult gr10 = new GameResult(1, 0);
    private GameResult gr01 = new GameResult(0, 1);
    private GameResult gr11 = new GameResult(1, 1);
    private GameResult gr21 = new GameResult(2, 1);
    private GameResult gr23 = new GameResult(2, 3);
    private User userA;
    private User userB;
    private User userC;
    private User userD;
    private User userE;
    private Season season;
    private Group group;

    /**
     * Testet den Listener der Klasse GameTipp. Bei Änderungen am Spielergebniss
     * schlägt sich dies auf den Punktestand des Tippers nieder, der für dieses
     * Spiel einen Tipp abgegeben hat.
     * 
     * @throws StorageObjectNotFoundException
     *             Da ging was schief.
     */
    @Test
    public void testGameListener() throws StorageObjectNotFoundException {
        // GameTipp ist ein Listener für die Objekte Game. Ändert sich
        // das Ergebnis eines Spiels, wird in GameTipp automatisch
        // der neue Punktewert des Tippers berechnet.

        // Anmerkung: User_A 1:0, User_B 0:1, User_C 1:1 und User_D 2:1.
        TestSpec[] tests = { new TestSpec(gr10, TotoResult.EQUAL, userA),
                new TestSpec(gr10, TotoResult.ZERO, userB),
                new TestSpec(gr10, TotoResult.ZERO, userC),
                new TestSpec(gr10, TotoResult.TOTO, userD),

                new TestSpec(gr01, TotoResult.ZERO, userA),
                new TestSpec(gr01, TotoResult.EQUAL, userB),
                new TestSpec(gr01, TotoResult.ZERO, userC),
                new TestSpec(gr01, TotoResult.ZERO, userD),

                new TestSpec(gr21, TotoResult.TOTO, userA),
                new TestSpec(gr21, TotoResult.ZERO, userB),
                new TestSpec(gr21, TotoResult.ZERO, userC),
                new TestSpec(gr21, TotoResult.EQUAL, userD),

                new TestSpec(gr11, TotoResult.ZERO, userA),
                new TestSpec(gr11, TotoResult.ZERO, userB),
                new TestSpec(gr11, TotoResult.EQUAL, userC),
                new TestSpec(gr11, TotoResult.ZERO, userD),

                new TestSpec(gr23, TotoResult.ZERO, userA),
                new TestSpec(gr23, TotoResult.TOTO, userB),
                new TestSpec(gr23, TotoResult.ZERO, userC),
                new TestSpec(gr23, TotoResult.ZERO, userD) };

        ArrayList<TestSpec> failedTests = new ArrayList<>();

        for (int i = 0; i < tests.length; i++) {
            if (!tests[i].executeTest()) {
                fail("Test Nr: " + i + "; TestCase: " + tests[i].toString());
                failedTests.add(tests[i]);
            }
            // Optional...
            assertTrue(tests[i].executeTest());
        }
        assertEquals(0, failedTests.size());

        // Bei Eintrag des Ergebnisses 1:0 erhält Spieler A
        // die volle Punktzahl. Der Rest geht leer aus.
        game.setResult(gr10);
        game.setPlayed(true);
        seasonManagerService.updateMatch(game);

        {
            GameTipp gameTippUserA = tippService.findTipp(game, userA).orElseThrow();
            assertThat(gameTippUserA.getTotoResult()).isEqualTo(TotoResult.EQUAL);
            assertThat(gameTippUserA.getPoints()).isEqualTo(UserResult.nEqualValue);

            GameTipp gameTippUserB = tippService.findTipp(game, userB).orElseThrow();
            assertThat(gameTippUserB.getTotoResult()).isEqualTo(TotoResult.ZERO);
            assertThat(gameTippUserB.getPoints()).isEqualTo(UserResult.nZeroValue);

            GameTipp gameTippUserC = tippService.findTipp(game, userC).orElseThrow();
            assertThat(gameTippUserC.getTotoResult()).isEqualTo(TotoResult.ZERO);
            assertThat(gameTippUserC.getPoints()).isEqualTo(UserResult.nZeroValue);
        }

        // Bei Eintrag des Ergebnisses 0:1 erhält Spieler B
        // die volle Punktzahl. Der Rest geht leer aus.
        game.setResult(gr01);
        seasonManagerService.updateMatch(game);

        {
            GameTipp gameTippUserA = tippService.findTipp(game, userA).orElseThrow();
            assertThat(gameTippUserA.getTotoResult()).isEqualTo(TotoResult.ZERO);
            assertThat(gameTippUserA.getPoints()).isEqualTo(UserResult.nZeroValue);

            GameTipp gameTippUserB = tippService.findTipp(game, userB).orElseThrow();
            assertThat(gameTippUserB.getTotoResult()).isEqualTo(TotoResult.EQUAL);
            assertThat(gameTippUserB.getPoints()).isEqualTo(UserResult.nEqualValue);

            GameTipp gameTippUserC = tippService.findTipp(game, userC).orElseThrow();
            assertThat(gameTippUserC.getTotoResult()).isEqualTo(TotoResult.ZERO);
            assertThat(gameTippUserC.getPoints()).isEqualTo(UserResult.nZeroValue);
        }

        // Bei Eintrag des Ergebnisses 1:1 erhält Spieler C
        // die volle Punktzahl. Der Rest geht leer aus.
        game.setResult(gr11);
        seasonManagerService.updateMatch(game);

        {
            GameTipp gameTippUserA = tippService.findTipp(game, userA).orElseThrow();
            assertThat(gameTippUserA.getTotoResult()).isEqualTo(TotoResult.ZERO);
            assertThat(gameTippUserA.getPoints()).isEqualTo(UserResult.nZeroValue);

            GameTipp gameTippUserB = tippService.findTipp(game, userB).orElseThrow();
            assertThat(gameTippUserB.getTotoResult()).isEqualTo(TotoResult.ZERO);
            assertThat(gameTippUserB.getPoints()).isEqualTo(UserResult.nZeroValue);

            GameTipp gameTippUserC = tippService.findTipp(game, userC).orElseThrow();
            assertThat(gameTippUserC.getTotoResult()).isEqualTo(TotoResult.EQUAL);
            assertThat(gameTippUserC.getPoints()).isEqualTo(UserResult.nEqualValue);
            
            GameTipp gameTippUserD = tippService.findTipp(game, userD).orElseThrow();
            assertThat(gameTippUserD.getTotoResult()).isEqualTo(TotoResult.ZERO);
            assertThat(gameTippUserD.getPoints()).isEqualTo(UserResult.nZeroValue);
            assertThat(gameTippUserD.getTipp()).isEqualTo(GameResult.of(2, 1));
        }

        {
            GameTipp tippD = tippService.createOrUpdateTipp(JUNIT_TOKEN, game, userD, gr11, TippStatusType.USER);
            assertThat(tippD.getTotoResult()).isEqualTo(TotoResult.EQUAL);
        }

        // Spiel auf ungültig setzen.
        game.setPlayed(false);
        seasonManagerService.updateMatch(game);

        {
            // Alle Spieler erhalten eine 0-Punkte Tipp: Spiel ist nicht beendet.
            GameTipp gameTippUserA = tippService.findTipp(game, userA).orElseThrow();
            assertThat(gameTippUserA.getTotoResult()).isEqualTo(TotoResult.UNDEFINED);
            assertThat(gameTippUserA.getPoints()).isEqualTo(UserResult.nZeroValue);

            GameTipp gameTippUserB = tippService.findTipp(game, userB).orElseThrow();
            assertThat(gameTippUserB.getTotoResult()).isEqualTo(TotoResult.UNDEFINED);
            assertThat(gameTippUserB.getPoints()).isEqualTo(UserResult.nZeroValue);

            GameTipp gameTippUserC = tippService.findTipp(game, userC).orElseThrow();
            assertThat(gameTippUserC.getTotoResult()).isEqualTo(TotoResult.UNDEFINED);
            assertThat(gameTippUserC.getPoints()).isEqualTo(UserResult.nZeroValue);
            
            GameTipp gameTippUserD = tippService.findTipp(game, userD).orElseThrow();
            assertThat(gameTippUserD.getTotoResult()).isEqualTo(TotoResult.UNDEFINED);
            assertThat(gameTippUserD.getPoints()).isEqualTo(UserResult.nZeroValue);
            
            List<GameTipp> tipp = tippService.findTipps(game.getGameList().getId());
            assertThat(tipp).hasSize(4);
            assertThat(tipp).contains(gameTippUserA, gameTippUserB, gameTippUserC);
        }
    }

    private void createData() throws Exception {
        // Spiel wird erzeugt, 3 Tipper geben einen Tipp ab
        // mit den Tipps User_A 1:0, User_B 0:1 und User_C 1:1.

        // Beteiligte Mannschaften erzeugen.
        Team luebeck = new Team("Vfb Lübeck", "Vfb Lübeck", "luebeck.gif");
        masterDataManagerService.createTeam(luebeck);
        Team rwe = new Team("RWE", "Rot-Weiss-Essen", "rwe.gif");
        masterDataManagerService.createTeam(rwe);

        // Saison erzeugen.
        SeasonReference seasonReference = SeasonReference.of("1999/2000", "Bundesliga");
        season = new Season();
        season.setMode(SeasonType.LEAGUE);
        season.setReference(seasonReference);
        seasonManagerService.createSeason(season);

        // Gruppe erzeugen.
        GroupType buli1 = new GroupType();
        buli1.setName("1. Bundesliga");
        masterDataManagerService.createGroupType(buli1);

        season = seasonManagerService.addGroupType(season, buli1);
        group = season.getGroup(buli1);
        seasonManagerService.addTeam(season, buli1, rwe);
        seasonManagerService.addTeam(season, buli1, luebeck);

        GameList round = seasonManagerService.addRound(season, DateTimeDummyProducer.DATE_1971_03_24, buli1);

        // Spiel erzeugen.
        game = seasonManagerService.addMatch(round, DateTimeDummyProducer.DATE_1971_03_24, group, luebeck, rwe);

        // Neuen Tipp erzeugen lassen...
        userA = new User();
        Nickname communityAdmin = Nickname.of("User A");
        userA.setNickname(communityAdmin);
        communityService.createUser(userA);

        userB = new User();
        userB.setNickname(Nickname.of("User B"));
        communityService.createUser(userB);

        userC = new User();
        userC.setNickname(Nickname.of("User C"));
        communityService.createUser(userC);

        userD = new User();
        userD.setNickname(Nickname.of("User D"));
        communityService.createUser(userD);

        userE = new User();
        userE.setNickname(Nickname.of("User E"));
        communityService.createUser(userE);
        
        CommunityReference communityReference = CommunityReference.of("TDKB BL 1999/2000");
        communityService.create(communityReference, seasonReference, "TDKB Bundesliga 1999/2000", "1999/2000", communityAdmin);

        List<User> users = communityService.findAllUsers();

        Set<Nickname> nicknames = users.stream().map(u -> u.getNickname()).collect(Collectors.toSet());
        Community community = communityService.addMembers(communityReference, nicknames);

        tippService.createOrUpdateTipp(JUNIT_TOKEN, game, userA, gr10, TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game, userB, gr01, TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game, userC, gr11, TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game, userD, gr21, TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game, userD, gr21, TippStatusType.USER);
    }

    /**
     * Liefet eine Spielpaarung.
     * 
     * @return Ein <code>Game</code>.
     */
    public Game getGame() {
        return game;
    }

}
