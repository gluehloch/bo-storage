/*
 * $Id: UserDao.java 3782 2013-07-27 08:44:32Z andrewinkler $
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

import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.UserResult;

/**
 * DAO Klasse für den Zugriff auf {@link de.winkler.betoffice.storage.User}.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3782 $ $LastChangedDate: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
public interface UserDao {

    /**
     * Liefert eine Liste alle Teilnehmer.
     *
     * @return Eine Liste aller Teilnehmer.
     */
    public List<User> findAll();

    /**
     * Liefert einen Teilnehmer mit gesuchten Nickname.
     *
     * @param nickname Der gesuchte Nickname.
     * @return Ein Teilnehmer.
     */
    public User findByNickname(String nickname);

    /**
     * Legt einen neuen Teilnehmer an.
     *
     * @param user Ein neuer Teilnehmer.
     */
    public void save(User user);

    /**
     * Speichert mehrere neue Teilnehmer.
     *
     * @param users Eine Liste von Usern.
     */
    public void saveAll(List<User> users);

    /**
     * Ein Update.
     *
     * @param user Ein Teilnehmer
     */
    public void update(User user);

    /**
     * Löscht einen Teilnehmer.
     *
     * @param user Ein Teilnehmer.
     */
    public void delete(User user);

    /**
     * Löscht alle Teilnehmer.
     *
     * @param users Die Teilnehmer.
     */
    public void deleteAll(List<User> users);

    /**
     * Berechnet das Tipper-Ranking für eine Meisterschaft.
     *
     * @param users Diese Teilnehmer werden im Ergebnis erwartet.
     * @param season Die betreffende Meisterschaft.
     * @param startIndex Index des Spieltags ab dem gezählt wird (0..N-1).
     * @param endIndex Index des Spieltags bis zu dem gezählt wird (0..N-1).
     * @return Das Ranking der Tipper.
     */
    public List<UserResult> calculateUserRanking(List<User> users,
            Season season, int startIndex, int endIndex);

    /**
     * Berechnet das Tipper-Ranking für eine Meisterschaft.
     *
     * @param users Diese Teilnehmer werden im Ergebnis erwartet.
     * @param season Die betreffende Meisterschaft.
     * @return Das Ranking der Tipper.
     */
    public List<UserResult> calculateUserRanking(List<User> users, Season season);

    /**
     * Berechnet das Tipper-Ranking für einen Spieltag.
     *
     * @param users Diese Teilnehmer werden im Ergebnis erwartet.
     * @param round Der betreffende Spieltag.
     * @return Das Ranking der Tipper.
     */
    public List<UserResult> calculateUserRanking(List<User> users,
            GameList round);

}
