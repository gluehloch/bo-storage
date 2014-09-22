/*
 * $Id: CreateNewSeasonTest.java 3782 2013-07-27 08:44:32Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2010 by Andre Winkler. All
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
 */

package de.winkler.betoffice.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.betoffice.database.data.MySqlDatabasedTestSupport.DataLoader;
import de.betoffice.database.test.PersistenceTestSupport;
import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.Group;
import de.winkler.betoffice.storage.GroupType;
import de.winkler.betoffice.storage.Location;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.Team;
import de.winkler.betoffice.storage.enums.SeasonType;
import de.winkler.betoffice.storage.enums.TeamType;

/**
 * Testet das Erstellen einer neuen Meisterschaft.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3782 $ $LastChangedDate: 2013-07-27 10:44:32
 *          +0200 (Sat, 27 Jul 2013) $
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/betoffice-datasource.xml",
        "/betoffice-persistence.xml", "/test-mysql-piratestest.xml" })
public class CreateNewSeasonTest {

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
        dsuatd.setUp(DataLoader.MASTER_DATA);
    }

    @After
    public void tearDown() throws SQLException {
        dsuatd.tearDown();
    }

    @Test
    public void testDeleteDatabase() throws Exception {
        Connection conn = dataSource.getConnection();
        conn.setAutoCommit(false);
        try {
            tearDown();
        } finally {
            conn.close();
        }
    }

    @Test
    public void testAddIncompatibleTeamToSeason() throws Exception {
        SeasonManagerService sms = seasonManagerService;
        MasterDataManagerService mdms = masterDataManagerService;

        Season season = new Season();
        season.setMode(SeasonType.LEAGUE);
        season.setName("Bundesliga");
        season.setYear("2000/2001");
        season.setTeamType(TeamType.DFB);
        sms.createSeason(season);

        PersistenceTestSupport.assertTableEquals(dataSource,
                CreateNewSeasonTest.class, "bo_season", "createNewSeason");

        Team stuttgart = mdms.findTeam("VfB Stuttgart");
        Team hsv = mdms.findTeam("Hamburger SV");
        Team deutschland = mdms.findTeam("Deutschland");

        GroupType groupA = mdms.findGroupType("Gruppe A");
        sms.addGroupType(season, groupA);
        sms.addTeam(season, groupA, stuttgart);
        sms.addTeam(season, groupA, hsv);
        try {
            sms.addTeam(season, groupA, deutschland);
            fail("Expected a validation exception.");
        } catch (Exception ex) {
            // Ok!
        }
    }

    @Test
    public void testCreateNewSeason() throws Exception {
        SeasonManagerService sms = seasonManagerService;
        MasterDataManagerService mdms = masterDataManagerService;

        Season season = new Season();
        season.setMode(SeasonType.LEAGUE);
        season.setName("Bundesliga");
        season.setYear("2000/2001");
        season.setTeamType(TeamType.DFB);
        sms.createSeason(season);

        PersistenceTestSupport.assertTableEquals(dataSource,
                CreateNewSeasonTest.class, "bo_season", "createNewSeason");

        Team stuttgart = mdms.findTeam("VfB Stuttgart");
        Team hsv = mdms.findTeam("Hamburger SV");

        GroupType groupTypeA = mdms.findGroupType("Gruppe A");
        GroupType groupTypeB = mdms.findGroupType("Gruppe B");

        Group groupA = sms.addGroupType(season, groupTypeA);
        /*Group groupB =*/ sms.addGroupType(season, groupTypeB);

        sms.addTeam(season, groupTypeA, stuttgart);
        sms.addTeam(season, groupTypeA, hsv);

        List<GroupType> groupTypes = sms.findGroupTypesBySeason(season);
        assertEquals(groupTypeA.getId(), groupTypes.get(0).getId());
        assertEquals(groupTypeB.getId(), groupTypes.get(1).getId());

        sms.removeGroupType(season, groupTypeB);
        groupTypes = sms.findGroupTypesBySeason(season);
        assertEquals(groupTypeA.getId(), groupTypes.get(0).getId());
        assertEquals(1, groupTypes.size());

        DateTime now = new DateTime();
        GameList round = sms.addRound(season, now, groupTypeA);
        Game match = sms.addMatch(round, now, groupA, stuttgart, hsv);

        Location imtecharena = new Location();
        imtecharena.setCity("Hamburg");
        imtecharena.setName("Imtecharena");
        imtecharena.setGeodat("10.10.10.10");
        imtecharena.setOpenligaid(4711L);
        masterDataManagerService.createLocation(imtecharena);

        match.setLocation(imtecharena);
        sms.updateMatch(match);

        List<Game> matches = sms.findMatches(stuttgart, hsv);
        assertThat(matches.size(), equalTo(1));
        assertThat(matches.get(0).getLocation().getName(),
                equalTo("Imtecharena"));
    }

}
