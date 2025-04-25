/*
 * ============================================================================
 * Project betoffice-storage
<<<<<<< HEAD
 * Copyright (c) 2000-2020 by Andre Winkler. All rights reserved.
=======
 * Copyright (c) 2000-2019 by Andre Winkler. All rights reserved.
>>>>>>> work/community
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import de.winkler.betoffice.storage.enums.TeamType;
import de.winkler.betoffice.validation.ValidationException;

/**
 * Test CRUD operations on storage object {@link Team}.
 * 
 * TODO: It would be interesting to test a more complex scenario. A user with
 * tips and other related informations. Does MySQL allow delete statements here?
 *
 * @author Andre Winkler
 */
public class MasterDataManagerServiceTeamTest extends AbstractServiceTest{

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
        dsuatd.setUp(DataLoader.EMPTY);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        dsuatd.tearDown();
    }

    // ------------------------------------------------------------------------

    @Test
    public void testCreateTeam() {
        createTeam("RWE", "Rot-Weiss-Essen");
        createTeam("RWO", "Rot-Weiss-Oberhausen");

        List<Team> teams = masterDataManagerService.findAllTeams();

        assertThat(teams.size()).isEqualTo(2);
        assertThat(teams.get(0).getName()).isEqualTo("RWE");
        assertThat(teams.get(1).getName()).isEqualTo("RWO");
    }

    @Test
    public void testCreateInvalidTeam() {
        Team invalidTeam = new Team();
        ValidationException  ex = assertThrows(ValidationException.class, () -> {
            masterDataManagerService.createTeam(invalidTeam);
        });
        assertThat(ex.getMessages()).isNotEmpty();
    }

    @Test
    public void testUpdateTeam() {
        Team rwe = createTeam("RWE", "Rot-Weiss-Essen");
        createTeam("RWO", "Rot-Weiss-Oberhausen");

        rwe.setName("RWE_2010");
        masterDataManagerService.updateTeam(rwe);

        Optional<Team> rwe_2010 = masterDataManagerService.findTeam("RWE_2010");
        assertThat(rwe_2010).isNotNull();
        assertThat(rwe_2010.get().getName()).isEqualTo("RWE_2010");
    }

    @Test
    public void testUpdateTeamType() {
        Team rwe = createTeam("RWE", "Rot-Weiss-Essen");
        createTeam("RWO", "Rot-Weiss-Oberhausen");

        rwe.setName("RWE_2010");
        rwe.setTeamType(TeamType.FIFA);
        masterDataManagerService.updateTeam(rwe);

        Optional<Team> rwe_2010 = masterDataManagerService.findTeam("RWE_2010");
        assertThat(rwe_2010).isNotNull();
        assertThat(rwe_2010.get().getName()).isEqualTo("RWE_2010");
        assertThat(rwe_2010.get().getTeamType()).isEqualTo(TeamType.FIFA);
    }

    @Test
    public void testDeleteTeam() {
        Team rwe = createTeam("RWE", "Rot-Weiss-Essen");
        Team rwo = createTeam("RWO", "Rot-Weiss-Oberhausen");

        masterDataManagerService.deleteTeam(rwe);
        List<Team> teams = masterDataManagerService.findAllTeams();

        assertThat(teams.size()).isEqualTo(1);
        assertThat(teams.get(0).getName()).isEqualTo("RWO");
        assertThat(teams.get(0).getTeamType()).isEqualTo(TeamType.DFB);

        masterDataManagerService.deleteTeam(rwo);
        teams = masterDataManagerService.findAllTeams();

        assertThat(teams.size()).isEqualTo(0);
    }

    @Test
    public void testFindTeamByTeamType() {
        Team rwe = createTeam("RWE", "Rot-Weiss-Essen");
        Team rwo = createTeam("RWO", "Rot-Weiss-Oberhausen");

        List<Team> teams = masterDataManagerService.findTeams(TeamType.DFB);
        assertThat(teams).hasSize(2);
        assertThat(teams.get(0)).isEqualTo(rwe);
        assertThat(teams.get(1)).isEqualTo(rwo);
    }

    private Team createTeam(final String name, final String longname) {
        Team team = new Team();
        team.setName(name);
        team.setLongName(longname);
        team.setTeamType(TeamType.DFB);
        masterDataManagerService.createTeam(team);
        return team;
    }

}
