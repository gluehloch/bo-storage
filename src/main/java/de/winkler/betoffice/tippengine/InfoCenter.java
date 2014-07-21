/*
 * $Id: InfoCenter.java 3782 2013-07-27 08:44:32Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2010 by Andre Winkler. All rights reserved.
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

package de.winkler.betoffice.tippengine;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.Validate;

import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.GameResult;
import de.winkler.betoffice.storage.GameTipp;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.UserResultOfDay;
import de.winkler.betoffice.storage.enums.TippStatusType;

/**
 * Eine Helferklasse. Hier befinden sich die Methoden um den besten,
 * schlechtesten und den Durchschnittstipp zu ermitteln.
 *
 * @author   $Author: andrewinkler $
 * @version  $Revision: 3782 $ $Date: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
public final class InfoCenter {

    /** Utility Klasse. */
    protected InfoCenter() {
    }

    /**
     * Ermittelt den besten abgegebenen Tipp des Spieltages.
     *
     * @param gamelist Der auszuwertende Spieltag.
     * @param users Die zu bewertenden Teilnehmer.
     * @return Der Spieler, der den besten Tipp abgegeben hat. Oder
     *     <code>null</code>, wenn eine leere Teilnehmerliste übergeben wurde.
     * @throws IllegalArgumentException Falls gamelist gleich null.
     */
    public static User getMaxTipp(final GameList gamelist,
            final List<User> users) {
        Validate.notNull(gamelist);
        Validate.notNull(users);

        UserResultOfDay max = null;
        for (Iterator<User> i = users.iterator(); i.hasNext();) {
            User user = (User) i.next();

            if (!user.isExcluded()) {
                UserResultOfDay urod = gamelist.getUserPoints(user);

                if (max == null) {
                    max = urod;
                } else if (urod.getPoints() > max.getPoints()) {
                    max = urod;
                }
            }
        }
        return max.getUser();
    }

    /**
     * Ermittelt den schlechtesten abgegebenen Tipp des Spieltags. Nur Tipps,
     * die von menschlichen Teilnehmern abgegeben wurden, werden zur Berechnung
     * herangezogen.
     *
     * @param gamelist Der auszuwertende Spieltag.
     * @param users Die zu bewertenden Teilnehmer.
     * @return Der Tagestipp, der den schlechtesten Tipp abgegeben hat. Kann
     *      0 sein, wenn kein 'echter', gültiger Tagestipp vorhanden ist.
     */
    public static UserResultOfDay getMinTipp(final GameList gamelist,
            final List<User> users) {

        Validate.notNull(gamelist);
        Validate.notNull(users);

        UserResultOfDay min = null;

        for (Iterator<User> i = users.iterator(); i.hasNext();) {
            User user = (User) i.next();

            // Nur User, die Aktiv geschaltet sind.
            if (!user.isExcluded()) {
                UserResultOfDay urod = gamelist.getUserPoints(user);

                // Muss ein Tipp sein, der nicht automatisch generiert wurde.
                if (TippStatusType.USER.equals(urod.getStatus())) {
                    if (min == null) {
                        min = urod;
                    } else if (urod.getIsTipped()) {
                        if (urod.getPoints() < min.getPoints()) {
                            min = urod;
                        }
                    }
                }
            }
        }
        return min;
    }

    /**
     * Ermittelt den Mitteltipp für ein Spiel. Vorausgesetzt, es sind
     * Tipps für das Spiel vorhanden. Zur Berechnung werden nur USER
     * Tipps herangezogen.
     *
     * @param game Das Spiel für den der Mitteltipp erzeugt werden soll.
     * @return Der Mitteltipp. null wenn keine Tipps vorhanden,
     *     sonst den Mitteltipp.
     * @throws IllegalArgumentException game ist gleich <null>.
     */
    public static GameResult getMediumTipp(final Game game) {
        Validate.notNull(game, "game ist null");

        int homeGoals = 0;
        int guestGoals = 0;
        int counter = 0;
        int tipps = game.tippSize();

        for (int n = 0; n < tipps; n++) {
            GameTipp tipp = game.getGameTipp(n);

            if (TippStatusType.USER.equals(tipp.getStatus())) {
                counter++;
                homeGoals += tipp.getTipp().getHomeGoals();
                guestGoals += tipp.getTipp().getGuestGoals();
            }
        }

        if (counter != 0) {
            return new GameResult(Math.round(homeGoals / counter), Math
                .round(guestGoals / counter));
        } else {
            return null;
        }
    }

}
