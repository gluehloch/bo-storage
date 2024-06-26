/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2020 by Andre Winkler. All rights reserved.
 * ============================================================================
 *          GNU GENERAL  LICENSE
 *  TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General  License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General  License for more details.
 *
 *   You should have received a copy of the GNU General  License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package de.winkler.betoffice.dao;

import java.util.List;
import java.util.Optional;

import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.GameTipp;
import de.winkler.betoffice.storage.User;

/**
 * Persistiert die {@link GameTipp} Objekte.
 *
 * @author by Andre Winkler
 */
public interface GameTippDao extends CommonDao<GameTipp> {

    /**
     * Liefert alle Spiel-Tipps zu einem Spiel.
     *
     * @param match
     *            Die Spiel-Tipps zu dieser Spielpaarung.
     * @return Die Spiel-Tipps.
     */
    List<GameTipp> find(Game match);

    /**
     * Liefert einen Spieltipp zu einem Spiel und Teilnehmer.
     * 
     * @param game
     *            Das Spiel
     * @param user
     *            Der Teilnehmer
     * @return Der Spieltipp
     */
    Optional<GameTipp> find(Game game, User user);

    /**
     * Liefert alle Spieltipps zu einem Spieltag zu einem Teilnehmer.
     *
     * @param round
     *            Der Spieltag.
     * @param user
     *            Der Teilnehmer.
     * @return Eine Liste der Tipps zu dem gesuchten Spieltag und Teilnehmer.
     */
    default List<GameTipp> find(GameList round, User user) {
        return find(round.getId(), user.getId());
    }

    /**
     * Liefert alle Spieltipps zu einem Spieltag zu einem Teilnehmer.
     *
     * @param roundId
     *            Der Spieltag.
     * @param userId
     *            Der Teilnehmer.
     * @return Eine Liste der Tipps zu dem gesuchten Spieltag und Teilnehmer.
     */
    List<GameTipp> find(long roundId, long userId);

    /**
     * Liefert alle Spieltipos zu einem Spieltag.
     * 
     * @param roundId
     *            Der Spieltag.
     * @return Eine List der Tipps zu dem gesuchten Spieltag.
     */
    List<GameTipp> find(long roundId);

}
