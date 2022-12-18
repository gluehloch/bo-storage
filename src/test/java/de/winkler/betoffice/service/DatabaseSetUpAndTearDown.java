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

package de.winkler.betoffice.service;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import de.betoffice.database.data.DatabaseTestData;
import de.betoffice.database.data.DatabaseTestData.DataLoader;
import de.betoffice.database.data.DeleteDatabase;

/**
 * Database test setup and tear down methods.
 * 
 * @author by Andre Winkler
 */
public final class DatabaseSetUpAndTearDown {

    private final DataSource dataSource;

    private DatabaseTestData mysql;

    public DatabaseSetUpAndTearDown(final DataSource _dataSource) {
        dataSource = _dataSource;
    }

    /**
     * Here we delete and set up the test database. This is an expensive
     * operation!.
     * 
     * @param _dataLoader The data for the setup
     * @throws Exception Problems?
     */
    public void setUp(final DataLoader _dataLoader) throws Exception {
        mysql = new DatabaseTestData();
        try (Connection conn = getConnection()) {
            mysql = new DatabaseTestData();
            mysql.setUp(conn, _dataLoader);
        }
    }
    
    public void tearDown() throws SQLException {
        try (Connection conn = getConnection()) {
            DeleteDatabase.deleteDatabase(conn);
        }
    }

    private Connection getConnection() throws SQLException {
        Connection conn = dataSource.getConnection();
        conn.setAutoCommit(false);
        return conn;
    }

}
