/*
 * $Id: TeamTest.java 3782 2013-07-27 08:44:32Z andrewinkler $
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

package de.winkler.betoffice.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Testklasse für die Klasse Team.
 *
 * @author  $Author: andrewinkler $
 * @version $Revision: 3782 $
 */
public class TeamTest {

    private Team team1;

    @Test
    public void testSettings() {
        Assert.assertTrue(team1.getName().compareTo("RWE") == 0);
        Assert.assertTrue(team1.getLongName().compareTo("Fussball") == 0);
        Assert.assertTrue(team1.getLogo().compareTo("Logo") == 0);
    }

    @Before
    public void setUp() throws Exception {
        team1 = new Team();
        team1.setName("RWE");
        team1.setLongName("Fussball");
        team1.setLogo("Logo");
    }

}
