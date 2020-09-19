/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2016 by Andre Winkler. All rights reserved.
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.winkler.betoffice.service.SeasonManagerService;
import de.winkler.betoffice.service.TippService;
import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.GameResult;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.enums.TippStatusType;

/**
 * Erster Automat des Wettbüros. Der Automat simuliert einen Teilnehmer. Aus
 * allen Tipps zu einem Spiel ermittelt der Automat den Durchschnitt, rundet das
 * Ergebnis ab und verwendet das Ergebnis als eigenen Tipp.
 *
 * @author Andre Winkler
 */
@Service
public class MediumTippGenerator implements TippGenerator {

    private final String BOT_MEDIUM_TIPP = "#BOT_MEDIUM_TIPP#";

    @Autowired
    private InfoCenter infoCenter;
    
    @Autowired
    private TippService tippService;
    
    @Autowired
    private SeasonManagerService seasonManagerService;
    
    @Transactional
    public void generateTipp(Season season, User user) {
        for (GameList gameList : season.toGameList()) {
            generateTipp(gameList, user);
        }
    }

    @Transactional
    public void generateTipp(GameList round, User user) {
        List<User> users = seasonManagerService.findActivatedUsers(round.getSeason());
        
        List<Game> games = round.unmodifiableList();
        for (Game game : games) {
            GameResult gr = infoCenter.findMediumTipp(game, users);
            if (gr != null) {
                tippService.createOrUpdateTipp(BOT_MEDIUM_TIPP, game, user, gr, TippStatusType.AUTO);
            }
        }
    }

}
