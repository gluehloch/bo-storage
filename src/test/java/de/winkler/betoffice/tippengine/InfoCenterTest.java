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

package de.winkler.betoffice.tippengine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import de.betoffice.database.data.MySqlDatabasedTestSupport.DataLoader;
import de.winkler.betoffice.service.AbstractServiceTest;
import de.winkler.betoffice.service.DatabaseSetUpAndTearDown;
import de.winkler.betoffice.service.SeasonManagerService;
import de.winkler.betoffice.service.TippService;
import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.GameResult;
import de.winkler.betoffice.storage.GameTipp;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.UserResultOfDay;
import de.winkler.betoffice.storage.enums.TippStatusType;
import de.winkler.betoffice.test.DummyUsers;
import de.winkler.betoffice.test.ScenarioBuilder;
import de.winkler.betoffice.util.LoggerFactory;

/**
 * Testet die Klasse InfoCenter.
 * 
 * @author Andre Winkler
 */
public class InfoCenterTest extends AbstractServiceTest {

    private static final String JUNIT_TOKEN = "#JUNIT#";

    private final Logger log = LoggerFactory.make();

    private GameResult gr10 = GameResult.of(1, 0);

    private GameResult gr01 = GameResult.of(0, 1);

    private GameResult gr11 = GameResult.of(1, 1);

    @Autowired
    private TippService tippService;
    
    @Autowired
    private SeasonManagerService seasonManagerService;
    
    @Autowired
    private ScenarioBuilder scene;

    @Autowired
    private InfoCenter infoCenter;
    
    private List<User> users;

    private User frosch;

    private User hattwig;

    private User mrTipp;

    private User peter;

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
    public void testMaxTipp() throws Exception {
        assertThrows(NullPointerException.class, () -> {
            infoCenter.findBestTipp(null, null);
        });

        GameList firstRound = seasonManagerService.findRound(scene.getSeason(), 0).orElseThrow();
        
        // Bei einer Standardeinstellung 13/10/0 ergeben sich folgende Werte:
        Game game = firstRound.get(0);
        GameTipp tipp = tippService.findTipp(game, frosch).orElseThrow();
        assertEquals(tipp.getTipp().getToto(), game.getResult().getToto());

        UserResultOfDay urod = tippService.getUserPoints(firstRound, frosch);

        log.info("Fertig: " + game.isPlayed());
        log.info("User-Result-Of-Day: {}", urod.toString());

        UserResultOfDay points = tippService.getUserPoints(firstRound, frosch);
        assertEquals(1, points.getToto());
        assertEquals(0, points.getWin());
        assertEquals(4, points.getTipps());

        assertEquals(10, tippService.getUserPoints(firstRound, frosch).getPoints());
        assertEquals(13, tippService.getUserPoints(firstRound, hattwig).getPoints());
        assertEquals(23, tippService.getUserPoints(firstRound, mrTipp).getPoints());
        assertEquals(13, tippService.getUserPoints(firstRound, peter).getPoints());

        UserResultOfDay bestTipp = infoCenter.findBestTipp(firstRound, scene.getUsers().toList());
        assertEquals(mrTipp, bestTipp.getUser());
    }

    @Test
    public void testMinTipp() {
        assertThrows(NullPointerException.class, () -> {
            infoCenter.findBestTipp(null, null);
        });

        // User A: 39 Punkte
        // User B: 13 Punkte
        // User C: 0 Punkte
        // User D: 30 Punkte
        scene.getGame1().setResult(gr10);
        scene.getGame1().setPlayed(true);
        seasonManagerService.updateMatch(scene.getGame1());

        scene.getGame2().setResult(gr10);
        scene.getGame2().setPlayed(true);
        seasonManagerService.updateMatch(scene.getGame2());

        scene.getGame3().setResult(gr01);
        scene.getGame3().setPlayed(true);
        seasonManagerService.updateMatch(scene.getGame3());

        scene.getGame4().setResult(gr10);
        scene.getGame4().setPlayed(true);
        seasonManagerService.updateMatch(scene.getGame4());

        // User C hat keinen Tipp richtig. Alle anderen mind. einen
        // Tipp richtig.
        assertEquals(mrTipp, infoCenter.findWorstTipp(scene.getSeason().getGamesOfDay(0), users).getUser());

        // User A: 26 Punkte
        // User B: 0 Punkte
        // User C: 26 Punkte
        // User D: 20 Punkte
        scene.getGame1().setResult(gr11);
        scene.getGame1().setPlayed(true);
        seasonManagerService.updateMatch(scene.getGame1());

        scene.getGame2().setResult(gr11);
        scene.getGame2().setPlayed(true);
        seasonManagerService.updateMatch(scene.getGame2());

        scene.getGame3().setResult(gr10);
        scene.getGame3().setPlayed(true);
        seasonManagerService.updateMatch(scene.getGame3());

        scene.getGame4().setResult(gr10);
        scene.getGame4().setPlayed(true);
        seasonManagerService.updateMatch(scene.getGame4());

        // User C hat keinen Tipp richtig. Alle anderen mind. einen
        // einen Tipp richtig.
        assertEquals(hattwig, infoCenter.findWorstTipp(scene.getSeason().getGamesOfDay(0), users).getUser());
    }

    @Test
    public void testMediumTipp() {
        assertThrows(NullPointerException.class, () -> {
            infoCenter.findMediumTipp(null, null);
        });

        GameResult gr = new GameResult(1, 0);
        assertEquals(infoCenter.findMediumTipp(scene.getGame1(), scene.getUsers().toList()), gr);

        Game ohneTipp = new Game();
        assertTrue(infoCenter.findMediumTipp(ohneTipp, scene.getUsers().toList()) == null);

        Game nurAutoTipp = new Game();

        tippService.createOrUpdateTipp(JUNIT_TOKEN, nurAutoTipp, frosch, new GameResult(1, 0), TippStatusType.AUTO);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, nurAutoTipp, hattwig, new GameResult(1, 0), TippStatusType.AUTO);

        assertTrue(infoCenter.findMediumTipp(nurAutoTipp, scene.getUsers().toList()) == null);

        Game g = new Game();

        tippService.createOrUpdateTipp(JUNIT_TOKEN, g, frosch, new GameResult(1, 0), TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, g, hattwig, new GameResult(1, 0), TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, g, mrTipp, new GameResult(1, 0), TippStatusType.USER);

        assertEquals(infoCenter.findMediumTipp(g, scene.getUsers().toList()), new GameResult(1, 0));

        tippService.createOrUpdateTipp(JUNIT_TOKEN, g, frosch, new GameResult(0, 1), TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, g, hattwig, new GameResult(0, 1), TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, g, mrTipp, new GameResult(0, 1), TippStatusType.USER);        

        assertEquals(infoCenter.findMediumTipp(g, scene.getUsers().toList()), new GameResult(0, 1));

        tippService.createOrUpdateTipp(JUNIT_TOKEN, g, frosch, new GameResult(1, 1), TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, g, hattwig, new GameResult(1, 1), TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, g, mrTipp, new GameResult(1, 1), TippStatusType.USER);

        assertEquals(infoCenter.findMediumTipp(g, scene.getUsers().toList()), new GameResult(1, 1));

        tippService.createOrUpdateTipp(JUNIT_TOKEN, g, frosch, new GameResult(2, 1), TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, g, hattwig, new GameResult(1, 3), TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, g, mrTipp, new GameResult(2, 1), TippStatusType.USER);                

        assertEquals(infoCenter.findMediumTipp(g, scene.getUsers().toList()), new GameResult(1, 1));

        tippService.createOrUpdateTipp(JUNIT_TOKEN, g, frosch, new GameResult(2, 1), TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, g, hattwig, new GameResult(1, 4), TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, g, mrTipp, new GameResult(2, 1), TippStatusType.USER);        

        assertEquals(infoCenter.findMediumTipp(g, scene.getUsers().toList()), new GameResult(1, 2));

        tippService.createOrUpdateTipp(JUNIT_TOKEN, g, frosch, new GameResult(0, 1), TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, g, hattwig, new GameResult(4, 3), TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, g, mrTipp, new GameResult(2, 2), TippStatusType.USER);        

        assertEquals(infoCenter.findMediumTipp(g, scene.getUsers().toList()), new GameResult(2, 2));
    }

    private void createData() throws Exception {
        scene.initialize();
        
        frosch = scene.getUsers().users()[DummyUsers.FROSCH];
        hattwig = scene.getUsers().users()[DummyUsers.HATTWIG];
        mrTipp = scene.getUsers().users()[DummyUsers.MRTIPP];
        peter = scene.getUsers().users()[DummyUsers.PETER];
        users = scene.getUsers().toList();
    }

}
