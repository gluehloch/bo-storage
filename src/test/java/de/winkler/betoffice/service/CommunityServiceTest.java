/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2019 by Andre Winkler. All
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

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.betoffice.database.data.MySqlDatabasedTestSupport.DataLoader;
import de.winkler.betoffice.storage.Community;
import de.winkler.betoffice.storage.User;

/**
 * Service test for the community manager.
 * 
 * @author Andre Winkler
 */
public class CommunityServiceTest extends AbstractServiceTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CommunityService communityService;

    @Autowired
    private MasterDataManagerService masterDataManagerService;

    private DatabaseSetUpAndTearDown dsuatd;

    @BeforeEach
    public void setUp() throws Exception {
        dsuatd = new DatabaseSetUpAndTearDown(dataSource);
        dsuatd.tearDown();
        dsuatd.setUp(DataLoader.EMPTY);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        dsuatd.tearDown();
    }

    @Test
    public void createCommunity() {
        User communityAdmin = new User();
        communityAdmin.setEmail("email@email.de");
        communityAdmin.setName("Andre");
        communityAdmin.setNickName("Frosch");
        communityAdmin.setPassword("Passwort");

        masterDataManagerService.createUser(communityAdmin);

        Community community = new Community();
        community.setName("TDKB");
        community.setCommunityManager(communityAdmin);
        community.addCommunityMember(communityAdmin);

        Community persisted = communityService.create("TDKB", "Frosch");
    }

}
