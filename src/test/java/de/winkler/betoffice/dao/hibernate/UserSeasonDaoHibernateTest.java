/*
 * $Id: UserSeasonDaoHibernateTest.java 3782 2013-07-27 08:44:32Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2010 by Andre Winkler. All rights reserved.
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

package de.winkler.betoffice.dao.hibernate;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.winkler.betoffice.dao.UserSeasonDao;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.User;

/**
 * Test for class {@link UserSeasonDao}.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3782 $ $LastChangedDate: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
public class UserSeasonDaoHibernateTest extends HibernateDaoTestSupport {

    private UserSeasonDaoHibernate userSeasonDaoHibernate;

    private SeasonDaoHibernate seasonDaoHibernate;

    @Test
    public void testUserSeasonDaoHibernate() {
        Season season = seasonDaoHibernate.findByName("1. Bundesliga", "1999/2000");
        List<User> users = userSeasonDaoHibernate.findUsers(season);
        assertEquals(3, users.size());
        assertEquals("Frosch", users.get(0).getNickName());
        assertEquals("Hattwig", users.get(1).getNickName());
        assertEquals("Peter", users.get(2).getNickName());
    }

    @Before
    public void setUp() {
        seasonDaoHibernate = new SeasonDaoHibernate();
        seasonDaoHibernate.setSessionFactory(sessionFactory);
        
        userSeasonDaoHibernate = new UserSeasonDaoHibernate();
        userSeasonDaoHibernate.setSessionFactory(sessionFactory);
    }

}
