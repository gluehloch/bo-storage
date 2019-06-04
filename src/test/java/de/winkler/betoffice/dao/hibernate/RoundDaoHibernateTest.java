/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2019 by Andre Winkler. All
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

import java.util.Date;
import java.util.Optional;

import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.winkler.betoffice.dao.MatchDao;
import de.winkler.betoffice.dao.RoundDao;
import de.winkler.betoffice.dao.SeasonDao;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.Group;
import de.winkler.betoffice.storage.GroupType;
import de.winkler.betoffice.storage.Season;

/**
 * The test for {@link RoundDaoHibernate}.
 *
 * @author by Andre Winkler
 */
public class RoundDaoHibernateTest extends AbstractDaoTestSupport {

    @Autowired
    private RoundDao roundDao;

    @Autowired
    private MatchDao matchDao;

    @Autowired
    private SeasonDao seasonDao;

    @BeforeEach
    public void init() {
        prepareDatabase(RoundDaoHibernateTest.class);
    }

    @Test
    public void testCreateRound() {
        Season season = seasonDao.findById(1);
        season = seasonDao.findRoundGroupTeamUser(season);

        final Date now = new DateTime(2018, 5, 22, 17, 0, 0).toDate();

        final GameList newRound = new GameList();
        newRound.setDateTime(now);

        GroupType liga1 = season.getGroupTypes().get(0);
        Group liga1_1000 = season.getGroup(liga1);
        newRound.setGroup(liga1_1000);
        season.addGameList(newRound);

        // Achtung: dateTime ist vom Typ java.util.Date
        assertThat(newRound.getDateTime()).isInstanceOf(Date.class);
        assertThat(newRound.getDateTime())
                .isNotInstanceOf(java.sql.Timestamp.class);
        assertThat(newRound.getDateTime()).isEqualTo(now);

        roundDao.save(newRound);
        roundDao.refresh(newRound);
        
        assertThat(newRound.getDateTime().getTime()).isEqualTo(now.getTime());

        // nach save und flush aber ein java.util.Timestamp
        assertThat(newRound.getDateTime()).isInstanceOf(Date.class);
        assertThat(newRound.getDateTime())
                .isInstanceOf(java.sql.Timestamp.class);
        assertThat(newRound.getDateTime()).isNotEqualTo(now);
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
        assertThat(roundDao.findPrevious(0).isPresent()).isFalse();
        assertThat(roundDao.findPrevious(1).isPresent()).isFalse();
        assertThat(roundDao.findPrevious(2).get()).isEqualTo(1L);
        assertThat(roundDao.findPrevious(3).get()).isEqualTo(2L);
        assertThat(roundDao.findPrevious(4).get()).isEqualTo(3L);
        assertThat(roundDao.findPrevious(5).get()).isEqualTo(4L);
        assertThat(roundDao.findPrevious(6).isPresent()).isFalse();
    }

    @Test
    public void testFindNextTippRound() {
        // Everything as expected?
        DateTime matchDateTime = new DateTime(
                matchDao.findById(1L).getDateTime());
        assertThat(matchDateTime).isEqualTo(new DateTime(2016, 1, 5, 15, 0, 0));
        assertThat(matchDao.findById(18L).isPlayed()).isTrue();
        assertThat(matchDao.findById(19L).isPlayed()).isTrue();
        assertThat(matchDao.findById(20L).isPlayed()).isFalse();

        // Datum kurz vor dem Spieltag
        assertThat(roundDao.findNextTippRound(1L,
                new DateTime(2016, 1, 1, 0, 0, 0)).get()).isEqualTo(1L);
        assertThat(roundDao.findNextTippRound(1L,
                new DateTime(2016, 2, 1, 0, 0, 0)).get()).isEqualTo(2L);
        assertThat(roundDao.findNextTippRound(1L,
                new DateTime(2016, 3, 1, 0, 0, 0)).get()).isEqualTo(3L);
        assertThat(roundDao.findNextTippRound(1L,
                new DateTime(2016, 4, 1, 0, 0, 0)).get()).isEqualTo(4L);
        assertThat(roundDao.findNextTippRound(1L,
                new DateTime(2016, 5, 1, 0, 0, 0)).get()).isEqualTo(5L);
        assertThat(roundDao.findNextTippRound(1L,
                new DateTime(2016, 6, 1, 0, 0, 0)).isPresent()).isFalse();

        // Datum direkt am Spieltag
        assertThat(roundDao.findNextTippRound(1L,
                new DateTime(2016, 2, 5, 16, 0, 0)).get()).isEqualTo(2L);
        assertThat(roundDao.findNextTippRound(1L,
                new DateTime(2016, 5, 5, 13, 0, 0)).get()).isEqualTo(5L);
        assertThat(roundDao.findNextTippRound(1L,
                new DateTime(2016, 5, 5, 16, 0, 0)).get()).isEqualTo(5L);
        assertThat(roundDao.findNextTippRound(1L,
                new DateTime(2016, 5, 6, 16, 0, 0)).get()).isEqualTo(5L);
    }

    @Test
    public void testFindLastTippRound() {
        // Everything as expected?
        DateTime matchDateTime = new DateTime(
                matchDao.findById(1L).getDateTime());
        assertThat(matchDateTime).isEqualTo(new DateTime(2016, 1, 5, 15, 0, 0));
        assertThat(matchDao.findById(18L).isPlayed()).isTrue();
        assertThat(matchDao.findById(19L).isPlayed()).isTrue();
        assertThat(matchDao.findById(20L).isPlayed()).isFalse();

        // Datum kurz vor dem Spieltag
        assertThat(roundDao.findLastTippRound(1L,
                new DateTime(2016, 1, 1, 0, 0, 0)).isPresent()).isFalse();
        assertThat(roundDao.findLastTippRound(1L,
                new DateTime(2016, 2, 1, 0, 0, 0)).get()).isEqualTo(1L);
        assertThat(roundDao.findLastTippRound(1L,
                new DateTime(2016, 3, 1, 0, 0, 0)).get()).isEqualTo(2L);
        assertThat(roundDao.findLastTippRound(1L,
                new DateTime(2016, 4, 1, 0, 0, 0)).get()).isEqualTo(3L);
        assertThat(roundDao.findLastTippRound(1L,
                new DateTime(2016, 5, 1, 0, 0, 0)).get()).isEqualTo(4L);
        assertThat(roundDao.findLastTippRound(1L,
                new DateTime(2016, 6, 1, 0, 0, 0)).get()).isEqualTo(5L);

        // Datum direkt am Spieltag
        assertThat(roundDao.findLastTippRound(1L,
                new DateTime(2016, 2, 5, 16, 0, 0)).get()).isEqualTo(1L);
        assertThat(roundDao.findLastTippRound(1L,
                new DateTime(2016, 5, 5, 13, 0, 0)).get()).isEqualTo(4L);
        assertThat(roundDao.findLastTippRound(1L,
                new DateTime(2016, 5, 5, 16, 0, 0)).get()).isEqualTo(4L);
        assertThat(roundDao.findLastTippRound(1L,
                new DateTime(2016, 5, 6, 16, 0, 0)).get()).isEqualTo(5L);
    }

    @Test
    public void testFindLastRound() {
        Season season = seasonDao.findById(1l);
        Optional<GameList> lastRound = roundDao.findLastRound(season);
        assertThat(lastRound.get().getId()).isEqualTo(5L);
    }

}
