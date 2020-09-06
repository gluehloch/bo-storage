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

package de.winkler.betoffice.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.util.Properties;

import org.junit.Test;

/**
 * Testet das Auslesen der Property-Dateien.
 *
 * @author by Andre Winkler
 */
public class PropertyReaderTest {

    /** This file is supported by betoffice-testutils. */
    private static final String PROPERTY_FILE = "/de/betoffice/database/test/botest.properties";

    /**
     * Der Test mit {@link Properties} funktioniert.
     *
     * @throws Exception
     *             Ups
     */
    @Test
    public void testReadPropertyFiles() throws Exception {
        InputStream is = this.getClass().getResourceAsStream(PROPERTY_FILE);
        assertNotNull(is);
        Properties props = new Properties();
        props.load(is);

        assertEquals("test", props.getProperty("betoffice.persistence.username"));
        assertEquals("test", props.getProperty("betoffice.persistence.password"));
        assertEquals("jdbc:mariadb://127.0.0.1/botest", props.getProperty("betoffice.persistence.url"));
        assertEquals("org.mariadb.jdbc.Driver", props.getProperty("betoffice.persistence.classname"));
        assertEquals("org.hibernate.dialect.MariaDBDialect", props.getProperty("betoffice.persistence.dialect"));
    }

}
