/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2020 by Andre Winkler. All
 * rights reserved.
 * ============================================================================
 * GNU GENERAL  LICENSE TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND
 * MODIFICATION
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General  License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General  License for more
 * details.
 * 
 * You should have received a copy of the GNU General  License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

package de.winkler.betoffice.dao;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.Group;
import de.winkler.betoffice.storage.Season;

/**
 * DAO für das persistente Verhalten von Spieltagen.
 *
 * @author by Andre Winkler
 */
public interface RoundDao extends CommonDao<GameList> {

    /**
     * Liefert alle Spieltage einer Meisterschaft. Die Spiele der Spieltage werden nicht geladen.
     *
     * @param  season Die Meisterschaft dessen Spieltage gesucht werden.
     * @return        Die Spieltage der gesuchten Meisterschaft.
     */
    List<GameList> findRounds(Season season);

    /**
     * Liefert alle Spieltage inklusive der Spiele einer Gruppe. Falls keine Spiele vorliegen, werden keine Spieltage
     * gefunden.
     * 
     * @param  group Die Gruppe
     * @return       Die Spieltage dieser Gruppe
     */
    List<GameList> findRounds(Group group);

    /**
     * Liefert einen Spieltag einer Meisterschaft. Die Spiele des Spieltags werden ebenfalls geladen.
     *
     * @param  season Die betreffende Meisterschaft.
     * @param  index  Der Index des Spieltags (0 .. size-1).
     * @return        Der Spieltag.
     */
    Optional<GameList> findRound(Season season, int index);

    /**
     * Sucht nach einem Spieltag zu dem uerbgebenen Spiel. Die Spiele werden nicht geladen.
     * 
     * @param  game Das Spiel.
     * @return      Der Spieltag zu dem dieses Spiel gehört.
     */
    Optional<GameList> findRoundByGame(Game game);

    /**
     * Liefert einen Spieltag einer Meisterschaft mit allen Spielen.
     * 
     * @see            #findRound(Season, int)
     * @param  roundId Die ID des Spieltags
     * @return         Der Spieltag
     */
    Optional<GameList> findRound(long roundId);

    /**
     * Liefert den nächsten Spieltag der Meisterschaft.
     * 
     * @param  id Die ID des aktuellen Spieltags.
     * @return    Der nächste Spieltag.
     */
    Optional<Long> findNext(long id);

    /**
     * Liefert den vorhergehenden Spieltag der Meisterschaft.
     * 
     * @param  id Die ID des aktuellen Spieltags.
     * @return    Der vorhergehende Spieltag.
     */
    Optional<Long> findPrevious(long id);

    /**
     * Liefert das nächste Spieltagsdatum (über alle Meisterschaften).
     * 
     * @param  seasonId Die ID der Meisterschaft.
     * @param  date     Das Bezugsdatum.
     * @return          Der nächste Spieltag.
     */
    ZonedDateTime findNearestGame(ZonedDateTime date);

    /**
     * Liefert das passende Spiel zu einem Datum.
     * 
     * @param  date Das Bezugsdatum.
     * @return      Das Spiel zu diesem Datum.
     */
    List<Game> findGames(ZonedDateTime date);

    /**
     * Liefert den nächsten zu tippenden Spieltag.
     *
     * @param  seasonId Die betreffende Meisterschaft.
     * @param  date     Das Bezugsdatum.
     * @return          Der nächste zu tippende Spieltag.
     */
    Optional<Long> findNextTippRound(long seasonId, ZonedDateTime date);

    /**
     * Liefert den letzten zu tippenden Spieltag.
     * 
     * @param  seasonId Die betreffende Meisterschaft
     * @param  date     Das Bezugsdatum
     * @return          Der letzte zu tippende Spieltag.
     */
    Optional<Long> findLastTippRound(long seasonId, ZonedDateTime date);

    /**
     * Liefert die letzte Runde einer Meisterschaft-
     * 
     * @param  season Die betreffende Meisterschaft
     * @return        der letzte Spieltag der Meisterschaft
     */
    Optional<GameList> findLastRound(Season season);

    /**
     * Liefert die erste Runde einer Meisterschaft.
     * 
     * @param  season Die betrefffende Meisterschaft
     * @return        der erste Spieltag der Meisterschaft
     */
    Optional<GameList> findFirstRound(Season season);

}
