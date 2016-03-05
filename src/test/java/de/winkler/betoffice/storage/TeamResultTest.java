/*
 * $Id: TeamResultTest.java 3782 2013-07-27 08:44:32Z andrewinkler $
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

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import de.winkler.betoffice.storage.enums.SeasonType;
import de.winkler.betoffice.test.DummyGroups;
import de.winkler.betoffice.test.DummyTeams;

/**
 * Testet die Klasse {@link de.winkler.betoffice.storage.TeamResult}.
 *
 * @author $Author: andrewinkler $
 * @version $Revision: 3782 $ $Date: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
public class TeamResultTest {

    @Test
    public void testTeamResultEquals() {
        DummyGroups groupTypes = new DummyGroups();
        DummyTeams teams = new DummyTeams();

        Season season = new Season();
        season.setName("Bundesliga");
        season.setYear("2004/05");
        season.setMode(SeasonType.LEAGUE);

        Group groupA = new Group();
        groupA.setGroupType(groupTypes.groupTypes()[DummyGroups.BULI_1]);
        season.addGroup(groupA);

        Group groupB = new Group();
        groupB.setGroupType(groupTypes.groupTypes()[DummyGroups.BULI_2]);
        season.addGroup(groupB);

        groupA.addTeam(teams.teams()[DummyTeams.BOCHUM]);
        groupA.addTeam(teams.teams()[DummyTeams.BVB]);

        groupB.addTeam(teams.teams()[DummyTeams.BOCHUM]);
        groupB.addTeam(teams.teams()[DummyTeams.BVB]);

        TeamResult teamResultA = new TeamResult(groupA,
                teams.teams()[DummyTeams.BOCHUM]);
        TeamResult teamResultB = new TeamResult(groupA,
                teams.teams()[DummyTeams.BVB]);
        TeamResult teamResultC = new TeamResult(groupB,
                teams.teams()[DummyTeams.BOCHUM]);
        TeamResult teamResultD = new TeamResult(groupB,
                teams.teams()[DummyTeams.BVB]);

        assertFalse(teamResultA.equals(teamResultB));
        assertFalse(teamResultA.equals(teamResultC));
        assertFalse(teamResultA.equals(teamResultD));

        assertFalse(teamResultB.equals(teamResultC));
        assertFalse(teamResultB.equals(teamResultD));

        assertFalse(teamResultC.equals(teamResultD));
    }

}
