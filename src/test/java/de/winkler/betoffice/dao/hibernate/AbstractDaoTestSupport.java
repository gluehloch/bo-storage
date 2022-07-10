/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2022 by Andre Winkler. All
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

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import de.betoffice.database.data.DeleteDatabase;
import de.dbload.Dbload;

/**
 * DAO test support. The test method runs in his own transaction. So a lazy-loading exception
 * should not happen here.
 *
 * @author by Andre Winkler
 */
@SpringJUnitConfig(locations = { "/betoffice-test-properties.xml", "/betoffice.xml" })
public abstract class AbstractDaoTestSupport extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SessionFactory sessionFactory;

    public void prepareDatabase(final Class<?> clazz) {
        deleteDatabase();
        sessionFactory.getCurrentSession().doWork(connection -> Dbload.read(connection, clazz));
    }

    @AfterEach
    public void deleteDatabase() {
        getSessionFactory().getCurrentSession().doWork(connection -> DeleteDatabase.deleteDatabase(connection));
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
