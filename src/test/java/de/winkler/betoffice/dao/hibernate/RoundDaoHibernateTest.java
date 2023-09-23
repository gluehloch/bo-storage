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

package de.winkler.betoffice.dao.hibernate;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.winkler.betoffice.dao.GroupDao;
import de.winkler.betoffice.dao.GroupTypeDao;
import de.winkler.betoffice.dao.MatchDao;
import de.winkler.betoffice.dao.RoundDao;
import de.winkler.betoffice.dao.SeasonDao;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.Group;
import de.winkler.betoffice.storage.GroupType;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.test.DateTimeDummyProducer;

/**
 * The test for {@link RoundDaoHibernate}.
 *
 * @author by Andre Winkler
 */
public class RoundDaoHibernateTest extends AbstractDaoTestSupport {

    private static final ZoneId ZONE_UTC = ZoneId.of("UTC");
    private static final ZoneId ZONE_EUROPE_PARIS = ZoneId.of("Europe/Paris");
    private static final ZoneId ZONE_EUROPE_BERLIN = ZoneId.of("Europe/Berlin");

    @Autowired
    private RoundDao roundDao;

    @Autowired
    private MatchDao matchDao;

    @Autowired
    private SeasonDao seasonDao;

    @Autowired
    private GroupTypeDao groupTypeDao;
    
    @Autowired
    private GroupDao groupDao;
    
    @BeforeEach
    public void init() {
        prepareDatabase(RoundDaoHibernateTest.class);
    }

    @Test
    public void testCreateRound() {
        Season season = seasonDao.findById(1);
        // season = seasonDao.findRoundGroupTeamUser(season);

        final ZonedDateTime now = DateTimeDummyProducer.DATE_2002_01_01;

        final GameList newRound = new GameList();
        newRound.setDateTime(now);

        GroupType liga1 = groupTypeDao.findByName("1. Liga").orElseThrow();
        Group liga1_1000 =  groupDao.findBySeasonAndGroupType(season, liga1);

        newRound.setGroup(liga1_1000);
        season.addGameList(newRound);

        assertThat(newRound.getDateTime()).isInstanceOf(ZonedDateTime.class);
        assertThat(newRound.getDateTime()).isNotInstanceOf(java.sql.Timestamp.class);
        assertThat(newRound.getDateTime()).isEqualTo(now);

        roundDao.save(newRound);
        roundDao.refresh(newRound);

        assertThat(newRound.getDateTime()).isEqualTo(now);

        // Durch die Umstellung auf ZonedDateTime ist das kein Thema mehr:
        // nach save und flush aber ein java.util.Timestamp
        // assertThat(newRound.getDateTime()).isInstanceOf(java.sql.Timestamp.class);
        // assertThat(newRound.getDateTime()).isNotEqualTo(now);
        // assertThat(now).isEqualTo(newRound.getDateTime());

        // Jetzt, mit Umstellung ZonedDateTime, geht das equalTo() in beide
        // Richtungen!!!
        assertThat(newRound.getDateTime()).isInstanceOf(ZonedDateTime.class);
        assertThat(newRound.getDateTime()).isEqualTo(now);
        assertThat(now).isEqualTo(newRound.getDateTime());

        // oder besser
        assertThat(newRound.getDateTime().compareTo(now)).isEqualTo(0);
    }

    @Test
    public void testFindNextRound() {
        assertThat(roundDao.findNext(0).isPresent()).isFalse();
        assertThat(roundDao.findNext(1).get()).isEqualTo(2L);
        assertThat(roundDao.findNext(2).get()).isEqualTo(3L);
        assertThat(roundDao.findNext(3).get()).isEqualTo(4L);
        assertThat(roundDao.findNext(4).get()).isEqualTo(5L);
        assertThat(roundDao.findNext(5).isPresent()).isFalse();
        assertThat(roundDao.findNext(6).isPresent()).isFalse();
    }

    @Test
    public void testFindPreviousRound() {
        assertThat(roundDao.findPrevious(0)).isEmpty();
        assertThat(roundDao.findPrevious(1)).isEmpty();
        assertThat(roundDao.findPrevious(2)).contains(1L);
        assertThat(roundDao.findPrevious(3)).contains(2L);
        assertThat(roundDao.findPrevious(4)).contains(3L);
        assertThat(roundDao.findPrevious(5)).contains(4L);
        assertThat(roundDao.findPrevious(6)).isEmpty();
    }

    @Test
    public void testFindNextTippRound() {
        // Everything as expected?
        ZonedDateTime matchDateTime = matchDao.findById(1L).getDateTime();
        assertThat(matchDateTime)
            .isEqualTo(ZonedDateTime.of(2016, 1, 5, 15, 0, 0, 0, ZONE_EUROPE_BERLIN))
            .isEqualTo(ZonedDateTime.of(2016, 1, 5, 15, 0, 0, 0, ZONE_EUROPE_PARIS));
        assertThat(matchDao.findById(18L).isPlayed()).isTrue();
        assertThat(matchDao.findById(19L).isPlayed()).isTrue();
        assertThat(matchDao.findById(20L).isPlayed()).isFalse();

        // Datum kurz vor dem Spieltag
        assertThat(roundDao.findNextTippRound(1L,
                ZonedDateTime.of(2016, 1, 1, 0, 0, 0, 0, ZONE_EUROPE_PARIS))
                .get()).isEqualTo(1L);
        assertThat(roundDao.findNextTippRound(1L,
                ZonedDateTime.of(2016, 2, 1, 0, 0, 0, 0, ZONE_EUROPE_PARIS))
                .get()).isEqualTo(2L);
        assertThat(roundDao.findNextTippRound(1L,
                ZonedDateTime.of(2016, 3, 1, 0, 0, 0, 0, ZONE_EUROPE_PARIS))
                .get()).isEqualTo(3L);
        assertThat(roundDao.findNextTippRound(1L,
                ZonedDateTime.of(2016, 4, 1, 0, 0, 0, 0, ZONE_EUROPE_PARIS))
                .get()).isEqualTo(4L);
        assertThat(roundDao.findNextTippRound(1L,
                ZonedDateTime.of(2016, 5, 1, 0, 0, 0, 0, ZONE_EUROPE_PARIS))
                .get()).isEqualTo(5L);
        assertThat(roundDao.findNextTippRound(1L,
                ZonedDateTime.of(2016, 6, 1, 0, 0, 0, 0, ZONE_EUROPE_PARIS))
                .isPresent()).isFalse();

        // Datum direkt am Spieltag
        assertThat(roundDao.findNextTippRound(1L,
                ZonedDateTime.of(2016, 2, 5, 16, 0, 0, 0, ZONE_EUROPE_PARIS))
                .get()).isEqualTo(2L);
        assertThat(roundDao.findNextTippRound(1L,
                ZonedDateTime.of(2016, 5, 5, 13, 0, 0, 0, ZONE_EUROPE_PARIS))
                .get()).isEqualTo(5L);
        assertThat(roundDao.findNextTippRound(1L,
                ZonedDateTime.of(2016, 5, 5, 16, 0, 0, 0, ZONE_EUROPE_PARIS))
                .get()).isEqualTo(5L);
        assertThat(roundDao.findNextTippRound(1L,
                ZonedDateTime.of(2016, 5, 6, 16, 0, 0, 0, ZONE_EUROPE_PARIS))
                .get()).isEqualTo(5L);
    }

    @Test
    public void zonedDateTimeComparison() {
        //
        // Winterzeit: 01.05.2016 - 1 Stunde Differenz zwischen UTC und Europe/Paris. D.h.
        // die UTC Zeit laeuft 1 Stunde hinterher. Europe/Paris(Winterzeit) -1h => UTC
        //
        ZonedDateTime zdtEuropeParisWinter = ZonedDateTime.of(
                LocalDateTime.of(LocalDate.of(2016, 1, 5), LocalTime.of(15, 0)),
                ZONE_EUROPE_PARIS);
        ZonedDateTime zdtCoordinatedUniversalTimeZoneWinter = ZonedDateTime.of(
                LocalDateTime.of(LocalDate.of(2016, 1, 5), LocalTime.of(14, 0)),
                ZONE_UTC);

        assertThat(zdtEuropeParisWinter.isBefore(zdtCoordinatedUniversalTimeZoneWinter)).isFalse();
        assertThat(zdtCoordinatedUniversalTimeZoneWinter.isBefore(zdtEuropeParisWinter)).isFalse();
        assertThat(zdtEuropeParisWinter).isEqualTo(zdtCoordinatedUniversalTimeZoneWinter);

        //
        // Sommerzeit: 06.05.2016 - 2 Stunden Differenz zwischen UTC und Europe/Paris. D.h.
        // die UTC Zeit laeuft 2 Stunden hinterher. Europe/Paris(Sommerzeit) -2h => UTC
        //
        ZonedDateTime zdtEuropeParisSommer = ZonedDateTime.of(
                LocalDateTime.of(LocalDate.of(2016, 6, 5), LocalTime.of(15, 0)),
                ZONE_EUROPE_PARIS);
        ZonedDateTime zdtCoordinatedUniversalTimeZoneSommer = ZonedDateTime.of(
                LocalDateTime.of(LocalDate.of(2016, 6, 5), LocalTime.of(13, 0)),
                ZONE_UTC);

        assertThat(zdtEuropeParisSommer.isBefore(zdtCoordinatedUniversalTimeZoneSommer)).isFalse();
        assertThat(zdtCoordinatedUniversalTimeZoneSommer.isBefore(zdtEuropeParisSommer)).isFalse();
        assertThat(zdtEuropeParisSommer).isEqualTo(zdtCoordinatedUniversalTimeZoneSommer);
    }

    @Test
    public void testFindLastTippRound() {
        // Everything as expected?
        ZonedDateTime matchDateTime = matchDao.findById(1L).getDateTime();

        assertThat(matchDateTime)
            .isEqualTo(ZonedDateTime.of(LocalDateTime.of(LocalDate.of(2016, 1, 5), LocalTime.of(15, 0)), ZONE_EUROPE_PARIS))
            .isEqualTo(ZonedDateTime.of(LocalDateTime.of(LocalDate.of(2016, 1, 5), LocalTime.of(14, 0)), ZONE_UTC));

        assertThat(matchDao.findById(18L).isPlayed()).isTrue();
        assertThat(matchDao.findById(19L).isPlayed()).isTrue();
        assertThat(matchDao.findById(20L).isPlayed()).isFalse();

        // Datum kurz vor dem Spieltag
        assertThat(roundDao.findLastTippRound(1L, ZonedDateTime.of(2016, 1, 1, 0, 0, 0, 0, ZONE_EUROPE_PARIS))).isNotPresent();
        assertThat(roundDao.findLastTippRound(1L, ZonedDateTime.of(2016, 2, 1, 0, 0, 0, 0, ZONE_EUROPE_PARIS))).contains(1L);
        assertThat(roundDao.findLastTippRound(1L, ZonedDateTime.of(2016, 3, 1, 0, 0, 0, 0, ZONE_EUROPE_PARIS))).contains(2L);
        assertThat(roundDao.findLastTippRound(1L, ZonedDateTime.of(2016, 4, 1, 0, 0, 0, 0, ZONE_EUROPE_PARIS))).contains(3L);
        assertThat(roundDao.findLastTippRound(1L, ZonedDateTime.of(2016, 5, 1, 0, 0, 0, 0, ZONE_EUROPE_PARIS))).contains(4L);
        assertThat(roundDao.findLastTippRound(1L, ZonedDateTime.of(2016, 6, 1, 0, 0, 0, 0, ZONE_EUROPE_PARIS))).contains(5L);

        // Datum direkt am Spieltag
        assertThat(roundDao.findLastTippRound(1L, ZonedDateTime.of(2016, 2, 5, 16, 0, 0, 0, ZONE_EUROPE_PARIS))).contains(2L);
        assertThat(roundDao.findLastTippRound(1L, ZonedDateTime.of(2016, 2, 5, 16, 0, 0, 0, ZONE_UTC))).contains(2L);
        assertThat(roundDao.findLastTippRound(1L, ZonedDateTime.of(2016, 2, 5, 15, 0, 0, 0, ZONE_UTC))).contains(2L);
        assertThat(roundDao.findLastTippRound(1L, ZonedDateTime.of(2016, 5, 5, 13, 0, 0, 0, ZONE_EUROPE_PARIS))).contains(4L);
        assertThat(roundDao.findLastTippRound(1L, ZonedDateTime.of(2016, 5, 5, 16, 0, 0, 0, ZONE_EUROPE_PARIS))).contains(5L);
        assertThat(roundDao.findLastTippRound(1L, ZonedDateTime.of(2016, 5, 6, 16, 0, 0, 0, ZONE_EUROPE_PARIS))).contains(5L);
    }

    @Test
    public void testFindLastRound() {
        Season season = seasonDao.findById(1l);
        Optional<GameList> lastRound = roundDao.findLastRound(season);
        assertThat(lastRound.get().getId()).isEqualTo(5L);
    }

}
