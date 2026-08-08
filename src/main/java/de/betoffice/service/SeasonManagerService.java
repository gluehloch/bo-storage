/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2024 by Andre Winkler. All
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

package de.betoffice.service;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import de.betoffice.storage.group.entity.GroupTypeEntity;
import de.betoffice.storage.season.entity.GameEntity;
import de.betoffice.storage.season.entity.GameListEntity;
import de.betoffice.storage.season.entity.GameResult;
import de.betoffice.storage.season.entity.GoalEntity;
import de.betoffice.storage.season.entity.GroupEntity;
import de.betoffice.storage.season.entity.PlayerEntity;
import de.betoffice.storage.season.entity.SeasonEntity;
import de.betoffice.storage.team.TeamResult;
import de.betoffice.storage.team.entity.TeamEntity;
import de.betoffice.storage.tip.GameTippEntity;
import de.betoffice.storage.user.entity.UserEntity;

/**
 * Definiert die allgemeine Service-Schnittstelle zur Verwaltung einer Meisterschaft.
 *
 * @author by Andre Winkler
 */
public interface SeasonManagerService {

    /**
     * Startet die Tabellenberechnung der Mannschaften einer Meisterschaft.
     *
     * @param  season    Die Meisterschaft.
     * @param  groupType Die Liga/Gruppe die berechnet werden soll.
     * @return           Eine sortierte Liste der Tabelle.
     */
    List<TeamResult> calculateTeamRanking(SeasonEntity season, GroupTypeEntity groupType);

    /**
     * Startet die Tabellenberechnung der Mannschaften einer Meisterschaft über bestimmte Spielrunden.
     *
     * @param  season     Die Meisterschaft.
     * @param  groupType  Die Liga/Gruppe die berechnet werden soll.
     * @param  startIndex Index des Start-Spieltags (0..N-1).
     * @param  endIndex   Index des End-Spieltags (0..N-1).
     * @return            Eine sortierte Liste der Tabelle.
     */
    List<TeamResult> calculateTeamRanking(SeasonEntity season, GroupTypeEntity groupType, int startIndex, int endIndex);

    /**
     * Sucht nach allen Meisterschaften.
     *
     * @return Eine Liste aller Meisterschaften.
     */
    List<SeasonEntity> findAllSeasons();

    /**
     * Liefert alle bekannten Tore.
     * 
     * @return Eine Liste aller Tore.
     */
    List<GoalEntity> findAllGoals();

    /**
     * Liefert alle Mannschaften einer Gruppe.
     *
     * @param  group Die Mannschaften dieser Gruppe ermitteln.
     * @return       Ein Liste aller Mannschaften dieser Gruppe.
     */
    List<TeamEntity> findTeams(GroupEntity group);

    /**
     * Liefert die Mannschaften für einen Gruppentyp einer bestimmten Meisteschaft.
     * 
     * @param  season    Die betreffende Meisterschaft.
     * @param  groupType Der Gruppentyp.
     * @return           Eine Lister aller Mannschaften des gesuchten Gruppentyps und Meisterschaft.
     */
    List<TeamEntity> findTeams(SeasonEntity season, GroupTypeEntity groupType);

    /**
     * Sucht nach einer bestimmten Meisterschaft.
     *
     * @param  name Bezeichnung der Meisterschaft.
     * @param  year Das Jahr der Meisterschaft.
     * @return      Eine Meisterschaft.
     */
    Optional<SeasonEntity> findSeasonByName(String name, String year);

    /**
     * Sucht nach einer bestimmten Meisterschaft.
     *
     * @param  id Die ID der Meisterschaft.
     * @return    Eine Meisterschaft.
     */
    SeasonEntity findSeasonById(long id);

    /**
     * Liefert einen Spieltag für eine Meisterschaft.
     *
     * @param  season Die betreffende Meisterschaft.
     * @param  index  Der Index des Spieltags (0 .. size-1).
     * @return        Der Spieltag.
     */
    Optional<GameListEntity> findRound(SeasonEntity season, int index);

    /**
     * Liefert den letzten Spieltag einer Meisterschaft.
     * 
     * @param  season die betreffende Meisterschaft
     * @return        Der Spieltag
     */
    Optional<GameListEntity> findLastRound(SeasonEntity season);

    /**
     * Liefert den ersten Spieltag einer Meisterschaft.
     * 
     * @param  season Die betreffende Meisterschaft.
     * @return        Der Spieltag
     */
    Optional<GameListEntity> findFirstRound(SeasonEntity season);

    /**
     * Liefert einen Spieltag für eine Meisterschaft.
     *
     * @param  roundId Die ID des Spieltags.
     * @return         Der Spieltag.
     */
    GameListEntity findRound(long roundId);

    /**
     * Liefert einen Spieltag einer Meisterschaft inklusive der Spiele.
     * 
     * @param  roundId Die ID des Spieltags
     * @return         Der Spieltag
     */
    Optional<GameListEntity> findRoundGames(long roundId);

    /**
     * Liefert den nächsten Spieltag.
     * 
     * @param  id Die ID des Spieltags
     * @return    Der nächste Spieltag.
     */
    Optional<GameListEntity> findNextRound(long id);

    /**
     * Liefert den vorhergehenden Spieltag.
     * 
     * @param  id Die ID des Spieltags.
     * @return    Der vorhergehende Spieltag.
     */
    Optional<GameListEntity> findPrevRound(long id);

    /**
     * Liefert alle Spieltage einer Meisterschaft.
     *
     * @param  season Die betreffende Meisterschaft.
     * @return        Die Spieltage der Meisterschaft.
     */
    List<GameListEntity> findRounds(SeasonEntity season);

    /**
     * Liefert alle Spieltage einer Meisterschaft zu einer Gruppe. Falls dem Spieltag keine Spieltage zugeordnet sind,
     * werden keine Spieltag zurueck geliefert.
     * 
     * @param  group Die Spieltag fuer diese Gruppe.
     * @return       Die Spieltage der gesuchten Gruppe und Meisterschaft.
     */
    List<GameListEntity> findRounds(GroupEntity group);

    /**
     * Liefert alle Spieltipps zu einer Spielpaarung.
     *
     * @param  match Die Spielpaarung deren Spieltipps gesucht werden.
     * @return       Die Spieltipps.
     */
    List<GameTippEntity> findTippsByMatch(GameEntity match);

    /**
     * Liefert alle Spieltipps zu einem Spieltag von einem Teilnehmer.
     *
     * @param  round Der Spieltag der für die Suche herangezogen werden soll.
     * @param  user  Die Spieltipps von diesem User suchen.
     * @return       Die Spieltipps.
     */
    List<GameTippEntity> findTipps(GameListEntity round, UserEntity user);

    /**
     * Liefert die Gruppen zu einer Meisterschaft.
     *
     * @param  season Die betreffende Meisterschaft.
     * @return        Die aktiven Gruppen zu der gesuchten Meisterschaft.
     */
    List<GroupEntity> findGroups(SeasonEntity season);

    /**
     * Liefert alle Gruppentypen zu einer Meisterschaft.
     * 
     * @param  season Die betreffende Meisterschaft
     * @return        Die aktiven Gruppen der Meisterschaft
     */
    List<GroupTypeEntity> findGroupTypes(SeasonEntity season);

    /**
     * Liefert die Gruppe zu einer Meisterschaft.
     * 
     * @param  season    Die betreffende Meisterschaft
     * @param  groupType Der Gruppentyp
     * @return           Die Gruppe zu der Meisterschaft
     */
    GroupEntity findGroup(SeasonEntity season, GroupTypeEntity groupType);

    /**
     * Liefert die Spiele zu einem Spieltag.
     * 
     * @param  round Der Spieltag.
     * @return       Die Spiele an diesem Spieltag.
     */
    List<GameEntity> findMatches(GameListEntity round);

    /**
     * Liefert alle Spielpaarungen für ein Datum.
     * 
     * @param  dateTime Das Bezugsdatum
     * @return          Alle Spielpaarungen für dieses Datum.
     */
    List<GameEntity> findMatches(ZonedDateTime dateTime);

    /**
     * Sucht nach Spielen der vorgegebenen Mannschaften.
     *
     * @param  homeTeam  Die Heimmannschaft.
     * @param  guestTeam Die Gastmannschaft.
     * @param  limit     Maximal Anzahl an Spielpaarungen ist zurück geliefert werden sollen
     * @return           Die gemeinsamen Spiele.
     */
    List<GameEntity> findMatches(TeamEntity homeTeam, TeamEntity guestTeam, int limit);

    /**
     * Sucht nach Spielen der vorgegebenen Mannschaften.
     *
     * @param  homeTeam  Die Heimmannschaft.
     * @param  guestTeam Die Gastmannschaft.
     * @param  spin      Heim- und Gastmannschaft vertauschen?
     * @param  limit     Maximal Anzahl an Spielpaarungen ist zurück geliefert werden sollen
     * @return           Die gemeinsamen Spiele.
     */
    List<GameEntity> findMatches(TeamEntity homeTeam, TeamEntity guestTeam, boolean spin, int limit);

    /**
     * Sucht nach Spielen mit der vorgegebenen Mannschaften.
     *
     * @param  team  Die Mannschaft.
     * @param  limit Maximal Anzahl an Spielpaarungen ist zurück geliefert werden sollen
     * @return       Die gefundenen Spiel.
     */
    List<GameEntity> findMatches(TeamEntity team, int limit);

    /**
     * Sucht nach alle Heimspielen mit der übergebenen Mannschaft.
     * 
     * @param  team  Die Mannschaft
     * @param  limit Maximale Anzahl an Spielpaarungen
     * @return       Die gefundenen Spiele
     */
    List<GameEntity> findMatchesWithHomeTeam(TeamEntity team, int limit);

    /**
     * Sucht nach alle Gastspielen mit der übergebenen Mannschaft.
     * 
     * @param  team  Die Mannschaft
     * @param  limit Maximal Anzahl an Spielpaarungen ist zurück geliefert werden sollen
     * @return       Die gefundenen Spiele
     */
    List<GameEntity> findMatchesWithGuestTeam(TeamEntity team, int limit);

    /**
     * Sucht nach einem Spiel.
     * 
     * @param  gameId Die technische ID des Spiels/game/match.
     * @return        Das Spiel.
     */
    GameEntity findMatch(Long gameId);

    /**
     * Sucht nach einem Spiel für einen Spieltag.
     *
     * @param  round     Die Spielrunde.
     * @param  homeTeam  Die Heimmannschaft.
     * @param  guestTeam Die Gastmannschaft.
     * @return           Das Spiel der beiden Mannschaften für den Spieltga. Liefert <code>null</code>, wenn kein Spiel
     *                   gefunden werden konnte.
     */
    Optional<GameEntity> findMatch(GameListEntity round, TeamEntity homeTeam, TeamEntity guestTeam);

    /**
     * Liefert alle Tore zu einem Spiel.
     *
     * @param  game
     * @return      alle Tore
     */
    List<GoalEntity> findGoalsOfMatch(GameEntity game);

    /**
     * Liefert einen Spieler inklusive aller seiner Tore.
     * 
     * @param  id Die Spieler ID
     * @return    Der Spieler inklusive aller seiner Tore.
     */
    Optional<PlayerEntity> findGoalsOfPlayer(long id);

    /**
     * Erstellt eine neue Spielzeit.<br>
     * <b>ACHTUNG:</b> Nur die direkten Eigenschaften von 'Season' werden angelegt. Alle ausgehenden Referenzen bleiben
     * unberührt.
     *
     * @param  season Eine Spielzeit.
     * @return        Die angelegt Spielzeit. Mit Datenbank-ID.
     */
    SeasonEntity createSeason(SeasonEntity season);

    /**
     * Löscht eine Spielzeit.<br>
     * <b>ACHTUNG:</b> Alle Spieltage, Ergebnisse und Tipps werden ebenfalls gelöscht. Also Vorsicht!
     *
     * @param season Eine Spielzeit.
     */
    void deleteSeason(SeasonEntity season);

    /**
     * Aktualisierung einer Meisterschaft.
     *
     * @param season
     */
    void updateSeason(SeasonEntity season);

    /**
     * Hinzufügen einer Gruppe zu einer Meisterschaft.
     *
     * @param  season    Die zu bearbeitende Meisterschaft.
     * @param  groupType Diese Gruppe hinzufügen.
     * @return           Die Meisterschaft mit einer neuen Gruppe.
     */
    SeasonEntity addGroupType(SeasonEntity season, GroupTypeEntity groupType);

    /**
     * Hinzufügen mehrerer Gruppe zu einer Meisterschaft.
     *
     * @param season     Die zu bearbeitende Meisterschaft.
     * @param groupTypes Diese Gruppe hinzufügen.
     */
    void addGroupType(SeasonEntity season, Collection<GroupTypeEntity> groupTypes);

    /**
     * Entfernt eine Gruppe aus einer Meisterschaft. Die Mannschaften zu dieser Gruppe werden aus der Beziehung
     * ebenfalls gelöst.
     *
     * @param season    Die zu bearbeitende Meisterschaft.
     * @param groupType Diese Gruppe entfernen.
     */
    void removeGroupType(SeasonEntity season, GroupTypeEntity groupType);

    /**
     * Entfernt Gruppen aus einer Meisterschaft. Die Mannschaften zu dieser Gruppe werden aus der Beziehung ebenfalls
     * gelöst.
     *
     * @param season     Die zu bearbeitende Meisterschaft.
     * @param groupTypes Diese Gruppe entfernen.
     */
    void removeGroupType(SeasonEntity season, Collection<GroupTypeEntity> groupTypes);

    /**
     * Mannschaften einer Gruppe hinzufügen.
     *
     * @param  season    Die betreffende Meisterschaft.
     * @param  groupType Eine Gruppe.
     * @param  team      Die zu aktivierenden Mannschaft.
     * @return           Die Gruppe mit der ergaenzten Mannschaft
     */
    GroupEntity addTeam(SeasonEntity season, GroupTypeEntity groupType, TeamEntity team);

    /**
     * Mannschaften einer Gruppe hinzufügen.
     *
     * @param  season    Die betreffende Meisterschaft.
     * @param  groupType Eine Gruppe.
     * @param  teams     Die zu aktivierenden Mannschaften.
     * @return           Die neu angelegte Gruppe.
     */
    GroupEntity addTeams(SeasonEntity season, GroupTypeEntity groupType, Collection<TeamEntity> teams);

    /**
     * Mannschaften aus einer Gruppe entfernen.
     *
     * @param season    Die betreffende Meisterschaft.
     * @param groupType Eine Gruppe.
     * @param team      Die zu deaktivierenden Mannschaft.
     */
    void removeTeam(SeasonEntity season, GroupTypeEntity groupType, TeamEntity team);

    /**
     * Mannschaften aus einer Gruppe entfernen.
     *
     * @param season    Die betreffende Meisterschaft.
     * @param groupType Eine Gruppe.
     * @param teams     Die zu deaktivierenden Mannschaften.
     */
    void removeTeams(SeasonEntity season, GroupTypeEntity groupType, Collection<TeamEntity> teams);

    /**
     * Ergänzt eine Meisterschaft um einen Spieltag.
     *
     * @param  season    Meisterschaft
     * @param  index     Index der Runde (zwischen 0..N-1)
     * @param  data      Spieltagsdatum
     * @param  groupType Gruppentyp
     * @return           Der angelegt Spieltag.
     */
    GameListEntity addRound(SeasonEntity season, int index, ZonedDateTime data, GroupTypeEntity groupType);

    /**
     * Ergänzt die Meisterschaft um einen Spieltag.
     *
     * @param  season    Zugehörige Meisterschaft.
     * @param  date      Datum des Spieltags. Einzelne Spielpaarungen können von diesem Datum abweichen!
     * @param  groupType Spieltag einer Gruppe? Dieser Wert ist optional und kann für einzelne Spielpaarungen des
     *                       Spieltags abweichen!
     * @return           Der angelegte Spieltag.
     */
    GameListEntity addRound(SeasonEntity season, ZonedDateTime date, GroupTypeEntity groupType);

    /**
     * TODO Funktioniert in dieser Form nicht. Die Transaktionsgenzen werden hier nicht korrekt gesetzt.
     * 
     * Entfernt einen Spieltag.
     *
     * @param season Die betreffende Meisterschaft.
     * @param round  Der zu entfernende Spieltag.
     */
    void removeRound(SeasonEntity season, GameListEntity round);

    /**
     * Eine neue Spielpaarung der Meisterschaft hinzufügen.
     *
     * @param  round     Der Spieltag.
     * @param  date      Datum des Spiels.
     * @param  group     Die Gruppe, der das Spiel zugeordnet wird.
     * @param  homeTeam  Die Heimmannschaft.
     * @param  guestTeam Die Gastmannschaft.
     * @return           Eine Spielpaarung.
     */
    GameEntity addMatch(GameListEntity round, ZonedDateTime date, GroupEntity group, TeamEntity homeTeam, TeamEntity guestTeam);

    /**
     * Eine neue Spielpaarung der Meisterschaft hinzufügen. Das Spiel gilt als beendet.
     *
     * @param  round      Der Spieltag.
     * @param  date       Datum des Spiels.
     * @param  group      Die Gruppe, der das Spiel zugeordnet wird.
     * @param  homeTeam   Die Heimmannschaft.
     * @param  guestTeam  Die Gastmannschaft.
     * @param  homeGoals  Tore der Heimmannschaft.
     * @param  guestGoals Tore der Gastmannschaft.
     * @param  result     Spielergebnis
     * @return            Eine Spielpaarung.
     */
    GameEntity addMatch(GameListEntity round, ZonedDateTime date, GroupEntity group, TeamEntity homeTeam, TeamEntity guestTeam, GameResult result);

    /**
     * Eine neue Spielpaarung der Meisterschaft hinzufügen. Das Spiel gilt als beendet.
     *
     * @param  round      Der Spieltag.
     * @param  date       Datum des Spiels.
     * @param  group      Die Gruppe, der das Spiel zugeordnet wird.
     * @param  homeTeam   Die Heimmannschaft.
     * @param  guestTeam  Die Gastmannschaft.
     * @param  homeGoals  Tore der Heimmannschaft.
     * @param  guestGoals Tore der Gastmannschaft.
     * @return            Eine Spielpaarung.
     */
    GameEntity addMatch(GameListEntity round, ZonedDateTime date, GroupEntity group, TeamEntity homeTeam, TeamEntity guestTeam, int homeGoals,
            int guestGoals);

    /**
     * Eine neue Spielpaarung der Meisterschaft hinzufügen.
     *
     * @param  season    Die Meisterschaft.
     * @param  round     Index des Spieltags.
     * @param  date      Datum des Spiels.
     * @param  groupType Die Gruppe der das Spiel zugeordnet wird.
     * @param  homeTeam  Die Heimmannschaft.
     * @param  guestTeam Die Gastmannschaft.
     * @return           Eine Spielpaarung.
     */
    GameEntity addMatch(SeasonEntity season, int round, ZonedDateTime date, GroupTypeEntity groupType, TeamEntity homeTeam, TeamEntity guestTeam);

    /**
     * Eine neue Spielpaarung der Meisterschaft hinzufügen.
     *
     * @param  season     Die Meisterschaft.
     * @param  round      Index des Spieltags.
     * @param  date       Datum des Spiels.
     * @param  groupType  Die Gruppe der das Spiel zugeordnet wird.
     * @param  homeTeam   Die Heimmannschaft.
     * @param  guestTeam  Die Gastmannschaft.
     * @param  homeGoals  Die Tore der Heimmannschaft.
     * @param  guestGoals Die Tore der Gastmannschaft.
     * @return            Eine Spielpaarung.
     */
    GameEntity addMatch(SeasonEntity season, int round, ZonedDateTime date, GroupTypeEntity groupType, TeamEntity homeTeam, TeamEntity guestTeam,
            int homeGoals, int guestGoals);

    /**
     * Aktualisiert die Daten zu einer Spielpaarung.
     *
     * @param match Die zu aktualisierende Spielpaarung.
     */
    void updateMatch(GameEntity match);

    /**
     * Aktualisiert die übergebenen Spielpaarungen.
     *
     * @param modifiedMatches Die zu aktualisierenden Spielpaarungen.
     */
    void updateMatch(Collection<GameEntity> modifiedMatches);

    /**
     * Aktualisiert das Datum/Gruppentyp für einen Spieltag.
     *
     * @param  season    Meisterschaft
     * @param  index     Inder der Runde (zwischen 0..N-1)
     * @param  date      Spieltagsdatum
     * @param  groupType Grupppentyp
     * @return           Der aktualisierte Spieltag.
     */
    GameListEntity updateRound(SeasonEntity season, int index, ZonedDateTime date, GroupTypeEntity groupType);

    /**
     * Entfernt eine Spielpaarung aus der Spieltagsliste.
     *
     * @param match Eine Spielpaarung.
     */
    void removeMatch(GameEntity match);

    /**
     * Ergaenzt eine Spielpaarung um ein Tor.
     * 
     * @param match Eine Spielpaarung
     * @param goal  Ein Tor
     */
    void addGoal(GameEntity match, GoalEntity goal);

}
