/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2014 by Andre Winkler. All
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

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.winkler.betoffice.dao.SeasonDao;
import de.winkler.betoffice.dao.UserSeasonDao;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.User;

/**
 * Test for class {@link UserSeasonDao}.
 *
 * @author Andre Winkler
 */
public class UserSeasonDaoHibernateTest extends AbstractDaoTestSupport {

    @Autowired
    private UserSeasonDao userSeasonDao;

    @Autowired
    private SeasonDao seasonDao;

    @Before
    public void init() {
        prepareDatabase(UserSeasonDaoHibernateTest.class);
    }

    @Test
    public void testUserSeasonDaoHibernate() {
        Optional<Season> season = seasonDao.findByName("1. Bundesliga", "1999/2000");
        List<User> users = userSeasonDao.findUsers(season.get());
        assertEquals(3, users.size());
        assertEquals("Frosch", users.get(0).getNickName());
        assertEquals("Hattwig", users.get(1).getNickName());
        assertEquals("Peter", users.get(2).getNickName());
    }

}
