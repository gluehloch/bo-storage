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

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.hibernate.jdbc.Work;
import org.junit.After;
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
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3782 $ $LastChangedDate: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
public class UserSeasonDaoHibernateTest extends AbstractDaoTestSupport {

    @Autowired
    private UserSeasonDao userSeasonDao;

    @Autowired
    private SeasonDao seasonDao;

    @Before
    public void init() {
        cleanUpDatabase();
        prepareDatabase(UserSeasonDaoHibernateTest.class);
    }

    @After
    public void shutdown() {
        cleanUpDatabase();
    }

    private void cleanUpDatabase() {
        getSessionFactory().getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                Statement stmt = connection.createStatement();
                stmt.execute("DELETE FROM bo_user_season");
                stmt.execute("DELETE FROM bo_user");
                stmt.execute("DELETE FROM bo_season");
                stmt.close();
            }
        });
    }

    @Test
    public void testUserSeasonDaoHibernate() {
        Season season = seasonDao.findByName("1. Bundesliga", "1999/2000");
        List<User> users = userSeasonDao.findUsers(season);
        assertEquals(3, users.size());
        assertEquals("Frosch", users.get(0).getNickName());
        assertEquals("Hattwig", users.get(1).getNickName());
        assertEquals("Peter", users.get(2).getNickName());
    }

}
