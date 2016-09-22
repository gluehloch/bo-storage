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

package de.winkler.betoffice.dao.hibernate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.winkler.betoffice.dao.MatchDao;
import de.winkler.betoffice.dao.RoundDao;
import de.winkler.betoffice.dao.SeasonDao;
import de.winkler.betoffice.storage.GameList;
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

    @Before
    public void init() {
        prepareDatabase(RoundDaoHibernateTest.class);
    }

    @Test
    public void testFindNextRound() {
        assertThat(roundDao.findNext(0).isPresent(), is(false));
        assertThat(roundDao.findNext(1).get(), equalTo(2L));
        assertThat(roundDao.findNext(2).get(), equalTo(3L));
        assertThat(roundDao.findNext(3).get(), equalTo(4L));
        assertThat(roundDao.findNext(4).get(), equalTo(5L));
        assertThat(roundDao.findNext(5).isPresent(), is(false));
        assertThat(roundDao.findNext(6).isPresent(), is(false));
    }

    @Test
    public void testFindPreviousRound() {
        assertThat(roundDao.findPrevious(0).isPresent(), is(false));
        assertThat(roundDao.findPrevious(1).isPresent(), is(false));
        assertThat(roundDao.findPrevious(2).get(), equalTo(1L));
        assertThat(roundDao.findPrevious(3).get(), equalTo(2L));
        assertThat(roundDao.findPrevious(4).get(), equalTo(3L));
        assertThat(roundDao.findPrevious(5).get(), equalTo(4L));
        assertThat(roundDao.findPrevious(6).isPresent(), is(false));
    }

    @Test
    public void testFindTippRound() {
        // Everything as expected?
        DateTime matchDateTime = new DateTime(
                matchDao.findById(1L).getDateTime());
        assertThat(matchDateTime, equalTo(new DateTime(2016, 1, 5, 15, 0, 0)));
        assertThat(matchDao.findById(18L).isPlayed(), is(true));
        assertThat(matchDao.findById(19L).isPlayed(), is(true));
        assertThat(matchDao.findById(20L).isPlayed(), is(false));

        // Datum kurz vor dem Spieltag
        assertThat(roundDao.findNextTippRound(1L,
                new DateTime(2016, 1, 1, 0, 0, 0)).get(), equalTo(1L));
        assertThat(roundDao.findNextTippRound(1L,
                new DateTime(2016, 2, 1, 0, 0, 0)).get(), equalTo(2L));
        assertThat(roundDao.findNextTippRound(1L,
                new DateTime(2016, 3, 1, 0, 0, 0)).get(), equalTo(3L));
        assertThat(roundDao.findNextTippRound(1L,
                new DateTime(2016, 4, 1, 0, 0, 0)).get(), equalTo(4L));
        assertThat(roundDao.findNextTippRound(1L,
                new DateTime(2016, 5, 1, 0, 0, 0)).get(), equalTo(5L));
        assertThat(roundDao.findNextTippRound(1L,
                new DateTime(2016, 6, 1, 0, 0, 0)).isPresent(), is(false));

        // Datum direkt am Spieltag
        assertThat(roundDao.findNextTippRound(1L,
                new DateTime(2016, 2, 5, 16, 0, 0)).get(), equalTo(2L));
        assertThat(roundDao.findNextTippRound(1L,
                new DateTime(2016, 5, 5, 13, 0, 0)).get(), equalTo(5L));
        assertThat(roundDao.findNextTippRound(1L,
                new DateTime(2016, 5, 5, 16, 0, 0)).get(), equalTo(5L));
        assertThat(roundDao.findNextTippRound(1L,
                new DateTime(2016, 5, 6, 16, 0, 0)).get(), equalTo(5L));
    }

    @Test
    public void testFindLastRound() {
        Season season = seasonDao.findById(1l);
        Optional<GameList> lastRound = roundDao.findLastRound(season);
        assertThat(lastRound.get().getId(), equalTo(5L));
    }

}
