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
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.betoffice.database.data.MySqlDatabasedTestSupport.DataLoader;
import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.GameResult;
import de.winkler.betoffice.storage.Group;
import de.winkler.betoffice.storage.GroupType;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.Team;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.UserResult;
import de.winkler.betoffice.storage.enums.SeasonType;
import de.winkler.betoffice.storage.enums.TippStatusType;
import de.winkler.betoffice.validation.BetofficeValidationException;

/**
 * Creates a season and test some service methods of class {@link DefaultSeasonManagerService}.
 *
 * @author Andre Winkler
 */
public class SeasonManagerServiceCreateSeasonTest extends AbstractServiceTest {

    private static final String JUNIT_TOKEN = "#JUNIT#";

    private static final ZonedDateTime DATE_15_09_2010 = ZonedDateTime
            .of(LocalDateTime.of(LocalDate.of(2010, 9, 15), LocalTime.of(0, 0)), ZoneId.of("Europe/Berlin"));
    private static final ZonedDateTime DATE_08_09_2010 = ZonedDateTime
            .of(LocalDateTime.of(LocalDate.of(2010, 9, 8), LocalTime.of(0, 0)), ZoneId.of("Europe/Berlin"));
    private static final ZonedDateTime DATE_01_09_2010 = ZonedDateTime
            .of(LocalDateTime.of(LocalDate.of(2010, 9, 9), LocalTime.of(0, 0)), ZoneId.of("Europe/Berlin"));

    @Autowired
    protected DataSource dataSource;

    @Autowired
    protected SeasonManagerService seasonManagerService;

    @Autowired
    protected TippService tippService;

    @Autowired
    protected MasterDataManagerService masterDataManagerService;

    @Autowired
    protected DatabaseMaintenanceService databaseMaintenanceService;

    private DatabaseSetUpAndTearDown dsuatd;

    @BeforeEach
    public void setUp() throws Exception {
        dsuatd = new DatabaseSetUpAndTearDown(dataSource);
        dsuatd.setUp(DataLoader.EMPTY);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        dsuatd.tearDown();
    }

    private Team rwe;
    private Team schalke;
    private Team burghausen;
    private Team hsv;

    private GroupType bundesliga_1;
    private GroupType bundesliga_2;

    private Season buli_2010;
    private Group buli_1_group;
    @SuppressWarnings("unused")
    private Group buli_2_group;

//    private GameList round_01;
//    private GameList round_02;
//    private GameList round_03;

    private User frosch;
    private User peter;
    private User mrTipp;

    // ------------------------------------------------------------------------

    @Test
    public void testCreateSeasonAndAddTeams() {
        createTeams();
        createGroupTypes();
        createSeason();
        createGroups();
        addTeamsToBuli1();
    }

    @Test
    public void testCreateSeasonAndAddRemoveTeams() {
        createTeams();
        createGroupTypes();
        createSeason();
        createGroups();
        addTeamsToBuli1();
        createUsers();

        List<Team> removeTeams = Arrays.asList(rwe, schalke);
        seasonManagerService.removeTeams(buli_2010, bundesliga_1, removeTeams);

        List<Team> teams = seasonManagerService.findTeams(buli_2010, bundesliga_1);
        assertThat(teams).hasSize(2);
        assertThat(teams.get(0)).isEqualTo(hsv);
        assertThat(teams.get(1)).isEqualTo(burghausen);
    }

    @Test
    public void testCreateAndDeleteSeason() {
        createTeams();
        createGroupTypes();
        createSeason();
        createUsers();
        createGroups();
        addTeamsToBuli1();
        createRounds();

        try {
            seasonManagerService.deleteSeason(buli_2010);
            fail("Expected a BetofficeValidationException.");
        } catch (BetofficeValidationException ex) {
            assertThat(ex.getMessages()).hasSize(3);
        }
    }

    @Test
    public void testCreateSeasonAndAddAllTeams() {
        createTeams();
        createGroupTypes();
        createSeason();
        createGroups();

        List<Team> teams = new ArrayList<>();
        teams.add(hsv);
        teams.add(schalke);
        teams.add(burghausen);
        teams.add(rwe);
        buli_1_group = seasonManagerService.addTeams(buli_2010, bundesliga_1, teams);
        assertThat(buli_1_group.getTeams()).hasSize(4);
    }

    @Test
    public void testCreateSeasonAndAddRounds() {
        createTeams();
        createGroupTypes();
        createSeason();
        createGroups();
        addTeamsToBuli1();
        createRounds();
    }

    @Test
    public void testCreateSeasonAndAddUsersTwice() {
        createTeams();
        createGroupTypes();
        createSeason();

        User frosch = createUser("Frosch", "Andre", "Winkler");
        User peter = createUser("Peter", "Peter", "Groth");

        List<User> users = Arrays.asList(frosch, peter);
        seasonManagerService.addUsers(buli_2010, users);

        users = seasonManagerService.findActivatedUsers(buli_2010);
        assertThat(users).hasSize(2);
        assertThat(users.get(0)).isEqualTo(frosch);
        assertThat(users.get(1)).isEqualTo(peter);
    }

    @Test
    public void testCreateMatches() {
        createTeams();
        createGroupTypes();
        Season season = createSeason();
        createGroups();
        addTeamsToBuli1();
        createRounds();
        createMatches();
        
        List<GameList> rounds = seasonManagerService.findRounds(season);
        assertThat(rounds).hasSize(3);
        
        GameList round1 = seasonManagerService.findRound(season, 0).orElseThrow();
        assertThat(round1.size()).isEqualTo(2);
        
        GameList round2 = seasonManagerService.findRound(season, 1).orElseThrow();
        assertThat(round2.size()).isEqualTo(2);
        
        GameList round3 = seasonManagerService.findRound(season, 2).orElseThrow();
        assertThat(round3.size()).isEqualTo(2);
    }

    @Test
    public void testCreateMatchTipp() {
        createTeams();
        createGroupTypes();
        createSeason();
        createGroups();
        addTeamsToBuli1();
        createRounds();
        createMatches();
        createUsers();

//        DATE_01_09_2010, rwe, schalke, 2, 0
//        DATE_01_09_2010, hsv, burghausen, 1, 1
//
//        DATE_08_09_2010, burghausen, rwe, 1, 1
//        DATE_08_09_2010, schalke, hsv, 1, 1
//
//        DATE_15_09_2010, rwe, hsv, 1, 2
//        DATE_15_09_2010, burghausen, schalke, 0, 1        
        
        //
        // Frosch 2:0 | 1:1 || 1:1 | 1:1 || 1:2 | 0:1 
        //
        
        GameList round_01 = seasonManagerService.findRound(buli_2010, 0).orElseThrow();
        GameList round_02 = seasonManagerService.findRound(buli_2010, 1).orElseThrow();
        GameList round_03 = seasonManagerService.findRound(buli_2010, 2).orElseThrow();
        
        tippService.createOrUpdateTipp(JUNIT_TOKEN, round_01, frosch,
                List.of(new GameResult(2, 0), new GameResult(1, 1)), TippStatusType.USER);

        tippService.createOrUpdateTipp(JUNIT_TOKEN, round_02, frosch,
                List.of(new GameResult(1, 1), new GameResult(1, 1)), TippStatusType.USER);

        tippService.createOrUpdateTipp(JUNIT_TOKEN, round_03, frosch,
                List.of(new GameResult(1, 2), new GameResult(0, 1)), TippStatusType.USER);

        //
        // Peter
        //
        tippService.createOrUpdateTipp(JUNIT_TOKEN, round_01, peter,
                List.of(new GameResult(1, 1), new GameResult(1, 1)), TippStatusType.USER);

        tippService.createOrUpdateTipp(JUNIT_TOKEN, round_02, peter,
                List.of(new GameResult(2, 1), new GameResult(2, 1)), TippStatusType.USER);

        tippService.createOrUpdateTipp(JUNIT_TOKEN, round_03, peter,
                List.of(new GameResult(1, 2), new GameResult(0, 1)), TippStatusType.USER);

        //
        // mrTipp
        //
        tippService.createOrUpdateTipp(JUNIT_TOKEN, round_01, mrTipp,
                List.of(new GameResult(2, 1), new GameResult(0, 0)), TippStatusType.USER);

        tippService.createOrUpdateTipp(JUNIT_TOKEN, round_02, mrTipp,
                List.of(new GameResult(2, 2), new GameResult(2, 2)), TippStatusType.USER);

        tippService.createOrUpdateTipp(JUNIT_TOKEN, round_03, mrTipp,
                List.of(new GameResult(1, 3), new GameResult(0, 2)), TippStatusType.USER);

        //
        // Calculate user ranking
        //
        List<UserResult> userResult = seasonManagerService.calculateUserRanking(buli_2010);
        assertThat(userResult.get(0)).isEqualTo(frosch);
        assertThat(userResult.get(0).getTabPos()).isEqualTo(1);
        assertThat(userResult.get(0).getUserWin()).isEqualTo(6);
        assertThat(userResult.get(0).getUserTotoWin()).isEqualTo(0);
        assertThat(userResult.get(0).getTicket()).isEqualTo(0);

        assertThat(userResult.get(1)).isEqualTo(mrTipp);
        assertThat(userResult.get(1).getTabPos()).isEqualTo(2);
        assertThat(userResult.get(1).getUserWin()).isEqualTo(0);
        assertThat(userResult.get(1).getUserTotoWin()).isEqualTo(6);
        assertThat(userResult.get(1).getTicket()).isEqualTo(0);

        assertThat(userResult.get(2)).isEqualTo(peter);
        assertThat(userResult.get(2).getTabPos()).isEqualTo(3);
        assertThat(userResult.get(2).getUserWin()).isEqualTo(3);
        assertThat(userResult.get(2).getUserTotoWin()).isEqualTo(0);
        assertThat(userResult.get(2).getTicket()).isEqualTo(3);
    }

    private void createMatches() {
        createMatch(0, DATE_01_09_2010, rwe, schalke, 2, 0);
        createMatch(0, DATE_01_09_2010, hsv, burghausen, 1, 1);

        createMatch(1, DATE_08_09_2010, burghausen, rwe, 1, 1);
        createMatch(1, DATE_08_09_2010, schalke, hsv, 1, 1);

        createMatch(2, DATE_15_09_2010, rwe, hsv, 1, 2);
        createMatch(2, DATE_15_09_2010, burghausen, schalke, 0, 1);
    }

    private void createMatch(final int roundIndex, final ZonedDateTime date,
            final Team homeTeam, final Team guestTeam, final int homeGoals,
            final int guestGoals) {

        seasonManagerService.addMatch(buli_2010, roundIndex, date, bundesliga_1,
                homeTeam, guestTeam, homeGoals, guestGoals);

        GameList round = seasonManagerService.findRound(buli_2010, roundIndex).orElseThrow();
        Game match = seasonManagerService.findMatch(round, homeTeam, guestTeam).orElseThrow();

        assertThat(match.getHomeTeam()).isEqualTo(homeTeam);
        assertThat(match.getGuestTeam()).isEqualTo(guestTeam);
        assertThat(match.getResult().getHomeGoals()).isEqualTo(homeGoals);
        assertThat(match.getResult().getGuestGoals()).isEqualTo(guestGoals);
    }

    private void createRounds() {
        /* GameList round_01 = */ createRound(1, DATE_01_09_2010);
        /* GameList round_02 = */ createRound(2, DATE_08_09_2010);
        /* GameList round_03 = */ createRound(3, DATE_15_09_2010);

        assertThat(seasonManagerService.findRounds(buli_2010)).hasSize(3);

        assertThat(seasonManagerService.findRound(buli_2010, 0).orElseThrow()
                .getDateTime()).isEqualTo(DATE_01_09_2010);
        assertThat(seasonManagerService.findRound(buli_2010, 1).orElseThrow()
                .getDateTime()).isEqualTo(DATE_08_09_2010);
        assertThat(seasonManagerService.findRound(buli_2010, 2).orElseThrow()
                .getDateTime()).isEqualTo(DATE_15_09_2010);
    }

    private void addTeamsToBuli1() {
        Group group = seasonManagerService.addTeam(buli_2010, bundesliga_1, hsv);
        group = seasonManagerService.addTeam(buli_2010, bundesliga_1, schalke);
        group = seasonManagerService.addTeam(buli_2010, bundesliga_1, burghausen);
        group = seasonManagerService.addTeam(buli_2010, bundesliga_1, rwe);
        assertThat(group.getTeams()).hasSize(4);
        List<Group> groups = seasonManagerService.findGroups(buli_2010);
        assertThat(groups.get(0).getTeams()).hasSize(4);
    }

    private GameList createRound(final int roundNr, final ZonedDateTime date) {
        GameList round = seasonManagerService.addRound(buli_2010, date, bundesliga_1);
        assertThat(round.getSeason().size()).isEqualTo(roundNr);
        List<GameList> rounds = seasonManagerService.findRounds(buli_2010);
        assertThat(rounds).hasSize(roundNr);
        assertThat(rounds.get(roundNr - 1).getDateTime()).isEqualTo(date);
        return round;
    }

    private void createGroups() {
        buli_2010 = seasonManagerService.addGroupType(buli_2010, bundesliga_1);
        buli_1_group = buli_2010.getGroup(bundesliga_1);
        assertThat(buli_1_group).isNotNull();
        assertThat(buli_1_group.getSeason()).isEqualTo(buli_2010);
        assertThat(buli_1_group.getGroupType()).isEqualTo(bundesliga_1);

        buli_2010 = seasonManagerService.addGroupType(buli_2010, bundesliga_2);
        buli_2_group = buli_2010.getGroup(bundesliga_2);
        assertThat(buli_2_group).isNotNull();
        assertThat(buli_2_group.getSeason()).isEqualTo(buli_2010);
        assertThat(buli_2_group.getGroupType()).isEqualTo(bundesliga_2);

        List<Group> groups = seasonManagerService.findGroups(buli_2010);
        assertThat(groups).hasSize(2);
        assertThat(groups).containsExactlyInAnyOrder(buli_1_group, buli_2_group);
    }

    private Season createSeason() {
        buli_2010 = new Season();
        buli_2010.setName("Bundesliga 2010/2011");
        buli_2010.setYear("2010/2011");
        buli_2010.setMode(SeasonType.LEAGUE);
        seasonManagerService.createSeason(buli_2010);
        assertThat(seasonManagerService.findAllSeasons()).hasSize(1);
        
        return buli_2010;
    }

    private void createGroupTypes() {
        bundesliga_1 = createGroupType("1. Bundesliga");
        bundesliga_2 = createGroupType("2. Bundesliga");
        assertThat(masterDataManagerService.findAllGroupTypes()).hasSize(2);
    }

    private void createTeams() {
        rwe = createTeam("RWE", "Rot-Weiss-Essen");
        schalke = createTeam("S04", "Schalke 04");
        burghausen = createTeam("Wacker", "Wacker Burghausen");
        hsv = createTeam("HSV", "Hamburger SV");
        assertThat(masterDataManagerService.findAllTeams()).hasSize(4);
    }

    private Team createTeam(final String name, final String longname) {
        Team team = new Team();
        team.setName(name);
        team.setLongName(longname);
        masterDataManagerService.createTeam(team);
        assertThat(masterDataManagerService.findTeam(name).get())
                .isEqualTo(team);
        return team;
    }

    private GroupType createGroupType(final String groupTypeName) {
        GroupType groupType = new GroupType();
        groupType.setName(groupTypeName);
        masterDataManagerService.createGroupType(groupType);
        assertThat(masterDataManagerService.findGroupType(groupTypeName).get())
                .isEqualTo(groupType);
        return groupType;
    }

    private void createUsers() {
        frosch = createUser("Frosch", "Andre", "Winkler");
        peter = createUser("Peter", "Peter", "Groth");
        mrTipp = createUser("mrTipp", "Markus", "Rohloff");
    }

    private User createUser(final String nickname, final String surname,
            final String name) {

        User user = new User();
        user.setNickname(nickname);
        user.setName(name);
        user.setSurname(surname);
        masterDataManagerService.createUser(user);
        List<User> users = new ArrayList<User>();
        users.add(user);
        seasonManagerService.addUsers(buli_2010, users);
        return user;
    }

}
