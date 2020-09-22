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

package de.winkler.betoffice.tippengine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;
import java.util.Date;

import javax.sql.DataSource;

import org.joda.time.DateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.betoffice.database.data.MySqlDatabasedTestSupport.DataLoader;
import de.winkler.betoffice.service.AbstractServiceTest;
import de.winkler.betoffice.service.DatabaseSetUpAndTearDown;
import de.winkler.betoffice.service.MasterDataManagerService;
import de.winkler.betoffice.service.SeasonManagerService;
import de.winkler.betoffice.service.TippService;
import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.GameResult;
import de.winkler.betoffice.storage.Group;
import de.winkler.betoffice.storage.GroupType;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.Team;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.UserResult;
import de.winkler.betoffice.storage.UserSeason;
import de.winkler.betoffice.storage.enums.SeasonType;
import de.winkler.betoffice.storage.enums.TippStatusType;
import de.winkler.betoffice.storage.exception.StorageObjectNotFoundException;
import de.winkler.betoffice.test.DateTimeDummyProducer;
import de.winkler.betoffice.test.DummyTeams;
import de.winkler.betoffice.test.DummyUsers;

/**
 * Testet die Klasse MinTippGenerator.
 *
 * @author Andre Winkler
 */
public class MinTippGeneratorTest extends AbstractServiceTest {

    private static final String JUNIT_TOKEN = "#JUNIT#";

    private static final Date DATE_2002_01_01 = new DateTime(2002, 1, 1, 0, 0).toDate();

    private Game game1;
    private Game game2;
    private Game game3;
    private Game game4;

    private GameList round;

    private GameResult gr10 = new GameResult(1, 0);
    private GameResult gr01 = new GameResult(0, 1);
    private GameResult gr11 = new GameResult(1, 1);
    private GameResult gr21 = new GameResult(2, 1);

    private Season season;

    @Autowired
    private MasterDataManagerService masterDataManagerService;

    @Autowired
    private SeasonManagerService seasonManagerService;

    @Autowired
    private TippService tippService;
    
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

    @Test
    public void testGenerateTipp() throws StorageObjectNotFoundException {
        TippGenerator gen = new MinTippGenerator();

        // User A: 39 Punkte
        // User B: 13 Punkte
        // User C: 0 Punkte
        // User D: 30 Punkte
        game1.setResult(gr10);
        game1.setPlayed(true);

        game2.setResult(gr10);
        game2.setPlayed(true);

        game3.setResult(gr01);
        game3.setPlayed(true);

        game4.setResult(gr10);
        game4.setPlayed(true);

        // userMinTipp bekommt die Tipps von User C.
        User userMinTipp = new User();
        userMinTipp.setNickName("userMinTipp");
        masterDataManagerService.createUser(userMinTipp);
        seasonManagerService.addUser(season, userMinTipp);

        tippService.createOrUpdateTipp(JUNIT_TOKEN, game1, userMinTipp, gr11, TippStatusType.USER);

        assertEquals(gr11, tippService.findTipp(game1, userMinTipp).orElseThrow().getTipp());
        assertEquals(gr11, tippService.findTipp(game2, userMinTipp).orElseThrow().getTipp());
        assertEquals(gr11, tippService.findTipp(game3, userMinTipp).orElseThrow().getTipp());
        assertEquals(gr11, tippService.findTipp(game4, userMinTipp).orElseThrow().getTipp());

        // User A: 26 Punkte
        // User B: 0 Punkte
        // User C: 26 Punkte
        // User D: 20 Punkte
        game1.setResult(gr11);
        game1.setPlayed(true);
        seasonManagerService.updateMatch(game1);

        game2.setResult(gr11);
        game2.setPlayed(true);
        seasonManagerService.updateMatch(game2);

        game3.setResult(gr10);
        game3.setPlayed(true);
        seasonManagerService.updateMatch(game3);

        game4.setResult(gr10);
        game4.setPlayed(true);
        seasonManagerService.updateMatch(game4);

        assertEquals(gr01, tippService.findTipp(game1, userMinTipp).orElseThrow().getTipp());
        assertEquals(gr01, tippService.findTipp(game2, userMinTipp).orElseThrow().getTipp());
        assertEquals(gr01, tippService.findTipp(game3, userMinTipp).orElseThrow().getTipp());
        assertEquals(gr01, tippService.findTipp(game4, userMinTipp).orElseThrow().getTipp());
    }

    private void createData() throws Exception {
        // Insgesamt 4 Tipper. Es sind 4 Spiele zu tippen.
        // User A tippt immer 1:0
        // User B tippt immer 0:1
        // User C tippt immer 1:1
        // User D tippt immer 2:1

        // Saison erzeugen.
        season = new Season();
        season.setMode(SeasonType.CUP);
        season.setName("Weltmeisterschaft");
        season.setYear("2002");

        seasonManagerService.createSeason(season);

        // Gruppe erzeugen.
        GroupType groupType = new GroupType();
        groupType.setName("Test-Gruppe");
        masterDataManagerService.createGroupType(groupType);

        Group group = seasonManagerService.addGroupType(season, groupType);

        DummyTeams testTeams = new DummyTeams();
        Team[] teams = testTeams.teams();

        testTeams.toList().stream().forEach(masterDataManagerService::createTeam);
        group = seasonManagerService.addTeams(season, groupType, testTeams.toList());

        // Spieltag erzeugen, Spiel eintragen.
        round = seasonManagerService.addRound(season, DateTimeDummyProducer.DATE_1971_03_24, group.getGroupType());

        // Spiele erzeugen.
        game1 = seasonManagerService.addMatch(round,
                DateTimeDummyProducer.DATE_2002_01_01,
                group,
                teams[DummyTeams.BOCHUM],
                teams[DummyTeams.BVB]);

        game2 = seasonManagerService.addMatch(round,
                DateTimeDummyProducer.DATE_2002_01_01,
                group,
                teams[DummyTeams.FCB],
                teams[DummyTeams.HSV]);

        game3 = seasonManagerService.addMatch(round,
                DateTimeDummyProducer.DATE_2002_01_01,
                group,
                teams[DummyTeams.BOCHUM],
                teams[DummyTeams.BVB]);

        game4 = seasonManagerService.addMatch(round,
                DateTimeDummyProducer.DATE_2002_01_01,
                group,
                teams[DummyTeams.BOCHUM],
                teams[DummyTeams.BVB]);        

        User userMinTipp = new User();
        userMinTipp.setNickName("User MinTipp");
        masterDataManagerService.createUser(userMinTipp);
        
        DummyUsers testUsers = new DummyUsers();
        User[] users = testUsers.users();
        testUsers.toList().stream().forEach(masterDataManagerService::createUser);
        seasonManagerService.addUsers(season, testUsers.toList());
        
        UserResult.nEqualValue = 10;
        UserResult.nTotoValue = 20;
        UserResult.nZeroValue = 30;

        // Tipps erzeugen und eintragen.

        // Spiel 1
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game1, users[DummyUsers.FROSCH], gr10, TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game1, users[DummyUsers.HATTWIG], gr01, TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game1, users[DummyUsers.MRTIPP], gr11, TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game1, users[DummyUsers.PETER], gr21, TippStatusType.USER);

        // Spiel 2
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game2, users[DummyUsers.FROSCH], gr10, TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game2, users[DummyUsers.HATTWIG], gr01, TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game2, users[DummyUsers.MRTIPP], gr11, TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game2, users[DummyUsers.PETER], gr21, TippStatusType.USER);

        // Spiel 3
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game3, users[DummyUsers.FROSCH], gr10, TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game3, users[DummyUsers.HATTWIG], gr01, TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game3, users[DummyUsers.MRTIPP], gr11, TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game3, users[DummyUsers.PETER], gr21, TippStatusType.USER);

        // Spiel 4
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game4, users[DummyUsers.FROSCH], gr10, TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game4, users[DummyUsers.HATTWIG], gr01, TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game4, users[DummyUsers.MRTIPP], gr11, TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game4, users[DummyUsers.PETER], gr21, TippStatusType.USER);
    }

}
