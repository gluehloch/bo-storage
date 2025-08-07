/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2018 by Andre Winkler. All
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

package de.betoffice.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.betoffice.database.data.DatabaseTestData.DataLoader;
import de.betoffice.storage.team.Team;
import de.betoffice.storage.team.TeamAlias;

/**
 * Test of class {@link DefaultMasterDataManagerService}.
 *
 * @author by Andre Winkler
 */
public class MasterDataManagerServiceUpdateTest extends AbstractServiceTest {

    @Autowired
    protected DataSource dataSource;

    @Autowired
    protected SeasonManagerService seasonManagerService;

    @Autowired
    protected MasterDataManagerService masterDataManagerService;

    @Autowired
    protected DatabaseMaintenanceService databaseMaintenanceService;

    private DatabaseSetUpAndTearDown dsuatd;

    @BeforeEach
    public void setUp() throws Exception {
        dsuatd = new DatabaseSetUpAndTearDown(dataSource);
        dsuatd.setUp(DataLoader.CORE);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        dsuatd.tearDown();
    }

    // ------------------------------------------------------------------------

    @Test
    public void testDeleteTeamAliasName() {
        Team koeln = masterDataManagerService.findTeam("1.FC Köln").orElseThrow();

        TeamAlias teamAlias = masterDataManagerService.createTeamAlias(koeln, "Karnevalsverein");
        List<TeamAlias> teamAliasNames = masterDataManagerService.findAllTeamAlias(koeln);
        assertThat(teamAliasNames.size()).isEqualTo(2);

        masterDataManagerService.deleteTeamAlias(teamAlias);
        teamAliasNames = masterDataManagerService.findAllTeamAlias(koeln);
        assertThat(teamAliasNames.size()).isEqualTo(1);
    }

    @Test
    public void testUpdateTeamAliasName() {
        List<TeamAlias> teamAliasNames = null;
        Team koeln = masterDataManagerService.findTeam("1.FC Köln").orElseThrow();

        TeamAlias teamAlias = masterDataManagerService.createTeamAlias(koeln, "Karnevalsverein");
        teamAliasNames = masterDataManagerService.findAllTeamAlias(koeln);
        assertThat(teamAliasNames.size()).isEqualTo(2);

        teamAlias.setAliasName("Fortuna Köln");
        masterDataManagerService.updateTeamAlias(teamAlias);
        Team koeln2 = masterDataManagerService.findTeamByAlias("Fortuna Köln").orElseThrow();
        assertThat(koeln2.getName()).isEqualTo(koeln.getName());
    }

}
