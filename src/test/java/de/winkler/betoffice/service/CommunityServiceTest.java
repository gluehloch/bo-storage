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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        User communityManager = new User();
        communityManager.setEmail("email@email.de");
        communityManager.setName("Andre");
        communityManager.setNickName("Frosch");
        communityManager.setPassword("Passwort");

        communityManager = masterDataManagerService
                .createUser(communityManager);

        Community communityPersisted = communityService.create("TDKB",
                "Frosch");
        assertThat(communityPersisted.getCommunityManager())
                .isEqualTo(communityManager);
        assertEquals("TDKB", communityPersisted.getName());
        assertEquals(communityManager.getId(),
                communityPersisted.getCommunityManager().getId());
    }

    @Test
    public void addAndRemoveCommunityMembers() {
        User communityManager = new User();
        communityManager.setEmail("email@email.de");
        communityManager.setName("Andre");
        communityManager.setNickName("Frosch");
        communityManager.setPassword("Passwort");

        communityManager = masterDataManagerService
                .createUser(communityManager);
        Community communityPersisted = communityService.create("TDKB",
                "Frosch");

        User demoUserA = new User();
        demoUserA.setEmail("demoA@email.de");
        demoUserA.setName("DemoA-Name");
        demoUserA.setNickName("DemoA");
        demoUserA.setPassword("DemoA-Password");
        demoUserA = masterDataManagerService.createUser(demoUserA);

        User demoUserB = new User();
        demoUserB.setEmail("demoB@email.de");
        demoUserB.setName("DemoB-Name");
        demoUserB.setNickName("DemoB");
        demoUserB.setPassword("DemoB-Password");
        demoUserB = masterDataManagerService.createUser(demoUserB);

        communityService.addMember(communityPersisted.getName(),
                demoUserA.getNickName());
        communityService.addMember(communityPersisted.getName(),
                demoUserB.getNickName());
    }

}
