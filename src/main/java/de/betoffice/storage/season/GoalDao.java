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

package de.betoffice.storage.season;

import java.util.List;
import java.util.Optional;

import de.betoffice.storage.community.CommonDao;
import de.betoffice.storage.season.entity.Game;
import de.betoffice.storage.season.entity.Goal;

/**
 * The DAO for the goals.
 *
 * @author by Andre Winkler
 */
public interface GoalDao extends CommonDao<Goal> {

    /**
     * Get a list of all goals.
     * 
     * @return a list of all goals
     */
    List<Goal> findAll();

    /**
     * Get a list of all goals of a match
     * 
     * @param  game The game
     * @return      the goals
     */
    List<Goal> find(Game game);

    /**
     * Get a list of all goals of a match
     * 
     * @param  match The match
     * @return       the goals
     */
    List<Goal> find(long matchId);

    /**
     * Find a goal by the openligadbid
     * 
     * @param  openligaid openligadb ID
     * @return            A goal
     */
    Optional<Goal> findByOpenligaid(long openligaid);

    /**
     * Delete all goals of a game.
     * 
     * @param game the game
     */
    void deleteAll(Game game);

}
