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

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.dbload.Dbload;

/**
 * DAO test support.
 *
 * @author by Andre Winkler
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/betoffice-datasource.xml",
        "/betoffice-persistence.xml", "/test-mysql-piratestest.xml" })
public abstract class AbstractDaoTestSupport extends
        AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SessionFactory sessionFactory;

    public void prepareDatabase(final Class<?> clazz) {
        deleteDatabase();
        sessionFactory.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                Dbload.start(connection, clazz);
            }
        });
    }

    public void deleteDatabase() {
        sessionFactory.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                Statement stmt = connection.createStatement();
                stmt.execute("UPDATE bo_season set bo_current_ref = NULL");
                stmt.execute("DELETE FROM bo_gametipp");
                stmt.execute("DELETE FROM bo_game");
                stmt.execute("DELETE FROM bo_gamelist");
                stmt.execute("DELETE FROM bo_team_group");
                stmt.execute("DELETE FROM bo_group");
                stmt.execute("DELETE FROM bo_user_season");
                stmt.execute("DELETE FROM bo_season");
                stmt.execute("DELETE FROM bo_teamalias");
                stmt.execute("DELETE FROM bo_team");
                stmt.execute("DELETE FROM bo_user");
                stmt.execute("DELETE FROM bo_grouptype");
                stmt.close();
            }
        });
    }

    /**
     * @return the dataSource
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * @return the sessionFactory
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
