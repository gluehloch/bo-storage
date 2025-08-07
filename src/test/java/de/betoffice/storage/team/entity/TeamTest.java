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

package de.betoffice.storage.team.entity;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Testklasse für die Klasse Team.
 *
 * @author Andre Winkler
 */
public class TeamTest {

    private Team team1;

    @Test
    public void testSettings() {
        assertTrue(team1.getName().compareTo("RWE") == 0);
        assertTrue(team1.getLongName().compareTo("Fussball") == 0);
        assertTrue(team1.getLogo().compareTo("Logo") == 0);
    }

    @BeforeEach
    public void setUp() throws Exception {
        team1 = new Team();
        team1.setName("RWE");
        team1.setLongName("Fussball");
        team1.setLogo("Logo");
    }

}
