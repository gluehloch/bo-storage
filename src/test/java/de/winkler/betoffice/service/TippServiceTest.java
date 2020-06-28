package de.winkler.betoffice.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

public class TippServiceTest {

    @Test
    public void beforeOrAfterGameDateTimeWithDifferenTimeZone() {
        DateTime gameDateTime = new DateTime(2020, 6, 20, 16, 00, DateTimeZone.forID("Europe/Paris"));
        
        // Die UTC Zeitzone ist im Sommer zwei Stunden hinter 'Europe/Paris'
        // D.h. Sommerzeit: 'Europe/Paris' 16:00 Uhr => 'UTC'          14:00 oder
        //                  'UTC' 16 Uhr             => 'Europe/Paris' 18:00
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
