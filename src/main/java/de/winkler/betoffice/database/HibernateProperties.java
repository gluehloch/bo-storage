/*
 * $Id: HibernateProperties.java 3782 2013-07-27 08:44:32Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2008 by Andre Winkler. All rights reserved.
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

import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.GameTipp;
import de.winkler.betoffice.storage.Group;
import de.winkler.betoffice.storage.GroupType;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.Team;
import de.winkler.betoffice.storage.TeamAlias;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.UserSeason;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Definiert die Konstanten für die <code>hibernate.properties</code> Datei.
 * Diese Klasse kann eine <code>java.sql.Connection</code> oder eine Hibernate
 * <code>Configuration</code> erzeugen.
 * 
 * @author $Author: andrewinkler $
 * @version $Revision: 3782 $ $Date: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
public class HibernateProperties {

	/** hibernate.dialect */
	public static final String DIALECT = "hibernate.dialect";

	/** hibernate.connection.url */
	public static final String URL = "hibernate.connection.url";

	/** hibernate.connection.driver_class */
	public static final String DRIVER = "hibernate.connection.driver_class";

	/** hibernate.connection.username */
	public static final String USERNAME = "hibernate.connection.username";

	/** hibernate.connection.password */
	public static final String PASSWORD = "hibernate.connection.password";

	/** Alle Hibernate Properties in einem Array. */
	private static final String[] KEYS = { DIALECT, URL, DRIVER, USERNAME,
			PASSWORD };

	/** Der Logger der Klasse. */
	private final Log log = LogFactory.getLog(HibernateProperties.class);

	// ------------------------------------------------------------------------

	/** Hibernate Connection Properties. */
	private final Properties properties;

	/**
	 * Konstruktor.
	 *
	 * @param _properties Hibernate Connection Properties.
	 */
	public HibernateProperties(final Properties _properties) {
		properties = _properties;
	}

	/**
	 * Prüft, ob alle notwendigen Hibernate Properties vorhanden sind.
	 *
	 * @return Liefert <code>true</code> wenn alle Eigenschaften vorhanden
	 *     sind.
	 */
	public boolean validate() {
		boolean ok = true;
		for (String key : KEYS) {
			if (StringUtils.isBlank(properties.getProperty(key))) {
				log.info("Hibernate property '" + key + "' not set!");
				ok = false;
			}
		}
		return ok;
	}

	/**
	 * Erstellt eine Connection anhand der Daten einer hibernate.properties
	 * Datei bzw. eine Properties Objekts.
	 *
	 * @return Eine Connection zur Datenbank.
	 */
	public Connection createConnection() {
		Connection jdbcConnection = null;
		try {
			Class
				.forName(properties.get(HibernateProperties.DRIVER).toString());
			jdbcConnection = DriverManager.getConnection(properties
				.getProperty(HibernateProperties.URL), properties
				.getProperty(HibernateProperties.USERNAME), properties
				.getProperty(HibernateProperties.PASSWORD));
		} catch (SQLException ex) {
			log.error("connection not created", ex);
			throw new RuntimeException(ex);
		} catch (ClassNotFoundException ex) {
			log.error("connection not created", ex);
			throw new RuntimeException(ex);
		}
		return jdbcConnection;
	}

	/**
	 * Erstellt ein Hibernate <code>Configuration</code> Objekt anhand eines
	 * Property Objekts.
	 *
	 * @return Eine Hibernate Configuration.
	 */
	public Configuration createConfiguration() {
		Configuration config = new Configuration();
		config.addProperties(properties);
		config.addClass(Game.class);
		config.addClass(GameList.class);
		config.addClass(GameTipp.class);
		config.addClass(Group.class);
		config.addClass(GroupType.class);
		config.addClass(Season.class);
		config.addClass(Team.class);
		config.addClass(TeamAlias.class);
		config.addClass(User.class);
		config.addClass(UserSeason.class);
		return config;
	}

	// ------------------------------------------------------------------------

	/**
	 * Liefert die Connection.
	 *
	 * @param properties Hibernate Connection Properties.
	 * @return Eine SQL Connection.
	 */
	public static Connection createConnection(final Properties properties) {
		HibernateProperties hibernateProperties = new HibernateProperties(
			properties);
		return hibernateProperties.createConnection();
	}

	/**
	 * Liefert die Hibernate Configuration.
	 *
	 * @param properties Hibernate Connection Properties.
	 * @return Eine Hibernate Configuration.
	 */
	public static Configuration createConfiguration(final Properties properties) {
		HibernateProperties hibernateProperties = new HibernateProperties(
			properties);
		return hibernateProperties.createConfiguration();
	}

}
