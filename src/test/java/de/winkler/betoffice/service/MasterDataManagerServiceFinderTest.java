/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2014 by Andre Winkler. All
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

package de.winkler.betoffice.service;

import static org.fest.assertions.Assertions.assertThat;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.winkler.betoffice.dao.hibernate.AbstractDaoTestSupport;
import de.winkler.betoffice.storage.GroupType;
import de.winkler.betoffice.storage.Team;
import de.winkler.betoffice.storage.TeamAlias;
import de.winkler.betoffice.storage.User;

/**
 * Test for class {@link DefaultMasterDataManagerService}.
 * 
 * @author by Andre Winkler
 */
public class MasterDataManagerServiceFinderTest extends AbstractDaoTestSupport {

    @Autowired
    protected SeasonManagerService seasonManagerService;

    @Autowired
    protected MasterDataManagerService masterDataManagerService;

    @Autowired
    protected DatabaseMaintenanceService databaseMaintenanceService;

    @Before
    public void setUp() throws Exception {
        deleteDatabase();
        prepareDatabase(MasterDataManagerServiceFinderTest.class);
    }

    @After
    public void tearDown() throws SQLException {
        deleteDatabase();
    }

    // ------------------------------------------------------------------------

    @Test
    public void testFindTeamAliasNames() {
        Optional<Team> wolfsburg = masterDataManagerService
                .findTeamByAlias("VfL Wolfsburg");
        assertThat(wolfsburg.isPresent()).isTrue();

        Optional<Team> koeln = masterDataManagerService
                .findTeamByAlias("1.FC Köln");
        assertThat(koeln.isPresent()).isTrue();

        Optional<Team> bayernMuenchen2 = masterDataManagerService
                .findTeam("FC Bayern München");
        Optional<Team> bayernMuenchen = masterDataManagerService
                .findTeamByAlias("Bayern München");

        assertThat(bayernMuenchen.get()).isEqualTo(bayernMuenchen2.get());
    }

    @Test
    public void testFindAliasNamesByTeam() {
        Optional<Team> koeln = masterDataManagerService
                .findTeamByAlias("1.FC Köln");
        assertThat(koeln.isPresent()).isTrue();

        List<TeamAlias> teamAliasNames = masterDataManagerService
                .findAllTeamAlias(koeln.get());
        assertThat(teamAliasNames.size()).isEqualTo(3);

        masterDataManagerService.createTeamAlias(koeln.get(),
                "Karnevalsverein");
        Optional<Team> koeln2 = masterDataManagerService
                .findTeamByAlias("Karnevalsverein");

        assertThat(koeln2.get().getName()).isEqualTo(koeln.get().getName());
    }

    @Test
    public void testFindAllTeams() {
        List<Team> teams = masterDataManagerService.findAllTeams();
        assertThat(teams.size()).isEqualTo(85);
    }

    @Test
    public void testFindAllUsers() {
        List<User> users = masterDataManagerService.findAllUsers();
        assertThat(users.size()).isEqualTo(45);
    }

    @Test
    public void testFindAllGroupTypes() {
        List<GroupType> groupTypes = masterDataManagerService
                .findAllGroupTypes();
        assertThat(groupTypes.size()).isEqualTo(14);
    }

}
