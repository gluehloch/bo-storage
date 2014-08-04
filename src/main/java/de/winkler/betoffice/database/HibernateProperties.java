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

}
