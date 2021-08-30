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

import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.UserResult;

/**
 * DAO Klasse für den Zugriff auf {@link de.winkler.betoffice.storage.User}.
 *
 * @author by Andre Winkler
 */
public interface UserDao extends CommonDao<User> {

    /**
     * A list of users ordered by nickname.
     * 
     * @return a list of users
     */
    List<User> findAll();

    List<User> findAll(Pageable page);

    /**
     * Liefert einen Teilnehmer mit gesuchten Nickname.
     *
     * @param  nickname Der gesuchte Nickname.
     * @return          Ein Teilnehmer.
     */
    Optional<User> findByNickname(String nickname);

    /**
     * Berechnet das Tipper-Ranking für eine Meisterschaft.
     *
     * @param  users      Diese Teilnehmer werden im Ergebnis erwartet.
     * @param  season     Die betreffende Meisterschaft.
     * @param  startIndex Index des Spieltags ab dem gezählt wird (0..N-1).
     * @param  endIndex   Index des Spieltags bis zu dem gezählt wird (0..N-1).
     * @return            Das Ranking der Tipper.
     */
    List<UserResult> calculateUserRanking(List<User> users, Season season, int startIndex, int endIndex);

    /**
     * Berechnet das Tipper-Ranking für eine Meisterschaft.
     *
     * @param  users  Diese Teilnehmer werden im Ergebnis erwartet.
     * @param  season Die betreffende Meisterschaft.
     * @return        Das Ranking der Tipper.
     */
    List<UserResult> calculateUserRanking(List<User> users, Season season);

    /**
     * Berechnet das Tipper-Ranking für einen Spieltag.
     *
     * @param  users Diese Teilnehmer werden im Ergebnis erwartet.
     * @param  round Der betreffende Spieltag.
     * @return       Das Ranking der Tipper.
     */
    List<UserResult> calculateUserRanking(List<User> users, GameList round);

}
