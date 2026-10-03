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

package de.betoffice.storage.team;

import java.util.List;
import java.util.Optional;

import de.betoffice.storage.community.CommonDao;
import de.betoffice.storage.group.entity.GroupTypeEntity;
import de.betoffice.storage.season.entity.SeasonEntity;
import de.betoffice.storage.team.entity.TeamEntity;

/**
 * DAO Klasse für den Zugriff auf {@link de.betoffice.storage.team.entity.TeamEntity} Objekte.
 *
 * @author by Andre Winkler
 */
public interface TeamDao extends CommonDao<TeamEntity> {

    /**
     * All teams ordered by team name.
     * 
     * @return a list of team names
     */
    List<TeamEntity> findAll();

    /**
     * Sucht nach allen Mannschaften zu einer Meisterschaft und Gruppe.
     *
     * @param  season    Season
     * @param  groupType GroupType
     * @return           List of teams
     */
    List<TeamEntity> findTeamsBySeasonAndGroup(SeasonEntity season, GroupTypeEntity groupType);

    /**
     * Liefert alle Mannschaften, die vom Typ {@link TeamType} sind.
     *
     * @param  teamType Der gesuchte Mannschaftstyp.
     * @return          Eine Liste mit Mannschaften.
     */
    List<TeamEntity> findTeams(TeamType teamType);

    /**
     * Liefert alle Mannschaften, die vom Typ {@link TeamType} sind.
     *
     * @param  teamType Der gesuchte Mannschaftstyp.
     * @param  filter   für name, long-name, short-name, alias, logo,...
     * @return          Eine Liste mit Mannschaften.
     */
    List<TeamEntity> findTeams(Optional<TeamType> teamType, String filter);

    /**
     * Liefert eine Mannschaften mit gesuchten Namen.
     *
     * @param  name Der gesuchte Name.
     * @return      Eine Mannschaften.
     */
    Optional<TeamEntity> findByName(String name);

    /**
     * Sucht nach einer Mannschaft anhand der Openligadb ID.
     *
     * @param  id Die Openligadb ID
     * @return    Eine Mannschaft.
     */
    Optional<TeamEntity> findByOpenligaid(long id);

}
