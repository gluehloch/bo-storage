/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2020 by Andre Winkler. All rights reserved.
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.betoffice.database.data.MySqlDatabasedTestSupport.DataLoader;
import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.GameResult;
import de.winkler.betoffice.storage.GameTipp;
import de.winkler.betoffice.storage.Group;
import de.winkler.betoffice.storage.GroupType;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.Team;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.enums.SeasonType;
import de.winkler.betoffice.storage.enums.TippStatusType;
import de.winkler.betoffice.storage.enums.TotoResult;
import de.winkler.betoffice.test.DateTimeDummyProducer;

/**
 * Testklasse für die Klasse Game. Auf das Testen der einfachen setter-Methoden
 * setHomeTeam(), setGuestTeam(), setGroup() und setGameNumber() wird
 * verzichtet.
 *
 * @author Andre Winkler
 */
public class GameTest extends AbstractServiceTest {

    private static final String JUNIT_TOKEN = "#JUNIT#";

    @Autowired
    private SeasonManagerService seasonManagerService;

    @Autowired
    private TippService tippService;

    @Autowired
    private MasterDataManagerService masterDataManagerService;

    @Autowired
    protected DataSource dataSource;

    private DatabaseSetUpAndTearDown dsuatd;

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
        tippX = tippService.createOrUpdateTipp(JUNIT_TOKEN, game1, userA, gameResult10, TippStatusType.USER);
        assertNotNull(tippX.getGame());
        assertNotNull(tippX.getStatus());
        assertNotNull(tippX.getTipp());
        assertNotNull(tippX.getTotoResult());
        assertNotNull(tippX.getUser());

        // Der Service überschreibt den alten Tipp.
        assertThat(tippA.getId()).isEqualTo(tippX.getId());
        assertTrue(tippA.equals(tippX));
        assertFalse(tippB.equals(tippX));
        assertFalse(tippC.equals(tippX));

        // Legt keinen neuen Tipp an, überschreibt den alten Tipp.
        tippX = tippService.createOrUpdateTipp(JUNIT_TOKEN, game1, userB, gameResult10, TippStatusType.USER);
        assertNotNull(tippX.getGame());
        assertNotNull(tippX.getStatus());
        assertNotNull(tippX.getTipp());
        assertNotNull(tippX.getTotoResult());
        assertNotNull(tippX.getUser());

        // Der Service überschreibt den alten Tipp.
        assertThat(tippB.getId()).isEqualTo(tippX.getId());
        assertTrue(tippB.equals(tippX));
        assertFalse(tippA.equals(tippX));
        assertFalse(tippC.equals(tippX));

        // Legt keinen neuen Tipp an, überschreibt den alten Tipp.
        tippX = tippService.createOrUpdateTipp(JUNIT_TOKEN, game1, userC, gameResult10, TippStatusType.USER);
        assertNotNull(tippX.getGame());
        assertNotNull(tippX.getStatus());
        assertNotNull(tippX.getTipp());
        assertNotNull(tippX.getTotoResult());
        assertNotNull(tippX.getUser());

        // Der Service überschreibt den alten Tipp.
        assertThat(tippC.getId()).isEqualTo(tippX.getId());
        assertTrue(tippC.equals(tippX));
        assertFalse(tippA.equals(tippX));
        assertFalse(tippB.equals(tippX));

        //
        // 01-01-2002 00:00 Uhr Europe/Berlin ==>> 31-12-2001 23:00 Uhr UTC
        //
        ZonedDateTime expectedGameDateTime1 = ZonedDateTime.of(
                LocalDateTime.of(LocalDate.of(2002, 1, 1), LocalTime.of(0, 0)), ZoneId.of("Europe/Berlin"));

        // Offset +1 Stunde zu UTC
        ZonedDateTime expectedGameDateTime2 = ZonedDateTime.parse("2002-01-01T00:00:00+01:00[Europe/Berlin]");
        // Offset +2 Stunden zu UTC (Frage: Wird diese Angabe ignoriert oder
        // tatsaechlich verarbeitet?)
        ZonedDateTime expectedGameDateTime3 = ZonedDateTime.parse("2002-01-01T00:00:00+02:00[Europe/Berlin]");

        assertThat(expectedGameDateTime1).isEqualTo(expectedGameDateTime2);
        assertThat(expectedGameDateTime1).isNotEqualTo(expectedGameDateTime3);
        assertThat(expectedGameDateTime1).isAfter(expectedGameDateTime3);

        assertThat(game1.getDateTime()).isEqualTo(expectedGameDateTime1);

        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> {
                    tippService.createOrUpdateTipp(JUNIT_TOKEN, game1, null, null, null);
                });
    }

    @Test
    public void testGameContainsTipp() {
        assertTrue(tippService.findTipp(game1, userA).isPresent());
        assertTrue(tippService.findTipp(game1, userB).isPresent());
        assertTrue(tippService.findTipp(game1, userC).isPresent());
        assertTrue(tippService.findTipp(game1, userD).isEmpty());
    }

    @Test
    public void testGameAddTipp() {
        GameTipp tipp1 = tippService.createOrUpdateTipp(JUNIT_TOKEN, game1, userA, gameResult10, TippStatusType.USER);
        assertThat(tipp1.getTotoResult()).isEqualTo(TotoResult.EQUAL);
        
        GameTipp tipp2 = tippService.createOrUpdateTipp(JUNIT_TOKEN, game1, userB, gameResult10, TippStatusType.USER);
        assertThat(tipp2.getTotoResult()).isEqualTo(TotoResult.EQUAL);
        
        GameTipp tipp3 = tippService.createOrUpdateTipp(JUNIT_TOKEN, game1, userB, gameResult10, TippStatusType.USER);
        assertThat(tipp3.getTotoResult()).isEqualTo(TotoResult.EQUAL);
        
        GameTipp tipp4 = tippService.createOrUpdateTipp(JUNIT_TOKEN, game1, userD, gameResult01, TippStatusType.USER);
        assertThat(tipp4.getTotoResult()).isEqualTo(TotoResult.ZERO);
    }

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

    private void createData() throws Exception {
        userA = new User();
        userA.setName("User A");
        userA.setNickname("User A");
        masterDataManagerService.createUser(userA);

        userB = new User();
        userB.setName("User B");
        userB.setNickname("User B");
        masterDataManagerService.createUser(userB);

        userC = new User();
        userC.setName("User C");
        userC.setNickname("User C");
        masterDataManagerService.createUser(userC);

        userD = new User();
        userD.setName("User D");
        userD.setNickname("User D");
        masterDataManagerService.createUser(userD);

        homeTeam = new Team();
        homeTeam.setName("RWE");
        masterDataManagerService.createTeam(homeTeam);

        guestTeam = new Team();
        guestTeam.setName("S04");
        masterDataManagerService.createTeam(guestTeam);

        Season season = new Season();
        season.setMode(SeasonType.LEAGUE);
        season.setName("Bundesliga 2");
        season.setYear("1971");

        seasonManagerService.createSeason(season);

        GroupType buli1 = new GroupType();
        buli1.setName("1. Bundesliga");
        masterDataManagerService.createGroupType(buli1);

        season = seasonManagerService.addGroupType(season, buli1);
        group = season.getGroup(buli1);

        GameList round = seasonManagerService.addRound(season, DateTimeDummyProducer.DATE_1971_03_24, buli1);

        game1 = seasonManagerService.addMatch(
                round,
                DateTimeDummyProducer.DATE_2002_01_01,
                group,
                homeTeam,
                guestTeam, 1, 0);

        tippA = tippService.createOrUpdateTipp(JUNIT_TOKEN, game1, userA, gameResult01, TippStatusType.USER);
        tippB = tippService.createOrUpdateTipp(JUNIT_TOKEN, game1, userB, gameResult01, TippStatusType.USER);
        tippC = tippService.createOrUpdateTipp(JUNIT_TOKEN, game1, userC, gameResult01, TippStatusType.USER);
    }

}
