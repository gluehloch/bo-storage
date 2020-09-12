/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2020 by Andre Winkler. All
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
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.winkler.betoffice.dao.GameTippDao;
import de.winkler.betoffice.dao.MatchDao;
import de.winkler.betoffice.dao.RoundDao;
import de.winkler.betoffice.dao.UserDao;
import de.winkler.betoffice.service.TippDto.GameTippDto;
import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.GameResult;
import de.winkler.betoffice.storage.GameTipp;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.UserResultOfDay;
import de.winkler.betoffice.storage.enums.TippStatusType;
import de.winkler.betoffice.storage.enums.TotoResult;
import de.winkler.betoffice.storage.exception.StorageObjectExistsException;
import de.winkler.betoffice.storage.exception.StorageObjectNotFoundException;
import de.winkler.betoffice.storage.exception.StorageObjectNotValidException;
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
public class DefaultTippService extends AbstractManagerService
        implements TippService {

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

    @Override
    @Transactional
    public void addTipp(String token, GameList round, User user,
            List<GameResult> tipps, TippStatusType status) {

        for (int i = 0; i < round.size(); i++) {
            addTipp(token, round.get(i), user, tipps.get(i), status);
        }
    }

    private static BetofficeValidationMessage messageUnknwonUser(
            String unknownUser) {
        String message = String.format("Unknown user with nickname=[%s]",
                unknownUser);
        return new BetofficeValidationMessage(message, null, Severity.INFO);
    }

    private static BetofficeValidationMessage unknwonRoundId(long roundId) {
        String message = String.format("The roundId=[%d] is invalid.", roundId);
        return new BetofficeValidationMessage(message, null, Severity.ERROR);
    }

    private static BetofficeValidationException newException(
            BetofficeValidationMessage message) {
        return new BetofficeValidationException(List.of(message));
    }

    private static Optional<GameTipp> findTipp(Game game, List<GameTipp> tipps) {
        return tipps.stream().filter(t -> t.getGame().equals(game)).findFirst();
    }

    @Override
    @Transactional
    public void addTipp(TippDto tippDto) {
        User user = userDao.findByNickname(tippDto.getNickname()).orElseThrow(
                () -> newException(messageUnknwonUser(tippDto.getNickname())));

        GameList gameList = roundDao.findById(tippDto.getRoundId());
        if (gameList == null) {
            throw newException(unknwonRoundId(tippDto.getRoundId()));
        }

        List<GameTipp> tipps = gameTippDao.findTippsByRoundAndUser(gameList, user);
        
        for (GameTippDto gameTippDto : tippDto.getGameTipps()) {
            Game game = matchDao.findById(tipp.getGameId());

            ZonedDateTime gameDateTime = game.getDateTime();

            // Falls die Spielpaarung keinen festgelegten Zeitpunkt definiert,
            // wird der Tippzeitpunkt nicht bewertet.

            if (gameDateTime == null) {
                Optional<GameTipp> predefinedTipp = findTipp(game, tipps);
                
                GameTipp gameTipp = new GameTipp();
                gameTipp.setToken(tippDto.getToken());
                gameTipp.setGame(game);
                gameTipp.setUser(user);
                gameTipp.setCreationTime(
                        Date.from(tippDto.getSubmitTime().toInstant()));
                gameTipp.setLastUpdateTime(gameTipp.getCreationTime());
                gameTipp.setToken(tippDto.getToken());
                gameTipp.setTipp(
                        GameResult.of(gameTippDto.getHomeGoals(),
                                gameTippDto.getGuestGoals()),
                        TippStatusType.USER);

                gameTippDao.save(gameTipp);
            } else {
                if (tippDto.getSubmitTime().isBefore(gameDateTime)) {
                    GameTipp gameTipp = new GameTipp();
                    gameTipp.setToken(tippDto.getToken());
                    gameTipp.setGame(game);
                    gameTipp.setUser(user);
                    gameTipp.setCreationTime(
                            Date.from(tippDto.getSubmitTime().toInstant()));
                    gameTipp.setLastUpdateTime(gameTipp.getCreationTime());
                    gameTipp.setToken(tippDto.getToken());
                    gameTipp.setTipp(GameResult.of(gameTippDto.getHomeGoals(),
                            gameTippDto.getGuestGoals()), TippStatusType.USER);

                    gameTippDao.save(gameTipp);
                }
            }
        }
    }

    @Override
    @Transactional
    public void updateTipp(String token, Game match, User user, GameResult gr,
            TippStatusType status) {

        // TODO A performance killer?
        matchDao.refresh(match);

        addTipp(token, match, user, gr, status);
    }

    @Override
    @Transactional
    public void updateTipp(String token, List<GameTipp> tipps) {
        Date now = DateTime.now().toDate();
        for (GameTipp tipp : tipps) {
            tipp.setLastUpdateTime(now);
            gameTippDao.update(tipp);
        }
    }

    @Override
    @Transactional
    public void removeTipp(Game match, User user) {
        try {
            GameTipp gameTipp = match.getGameTipp(user);
            match.removeTipp(gameTipp);
            gameTippDao.delete(gameTipp);
        } catch (StorageObjectNotFoundException ex) {
            log.info("Zu der Spielpaarung " + match + " besitzt der User "
                    + user + " keine Spieltipps.");
            log.info("Der Löschvorgang wurde nicht durchgeführt!");
        }
    }

    @Override
    @Transactional
    public List<GameTipp> findTippsByRoundAndUser(GameList round, User user) {
        return gameTippDao.findTippsByRoundAndUser(round,
                user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GameTipp> findTippsByMatch(Game match) {
        return gameTippDao.findByMatch(match);
    }

    @Override
    @Transactional(readOnly = true)
    public GameList findTipp(GameList round, User user) {
        return gameTippDao.findRound(round, user);
    }

    @Override
    @Transactional(readOnly = true)
    public GameList findTipp(long roundId, long userId) {
        return gameTippDao.findRound(roundId, userId);
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
     * Liefert alle Tipps dieses Spieltags für den gefordeten User.
     *
     * @param user
     *            Die Tipps des gesuchten Users.
     * @return Eine Liste mit allen Tipps für diesen Spieltag vom gesuchten
     *         User.
     */
    public List<GameTipp> getTippsOfUser(final User user) {
        List<GameTipp> tippList = new ArrayList<GameTipp>();
        for (Game game : gameList) {
            try {
                GameTipp tipp = game.getGameTipp(user);
                tippList.add(tipp);
            } catch (StorageObjectNotFoundException ex) {
                log.info(new StringBuffer("Für User ").append(user)
                        .append(" keinen Tipp gefunden.").toString());
            }
        }
        return tippList;
    }

    /**
     * Fügt dem Spiel einen Tipp hinzu. Bestehende Tipps werden überschrieben.
     *
     * @param token
     *            Das Anmeldetoken mit dem dieser Spieltipp angelegt wird.
     * @param user
     *            Der Spieler von dem der Tipp kommt.
     * @param gr
     *            Der Tipp des Spielers.
     * @param status
     *            Der Status des Tipps.
     * @return Ein neu erzeugter Tipp oder ein bereits abgegebener Tipp.
     */
    public GameTipp addTipp(String token, Game game, User user, GameResult tipp,
            TippStatusType status) {

        Validate.notNull(token);
        Validate.notNull(user);
        Validate.notNull(tipp);
        Validate.notNull(status);

        Game persistedGame = matchDao.findById(game.getId());

        // Create or Update
        GameTipp gameTipp = null;
        if (containsTipp(user)) {
            try {
                tipp = getGameTipp(user);
                tipp.setToken(token);
                tipp.setTipp(gr, status);
                tipp.setLastUpdateTime(DateTime.now().toDate());
            } catch (StorageObjectNotFoundException ex) {
                // Nach Abfrage nicht möglich!
                log.error("storage object not found exception", ex);
                throw new StorageRuntimeException(ex);
            }
        } else {
            tipp = new GameTipp();
            tipp.setToken(token);
            tipp.setUser(user);
            tipp.setGame(persistedGame);
            tipp.setTipp(tipp, status);
            Date now = DateTime.now().toDate();
            tipp.setLastUpdateTime(now);
            tipp.setCreationTime(now);

            try {
                addTipp(tipp);
            } catch (StorageObjectExistsException ex) {
                // Nach Abfrage nicht möglich!
                log.error("storage object exists exception", ex);
                throw new StorageRuntimeException(ex);
            } catch (StorageObjectNotValidException ex) {
                // Selbst zusammen gebaut!
                log.error("storage object not valid exception", ex);
                throw new StorageRuntimeException(ex);
            }
        }

        gameTippDao.save(tipp);
        return tipp;
    }

    /**
     * Ermittelt für einen Spieler den für dieses Spiel abgegebenen Tipp.
     *
     * @param user
     *            Der gesuchte Tipp von diesem Spieler.
     * @return Der abgegebene Tipp des Spielers.
     * @throws StorageObjectNotFoundException
     *             Keinen Tipp für gesuchten User gefunden.
     */
    public GameTipp getGameTipp(final User user)
            throws StorageObjectNotFoundException {
        Validate.notNull(user, "user als null Parameter");

        Optional<GameTipp> userTipp = tippList.stream()
                .filter(tipp -> tipp != null && tipp.getUser().equals(user))
                .findFirst();

        if (userTipp.isPresent()) {
            return userTipp.get();
        } else {
            throw new StorageObjectNotFoundException(
                    "Der Teilnehmer [" + user.getNickName()
                            + "] hat keinen Spieltipp.");
        }
    }

    /**
     * Liefert einen Spieltipp oder, wenn kein Tipp gefunden werden konnte,
     * einen ungültigen Spieltipp.
     *
     * @param user
     *            Den Tipp von diesem Teilnehmer suchen.
     * @return Der Tipp.
     */
    public GameTipp getGameTippOrInvalid(final User user) {
        GameTipp gameTipp = null;
        try {
            gameTipp = getGameTipp(user);
        } catch (StorageObjectNotFoundException ex) {
            gameTipp = new GameTipp();
            gameTipp.setGame(this);
            gameTipp.setUser(user);
            gameTipp.setTipp(0, 0, TippStatusType.INVALID);
        }
        return gameTipp;
    }

    /**
     * Prüft, ob der Teilnehmer user einen Tipp abgegeben hat.
     *
     * @param user
     *            Der zu prüfende User.
     * @return true, Tipp vorhanden; false, kein Tipp vorhanden.
     */
    public boolean containsTipp(User user) {
        return tippList.stream().anyMatch(tipp -> tipp.getUser().equals(user));
    }

    /**
     * Liefert die Anzahl der abgegebenen Tipps für dieses Spiel.
     *
     * @return Anzahl der Tipps.
     */
    public int tippSize() {
        return tippList.size();
    }

    /**
     * Liefert ein nicht-modifizierbare Liste aller Tipps dieses Spiels.
     *
     * @return Eine nicht modifizierbare Kopie der internen Tipp-Liste.
     */
    public Set<GameTipp> getTipps() {
        return tippList;
    }

    /**
     * Berechnet die erzielten Punkte eines Spielers für diesen Spieltag.
     *
     * @param user
     *            Der Teilnehmer für den die Punktzahl ermittelt wird.
     * @return Ein <code>UserResultOfDay</code>
     */
    public UserResultOfDay getUserPoints(User user) {
        UserResultOfDay urod = new UserResultOfDay();

        urod.setUser(user);
        for (Game game : gameList) {
            if (game.isPlayed()) {
                try {
                    GameTipp tipp = game.getGameTipp(user);
                    //
                    // Zu bemerken ist, dass alle Tipps eines Spieltags den
                    // gleichen Status besitzen müssen. Ist dies nicht der
                    // Fall, wird eine RuntimeException geworfen.
                    //
                    // TODO Dieser Fall kann aber trotzdem auftreten:
                    // Automatische MinTipp-Generierung und anschliessendes
                    // teilweises, manuelles Ändern der Tipps.
                    //
                    if ((urod.getStatus() != null)
                            && !(tipp.getStatus().equals(urod.getStatus()))) {

                        log.error("Der Tipp " + tipp + " ist fehlerhaft!");
                        throw new StorageRuntimeException(
                                "Ein Tipp wurde automatisch generiert. "
                                        + "Ein anderer Tipp wurde per Teilnehmer "
                                        + "generiert. Dieser Zustand sollte nicht "
                                        + "auftreten!");
                    }

                    urod.setStatus(tipp.getStatus());
                    urod.setTipps(urod.getTipps() + 1);

                    if (tipp.getTotoResult() == TotoResult.EQUAL) {
                        urod.setWin(urod.getWin() + 1);
                    } else if (tipp.getTotoResult() == TotoResult.TOTO) {
                        urod.setToto(urod.getToto() + 1);
                    }
                } catch (StorageObjectNotFoundException ex) {
                    // Ist Ok, dann müssen auch keine Punkte addiert werden.
                    log.info("Kein Tipp für game: " + game + " vorhanden");
                }
            }
        }
        return urod;
    }

}
