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
package de.betoffice.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.jupiter.api.Test;

public class TippServiceTest {

    @Test
    public void beforeOrAfterGameDateTimeWithDifferenTimeZone() {
        DateTime gameDateTime = new DateTime(2020, 6, 20, 16, 00, DateTimeZone.forID("Europe/Berlin"));
        
        // Die UTC Zeitzone ist im Sommer zwei Stunden hinter 'Europe/Berlin'
        // D.h. Sommerzeit: 'Europe/Berlin' 16:00 Uhr => 'UTC'          14:00 oder
        //                  'UTC' 16 Uhr             => 'Europe/Berlin' 18:00
        assertThat(new DateTime(2020, 6, 20, 12, 00, DateTimeZone.UTC).isBefore(gameDateTime)).isTrue();
        assertThat(new DateTime(2020, 6, 20, 13, 00, DateTimeZone.UTC).isBefore(gameDateTime)).isTrue();
        assertThat(new DateTime(2020, 6, 20, 13, 59, DateTimeZone.UTC).isBefore(gameDateTime)).isTrue();
        assertThat(new DateTime(2020, 6, 20, 13, 59, 59, DateTimeZone.UTC).isBefore(gameDateTime)).isTrue();
        assertThat(new DateTime(2020, 6, 20, 14, 00, DateTimeZone.UTC).isBefore(gameDateTime)).isFalse();
        assertThat(new DateTime(2020, 6, 20, 15, 00, DateTimeZone.UTC).isBefore(gameDateTime)).isFalse();
        assertThat(new DateTime(2020, 6, 20, 16, 00, DateTimeZone.UTC).isBefore(gameDateTime)).isFalse();
        assertThat(new DateTime(2020, 6, 20, 17, 00, DateTimeZone.UTC).isBefore(gameDateTime)).isFalse();
        assertThat(new DateTime(2020, 6, 20, 18, 00, DateTimeZone.UTC).isBefore(gameDateTime)).isFalse();
        assertThat(new DateTime(2020, 6, 20, 19, 00, DateTimeZone.UTC).isBefore(gameDateTime)).isFalse();
    }

}
