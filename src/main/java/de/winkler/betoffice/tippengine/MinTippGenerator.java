/*
 * $Id: MinTippGenerator.java 3782 2013-07-27 08:44:32Z andrewinkler $
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

import org.slf4j.Logger;

import de.awtools.basic.LoggerFactory;
import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.GameResult;
import de.winkler.betoffice.storage.GameTipp;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.UserResultOfDay;
import de.winkler.betoffice.storage.enums.TippStatusType;
import de.winkler.betoffice.storage.exception.StorageObjectNotFoundException;

/**
 * Klasse zur Generierung eines Min-Tipps für mehrere User.
 * Es wird der schlechteste Tipp des Spieltages ermittelt und dieser
 * dann dem User zugewiesen.
 * 
 * @author   $Author: andrewinkler $
 * @version  $Revision: 3782 $ $Date: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
public class MinTippGenerator implements TippGenerator {

    /** Der private Logger der Klasse. */
    private final Logger log = LoggerFactory.make();

    public void generateTipp(final Season season) {
        for (int i = 0; i < season.unmodifiableGameList().size(); i++) {
            generateTipp(season.getGamesOfDay(i));
        }
    }

    public void generateTipp(final GameList round) {
        List<User> users = round.getSeason().getUsers();
        for (Iterator<User> i = users.iterator(); i.hasNext();) {
            User user = (User) i.next();
            if (!user.isExcluded() && !user.isAutomat()) {
                generateTipp(round, user, users);
            }
        }
    }

    private void generateTipp(final GameList round, final User user,
            final List<User> users) {

        // Den schwächsten Tipper des Spieltages ermitteln.
        UserResultOfDay minUser = InfoCenter.getMinTipp(round, users);

        if (log.isDebugEnabled()) {
            log.debug(new StringBuffer("Min-Tipp von User: ").append(minUser)
                .toString());
        }

        List<Game> games = round.unmodifiableList();

        for (Iterator<Game> i = games.iterator(); i.hasNext();) {
            Game game = (Game) i.next();

            if (log.isDebugEnabled()) {
                StringBuffer buf = new StringBuffer();
                buf.append(">>> Generiere Tipp für User: ");
                buf.append(user);
                buf.append(" für Spiel: ");
                buf.append(game);
                log.debug(buf.toString());
            }

            // Den Min-Tipp ermitteln...
            GameResult minTippResult = null;
            try {
                if (minUser == null) {
                    minTippResult = new GameResult(0, 0);
                } else {
                    GameTipp minTipp = game.getGameTipp(minUser.getUser());
                    minTippResult = minTipp.getTipp();
                }
            } catch (StorageObjectNotFoundException ex) {
                log.info("Kein MinTipp vorhanden.");
                minTippResult = new GameResult(0, 0);
            }

            if (log.isDebugEnabled()) {
                log.debug("Ermittelter Min-Tipp: " + minTippResult);
            }

            // Hat Spieler bereits einen Tipp angelegt?
            try {
                GameTipp tipp = game.getGameTipp(user);
                // Tipp bereits vorhanden. Kann er überschrieben werden?
                if (tipp.getStatus() == TippStatusType.MIN
                        || tipp.getStatus() == TippStatusType.UNDEFINED
                        || tipp.getStatus() == TippStatusType.INVALID) {
                    // Einen MIN. UNDEFINED oder INVALID Tipp überschreiben...
                    tipp.setTipp(minTippResult, TippStatusType.MIN);
                } else if (tipp.getStatus() == TippStatusType.AUTO
                        || tipp.getStatus() == TippStatusType.USER) {
                    // AUTO und USER Tipps nicht überschreiben.
                    log.debug("Tipp wird nicht überschrieben.");
                } else {
                    throw new IllegalStateException("GameTippStatus "
                            + tipp.getStatus() + " nicht vorgesehen.");
                }
            } catch (StorageObjectNotFoundException ex) {
                // Dann einen neuen Tipp anlegen.
                game.addTipp(user, minTippResult, TippStatusType.MIN);
            }

            if (log.isDebugEnabled()) {
                StringBuilder buf = new StringBuilder();
                buf.append("User: ");
                buf.append(user);
                buf.append(" Min-Tipp: ");
                try {
                    buf.append(game.getGameTipp(user));
                } catch (StorageObjectNotFoundException ex) {
                    buf.append("Kein Tipp vorhanden.");
                }
                log.debug(buf.toString());
            }
        }
    }

}
