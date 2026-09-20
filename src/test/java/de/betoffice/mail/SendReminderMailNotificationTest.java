/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2026 by Andre Winkler. All rights reserved.
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

package de.betoffice.mail;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Optional;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;

import de.betoffice.dao.hibernate.AbstractDaoTestSupport;
import de.betoffice.service.CommunityService;
import de.betoffice.service.TippService;
import de.betoffice.service.request.CommunityCreateCommand;
import de.betoffice.service.request.UserCreateCommand;
import de.betoffice.storage.community.CommunityDto;
import de.betoffice.storage.community.entity.CommunityReference;
import de.betoffice.storage.season.RoundDaoHibernateTest;
import de.betoffice.storage.season.entity.GameListEntity;
import de.betoffice.storage.season.entity.SeasonEntity;
import de.betoffice.storage.season.entity.SeasonReference;
import de.betoffice.storage.time.DateTimeProvider;
import de.betoffice.storage.user.entity.Nickname;
import de.betoffice.validation.ServiceResult;

@ContextConfiguration(classes = { SendReminderMailNotificationConfiguration.class })
class SendReminderMailNotificationTest extends AbstractDaoTestSupport {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP);

    @Autowired
    private DateTimeProvider dateTimeProvider;

    @Autowired
    private SendReminderMailNotification sendReminderMailNotification;

    @Autowired
    private CommunityService communityService;

    @Autowired
    private TippService tippService;

    @BeforeEach
    void before() {
        this.prepareDatabase(RoundDaoHibernateTest.class);
    }

    @Test
    void sendNotification() throws MessagingException, IOException {
        final ZonedDateTime zonedDateTime = ZonedDateTime.of(2016, 2, 5, 0, 0, 0, 0, dateTimeProvider.defaultZoneId());
        assertThat(dateTimeProvider.currentDateTime()).isEqualTo(zonedDateTime);

        final Optional<GameListEntity> nextTippRound = sendReminderMailNotification.findNextTippRound();
        assertThat(nextTippRound).isNotEmpty();

        final Optional<GameListEntity> nextTippRound2 = tippService.findNextTippRound(zonedDateTime);
        assertThat(nextTippRound2).isNotEmpty();
        assertThat(nextTippRound2.get().getSeason().getReference())
                .isEqualTo(nextTippRound.get().getSeason().getReference());

        final UserCreateCommand userCreateCommand = new UserCreateCommand(
                "Nickname",
                "Winkler",
                "Andre",
                "mail@mail.com",
                "password",
                "12121212");
        communityService.create(userCreateCommand);

        final SeasonEntity season = nextTippRound.get().getSeason();
        final SeasonReference seasonReference = season.getReference();
        final CommunityReference communityReference = CommunityService.defaultPlayerGroup(seasonReference);

        final CommunityCreateCommand createCommunityCommand = new CommunityCreateCommand(
                communityReference,
                seasonReference,
                "Test Community",
                "2024",
                userCreateCommand.toNickname());
        communityService.create(createCommunityCommand);

        ServiceResult<CommunityDto> member = communityService.addMember(communityReference, Nickname.of("Nickname"));
        assertThat(member.isSuccessful()).isTrue();

        //greenMail.setServerStartupTimeout(5000);
        // ServerSetup serverSetup = new ServerSetup(0, null, null);
        assertThat(greenMail.isRunning()).isTrue();
        sendReminderMailNotification.send();

        final MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertThat(receivedMessages.length).isEqualTo(1);
        assertThat(receivedMessages[0].getSubject()).isEqualTo("Spieltag!");
        assertThat(receivedMessages[0].getContent().toString()).isEqualToNormalizingWhitespace(
                """
                        Heute ist Spieltag. Vergiss deinen Tipp nicht: https://tippdiekistebier.de
                          Für den aktuellen Spieltag liegen die folgenden Tipps von dir vor:
                          2016-05-02 15:00 RWE - RWO -nicht vorhanden-
                          2016-05-02 15:00 RWE - RWO -nicht vorhanden-
                          2016-05-02 20:00 RWE - RWO -nicht vorhanden-
                          2016-06-02 18:00 RWE - RWO -nicht vorhanden-
                                        """);
    }

}
