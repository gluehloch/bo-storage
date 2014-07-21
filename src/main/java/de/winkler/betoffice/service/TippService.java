/*
 * $Id: TippService.java 3849 2013-11-29 21:36:13Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2013 by Andre Winkler. All rights reserved.
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

package de.winkler.betoffice.service;

import java.util.List;

import org.joda.time.DateTime;

import de.winkler.betoffice.mail.MailContentDetails;
import de.winkler.betoffice.mail.TippMailParameter;
import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.GameResult;
import de.winkler.betoffice.storage.GameTipp;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.enums.TippStatusType;

/**
 * Allocates a service for adding, updating and removing tipps. A special method
 * for evaluating Tipp mails.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3849 $ $LastChangedDate: 2013-11-29 22:36:13 +0100 (Fr, 29 Nov 2013) $
 */
public interface TippService {

    /**
     * Sends the tipp mail (xml for betoffice and plain text as confirmation
     * for the user.
     *
     * @param tippMailParameter the mail parameters
     * @param user the user who created the tipp
     */
    public void sendMailTipp(User user, TippMailParameter tippMailParameter);

    /**
     * Startet die Mail-Auswertung. Es wird angenommen, daß eine Tipp-Mail
     * hier übergeben wird.
     *
     * @param season Die Meisterschaft, der die Mail zugeordnet wird.
     * @param mail Eine Tipp-Mail.
     */
    public void evaluateMailTipp(Season season, MailContentDetails mail);

    /**
     * Einen Tipp einer Spielpaarung hinzufügen/aktualisieren.
     *
     * @param match Die betreffende Partie.
     * @param user Der Tipper.
     * @param gr Das getippte Endergebnis
     * @param status Tipp-Status.
     * @return Der erstellte {@link GameTipp}.
     *
     * @see #updateTipp(Game, User, GameResult, TippStatusType)
     */
    public GameTipp addTipp(Game match, User user, GameResult gr,
            TippStatusType status);

    /**
     * Legt die Tipps für einen kompletten Spieltag in der Datenbank an.
     *
     * @param round Der betreffende Spieltag.
     * @param user Der Tipper.
     * @param tipps Die Tipps für alle Spiele des Spieltags.
     * @param status Der Status für diese Tipps.
     */
    public void addTipp(GameList round, User user, List<GameResult> tipps,
            TippStatusType status);

    /**
     * Einen Tipp einer Spielpaarung hinzufügen/aktualisieren.
     *
     * @param match Die betreffende Partie.
     * @param user Der Tipper.
     * @param gr Das getippte Endergebnis
     * @param status Tipp-Status.
     *
     * @see #addTipp(Game, User, GameResult, TippStatusType)
     */
    public void updateTipp(Game match, User user, GameResult gr,
            TippStatusType status);

    /** 
     * Aktualisiert eine Liste von Tipps.
     *
     * @param tipps Eine Liste mit den zu aktualisierende Tipps.
     */
    public void updateTipp(List<GameTipp> tipps);

    /**
     * Entfernt einen Spieltipp.
     *
     * @param match Die betreffende Spielpaarung.
     * @param user Der Tipp-Teilnehmer dessen Tipp entfernt werden soll.
     */
    public void removeTipp(Game match, User user);

    /**
     * Liefert alle Spieltipps zu einer Spielpaarung.
     *
     * @param match Die Spielpaarung deren Spieltipps gesucht werden.
     * @return Die Spieltipps.
     */
    public List<GameTipp> findTippsByMatch(Game match);

    /**
     * Liefert alle Spieltipps zu einem Spieltag von einem Teilnehmer.
     *
     * @param round Der Spieltag der für die Suche herangezogen werden soll.
     * @param user Die Spieltipps von diesem User suchen.
     * @return Die Spieltipps.
     */
    public List<GameTipp> findTippsByRoundAndUser(GameList round, User user);

    /**
     * Liefert alle Spieltipp zu einem Spieltag von einem Teilnehmer.
     *
     * @param seasonId Die betreffende Spielzeit
     * @param roundId Die ID des Spieltags
     * @param nickname Der Nickname des Tippers
     * @return Eine List mit allen Spieltipps für einen Tipper
     */
    public List<GameTipp> findTippsByRoundAndUser(long seasonId, long roundId,
            String nickname);

    /**
     * Ermittelt den naechsten zu tippenden Spieltag.
     *
     * @param season Die Bezugsmeisterschaft
     * @param date Das Bezugsdatum
     * @return Der naechste zu tippende Spieltag
     */
    public GameList findNextTippRound(Season season, DateTime date);
}
