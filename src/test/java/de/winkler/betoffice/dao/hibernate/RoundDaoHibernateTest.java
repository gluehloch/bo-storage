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
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.winkler.betoffice.dao.RoundDao;

/**
 * The test for {@link RoundDaoHibernate}.
 *
 * @author by Andre Winkler
 */
public class RoundDaoHibernateTest extends AbstractDaoTestSupport {

    @Autowired
    private RoundDao roundDaoHibernate;

    @Before
    public void init() {
        prepareDatabase(RoundDaoHibernateTest.class);
    }

    @Test
    public void testFindNextRound() {
        assertThat(roundDaoHibernate.findNext(0), nullValue());
        assertThat(roundDaoHibernate.findNext(1), equalTo(2L));
        assertThat(roundDaoHibernate.findNext(2), equalTo(3L));
        assertThat(roundDaoHibernate.findNext(3), equalTo(4L));
        assertThat(roundDaoHibernate.findNext(4), equalTo(5L));
        assertThat(roundDaoHibernate.findNext(5), nullValue());
        assertThat(roundDaoHibernate.findNext(6), nullValue());
    }

    @Test
    public void testFindPreviousRound() {
        assertThat(roundDaoHibernate.findPrevious(0), nullValue());
        assertThat(roundDaoHibernate.findPrevious(1), nullValue());
        assertThat(roundDaoHibernate.findPrevious(2), equalTo(1L));
        assertThat(roundDaoHibernate.findPrevious(3), equalTo(2L));
        assertThat(roundDaoHibernate.findPrevious(4), equalTo(3L));
        assertThat(roundDaoHibernate.findPrevious(5), equalTo(4L));
        assertThat(roundDaoHibernate.findPrevious(6), nullValue());
    }

}
