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

import de.winkler.betoffice.storage.Team;
import de.winkler.betoffice.storage.TeamAlias;

/**
 * DAO für die Datenbanktabelle bo_teamalias.
 *
 * @author by Andre Winkler
 */
public interface TeamAliasDao extends CommonDao<TeamAlias> {

    /**
     * Liefert eine Liste alle Mannschafts Aliasnamen.
     *
     * @return Eine Liste aller Mannschafts Aliasnamen.
     */
    public List<TeamAlias> findAll();

    /**
     * Liefert eine Mannschaften mit gesuchten Aliasnamen.
     *
     * @param aliasName Der gesuchte Aliasname.
     * @return Eine Mannschaften.
     */
    public Team findByAliasName(String aliasName);

    /**
     * Liefert die Alias Namen einer Mannschaft.
     *
     * @param team Die gesuchte Mannschaft.
     * @return Die Alias Namen der Mannschaft.
     */
    public List<TeamAlias> findAliasNames(Team team);

    /**
     * Legt eine neuen Aliasnamen an.
     *
     * @param team Ein Mannschaft.
     */
    public void save(TeamAlias team);

    /**
     * Legt mehrere neue Aliasnamen an.
     *
     * @param teams Eine Liste von Mannschaften.
     */
    public void saveAll(List<TeamAlias> teams);

    /**
     * Eine Update-Operation.
     *
     * @param aliasName Ein Aliasname.
     */
    public void update(TeamAlias aliasName);

    /**
     * Löscht eine Mannschaft.
     *
     * @param value Der zu löschende Aliasname.
     */
    public void delete(TeamAlias value);

    /**
     * Löscht alle Mannschaften.
     *
     * @param aliasNames Der zu löschende Aliasname.
     */
    public void deleteAll(List<TeamAlias> aliasNames);

}
