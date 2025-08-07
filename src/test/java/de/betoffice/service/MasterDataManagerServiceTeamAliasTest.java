/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2024 by Andre Winkler. All
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
 *
 */

package de.betoffice.service;

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
import de.betoffice.service.DatabaseMaintenanceService;
import de.betoffice.service.MasterDataManagerService;
import de.betoffice.service.SeasonManagerService;
import de.betoffice.storage.Team;
import de.betoffice.storage.TeamAlias;
import de.betoffice.validation.ValidationException;

/**
 * Test CRUD operations on storage object {@link TeamAlias}.
 * 
 * TODO: It would be interesting to test a more complex scenario. A user with
 * tips and other related informations. Does MySQL allow delete statements here?
 *
 * @author Andre Winkler
 */
public class MasterDataManagerServiceTeamAliasTest extends AbstractServiceTest {

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
    public void testCreateTeamAlias() {
        Team rwe = createTeam("RWE", "Rot-Weiss-Essen");
        Team rwo = createTeam("RWO", "Rot-Weiss-Oberhausen");

        TeamAlias rweAlias = masterDataManagerService.createTeamAlias(rwe, "Rot Weiss Essen");
        TeamAlias rwoAlias = masterDataManagerService.createTeamAlias(rwo, "Rot Weiss Oberhausen");

        assertThat(rweAlias.getAliasName()).isEqualTo("Rot Weiss Essen");
        assertThat(rwoAlias.getAliasName()).isEqualTo("Rot Weiss Oberhausen");

        List<TeamAlias> rweAliasNames = masterDataManagerService.findAllTeamAlias(rwe);
        assertThat(rweAliasNames).hasSize(1);
        assertThat(rweAliasNames.get(0).getAliasName()).isEqualTo("Rot Weiss Essen");

        List<TeamAlias> rwoAliasNames = masterDataManagerService.findAllTeamAlias(rwo);
        assertThat(rwoAliasNames).hasSize(1);
        assertThat(rwoAliasNames.get(0).getAliasName()).isEqualTo("Rot Weiss Oberhausen");

        TeamAlias rweAlias_2 = masterDataManagerService.createTeamAlias(rwe, "Schreck vom Niederrhein");
        assertThat(rweAlias_2.getAliasName()).isEqualTo("Schreck vom Niederrhein");
        List<TeamAlias> rweAliasNames_2 = masterDataManagerService.findAllTeamAlias(rwe);
        assertThat(rweAliasNames_2).hasSize(2);
        assertThat(rweAliasNames_2.get(0).getAliasName()).isEqualTo("Rot Weiss Essen");
        assertThat(rweAliasNames_2.get(1).getAliasName()).isEqualTo("Schreck vom Niederrhein");
    }

    @Test
    public void testCreateInvalidTeamAlias() {
        Team rwe = createTeam("RWE", "Rot-Weiss-Essen");

        ValidationException ex1 = assertThrows(
                ValidationException.class, () -> {
                    masterDataManagerService.createTeamAlias(rwe, "");
                });
        assertThat(ex1.getMessages()).isNotEmpty();

        ValidationException ex2 = assertThrows(
                ValidationException.class, () -> {
                    masterDataManagerService.createTeamAlias(rwe, null);
                });
        assertThat(ex2.getMessages()).isNotEmpty();
    }

    @Test
    public void testUpdateTeamAlias() {
        Team rwe = createTeam("RWE", "Rot-Weiss-Essen");
        Team rwo = createTeam("RWO", "Rot-Weiss-Oberhausen");

        TeamAlias rweAlias = masterDataManagerService.createTeamAlias(rwe, "Rot Weiss Essen");
        TeamAlias rwoAlias = masterDataManagerService.createTeamAlias(rwo, "Rot Weiss Oberhausen");

        rweAlias.setAliasName("A new alias name");
        masterDataManagerService.updateTeamAlias(rweAlias);
        rwoAlias.setAliasName("An other value");
        masterDataManagerService.updateTeamAlias(rwoAlias);

        Optional<Team> rweByAlias = masterDataManagerService.findTeamByAlias("A new alias name");
        assertThat(rweByAlias.get()).isEqualTo(rwe);

        Optional<Team> rwoByAlias = masterDataManagerService.findTeamByAlias("An other value");
        assertThat(rwoByAlias.get()).isEqualTo(rwo);
    }

    @Test
    public void testDeleteTeamAlias() {
        Team rwe = createTeam("RWE", "Rot-Weiss-Essen");

        TeamAlias rweAlias = masterDataManagerService.createTeamAlias(rwe, "Rot Weiss Essen");

        List<TeamAlias> rweAliasNames = masterDataManagerService.findAllTeamAlias(rwe);
        assertThat(rweAliasNames).hasSize(1);

        masterDataManagerService.deleteTeamAlias(rweAlias);
        rweAliasNames = masterDataManagerService.findAllTeamAlias(rwe);
        assertThat(rweAliasNames).hasSize(0);

        Optional<Team> rwePersistent = masterDataManagerService.findTeamByAlias("Rot Weiss Essen");
        assertThat(rwePersistent.isPresent()).isFalse();
    }

    private Team createTeam(final String name, final String longname) {
        Team team = new Team();
        team.setName(name);
        team.setLongName(longname);
        masterDataManagerService.createTeam(team);
        return team;
    }

}
