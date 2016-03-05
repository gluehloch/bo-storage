/*
 * $Id: MatchDao.java 3834 2013-11-15 18:56:03Z andrewinkler $
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

import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.Team;

/**
 * Dieses DAO sucht nach bestimmten Spielpaarungen.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3834 $ $LastChangedDate: 2013-11-15 19:56:03 +0100 (Fr, 15 Nov 2013) $
 */
public interface MatchDao {

    /**
     * Sucht nach einem Spiel.
     *
     * @param id Die Datenbank ID
     * @return Das gesuchte Spiel
     */
    public Game findById(long id);
    
    /**
     * Sucht nach einer bestimmten Spielpaarung für einen Spieltag.
     *
     * @param round Der zu untersuchende Spieltag.
     * @param homeTeam Die Heimmannschaft.
     * @param guestTeam Die Gastmannschaft.
     * @return Das gefundene Spiel oder <code>null</code>, wenn kein Spiel
     *     mit diesem Bedingungen vorhanden.
     */
    public Game find(GameList round, Team homeTeam, Team guestTeam);
    
    /**
     * Sucht nach allen Spielpaarungen mit der beteiligten Heimmannschaft.
     *
     * @param homeTeam Die gesuchte Heimmannschaft.
     * @return Eine Liste von {@link de.winkler.betoffice.storage.Game}
     *     Objekten.
     */
    public List<Game> findByHomeTeam(Team homeTeam);

    /**
     * Sucht nach allen Spielpaarungen mit der beteiligten Gastmannschaft.
     *
     * @param guestTeam Die gesuchte Gastmannschaft.
     * @return Eine Liste von {@link de.winkler.betoffice.storage.Game}
     *     Objekten.
     */
    public List<Game> findByGuestTeam(Team guestTeam);

    /**
     * Sucht nach allen Spielpaarungen mit der beteiligten Heim- und
     * Gastmannschaft.
     *
     * @param homeTeam Die gesuchte Heimmannschaft.
     * @param guestTeam Die gesuchte Gastmannschaft.
     * @return Eine Liste von {@link de.winkler.betoffice.storage.Game}
     *     Objekten.
     */
    public List<Game> find(Team homeTeam, Team guestTeam);

    /**
     * Sucht nach allen Spielpaarungen mit den beteiligten Mannschaften.
     *
     * @param team1 Die erste gesuchte Mannschaft (Heim wie auswärts).
     * @param team2 Die zweite gesuchte Mannschaft (Heim wie auswärts).
     * @return Eine Liste von {@link de.winkler.betoffice.storage.Game}
     *     Objekten.
     */
    public List<Game> findAll(Team team1, Team team2);

    /**
     * Legt eine neue Spielpaarung persistent an.
     *
     * @param match Eine Spielpaarung.
     */
    public void save(Game match);

    /**
     * Legt mehrere neue Spielpaarungen an.
     *
     * @param matches Eine Liste von Spielpaarungen.
     */
    public void saveAll(List<Game> matches);

    /**
     * Eine Update-Operation.
     *
     * @param match Eine Spielpaarung.
     */
    public void update(Game match);

    /**
     * Löscht eine Spielpaarung.
     *
     * @param match Die zu löschende Spielpaarung.
     */
    public void delete(Game match);

    /**
     * Löscht alle übergebenen Spielpaarungen.
     *
     * @param matches Die zu Spielpaarungen.
     */
    public void deleteAll(List<Game> matches);

}
