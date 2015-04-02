/*
 * $Id: RoundDao.java 3850 2013-11-30 18:24:08Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2007 by Andre Winkler. All
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

package de.winkler.betoffice.dao;

import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.Season;

import java.util.List;

import org.joda.time.DateTime;

/**
 * DAO für das persistente Verhalten von Spieltagen.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3850 $ $LastChangedDate: 2013-11-30 19:24:08
 *          +0100 (Sa, 30 Nov 2013) $
 */
public interface RoundDao {

    /**
     * Liefert alle Spieltage einer Meisterschaft.
     *
     * @param season
     *            Die Meisterschaft dessen Spieltage gesucht werden.
     * @return Die Spieltage der gesuchten Meisterschaft.
     */
    public List<GameList> findRounds(Season season);

    /**
     * Liefert einen Spieltag einer Meisterschaft.
     *
     * @param season
     *            Die betreffende Meisterschaft.
     * @param index
     *            Der Index des Spieltags (0 .. size-1).
     * @return Der Spieltag.
     */
    public GameList findRound(Season season, int index);

    /**
     * Liefert einen Spieltag einer Meisterschaft.
     *
     * @param id
     *            Die ID des Spieltags.
     * @return Der Spieltag
     */
    public GameList findById(long id);

    /**
     * Liefert den nächsten Spieltag der Meisterschaft.
     * 
     * @param id
     *            Die ID des aktuellen Spieltags.
     * @return Der nächste Spieltag.
     */
    public Long findNext(long id);

    /**
     * Liefert den vorhergehenden Spieltag der Meisterschaft.
     * 
     * @param id
     *            Die ID des aktuellen Spieltags.
     * @return Der vorhergehende Spieltag.
     */
    public Long findPrevious(long id);

    /**
     * Liefert einen Spieltag einer Meisterschaft inklusive aller abhöngigen
     * Objekte, wie Spieltipps, Tippteilnehmer, Gruppe, Mannschaften und
     * natürlich der Spiele.
     *
     * @param season
     *            Die betreffende Meisterschaft.
     * @param index
     *            Der Index des Spieltags (0 .. size-1).
     * @return Der Spieltag.
     */
    public GameList findAllRoundObjects(Season season, int index);

    /**
     * Legt eine neuen Spieltag persistent an.
     *
     * @param round
     *            Ein Spieltag.
     */
    public void save(GameList round);

    /**
     * Legt mehrere neue Spieltage an.
     *
     * @param rounds
     *            Eine Liste von Spieltagen.
     */
    public void saveAll(List<GameList> rounds);

    /**
     * Eine Update-Operation.
     *
     * @param round
     *            Ein Spieltag.
     */
    public void update(GameList round);

    /**
     * Löscht einen Spieltag.
     *
     * @param round
     *            Der zu löschende Spieltag.
     */
    public void delete(GameList round);

    /**
     * Löscht alle übergebenen Spieltage.
     *
     * @param rounds
     *            Die zu löschenden Spieltage.
     */
    public void deleteAll(List<GameList> rounds);

    /**
     * Liefert den nächsten zu tippenden Spieltag.
     *
     * @param season
     *            Die Bezugsmeisterschaft.
     * @param date
     *            Das Bezugsdatum.
     * @return Der nächste zu tippende Spieltag.
     */
    public Long findNextTippRound(Season season, DateTime date);

}
