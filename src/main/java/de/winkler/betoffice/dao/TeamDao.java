/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2016 by Andre Winkler. All rights reserved.
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

package de.winkler.betoffice.dao;

import java.util.List;
import java.util.Optional;

import de.winkler.betoffice.storage.GroupType;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.Team;
import de.winkler.betoffice.storage.enums.TeamType;

/**
 * DAO Klasse für den Zugriff auf {@link de.winkler.betoffice.storage.Team} Objekte.
 *
 * @author by Andre Winkler
 */
public interface TeamDao extends CommonDao<Team> {

    /**
     * All teams ordered by team name.
     * 
     * @return a list of team names
     */
    List<Team> findAll();

    /**
     * Sucht nach allen Mannschaften zu einer Meisterschaft und Gruppe.
     *
     * @param  season    Season
     * @param  groupType GroupType
     * @return           List of teams
     */
    List<Team> findTeamsBySeasonAndGroup(Season season, GroupType groupType);

    /**
     * Liefert alle Mannschaften, die vom Typ {@link TeamType} sind.
     *
     * @param  teamType Der gesuchte Mannschaftstyp.
     * @return          Eine Liste mit Mannschaften.
     */
    List<Team> findTeams(TeamType teamType);

    /**
     * Liefert eine Mannschaften mit gesuchten Namen.
     *
     * @param  name Der gesuchte Name.
     * @return      Eine Mannschaften.
     */
    Optional<Team> findByName(String name);

    /**
     * Sucht nach einer Mannschaft anhand der Openligadb ID.
     *
     * @param  id Die Openligadb ID
     * @return    Eine Mannschaft.
     */
    Optional<Team> findByOpenligaid(long id);

}
