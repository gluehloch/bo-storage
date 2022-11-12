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

package de.winkler.betoffice.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.betoffice.database.data.DatabaseTestData.DataLoader;
import de.winkler.betoffice.storage.Team;
import de.winkler.betoffice.storage.TeamAlias;

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
        List<TeamAlias> teamAliasNames = null;
        Optional<Team> koeln = masterDataManagerService.findTeam("1.FC Köln");
        assertThat(koeln.isPresent()).isTrue();

        TeamAlias teamAlias = masterDataManagerService
                .createTeamAlias(koeln.get(), "Karnevalsverein");
        teamAliasNames = masterDataManagerService.findAllTeamAlias(koeln.get());
        assertThat(teamAliasNames.size()).isEqualTo(4);

        masterDataManagerService.deleteTeamAlias(teamAlias);
        teamAliasNames = masterDataManagerService.findAllTeamAlias(koeln.get());
        assertThat(teamAliasNames.size()).isEqualTo(3);
    }

    @Test
    public void testUpdateTeamAliasName() {
        List<TeamAlias> teamAliasNames = null;
        Optional<Team> koeln = masterDataManagerService.findTeam("1.FC Köln");
        assertThat(koeln.isPresent()).isTrue();

        TeamAlias teamAlias = masterDataManagerService
                .createTeamAlias(koeln.get(), "Karnevalsverein");
        teamAliasNames = masterDataManagerService.findAllTeamAlias(koeln.get());
        assertThat(teamAliasNames.size()).isEqualTo(4);

        teamAlias.setAliasName("Fortuna Köln");
        masterDataManagerService.updateTeamAlias(teamAlias);
        Optional<Team> koeln2 = masterDataManagerService
                .findTeamByAlias("Fortuna Köln");
        assertThat(koeln2.get().getName()).isEqualTo(koeln.get().getName());
    }

}
