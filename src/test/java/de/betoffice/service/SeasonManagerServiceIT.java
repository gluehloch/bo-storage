/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-20120 by Andre Winkler. All
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
import static org.assertj.core.api.Assertions.tuple;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.betoffice.database.data.DatabaseTestData.DataLoader;
import de.betoffice.storage.community.entity.Community;
import de.betoffice.storage.community.entity.CommunityReference;
import de.betoffice.storage.group.entity.GroupType;
import de.betoffice.storage.season.entity.GameList;
import de.betoffice.storage.season.entity.Group;
import de.betoffice.storage.season.entity.Season;
import de.betoffice.storage.tip.GameTipp;
import de.betoffice.storage.user.entity.User;

/**
 * Testet das Verhalten von {@link DefaultSeasonManagerService}.
 *
 * @author Andre Winkler
 */
@Tag("complete-database")
class SeasonManagerServiceIT extends AbstractServiceTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private TippService tippService;

    @Autowired
    private SeasonManagerService seasonManagerService;

    @Autowired
    private MasterDataManagerService masterDataManagerService;

    @Autowired
    private CommunityService communityService;

    private DatabaseSetUpAndTearDown dsuatd;

    @BeforeEach
    public void setUp() throws Exception {
        dsuatd = new DatabaseSetUpAndTearDown(dataSource);
        dsuatd.setUp(DataLoader.COMPLETE);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        dsuatd.tearDown();
    }

    @Test
    void testAddGroupToBundesliga2006() {
        Season bundesliga = seasonManagerService.findSeasonByName("Fussball Bundesliga", "2006/2007").orElseThrow();
        GroupType liga2 = new GroupType();
        liga2.setName("2. Liga");
        masterDataManagerService.createGroupType(liga2);
        Season modifiedSeason = seasonManagerService.addGroupType(bundesliga, liga2);
        Group group = seasonManagerService.findGroup(modifiedSeason, liga2);
        assertThat(modifiedSeason.getGroups()).contains(group);
    }

    @Test
    void findTipp() {
        final CommunityReference communityReference = CommunityReference.of("TDKB 2006/2007");
        Season bundesliga = seasonManagerService.findSeasonByName("Fussball Bundesliga", "2006/2007").orElseThrow();

        Community community = communityService.find(communityReference).orElseThrow();
        assertThat(community.getSeason()).isEqualTo(bundesliga);
        assertThat(community.getSeason()).isEqualTo(bundesliga);

        Set<User> users = communityService.findMembers(communityReference);

        assertThat(users).hasSize(10);
        assertThat(users).extracting("nickname.nickname", "name").contains(
                tuple("BayJan", "Hasselmann"),
                tuple("chris", "seidl"),
                tuple("Frosch", "Winkler"),
                tuple("Hattwig", "Hattwig"),
                tuple("Jogi", "Groth"),
                tuple("mrTipp", "Rohloff"),
                tuple("Persistenz", "Rohloff"),
                tuple("Peter", "Groth"),
                tuple("Roenne", "Rönnebeck"),
                tuple("Steffen", "Bucksath"));

        GameList round = seasonManagerService.findRound(bundesliga, 0).orElseThrow();
        assertThat(round.unmodifiableList()).hasSize(9);

        List<GameTipp> tipp = tippService.findTipps(round.getId());
        assertThat(tipp).hasSize(81);
    }

}
