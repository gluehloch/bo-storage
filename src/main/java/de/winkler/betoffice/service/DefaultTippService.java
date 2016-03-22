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

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.mail.MessagingException;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.awtools.basic.AWTools;
import de.awtools.basic.LoggerFactory;
import de.winkler.betoffice.dao.RoundDao;
import de.winkler.betoffice.mail.DefaultBetofficeMailer;
import de.winkler.betoffice.mail.MailContent;
import de.winkler.betoffice.mail.MailContentDetails;
import de.winkler.betoffice.mail.MailXMLParser;
import de.winkler.betoffice.mail.TippMail;
import de.winkler.betoffice.mail.TippMailParameter;
import de.winkler.betoffice.mail.TippToInfoMailConverter;
import de.winkler.betoffice.mail.TippToXmlConverter;
import de.winkler.betoffice.service.TippDto.GameTippDto;
import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.GameResult;
import de.winkler.betoffice.storage.GameTipp;
import de.winkler.betoffice.storage.Season;
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
public class DefaultTippService extends AbstractManagerService implements
        TippService {

    /** Logger für die Klasse. */
    private final Logger log = LoggerFactory.make();

    @Override
    @Transactional
    public GameTipp addTipp(Game match, User user, GameResult gr,
            TippStatusType status) {

        // TODO A performance killer?
        getConfig().getMatchDao().refresh(match);

        GameTipp gameTipp = match.addTipp(user, gr, status);
        getConfig().getGameTippDao().save(gameTipp);
        return gameTipp;
    }

    @Override
    @Transactional
    public void addTipp(GameList round, User user, List<GameResult> tipps,
            TippStatusType status) {

        for (int i = 0; i < round.size(); i++) {
            addTipp(round.get(i), user, tipps.get(i), status);
        }
    }

    @Override
    @Transactional
    public void addTipp(TippDto tippDto) {
        List<BetofficeValidationMessage> messages = new ArrayList<>();

        // Find related user by nick name...
        User user = getConfig().getUserDao().findByNickname(
                tippDto.getNickname());
        if (user == null) {
            BetofficeValidationMessage msg = new BetofficeValidationMessage(
                    "Unknown user with nickname=[" + tippDto.getNickname()
                            + "]", null, Severity.INFO);
            messages.add(msg);
            throw new BetofficeValidationException(messages);
        }

        // Find related round ...
        GameList gameList = getConfig().getRoundDao().findById(
                tippDto.getRoundId());

        if (gameList == null) {
            BetofficeValidationMessage msg = new BetofficeValidationMessage(
                    "The roundId=[" + tippDto.getRoundId() + "] is invalid.",
                    null, Severity.ERROR);
            messages.add(msg);
            throw new BetofficeValidationException(messages);
        }

        for (GameTippDto tipp : tippDto.getGameTipps()) {
            Game game = getConfig().getMatchDao().findById(tipp.getGameId());
            GameTipp gameTipp = game.addTipp(user,
                    new GameResult(tipp.getHomeGoals(), tipp.getGuestGoals()),
                    TippStatusType.USER);
            getConfig().getGameTippDao().save(gameTipp);
        }
    }

    @Override
    @Transactional
    public void updateTipp(Game match, User user, GameResult gr,
            TippStatusType status) {

        addTipp(match, user, gr, status);
    }

    @Override
    @Transactional
    public void updateTipp(List<GameTipp> tipps) {
        for (GameTipp tipp : tipps) {
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
    public void evaluateMailTipp(Season season, MailContentDetails mail) {
        List<BetofficeValidationMessage> messages = new ArrayList<>();

        if (!mail.getUsing().startsWith(MailXMLParser.TIPP)) {
            BetofficeValidationMessage msg = new BetofficeValidationMessage(
                    "Mail is not a 'betoffice tipp' mail. Check the mail format.",
                    null, Severity.INFO);
            messages.add(msg);
            throw new BetofficeValidationException(messages);
        }

        // Find related user by nick name...
        User user = getConfig().getUserDao().findByNickname(mail.getNickName());
        if (user == null) {
            BetofficeValidationMessage msg = new BetofficeValidationMessage(
                    "Unknown user with nickname ->" + mail.getNickName()
                            + "<-.", null, Severity.INFO);
            messages.add(msg);
            throw new BetofficeValidationException(messages);
        }

        // Password Check
        if (!user.getPassword().equals(mail.getPwdA())) {
            BetofficeValidationMessage msg = new BetofficeValidationMessage(
                    "Password does not match!", null, Severity.INFO);
            messages.add(msg);
            throw new BetofficeValidationException(messages);
        }

        // Find related championship...
        long championshipId = Long.parseLong(mail.getChampionship());

        if (season.getId().longValue() != championshipId) {
            BetofficeValidationMessage msg = new BetofficeValidationMessage(
                    "The championship id of the tipp mail ->"
                            + mail.getChampionship()
                            + "<- is not equal to the expected championship from"
                            + " the parameter ->" + season + "<-.", null,
                    Severity.ERROR);
            messages.add(msg);
            throw new BetofficeValidationException(messages);
        }

        // Find related game day / round...
        // Achtung: Spieltage werden im HTML-Formular mit 1..N indiziert;
        // Intern aber mit 0..N-1 dargestellt!
        int roundIndex = Integer.parseInt(mail.getRound());
        if (roundIndex < 1) {
            throw new NumberFormatException("Round is smaller then 1!");
        }

        //
        // TODO: Is this a good call?!? I don´t know.
        // Season championship =
        // getConfig().getSeasonDao().findRoundGroupTeamUserTipp(season);
        // GameList round = season.getGamesOfDay(roundIndex - 1);
        //

        // OLD but lazy
        GameList round = getConfig().getRoundDao().findAllRoundObjects(season,
                roundIndex - 1);
        if (round == null) {
            throw new RuntimeException("Unable to find the specified round ->"
                    + roundIndex + "<- of championship: ->" + season + "<-.");
        }

        // Spiel-Tipps parsen
        StringTokenizer tokHome = new StringTokenizer(mail.getHomeGoals()
                + MailContent.DELIM, MailContent.DELIM);
        StringTokenizer tokGuest = new StringTokenizer(mail.getGuestGoals()
                + MailContent.DELIM, MailContent.DELIM);

        if (tokHome.countTokens() != tokGuest.countTokens()) {
            throw new RuntimeException("Mail contains has an invalid format.");
        }

        int gameNr = 0;
        int home = 0;
        int guest = 0;

        while (tokHome.hasMoreTokens()) {
            Game game = null;
            try {
                game = round.get(gameNr);
            } catch (IndexOutOfBoundsException ex) {
                log.error("Vorgang abgebrochen!", ex);
                throw new RuntimeException(ex);
            }

            String _st1 = tokHome.nextToken();
            String _st2 = tokGuest.nextToken();

            if (log.isDebugEnabled()) {
                StringBuilder debug = new StringBuilder("Spiel-Index: ");
                debug.append(gameNr);
                debug.append(IOUtils.LINE_SEPARATOR);
                debug.append("Spiel-toString: ");
                debug.append(game.toString());
                debug.append(IOUtils.LINE_SEPARATOR);
                debug.append("Tipp: ");
                debug.append(_st1);
                debug.append(":");
                debug.append(_st2);
                log.debug(debug.toString());
            }

            // Ergebnis eintragen
            try {
                home = Integer.parseInt(_st1.trim());
                guest = Integer.parseInt(_st2.trim());
            } catch (NumberFormatException ex) {
                log.error("Catched a NumberFormatException exception.", ex);
                throw new RuntimeException(ex);
            }

            // Tipp-Objekt für das Spiel generieren.
            game.addTipp(user, new GameResult(home, guest), TippStatusType.USER);

            // nächstes Spiel
            gameNr++;
        }
        getConfig().getRoundDao().save(round);
    }

    @Override
    @Transactional
    public void sendMailTipp(User user, TippMailParameter tippMailParameter) {
        long championshipId = tippMailParameter.getChampionshipId();
        // HTML form counts from 1 .. last!
        int roundNr = tippMailParameter.getRoundNr();

        Season season = getConfig().getSeasonDao().findById(championshipId);
        if (season == null) {
            throw new RuntimeException("Unknown season id=" + championshipId);
        }

        GameList round = getConfig().getRoundDao().findRound(season,
                roundNr - 1);
        if (round == null) {
            throw new RuntimeException("Unknown round nr=" + roundNr);
        }

        TippToXmlConverter tippToXmlConverter = new TippToXmlConverter();
        TippToInfoMailConverter tippToInfoMailConverter = new TippToInfoMailConverter();

        DefaultBetofficeMailer dbm = new DefaultBetofficeMailer();

        TippMail tippXmlMail = tippToXmlConverter
                .createTippMail(tippMailParameter);
        TippMail tippInfoMail = tippToInfoMailConverter
                .createTippMail(tippMailParameter);

        MailContentDetails mail = new MailContentDetails();
        mail.setUsing(MailXMLParser.TIPP);
        mail.setNickName(user.getNickName());
        mail.setPwdA(user.getPassword());
        mail.setPwdB(user.getPassword());
        mail.setChampionship(Long.toString(championshipId));
        mail.setRound(Integer.toString(roundNr));
        mail.setHomeGoals(AWTools.toString(tippMailParameter.getHomeGoals()));
        mail.setGuestGoals(AWTools.toString(tippMailParameter.getGuestGoals()));

        evaluateMailTipp(season, mail);

        // Could be something other ...
        final String mailToBetoffice = "betoffice@andre-winkler.de";
        final String mailToUser = user.getEmail();

        try {
            dbm.sendMail(mailToBetoffice, tippXmlMail);
            dbm.sendMail(mailToUser, tippInfoMail);
            dbm.sendMail("gluehloch@googlemail.com", tippInfoMail);
        } catch (MessagingException ex) {
            log.error("Mail system is not available.", ex);
            // throw new RuntimeException(ex);
        }
    }

    @Override
    @Transactional
    public List<GameTipp> findTippsByRoundAndUser(GameList round, User user) {
        return getConfig().getGameTippDao()
                .findTippsByRoundAndUser(round, user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GameTipp> findTippsByMatch(Game match) {
        return getConfig().getGameTippDao().findByMatch(match);
    }

    @Override
    @Transactional(readOnly = true)
    public GameList findTippRound(long seasonId, DateTime date) {
        RoundDao roundDao = getConfig().getRoundDao();
        Long roundId = roundDao.findNextTippRound(seasonId, date);
        if (roundId != null) {
            return roundDao.findById(roundId);
        } else {
            return null;
        }
    }

}
