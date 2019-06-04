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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.InputStream;
import java.util.Properties;

import org.junit.jupiter.api.Test;

/**
 * Testet das Auslesen der Property-Dateien.
 *
 * @author by Andre Winkler
 */
public class PropertyReaderTest {

    /** This file is supported by betoffice-testutils. */
    private static final String PROPERTY_FILE = "/botest.properties";

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

        assertThat(props.getProperty("betoffice.persistence.username"))
                .isEqualTo("test");
        assertThat(props.getProperty("betoffice.persistence.password"))
                .isEqualTo("test");
        assertThat(props.getProperty("betoffice.persistence.url"))
                .isEqualTo("jdbc:mysql://192.168.99.100/botest");
        assertThat(props.getProperty("betoffice.persistence.classname"))
                .isEqualTo("com.mysql.jdbc.Driver");
        assertThat(props.getProperty("betoffice.persistence.dialect"))
                .isEqualTo("org.hibernate.dialect.MySQLDialect");
    }

}
