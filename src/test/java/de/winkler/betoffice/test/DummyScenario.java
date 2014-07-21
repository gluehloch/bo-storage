/*
 * $Id: DummyScenario.java 3782 2013-07-27 08:44:32Z andrewinkler $
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

package de.winkler.betoffice.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.lang.Validate;
import org.joda.time.DateTime;

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
 * @author $Author: andrewinkler $
 * @version $Revision: 3782 $ $Date: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
public class DummyScenario {

    private static final Date DATE_2002_01_01 = new DateTime(2002, 1, 1, 0, 0).toDate();
    private static final Date DATE_2002_01_02 = new DateTime(2002, 1, 2, 0, 0).toDate();

    private final List<Season> seasons = new ArrayList<Season>();

    private Season season;
    private Group buli_1;
    private Group buli_2;
    private Team rwe;
    private Team s04;
    private Game game1;
    private Game game2;
    private Game game3;
    private Game game4;

    private final List<Game> matches = new ArrayList<Game>();

    private GameList round1;

    private final GameResult gr10;
    private final GameResult gr01;
    private final GameResult gr11;
    private final GameResult gr21;

    private DummyTeams teams;

    private DummyGroups groups;

    private DummyUsers users;

    public DummyScenario() throws Exception {
        gr10 = new GameResult(1, 0);
        gr01 = new GameResult(0, 1);
        gr11 = new GameResult(1, 1);
        gr21 = new GameResult(2, 1);

        initialize();
    }

    /**
     * Erzeugt ein Testszenario. Das Szenario beinhaltet:
     * <ul>
     * <li>4 Mannschaften: Team_A, Team_B, Team_C und Team_D.</li>
     * <li>Eine Gruppe: Test-Gruppe.</li>
     * <li>Einem Spieltag dem 12.12.2002.</li>
     * <li>4 Spiele an diesem Spieltag: <table border="1">
     * <tr>
     * <td>Spiel</td>
     * <td>User A</td>
     * <td>User B</td>
     * <td>User C</td>
     * <td>User D</td>
     * <td>Medium</td>
     * </tr>
     * <tr>
     * <td>(1.1.) Team_A - Team_B 2:1 </td>
     * <td>1:0</td>
     * <td>0:1</td>
     * <td>1:1</td>
     * <td>2:1</td>
     * <td>1:0</td>
     * </tr>
     * <tr>
     * <td>(1.2.) Team_C - Team_D 1:1 </td>
     * <td>1:0</td>
     * <td>0:1</td>
     * <td>1:1</td>
     * <td>2:1</td>
     * <td>1:0</td>
     * </tr>
     * <tr>
     * <td>(1.3.) Team_A - Team_B 0:1 </td>
     * <td>1:0</td>
     * <td>0:1</td>
     * <td>1:1</td>
     * <td>2:1</td>
     * <td>1:0</td>
     * </tr>
     * <tr>
     * <td>(1.4.) Team_A - Team_B 3:3 </td>
     * <td>1:0</td>
     * <td>0:1</td>
     * <td>1:1</td>
     * <td>2:1</td>
     * </tr>
     * </table> </li>
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
     * @throws Exception Da ging was schief.
     */
    private void initialize() throws Exception {
        teams = new DummyTeams();
        groups = new DummyGroups();
        users = new DummyUsers();

        UserResult.nEqualValue = 13;
        UserResult.nTotoValue = 10;
        UserResult.nZeroValue = 0;

        // Saison erzeugen.
        season = new Season();
        season.setMode(SeasonType.LEAGUE);
        season.setName("Bundesliga");
        season.setYear("1994/1995");
        seasons.add(season);

        // Gruppen erzeugen und eintragen...
        createGroups(season);

        // Die beteiligten Mannschaften erzeugen.
        createTeams();

        // In der Bundesliga nicht möglich. Unter WM oder EM Bedingungen
        // mit mehreren Gruppenphasen aber plausibel.
        buli_2.addTeam(teams.teams()[DummyTeams.BOCHUM]);
        buli_2.addTeam(teams.teams()[DummyTeams.BVB]);
        buli_2.addTeam(teams.teams()[DummyTeams.RWE]);

        // Spiele erzeugen.
        game1 = new Game();
        matches.add(game1);
        game2 = new Game();
        matches.add(game2);
        game3 = new Game();
        matches.add(game3);
        game4 = new Game();
        matches.add(game4);

        game1.setGroup(buli_1);
        game2.setGroup(buli_1);
        game3.setGroup(buli_1);
        game4.setGroup(buli_1);

        int n = 0;

        game1.setDateTime(DATE_2002_01_01);
        game1.setHomeTeam(teams.teams()[DummyTeams.BOCHUM]);
        game1.setGuestTeam(teams.teams()[DummyTeams.BVB]);
        game1.setResult(new GameResult(2, 1));
        game1.setPlayed(true);
        game1.setIndex(n++);

        game2.setDateTime(DATE_2002_01_01);
        game2.setHomeTeam(teams.teams()[DummyTeams.HSV]);
        game2.setGuestTeam(teams.teams()[DummyTeams.STPAULI]);
        game2.setResult(new GameResult(1, 1));
        game2.setPlayed(true);
        game2.setIndex(n++);

        game3.setDateTime(DATE_2002_01_02);
        game3.setHomeTeam(teams.teams()[DummyTeams.BOCHUM]);
        game3.setGuestTeam(teams.teams()[DummyTeams.BVB]);
        game3.setResult(new GameResult(0, 1));
        game3.setPlayed(true);
        game3.setIndex(n++);

        game4.setDateTime(DATE_2002_01_02);
        game4.setHomeTeam(teams.teams()[DummyTeams.BOCHUM]);
        game4.setGuestTeam(teams.teams()[DummyTeams.BVB]);
        game4.setResult(new GameResult(3, 3));
        game4.setPlayed(true);
        game4.setIndex(n++);

        // Spieltag erzeugen, Spiel eintragen.
        round1 = new GameList();
        round1.setDateTime(new DateTime(2002, 12, 12, 0, 0).toDate());
        round1.setGroup(buli_1);
        season.addGameList(round1);
        Assert.assertEquals(0, round1.unmodifiableList().size());
        round1.addGame(game1);
        round1.addGame(game2);
        round1.addGame(game3);
        round1.addGame(game4);
        round1.setGroup(buli_1);

        season.setCurrentGameList(round1);

        // Tipps erzeugen und eintragen.
        Assert.assertNotNull(game1.getGameList().getSeason());
        Assert.assertNotNull(game2.getGameList().getSeason());
        Assert.assertNotNull(game3.getGameList().getSeason());
        Assert.assertNotNull(game4.getGameList().getSeason());

        // Spiel 1
        game1.addTipp(users.users()[DummyUsers.FROSCH], gr10,
            TippStatusType.USER);
        game1.addTipp(users.users()[DummyUsers.HATTWIG], gr01,
            TippStatusType.USER);
        game1.addTipp(users.users()[DummyUsers.MRTIPP], gr11,
            TippStatusType.USER);
        game1.addTipp(users.users()[DummyUsers.PETER], gr21,
            TippStatusType.USER);

        // Spiel 2
        game2.addTipp(users.users()[DummyUsers.FROSCH], gr10,
            TippStatusType.USER);
        game2.addTipp(users.users()[DummyUsers.HATTWIG], gr01,
            TippStatusType.USER);
        game2.addTipp(users.users()[DummyUsers.MRTIPP], gr11,
            TippStatusType.USER);
        game2.addTipp(users.users()[DummyUsers.PETER], gr21,
            TippStatusType.USER);

        // Spiel 3
        game3.addTipp(users.users()[DummyUsers.FROSCH], gr10,
            TippStatusType.USER);
        game3.addTipp(users.users()[DummyUsers.HATTWIG], gr01,
            TippStatusType.USER);
        game3.addTipp(users.users()[DummyUsers.MRTIPP], gr11,
            TippStatusType.USER);
        game3.addTipp(users.users()[DummyUsers.PETER], gr21,
            TippStatusType.USER);

        // Spiel 4
        game4.addTipp(users.users()[DummyUsers.FROSCH], gr10,
            TippStatusType.USER);
        game4.addTipp(users.users()[DummyUsers.HATTWIG], gr01,
            TippStatusType.USER);
        game4.addTipp(users.users()[DummyUsers.MRTIPP], gr11,
            TippStatusType.USER);
        game4.addTipp(users.users()[DummyUsers.PETER], gr21,
            TippStatusType.USER);

        Validate.isTrue(getBuli_2().getTeams().size() == 3);
    }

    //
    // Getter Methoden für die generierten Objekte nach Aufruf
    // von createScenario.
    //

    public Group getBuli_1() {
        return buli_1;
    }

    public Group getBuli_2() {
        return buli_2;
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

    /**
     * Generiert einige Gruppen. Eine entsprechende Factory muss vorhanden sein.
     *
     * @param _season Die betreffende Saison.
     */
    private void createGroups(final Season _season) {
        Validate.notNull(season);

        buli_1 = new Group();
        buli_1.setGroupType(groups.groupTypes()[DummyGroups.BULI_1]);
        _season.addGroup(buli_1);

        buli_2 = new Group();
        buli_2.setGroupType(groups.groupTypes()[DummyGroups.BULI_2]);
        _season.addGroup(buli_2);
    }

    private void createTeams() {
        for (Iterator<Team> i = teams.toList().iterator(); i.hasNext();) {
            buli_1.addTeam(i.next());
        }

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
