/*
 * $Id: GroupTypeDao.java 3782 2013-07-27 08:44:32Z andrewinkler $
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

import de.winkler.betoffice.storage.GroupType;
import de.winkler.betoffice.storage.Season;

/**
 * DAO Klasse für den Zugriff auf {@link de.winkler.betoffice.storage.GroupType}
 * Objekte.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3782 $ $LastChangedDate: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
public interface GroupTypeDao {

    /**
     * Liefert eine Liste aller Gruppentypen.
     *
     * @return Eine Liste aller Gruppentypen.
     */
    public List<GroupType> findAll();

    /**
     * Liefert einen Gruppetyp mit gesuchten Namen.
     *
     * @param name Der gesuchte Name.
     * @return Ein Gruppentyp.
     */
    public GroupType findByName(String name);

    /**
     * Liefert eine Liste der zugeordneten Gruppentypen einer Meisterschaft.
     *
     * @param season Die gesuchte Meisterschaft.
     * @return Die zugeordneten Gruppentypen der gesuchten Meisterschaft.
     */
    public List<GroupType> findBySeason(Season season);

    /**
     * Legt einen neuen Gruppetyp an.
     *
     * @param groupType Ein Gruppentyp.
     */
    public void save(GroupType groupType);

    /**
     * Legt eine Liste von Gruppentypen an.
     *
     * @param groupTypes Ein Liste mit Gruppentypen.
     */
    public void saveAll(List<GroupType> groupTypes);

    /**
     * Eine Update-Operation.
     *
     * @param groupType Ein Gruppentyp.
     */
    public void update(GroupType groupType);

    /**
     * Löscht einen Gruppentyp.
     *
     * @param groupType Ein Gruppentyp.
     */
    public void delete(GroupType groupType);

    /**
     * Löscht alle Gruppentypen.
     *
     * @param groupTypes Ein Gruppentyp.
     */
    public void deleteAll(List<GroupType> groupTypes);

}
