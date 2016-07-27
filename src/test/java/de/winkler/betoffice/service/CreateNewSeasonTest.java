/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2014 by Andre Winkler. All
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

import de.betoffice.database.data.DeleteDatabase;
import de.betoffice.database.data.MySqlDatabasedTestSupport.DataLoader;
import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.GameResult;
import de.winkler.betoffice.storage.Goal;
import de.winkler.betoffice.storage.Group;
import de.winkler.betoffice.storage.GroupType;
import de.winkler.betoffice.storage.Location;
import de.winkler.betoffice.storage.Player;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.Team;
import de.winkler.betoffice.storage.enums.GoalType;
import de.winkler.betoffice.storage.enums.SeasonType;
import de.winkler.betoffice.storage.enums.TeamType;

/**
 * Testet das Erstellen einer neuen Meisterschaft.
 *
 * @author by Andre Winkler
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
        DeleteDatabase.deleteDatabase(dataSource.getConnection());
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

        Season seasonClone = sms.findSeasonById(season.getId());
        assertThat(seasonClone.getName(), equalTo(season.getName()));
        assertThat(seasonClone.getYear(), equalTo(season.getYear()));

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

        Season seasonClone = sms.findSeasonById(season.getId());
        assertThat(seasonClone.getName(), equalTo(season.getName()));
        assertThat(seasonClone.getYear(), equalTo(season.getYear()));

        Team stuttgart = mdms.findTeam("VfB Stuttgart");
        Team hsv = mdms.findTeam("Hamburger SV");

        GroupType groupTypeA = mdms.findGroupType("Gruppe A");
        GroupType groupTypeB = mdms.findGroupType("Gruppe B");

        Group groupA = sms.addGroupType(season, groupTypeA);
        /* Group groupB = */sms.addGroupType(season, groupTypeB);

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
        match.setResult(2, 2, true);
        match.setHalfTimeGoals(new GameResult(1, 1));

        Location imtecharena = new Location();
        imtecharena.setCity("Hamburg");
        imtecharena.setName("Imtecharena");
        imtecharena.setGeodat("10.10.10.10");
        imtecharena.setOpenligaid(4711L);
        masterDataManagerService.createLocation(imtecharena);

        Player frankMill = new Player();
        frankMill.setName("Mill");
        frankMill.setVorname("Frank");
        frankMill.setOpenligaid(1L);
        masterDataManagerService.createPlayer(frankMill);

        Player enteLippens = new Player();
        enteLippens.setName("Lippens");
        enteLippens.setVorname("Ente");
        enteLippens.setOpenligaid(2L);
        masterDataManagerService.createPlayer(enteLippens);

        match.setLocation(imtecharena);
        sms.updateMatch(match);

        Goal goal1 = new Goal();
        goal1.setIndex(0);
        goal1.setComment("RWE mach wieder ein Tor.");
        goal1.setGoalType(GoalType.REGULAR);
        goal1.setMinute(55);
        goal1.setOpenligaid(5711L);
        goal1.setPlayer(enteLippens);
        goal1.setResult(new GameResult(0, 1));
        sms.addGoal(match, goal1);

        List<Game> matches = sms.findMatches(stuttgart, hsv);
        assertThat(matches.size(), equalTo(1));
        Game actualMatch = matches.get(0);
        assertThat(actualMatch.getResult().getHomeGoals(), equalTo(2));
        assertThat(actualMatch.getResult().getGuestGoals(), equalTo(2));
        assertThat(actualMatch.getHalfTimeGoals().getHomeGoals(), equalTo(1));
        assertThat(actualMatch.getHalfTimeGoals().getGuestGoals(), equalTo(1));
        assertThat(actualMatch.getLocation().getName(), equalTo("Imtecharena"));
        assertThat(actualMatch.getGoals().size(), equalTo(1));
        assertThat(actualMatch.getLocation().getName(), equalTo("Imtecharena"));

        Player playerByOpenligaid = masterDataManagerService
                .findPlayerByOpenligaid(1L);
        assertThat(playerByOpenligaid.getName(), equalTo("Mill"));

        List<Goal> goals = sms.findAllGoals();
        assertThat(goals.size(), equalTo(1));
        assertThat(goals.get(0).getPlayer().getName(), equalTo("Lippens"));

        Player lippens = masterDataManagerService.findPlayerByOpenligaid(2L);
        Player lippens2 = seasonManagerService.findGoalsOfPlayer(lippens
                .getId());
        assertThat(lippens2.getGoals().size(), equalTo(1));
    }

}
