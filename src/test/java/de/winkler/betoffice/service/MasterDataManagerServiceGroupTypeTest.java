/*
 * $Id: MasterDataManagerServiceGroupTypeTest.java 3782 2013-07-27 08:44:32Z andrewinkler $
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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.betoffice.database.data.MySqlDatabasedTestSupport.DataLoader;
import de.winkler.betoffice.storage.GroupType;
import de.winkler.betoffice.validation.BetofficeValidationException;

/**
 * Test for class {@link DefaultMasterDataManagerService}.
 * 
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3782 $ $LastChangedDate: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/betoffice-datasource.xml",
        "/betoffice-persistence.xml", "/test-mysql-piratestest.xml" })
public class MasterDataManagerServiceGroupTypeTest {

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
        dsuatd.setUp(DataLoader.EMPTY);
    }

    @After
    public void tearDown() throws SQLException {
        dsuatd.tearDown();
    }

    // ------------------------------------------------------------------------

    @Test
    public void testCreateGroupType() {
        createGroupType("1. Bundesliga");
        createGroupType("2. Bundesliga");

        List<GroupType> groupTypes = masterDataManagerService
                .findAllGroupTypes();

        assertThat(groupTypes.size()).isEqualTo(2);
        assertThat(groupTypes.get(0).getName()).isEqualTo("1. Bundesliga");
        assertThat(groupTypes.get(1).getName()).isEqualTo("2. Bundesliga");
    }

    @Test
    public void testCreateInvalidGroupType() {
        GroupType invalidGroupType = new GroupType();
        try {
            masterDataManagerService.createGroupType(invalidGroupType);
            Assert.fail("Expected an BetofficeValidationException exception.");
        } catch (BetofficeValidationException ex) {
            assertThat(ex.getMessages()).isNotEmpty();
        }
    }

    @Test
    public void testUpdateGroupType() {
        createGroupType("1. Bundesliga");
        createGroupType("2. Bundesliga");
        createGroupType("Regionalliga Nord");
        createGroupType("Regionalliga Süd");

        GroupType regioNord = masterDataManagerService
                .findGroupType("Regionalliga Nord");
        assertThat(regioNord.getName()).isEqualTo("Regionalliga Nord");

        regioNord.setName("3. Bundesliga");
        masterDataManagerService.updateGroupType(regioNord);

        GroupType bundesliga_3 = masterDataManagerService
                .findGroupType("3. Bundesliga");
        assertThat(bundesliga_3.getName()).isEqualTo("3. Bundesliga");
    }

    @Test
    public void testDeleteGroupType() {
        GroupType bundesliga_1 = createGroupType("1. Bundesliga");
        GroupType bundesliga_2 = createGroupType("2. Bundesliga");

        List<GroupType> groupTypes = masterDataManagerService
                .findAllGroupTypes();
        assertThat(groupTypes.size()).isEqualTo(2);

        masterDataManagerService.deleteGroupType(bundesliga_1);

        groupTypes = masterDataManagerService.findAllGroupTypes();
        assertThat(groupTypes.size()).isEqualTo(1);

        masterDataManagerService.deleteGroupType(bundesliga_2);

        groupTypes = masterDataManagerService.findAllGroupTypes();
        assertThat(groupTypes.size()).isEqualTo(0);
    }

    private GroupType createGroupType(final String groupTypeName) {
        GroupType groupType = new GroupType();
        groupType.setName(groupTypeName);
        masterDataManagerService.createGroupType(groupType);
        return groupType;
    }

}
