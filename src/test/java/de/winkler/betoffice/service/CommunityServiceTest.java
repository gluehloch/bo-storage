/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2020 by Andre Winkler. All
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import de.betoffice.database.data.MySqlDatabasedTestSupport.DataLoader;
import de.winkler.betoffice.storage.Community;
import de.winkler.betoffice.storage.CommunityFilter;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.enums.SeasonType;

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

    @Autowired
    private SeasonManagerService seasonManagerService;
    
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
        Season bundesliga = new Season();
        bundesliga.setName("Bundesliga");
        bundesliga.setYear("2020/2021");
        bundesliga.setMode(SeasonType.LEAGUE);
        seasonManagerService.createSeason(bundesliga);

        User communityManager = new User();
        communityManager.setEmail("email@email.de");
        communityManager.setName("Andre");
        communityManager.setNickname("Frosch");
        communityManager.setPassword("Passwort");

        communityManager = masterDataManagerService.createUser(communityManager);

        Community community = communityService.create(bundesliga, "TDKB", "TDKB_short", "Frosch");
        assertThat(community.getCommunityManager()).isEqualTo(communityManager);
        assertEquals("TDKB", community.getName());
        assertEquals(communityManager.getId(), community.getCommunityManager().getId());
    }

    @Test
    public void addAndRemoveCommunityMembers() {
        Season bundesliga = new Season();
        bundesliga.setName("Bundesliga");
        bundesliga.setYear("2020/2021");
        bundesliga.setMode(SeasonType.LEAGUE);
        seasonManagerService.createSeason(bundesliga);

        User communityManager = new User();
        communityManager.setEmail("email@email.de");
        communityManager.setName("Andre");
        communityManager.setNickname("Frosch");
        communityManager.setPassword("Passwort");

        communityManager = masterDataManagerService.createUser(communityManager);
        Community community = communityService.create(bundesliga, "TDKB", "TDKB_short", "Frosch");

        User demoUserA = new User();
        demoUserA.setEmail("demoA@email.de");
        demoUserA.setName("DemoA-Name");
        demoUserA.setNickname("DemoA");
        demoUserA.setPassword("DemoA-Password");
        demoUserA = masterDataManagerService.createUser(demoUserA);

        User demoUserB = new User();
        demoUserB.setEmail("demoB@email.de");
        demoUserB.setName("DemoB-Name");
        demoUserB.setNickname("DemoB");
        demoUserB.setPassword("DemoB-Password");
        demoUserB = masterDataManagerService.createUser(demoUserB);

        communityService.addMember(community.getName(), demoUserA.getNickname());
        communityService.addMember(community.getName(), demoUserB.getNickname());

        Community cm = communityService.findCommunityMembers(community.getName());
        assertThat(cm.getUsers()).hasSize(2);
    }

    @Test
    public void filterCommunities() {
        Season bundesliga = new Season();
        bundesliga.setName("Bundesliga");
        bundesliga.setYear("2020/2021");
        bundesliga.setMode(SeasonType.LEAGUE);
        seasonManagerService.createSeason(bundesliga);

        User communityManager = new User();
        communityManager.setEmail("email@email.de");
        communityManager.setName("Andre");
        communityManager.setNickname("Frosch");
        communityManager.setPassword("Passwort");

        communityManager = masterDataManagerService.createUser(communityManager);

        communityService.create(bundesliga, "CM_A", "CM_A_short", "Frosch");
        communityService.create(bundesliga, "CM_B", "CM_B_short", "Frosch");
        communityService.create(bundesliga, "CM_C", "CM_C_short", "Frosch");
        communityService.create(bundesliga, "CM_D", "CM_D_short", "Frosch");
        communityService.create(bundesliga, "CM_E", "CM_E_short", "Frosch");
        
        CommunityFilter communityFilter = new CommunityFilter();
        communityFilter.setShortName("CM");
        
        Page<Community> list = communityService.findCommunities(communityFilter, PageRequest.of(0, 10));
        assertThat(list).hasSize(5);
        
        communityService.create(bundesliga, "aw1", "aw1_short", "Frosch");
        communityService.create(bundesliga, "aw2", "aw2_short", "Frosch");
        communityService.create(bundesliga, "aw3", "aw3_short", "Frosch");
        communityService.create(bundesliga, "aw4", "aw4_short", "Frosch");
        
        assertThat(communityService.findCommunities(CommunityFilter.shortName("aw"), PageRequest.of(0, 10))).hasSize(4);
        assertThat(communityService.findCommunities(CommunityFilter.shortName("aw1"), PageRequest.of(0, 10))).hasSize(1);
    }

}
