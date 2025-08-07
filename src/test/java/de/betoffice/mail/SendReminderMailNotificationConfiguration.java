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

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import de.betoffice.service.DateTimeProvider;

public class SendReminderMailNotificationConfiguration {

    @Bean
    @Primary
    DateTimeProvider dateTimeProvider() {
        return new DateTimeProvider() {
            @Override
            public ZoneId defaultZoneId() {
                return ZoneId.of("Europe/Berlin");
            }

            @Override
            public ZonedDateTime currentDateTime() {
                return ZonedDateTime.of(2016, 2, 5, 0, 0, 0, 0, defaultZoneId());
            }
        };
    }

}
