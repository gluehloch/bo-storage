/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2019 by Andre Winkler. All
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

package de.winkler.betoffice.test;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import de.betoffice.database.data.DatabaseTestData;
import de.betoffice.database.data.DatabaseTestData.DataLoader;
import de.winkler.betoffice.service.DatabaseMaintenanceService;
import de.winkler.betoffice.service.MasterDataManagerService;
import de.winkler.betoffice.service.SeasonManagerService;

/**
 * Oberklasse für alle Service-Layer Tests.
 *
 * @author by Andre Winkler
 */
@SpringJUnitConfig(locations = { "/betoffice-datasource.xml",
        "/betoffice-persistence.xml", "/test-mysql-piratestest.xml" })
public abstract class ServiceTestSupport {

    @Autowired
    protected DataSource dataSource;

    @Autowired
    protected SeasonManagerService seasonManagerService;

    @Autowired
    protected MasterDataManagerService masterDataManagerService;

    @Autowired
    protected DatabaseMaintenanceService databaseMaintenanceService;

    private DatabaseTestData mysql;

    public final DatabaseTestData getMySqlDatabasedTestSupport() {
        return mysql;
    }

    public final void setUp(final DataLoader dataLoader) throws SQLException {
        mysql = new DatabaseTestData();
        Connection conn = dataSource.getConnection();
        conn.setAutoCommit(false);
        try {
            mysql.setUp(conn, dataLoader);
        } finally {
            conn.close();
        }
    }

}
