/*
 * $Id: ServiceTestSupport.java 3782 2013-07-27 08:44:32Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2012 by Andre Winkler. All rights reserved.
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

package de.winkler.betoffice.test;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.winkler.betoffice.service.DatabaseMaintenanceService;
import de.winkler.betoffice.service.MasterDataManagerService;
import de.winkler.betoffice.service.SeasonManagerService;
import de.winkler.betoffice.test.database.MySqlDatabasedTestSupport;
import de.winkler.betoffice.test.database.MySqlDatabasedTestSupport.DataLoader;
import de.winkler.betoffice.test.database.PersistenceTestSupport;

/**
 * Oberklasse für alle Service-Layer Tests.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3782 $ $LastChangedDate: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/betoffice-datasource.xml",
        "/betoffice-persistence.xml", "/test-mysql-piratestest.xml" })
public class ServiceTestSupport {

    @Autowired
    protected DataSource dataSource;

    @Autowired
    protected SeasonManagerService seasonManagerService;

    @Autowired
    protected MasterDataManagerService masterDataManagerService;

    @Autowired
    protected DatabaseMaintenanceService databaseMaintenanceService;

    private PersistenceTestSupport pts;
    private MySqlDatabasedTestSupport mysql;

    public final PersistenceTestSupport getPersistenceTestSupport() {
        return pts;
    }

    public final MySqlDatabasedTestSupport getMySqlDatabasedTestSupport() {
        return mysql;
    }

    @Test
    public void ignoreTheTest() {
        // Eclipse JUnit test runner expects a defined test.
    }

    public final void setUp(final DataLoader dataLoader) throws SQLException {
        mysql = new MySqlDatabasedTestSupport();
        Connection conn = dataSource.getConnection();
        conn.setAutoCommit(false);
        try {
            mysql.setUp(conn, dataLoader);
        } finally {
            conn.close();
        }
        pts = new PersistenceTestSupport();
    }

}
