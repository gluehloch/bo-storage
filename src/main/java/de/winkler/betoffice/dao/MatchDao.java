/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2024 by Andre Winkler. All rights reserved.
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
import de.winkler.betoffice.storage.Team;

/**
 * Dieses DAO sucht nach bestimmten Spielpaarungen.
 *
 * @author by Andre Winkler
 */
public interface MatchDao extends CommonDao<Game> {

    /**
     * Sucht nach einer bestimmten Spielpaarung für einen Spieltag.
     *
     * @param  round     Der zu untersuchende Spieltag.
     * @param  homeTeam  Die Heimmannschaft.
     * @param  guestTeam Die Gastmannschaft.
     * @return           Das gefundene Spiel oder <code>null</code>, wenn kein Spiel mit diesem Bedingungen vorhanden.
     */
    Optional<Game> find(GameList round, Team homeTeam, Team guestTeam);

    /**
     * Sucht nach allen Spielen zu einem Spieltag.
     * 
     * @param  round Der Spieltag
     * @return       Die Spiele an diesem Spieltag.
     */
    List<Game> find(GameList round);

    /**
     * Sucht nach allen Spielpaarungen mit der beteiligten Heimmannschaft.
     *
     * @param  homeTeam Die gesuchte Heimmannschaft.
     * @param  limit    Maximal Anzahl an Spielpaarungen
     * @return          Eine Liste von {@link de.winkler.betoffice.storage.Game} Objekten.
     */
    List<Game> findByHomeTeam(Team homeTeam, int limit);

    /**
     * Sucht nach allen Spielpaarungen mit der beteiligten Gastmannschaft.
     *
     * @param  guestTeam Die gesuchte Gastmannschaft.
     * @return           Eine Liste von {@link de.winkler.betoffice.storage.Game} Objekten.
     */
    List<Game> findByGuestTeam(Team guestTeam);

    /**
     * Sucht nach allen Spielpaarungen mit der beteiligten Heim- und Gastmannschaft.
     *
     * @param  homeTeam  Die gesuchte Heimmannschaft.
     * @param  guestTeam Die gesuchte Gastmannschaft.
     * @return           Eine Liste von {@link de.winkler.betoffice.storage.Game} Objekten.
     */
    List<Game> find(Team homeTeam, Team guestTeam);

    /**
     * Sucht nach allen Spielpaarungen mit den beteiligten Mannschaften.
     *
     * @param  team1 Die erste gesuchte Mannschaft (Heim wie auswärts).
     * @param  team2 Die zweite gesuchte Mannschaft (Heim wie auswärts).
     * @return       Eine Liste von {@link de.winkler.betoffice.storage.Game} Objekten.
     */
    List<Game> findAll(Team team1, Team team2);

    /**
     * Sucht nach alle Spielpaarungen mit der beteiligten Mannschaften.
     * 
     * @param  team Die gesuchte Mannschaft
     * @return      Eine Liste von {@link de.winkler.betoffice.storage.Game} Objekten.
     */
    List<Game> find(Team team);

}
