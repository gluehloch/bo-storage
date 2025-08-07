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
 */

package de.betoffice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.betoffice.database.data.DatabaseTestData.DataLoader;
import de.betoffice.database.data.DeleteDatabase;
import de.betoffice.storage.enums.GoalType;
import de.betoffice.storage.enums.SeasonType;
import de.betoffice.storage.enums.TeamType;
import de.betoffice.storage.season.Game;
import de.betoffice.storage.season.GameList;
import de.betoffice.storage.season.GameResult;
import de.betoffice.storage.season.Goal;
import de.betoffice.storage.season.Group;
import de.betoffice.storage.season.GroupType;
import de.betoffice.storage.season.Location;
import de.betoffice.storage.season.Player;
import de.betoffice.storage.season.Season;
import de.betoffice.storage.season.SeasonReference;
import de.betoffice.storage.team.Team;

/**
 * Testet das Erstellen einer neuen Meisterschaft.
 *
 * @author by Andre Winkler
 */
class CreateNewSeasonTest extends AbstractServiceTest {

    private static final ZonedDateTime NOW = ZonedDateTime.now();

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
    void setUp() throws Exception {
        dsuatd = new DatabaseSetUpAndTearDown(dataSource);
        dsuatd.setUp(DataLoader.CORE);
    }

    @AfterEach
    void tearDown() throws SQLException {
        dsuatd.tearDown();
    }

    @Test
    void testDeleteDatabase() throws Exception {
        DeleteDatabase.deleteDatabase(dataSource.getConnection());
    }

    @Test
    void updateAndValidateRoundIndex() {
        final SeasonManagerService sms = seasonManagerService;
        final MasterDataManagerService mdms = masterDataManagerService;

        GroupType groupA = mdms.findGroupType("Gruppe A").orElseThrow();

        Season season = new Season(SeasonReference.of("2000/2001", "Bundesliga"));
        season.setMode(SeasonType.LEAGUE);
        season.setTeamType(TeamType.DFB);
        sms.createSeason(season);
        sms.addGroupType(season, groupA);

        GameList round0 = seasonManagerService.addRound(season, 0, NOW, groupA);
        GameList round1 = seasonManagerService.addRound(season, 1, NOW.plusDays(1), groupA);
        GameList round2 = seasonManagerService.addRound(season, 2, NOW.plusDays(2), groupA);

        assertThat(round0.getIndex()).isEqualTo(0);
        assertThat(round1.getIndex()).isEqualTo(1);
        assertThat(round2.getIndex()).isEqualTo(2);

        assertThatThrownBy(() -> {
            seasonManagerService.addRound(season, 0, NOW, groupA);
        }).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> {
            seasonManagerService.addRound(season, 1, NOW, groupA);
        }).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> {
            seasonManagerService.addRound(season, 2, NOW, groupA);
        }).isInstanceOf(IllegalArgumentException.class);

        round0 = sms.updateRound(season, 0, NOW.minusDays(1), groupA);
        assertThat(round0.getDateTime()).isEqualTo(NOW.minusDays(1));
    }

    @Test
    void testAddIncompatibleTeamToSeason() throws Exception {
        SeasonManagerService sms = seasonManagerService;
        MasterDataManagerService mdms = masterDataManagerService;

        Season season = new Season(SeasonReference.of("2001/2002", "Bundesliga"));
        season.setMode(SeasonType.LEAGUE);
        season.setTeamType(TeamType.DFB);
        sms.createSeason(season);

        Season seasonClone = sms.findSeasonById(season.getId());
        assertThat(seasonClone.getReference().getName()).isEqualTo(season.getReference().getName());
        assertThat(seasonClone.getReference().getYear()).isEqualTo(season.getReference().getYear());

        Team stuttgart = mdms.findTeam("VfB Stuttgart").orElseThrow();
        Team hsv = mdms.findTeam("Hamburger SV").orElseThrow();
        Team deutschland = mdms.findTeam("Deutschland").orElseThrow();

        GroupType groupA = mdms.findGroupType("Gruppe A").orElseThrow();
        final Season updatedSeason = sms.addGroupType(season, groupA);
        Group group = updatedSeason.getGroup(groupA);
        group = sms.addTeam(updatedSeason, groupA, stuttgart);
        group = sms.addTeam(updatedSeason, groupA, hsv);
        assertThat(group.getTeams()).hasSize(2);

        Exception ex = assertThrows(Exception.class, () -> {
            sms.addTeam(updatedSeason, groupA, deutschland);
        });
        assertEquals(null, ex.getMessage());
    }

    @Test
    void testCreateNewSeason() throws Exception {
        SeasonManagerService sms = seasonManagerService;
        MasterDataManagerService mdms = masterDataManagerService;

        Season season = new Season(SeasonReference.of("2020/2021", "Bundesliga"));
        season.setMode(SeasonType.LEAGUE);
        season.setTeamType(TeamType.DFB);
        sms.createSeason(season);

        Season season2 = sms.findSeasonById(season.getId());
        assertThat(season2.getReference()).isEqualTo(season.getReference());

        Team stuttgart = mdms.findTeam("VfB Stuttgart").orElseThrow();
        Team hsv = mdms.findTeam("Hamburger SV").orElseThrow();
        Team dortmund = mdms.findTeam("Borussia Dortmund").orElseThrow();
        Team wolfsburg = mdms.findTeam("VfL Wolfsburg").orElseThrow();
        Team fcKoeln = mdms.findTeam("1.FC Köln").orElseThrow();
        Team hansaRostock = mdms.findTeam("Hansa Rostock").orElseThrow();
        
        GroupType groupTypeA = mdms.findGroupType("Gruppe A").orElseThrow();
        GroupType groupTypeB = mdms.findGroupType("Gruppe B").orElseThrow();

        season = sms.addGroupType(season, groupTypeA);
        season = sms.addGroupType(season, groupTypeB);

        Group groupA = season.getGroup(groupTypeA);
        Group groupB = season.getGroup(groupTypeB);
        
        sms.addTeam(season, groupTypeA, stuttgart);
        sms.addTeam(season, groupTypeA, hsv);
        sms.addTeam(season, groupTypeA, dortmund);
        sms.addTeam(season, groupTypeA, wolfsburg);
        sms.addTeam(season, groupTypeA, fcKoeln);
        sms.addTeam(season, groupTypeA, hansaRostock);

        List<GroupType> groupTypes = sms.findGroupTypesBySeason(season);
        assertEquals(groupTypeA.getId(), groupTypes.get(0).getId());
        assertEquals(groupTypeB.getId(), groupTypes.get(1).getId());
        assertThat(groupTypes).hasSize(2);

        sms.removeGroupType(season, groupTypeB);
        groupTypes = sms.findGroupTypesBySeason(season);
        assertEquals(groupTypeA.getId(), groupTypes.get(0).getId());
        assertThat(groupTypes).hasSize(1);

        ZonedDateTime now = ZonedDateTime.now();
        GameList round1 = sms.addRound(season, now, groupTypeA);
        GameList round2 = sms.addRound(season, now.plusDays(1), groupTypeA);
        
        Game stuttgartVsHamburg = sms.addMatch(round1, now, groupA, stuttgart, hsv);
        assertThat(stuttgartVsHamburg.getIndex()).isEqualTo(0);
        
        Game dortmundVsWolfsburg = sms.addMatch(round1, now, groupA, dortmund, wolfsburg);
        assertThat(dortmundVsWolfsburg.getIndex()).isEqualTo(1);
        
        Game fcKoelnVsHansRostock = sms.addMatch(round1,  now,  groupA,  fcKoeln,  hansaRostock);
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

        List<Game> matches = sms.findMatches(stuttgart, hsv, 100);
        assertThat(matches).hasSize(1);
        Game actualMatch = matches.get(0);
        assertThat(actualMatch.getResult().getHomeGoals()).isEqualTo(2);
        assertThat(actualMatch.getResult().getGuestGoals()).isEqualTo(2);
        assertThat(actualMatch.getHalfTimeGoals().getHomeGoals()).isEqualTo(1);
        assertThat(actualMatch.getHalfTimeGoals().getGuestGoals()).isEqualTo(1);
        assertThat(actualMatch.getLocation().getName()).isEqualTo("Imtecharena");
        assertThat(actualMatch.getGoals().size()).isEqualTo(1);
        assertThat(actualMatch.getLocation().getName()).isEqualTo("Imtecharena");

        Player playerByOpenligaid = masterDataManagerService.findPlayerByOpenligaid(1L).orElseThrow();
        assertThat(playerByOpenligaid.getName()).isEqualTo("Mill");

        List<Goal> goals = sms.findAllGoals();
        assertThat(goals.size()).isEqualTo(1);
        assertThat(goals.get(0).getPlayer().getName()).isEqualTo("Lippens");

        Optional<Player> lippens = masterDataManagerService
                .findPlayerByOpenligaid(2L);
        Optional<Player> lippens2 = seasonManagerService
                .findGoalsOfPlayer(lippens.get().getId());
        assertThat(lippens2.get().getGoals().size()).isEqualTo(1);
        
        List<GameList> rounds = sms.findRounds(season);
        GameList gameList = sms.findRoundGames(rounds.get(0).getId()).orElseThrow();
        assertThat(gameList.size()).isEqualTo(3);

        Game dortmundVsHsv = seasonManagerService.addMatch(round2, now.plusDays(1), groupA, dortmund, hsv);
        Game hansaRostockVsWolfsburg = seasonManagerService.addMatch(round2, now.plusDays(1), groupA, hansaRostock, wolfsburg);
        assertThat(seasonManagerService.findRound(season, 1)).isPresent().hasValueSatisfying(round -> {
            assertThat(round.getIndex()).isEqualTo(1);
            assertThat(round.getGroup()).isEqualTo(groupA);
        });
        assertThat(dortmundVsHsv.getResult()).isEqualTo(GameResult.of(0, 0));
        assertThat(hansaRostockVsWolfsburg.getResult()).isEqualTo(GameResult.of(0, 0));

    }

}
