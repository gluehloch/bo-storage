/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2024 by Andre Winkler. All
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

package de.betoffice.dao.hibernate;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.betoffice.dao.GroupDao;
import de.betoffice.dao.GroupTypeDao;
import de.betoffice.dao.MatchDao;
import de.betoffice.dao.RoundDao;
import de.betoffice.dao.SeasonDao;
import de.betoffice.storage.season.GameList;
import de.betoffice.storage.season.Group;
import de.betoffice.storage.season.GroupType;
import de.betoffice.storage.season.Season;
import de.betoffice.test.DateTimeDummyProducer;

/**
 * The test for {@link RoundDaoHibernate}.
 *
 * @author by Andre Winkler
 */
public class RoundDaoHibernateTest extends AbstractDaoTestSupport {

    private static final long SEASON_ID = 1L;
    private static final long ROUND_1_ID = 1L;

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
    void init() {
        prepareDatabase(RoundDaoHibernateTest.class);
    }

    @Test
    void testCreateRound() {
        Season season = seasonDao.findById(1);
        // season = seasonDao.findRoundGroupTeamUser(season);

        final ZonedDateTime now = DateTimeDummyProducer.DATE_2002_01_01;

        final GameList newRound = new GameList();
        newRound.setDateTime(now);

        GroupType liga1 = groupTypeDao.findByName("1. Liga").orElseThrow();
        Group liga1_1000 = groupDao.findBySeasonAndGroupType(season, liga1);

        newRound.setGroup(liga1_1000);
        season.addGameList(newRound);

        assertThat(newRound.getDateTime()).isInstanceOf(ZonedDateTime.class);
        assertThat(newRound.getDateTime()).isNotInstanceOf(java.sql.Timestamp.class);
        assertThat(newRound.getDateTime()).isEqualTo(now);

        roundDao.persist(newRound);
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
    void testFindNextRound() {
        assertThat(roundDao.findNext(0).isPresent()).isFalse();
        assertThat(roundDao.findNext(1).get()).isEqualTo(2L);
        assertThat(roundDao.findNext(2).get()).isEqualTo(3L);
        assertThat(roundDao.findNext(3).get()).isEqualTo(4L);
        assertThat(roundDao.findNext(4).get()).isEqualTo(5L);
        assertThat(roundDao.findNext(5).isPresent()).isFalse();
        assertThat(roundDao.findNext(6).isPresent()).isFalse();
    }

    @Test
    void testFindPreviousRound() {
        assertThat(roundDao.findPrevious(0)).isEmpty();
        assertThat(roundDao.findPrevious(1)).isEmpty();
        assertThat(roundDao.findPrevious(2)).contains(SEASON_ID);
        assertThat(roundDao.findPrevious(3)).contains(2L);
        assertThat(roundDao.findPrevious(4)).contains(3L);
        assertThat(roundDao.findPrevious(5)).contains(4L);
        assertThat(roundDao.findPrevious(6)).isEmpty();
    }

    @Test
    void findNearestGameDate() {
        var dateTime_2016_01_01 = roundDao.findNearestGame(ZonedDateTime.of(2016, 1, 1, 0, 0, 0, 0, ZONE_EUROPE_BERLIN));
        assertThat(dateTime_2016_01_01.getYear()).isEqualTo(2016);
        assertThat(dateTime_2016_01_01.getMonth()).isEqualTo(Month.JANUARY);
        assertThat(dateTime_2016_01_01.getDayOfMonth()).isEqualTo(5);

        var dateTime_2016_02_01 = roundDao.findNearestGame(ZonedDateTime.of(2016, 2, 1, 0, 0, 0, 0, ZONE_EUROPE_BERLIN));
        assertThat(dateTime_2016_02_01.getYear()).isEqualTo(2016);
        assertThat(dateTime_2016_02_01.getMonth()).isEqualTo(Month.FEBRUARY);
        assertThat(dateTime_2016_02_01.getDayOfMonth()).isEqualTo(5);

        var dateTime_2016_05__18_00_00 = roundDao.findNearestGame(ZonedDateTime.of(2016, 3, 5, 18, 0, 0, 0, ZONE_EUROPE_BERLIN));
        assertThat(dateTime_2016_05__18_00_00.getYear()).isEqualTo(2016);
        assertThat(dateTime_2016_05__18_00_00.getMonth()).isEqualTo(Month.MARCH);
        assertThat(dateTime_2016_05__18_00_00.getDayOfMonth()).isEqualTo(5);
        assertThat(dateTime_2016_05__18_00_00.getHour()).isEqualTo(20);
    }

    @Test
    void findGamesWithTimeZoneEuropeOrUtc() {
        // Suche mit TZ Europe/Berlin liefert kein Ergebnis! (keine Normalisierung der Abfrage durch Hibernate!)
        var dt_2016_01_05__15_00_00_EuropeBerlin = ZonedDateTime.of(2016, 1, 5, 15, 0, 0, 0, ZONE_EUROPE_BERLIN);
        var gamesByEuropeBerlin = roundDao.findGames(dt_2016_01_05__15_00_00_EuropeBerlin);
        assertThat(gamesByEuropeBerlin).isEmpty();

        //
        // Suche mit TZ UTC liefert das gewünschte Ergebnis! Aber die Entity ...
        //
        var dt_2016_01_05__15_00_00_utc = ZonedDateTime.of(2016, 1, 5, 15, 0, 0, 0, ZONE_UTC);
        var gamesByUTC = roundDao.findGames(dt_2016_01_05__15_00_00_utc);
        assertThat(gamesByUTC).hasSize(2);
        assertThat(gamesByUTC.get(0).getDateTime()).isNotEqualTo(dt_2016_01_05__15_00_00_utc);
        assertThat(gamesByUTC.get(1).getDateTime()).isNotEqualTo(dt_2016_01_05__15_00_00_utc);
        //
        // ... enthält die normalisierten Zeitstempel. In dem Fall die Zeitstempel mit der TZ Europe/Berlin.
        //
        assertThat(gamesByUTC.get(0).getDateTime()).isEqualTo(dt_2016_01_05__15_00_00_EuropeBerlin);
        assertThat(gamesByUTC.get(1).getDateTime()).isEqualTo(dt_2016_01_05__15_00_00_EuropeBerlin);

        //
        // Folgerung: Die Suche mit UTC liefert das gewünschte Ergebnis. Die Entity enthält die normalisierten Zeitstempel Europe/Berlin.
        // Wie werden die Zeitstempel in der Datenbank gespeichert? MARIAD DB speichert alle Zeitstempel als UTC!
        //
        // D.h. alle Abfragen mit einem Zeitstempel müssen in UTC erfolgen.
        // 
    }

    @Test
    void testFindNextTippRound() {
        // Everything as expected?
        ZonedDateTime matchDateTime = matchDao.findById(SEASON_ID).getDateTime();
        assertThat(matchDateTime)
                .isNotEqualTo(ZonedDateTime.of(2016, 1, 5, 15, 0, 0, 0, ZONE_UTC))
                .isEqualTo(ZonedDateTime.of(2016, 1, 5, 15, 0, 0, 0, ZONE_EUROPE_BERLIN))
                .isEqualTo(ZonedDateTime.of(2016, 1, 5, 15, 0, 0, 0, ZONE_EUROPE_PARIS));
        assertThat(matchDao.findById(18L).isPlayed()).isTrue();
        assertThat(matchDao.findById(19L).isPlayed()).isTrue();
        assertThat(matchDao.findById(20L).isPlayed()).isFalse();

        // Datum kurz vor dem Spieltag
        assertThat(roundDao.findNextTippRound(SEASON_ID,
                ZonedDateTime.of(2016, 1, 1, 0, 0, 0, 0, ZONE_UTC))
                .get()).isEqualTo(ROUND_1_ID);
        assertThat(roundDao.findNextTippRound(SEASON_ID,
                ZonedDateTime.of(2016, 2, 1, 0, 0, 0, 0, ZONE_EUROPE_PARIS))
                .get()).isEqualTo(2L);
        assertThat(roundDao.findNextTippRound(SEASON_ID,
                ZonedDateTime.of(2016, 3, 1, 0, 0, 0, 0, ZONE_EUROPE_PARIS))
                .get()).isEqualTo(3L);
        assertThat(roundDao.findNextTippRound(SEASON_ID,
                ZonedDateTime.of(2016, 4, 1, 0, 0, 0, 0, ZONE_EUROPE_PARIS))
                .get()).isEqualTo(4L);
        assertThat(roundDao.findNextTippRound(SEASON_ID,
                ZonedDateTime.of(2016, 5, 1, 0, 0, 0, 0, ZONE_EUROPE_PARIS))
                .get()).isEqualTo(5L);
        assertThat(roundDao.findNextTippRound(SEASON_ID,
                ZonedDateTime.of(2016, 6, 1, 0, 0, 0, 0, ZONE_EUROPE_PARIS))
                .isPresent()).isFalse();

        // Datum direkt am Spieltag
        assertThat(roundDao.findNextTippRound(SEASON_ID,
                ZonedDateTime.of(2016, 2, 5, 16, 0, 0, 0, ZONE_EUROPE_PARIS))
                .get()).isEqualTo(2L);
        assertThat(roundDao.findNextTippRound(SEASON_ID,
                ZonedDateTime.of(2016, 5, 5, 13, 0, 0, 0, ZONE_EUROPE_PARIS))
                .get()).isEqualTo(5L);
        assertThat(roundDao.findNextTippRound(SEASON_ID,
                ZonedDateTime.of(2016, 5, 5, 16, 0, 0, 0, ZONE_EUROPE_PARIS))
                .get()).isEqualTo(5L);
        assertThat(roundDao.findNextTippRound(SEASON_ID,
                ZonedDateTime.of(2016, 5, 6, 16, 0, 0, 0, ZONE_EUROPE_PARIS))
                .get()).isEqualTo(5L);
    }

    @Test
    void zonedDateTimeComparison() {
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
    void testFindLastTippRound() {
        // Everything as expected?
        ZonedDateTime matchDateTime = matchDao.findById(SEASON_ID).getDateTime();

        assertThat(matchDateTime)
                .isEqualTo(ZonedDateTime.of(LocalDateTime.of(LocalDate.of(2016, 1, 5), LocalTime.of(15, 0)),
                        ZONE_EUROPE_PARIS))
                .isEqualTo(ZonedDateTime.of(LocalDateTime.of(LocalDate.of(2016, 1, 5), LocalTime.of(14, 0)), ZONE_UTC));

        assertThat(matchDao.findById(18L).isPlayed()).isTrue();
        assertThat(matchDao.findById(19L).isPlayed()).isTrue();
        assertThat(matchDao.findById(20L).isPlayed()).isFalse();

        // Datum kurz vor dem Spieltag
        assertThat(roundDao.findLastTippRound(SEASON_ID, ZonedDateTime.of(2016, 1, 1, 0, 0, 0, 0, ZONE_EUROPE_PARIS)))
                .isNotPresent();
        assertThat(roundDao.findLastTippRound(SEASON_ID, ZonedDateTime.of(2016, 2, 1, 0, 0, 0, 0, ZONE_EUROPE_PARIS)))
                .contains(SEASON_ID);
        assertThat(roundDao.findLastTippRound(SEASON_ID, ZonedDateTime.of(2016, 3, 1, 0, 0, 0, 0, ZONE_EUROPE_PARIS)))
                .contains(2L);
        assertThat(roundDao.findLastTippRound(SEASON_ID, ZonedDateTime.of(2016, 4, 1, 0, 0, 0, 0, ZONE_EUROPE_PARIS)))
                .contains(3L);
        assertThat(roundDao.findLastTippRound(SEASON_ID, ZonedDateTime.of(2016, 5, 1, 0, 0, 0, 0, ZONE_EUROPE_PARIS)))
                .contains(4L);
        assertThat(roundDao.findLastTippRound(SEASON_ID, ZonedDateTime.of(2016, 6, 1, 0, 0, 0, 0, ZONE_EUROPE_PARIS)))
                .contains(5L);

        // Datum direkt am Spieltag
        assertThat(roundDao.findLastTippRound(SEASON_ID, ZonedDateTime.of(2016, 2, 5, 18, 0, 0, 0, ZONE_EUROPE_PARIS)))
                .contains(2L);
        assertThat(roundDao.findLastTippRound(SEASON_ID, ZonedDateTime.of(2016, 2, 5, 16, 0, 0, 0, ZONE_UTC)))
                .contains(2L);
        assertThat(roundDao.findLastTippRound(SEASON_ID, ZonedDateTime.of(2016, 2, 5, 18, 0, 0, 0, ZONE_UTC)))
                .contains(2L);
        assertThat(roundDao.findLastTippRound(SEASON_ID, ZonedDateTime.of(2016, 5, 5, 13, 0, 0, 0, ZONE_EUROPE_PARIS)))
                .contains(4L);
        assertThat(roundDao.findLastTippRound(SEASON_ID, ZonedDateTime.of(2016, 5, 5, 21, 0, 0, 0, ZONE_EUROPE_PARIS)))
                .contains(5L);
        assertThat(roundDao.findLastTippRound(SEASON_ID, ZonedDateTime.of(2016, 5, 6, 16, 0, 0, 0, ZONE_EUROPE_PARIS)))
                .contains(5L);
    }

    @Test
    public void testFindLastRound() {
        Season season = seasonDao.findById(1l);
        Optional<GameList> lastRound = roundDao.findLastRound(season);
        assertThat(lastRound).isPresent();
        assertThat(lastRound.get().getId()).isEqualTo(5L);
    }

    @Test
    public void testFindFirstRound() {
        Season season = seasonDao.findById(1l);
        Optional<GameList> firstRound = roundDao.findFirstRound(season);
        assertThat(firstRound).isPresent();
        assertThat(firstRound.get().getId()).isEqualTo(SEASON_ID);
    }

}
