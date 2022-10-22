/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2022 by Andre Winkler. All
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

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.winkler.betoffice.dao.GameTippDao;
import de.winkler.betoffice.dao.MatchDao;
import de.winkler.betoffice.dao.RoundDao;
import de.winkler.betoffice.dao.UserDao;
import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.GameResult;
import de.winkler.betoffice.storage.GameTipp;
import de.winkler.betoffice.storage.Nickname;
import de.winkler.betoffice.storage.TippDto;
import de.winkler.betoffice.storage.TippDto.GameTippDto;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.UserResultOfDay;
import de.winkler.betoffice.storage.enums.TippStatusType;
import de.winkler.betoffice.storage.enums.TotoResult;
import de.winkler.betoffice.storage.exception.StorageObjectNotFoundException;
import de.winkler.betoffice.storage.exception.StorageRuntimeException;
import de.winkler.betoffice.util.LoggerFactory;
import de.winkler.betoffice.validation.BetofficeValidationException;
import de.winkler.betoffice.validation.BetofficeValidationMessage;
import de.winkler.betoffice.validation.BetofficeValidationMessage.Severity;

/**
 * The default implementation of the {@link TippService}.
 *
 * @author by Andre Winkler
 */
@Service("tippService")
public class DefaultTippService extends AbstractManagerService implements TippService {

    /** Logger für die Klasse. */
    private final Logger log = LoggerFactory.make();

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoundDao roundDao;

    @Autowired
    private MatchDao matchDao;

    @Autowired
    private GameTippDao gameTippDao;

    @Autowired
    private DateTimeProvider datetimeProvider;

    @Override
    @Transactional
    public GameTipp createOrUpdateTipp(String token, Game match, User user, GameResult tipp, TippStatusType status) {
        GameList round = roundDao.findRoundByGame(match).orElseThrow();
        return createOrUpdateTipp(token, round, match, user, tipp, status);
    }

    private GameTipp createOrUpdateTipp(String token, GameList round, Game game, User user, GameResult tipp, TippStatusType status) {
        Date now = Date.from(datetimeProvider.currentDateTime().toInstant());

        Optional<GameTipp> gameTipp = gameTippDao.find(game, user);
        if (gameTipp.isPresent()) {
            GameTipp updateGameTipp = gameTipp.get();
            updateGameTipp.setToken(token);
            updateGameTipp.setLastUpdateTime(now);
            updateGameTipp.setUser(user);
            updateGameTipp.setTipp(tipp, status);
            return gameTippDao.save(updateGameTipp);
        } else {
            GameTipp newGameTipp = new GameTipp();
            newGameTipp.setToken(token);
            newGameTipp.setCreationTime(now);
            newGameTipp.setLastUpdateTime(now);
            newGameTipp.setUser(user);
            newGameTipp.setGame(game);
            newGameTipp.setTipp(tipp, status);
            return gameTippDao.save(newGameTipp);
        }
    }

    @Override
    @Transactional
    public List<GameTipp> createOrUpdateTipp(String token, GameList round, User user, List<GameResult> tipps,
            TippStatusType status) {
        List<GameTipp> result = new ArrayList<>();
        for (int i = 0; i < round.size(); i++) {
            result.add(createOrUpdateTipp(token, round.get(i), user, tipps.get(i), status));
        }
        return result;
    }

    private static BetofficeValidationMessage unknwonUser(String unknownUser) {
        String message = String.format("Unknown user with nickname=[%s]", unknownUser);
        return new BetofficeValidationMessage(message, null, Severity.INFO);
    }

    private static BetofficeValidationMessage unknwonRoundId(long roundId) {
        String message = String.format("The roundId=[%d] is invalid.", roundId);
        return new BetofficeValidationMessage(message, null, Severity.ERROR);
    }

    private static BetofficeValidationException newException(BetofficeValidationMessage message) {
        return new BetofficeValidationException(List.of(message));
    }

    private static Optional<GameTipp> findTipp(Game game, List<GameTipp> tipps) {
        return tipps.stream().filter(t -> t.getGame().equals(game)).findFirst();
    }

    private static class PartiallyCompleteTippDto {
        private final User user;
        private final Game game;
        private final String token;
        private final ZonedDateTime submitTime;
        private final GameTippDto tippDto;

        private PartiallyCompleteTippDto(User user, Game game, String token, ZonedDateTime submitTime,
                GameTippDto gameTippDto) {

            this.user = user;
            this.game = game;
            this.token = token;
            this.submitTime = submitTime;
            this.tippDto = gameTippDto;
        }

        public User getUser() {
            return user;
        }

        public Game getGame() {
            return game;
        }

        public String getToken() {
            return token;
        }

        public ZonedDateTime getSubmitTime() {
            return submitTime;
        }

        public GameTippDto getTippDto() {
            return tippDto;
        }

        public static PartiallyCompleteTippDto of(User user, Game game, String token, ZonedDateTime submitTime,
                GameTippDto gameTipp) {

            return new PartiallyCompleteTippDto(user, game, token, submitTime, gameTipp);
        }

        public static GameTipp toGameTipp(PartiallyCompleteTippDto tippDto) {
            Date submitTime = Date.from(tippDto.getSubmitTime().toInstant());

            GameTipp gameTipp = new GameTipp();
            gameTipp.setToken(tippDto.getToken());
            gameTipp.setGame(tippDto.getGame());
            gameTipp.setUser(tippDto.getUser());
            gameTipp.setCreationTime(submitTime);
            gameTipp.setLastUpdateTime(submitTime);
            gameTipp.setToken(tippDto.getToken());
            gameTipp.setTipp(GameResult.of(tippDto.getTippDto().getHomeGoals(), tippDto.getTippDto().getGuestGoals()),
                    TippStatusType.USER);

            return gameTipp;
        }
    }

    /**
     * Added a user tipp to the database. If the tipp date is after kick off, the tipp will not be persisted.
     * 
     * @param tippDto The user tipp to persist.
     * @return the persisted (or not) persisted user tipps.
     */
    @Override
    @Transactional
    public List<GameTipp> validateKickOffTimeAndAddTipp(TippDto tippDto) {
        User user = userDao.findByNickname(Nickname.of(tippDto.getNickname())).orElseThrow(
                () -> newException(unknwonUser(tippDto.getNickname())));

        GameList gameList = roundDao.findById(tippDto.getRoundId());
        if (gameList == null) {
            throw newException(unknwonRoundId(tippDto.getRoundId()));
        }

        List<GameTipp> predefinedTipps = gameTippDao.find(gameList, user);

        for (GameTippDto gameTippDto : tippDto.getGameTipps()) {
            Game game = matchDao.findById(gameTippDto.getGameId());

            if (isSubmitTimeBeforeGameTime(tippDto, game)) {
                Optional<GameTipp> predefinedTipp = findTipp(game, predefinedTipps);

                if (predefinedTipp.isPresent()) {
                    GameTipp persistedTipp = predefinedTipp.get();
                    persistedTipp.setLastUpdateTime(Date.from(tippDto.getSubmitTime().toInstant()));
                    persistedTipp.setToken(tippDto.getToken());
                    persistedTipp.setTipp(GameResult.of(
                            gameTippDto.getHomeGoals(), gameTippDto.getGuestGoals()), TippStatusType.USER);
                    gameTippDao.update(persistedTipp);
                } else {
                    GameTipp gameTipp = PartiallyCompleteTippDto.toGameTipp(PartiallyCompleteTippDto.of(
                            user, game, tippDto.getToken(), tippDto.getSubmitTime(), gameTippDto));

                    gameTippDao.save(gameTipp);
                }
            }
        }

        return gameTippDao.find(gameList, user);
    }

    /**
     * Falls die Spielpaarung keinen festgelegten Zeitpunkt definiert, wird der
     * Tippzeitpunkt nicht bewertet.
     * 
     * @param tipp
     *            Der Tipp
     * @param game
     *            Das Spiel
     * @return <code>true</code>, falls der Tippzeitpunkt vor Spielbeginn liegt.
     */
    private boolean isSubmitTimeBeforeGameTime(TippDto tipp, Game game) {
        return game.getDateTime() == null || tipp.getSubmitTime().isBefore(game.getDateTime());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GameTipp> findTipps(Game match) {
        return gameTippDao.find(match);
    }

    @Override
    @Transactional
    public Optional<GameTipp> findTipp(Game game, User user) {
        return gameTippDao.find(game, user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GameTipp> findTipps(long roundId, long userId) {
        return gameTippDao.find(roundId, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GameTipp> findTipps(long roundId) {
        return gameTippDao.find(roundId);
    }

    @Override
    @Transactional(readOnly = true)
    public GameList findNextTippRound(long seasonId, ZonedDateTime date) {
        Optional<Long> roundId = roundDao.findNextTippRound(seasonId, date);
        if (roundId.isPresent()) {
            return roundDao.findById(roundId.get());
        } else {
            return null;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public GameList findPreviousTippRound(long seasonId, ZonedDateTime date) {
        Optional<Long> roundId = roundDao.findLastTippRound(seasonId, date);
        if (roundId.isPresent()) {
            return roundDao.findById(roundId.get());
        } else {
            return null;
        }
    }

    // ------------------------------------------------------------------------

    /**
     * Berechnet die erzielten Punkte eines Spielers für diesen Spieltag.
     *
     * @param round
     *            Der Spieltag fuer den die Punkte ermittelt werden sollen.
     * @param user
     *            Der Teilnehmer für den die Punktzahl ermittelt wird.
     * @return Ein <code>UserResultOfDay</code>
     */
    @Override
    @Transactional
    public UserResultOfDay getUserPoints(GameList round, User user) {
        UserResultOfDay urod = new UserResultOfDay();

        List<GameTipp> tippsByRoundAndUser = findTipps(round, user);

        urod.setUser(user);
        for (GameTipp gameTipp : tippsByRoundAndUser) {
            if (gameTipp.getGame().isPlayed()) {
                try {
                    //
                    // Zu bemerken ist, dass alle Tipps eines Spieltags den
                    // gleichen Status besitzen müssen. Ist dies nicht der
                    // Fall, wird eine RuntimeException geworfen.
                    //
                    // TODO Dieser Fall kann aber trotzdem auftreten:
                    // Automatische MinTipp-Generierung und anschliessendes
                    // teilweises, manuelles Ändern der Tipps.
                    //
                    if ((urod.getStatus() != null) && !(gameTipp.getStatus().equals(urod.getStatus()))) {

                        log.error("Der Tipp " + gameTipp + " ist fehlerhaft!");
                        throw new StorageRuntimeException(
                                "Ein Tipp wurde automatisch generiert. "
                                        + "Ein anderer Tipp wurde per Teilnehmer "
                                        + "generiert. Dieser Zustand sollte nicht "
                                        + "auftreten!");
                    }

                    urod.setStatus(gameTipp.getStatus());
                    urod.setTipps(urod.getTipps() + 1);

                    if (gameTipp.getTotoResult() == TotoResult.EQUAL) {
                        urod.setWin(urod.getWin() + 1);
                    } else if (gameTipp.getTotoResult() == TotoResult.TOTO) {
                        urod.setToto(urod.getToto() + 1);
                    }
                } catch (StorageObjectNotFoundException ex) {
                    // Ist Ok, dann müssen auch keine Punkte addiert werden.
                    log.info("Kein Tipp für game: " + gameTipp.getGame() + " vorhanden");
                }
            }
        }
        return urod;
    }

}
