/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2016 by Andre Winkler. All
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.awtools.basic.LoggerFactory;
import de.winkler.betoffice.dao.RoundDao;
import de.winkler.betoffice.service.TippDto.GameTippDto;
import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.GameResult;
import de.winkler.betoffice.storage.GameTipp;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.enums.TippStatusType;
import de.winkler.betoffice.storage.exception.StorageObjectNotFoundException;
import de.winkler.betoffice.validation.BetofficeValidationException;
import de.winkler.betoffice.validation.BetofficeValidationMessage;
import de.winkler.betoffice.validation.BetofficeValidationMessage.Severity;

/**
 * The default implementation of the {@link TippService}.
 *
 * @author by Andre Winkler
 */
@Service("tippService")
public class DefaultTippService extends AbstractManagerService
        implements TippService {

    /** Logger für die Klasse. */
    private final Logger log = LoggerFactory.make();

    @Override
    @Transactional
    public GameTipp addTipp(String token, Game match, User user, GameResult gr,
            TippStatusType status) {

        GameTipp gameTipp = match.addTipp(token, user, gr, status);
        getConfig().getGameTippDao().save(gameTipp);
        return gameTipp;
    }

    @Override
    @Transactional
    public void addTipp(String token, GameList round, User user,
            List<GameResult> tipps, TippStatusType status) {

        for (int i = 0; i < round.size(); i++) {
            addTipp(token, round.get(i), user, tipps.get(i), status);
        }
    }

    @Override
    @Transactional
    public void addTipp(TippDto tippDto) {
        List<BetofficeValidationMessage> messages = new ArrayList<>();

        // Find related user by nick name...
        Optional<User> user = getConfig().getUserDao()
                .findByNickname(tippDto.getNickname());

        if (!user.isPresent()) {
            BetofficeValidationMessage msg = new BetofficeValidationMessage(
                    "Unknown user with nickname=[" + tippDto.getNickname()
                            + "]",
                    null, Severity.INFO);
            messages.add(msg);
            throw new BetofficeValidationException(messages);
        }
        
        // Find related round ...
        GameList gameList = getConfig().getRoundDao()
                .findById(tippDto.getRoundId());

        if (gameList == null) {
            BetofficeValidationMessage msg = new BetofficeValidationMessage(
                    "The roundId=[" + tippDto.getRoundId() + "] is invalid.",
                    null, Severity.ERROR);
            messages.add(msg);
            throw new BetofficeValidationException(messages);
        }

        // Find related tipp ...
        for (GameTippDto tipp : tippDto.getGameTipps()) {
            Game game = getConfig().getMatchDao().findById(tipp.getGameId());

            // Time point of tipp submit must be before kick off.
            if (game.getDateTime() != null && tippDto.getSubmitTime()
                    .isBefore(new DateTime(game.getDateTime().getTime()))) {

                GameTipp gameTipp = game
                        .addTipp(tippDto.getToken(), user.get(),
                                new GameResult(tipp.getHomeGoals(),
                                        tipp.getGuestGoals()),
                                TippStatusType.USER);
                getConfig().getGameTippDao().save(gameTipp);
            }
        }
    }

    @Override
    @Transactional
    public void updateTipp(String token, Game match, User user, GameResult gr,
            TippStatusType status) {

        // TODO A performance killer?
        getConfig().getMatchDao().refresh(match);

        addTipp(token, match, user, gr, status);
    }

    @Override
    @Transactional
    public void updateTipp(String token, List<GameTipp> tipps) {
        Date now = DateTime.now().toDate();
        for (GameTipp tipp : tipps) {
            tipp.setLastUpdateTime(now);
            getConfig().getGameTippDao().update(tipp);
        }
    }

    @Override
    @Transactional
    public void removeTipp(Game match, User user) {
        try {
            GameTipp gameTipp = match.getGameTipp(user);
            match.removeTipp(gameTipp);
            getConfig().getGameTippDao().delete(gameTipp);
        } catch (StorageObjectNotFoundException ex) {
            log.info("Zu der Spielpaarung " + match + " besitzt der User "
                    + user + " keine Spieltipps.");
            log.info("Der Löschvorgang wurde nicht durchgeführt!");
        }
    }

    @Override
    @Transactional
    public List<GameTipp> findTippsByRoundAndUser(GameList round, User user) {
        return getConfig().getGameTippDao().findTippsByRoundAndUser(round,
                user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GameTipp> findTippsByMatch(Game match) {
        return getConfig().getGameTippDao().findByMatch(match);
    }

    @Override
    @Transactional(readOnly = true)
    public GameList findTipp(GameList round, User user) {
        return getConfig().getGameTippDao().findRound(round, user);
    }

    @Override
    @Transactional(readOnly = true)
    public GameList findTipp(long roundId, long userId) {
        return getConfig().getGameTippDao().findRound(roundId, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public GameList findTippRound(long seasonId, DateTime date) {
        RoundDao roundDao = getConfig().getRoundDao();
        Optional<Long> roundId = roundDao.findNextTippRound(seasonId, date);
        if (roundId.isPresent()) {
            return roundDao.findById(roundId.get());
        } else {
            return null;
        }
    }

}
