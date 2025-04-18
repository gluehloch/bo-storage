/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2015-2025 by Andre Winkler. All rights
 * reserved.
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

package de.winkler.betoffice.mail;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import de.winkler.betoffice.service.CommunityService;
import de.winkler.betoffice.service.SeasonManagerService;
import de.winkler.betoffice.service.TippService;
import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.GameTipp;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.enums.NotificationType;

@Service
public class SendReminderMailNotification {

    private static final Logger LOG = LoggerFactory.getLogger(SendReminderMailNotification.class);

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-dd-MM HH:mm");

    private final TippService tippService;
    private final CommunityService communityService;
    private final SeasonManagerService seasonManagerService;
    private final MailTask mailTask;

    public SendReminderMailNotification(
            final SeasonManagerService seasonManagerService,
            final TippService tippService,
            final CommunityService communityService,
            final MailTask mailTask) {
        this.seasonManagerService = seasonManagerService;
        this.tippService = tippService;
        this.communityService = communityService;
        this.mailTask = mailTask;
    }

    public void send() {
        // TODO Wie stelle ich sicher, dass die Mail für einen Spieltag nur einmal rausgeschickt wird?
        // Der Job läuft nur einmal am Tag. Falls Spiele an diesem Tag sind, wird eine Mail verschickt.
        // Ist ein Spieltag über mehrere Tage verteilt, bekommt man dann für jeden Tag eine Email.
        final var now = ZonedDateTime.now();
        final var localNow = now.toLocalDate();

        Optional<GameList> nextTippRound = tippService.findNextTippRound(now);
        nextTippRound.ifPresent(nxtr -> {
            final var season = nextTippRound.get().getSeason();
            final var matches = seasonManagerService.findMatches(nxtr);
            if (!matches.isEmpty() && matches.get(0).getDateTime().toLocalDate().compareTo(localNow) == 0) {
                final var members = communityService
                        .findMembers(CommunityService.defaultPlayerGroup(season.getReference()));
                members.stream().filter(SendReminderMailNotification::notify).forEach(u -> {
                    try {
                        final List<GameTipp> sortedTipps = sort(tippService.findTipps(nxtr, u));
                        final List<Pair> gameWithTipp = new ArrayList<>();
                        for (Game m : matches) {
                            boolean findTipp = false;
                            for (GameTipp t : sortedTipps) {
                                if (m.equals(t.getGame())) {
                                    gameWithTipp.add(new Pair(m, t));
                                    findTipp = true;
                                }
                            }
                            if (!findTipp) {
                                gameWithTipp.add(new Pair(m, null));
                            }
                        }

                        StringBuilder sb = new StringBuilder();
                        sb.append("Heute ist Spieltag. Vergiss deinen Tipp nicht: https://tippdiekistebier.de\n");
                        sb.append("Für den aktuellen Spieltag liegen die folgenden Tipps von dir vor:");

                        for (var gwt : gameWithTipp) {
                            sb.append("\n")
                                    .append(gwt.game().getDateTime().format(formatter))
                                    .append(" ")
                                    .append(gwt.game().getHomeTeam().getName())
                                    .append(" - ")
                                    .append(gwt.game().getGuestTeam().getName());
                            if (gwt.gameTipp() != null) {
                                sb.append(" ")
                                        .append(gwt.gameTipp().getTipp().getHomeGoals())
                                        .append(":")
                                        .append(gwt.gameTipp().getTipp().getGuestGoals());
                            }
                        }
                        mailTask.send("betoffice@andre-winkler.de", u.getEmail(), "Spieltag!", sb.toString());

                    } catch (Exception ex) {
                        LOG.error(String.format("Unable to send an email to %s", u.getEmail()), ex);
                    }
                });
            }
        });
    }

    public static boolean notify(User user) {
        return NotificationType.TIPP.equals(user.getNotification());
    }

    public static List<GameTipp> sort(List<GameTipp> gameTipps) {
        List<GameTipp> sortedTipps = new ArrayList<>(gameTipps);
        sortedTipps.sort(new Comparator<GameTipp>() {
            @Override
            public int compare(GameTipp o1, GameTipp o2) {
                return o1.getGame().getDateTime().compareTo(o2.getGame().getDateTime());
            }
        });
        return sortedTipps;
    }

    private static record Pair(Game game, GameTipp gameTipp) {
    }

}
