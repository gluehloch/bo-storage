/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2024 by Andre Winkler. All
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

package de.winkler.betoffice.conf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Creates a JDBC connection.
 *
 * @author Andre Winkler
 */
public class DatabaseConnectionFactory {

    private final String username;
    private final String password;
    private final String url;
    private final String classname;
    private final String dialect;
    
    public DatabaseConnectionFactory(String username, String password, String url, String classname, String dialect) {
        this.username = username;
        this.password = password;
        this.url = url;
        this.classname = classname;
        this.dialect = dialect;
    }
    
    public String username() {
        return username;
    }

    public String getClassname() {
        return classname;
    }

    public String getDialect() {
        return dialect;
    }

    public Connection createConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

}
