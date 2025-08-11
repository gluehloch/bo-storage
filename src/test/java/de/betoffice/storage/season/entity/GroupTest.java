/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2022 by Andre Winkler. All rights reserved.
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

package de.betoffice.storage.season.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import de.betoffice.storage.group.entity.GroupType;
import de.betoffice.storage.season.SeasonType;

/**
 * Testet die Klassen Group und AllGroups.
 *
 * @author Andre Winkler
 */
class GroupTest {

    @Test
    void testAllGroupsContains() throws Exception {
        Season season = new Season(SeasonReference.of("1998/1999", "Bundesliga"));
        season.setMode(SeasonType.CL);

        GroupType groupA = new GroupType();
        groupA.setName("Gruppe A");
        GroupType groupB = new GroupType();
        groupB.setName("Gruppe B");
        GroupType groupC = new GroupType();
        groupC.setName("Gruppe C");

        
        assertThrows(Exception.class, () -> {
            season.addGroup(null);
        });

        Group group = new Group();
        group.setGroupType(groupA);
        season.addGroup(group);
        assertEquals(season, group.getSeason());
    }

}
