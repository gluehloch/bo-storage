/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2022 by Andre Winkler. All
 * rights reserved.
 * ============================================================================
 * GNU GENERAL PUBLIC LICENSE TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND
 * MODIFICATION
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

package de.winkler.betoffice.storage;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import de.winkler.betoffice.storage.enums.SeasonType;
import de.winkler.betoffice.test.DummyGroups;
import de.winkler.betoffice.test.DummyTeams;

/**
 * Testet die Klasse {@link de.winkler.betoffice.storage.TeamResult}.
 *
 * @author Andre Winkler
 */
class TeamResultTest {

    @Test
    void testTeamResultEquals() {
        DummyGroups groupTypes = new DummyGroups();
        DummyTeams teams = new DummyTeams();

        Season season = new Season(SeasonReference.of("2004/05", "Bundesliga"));
        season.setMode(SeasonType.LEAGUE);

        Group groupA = new Group();
        // groupA.setId(1L);
        groupA.setGroupType(groupTypes.groupTypes()[DummyGroups.BULI_1]);
        season.addGroup(groupA);

        Group groupB = new Group();
        // groupB.setId(2L);
        groupB.setGroupType(groupTypes.groupTypes()[DummyGroups.BULI_2]);
        season.addGroup(groupB);

        groupA.addTeam(teams.teams()[DummyTeams.BOCHUM]);
        groupA.addTeam(teams.teams()[DummyTeams.BVB]);

        groupB.addTeam(teams.teams()[DummyTeams.BOCHUM]);
        groupB.addTeam(teams.teams()[DummyTeams.BVB]);

        TeamResult teamResultA = new TeamResult(season, groupA.getGroupType(),
                teams.teams()[DummyTeams.BOCHUM]);
        TeamResult teamResultB = new TeamResult(season, groupA.getGroupType(),
                teams.teams()[DummyTeams.BVB]);
        TeamResult teamResultC = new TeamResult(season, groupB.getGroupType(),
                teams.teams()[DummyTeams.BOCHUM]);
        TeamResult teamResultD = new TeamResult(season, groupB.getGroupType(),
                teams.teams()[DummyTeams.BVB]);

        assertFalse(teamResultA.equals(teamResultB));
        assertFalse(teamResultA.equals(teamResultC));
        assertFalse(teamResultA.equals(teamResultD));

        assertFalse(teamResultB.equals(teamResultC));
        assertFalse(teamResultB.equals(teamResultD));

        assertFalse(teamResultC.equals(teamResultD));
    }

}
