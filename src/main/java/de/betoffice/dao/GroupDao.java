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

package de.betoffice.dao;

import java.util.List;

import de.betoffice.storage.Group;
import de.betoffice.storage.GroupType;
import de.betoffice.storage.Season;
import de.betoffice.storage.Team;

/**
 * DAO zur Verwaltung der {@link Group}s.
 *
 * @author by Andre Winkler
 */
public interface GroupDao extends CommonDao<Group> {

    /**
     * Liefert alle Gruppen.
     * 
     * @return Alle Gruppen
     */
    public List<Group> findAll();

    /**
     * Liefert eine Liste aller Gruppen zu einer Meisterschaft.
     *
     * @param  season Die Meisterschaft deren Gruppen gesucht werden.
     * @return        Eine Liste der Gruppen zu der gesuchten Meisterschaft.
     */
    public List<Group> findBySeason(Season season);

    /**
     * Liefert eine Liste aller Mannschaften zu einer Gruppe.
     *
     * @param  group Die Mannschaften dieser Gruppe werden gesucht.
     * @return       Eine Liste der Mannschaften der gesuchten Gruppe.
     */
    public List<Team> findTeams(Group group);

    /**
     * Löscht eine Gruppe.
     *
     * @param group Eine Gruppe.
     */
    public void delete(Group group);

    /**
     * @param  season    Die Meisterschaft deren Gruppe gesucht wird
     * @param  groupType Die Gruppe mit dem gesuchten Gruppentyp.
     * @return           Die Gruppe der Meisterschaft
     */
    public Group findBySeasonAndGroupType(Season season, GroupType groupType);

}
