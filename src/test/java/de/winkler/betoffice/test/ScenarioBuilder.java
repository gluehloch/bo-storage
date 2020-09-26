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
 */

package de.winkler.betoffice.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.winkler.betoffice.service.MasterDataManagerService;
import de.winkler.betoffice.service.SeasonManagerService;
import de.winkler.betoffice.service.TippService;
import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.GameResult;
import de.winkler.betoffice.storage.Group;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.Team;
import de.winkler.betoffice.storage.UserResult;
import de.winkler.betoffice.storage.enums.SeasonType;
import de.winkler.betoffice.storage.enums.TippStatusType;

/**
 * Baut ein Testszenario für BetOffice auf. <br>
 * <strong>Nur zu Testzwecken zu verwenden!</strong>
 * 
 * @author Andre Winkler
 */
@Service
public class ScenarioBuilder {

    private static final String JUNIT_TOKEN = "#JUNIT#";

    @Autowired
    private MasterDataManagerService masterDataManagerService;

    @Autowired
    private SeasonManagerService seasonManagerService;

    @Autowired
    private TippService tippService;

    private final List<Season> seasons = new ArrayList<Season>();

    private final GameResult gr10 = GameResult.of(1, 0);
    private final GameResult gr01 = GameResult.of(0, 1);
    private final GameResult gr11 = GameResult.of(1, 1);
    private final GameResult gr21 = GameResult.of(2, 1);

    private Season season;
    private Group ersteBundesliga;
    private Group zweiteBundesliga;
    private Team rwe;
    private Team s04;
    private Game game1;
    private Game game2;
    private Game game3;
    private Game game4;

    private final List<Game> matches = new ArrayList<Game>();

    private GameList round1;

    private DummyTeams teams;

    private DummyGroups groups;

    private DummyUsers users;

    /**
     * Erzeugt ein Testszenario. Das Szenario beinhaltet:
     * <ul>
     * <li>4 Mannschaften: Team_A, Team_B, Team_C und Team_D.</li>
     * <li>Eine Gruppe: Test-Gruppe.</li>
     * <li>Einem Spieltag dem 12.12.2002.</li>
     * <li>4 Spiele an diesem Spieltag:
     * <table border="1">
     * <tr>
     * <td>Spiel</td>
     * <td>User A</td>
     * <td>User B</td>
     * <td>User C</td>
     * <td>User D</td>
     * <td>Medium</td>
     * </tr>
     * <tr>
     * <td>(1.1.) Team_A - Team_B 2:1</td>
     * <td>1:0</td>
     * <td>0:1</td>
     * <td>1:1</td>
     * <td>2:1</td>
     * <td>1:0</td>
     * </tr>
     * <tr>
     * <td>(1.2.) Team_C - Team_D 1:1</td>
     * <td>1:0</td>
     * <td>0:1</td>
     * <td>1:1</td>
     * <td>2:1</td>
     * <td>1:0</td>
     * </tr>
     * <tr>
     * <td>(1.3.) Team_A - Team_B 0:1</td>
     * <td>1:0</td>
     * <td>0:1</td>
     * <td>1:1</td>
     * <td>2:1</td>
     * <td>1:0</td>
     * </tr>
     * <tr>
     * <td>(1.4.) Team_A - Team_B 3:3</td>
     * <td>1:0</td>
     * <td>0:1</td>
     * <td>1:1</td>
     * <td>2:1</td>
     * </tr>
     * </table>
     * </li>
     * <li>D.h. folgende Punktestände sind vorhanden:
     * <ul>
     * <ul>
     * Team A: 1 Sieg, 1 Remis, 1 Verloren. TV 5:5
     * </ul>
     * <ul>
     * Team B: 1 Sieg, 1 Remis, 1 Verloren. TV 5:5
     * </ul>
     * <ul>
     * Team C: 1 Remis. TV 1:1
     * </ul>
     * <ul>
     * Team D: 1 Remis. TV 1:1
     * </ul>
     * </ul>
     * </li>
     * <li>Für die Tipper:
     * <ul>
     * <ul>
     * (1) User B: 2 Ergebnis
     * </ul>
     * <ul>
     * (2) User A: 1 Toto
     * </ul>
     * <ul>
     * (2) User C: 1 Toto
     * </ul>
     * <ul>
     * (2) User D: 1 Toto
     * </ul>
     * </ul>
     * </li>
     * </ul>
     * 
     * @throws Exception
     *             Da ging was schief.
     */
    public void initialize() throws Exception {
        teams = new DummyTeams();
        teams.toList().stream().forEach(masterDataManagerService::createTeam);

        groups = new DummyGroups();
        groups.toList().stream().forEach(masterDataManagerService::createGroupType);

        users = new DummyUsers();
        users.toList().stream().forEach(masterDataManagerService::createUser);

        UserResult.nEqualValue = 13;
        UserResult.nTotoValue = 10;
        UserResult.nZeroValue = 0;

        // Saison erzeugen.
        season = new Season();
        season.setMode(SeasonType.LEAGUE);
        season.setName("Bundesliga");
        season.setYear("1994/1995");
        seasons.add(season);
        seasonManagerService.createSeason(season);

        // Gruppen erzeugen und eintragen...
        createGroups(season);

        // Die beteiligten Mannschaften erzeugen.
        createTeams();

        // In der Bundesliga nicht möglich. Unter WM oder EM Bedingungen
        // mit mehreren Gruppenphasen aber plausibel.
        seasonManagerService.addTeam(season, zweiteBundesliga.getGroupType(), teams.teams()[DummyTeams.BOCHUM]);
        seasonManagerService.addTeam(season, zweiteBundesliga.getGroupType(), teams.teams()[DummyTeams.BVB]);
        seasonManagerService.addTeam(season, zweiteBundesliga.getGroupType(), teams.teams()[DummyTeams.RWE]);

        // Spieltag erzeugen, Spiel eintragen.
        round1 = seasonManagerService.addRound(season, DateTimeDummyProducer.DATE_2002_01_02, ersteBundesliga.getGroupType());

        // Spiele erzeugen.
        game1 = seasonManagerService.addMatch(round1,
                DateTimeDummyProducer.DATE_2002_01_01,
                ersteBundesliga,
                teams.teams()[DummyTeams.BOCHUM],
                teams.teams()[DummyTeams.BVB],
                GameResult.of(2, 1));

        game2 = seasonManagerService.addMatch(round1,
                DateTimeDummyProducer.DATE_2002_01_01,
                ersteBundesliga,
                teams.teams()[DummyTeams.HSV],
                teams.teams()[DummyTeams.STPAULI],
                GameResult.of(1, 1));

        game3 = seasonManagerService.addMatch(round1,
                DateTimeDummyProducer.DATE_2002_01_02,
                ersteBundesliga,
                teams.teams()[DummyTeams.BOCHUM],
                teams.teams()[DummyTeams.BVB],
                GameResult.of(0, 1));

        game4 = seasonManagerService.addMatch(round1,
                DateTimeDummyProducer.DATE_2002_01_02,
                ersteBundesliga,
                teams.teams()[DummyTeams.BOCHUM],
                teams.teams()[DummyTeams.BVB],
                GameResult.of(3, 3));

        round1 = seasonManagerService.findRound(season, 0).orElseThrow();
        assertEquals(4, round1.unmodifiableList().size());

        matches.add(game1);
        matches.add(game2);
        matches.add(game3);
        matches.add(game4);
        
        // Spiel 1
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game1, users.users()[DummyUsers.FROSCH], gr10, TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game1, users.users()[DummyUsers.HATTWIG], gr01,
                TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game1, users.users()[DummyUsers.MRTIPP], gr11, TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game1, users.users()[DummyUsers.PETER], gr21, TippStatusType.USER);

        // Spiel 2
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game2, users.users()[DummyUsers.FROSCH], gr10, TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game2, users.users()[DummyUsers.HATTWIG], gr01,
                TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game2, users.users()[DummyUsers.MRTIPP], gr11, TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game2, users.users()[DummyUsers.PETER], gr21, TippStatusType.USER);

        // Spiel 3
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game3, users.users()[DummyUsers.FROSCH], gr10, TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game3, users.users()[DummyUsers.HATTWIG], gr01,
                TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game3, users.users()[DummyUsers.MRTIPP], gr11, TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game3, users.users()[DummyUsers.PETER], gr21, TippStatusType.USER);

        // Spiel 4
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game4, users.users()[DummyUsers.FROSCH], gr10, TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game4, users.users()[DummyUsers.HATTWIG], gr01,
                TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game4, users.users()[DummyUsers.MRTIPP], gr11, TippStatusType.USER);
        tippService.createOrUpdateTipp(JUNIT_TOKEN, game4, users.users()[DummyUsers.PETER], gr21, TippStatusType.USER);

        assertThat(seasonManagerService.findTeams(getSeason(), getZweiteBundesliga().getGroupType())).hasSize(3);
    }

    //
    // Getter Methoden für die generierten Objekte nach Aufruf
    // von createScenario.
    //

    public Group getErsteBundesliga() {
        return ersteBundesliga;
    }

    public Group getZweiteBundesliga() {
        return zweiteBundesliga;
    }

    public Team getRwe() {
        return rwe;
    }

    public Team getS04() {
        return s04;
    }

    public Season getSeason() {
        return season;
    }

    public List<Season> getChampionships() {
        return seasons;
    }

    public Game getGame1() {
        return game1;
    }

    public Game getGame2() {
        return game2;
    }

    public Game getGame3() {
        return game3;
    }

    public Game getGame4() {
        return game4;
    }

    public List<Game> getMatches() {
        return matches;
    }

    public GameList getRound1() {
        return round1;
    }

    //
    // Generator Methoden
    //

    private void createGroups(final Season _season) {
        season = seasonManagerService.addGroupType(season, groups.groupTypes()[DummyGroups.BULI_1]);
        ersteBundesliga = season.getGroup(groups.groupTypes()[DummyGroups.BULI_1]);
        season = seasonManagerService.addGroupType(season, groups.groupTypes()[DummyGroups.BULI_2]);
        zweiteBundesliga = season.getGroup(groups.groupTypes()[DummyGroups.BULI_2]);
    }

    private void createTeams() {
        seasonManagerService.addTeams(season, groups.groupTypes()[DummyGroups.BULI_1], teams.toList());
        rwe = teams.teams()[DummyTeams.RWE];
        s04 = teams.teams()[DummyTeams.S04];
    }

    public DummyGroups getGroups() {
        return groups;
    }

    public DummyTeams getTeams() {
        return teams;
    }

    public DummyUsers getUsers() {
        return users;
    }
}
