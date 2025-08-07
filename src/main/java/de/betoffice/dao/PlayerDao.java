/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2016 by Andre Winkler. All rights reserved.
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
import java.util.Optional;

import de.betoffice.storage.season.Player;

/**
 * Player DAO
 *
 * @author by Andre Winkler
 */
public interface PlayerDao extends CommonDao<Player> {

    /**
     * Liefert eine Liste aller Fussballspieler.
     *
     * @return Eine Liste aller Fussballspieler.
     */
    public List<Player> findAll();

    /**
     * Liefert einen Fussballspieler anhand der openligadb ID
     * 
     * @param openligaid
     *            openligadb ID
     * @return Ein Spieler
     */
    public Optional<Player> findByOpenligaid(long openligaid);

    /**
     * Liefert einen Spieler mit allen Toren.
     * 
     * @param id
     *            Die Spieler ID
     * @return Ein Spieler
     */
    public Optional<Player> findAllGoalsOfPlayer(long id);

}
