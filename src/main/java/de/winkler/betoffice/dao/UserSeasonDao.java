/*
 * $Id: UserSeasonDao.java 3782 2013-07-27 08:44:32Z andrewinkler $
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

import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.UserSeason;

/**
 * Verwaltet die Zuordnung Meisterschaft und Tippteilnehmer.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3782 $ $LastChangedDate: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
public interface UserSeasonDao {

	/**
	 * Liefert alle teilnehmenden User einer Meisterschaft.
	 *
	 * @param season Die Meisterschaft.
	 * @return Eine Liste der Teilnehmer.
	 */
	public List<User> findUsers(Season season);

    /**
     * Legt einen neuen Teilnehmer an.
     *
     * @param userSeason Eine Meisterschaft-Teilnehmer Zuordnung.
     */
    public void save(UserSeason userSeason);

    /**
     * Speichert mehrere neue Teilnehmer.
     *
     * @param userSeasons Eine Liste von Usern/Meisterschaft.
     */
    public void saveAll(List<UserSeason> userSeasons);

    /**
     * Ein Update.
     *
     * @param userSeason Ein Teilnehmer/Meisterschaft.
     */
    public void update(UserSeason userSeason);

    /**
     * Löscht einen Teilnehmer.
     *
     * @param userSeason Ein Teilnehmer/Meisterschaft.
     */
    public void delete(UserSeason userSeason);

    /**
     * Löscht alle Teilnehmer.
     *
     * @param userSeasons Eine Liste von Usern/Meisterschaft.
     */
    public void deleteAll(List<UserSeason> userSeasons);

}
