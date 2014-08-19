/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2014 by Andre Winkler. All
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

package de.winkler.betoffice.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import de.winkler.betoffice.storage.enums.SeasonType;
import de.winkler.betoffice.storage.enums.TippStatusType;
import de.winkler.betoffice.storage.enums.TotoResult;
import de.winkler.betoffice.storage.exception.StorageObjectNotFoundException;
import de.winkler.betoffice.storage.exception.StorageObjectNotValidException;

/**
 * Testklasse für die Klasse GameTipp.
 * 
 * @author Andre Winkler
 */
public class GameTippTest {

    private class TestSpec {
        private final GameResult testResult;
        private final TotoResult expectedToto;
        private final User user;

        /**
         * @param _gr
         *            Das zu setzende Spielergebnis.
         * @param _tr
         *            Das zu erwartende Toto-Ergebnis.
         * @param _us
         *            Das zu erwartende Toto-Egebnis von User sowieso.
         */
        public TestSpec(final GameResult _gr, final TotoResult _tr,
                final User _us) {
            testResult = _gr;
            expectedToto = _tr;
            user = _us;
        }

        public boolean executeTest() throws StorageObjectNotFoundException {
            getGame().setResult(testResult);
            getGame().setPlayed(true);
            TotoResult _testResult = getGame().getGameTipp(user)
                    .getTotoResult();
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
            try {
                buf.append(getGame().getGameTipp(user));
            } catch (StorageObjectNotFoundException ex) {
                throw new RuntimeException(ex);
                // buf.append("Kein Tipp vorhanden.");
            }
            return buf.toString();
        }
    }

    private List<User> allUsers = new ArrayList<User>();

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

    @Test
    public void testGameTippCheckValidity() {
        GameTipp tipp = new GameTipp();
        tipp.setTipp(new GameResult(1, 0), TippStatusType.USER);
        tipp.setUser(new User());

        try {
            tipp.validate();
        } catch (StorageObjectNotValidException e) {
            fail("Tipp war in Ordnung");
        }

        tipp = new GameTipp();
        try {
            tipp.validate();
            fail("Tipp war nicht in Ordnung");
        } catch (StorageObjectNotValidException e) {
            // Ok
        }

        tipp = new GameTipp();
        tipp.setTipp(new GameResult(1, 0), TippStatusType.USER);
        try {
            tipp.validate();
            fail("Tipp war nicht in Ordnung");
        } catch (StorageObjectNotValidException e) {
            // Ok
        }

        tipp = new GameTipp();
        tipp.setUser(new User());
        try {
            tipp.validate();
            fail("Tipp war nicht in Ordnung");
        } catch (StorageObjectNotValidException e) {
            // Ok
        }
    }

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

        ArrayList<TestSpec> failedTests = new ArrayList<TestSpec>();

        for (int i = 0; i < tests.length; i++) {
            if (!tests[i].executeTest()) {
                fail("Test Nr: " + i + "; TestCase: " + tests[i].toString());
                failedTests.add(tests[i]);
            }
            // Optional...
            assertEquals(tests[i].toString(), tests[i].executeTest(), true);
        }
        assertEquals(0, failedTests.size());

        // Bei Eintrag des Ergebnisses 1:0 erhält Spieler A
        // die volle Punktzahl. Der Rest geht leer aus.
        game.setResult(gr10);
        game.setPlayed(true);

        GameTipp tipp = game.getGameTipp(userA);
        assertTrue(TotoResult.EQUAL == tipp.getTotoResult());
        assertTrue(UserResult.nEqualValue == tipp.getPoints());

        tipp = game.getGameTipp(userB);
        assertTrue(TotoResult.ZERO == tipp.getTotoResult());
        assertTrue(UserResult.nZeroValue == tipp.getPoints());

        tipp = game.getGameTipp(userC);
        assertTrue(TotoResult.ZERO == tipp.getTotoResult());
        assertTrue(UserResult.nZeroValue == tipp.getPoints());

        // Bei Eintrag des Ergebnisses 0:1 erhält Spieler B
        // die volle Punktzahl. Der Rest geht leer aus.
        game.setResult(gr01);

        tipp = game.getGameTipp(userA);
        assertTrue(TotoResult.ZERO == tipp.getTotoResult());
        assertTrue(UserResult.nZeroValue == tipp.getPoints());

        tipp = game.getGameTipp(userB);
        assertTrue(TotoResult.EQUAL == tipp.getTotoResult());
        assertTrue(UserResult.nEqualValue == tipp.getPoints());

        tipp = game.getGameTipp(userC);
        assertTrue(TotoResult.ZERO == tipp.getTotoResult());
        assertTrue(UserResult.nZeroValue == tipp.getPoints());

        // Bei Eintrag des Ergebnisses 1:1 erhält Spieler C
        // die volle Punktzahl. Der Rest geht leer aus.
        game.setResult(gr11);

        tipp = game.getGameTipp(userA);
        assertTrue(TotoResult.ZERO == tipp.getTotoResult());
        assertTrue(UserResult.nZeroValue == tipp.getPoints());

        tipp = game.getGameTipp(userB);
        assertTrue(TotoResult.ZERO == tipp.getTotoResult());
        assertTrue(UserResult.nZeroValue == tipp.getPoints());

        tipp = game.getGameTipp(userC);
        assertTrue(TotoResult.EQUAL == tipp.getTotoResult());
        assertTrue(UserResult.nEqualValue == tipp.getPoints());

        GameTipp tippD = game.addTipp(userD, gr11, TippStatusType.USER);
        assertTrue(TotoResult.EQUAL == tippD.getTotoResult());

        // Spiel auf ungültig setzen.
        game.setPlayed(false);

        tipp = game.getGameTipp(userA);
        assertTrue(TotoResult.UNDEFINED == tipp.getTotoResult());

        tipp = game.getGameTipp(userB);
        assertTrue(TotoResult.UNDEFINED == tipp.getTotoResult());

        tipp = game.getGameTipp(userC);
        assertTrue(TotoResult.UNDEFINED == tipp.getTotoResult());
    }

    @Before
    public void setUp() throws Exception {
        // Spiel wird erzeugt, 3 Tipper geben einen Tipp ab
        // mit den Tipps User_A 1:0, User_B 0:1 und User_C 1:1.

        // Saison erzeugen.
        season = new Season();
        season.setMode(SeasonType.LEAGUE);
        season.setName("Bundesliga");
        season.setYear("1999/2000");

        // getConfiguration().getStorageContext().setCurrentSeason(season);

        // Gruppe erzeugen.
        GroupType buli1 = new GroupType();
        buli1.setName("1. Bundesliga");
        group = new Group();
        group.setGroupType(buli1);
        group.setSeason(season);
        season.addGroup(group);

        // Spiel erzeugen.
        game = new Game();
        game.setGroup(group);

        GameList round = new GameList();
        round.setDateTime(new DateTime(1971, 3, 24, 0, 0).toDate());
        round.setGroup(group);
        season.addGameList(round);
        round.addGame(game);

        // Beteiligte Mannschaften erzeugen.
        Team luebeck = new Team("Vfb Lübeck", "Vfb Lübeck", "luebeck.gif");
        Team rwe = new Team("RWE", "Rot-Weiss-Essen", "rwe.gif");

        group.addTeam(luebeck);
        group.addTeam(rwe);

        game.setHomeTeam(luebeck);
        game.setGuestTeam(rwe);

        // Neuen Tipp erzeugen lassen...
        userA = new User();
        userA.setNickName("User A");
        userB = new User();
        userB.setNickName("User B");
        userC = new User();
        userC.setNickName("User C");
        userD = new User();
        userD.setNickName("User D");
        userE = new User();
        userE.setNickName("User E");

        allUsers.clear();
        allUsers.add(userA);
        allUsers.add(userB);
        allUsers.add(userC);
        allUsers.add(userD);
        allUsers.add(userE);

        game.addTipp(userA, gr10, TippStatusType.USER);
        game.addTipp(userB, gr01, TippStatusType.USER);
        game.addTipp(userC, gr11, TippStatusType.USER);
        game.addTipp(userD, gr21, TippStatusType.USER);

        UserResult.nEqualValue = 10;
        UserResult.nTotoValue = 20;
        UserResult.nZeroValue = 30;
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
