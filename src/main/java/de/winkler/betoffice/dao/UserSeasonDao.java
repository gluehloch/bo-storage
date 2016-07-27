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

import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.UserSeason;

/**
 * Verwaltet die Zuordnung Meisterschaft und Tippteilnehmer.
 *
 * @author by Andre Winkler
 */
public interface UserSeasonDao extends CommonDao<UserSeason> {

	/**
	 * Liefert alle teilnehmenden User einer Meisterschaft.
	 *
	 * @param season Die Meisterschaft.
	 * @return Eine Liste der Teilnehmer.
	 */
	List<User> findUsers(Season season);

    /**
     * Legt einen neuen Teilnehmer an.
     *
     * @param userSeason Eine Meisterschaft-Teilnehmer Zuordnung.
     */
    void save(UserSeason userSeason);

    /**
     * Speichert mehrere neue Teilnehmer.
     *
     * @param userSeasons Eine Liste von Usern/Meisterschaft.
     */
    void saveAll(List<UserSeason> userSeasons);

    /**
     * Ein Update.
     *
     * @param userSeason Ein Teilnehmer/Meisterschaft.
     */
    void update(UserSeason userSeason);

    /**
     * Löscht einen Teilnehmer.
     *
     * @param userSeason Ein Teilnehmer/Meisterschaft.
     */
    void delete(UserSeason userSeason);

    /**
     * Löscht alle Teilnehmer.
     *
     * @param userSeasons Eine Liste von Usern/Meisterschaft.
     */
    void deleteAll(List<UserSeason> userSeasons);

}
