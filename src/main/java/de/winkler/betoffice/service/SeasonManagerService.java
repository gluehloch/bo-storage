/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2020 by Andre Winkler. All
 * rights reserved.
 * ============================================================================
 * GNU GENERAL  LICENSE TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND
 * MODIFICATION
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General  License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General  License for more
 * details.
 * 
 * You should have received a copy of the GNU General  License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

package de.winkler.betoffice.service;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.GameResult;
import de.winkler.betoffice.storage.GameTipp;
import de.winkler.betoffice.storage.Goal;
import de.winkler.betoffice.storage.Group;
import de.winkler.betoffice.storage.GroupType;
import de.winkler.betoffice.storage.Player;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.Team;
import de.winkler.betoffice.storage.TeamResult;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.UserResult;

/**
 * Definiert die allgemeine Service-Schnittstelle zur Verwaltung einer
 * Meisterschaft.
 *
 * @author by Andre Winkler
 */
public interface SeasonManagerService {

    /**
     * Berechnet das Tipper-Ranking für eine Meisterschaft.
     *
     * @param season
     *            Die betreffende Meisterschaft.
     * @return Das Ranking der Tipper.
     */
    List<UserResult> calculateUserRanking(Season season);

    /**
     * Berechnet das Tipper-Ranking für einen Spieltag.
     *
     * @param round
     *            Der betreffende Spieltag.
     * @return Das Ranking der Tipper.
     */
    List<UserResult> calculateUserRanking(GameList round);

    /**
     * Berechnet das Tipper-Ranking für einen Spieltag.
     *
     * @param season
     *            Die betreffende Meisterschaft.
     * @param startIndex
     *            Index des Spieltags ab dem gezählt wird (0..N-1).
     * @param endIndex
     *            Index des Spieltags bis zu dem gezählt wird (0..N-1).
     * @return Das Ranking der Tipper.
     */
    List<UserResult> calculateUserRanking(Season season, int startIndex,
            int endIndex);

    /**
     * Startet die Tabellenberechnung der Mannschaften einer Meisterschaft.
     *
     * @param season
     *            Die Meisterschaft.
     * @param groupType
     *            Die Liga/Gruppe die berechnet werden soll.
     * @return Eine sortierte Liste der Tabelle.
     */
    List<TeamResult> calculateTeamRanking(Season season,
            GroupType groupType);

    /**
     * Startet die Tabellenberechnung der Mannschaften einer Meisterschaft über
     * bestimmte Spielrunden.
     *
     * @param season
     *            Die Meisterschaft.
     * @param groupType
     *            Die Liga/Gruppe die berechnet werden soll.
     * @param startIndex
     *            Index des Start-Spieltags (0..N-1).
     * @param endIndex
     *            Index des End-Spieltags (0..N-1).
     * @return Eine sortierte Liste der Tabelle.
     */
    List<TeamResult> calculateTeamRanking(Season season,
            GroupType groupType, int startIndex, int endIndex);

    /**
     * Sucht nach den teilnehmenden Spieler einer Meisterschaft.
     *
     * @param season
     *            Die betreffende Meisterschaft.
     * @return Eine Liste der teilnehmenden Spieler für die Meisterschaft.
     */
    List<User> findActivatedUsers(Season season);

    /**
     * Sucht nach allen Meisterschaften.
     *
     * @return Eine Liste aller Meisterschaften.
     */
    List<Season> findAllSeasons();

    /**
     * Liefert alle bekannten Tore.
     * 
     * @return Eine Liste aller Tore.
     */
    List<Goal> findAllGoals();

    /**
     * Liefert alle Mannschaften einer Gruppe.
     *
     * @param group
     *            Die Mannschaften dieser Gruppe ermitteln.
     * @return Ein Liste aller Mannschaften dieser Gruppe.
     */
    List<Team> findTeams(Group group);

    /**
     * Liefert die Mannschaften für einen Gruppentyp einer bestimmten Meisteschaft.
     * 
     * @param season
     *            Die betreffende Meisterschaft.
     * @param groupType
     *            Der Gruppentyp.
     * @return Eine Lister aller Mannschaften des gesuchten Gruppentyps und
     *         Meisterschaft.
     */
    List<Team> findTeams(Season season, GroupType groupType);

    /**
     * Sucht nach einer bestimmten Meisterschaft.
     *
     * @param name
     *            Bezeichnung der Meisterschaft.
     * @param year
     *            Das Jahr der Meisterschaft.
     * @return Eine Meisterschaft.
     */
    Optional<Season> findSeasonByName(String name, String year);

    /**
     * Sucht nach einer bestimmten Meisterschaft.
     *
     * @param id
     *            Die ID der Meisterschaft.
     * @return Eine Meisterschaft.
     */
    Season findSeasonById(long id);

    /**
     * Liefert einen Spieltag für eine Meisterschaft.
     *
     * @param season
     *            Die betreffende Meisterschaft.
     * @param index
     *            Der Index des Spieltags (0 .. size-1).
     * @return Der Spieltag.
     */
    Optional<GameList> findRound(Season season, int index);

    /**
     * Liefert den letzten Spieltag einer Meisterschaft.
     * 
     * @param season
     *            die betreffende Meisterschaft
     * @return Der Spieltag
     */
    Optional<GameList> findLastRound(Season season);

    /**
     * Liefert den ersten Spieltag einer Meisterschaft.
     * 
     * @param season
     *            Die betreffende Meisterschaft.
     * @return Der Spieltag
     */
    Optional<GameList> findFirstRound(Season season);

    /**
     * Liefert einen Spieltag für eine Meisterschaft.
     *
     * @param roundId
     *            Die ID des Spieltags.
     * @return Der Spieltag.
     */
    GameList findRound(long roundId);

    /**
     * Liefert einen Spieltag einer Meisterschaft inklusive der Spiel und der
     * Spieltipps.
     * 
     * @param roundId
     *            Die ID des Spieltags
     * @return Der Spieltag
     */
    Optional<GameList> findRoundGameAndTipp(long roundId);

    /**
     * Liefert den nächsten Spieltag.
     * 
     * @param id
     *            Die ID des Spieltags
     * @return Der nächste Spieltag.
     */
    Optional<GameList> findNextRound(long id);

    /**
     * Liefert den vorhergehenden Spieltag.
     * 
     * @param id
     *            Die ID des Spieltags.
     * @return Der vorhergehende Spieltag.
     */
    Optional<GameList> findPrevRound(long id);

    /**
     * Liefert alle Spieltage einer Meisterschaft.
     *
     * @param season
     *            Die betreffende Meisterschaft.
     * @return Die Spieltage der Meisterschaft.
     */
    List<GameList> findRounds(Season season);

    /**
     * Liefert alle Spieltage einer Meisterschaft zu einer Gruppe. Falls dem
     * Spieltag keine Spieltage zugeordnet sind, werden keine Spieltag zurueck
     * geliefert.
     * 
     * @param group
     *            Die Spieltag fuer diese Gruppe.
     * @return Die Spieltage der gesuchten Gruppe und Meisterschaft.
     */
    List<GameList> findRounds(Group group);

    /**
     * Initialisiert die Liste der Spieltage, die zugehörigen Gruppen, Mannschaften
     * und Tippteilnehmer für die übergebene Meisterschaft.
     *
     * @param season
     *            Die Spieltage dieser Meisterschaft werden geladen.
     * @return Die übergebene Meisterschaft.
     */
    Season findRoundGroupTeamUserRelations(Season season);

    /**
     * Liefert alle Spieltipps zu einer Spielpaarung.
     *
     * @param match
     *            Die Spielpaarung deren Spieltipps gesucht werden.
     * @return Die Spieltipps.
     */
    List<GameTipp> findTippsByMatch(Game match);

    /**
     * Liefert alle Spieltipps zu einem Spieltag von einem Teilnehmer.
     *
     * @param round
     *            Der Spieltag der für die Suche herangezogen werden soll.
     * @param user
     *            Die Spieltipps von diesem User suchen.
     * @return Die Spieltipps.
     */
    List<GameTipp> findTipps(GameList round, User user);

    /**
     * Initialisiert die Liste der Spieltage, die zugehörigen Gruppen, Mannschaften
     * und Tippteilnehmer für die übergebene Meisterschaft inklusive aller
     * zugehörigen Spieltage und Spiele.
     *
     * @param season
     *            Die Spieltage dieser Meisterschaft werden geladen.
     * @return Die übergebene Meisterschaft.
     */
    Season findRoundGroupTeamUserGameRelations(Season season);

    /**
     * Liefert die Gruppen zu einer Meisterschaft.
     *
     * @param season
     *            Die betreffende Meisterschaft.
     * @return Die aktiven Gruppen zu der gesuchten Meisterschaft.
     */
    List<Group> findGroups(Season season);

    /**
     * Liefert alle Gruppentypen zu einer Meisterschaft.
     * 
     * @param season
     *            Die betreffende Meisterschaft
     * @return Die aktiven Gruppen der Meisterschaft
     */
    List<GroupType> findGroupTypes(Season season);

    /**
     * Liefert die Gruppe zu einer Meisterschaft.
     * 
     * @param season
     *            Die betreffende Meisterschaft
     * @param groupType
     *            Der Gruppentyp
     * @return Die Gruppe zu der Meisterschaft
     */
    Group findGroup(Season season, GroupType groupType);

    /**
     * Liefert die aktiven Gruppentypen der Meisterschaft.
     *
     * @param season
     *            Die betreffende Meisterschaft.
     * @return Die aktiven Gruppentypen zu der gesuchten Meisterschaft.
     */
    List<GroupType> findGroupTypesBySeason(Season season);

    /**
     * Sucht nach Spielen der vorgegebenen Mannschaften.
     *
     * @param homeTeam
     *            Die Heimmannschaft.
     * @param guestTeam
     *            Die Gastmannschaft.
     * @return Die gemeinsamen Spiele.
     */
    List<Game> findMatches(Team homeTeam, Team guestTeam);

    /**
     * Liefert die Spiele zu einem Spieltag.
     * 
     * @param round
     *            Der Spieltag.
     * @return Die Spiele an diesem Spieltag.
     */
    List<Game> findMatches(GameList round);

    /**
     * Sucht nach einem Spiel.
     * 
     * @param gameId
     *            Die technische ID des Spiels/game/match.
     * @return Das Spiel.
     */
    Game findMatch(Long gameId);

    /**
     * Sucht nach einem Spiel für einen Spieltag.
     *
     * @param round
     *            Die Spielrunde.
     * @param homeTeam
     *            Die Heimmannschaft.
     * @param guestTeam
     *            Die Gastmannschaft.
     * @return Das Spiel der beiden Mannschaften für den Spieltga. Liefert
     *         <code>null</code>, wenn kein Spiel gefunden werden konnte.
     */
    Optional<Game> findMatch(GameList round, Team homeTeam, Team guestTeam);

    /**
     * Sucht nach Spielen der vorgegebenen Mannschaften.
     *
     * @param homeTeam
     *            Die Heimmannschaft.
     * @param guestTeam
     *            Die Gastmannschaft.
     * @param spin
     *            Heim- und Gastmannschaft vertauschen?
     * @return Die gemeinsamen Spiele.
     */
    List<Game> findMatches(Team homeTeam, Team guestTeam, boolean spin);

    /**
     * Liefert einen Spieler inklusive aller seiner Tore.
     * 
     * @param id
     *            Die Spieler ID
     * @return Der Spieler inklusive aller seiner Tore.
     */
    Optional<Player> findGoalsOfPlayer(long id);

    /**
     * Erstellt eine neue Spielzeit.<br>
     * <b>ACHTUNG:</b> Nur die direkten Eigenschaften von 'Season' werden angelegt.
     * Alle ausgehenden Referenzen bleiben unberührt.
     *
     * @param season
     *            Eine Spielzeit.
     * @return Die angelegt Spielzeit. Mit Datenbank-ID.
     */
    Season createSeason(Season season);

    /**
     * Löscht eine Spielzeit.<br>
     * <b>ACHTUNG:</b> Alle Spieltage, Ergebnisse und Tipps werden ebenfalls
     * gelöscht. Also Vorsicht!
     *
     * @param season
     *            Eine Spielzeit.
     */
    void deleteSeason(Season season);

    /**
     * Aktualisierung einer Meisterschaft.
     *
     * @param season
     */
    void updateSeason(Season season);

    /**
     * Tippteilnehmer einer Spielzeit hinzufügen.
     *
     * @param season
     *            Die betreffende Meisterschaft.
     * @param user
     *            Der Teilnehmer.
     */
    void addUser(Season season, User user);

    /**
     * Tippteilnehmer einer Spielzeit hinzufügen.
     *
     * @param season
     *            Die betreffende Meisterschaft.
     * @param users
     *            Die zu aktivierenden Teilnehmer.
     */
    void addUsers(Season season, Collection<User> users);

    /**
     * Tippteilnehmer aus einer Spielzeit entfernen.
     *
     * @param season
     *            Die betreffende Meisterschaft.
     * @param users
     *            Die zu deaktivierenden Teilnehmer.
     */
    void removeUsers(Season season, Collection<User> users);

    /**
     * Hinzufügen einer Gruppe zu einer Meisterschaft.
     *
     * @param season
     *            Die zu bearbeitende Meisterschaft.
     * @param groupType
     *            Diese Gruppe hinzufügen.
     * @return Die Meisterschaft mit einer neuen Gruppe.
     */
    Season addGroupType(Season season, GroupType groupType);

    /**
     * Hinzufügen mehrerer Gruppe zu einer Meisterschaft.
     *
     * @param season
     *            Die zu bearbeitende Meisterschaft.
     * @param groupTypes
     *            Diese Gruppe hinzufügen.
     */
    void addGroupType(Season season, Collection<GroupType> groupTypes);

    /**
     * Entfernt eine Gruppe aus einer Meisterschaft. Die Mannschaften zu dieser
     * Gruppe werden aus der Beziehung ebenfalls gelöst.
     *
     * @param season
     *            Die zu bearbeitende Meisterschaft.
     * @param groupType
     *            Diese Gruppe entfernen.
     */
    void removeGroupType(Season season, GroupType groupType);

    /**
     * Entfernt Gruppen aus einer Meisterschaft. Die Mannschaften zu dieser Gruppe
     * werden aus der Beziehung ebenfalls gelöst.
     *
     * @param season
     *            Die zu bearbeitende Meisterschaft.
     * @param groupTypes
     *            Diese Gruppe entfernen.
     */
    void removeGroupType(Season season,
            Collection<GroupType> groupTypes);

    /**
     * Mannschaften einer Gruppe hinzufügen.
     *
     * @param season
     *            Die betreffende Meisterschaft.
     * @param groupType
     *            Eine Gruppe.
     * @param team
     *            Die zu aktivierenden Mannschaft.
     * @return Die Gruppe mit der ergaenzten Mannschaft
     */
    Group addTeam(Season season, GroupType groupType, Team team);

    /**
     * Mannschaften einer Gruppe hinzufügen.
     *
     * @param season
     *            Die betreffende Meisterschaft.
     * @param groupType
     *            Eine Gruppe.
     * @param teams
     *            Die zu aktivierenden Mannschaften.
     */
    Group addTeams(Season season, GroupType groupType,
            Collection<Team> teams);

    /**
     * Mannschaften aus einer Gruppe entfernen.
     *
     * @param season
     *            Die betreffende Meisterschaft.
     * @param groupType
     *            Eine Gruppe.
     * @param team
     *            Die zu deaktivierenden Mannschaft.
     */
    void removeTeam(Season season, GroupType groupType, Team team);

    /**
     * Mannschaften aus einer Gruppe entfernen.
     *
     * @param season
     *            Die betreffende Meisterschaft.
     * @param groupType
     *            Eine Gruppe.
     * @param teams
     *            Die zu deaktivierenden Mannschaften.
     */
    void removeTeams(Season season, GroupType groupType,
            Collection<Team> teams);

    /**
     * Ergänzt die Meisterschaft um einen Spieltag.
     *
     * @param season
     *            Zugehörige Meisterschaft.
     * @param date
     *            Datum des Spieltags. Einzelne Spielpaarungen können von diesem
     *            Datum abweichen!
     * @param groupType
     *            Spieltag einer Gruppe? Dieser Wert ist optional und kann für
     *            einzelne Spielpaarungen des Spieltags abweichen!
     * @return Der angelegte Spieltag.
     */
    GameList addRound(Season season, ZonedDateTime date, GroupType groupType);

    /**
     * Entfernt einen Spieltag.
     *
     * @param season
     *            Die betreffende Meisterschaft.
     * @param round
     *            Der zu entfernende Spieltag.
     */
    void removeRound(Season season, GameList round);

    /**
     * Eine neue Spielpaarung der Meisterschaft hinzufügen.
     *
     * @param round
     *            Der Spieltag.
     * @param date
     *            Datum des Spiels.
     * @param group
     *            Die Gruppe, der das Spiel zugeordnet wird.
     * @param homeTeam
     *            Die Heimmannschaft.
     * @param guestTeam
     *            Die Gastmannschaft.
     * @return Eine Spielpaarung.
     */
    Game addMatch(GameList round, ZonedDateTime date, Group group,
            Team homeTeam, Team guestTeam);

    /**
     * Eine neue Spielpaarung der Meisterschaft hinzufügen. Das Spiel gilt als
     * beendet.
     *
     * @param round
     *            Der Spieltag.
     * @param date
     *            Datum des Spiels.
     * @param group
     *            Die Gruppe, der das Spiel zugeordnet wird.
     * @param homeTeam
     *            Die Heimmannschaft.
     * @param guestTeam
     *            Die Gastmannschaft.
     * @param homeGoals
     *            Tore der Heimmannschaft.
     * @param guestGoals
     *            Tore der Gastmannschaft.
     * @param result
     *            Spielergebnis
     * @return Eine Spielpaarung.
     */
    Game addMatch(GameList round, ZonedDateTime date, Group group,
            Team homeTeam, Team guestTeam, GameResult result);

    /**
     * Eine neue Spielpaarung der Meisterschaft hinzufügen. Das Spiel gilt als
     * beendet.
     *
     * @param round
     *            Der Spieltag.
     * @param date
     *            Datum des Spiels.
     * @param group
     *            Die Gruppe, der das Spiel zugeordnet wird.
     * @param homeTeam
     *            Die Heimmannschaft.
     * @param guestTeam
     *            Die Gastmannschaft.
     * @param homeGoals
     *            Tore der Heimmannschaft.
     * @param guestGoals
     *            Tore der Gastmannschaft.
     * @return Eine Spielpaarung.
     */
    Game addMatch(GameList round, ZonedDateTime date, Group group,
            Team homeTeam, Team guestTeam, int homeGoals, int guestGoals);

    /**
     * Eine neue Spielpaarung der Meisterschaft hinzufügen.
     *
     * @param season
     *            Die Meisterschaft.
     * @param round
     *            Index des Spieltags.
     * @param date
     *            Datum des Spiels.
     * @param groupType
     *            Die Gruppe der das Spiel zugeordnet wird.
     * @param homeTeam
     *            Die Heimmannschaft.
     * @param guestTeam
     *            Die Gastmannschaft.
     * @return Eine Spielpaarung.
     */
    Game addMatch(Season season, int round, ZonedDateTime date,
            GroupType groupType, Team homeTeam, Team guestTeam);

    /**
     * Eine neue Spielpaarung der Meisterschaft hinzufügen.
     *
     * @param season
     *            Die Meisterschaft.
     * @param round
     *            Index des Spieltags.
     * @param date
     *            Datum des Spiels.
     * @param groupType
     *            Die Gruppe der das Spiel zugeordnet wird.
     * @param homeTeam
     *            Die Heimmannschaft.
     * @param guestTeam
     *            Die Gastmannschaft.
     * @param homeGoals
     *            Die Tore der Heimmannschaft.
     * @param guestGoals
     *            Die Tore der Gastmannschaft.
     * @return Eine Spielpaarung.
     */
    Game addMatch(Season season, int round, ZonedDateTime date,
            GroupType groupType, Team homeTeam, Team guestTeam,
            int homeGoals, int guestGoals);

    /**
     * Aktualisiert die Daten zu einer Spielpaarung.
     *
     * @param match
     *            Die zu aktualisierende Spielpaarung.
     */
    void updateMatch(Game match);

    /**
     * Aktualisiert die übergebenen Spielpaarungen.
     *
     * @param modifiedMatches
     *            Die zu aktualisierenden Spielpaarungen.
     */
    void updateMatch(Collection<Game> modifiedMatches);

    /**
     * Entfernt eine Spielpaarung aus der Spieltagsliste.
     *
     * @param match
     *            Eine Spielpaarung.
     */
    void removeMatch(Game match);

    /**
     * Ergaenzt eine Spielpaarung um ein Tor.
     * 
     * @param match
     *            Eine Spielpaarung
     * @param goal
     *            Ein Tor
     */
    void addGoal(Game match, Goal goal);

}
