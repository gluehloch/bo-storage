/*
 * $Id: SeasonManagerServiceTest.java 3796 2013-08-05 03:58:02Z andrewinkler $
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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.winkler.betoffice.mail.MailContentDetails;
import de.winkler.betoffice.mail.MailXMLParser;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.GameTipp;
import de.winkler.betoffice.storage.GroupType;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.test.database.MySqlDatabasedTestSupport.DataLoader;

/**
 * Testet das Verhalten von {@link DefaultSeasonManagerService}.
 *
 * @author  $Author: andrewinkler $
 * @version $Revision: 3796 $ $Date: 2013-08-05 05:58:02 +0200 (Mon, 05 Aug 2013) $
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
    public void testMailEvaluation() {
        MailContentDetails details = new MailContentDetails();
        details.setChampionship("11");
        details.setEvaluated(false);
        details.setUsing(MailXMLParser.TIPP);
        details.setRound("1");
        details.setHomeGoals("9,8,7,6,5,4,3,2,1");
        details.setGuestGoals("1,2,3,4,5,6,7,8,9");
        details.setNickName("Frosch");
        details.setPwdA("xxx");

        Season season = seasonManagerService.findSeasonById(11);
        tippService.evaluateMailTipp(season, details);

        GameList round = seasonManagerService.findRound(season, 0);
        User user = masterDataManagerService.findUserByNickname("Frosch");

        List<GameTipp> tipps = tippService.findTippsByRoundAndUser(round, user);

        assertThat(tipps.size()).isEqualTo(9);

        for (GameTipp tipp : tipps) {
            assertThat(tipp.getUser().getNickName()).isEqualTo("Frosch");
            assertThat(tipp.getGame().getGameList()).isEqualTo(round);
        }
    }

    @Test
    public void testAddGroupToBundesliga2006() {
        Season buli = seasonManagerService.findSeasonByName(
                "Fussball Bundesliga", "2006/2007");
        buli = seasonManagerService.findRoundGroupTeamUserRelations(buli);
        GroupType liga2 = new GroupType();
        liga2.setName("2. Liga");
        masterDataManagerService.createGroupType(liga2);
        seasonManagerService.addGroupType(buli, liga2);
    }

}
