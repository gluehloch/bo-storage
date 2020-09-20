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

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.winkler.betoffice.storage.enums.SeasonType;
import de.winkler.betoffice.test.ScenarioBuilder;

/**
 * Testklasse für das Storage-Objekt Season.
 *
 * @author Andre Winkler
 */
public class SeasonTest {

    private Season season;

    private ScenarioBuilder scene;

    @Test
    public void testSeasonProperties() {
        assertTrue(season.getName().compareTo("Bundesliga") == 0);
        assertTrue(season.getYear().compareTo("1998/1999") == 0);
        assertTrue(season.getMode() == SeasonType.CL);
    }

    @Test
    public void testGetGamesOfDay() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            season.getGamesOfDay(0);
        });

        assertThrows(IndexOutOfBoundsException.class, () -> {
            season.getGamesOfDay(-1);
        });

        assertThrows(IndexOutOfBoundsException.class, () -> {
            season.getGamesOfDay(season.getGameList().size());
        });

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

    @BeforeEach
    public void setUp() throws Exception {
        season = new Season();
        season.setYear("1998/1999");
        season.setName("Bundesliga");
        season.setMode(SeasonType.CL);

        scene = new ScenarioBuilder();
    }

}
