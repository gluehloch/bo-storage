/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2019 by Andre Winkler. All rights reserved.
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

package de.winkler.betoffice.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.winkler.betoffice.storage.enums.SeasonType;
import de.winkler.betoffice.storage.enums.TippStatusType;
import de.winkler.betoffice.storage.exception.StorageObjectExistsException;
import de.winkler.betoffice.storage.exception.StorageObjectNotValidException;

/**
 * Testklasse für die Klasse Game. Auf das Testen der einfachen setter-Methoden
 * setHomeTeam(), setGuestTeam(), setGroup() und setGameNumber() wird
 * verzichtet.
 *
 * @author Andre Winkler
 */
public class GameTest {

    private static final String JUNIT_TOKEN = "#JUNIT#";

    private Team homeTeam;

    private Team guestTeam;

    private Group group;

    private Game game1;

    private GameResult gameResult10 = new GameResult(1, 0);

    private GameResult gameResult01 = new GameResult(0, 1);

    private User userA;

    private User userB;

    private User userC;

    private User userD;

    private GameTipp tippA;

    private GameTipp tippB;

    private GameTipp tippC;

    /**
     * Prüft die Methode addNewTipp(). Drei Tipper geben ihre Tipps ab.
     */
    @Test
    public void testGameAddNewTipp() {
        GameTipp tippX;
        // Legt keinen neuen Tipp an, überschreibt den alten Tipp.
        tippX = game1.addTipp(JUNIT_TOKEN, userA, gameResult10,
                TippStatusType.USER);
        assertNotNull(tippX.getGame());
        assertNotNull(tippX.getStatus());
        assertNotNull(tippX.getTipp());
        assertNotNull(tippX.getTotoResult());
        assertNotNull(tippX.getUser());

        // Der Service überschreibt den alten Tipp.
        assertTrue(tippA == tippX);
        assertTrue(tippA.equals(tippX));
        assertFalse(tippB.equals(tippX));
        assertFalse(tippC.equals(tippX));

        // Legt keinen neuen Tipp an, überschreibt den alten Tipp.
        tippX = game1.addTipp(JUNIT_TOKEN, userB, gameResult10,
                TippStatusType.USER);
        assertNotNull(tippX.getGame());
        assertNotNull(tippX.getStatus());
        assertNotNull(tippX.getTipp());
        assertNotNull(tippX.getTotoResult());
        assertNotNull(tippX.getUser());

        // Der Service überschreibt den alten Tipp.
        assertTrue(tippB == tippX);
        assertTrue(tippB.equals(tippX));
        assertFalse(tippA.equals(tippX));
        assertFalse(tippC.equals(tippX));

        // Legt keinen neuen Tipp an, überschreibt den alten Tipp.
        tippX = game1.addTipp(JUNIT_TOKEN, userC, gameResult10,
                TippStatusType.USER);
        assertNotNull(tippX.getGame());
        assertNotNull(tippX.getStatus());
        assertNotNull(tippX.getTipp());
        assertNotNull(tippX.getTotoResult());
        assertNotNull(tippX.getUser());

        // Der Service überschreibt den alten Tipp.
        assertTrue(tippC == tippX);
        assertTrue(tippC.equals(tippX));
        assertFalse(tippA.equals(tippX));
        assertFalse(tippB.equals(tippX));

        assertEquals(DateTime.parse("1971-03-24T20:00:00").toDate(),
                game1.getDateTime());

        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> {
                    game1.addTipp(JUNIT_TOKEN, null, null, null);
                });
    }

    @Test
    public void testGameContainsTipp() {
        assertTrue(game1.containsTipp(userA));
        assertTrue(game1.containsTipp(userB));
        assertTrue(game1.containsTipp(userC));
        assertFalse(game1.containsTipp(userD));
    }

    @Test
    public void testGameAddTipp() {
        GameTipp tipp1 = new GameTipp();
        tipp1.setUser(userA);
        tipp1.setTipp(gameResult10, TippStatusType.USER);

        GameTipp tipp4 = new GameTipp();
        tipp4.setUser(userA);

        GameTipp tipp2 = new GameTipp();
        tipp2.setUser(userB);
        tipp2.setTipp(gameResult10, TippStatusType.USER);

        GameTipp tipp3 = new GameTipp();
        tipp3.setUser(userD);
        tipp3.setTipp(gameResult10, TippStatusType.USER);

        assertThrows(StorageObjectExistsException.class, () -> {
            game1.addTipp(tipp1);
        });

        assertThrows(StorageObjectExistsException.class, () -> {
            game1.addTipp(tipp4);
        });

        assertThrows(StorageObjectExistsException.class, () -> {
            game1.addTipp(tipp2);
        });

        tipp3.setGame(game1);
        game1.addTipp(tipp3);
    }

    @BeforeEach
    public void setUp() throws Exception {
        userA = new User();
        userA.setName("User A");
        userA.setNickname("User A");

        userB = new User();
        userB.setName("User B");
        userB.setNickname("User B");

        userC = new User();
        userC.setName("User C");
        userC.setNickname("User C");

        userD = new User();
        userD.setName("User D");
        userD.setNickname("User D");

        homeTeam = new Team();
        homeTeam.setName("RWE");
        guestTeam = new Team();
        guestTeam.setName("S04");

        game1 = new Game();
        game1.setDateTime(new DateTime(1971, 3, 24, 20, 0).toDate());
        game1.setHomeTeam(homeTeam);
        game1.setGuestTeam(guestTeam);
        game1.setGroup(group);
        game1.setPlayed(true);
        game1.setResult(new GameResult(1, 0));

        Season season = new Season();
        season.setMode(SeasonType.LEAGUE);
        season.setName("Bundesliga 2");
        season.setYear("1971");

        GroupType buli1 = new GroupType();
        buli1.setName("1. Bundesliga");
        group = new Group();
        group.setGroupType(buli1);
        group.setSeason(season);
        season.addGroup(group);

        GameList round = new GameList();
        round.setDateTime(new DateTime(1971, 3, 24, 0, 0).toDate());
        round.setGroup(group);

        season.addGameList(round);
        round.addGame(game1);

        tippA = game1.addTipp(JUNIT_TOKEN, userA, gameResult01,
                TippStatusType.USER);
        tippB = game1.addTipp(JUNIT_TOKEN, userB, gameResult01,
                TippStatusType.USER);
        tippC = game1.addTipp(JUNIT_TOKEN, userC, gameResult01,
                TippStatusType.USER);
    }

}
