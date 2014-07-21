/*
 * $Id: HibernateConnectionFactory.java 3782 2013-07-27 08:44:32Z andrewinkler $
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

package de.winkler.betoffice.test.database;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.cfg.Configuration;

import de.awtools.config.PropertiesGlueConfig;
import de.winkler.betoffice.database.CreateDatabaseSchema;
import de.winkler.betoffice.database.HibernateProperties;

/**
 * Handles Hibernate configuration and Hibernate properties.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3782 $ $LastChangedDate: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
public class HibernateConnectionFactory {

    /** Die Hibernate Configuration. */
    private Configuration config;

    /** Hibernate Eigenschaften zur Erstellung der Configuration. */
    private HibernateProperties hibernateProperties;

    public HibernateConnectionFactory() {
        URL resource = getClass().getResource(
                "test-mysql-piratestest.properties");

        PropertiesGlueConfig pc = new PropertiesGlueConfig(resource);
        try {
            pc.load();
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }

        hibernateProperties = new HibernateProperties(pc.getProperties());
        if (!hibernateProperties.validate()) {
            throw new IllegalStateException("Hibernate properties are not set!");
        }
        config = hibernateProperties.createConfiguration();
    }

    /**
     * Returns the Hibernate configuration.
     *
     * @return A Hibernate configuration
     */
    public Configuration getConfiguration() {
        return config;
    }

    /**
     * Returns the Hibernate properties.
     *
     * @return The Hibernate properties
     */
    public HibernateProperties getHibernateProperties() {
        return hibernateProperties;
    }

    /**
     * Creates and returns a connection.
     *
     * @return A database connection
     * @throws SQLException A SQL exception
     */
    public Connection createConnection() throws SQLException {
        Connection conn = hibernateProperties.createConnection();
        conn.setAutoCommit(false);
        return conn;
    }
    /**
     * Creates, if necessary, the database schema.
     */
    public void createDatabaseSchemaIfCurrentIsNotValid() {
        CreateDatabaseSchema cds = new CreateDatabaseSchema();
        if (!cds.validateSchema(config)) {
            cds.createSilently(config);
        }        
    }

}
