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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
public class CreateNewSeasonTest extends AbstractServiceTest {

    @Autowired
    protected DataSource dataSource;

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
        dsuatd.setUp(DataLoader.MASTER_DATA);
    }

    @AfterEach
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
        assertThat(seasonClone.getName()).isEqualTo(season.getName());
        assertThat(seasonClone.getYear()).isEqualTo(season.getYear());

        Team stuttgart = mdms.findTeam("VfB Stuttgart").orElseThrow(() -> new IllegalArgumentException());
        Team hsv = mdms.findTeam("Hamburger SV").orElseThrow(() -> new IllegalArgumentException());
        Team deutschland = mdms.findTeam("Deutschland").orElseThrow(() -> new IllegalArgumentException());

        GroupType groupA = mdms.findGroupType("Gruppe A").orElseThrow(() -> new IllegalArgumentException());
        Group group = sms.addGroupType(season, groupA);
        group = sms.addTeam(season, groupA, stuttgart);
        group = sms.addTeam(season, groupA, hsv);
        assertThat(group.getTeams()).hasSize(2);

        Exception ex = assertThrows(Exception.class, () -> {
            sms.addTeam(season, groupA, deutschland);
        });
        assertEquals(null, ex.getMessage());
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
        assertThat(seasonClone.getName()).isEqualTo(season.getName());
        assertThat(seasonClone.getYear()).isEqualTo(season.getYear());

        Team stuttgart = mdms.findTeam("VfB Stuttgart").orElseThrow(() -> new IllegalArgumentException());
        Team hsv = mdms.findTeam("Hamburger SV").orElseThrow(() -> new IllegalArgumentException());
        Team dortmund = mdms.findTeam("Borussia Dortmund").orElseThrow(() -> new IllegalArgumentException());
        Team wolfsburg = mdms.findTeam("VfL Wolfsburg").orElseThrow(() -> new IllegalArgumentException());
        Team fcKoeln = mdms.findTeam("1.FC Köln").orElseThrow(() -> new IllegalArgumentException());
        Team hansaRostock = mdms.findTeam("Hansa Rostock").orElseThrow(() -> new IllegalArgumentException());
        
        GroupType groupTypeA = mdms.findGroupType("Gruppe A").orElseThrow(() -> new IllegalArgumentException());
        GroupType groupTypeB = mdms.findGroupType("Gruppe B").orElseThrow(() -> new IllegalArgumentException());

        Group groupA = sms.addGroupType(season, groupTypeA);
        /* Group groupB = */ sms.addGroupType(season, groupTypeB);

        groupA = sms.addTeam(season, groupTypeA, stuttgart);
        groupA = sms.addTeam(season, groupTypeA, hsv);
        groupA = sms.addTeam(season, groupTypeA, dortmund);
        groupA = sms.addTeam(season, groupTypeA, wolfsburg);
        groupA = sms.addTeam(season, groupTypeA, fcKoeln);
        groupA = sms.addTeam(season, groupTypeA, hansaRostock);

        List<GroupType> groupTypes = sms.findGroupTypesBySeason(season);
        assertEquals(groupTypeA.getId(), groupTypes.get(0).getId());
        assertEquals(groupTypeB.getId(), groupTypes.get(1).getId());

        sms.removeGroupType(season, groupTypeB);
        groupTypes = sms.findGroupTypesBySeason(season);
        assertEquals(groupTypeA.getId(), groupTypes.get(0).getId());
        assertEquals(1, groupTypes.size());

        ZonedDateTime now = ZonedDateTime.now();
        GameList round = sms.addRound(season, now, groupTypeA);
        
        Game stuttgartVsHamburg = sms.addMatch(round, now, groupA, stuttgart, hsv);
        assertThat(stuttgartVsHamburg.getIndex()).isEqualTo(0);
        
        Game dortmundVsWolfsburg = sms.addMatch(round, now, groupA, dortmund, wolfsburg);
        assertThat(dortmundVsWolfsburg.getIndex()).isEqualTo(1);
        
        Game fcKoelnVsHansRostock = sms.addMatch(round,  now,  groupA,  fcKoeln,  hansaRostock);
        assertThat(fcKoelnVsHansRostock.getIndex()).isEqualTo(2);
        
        stuttgartVsHamburg.setResult(2, 2, true);
        stuttgartVsHamburg.setHalfTimeGoals(new GameResult(1, 1));

        dortmundVsWolfsburg.setResult(1, 0, true);
        dortmundVsWolfsburg.setHalfTimeGoals(0, 0);

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

        stuttgartVsHamburg.setLocation(imtecharena);
        sms.updateMatch(stuttgartVsHamburg);
        sms.updateMatch(dortmundVsWolfsburg);

        Goal goal1 = new Goal();
        goal1.setIndex(0);
        goal1.setComment("RWE macht wieder ein Tor.");
        goal1.setGoalType(GoalType.REGULAR);
        goal1.setMinute(55);
        goal1.setOpenligaid(5711L);
        goal1.setPlayer(enteLippens);
        goal1.setResult(new GameResult(0, 1));
        sms.addGoal(stuttgartVsHamburg, goal1);

        List<Game> matches = sms.findMatches(stuttgart, hsv);
        assertThat(matches).hasSize(1);
        Game actualMatch = matches.get(0);
        assertThat(actualMatch.getResult().getHomeGoals()).isEqualTo(2);
        assertThat(actualMatch.getResult().getGuestGoals()).isEqualTo(2);
        assertThat(actualMatch.getHalfTimeGoals().getHomeGoals()).isEqualTo(1);
        assertThat(actualMatch.getHalfTimeGoals().getGuestGoals()).isEqualTo(1);
        assertThat(actualMatch.getLocation().getName()).isEqualTo("Imtecharena");
        assertThat(actualMatch.getGoals().size()).isEqualTo(1);
        assertThat(actualMatch.getLocation().getName()).isEqualTo("Imtecharena");

        Optional<Player> playerByOpenligaid = masterDataManagerService
                .findPlayerByOpenligaid(1L);
        assertThat(playerByOpenligaid.get().getName()).isEqualTo("Mill");

        List<Goal> goals = sms.findAllGoals();
        assertThat(goals.size()).isEqualTo(1);
        assertThat(goals.get(0).getPlayer().getName()).isEqualTo("Lippens");

        Optional<Player> lippens = masterDataManagerService
                .findPlayerByOpenligaid(2L);
        Optional<Player> lippens2 = seasonManagerService
                .findGoalsOfPlayer(lippens.get().getId());
        assertThat(lippens2.get().getGoals().size()).isEqualTo(1);
    }

}
