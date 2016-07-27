/*
 * $Id: GameResultTest.java 3782 2013-07-27 08:44:32Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2007 by Andre Winkler. All rights reserved.
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

import de.winkler.betoffice.storage.enums.Toto;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

/**
 * Testet die Klasse GameResult.
 *
 * @author  $Author: andrewinkler $
 * @version $Revision: 3782 $
 */
public class GameResultTest {

    private GameResult res00;
    private GameResult res01;
    private GameResult res11;
    private GameResult res10;
    private GameResult res31;
    private GameResult res20;
    private GameResult res02;
    private GameResult res22;
    private GameResult res14;
    private GameResult res11_2;
    private GameResult res10_2;
    private GameResult clone1;
    private GameResult clone2;

    @Test
    public void testCompare() {
        // Testet die Methode GameResult.compare()

        // Inner-Class zum Abarbeiten der Testfälle
        class TestSpec {
            GameResult game1;
            GameResult game2;
            GameResult gameA1;
            GameResult gameA2;
            boolean equal;

            TestSpec(int hg1, int gg1, int hg2, int gg2, boolean b) {
                game1 = new GameResult(hg1, gg1);
                game2 = new GameResult(hg2, gg2);
                equal = b;

                gameA1 = new GameResult(game1);
                gameA2 = new GameResult(game2);
            }

            public boolean executeTest() {
                return (game1.compare(game2) == equal) &&
                    // (game11.compare(game22) == equal) &&
                    (game1.compare(game1) == true) &&
                    (game2.compare(game2) == true) &&
                    // (game11.compare(game11) == true) &&
                    // (game22.compare(game22) == true) &&
                    (game1.compare(gameA1) == true) &&
                    (game2.compare(gameA2) == true) &&
                    (game1.compare(gameA2) == equal) &&
                    (game1.getToto() == gameA1.getToto()) &&
                    (game2.getToto() == gameA2.getToto());
            }

            @Override
            public String toString() {
                StringBuffer buf = new StringBuffer();

                buf.append("Spiel_A: ");
                buf.append(game1.getHomeGoals());
                buf.append(':');
                buf.append(game1.getGuestGoals());
                buf.append(" Spiel_B: ");
                buf.append(game2.getHomeGoals());
                buf.append(':');
                buf.append(game2.getGuestGoals());
                return buf.toString();
            }
        }

        TestSpec[] tests = {
                // Gleiche Spielergebnisse testen.
                new TestSpec(1, 0, 1, 0, true),
                new TestSpec(1, 1, 1, 1, true),
                new TestSpec(2, 1, 2, 1, true),
                new TestSpec(1, 2, 1, 2, true),
                new TestSpec(2, 2, 2, 2, true),
                new TestSpec(4, 5, 4, 5, true),
                // Ungleiche Spielergebnisse testen.
                new TestSpec(1, 1, 0, 0, false),
                new TestSpec(0, 0, 1, 1, false),
                new TestSpec(0, 0, 1, 2, false),
                new TestSpec(1, 1, 1, 0, false),
                new TestSpec(0, 1, 1, 1, false),
                new TestSpec(3, 1, 1, 0, false),
                new TestSpec(2, 2, 1, 1, false),
                new TestSpec(4, 5, 2, 5, false)
            };

        ArrayList<TestSpec> failedTests = new ArrayList<TestSpec>();

        for (int i = 0; i < tests.length; i++) {
            if (!tests[i].executeTest()) {
                failedTests.add(tests[i]);
            }
            // Optional...
            assertEquals(tests[i].executeTest(), true);
        }

        assertEquals(failedTests.size(), 0);

        // Beide Ergebnisse müssen übereinstimmen --> true
        assertEquals(true, res10.compare(res10_2));
        assertEquals(true, res11.compare(res11_2));
        assertEquals(true, res11.compare(res11));

        // Beide Ergebnisse stimmen nicht überein --> false
        assertEquals(false, res01.compare(res11));
        assertEquals(false, res11.compare(res10));
        assertEquals(false, res31.compare(res10));
        assertEquals(false, res20.compare(res02));
        assertEquals(false, res22.compare(res11));
        assertEquals(false, res22.compare(null));
    }

    @Test
    public void testEquals() {
        // Testet die Methode GameResult.equals()

        // Beide Ergebnisse müssen übereinstimmen --> true
        assertTrue(res10.equals(res10_2));
        assertTrue(res11.equals(res11_2));
        assertTrue(res11.equals(res11));
        assertTrue(res01.equals(res01));

        // Beide Ergebnisse stimmen nicht überein --> false
        assertTrue(!res01.equals(res11));
        assertTrue(!res11.equals(res10));
        assertTrue(!res31.equals(res10));
        assertTrue(!res20.equals(res02));
        assertTrue(!res22.equals(res11));
    }

    @Test
    public void testToto() {
        // Testet den Toto-Wert

        // Heimsiege
        assertEquals(Toto.HOME_WIN, res10.getToto());
        assertEquals(Toto.HOME_WIN, res31.getToto());
        assertEquals(Toto.HOME_WIN, res20.getToto());

        // Auswärtssiege
        assertEquals(Toto.HOME_LOST, res01.getToto());
        assertEquals(Toto.HOME_LOST, res02.getToto());
        assertEquals(Toto.HOME_LOST, res14.getToto());

        // Unentschieden
        assertEquals(Toto.REMIS, res00.getToto());
        assertEquals(Toto.REMIS, res11.getToto());
        assertEquals(Toto.REMIS, res22.getToto());

        // Copy-Konstruktor
        GameResult gr10A = new GameResult(res10);
        GameResult gr01B = new GameResult(res01);
        GameResult gr11C = new GameResult(res11);

        assertEquals(res10.getToto(), gr10A.getToto());
        assertEquals(res01.getToto(), gr01B.getToto());
        assertEquals(res11.getToto(), gr11C.getToto());

        // Liefert die Aufzählung den richtigen Wert?
        assertEquals(res10.getToto().intValue(), 1);
        assertEquals(res01.getToto().intValue(), 2);
        assertEquals(res11.getToto().intValue(), 0);
    }

    @Test
    public void testClone() {
        // Testet die Methode clone()
        assertTrue(clone1.equals(res00));
        assertTrue(clone2.equals(res31));
    }

    @Test
    public void testConstructor() {
        // Testet, ob bei negativen Toren eine Exception
        // geworfen wird.
        boolean ok;

        try {
            new GameResult(-1, 0);
            ok = false;
        } catch (IllegalArgumentException e) {
            ok = true;
        }
        assertTrue(ok);

        try {
            new GameResult(0, -1);
            ok = false;
        } catch (IllegalArgumentException e) {
            ok = true;
        }
        assertTrue(ok);

        try {
            new GameResult(-1, -1);
            ok = false;
        } catch (IllegalArgumentException e) {
            ok = true;
        }
        assertTrue(ok);
    }

    @Before
    public void setUp() throws Exception {
        res00 = new GameResult(0, 0);
        res01 = new GameResult(0, 1);
        res11 = new GameResult(1, 1);
        res10 = new GameResult(1, 0);
        res31 = new GameResult(3, 1);
        res20 = new GameResult(2, 0);
        res02 = new GameResult(0, 2);
        res22 = new GameResult(2, 2);
        res14 = new GameResult(1, 4);
        res11_2 = new GameResult(1, 1);
        res10_2 = new GameResult(1, 0);

        try {
            clone1 = (GameResult) res00.clone();
            clone2 = (GameResult) res31.clone();
        } catch (Exception e) {}
    }

}
