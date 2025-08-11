/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2024 by Andre Winkler. All
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

package de.betoffice.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import de.betoffice.storage.season.entity.Game;
import de.betoffice.storage.season.entity.GameList;
import de.betoffice.storage.season.entity.GameResult;
import de.betoffice.storage.tip.GameTipp;
import de.betoffice.storage.tip.TippDto;
import de.betoffice.storage.tip.TippStatusType;
import de.betoffice.storage.tip.UserResultOfDay;
import de.betoffice.storage.user.entity.User;

/**
 * Allocates a service for adding, updating and removing tipps. A special method for evaluating Tipp mails.
 *
 * @author by Andre Winkler
 */
public interface TippService {

    /**
     * Berechnet die Spieltagspunkte fuer einen Teilnehmer.
     * 
     * @param  round Die Runde, die berechnet werden soll.
     * @param  user  Der Teilnehmer, dessen Punktestand ermittelt werden soll.
     * @return       Der errechnete Punktestand.
     */
    UserResultOfDay getUserPoints(GameList round, User user);

    /**
     * Einen Tipp einer Spielpaarung hinzufügen/aktualisieren. <b>ACHTUNG:</b> Es findet keine Prüfung statt, ob der
     * Tipp vor Spielbeginn abgegeben wurde.
     *
     * @param  token  Das Anmeldetoken mit dem dieser Tipp angelegt wird.
     * @param  match  Die betreffende Partie.
     * @param  user   Der Tipper.
     * @param  tipp   Das getippte Endergebnis
     * @param  status Tipp-Status.
     * @return        Der erstellte {@link GameTipp}.
     */
    GameTipp createOrUpdateTipp(String token, Game match, User user, GameResult tipp, TippStatusType status);

    /**
     * Legt die Tipps für einen kompletten Spieltag in der Datenbank an. <b>ACHTUNG:</b> Es findet keine Prüfung statt,
     * ob der Tipp vor Spielbeginn abgegeben wurde.
     *
     * @param  token  Das Anmeldetoken mit dem dieser Tipp angelegt wird.
     * @param  round  Der betreffende Spieltag.
     * @param  user   Der Tipper.
     * @param  tipps  Die Tipps für alle Spiele des Spieltags.
     * @param  status Der Status für diese Tipps.
     * @return        Die erstellen {@code GameTipps}.
     */
    List<GameTipp> createOrUpdateTipp(String token, GameList round, User user, List<GameResult> tipps,
            TippStatusType status);

    /**
     * Legt den Tipp für einen Spieler für einen kompletten Spieltag an.
     * 
     * <b>ACHTUNG:</b> Es findet eine PRÜFUNG statt, ob der Tipp VOR Spielbeginn abgegeben wurde.
     * 
     * @param  tippDto Der Spieltipp
     * @return         Die erstellen {@code GameTipps}.
     */
    List<GameTipp> validateKickOffTimeAndAddTipp(TippDto tippDto);

    /**
     * Liefert alle Spieltipps zu einer Spielpaarung.
     *
     * @param  match Die Spielpaarung deren Spieltipps gesucht werden.
     * @return       Die Spieltipps.
     */
    List<GameTipp> findTipps(Game match);

    /**
     * Liefert den Spieltipp einer Spielers zu einer Spielpaarung.
     * 
     * @param  game Das Spiel
     * @param  user Der Teilnehmer
     * @return      Der Spieltipp zu den gesuchten Parametern
     */
    Optional<GameTipp> findTipp(Game game, User user);

    /**
     * Liefert alle Spieltipps zu einem Spieltag von einem Teilnehmer.
     *
     * @param  round Der Spieltag der für die Suche herangezogen werden soll.
     * @param  user  Die Spieltipps von diesem User suchen.
     * @return       Die Spieltipps.
     */
    default List<GameTipp> findTipps(GameList round, User user) {
        return findTipps(round.getId(), user.getId());
    }

    /**
     * Liefert alle Spieltipps zu einem Spieltag von einem Teilnehmer.
     *
     * @param  roundId Der Spieltag der für die Suche herangezogen werden soll.
     * @param  userId  Die Spieltipps von diesem User suchen.
     * @return         Die Spieltipps.
     */
    List<GameTipp> findTipps(long roundId, long userId);

    /**
     * Liefert alle Spieltipps zu einem Spieltag.
     * 
     * @param  roundId Der gesuchte Spieltag.
     * @return         Die Spieltipps.
     */
    List<GameTipp> findTipps(long roundId);

    /**
     * Ermittelt den naechsten zu tippenden Spieltag.
     *
     * @param  date Das Bezugsdatum
     * @return      Der naechste zu tippende Spieltag
     */
    Optional<GameList> findNextTippRound(ZonedDateTime date);

    /**
     * Ermittelt den naechsten zu tippenden Spieltag.
     *
     * @param  seasonId Die Bezugsmeisterschaft
     * @param  date     Das Bezugsdatum
     * @return          Der naechste zu tippende Spieltag
     */
    Optional<GameList> findNextTippRound(long seasonId, ZonedDateTime date);

    /**
     * Ermittelt den letzten zu tippenden Spieltag.
     *
     * @param  seasonId Die Bezugsmeisterschaft
     * @param  date     Das Bezugsdatum
     * @return          Der naechste zu tippende Spieltag
     */
    Optional<GameList> findPreviousTippRound(long seasonId, ZonedDateTime date);

}
