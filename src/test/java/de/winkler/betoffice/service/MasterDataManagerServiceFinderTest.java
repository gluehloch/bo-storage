/*
 * $Id: MasterDataManagerServiceFinderTest.java 3782 2013-07-27 08:44:32Z andrewinkler $
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

package de.winkler.betoffice.service;

import static org.fest.assertions.Assertions.assertThat;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.winkler.betoffice.storage.GroupType;
import de.winkler.betoffice.storage.Team;
import de.winkler.betoffice.storage.TeamAlias;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.test.database.MySqlDatabasedTestSupport.DataLoader;

/**
 * Test for class {@link DefaultMasterDataManagerService}.
 * 
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3782 $ $LastChangedDate: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/betoffice-datasource.xml",
        "/betoffice-persistence.xml", "/test-mysql-piratestest.xml" })
public class MasterDataManagerServiceFinderTest {

    @Autowired
    protected DataSource dataSource;

    @Autowired
    protected SeasonManagerService seasonManagerService;

    @Autowired
    protected MasterDataManagerService masterDataManagerService;

    @Autowired
    protected DatabaseMaintenanceService databaseMaintenanceService;

    private DatabaseSetUpAndTearDown dsuatd;

    @Before
    public void setUp() throws Exception {
        dsuatd = new DatabaseSetUpAndTearDown(dataSource);
        dsuatd.setUp(DataLoader.MASTER_DATA);
    }

    @After
    public void tearDown() throws SQLException {
        dsuatd.tearDown();
    }

    // ------------------------------------------------------------------------

    @Test
    public void testFindTeamAliasNames() {
        Team koeln = masterDataManagerService.findTeamByAlias("1.FC Köln");
        assertThat(koeln).isNotNull();

        Team bayernMuenchen = masterDataManagerService
                .findTeamByAlias("Bayern München");
        Team bayernMuenchen2 = masterDataManagerService
                .findTeam("FC Bayern München");

        assertThat(bayernMuenchen).isEqualTo(bayernMuenchen2);
    }

    @Test
    public void testFindAliasNamesByTeam() {
        Team koeln = masterDataManagerService.findTeamByAlias("1.FC Köln");
        assertThat(koeln).isNotNull();

        List<TeamAlias> teamAliasNames = masterDataManagerService
                .findAllTeamAlias(koeln);
        assertThat(teamAliasNames.size()).isEqualTo(3);

        masterDataManagerService.createTeamAlias(koeln, "Karnevalsverein");
        Team koeln2 = masterDataManagerService
                .findTeamByAlias("Karnevalsverein");

        assertThat(koeln2.getName()).isEqualTo(koeln.getName());
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
