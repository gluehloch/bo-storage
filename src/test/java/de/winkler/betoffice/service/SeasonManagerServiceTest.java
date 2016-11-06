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

package de.winkler.betoffice.service;

import java.sql.SQLException;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.betoffice.database.data.MySqlDatabasedTestSupport.DataLoader;
import de.winkler.betoffice.storage.GroupType;
import de.winkler.betoffice.storage.Season;

/**
 * Testet das Verhalten von {@link DefaultSeasonManagerService}.
 *
 * @author Andre Winkler
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/betoffice-datasource.xml",
        "/betoffice-persistence.xml", "/test-mysql-piratestest.xml" })
public class SeasonManagerServiceTest {

    @Autowired
    protected DataSource dataSource;

    @Autowired
    protected TippService tippService;

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
        dsuatd.setUp(DataLoader.FULL);
    }

    @After
    public void tearDown() throws SQLException {
        dsuatd.tearDown();
    }

    @Test
    public void testAddGroupToBundesliga2006() {
        Optional<Season> buli = seasonManagerService
                .findSeasonByName("Fussball Bundesliga", "2006/2007");
        Season bundesliga = seasonManagerService
                .findRoundGroupTeamUserRelations(buli.get());
        GroupType liga2 = new GroupType();
        liga2.setName("2. Liga");
        masterDataManagerService.createGroupType(liga2);
        seasonManagerService.addGroupType(bundesliga, liga2);
    }

}
