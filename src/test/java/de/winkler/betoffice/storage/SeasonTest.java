/*
 * $Id: SeasonTest.java 3782 2013-07-27 08:44:32Z andrewinkler $
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.winkler.betoffice.storage.enums.SeasonType;
import de.winkler.betoffice.test.DummyScenario;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;

/**
 * Testklasse für das Storage-Objekt Season.
 *
 * @author $Author: andrewinkler $
 * @version $Revision: 3782 $ $Date: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
public class SeasonTest {

    private Season season;

    private DummyScenario scene;

    @Test
    public void testSeasonProperties() {
        assertTrue(season.getName().compareTo("Bundesliga") == 0);
        assertTrue(season.getYear().compareTo("1998/1999") == 0);
        assertTrue(season.getMode() == SeasonType.CL);
    }

    @Test
    public void testGetGamesOfDay() {
        try {
            season.getGamesOfDay(0);
            fail("IndexOutOfBoundsException erwartet.");
        } catch (IndexOutOfBoundsException ex) {
            // ok
        }

        try {
            season.getGamesOfDay(-1);
            fail("IndexOutOfBoundsException erwartet.");
        } catch (IndexOutOfBoundsException ex) {
            // ok
        }

        try {
            season.getGamesOfDay(season.getGameList().size());
            fail("IndexOutOfBoundsException erwartet.");
        } catch (IndexOutOfBoundsException ex) {
            // ok
        }

        assertTrue(scene.getSeason().getGameList().size() == 1);
    }

    @Test
    public void testTeamSelection() {
        Set<Team> teams = scene.getSeason().getTeams();

        assertNotNull(scene.getRwe());
        assertNotNull(scene.getS04());
        assertNotNull(scene.getBuli_1().getTeams());
        assertNotNull(scene.getBuli_2().getTeams());

        assertTrue(scene.getBuli_1().getTeams().size() > 0);
        assertTrue(scene.getBuli_2().getTeams().size() > 0);

        assertEquals(3, scene.getBuli_2().getTeams().size());
        assertEquals(scene.getTeams().toList().size(), scene.getBuli_1()
                .getTeams().size());

        assertTrue(teams.size() > 0);

        boolean removed = teams.remove(scene.getRwe());
        assertTrue(removed);

        // Objekt RWE sollte nur einmal in der Liste auftauchen!
        removed = teams.remove(scene.getRwe());
        assertFalse(removed);
    }

    @Before
    public void setUp() throws Exception {
        season = new Season();
        season.setYear("1998/1999");
        season.setName("Bundesliga");
        season.setMode(SeasonType.CL);

        scene = new DummyScenario();
    }

}
