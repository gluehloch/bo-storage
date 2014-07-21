/*
 * $Id: InfoCenterTest.java 3782 2013-07-27 08:44:32Z andrewinkler $
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

package de.winkler.betoffice.tippengine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;

import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameResult;
import de.winkler.betoffice.storage.GameTipp;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.UserResultOfDay;
import de.winkler.betoffice.storage.enums.TippStatusType;
import de.winkler.betoffice.test.DummyScenario;
import de.winkler.betoffice.test.DummyUsers;

/**
 * Testet die Klasse InfoCenter.
 * 
 * @author $Author: andrewinkler $
 * @version $Revision: 3782 $ $Date: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
public class InfoCenterTest {

    /** Der private Logger der Klasse. */
    private static Log log = LogFactory.getLog(InfoCenterTest.class);

    private GameResult gr10 = new GameResult(1, 0);

    private GameResult gr01 = new GameResult(0, 1);

    private GameResult gr11 = new GameResult(1, 1);

    private DummyScenario scene;

    private List<User> users;

    private User frosch;

    private User hattwig;

    private User mrTipp;

    private User peter;

    @Test
    public void testMaxTipp() throws Exception {
        try {
            InfoCenter.getMaxTipp(null, null);
            fail("IllegalArgumentException erwartet.");
        } catch (IllegalArgumentException e) {
            // in Ordnung
        }

        // Bei einer Standardeinstellung 13/10/0 ergeben sich
        // folgende Werte:
        Game game = scene.getSeason().getGamesOfDay(0).unmodifiableList()
            .get(0);
        GameTipp tipp = game.getGameTipp(frosch);
        assertEquals(tipp.getTipp().getToto(), game.getResult().getToto());

        UserResultOfDay urod = scene.getSeason().getGamesOfDay(0)
            .getUserPoints(frosch);

        log.error(game.getGameTipp(frosch));
        log.error("Fertig: " + game.isPlayed());
        log.error("Tipps: " + urod.getTipps());
        log.error("Toto: " + urod.getToto());
        log.error("Win: " + urod.getWin());
        log.error("Lost: " + urod.getLost());

        log.error("TotoResult: " + tipp.getTotoResult());

        assertEquals(1, scene.getSeason().getGamesOfDay(0)
            .getUserPoints(frosch).getToto());
        assertEquals(0, scene.getSeason().getGamesOfDay(0)
            .getUserPoints(frosch).getWin());
        assertEquals(4, scene.getSeason().getGamesOfDay(0)
            .getUserPoints(frosch).getTipps());

        assertEquals(10, scene.getSeason().getGamesOfDay(0).getUserPoints(
            frosch).getPoints());
        assertEquals(13, scene.getSeason().getGamesOfDay(0).getUserPoints(
            hattwig).getPoints());
        assertEquals(23, scene.getSeason().getGamesOfDay(0).getUserPoints(
            mrTipp).getPoints());
        assertEquals(13, scene.getSeason().getGamesOfDay(0)
            .getUserPoints(peter).getPoints());

        assertEquals(mrTipp, InfoCenter.getMaxTipp(scene.getSeason()
            .getGamesOfDay(0), scene.getUsers().toList()));
    }

    @Test
    public void testMinTipp() {
        try {
            InfoCenter.getMaxTipp(null, null);
            fail("IllegalArgumentException erwartet.");
        } catch (IllegalArgumentException e) {
            // in Ordnung
        }

        // User A: 39 Punkte
        // User B: 13 Punkte
        // User C: 0 Punkte
        // User D: 30 Punkte
        scene.getGame1().setResult(gr10);
        scene.getGame1().setPlayed(true);

        scene.getGame2().setResult(gr10);
        scene.getGame2().setPlayed(true);

        scene.getGame3().setResult(gr01);
        scene.getGame3().setPlayed(true);

        scene.getGame4().setResult(gr10);
        scene.getGame4().setPlayed(true);

        // User C hat keinen Tipp richtig. Alle anderen mind. einen
        // Tipp richtig.
        assertEquals(mrTipp, InfoCenter.getMinTipp(
            scene.getSeason().getGamesOfDay(0), users).getUser());

        // User A: 26 Punkte
        // User B: 0 Punkte
        // User C: 26 Punkte
        // User D: 20 Punkte
        scene.getGame1().setResult(gr11);
        scene.getGame1().setPlayed(true);

        scene.getGame2().setResult(gr11);
        scene.getGame2().setPlayed(true);

        scene.getGame3().setResult(gr10);
        scene.getGame3().setPlayed(true);

        scene.getGame4().setResult(gr10);
        scene.getGame4().setPlayed(true);

        // User C hat keinen Tipp richtig. Alle anderen mind. einen
        // einen Tipp richtig.
        assertEquals(hattwig, InfoCenter.getMinTipp(
            scene.getSeason().getGamesOfDay(0), users).getUser());
    }

    @Test
    public void testMediumTipp() {
        try {
            InfoCenter.getMediumTipp(null);
            fail("IllegalArgumentException erwartet.");
        } catch (IllegalArgumentException e) {
            // in Ordnung
        }

        GameResult gr = new GameResult(1, 0);
        assertEquals(InfoCenter.getMediumTipp(scene.getGame1()), gr);

        Game ohneTipp = new Game();
        assertTrue("Kein Tipp vorhanden.",
            InfoCenter.getMediumTipp(ohneTipp) == null);

        Game nurAutoTipp = new Game();

        nurAutoTipp.addTipp(frosch, new GameResult(1, 0), TippStatusType.AUTO);
        nurAutoTipp.addTipp(hattwig, new GameResult(1, 0), TippStatusType.AUTO);

        assertTrue("Nur AUTO-Tipps vorhanden.", InfoCenter
            .getMediumTipp(nurAutoTipp) == null);

        Game g = new Game();

        GameTipp tipp1 = g.addTipp(frosch, new GameResult(1, 0),
            TippStatusType.USER);
        GameTipp tipp2 = g.addTipp(hattwig, new GameResult(1, 0),
            TippStatusType.USER);
        GameTipp tipp3 = g.addTipp(mrTipp, new GameResult(1, 0),
            TippStatusType.USER);

        assertEquals(InfoCenter.getMediumTipp(g), new GameResult(1, 0));

        tipp1.setTipp(new GameResult(0, 1), TippStatusType.USER);
        tipp2.setTipp(new GameResult(0, 1), TippStatusType.USER);
        tipp3.setTipp(new GameResult(0, 1), TippStatusType.USER);

        assertEquals(InfoCenter.getMediumTipp(g), new GameResult(0, 1));

        tipp1.setTipp(new GameResult(1, 1), TippStatusType.USER);
        tipp2.setTipp(new GameResult(1, 1), TippStatusType.USER);
        tipp3.setTipp(new GameResult(1, 1), TippStatusType.USER);

        assertEquals(InfoCenter.getMediumTipp(g), new GameResult(1, 1));

        tipp1.setTipp(new GameResult(2, 1), TippStatusType.USER);
        tipp2.setTipp(new GameResult(1, 3), TippStatusType.USER);
        tipp3.setTipp(new GameResult(2, 1), TippStatusType.USER);

        assertEquals(InfoCenter.getMediumTipp(g), new GameResult(1, 1));

        tipp1.setTipp(new GameResult(2, 1), TippStatusType.USER);
        tipp2.setTipp(new GameResult(1, 4), TippStatusType.USER);
        tipp3.setTipp(new GameResult(2, 1), TippStatusType.USER);

        assertEquals(InfoCenter.getMediumTipp(g), new GameResult(1, 2));

        tipp1.setTipp(new GameResult(0, 1), TippStatusType.USER);
        tipp2.setTipp(new GameResult(4, 3), TippStatusType.USER);
        tipp3.setTipp(new GameResult(2, 2), TippStatusType.USER);

        assertEquals(InfoCenter.getMediumTipp(g), new GameResult(2, 2));
    }

    @Before
    public void setUp() throws Exception {
        scene = new DummyScenario();

        frosch = scene.getUsers().users()[DummyUsers.FROSCH];
        hattwig = scene.getUsers().users()[DummyUsers.HATTWIG];
        mrTipp = scene.getUsers().users()[DummyUsers.MRTIPP];
        peter = scene.getUsers().users()[DummyUsers.PETER];
        users = scene.getUsers().toList();
    }

}
