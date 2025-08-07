/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2016 by Andre Winkler. All
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
 *
 */

package de.betoffice.dao;

import java.util.List;
import java.util.Optional;

import de.betoffice.storage.team.Team;
import de.betoffice.storage.team.TeamAlias;

/**
 * DAO für die Datenbanktabelle bo_teamalias.
 *
 * @author by Andre Winkler
 */
public interface TeamAliasDao extends CommonDao<TeamAlias> {

    /**
     * Find all team alias ordered by alias name.
     * 
     * @return All team alias
     */
    List<TeamAlias> findAll();

    /**
     * Liefert eine Mannschaften mit gesuchten Aliasnamen.
     *
     * @param aliasName
     *            Der gesuchte Aliasname.
     * @return Eine Mannschaften.
     */
    Optional<Team> findByAliasName(String aliasName);

    /**
     * Liefert die Alias Namen einer Mannschaft.
     *
     * @param team
     *            Die gesuchte Mannschaft.
     * @return Die Alias Namen der Mannschaft.
     */
    List<TeamAlias> findAliasNames(Team team);

}
