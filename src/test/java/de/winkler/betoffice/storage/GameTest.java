/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2016 by Andre Winkler. All rights reserved.
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Before;
import org.junit.Test;

import de.winkler.betoffice.storage.enums.SeasonType;
import de.winkler.betoffice.storage.enums.TippStatusType;
import de.winkler.betoffice.storage.exception.StorageObjectExistsException;
import de.winkler.betoffice.storage.exception.StorageObjectNotValidException;
import de.winkler.betoffice.test.DateTimeDummyProducer;

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

        ZonedDateTime expectedGameDateTime1 = ZonedDateTime.of(
                LocalDateTime.of(LocalDate.of(2002, 1, 1), LocalTime.of(0, 0)), ZoneId.of("Europe/Paris"));
        
        ZonedDateTime expectedGameDateTime2 = ZonedDateTime.parse("2002-01-01T00:00:00+01:00[Europe/Paris]");
        ZonedDateTime expectedGameDateTime3 = ZonedDateTime.parse("2002-01-01T00:00:00+02:00[Europe/Paris]");
        
        assertThat(expectedGameDateTime1).isEqualTo(expectedGameDateTime2);
        assertThat(expectedGameDateTime1).isEqualTo(expectedGameDateTime3);
        
        assertThat(game1.getDateTime()).isEqualTo(expectedGameDateTime1);

        try {
            game1.addTipp(JUNIT_TOKEN, null, null, null);
            fail("NullPointerException erwartet");
        } catch (NullPointerException ex) {
            // Ok
        }
    }

    @Test
    public void testGameContainsTipp() {
        assertTrue("userA besitzt Tipp.", game1.containsTipp(userA));
        assertTrue("userB besitzt Tipp.", game1.containsTipp(userB));
        assertTrue("userC besitzt Tipp.", game1.containsTipp(userC));
        assertFalse("userD besitzt keinen Tipp.", game1.containsTipp(userD));
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

        try {
            game1.addTipp(tipp1);
            fail("StorageObjectExistsException erwartet.");
        } catch (StorageObjectExistsException e) {
            // Ok
        } catch (StorageObjectNotValidException e) {
            fail("StorageObjectNotValidException nicht erwartet.");
        }

        try {
            game1.addTipp(tipp4);
            fail("StorageObjectNotValidException erwartet.");
        } catch (StorageObjectExistsException e) {
            // Ok
        } catch (StorageObjectNotValidException e) {
            fail("StorageObjectNotValidException nicht erwartet.");
        }

        try {
            game1.addTipp(tipp2);
            fail("StorageObjectExistsException erwartet.");
        } catch (StorageObjectExistsException e) {
            // Ok
        } catch (StorageObjectNotValidException e) {
            fail("StorageObjectNotValidException nicht erwartet.");
        }

        try {
            tipp3.setGame(game1);
            game1.addTipp(tipp3);
        } catch (StorageObjectExistsException e) {
            fail("StorageObjectExistsException nicht erwartet.");
        } catch (StorageObjectNotValidException e) {
            fail("StorageObjectNotValidException nicht erwartet.");
        }
    }

    @Before
    public void setUp() throws Exception {
        userA = new User();
        userA.setName("User A");
        userA.setNickName("User A");

        userB = new User();
        userB.setName("User B");
        userB.setNickName("User B");

        userC = new User();
        userC.setName("User C");
        userC.setNickName("User C");

        userD = new User();
        userD.setName("User D");
        userD.setNickName("User D");

        homeTeam = new Team();
        homeTeam.setName("RWE");
        guestTeam = new Team();
        guestTeam.setName("S04");

        game1 = new Game();
        game1.setDateTime(DateTimeDummyProducer.DATE_2002_01_01);
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
        round.setDateTime(DateTimeDummyProducer.DATE_1971_03_24);
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
