/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2015 by Andre Winkler. All
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

package de.winkler.betoffice.service;

import java.util.List;

import org.joda.time.DateTime;

import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.GameResult;
import de.winkler.betoffice.storage.GameTipp;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.enums.TippStatusType;

/**
 * Allocates a service for adding, updating and removing tipps. A special method
 * for evaluating Tipp mails.
 *
 * @author by Andre Winkler
 */
public interface TippService {

    /**
     * Einen Tipp einer Spielpaarung hinzufügen/aktualisieren.
     *
     * @param token
     *            Das Anmeldetoken mit dem dieser Tipp angelegt wird.
     * @param match
     *            Die betreffende Partie.
     * @param user
     *            Der Tipper.
     * @param gr
     *            Das getippte Endergebnis
     * @param status
     *            Tipp-Status.
     * @return Der erstellte {@link GameTipp}.
     *
     * @see #updateTipp(Game, User, GameResult, TippStatusType)
     */
    public GameTipp addTipp(String token, Game match, User user, GameResult gr,
            TippStatusType status);

    /**
     * Legt die Tipps für einen kompletten Spieltag in der Datenbank an.
     *
     * @param token
     *            Das Anmeldetoken mit dem dieser Tipp angelegt wird.
     * @param round
     *            Der betreffende Spieltag.
     * @param user
     *            Der Tipper.
     * @param tipps
     *            Die Tipps für alle Spiele des Spieltags.
     * @param status
     *            Der Status für diese Tipps.
     */
    public void addTipp(String token, GameList round, User user,
            List<GameResult> tipps, TippStatusType status);

    /**
     * Legt den Tipp für einen Spieler für einen kompletten Spieltag an.
     * 
     * @param tippDto
     *            Der Spieltipp
     */
    public void addTipp(TippDto tippDto);

    /**
     * Einen Tipp einer Spielpaarung hinzufügen/aktualisieren.
     *
     * @param token
     *            Das Anmeldetoken mit dem dieser Tipp angelegt wird.
     * @param match
     *            Die betreffende Partie.
     * @param user
     *            Der Tipper.
     * @param gr
     *            Das getippte Endergebnis
     * @param status
     *            Tipp-Status.
     *
     * @see #addTipp(Game, User, GameResult, TippStatusType)
     */
    public void updateTipp(String token, Game match, User user, GameResult gr,
            TippStatusType status);

    /**
     * Aktualisiert eine Liste von Tipps.
     *
     * @param token
     *            Das Anmeldetoken mit dem dieser Tipp angelegt wird.
     * @param tipps
     *            Eine Liste mit den zu aktualisierende Tipps.
     */
    public void updateTipp(String token, List<GameTipp> tipps);

    /**
     * Entfernt einen Spieltipp.
     *
     * @param match
     *            Die betreffende Spielpaarung.
     * @param user
     *            Der Tipp-Teilnehmer dessen Tipp entfernt werden soll.
     */
    public void removeTipp(Game match, User user);

    /**
     * Liefert alle Spieltipps zu einer Spielpaarung.
     *
     * @param match
     *            Die Spielpaarung deren Spieltipps gesucht werden.
     * @return Die Spieltipps.
     */
    public List<GameTipp> findTippsByMatch(Game match);

    /**
     * Liefert alle Spieltipps zu einem Spieltag von einem Teilnehmer.
     *
     * @param round
     *            Der Spieltag der für die Suche herangezogen werden soll.
     * @param user
     *            Die Spieltipps von diesem User suchen.
     * @return Die Spieltipps.
     */
    public List<GameTipp> findTippsByRoundAndUser(GameList round, User user);

    /**
     * Liefert alle Spieltipps zu einem Spieltag von einem Teilnehmer.
     *
     * @param round
     *            Der Spieltag der für die Suche herangezogen werden soll.
     * 
     * @param user
     *            Die Spieltipps von diesem User suchen.
     * 
     * @return Die Spieltipps.
     */
    public GameList findTipp(GameList round, User user);

    /**
     * Liefert alle Spieltipps zu einem Spieltag von einem Teilnehmer.
     *
     * @param round
     *            Der Spieltag der für die Suche herangezogen werden soll.
     * 
     * @param user
     *            Die Spieltipps von diesem User suchen.
     * 
     * @return Die Spieltipps.
     */
    public GameList findTipp(long roundId, long userId);

    /**
     * Ermittelt den naechsten zu tippenden Spieltag.
     *
     * @param seasonId
     *            Die Bezugsmeisterschaft
     * @param date
     *            Das Bezugsdatum
     * @return Der naechste zu tippende Spieltag
     */
    public GameList findTippRound(long seasonId, DateTime date);

}
