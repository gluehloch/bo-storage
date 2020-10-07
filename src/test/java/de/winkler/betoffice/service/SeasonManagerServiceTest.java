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

package de.winkler.betoffice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.betoffice.database.data.MySqlDatabasedTestSupport.DataLoader;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.GameTipp;
import de.winkler.betoffice.storage.Group;
import de.winkler.betoffice.storage.GroupType;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.User;

/**
 * Testet das Verhalten von {@link DefaultSeasonManagerService}.
 *
 * @author Andre Winkler
 */
public class SeasonManagerServiceTest extends AbstractServiceTest {

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

    @BeforeEach
    public void setUp() throws Exception {
        dsuatd = new DatabaseSetUpAndTearDown(dataSource);
        dsuatd.setUp(DataLoader.FULL);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        dsuatd.tearDown();
    }

    @Test
    public void testAddGroupToBundesliga2006() {
        Season bundesliga = seasonManagerService.findSeasonByName("Fussball Bundesliga", "2006/2007").orElseThrow();
        GroupType liga2 = new GroupType();
        liga2.setName("2. Liga");
        masterDataManagerService.createGroupType(liga2);
        Season modifiedSeason = seasonManagerService.addGroupType(bundesliga, liga2);
        Group group = seasonManagerService.findGroup(modifiedSeason, liga2);
        assertThat(modifiedSeason.getGroups()).contains(group);
    }

    @Test
    public void findTipp() {
        Season bundesliga = seasonManagerService.findSeasonByName("Fussball Bundesliga", "2006/2007").orElseThrow();
        List<User> users = seasonManagerService.findActivatedUsers(bundesliga);
        assertThat(users).hasSize(10);
        assertThat(users).extracting("nickname", "name").contains(
                tuple("BayJan", "Heiner"),
                tuple("chris", "Hamster"),
                tuple("Frosch", "Erlohn"),
                tuple("Hattwig", "Huette"),
                tuple("Jogi", "Wagner"),
                tuple("mrTipp", "Meister"),
                tuple("Persistenz", "Schuster"),
                tuple("Peter", "Karlmann"),
                tuple("Roenne", "Rathaus"),
                tuple("Steffen", "Hummels"));

        GameList round = seasonManagerService.findRound(bundesliga, 0).orElseThrow();
        assertThat(round.unmodifiableList()).hasSize(9);

        List<GameTipp> tipp = tippService.findTipps(round.getId());
        assertThat(tipp).hasSize(81);
    }

}
