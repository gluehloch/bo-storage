/*
 * $Id: HibernateDaoTestSupport.java 3782 2013-07-27 08:44:32Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2011 by Andre Winkler. All rights reserved.
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

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.database.annotations.TestDataSource;
import org.unitils.database.annotations.Transactional;
import org.unitils.database.util.TransactionMode;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.dbunit.datasetloadstrategy.impl.CleanInsertLoadStrategy;
import org.unitils.orm.hibernate.annotation.HibernateSessionFactory;

import de.winkler.betoffice.test.database.HibernateConnectionFactory;
import de.winkler.betoffice.test.database.MySqlDatabasedTestSupport;

/**
 * Oberklasse für alle MySQL-Hibernate-DAO Tests. Die DAO Tests verwenden
 * zur Unterstützung das Unitils Framework.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3782 $ $LastChangedDate: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
@HibernateSessionFactory("test-mysql-hibernate.cfg.xml")
@Transactional(TransactionMode.COMMIT)
@DataSet(loadStrategy = CleanInsertLoadStrategy.class)
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class HibernateDaoTestSupport {

    @TestDataSource
    private DataSource dataSource;

    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    @HibernateSessionFactory
    protected SessionFactory sessionFactory;

    /**
     * @param sessionFactory the sessionFactory to set
     */
    @HibernateSessionFactory
    public void setSessionFactory(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * @return the sessionFactory
     */
    @HibernateSessionFactory
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Test
    public void testSessionFactoryAndDataSource() {
        Assert.assertNotNull(sessionFactory);
        Assert.assertNotNull(dataSource);
    }

    @BeforeClass
    public static void before() throws SQLException {
        HibernateConnectionFactory hcf = new HibernateConnectionFactory();
        Connection conn = hcf.createConnection();
        try {
            deleteAll(conn);
            conn.commit();
        } finally {
            conn.rollback();
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static void deleteAll(final Connection _conn) throws SQLException {
        MySqlDatabasedTestSupport testSupport = new MySqlDatabasedTestSupport();
        testSupport.deleteDatabase(_conn);
    }

}
