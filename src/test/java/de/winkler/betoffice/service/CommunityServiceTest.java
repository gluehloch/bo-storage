/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2022 by Andre Winkler. All
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
import java.util.Set;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import de.betoffice.database.data.DatabaseTestData.DataLoader;
import de.winkler.betoffice.storage.Community;
import de.winkler.betoffice.storage.CommunityFilter;
import de.winkler.betoffice.storage.CommunityReference;
import de.winkler.betoffice.storage.Nickname;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.SeasonReference;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.enums.SeasonType;

/**
 * Service test for the community manager.
 * 
 * @author Andre Winkler
 */
class CommunityServiceTest extends AbstractServiceTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CommunityService communityService;

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
    void createCommunity() {
        Season bundesliga = new Season(SeasonReference.of("2020/2021", "Bundesliga"));
        bundesliga.setMode(SeasonType.LEAGUE);
        seasonManagerService.createSeason(bundesliga);

        Nickname frosch = Nickname.of("Frosch");
        User communityManager = new User();
        communityManager.setEmail("email@email.de");
        communityManager.setName("Andre");
        communityManager.setNickname(frosch);
        communityManager.setPassword("Passwort");

        communityManager = communityService.createUser(communityManager);
        Community community = communityService
                .create(CommunityReference.of("TDKB"), bundesliga.getReference(), "TDKB_short", frosch)
                .result()
                .orElseThrow();

        assertThat(community.getCommunityManager()).isEqualTo(communityManager);
        assertEquals("TDKB", community.getName());
        assertEquals(communityManager.getId(), community.getCommunityManager().getId());
    }

    @Test
    void addAndRemoveCommunityMembers() {
        Season bundesliga = new Season(SeasonReference.of("2020/2021", "Bundesliga"));
        bundesliga.setMode(SeasonType.LEAGUE);
        seasonManagerService.createSeason(bundesliga);

        Nickname frosch = Nickname.of("Frosch");
        User communityManager = new User();
        communityManager.setEmail("email@email.de");
        communityManager.setName("Andre");
        communityManager.setNickname(frosch);
        communityManager.setPassword("Passwort");

        communityManager = communityService.createUser(communityManager);
        CommunityReference communityReference = CommunityReference.of("TDKB");
        Community community = communityService
                .create(communityReference, bundesliga.getReference(), "TDKB_short", frosch)
                .result()
                .orElseThrow();

        Nickname demoA = Nickname.of("DemoA");
        User demoUserA = new User();
        demoUserA.setEmail("demoA@email.de");
        demoUserA.setName("DemoA-Name");
        demoUserA.setNickname(demoA);
        demoUserA.setPassword("DemoA-Password");
        demoUserA = communityService.createUser(demoUserA);

        Nickname demoB = Nickname.of("DemoB");
        User demoUserB = new User();
        demoUserB.setEmail("demoB@email.de");
        demoUserB.setName("DemoB-Name");
        demoUserB.setNickname(demoB);
        demoUserB.setPassword("DemoB-Password");
        demoUserB = communityService.createUser(demoUserB);

        communityService.addMember(community.getReference(), demoUserA.getNickname());
        communityService.addMember(community.getReference(), demoUserB.getNickname());

        Set<User> members = communityService.findMembers(community.getReference());
        assertThat(members).hasSize(2);
    }

    @Test
    void filterCommunities() {
        SeasonReference bundesligaRef = SeasonReference.of("2020/2021", "Bundesliga");
        Season bundesliga = new Season(bundesligaRef);
        bundesliga.setMode(SeasonType.LEAGUE);
        seasonManagerService.createSeason(bundesliga);

        Nickname nickname = Nickname.of("Andre");
        User communityManager = new User();
        communityManager.setEmail("email@email.de");
        communityManager.setName("Andre");
        communityManager.setNickname(nickname);
        communityManager.setPassword("Passwort");

        communityManager = communityService.createUser(communityManager);

        communityService.create(CommunityReference.of("CM_A"), bundesligaRef, "CM_A_short", nickname);
        communityService.create(CommunityReference.of("CM_B"), bundesligaRef, "CM_B_short", nickname);
        communityService.create(CommunityReference.of("CM_C"), bundesligaRef, "CM_C_short", nickname);
        communityService.create(CommunityReference.of("CM_D"), bundesligaRef, "CM_D_short", nickname);
        communityService.create(CommunityReference.of("CM_E"), bundesligaRef, "CM_E_short", nickname);

        CommunityFilter communityFilter = new CommunityFilter();
        communityFilter.setShortName("CM");

        Page<Community> list = communityService.findCommunities(communityFilter, PageRequest.of(0, 10));
        assertThat(list).hasSize(5);

        communityService.create(CommunityReference.of("aw1"), bundesligaRef, "aw1_short", nickname);
        communityService.create(CommunityReference.of("aw2"), bundesligaRef, "aw2_short", nickname);
        communityService.create(CommunityReference.of("aw3"), bundesligaRef, "aw3_short", nickname);
        communityService.create(CommunityReference.of("aw4"), bundesligaRef, "aw4_short", nickname);

        assertThat(communityService.findCommunities(CommunityFilter.shortName("aw"), PageRequest.of(0, 10))).hasSize(4);
        assertThat(communityService.findCommunities(CommunityFilter.shortName("aw1"), PageRequest.of(0, 10))).hasSize(1);
    }

}
