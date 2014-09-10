/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2014 by Andre Winkler. All rights reserved.
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.hibernate.jdbc.Work;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.winkler.betoffice.dao.UserDao;
import de.winkler.betoffice.storage.User;

/**
 * Test for class {@link UserDaoHibernate}.
 *
 * @author by Andre Winkler
 */
public class UserDaoHibernateTest extends AbstractDaoTestSupport {

    @Autowired
    private UserDao userDaoHibernate;

    @Before
    public void init() {
        cleanUpDatabase();
        prepareDatabase(UserDaoHibernateTest.class);
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
                stmt.execute("DELETE FROM bo_user");
                stmt.close();
            }
        });
    }

    @Test
    public void testUserDaoHibernateFindAll() {
        List<User> users = userDaoHibernate.findAll();
        assertEquals(3, users.size());
        assertEquals("Frosch", users.get(0).getNickName());
        assertEquals("Hattwig", users.get(1).getNickName());
        assertEquals("Peter", users.get(2).getNickName());
    }

    @Test
    public void testUserDaoHibernateFindByNickname() {
        User user = userDaoHibernate.findByNickname("Frosch");
        assertNotNull(user);
        assertEquals("Adam", user.getSurname());

        user = userDaoHibernate.findByNickname("fehler");
        assertNull(user);
    }

}
