/*
 * $Id: PropertyReaderTest.java 3782 2013-07-27 08:44:32Z andrewinkler $
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

package de.winkler.betoffice.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.junit.Test;

import de.awtools.config.GlueConfig;
import de.awtools.config.PropertiesGlueConfig;

/**
 * Testet das Auslesen der Property-Dateien.
 * 
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3782 $ $LastChangedDate: 2010-08-01 05:43:29
 *          +0200 (Sun, 01 Aug 2010) $
 */
public class PropertyReaderTest {

	private static final String PROPERTY_FILE = "/de/winkler/betoffice/test/database/test-mysql-piratestest.properties";

	/**
	 * Der Test mit {@link Properties} funktioniert.
	 * 
	 * @throws Exception
	 *             Da ging was schief.
	 */
	@Test
	public void testReadPropertyFiles() throws Exception {
		InputStream is = this.getClass().getResourceAsStream(PROPERTY_FILE);
		assertNotNull(is);
		Properties props = new Properties();
		props.load(is);

		assertEquals("test", props.getProperty("hibernate.connection.username"));
		assertEquals("test", props.getProperty("hibernate.connection.password"));
		assertEquals("jdbc:mysql://localhost/botest",
				props.getProperty("hibernate.connection.url"));
		assertEquals("com.mysql.jdbc.Driver",
				props.getProperty("hibernate.connection.driver_class"));
		assertEquals("org.hibernate.dialect.MySQLDialect",
				props.getProperty("hibernate.dialect"));
	}

	/**
	 * Genau der gleiche Test funktioniert im Projekt gluehloch-util. Verlaesst
	 * der Code das Projekt und wird z.B. hier ausgefuehrt, liefert der Test
	 * einen Fehler zurueck.
	 * 
	 * @throws Exception
	 *             Da ging was schief.
	 */
	@Test
	public void testReadPropertyFileByPropertyHolder() throws Exception {
		URL resource = this.getClass().getResource(PROPERTY_FILE);
		PropertiesGlueConfig ph = new PropertiesGlueConfig(resource);
		ph.load();

		assertEquals("test", ph.getProperty("hibernate.connection.username"));
		assertEquals("test", ph.getProperty("hibernate.connection.password"));
	}

	/**
	 * Mit Commons-Configuration geht es auch noch.
	 * 
	 * @throws Exception
	 *             Da ging was schief.
	 */
	@Test
	public void testReadPropertyFileByCommonsConfiguration() throws Exception {
		URL resource = this.getClass().getResource(PROPERTY_FILE);
		PropertiesGlueConfig pc = new PropertiesGlueConfig(resource);
		pc.load();

		assertEquals("test", pc.getProperty("hibernate.connection.username"));
		assertEquals("test", pc.getProperty("hibernate.connection.password"));
	}

	/**
	 * Mit Commons-Configuration und interpoliert geht es auch noch?
	 * 
	 * @throws Exception
	 *             Da ging was schief.
	 */
	@Test
	public void testReadPropertyFileByCommonsConfigurationInterpolated()
			throws Exception {

		URL resource = this.getClass().getResource(PROPERTY_FILE);
		PropertiesGlueConfig pc = new PropertiesGlueConfig(resource);
		pc.load();
		GlueConfig gc = pc.interpolatedConfiguration();

		assertEquals("test", gc.getProperty("hibernate.connection.username"));
		assertEquals("test", gc.getProperty("hibernate.connection.password"));
	}

}
