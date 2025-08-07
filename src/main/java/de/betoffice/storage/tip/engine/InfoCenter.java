/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2020 by Andre Winkler. All rights reserved.
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

package de.betoffice.storage.tip.engine;

import java.util.List;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.betoffice.service.TippService;
import de.betoffice.storage.season.entity.Game;
import de.betoffice.storage.season.entity.GameList;
import de.betoffice.storage.season.entity.GameResult;
import de.betoffice.storage.tip.GameTipp;
import de.betoffice.storage.tip.TippStatusType;
import de.betoffice.storage.tip.UserResultOfDay;
import de.betoffice.storage.user.entity.User;

/**
 * Hier befinden sich die Methoden um den besten, schlechtesten und den Durchschnittstipp zu ermitteln.
 *
 * @author Andre Winkler
 */
@Service
public final class InfoCenter {

    @Autowired
    private TippService tippService;

    /**
     * Ermittelt den besten abgegebenen Tipp des Spieltages.
     *
     * @param gamelist
     *            Der auszuwertende Spieltag.
     * @param users
     *            Die zu bewertenden Teilnehmer.
     * @return Der Spieler, der den besten Tipp abgegeben hat. Oder
     *         <code>null</code>, wenn eine leere Teilnehmerliste übergeben wurde.
     * @throws IllegalArgumentException
     *             Falls gamelist gleich null.
     */
    public UserResultOfDay findBestTipp(GameList gamelist, List<User> users) {
        Validate.notNull(gamelist);
        Validate.notNull(users);

        UserResultOfDay max = null;
        for (User user : users) {
            if (!user.isExcluded()) {
                UserResultOfDay points = tippService.getUserPoints(gamelist, user);

                if (max == null) {
                    max = points;
                } else if (points.getPoints() > max.getPoints()) {
                    max = points;
                }
            }
        }
        return max;
    }

    /**
     * Ermittelt den schlechtesten abgegebenen Tipp des Spieltags. Nur Tipps, die
     * von menschlichen Teilnehmern abgegeben wurden, werden zur Berechnung
     * herangezogen.
     *
     * @param gamelist
     *            Der auszuwertende Spieltag.
     * @param users
     *            Die zu bewertenden Teilnehmer.
     * @return Der Tagestipp, der den schlechtesten Tipp abgegeben hat. Kann 0 sein,
     *         wenn kein 'echter', gültiger Tagestipp vorhanden ist.
     */
    public UserResultOfDay findWorstTipp(GameList gamelist, List<User> users) {
        Validate.notNull(gamelist);
        Validate.notNull(users);

        UserResultOfDay min = null;

        for (User user : users) {
            // Nur User, die Aktiv geschaltet sind.
            if (!user.isExcluded()) {
                UserResultOfDay urod = tippService.getUserPoints(gamelist, user);

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
     * Ermittelt den Mitteltipp für ein Spiel. Vorausgesetzt, es sind Tipps für das
     * Spiel vorhanden. Zur Berechnung werden nur USER Tipps herangezogen.
     *
     * @param game
     *            Das Spiel für den der Mitteltipp erzeugt werden soll.
     * @param users
     *            Die zu bewertenden Teilnehmer.
     * @return Der Mitteltipp. null wenn keine Tipps vorhanden, sonst den
     *         Mitteltipp.
     */
    public GameResult findMediumTipp(Game game, List<User> users) {
        Validate.notNull(game, "game ist null");

        int homeGoals = 0;
        int guestGoals = 0;
        int counter = 0;

        List<GameTipp> tipps = tippService.findTipps(game);

        for (GameTipp tipp : tipps) {
            if (TippStatusType.USER.equals(tipp.getStatus())) {
                counter++;
                homeGoals += tipp.getTipp().getHomeGoals();
                guestGoals += tipp.getTipp().getGuestGoals();
            }
        }

        if (counter != 0) {
            return new GameResult(Math.round(homeGoals / counter), Math.round(guestGoals / counter));
        } else {
            return null;
        }
    }

}
