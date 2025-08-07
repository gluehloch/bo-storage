/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2016 by Andre Winkler. All
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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;
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

/**
 * Testet die korrekte Verwendung von Unicode Zeichen.
 *
 * @author Andre Winkler
 */
public class UnicodeServiceTest extends AbstractServiceTest {

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

    @Test
    public void testUnicodeServiceTest() {
        Team team = new Team();
        team.setName("Äöüß");
        team.setLongName("Äöüß");
        team.setLogo("Äöüß");
        masterDataManagerService.createTeam(team);

        Optional<Team> team2 = masterDataManagerService.findTeam("Äöüß");
        assertEquals(team, team2.get());
    }

}
