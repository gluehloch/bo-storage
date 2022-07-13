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

package de.winkler.betoffice.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.winkler.betoffice.storage.enums.SeasonType;

/**
 * Testklasse für das Storage-Objekt Season.
 *
 * @author Andre Winkler
 */
class SeasonTest {

    private Season season;

    @Test
    void testSeasonProperties() {
        assertEquals(season.getReference().getName().compareTo("Bundesliga"), 0);
        assertEquals(season.getReference().getYear().compareTo("1998/1999"), 0);
        assertEquals(season.getMode(), SeasonType.CL);
    }

    @Test
    void testGetGamesOfDay() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            season.getGamesOfDay(0);
        });

        assertThrows(IndexOutOfBoundsException.class, () -> {
            season.getGamesOfDay(-1);
        });

        assertThrows(IndexOutOfBoundsException.class, () -> {
            season.getGamesOfDay(season.getGameList().size());
        });

        assertEquals(season.getGameList().size(), 0);
    }

    @BeforeEach
    public void setUp() throws Exception {
        season = new Season(SeasonReference.of("1998/1999", "Bundesliga"));
        season.setMode(SeasonType.CL);
    }

}
