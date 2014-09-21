/*
 * $Id: MasterDataManagerService.java 3935 2014-03-09 14:48:15Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2014 by Andre Winkler. All rights reserved.
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

package de.winkler.betoffice.service;

import java.util.List;

import de.winkler.betoffice.storage.GroupType;
import de.winkler.betoffice.storage.Team;
import de.winkler.betoffice.storage.TeamAlias;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.enums.TeamType;

/**
 * Verwaltet die Stammdaten von betoffice: Teilnehmer, Mannschaften und Ligen.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3935 $ $LastChangedDate: 2014-03-09 15:48:15 +0100 (Sun, 09 Mar 2014) $
 */
public interface MasterDataManagerService {

    /**
     * Neuanlage einer Mannschaft.
     *
     * @param team Die Mannschaft.
     */
    public void createTeam(Team team);

    /**
     * Löschen einer Mannschaft. Eine Mannschaft kann nur gelöscht werden,
     * wenn sie keiner Meisterschaft zugeordnet ist.
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
     * Liefert eine Liste aller Mannschaften, die dem gesuchten {@link TeamType}
     * entsprechen.
     *
     * @param teamType Der gesuchte {@link TeamType}.
     * @return Eine Liste mit Mannschaften.
     */
    public List<Team> findTeams(TeamType teamType);
 
    /**
     * Liefert alle Alias Namen zu einer Mannschaft.
     *
     * @param team Die gesuchte Mannschaft.
     * @return Die Alias Namen der Mannschaft.
     */
    public List<TeamAlias> findAllTeamAlias(Team team);

    /**
     * Sucht nach eine Mannschaft.
     *
     * @param name Der Name der Mannschaft.
     * @return Die Mannschaft
     */
    public Team findTeam(String name);

    /**
     * Sucht eine Mannschaft anhand seines Alias Namen.
     *
     * @param aliasName Der Aliasname der Mannschaft.
     * @return Die gesuchte Mannschaft.
     */
    public Team findTeamByAlias(String aliasName);

    /**
     * Liefert die Mannschaft zu einer ID.
     *
     * @param id Die ID der gesuchten Mannschaft.
     * @return Die gesuchte Mannschaft.
     */
    public Team findTeamById(long id);

    /**
     * Liefert eine Mannschaft anhand der openliga id.
     *
     * @param id Openligadb ID
     * @return Die gesuchte Mannschaft
     */
    public Team findTeamByOpenligaId(long id);
    
    /**
     * Erstellt einen neuen Team Alias Namen.
     *
     * @param team Die Mannschaft, die einen neuen Alias Namen erhalten soll.
     * @param teamAliasName Der neue Alias Name für die Mannschaft.
     * @return Das TeamAlias.
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
     * Neuanlage eines Teilnehmers.
     *
     * @param user Ein Teilnehmer.
     */
    public void createUser(User user);

    /**
     * Löschen eines Teilnehmers. Ein Teilnehmer kann nur gelöscht werden,
     * wenn dieser keiner Meisterschaft zugeordnet ist.
     * @param user Ein Teilnehmer.
     */
    public void deleteUser(User user);

    /**
     * Aktualisierung der Daten eines Teilnehmers.
     *
     * @param user Ein Teilnehmer.
     */
    public void updateUser(User user);

    /**
     * Liefert alle bekannten Teilnehmer zurück.
     *
     * @return Die bekannten Teilnehmer.
     */
    public List<User> findAllUsers();

    /**
     * Sucht nach einem Benutzer mit gesuchtem Nickname.
     *
     * @param nickname Der Nickname des gesuchten Teilnehmers.
     * @return Der User oder <code>null</code> wenn keiner gefunden.
     */
    public User findUserByNickname(String nickname);

    // ------------------------------------------------------------------------

    /**
     * Neuanlage einer Gruppe (1. Liga, 2. Liga, Regionalliga, etc)
     *
     * @param groupType Eine Gruppe.
     */
    public void createGroupType(GroupType groupType);

    /**
     * Löscht einen Gruppentyp. Eine Gruppe kann nur gelöscht werden, wenn
     * diese keiner Meisterschaft zugeordnet ist.
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
     * @param name Der Name der gesuchten Gruppe.
     * @return Die gesuchte Gruppe.
     */
    public GroupType findGroupType(String name);

}
