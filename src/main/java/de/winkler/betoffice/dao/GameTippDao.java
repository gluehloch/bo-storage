/*
 * $Id: GameTippDao.java 3798 2013-08-05 18:58:15Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2009 by Andre Winkler. All rights reserved.
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

import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.GameTipp;
import de.winkler.betoffice.storage.User;

/**
 * Persistiert die {@link GameTipp} Objekte.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3798 $ $LastChangedDate: 2013-08-05 20:58:15 +0200 (Mon, 05 Aug 2013) $
 */
public interface GameTippDao {

    /**
     * Liefert alle Spiel-Tipps zu einem Spiel.
     *
     * @param match Die Spiel-Tipps zu dieser Spielpaarung.
     * @return Die Spiel-Tipps.
     */
    public List<GameTipp> findByMatch(Game match);

    /**
     * Liefert alle Spieltipps zu einem Spieltag zu einem Teilnehmer.
     *
     * @param round Der Spieltag.
     * @param user Der Teilnehmer.
     * @return Eine Liste der Tipps zu dem gesuchten Spieltag und Teilnehmer.
     */
    public List<GameTipp> findTippsByRoundAndUser(GameList round, User user);

    /**
     * Liefert ähnliche Ergebnisse wie {@link #findTippsByRoundAndUser(GameList, User)}.
     * Hier sind die betreffenden Entity-IDs ausreichend. 
     *  
     * @see #findTippsByRoundAndUser(GameList, User)
     *
     * @param seasonId
     * @param roundId
     * @param nickname
     * @return Eine Liste aller Tipps zu dem gesuchten Spieltag und Teilnehmer.
     */
    public List<GameTipp> findTippsByRoundAndUser(long seasonId, long roundId,
            String nickname);

    /**
     * Speichert einen Spiel-Tipp..
     *
     * @param gameTipp Ein Spiel-Tipp.
     */
    public void save(GameTipp gameTipp);

    /**
     * Legt eine Liste von Spiel-Tipps an.
     *
     * @param gameTipps Ein Liste mit Spiel-Tipps.
     */
    public void saveAll(List<GameTipp> gameTipps);

    /**
     * Eine Update-Operation.
     *
     * @param gameTipp Ein Spiel-Tipp.
     */
    public void update(GameTipp gameTipp);

    /**
     * Löscht einen Spiel-Tipp.
     *
     * @param gameTipp Ein Spiel-Tipp.
     */
    public void delete(GameTipp gameTipp);

    /**
     * Löscht alle Spiel-Tipps.
     *
     * @param tipps Spiel-Tipps.
     */
    public void deleteAll(List<GameTipp> tipps);

}
