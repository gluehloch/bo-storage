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

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.betoffice.database.data.MySqlDatabasedTestSupport.DataLoader;
import de.winkler.betoffice.storage.Team;
import de.winkler.betoffice.storage.TeamAlias;

/**
 * Test of class {@link DefaultMasterDataManagerService}.
 *
 * @author by Andre Winkler
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/betoffice-datasource.xml",
        "/betoffice-persistence.xml", "/test-mysql-piratestest.xml" })
public class MasterDataManagerServiceUpdateTest {

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
    public void testDeleteTeamAliasName() {
        List<TeamAlias> teamAliasNames = null;
        Team koeln = masterDataManagerService.findTeam("1.FC Köln");
        assertThat(koeln).isNotNull();

        TeamAlias teamAlias = masterDataManagerService.createTeamAlias(koeln,
                "Karnevalsverein");
        teamAliasNames = masterDataManagerService.findAllTeamAlias(koeln);
        assertThat(teamAliasNames.size()).isEqualTo(4);

        masterDataManagerService.deleteTeamAlias(teamAlias);
        teamAliasNames = masterDataManagerService.findAllTeamAlias(koeln);
        assertThat(teamAliasNames.size()).isEqualTo(3);
    }

    @Test
    public void testUpdateTeamAliasName() {
        List<TeamAlias> teamAliasNames = null;
        Team koeln = masterDataManagerService.findTeam("1.FC Köln");
        assertThat(koeln).isNotNull();

        TeamAlias teamAlias = masterDataManagerService.createTeamAlias(koeln,
                "Karnevalsverein");
        teamAliasNames = masterDataManagerService.findAllTeamAlias(koeln);
        assertThat(teamAliasNames.size()).isEqualTo(4);

        teamAlias.setAliasName("Fortuna Köln");
        masterDataManagerService.updateTeamAlias(teamAlias);
        Team koeln2 = masterDataManagerService.findTeamByAlias("Fortuna Köln");
        assertThat(koeln2.getName()).isEqualTo(koeln.getName());
    }

}
