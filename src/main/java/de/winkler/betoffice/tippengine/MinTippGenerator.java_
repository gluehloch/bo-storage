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

package de.winkler.betoffice.tippengine;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.winkler.betoffice.service.SeasonManagerService;
import de.winkler.betoffice.service.TippService;
import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.GameResult;
import de.winkler.betoffice.storage.GameTipp;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.UserResultOfDay;
import de.winkler.betoffice.storage.enums.TippStatusType;
import de.winkler.betoffice.util.LoggerFactory;

/**
 * Klasse zur Generierung eines Min-Tipps für mehrere User. Es wird der
 * schlechteste Tipp des Spieltages ermittelt und dieser dann dem User
 * zugewiesen.
 * 
 * @author Andre Winkler
 */
@Service
public class MinTippGenerator {

    private static final String BOT_MIN_TIPP = "#BOT_MIN_TIPP#";

    /** Der private Logger der Klasse. */
    private final Logger log = LoggerFactory.make();

    @Autowired
    private InfoCenter infoCenter;

    @Autowired
    private TippService tippService;

    @Autowired
    private SeasonManagerService seasonManagerService;

    /**
     * Legt fuer den {@code user} fuer die gesamte Meisterschaft Spieltipps an.
     * 
     * @param season
     *            Die Meisterschaft
     * @param user
     *            Der Teilnehmer
     */
    @Transactional
    public void generateTipp(Season season, User user) {
        List<GameList> rounds = seasonManagerService.findRounds(season);
        for (GameList round : rounds) {
            generateTipp(round, user);
        }
    }

    @Transactional
    public void generateTipp(GameList round, User user) {
        List<User> users = seasonManagerService.findActivatedUsers(round.getSeason());

        if (!user.isExcluded() && !user.isAutomat()) {
            createOrUpdateMinimumTipp(round, user, users);
        }
    }

    private void createOrUpdateMinimumTipp(GameList round, User user, List<User> activeUsers) {
        // Den schwächsten Tipper des Spieltages ermitteln.
        UserResultOfDay worstUser = infoCenter.findWorstTipp(round, activeUsers);

        log.debug("Min-Tipp von User: {}", worstUser);

        // List<Game> games = round.unmodifiableList();
        List<Game> games = seasonManagerService.findMatches(round);

        for (Game game : games) {
            log.info("Generiere Tipp für User: {} fuer das Spiel {}", user, game);

            // Den schlechtesten Tipp des Spieltages ermitteln.
            GameResult minTippResult = null;
            if (worstUser == null) {
                minTippResult = GameResult.of(0, 0);
            } else {
                Optional<GameTipp> minTipp = tippService.findTipp(game, worstUser.getUser());
                if (minTipp.isPresent()) {
                    minTippResult = minTipp.get().getTipp();
                } else {
                    minTippResult = GameResult.of(0, 0);
                }
            }

            if (log.isDebugEnabled()) {
                log.debug("Ermittelter Min-Tipp: {}", minTippResult);
            }

            // Hat Spieler bereits einen Tipp angelegt?

            Optional<GameTipp> tipp = tippService.findTipp(game, user);

            // Tipp bereits vorhanden. Kann er überschrieben werden?
            if (tipp.isPresent()) {
                GameTipp presentTipp = tipp.get();
                if (presentTipp.getStatus() == TippStatusType.MIN
                        || presentTipp.getStatus() == TippStatusType.UNDEFINED
                        || presentTipp.getStatus() == TippStatusType.INVALID) {

                    //
                    // Einen MIN. UNDEFINED oder INVALID Tipp überschreiben...
                    //
                    tippService.createOrUpdateTipp(BOT_MIN_TIPP, game, user, minTippResult, TippStatusType.MIN);

                } else if (presentTipp.getStatus() == TippStatusType.AUTO
                        || presentTipp.getStatus() == TippStatusType.USER) {

                    //
                    // AUTO und USER Tipps nicht überschreiben.
                    //
                    log.debug("Tipp wird nicht überschrieben.");

                }
            } else {
                tippService.createOrUpdateTipp(BOT_MIN_TIPP, game, user, minTippResult, TippStatusType.MIN);
            }

            log.debug("User: {} Min-Tipp: {}", user, minTippResult);
        }
    }

}
