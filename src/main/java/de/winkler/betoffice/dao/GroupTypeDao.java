/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2016 by Andre Winkler. All rights reserved.
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
import java.util.Optional;

import de.winkler.betoffice.storage.GroupType;
import de.winkler.betoffice.storage.Season;

/**
 * DAO Klasse für den Zugriff auf {@link de.winkler.betoffice.storage.GroupType}
 * Objekte.
 *
 * @author by Andre Winkler
 */
public interface GroupTypeDao extends CommonDao<GroupType> {

    /**
     * Liefert einen Gruppetyp mit gesuchten Namen.
     *
     * @param name
     *            Der gesuchte Name.
     * @return Ein Gruppentyp.
     */
    Optional<GroupType> findByName(String name);

    /**
     * Liefert eine Liste der zugeordneten Gruppentypen einer Meisterschaft.
     *
     * @param season
     *            Die gesuchte Meisterschaft.
     * @return Die zugeordneten Gruppentypen der gesuchten Meisterschaft.
     */
    List<GroupType> findBySeason(Season season);

    /**
     * All group types sorted name.
     * 
     * @return all group types
     */
    List<GroupType> findAll();

}
