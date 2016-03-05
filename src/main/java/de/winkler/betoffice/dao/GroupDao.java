/*
 * $Id: GroupDao.java 3832 2013-11-15 18:42:54Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2008 by Andre Winkler. All rights reserved.
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

package de.winkler.betoffice.dao;

import java.util.List;

import de.winkler.betoffice.storage.Group;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.Team;

/**
 * DAO zur Verwaltung der {@link Group}s.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3832 $ $LastChangedDate: 2013-11-15 19:42:54 +0100 (Fr, 15 Nov 2013) $
 */
public interface GroupDao {

    /**
     * Liefert die Gruppe zu einer ID
     *
     * @param id Die Datenbank ID
     * @return Eine Gruppe
     */
    public Group findById(long id);
    
    /**
     * Liefert eine Liste alle Gruppen.
     *
     * @return Eine Liste aller Gruppen.
     */
    public List<Group> findAll();

    /**
     * Liefert eine Liste aller Gruppen zu einer Meisterschaft.
     *
     * @param season Die Meisterschaft deren Gruppen gesucht werden.
     * @return Eine Liste der Gruppen zu der gesuchten Meisterschaft.
     */
    public List<Group> findBySeason(Season season);

    /**
     * Liefert eine Liste aller Mannschaften zu einer Gruppe.
     *
     * @param group Die Mannschaften dieser Gruppe werden gesucht.
     * @return Eine Liste der Mannschaften der gesuchten Gruppe.
     */
    public List<Team> findTeams(Group group);

    /**
     * Legt eine neue Gruppe an.
     *
     * @param group Eine neue Gruppe.
     */
    public void save(Group group);

    /**
     * Speichert mehrere neue Gruppen.
     *
     * @param groups Eine Liste von Gruppen.
     */
    public void saveAll(List<Group> groups);

    /**
     * Ein Update.
     *
     * @param group Eine Gruppe
     */
    public void update(Group group);

    /**
     * Löscht eine Gruppe.
     *
     * @param group Eine Gruppe.
     */
    public void delete(Group group);

    /**
     * Löscht alle Gruppen.
     *
     * @param groups Die Gruppen.
     */
    public void deleteAll(List<Group> groups);
    
}
