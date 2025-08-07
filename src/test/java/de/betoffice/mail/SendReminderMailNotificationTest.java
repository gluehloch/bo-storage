/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2025 by Andre Winkler. All rights reserved.
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

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZonedDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import de.betoffice.dao.hibernate.AbstractDaoTestSupport;
import de.betoffice.service.DateTimeProvider;
import de.betoffice.storage.season.RoundDaoHibernateTest;
import de.betoffice.storage.season.entity.GameList;

@ContextConfiguration(classes = { SendReminderMailNotificationConfiguration.class })
class SendReminderMailNotificationTest extends AbstractDaoTestSupport {

    @Autowired
    private DateTimeProvider dateTimeProvider;

    @Autowired
    private SendReminderMailNotification sendReminderMailNotification;

    @BeforeEach
    void before() {
        this.prepareDatabase(RoundDaoHibernateTest.class);
    }

    @Test
    void sendNotification() {
        assertThat(dateTimeProvider.currentDateTime())
                .isEqualTo(ZonedDateTime.of(2016, 2, 5, 0, 0, 0, 0, dateTimeProvider.defaultZoneId()));

        Optional<GameList> nextTippRound = sendReminderMailNotification.findNextTippRound();
        assertThat(nextTippRound).isNotEmpty();

        sendReminderMailNotification.send();
    }

}
