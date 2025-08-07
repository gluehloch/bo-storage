/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2024 by Andre Winkler. All
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

import java.util.List;
import java.util.Optional;

import de.betoffice.storage.enums.TeamType;
import de.betoffice.storage.season.GroupType;
import de.betoffice.storage.season.Location;
import de.betoffice.storage.season.Player;
import de.betoffice.storage.season.Season;
import de.betoffice.storage.team.Team;
import de.betoffice.storage.team.TeamAlias;

/**
 * Verwaltet die Stammdaten von betoffice: Meisterschaft, Teilnehmer, Mannschaften und Ligen.
 *
 * @author by Andre Winkler
 */
public interface MasterDataManagerService {

    /**
     * Legt eine neue Meisterschaft an.
     * 
     * @param season Die Meisterschaft
     */
    public void createSeason(Season season);

    /**
     * Aktualisiert eine Meisterschaft.
     * 
     * @param season Die Meisterschaft
     */
    public void updateSeason(Season season);

    // ------------------------------------------------------------------------

    /**
     * Neuanlage einer Mannschaft.
     *
     * @param team Die Mannschaft.
     */
    public void createTeam(Team team);

    /**
     * Löschen einer Mannschaft. Eine Mannschaft kann nur gelöscht werden, wenn sie keiner Meisterschaft zugeordnet ist.
     *
     * @param team Die Mannschaft.
     */
    public void deleteTeam(Team team);

    /**
     * Aktualisierung der Daten einer Mannschaft.
     *
     * @param team Die Mannschaft.
     */
    public void updateTeam(Team team);

    /**
     * Liefert alle bekannten Mannschaften.
     *
     * @return Die bekannten Mannschaften.
     */
    public List<Team> findAllTeams();

    /**
     * Liefert eine Liste aller Mannschaften, die dem gesuchten {@link TeamType} entsprechen.
     *
     * @param  teamType Der gesuchte {@link TeamType}.
     * @return          Eine Liste mit Mannschaften.
     */
    public List<Team> findTeams(TeamType teamType);

    /**
     * Liefert einer Liste aller Mannschaften, die den gesuchten Parametern entsprechen.
     * 
     * @param  teamType Der gesuchte TeamType. Kann {@code null} sein.
     * @param  filter   Der Filter (name, long-name, short-name, logo, ...)
     * @return          Liste mit Mannschaften die den übergebenen Eigenschaften entsprechen
     */
    public List<Team> findTeams(Optional<TeamType> teamType, String filter);

    /**
     * Liefert alle Alias Namen zu einer Mannschaft.
     *
     * @param  team Die gesuchte Mannschaft.
     * @return      Die Alias Namen der Mannschaft.
     */
    public List<TeamAlias> findAllTeamAlias(Team team);

    /**
     * Sucht nach eine Mannschaft.
     *
     * @param  name Der Name der Mannschaft.
     * @return      Die Mannschaft
     */
    public Optional<Team> findTeam(String name);

    /**
     * Sucht eine Mannschaft anhand seines Alias Namen.
     *
     * @param  aliasName Der Aliasname der Mannschaft.
     * @return           Die gesuchte Mannschaft.
     */
    public Optional<Team> findTeamByAlias(String aliasName);

    /**
     * Liefert die Mannschaft zu einer ID.
     *
     * @param  id Die ID der gesuchten Mannschaft.
     * @return    Die gesuchte Mannschaft.
     */
    public Team findTeamById(long id);

    /**
     * Liefert eine Mannschaft anhand der openliga id.
     *
     * @param  id Openligadb ID
     * @return    Die gesuchte Mannschaft
     */
    public Optional<Team> findTeamByOpenligaid(long id);

    /**
     * Erstellt einen neuen Team Alias Namen.
     *
     * @param  team          Die Mannschaft, die einen neuen Alias Namen erhalten soll.
     * @param  teamAliasName Der neue Alias Name für die Mannschaft.
     * @return               Das TeamAlias.
     */
    public TeamAlias createTeamAlias(Team team, String teamAliasName);

    /**
     * Entfernt einen Alias Namen.
     *
     * @param teamAlias Der zu entfernende Alias Namen.
     */
    public void deleteTeamAlias(TeamAlias teamAlias);

    /**
     * Aktualisiert den Alias Namen einer Mannschaft.
     *
     * @param teamAlias Der geänderte Alias Name.
     */
    public void updateTeamAlias(TeamAlias teamAlias);

    // ------------------------------------------------------------------------

    /**
     * Neuanlage einer Gruppe (1. Liga, 2. Liga, Regionalliga, etc)
     *
     * @param groupType Eine Gruppe.
     */
    public void createGroupType(GroupType groupType);

    /**
     * Löscht einen Gruppentyp. Eine Gruppe kann nur gelöscht werden, wenn diese keiner Meisterschaft zugeordnet ist.
     *
     * @param groupType Eine Gruppe.
     */
    public void deleteGroupType(GroupType groupType);

    /**
     * Aktualisierung für eine Gruppe.
     *
     * @param groupType Eine Gruppe.
     */
    public void updateGroupType(GroupType groupType);

    /**
     * Liefert alle bekannten Gruppentypen.
     *
     * @return Alle Gruppentypen.
     */
    public List<GroupType> findAllGroupTypes();

    /**
     * Liefert einen bestimmten Gruppentyp.
     *
     * @param  name Der Name der gesuchten Gruppe.
     * @return      Die gesuchte Gruppe.
     */
    public Optional<GroupType> findGroupType(String name);

    /**
     * Liefert einen bestimmten Gruppentyp
     * 
     * @param  groupTypeId Die ID der gesuchten Gruppe
     * @return             Der entsprechende Gruppentyp
     */
    public GroupType findGroupType(long groupTypeId);

    // ------------------------------------------------------------------------

    /**
     * Erstellt eine neue Spielstaette.
     * 
     * @param location Eine Spielstaette.
     */
    public void createLocation(Location location);

    /**
     * Loescht eine Spielstaette.
     * 
     * @param location Die zu loeschende Spielstaette.
     */
    public void deleteLocation(Location location);

    /**
     * Aendern einer Spielstaette.
     * 
     * @param location Die zu aendernde Spielstaette
     */
    public void updateLocation(Location location);

    /**
     * Liefert alle Spielstaetten.
     * 
     * @return Eine Spielstaette
     */
    public List<Location> findAllLocations();

    /**
     * Findet eine Spielstaette.
     * 
     * @param  id Die ID der Spielstaette
     * @return    Eine Spielstaette
     */
    public Location findLocation(long id);

    /**
     * Findet eine Spielstaette anhand der 'openligadbid'
     * 
     * @param  openligaid Die openligadb ID
     * @return            Eine Spielstaette
     */
    public Optional<Location> findLocationByOpenligaid(long openligaid);

    // -- player --------------------------------------------------------------

    /**
     * Erstellt einen Spieler.
     * 
     * @param player Ein Spieler
     */
    public void createPlayer(Player player);

    /**
     * Loescht einen Spieler
     * 
     * @param player Ein Spieler
     */
    public void deletePlayer(Player player);

    /**
     * Aendert einen Spieler.
     * 
     * @param player Ein Spieler
     */
    public void updatePlayer(Player player);

    /**
     * Liefert eine Liste aller Spieler.
     * 
     * @return Eine Lister aller Spieler
     */
    public List<Player> findAllPlayers();

    /**
     * Liefert einen Spieler
     * 
     * @param  id Die ID des Spielers
     * @return    Ein Spieler oder <code>null</code>
     */
    public Player findPlayer(long id);

    /**
     * Liefert einen Spieler anhand der openligadb ID
     * 
     * @param  openligaid Die openligadb ID
     * @return            Ein Spieler
     */
    public Optional<Player> findPlayerByOpenligaid(long openligaid);

}
