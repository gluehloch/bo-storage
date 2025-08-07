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

package de.betoffice.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.hibernate.cfg.Configuration;

import de.betoffice.database.hibernate.HibernateProperties;
import de.betoffice.storage.Session;
import de.betoffice.storage.season.entity.Game;
import de.betoffice.storage.season.entity.GameList;
import de.betoffice.storage.season.entity.Group;
import de.betoffice.storage.season.entity.GroupType;
import de.betoffice.storage.season.entity.Season;
import de.betoffice.storage.team.TeamAlias;
import de.betoffice.storage.team.entity.Team;
import de.betoffice.storage.tip.GameTipp;
import de.betoffice.storage.user.entity.User;

/**
 * Definiert die Konstanten für die <code>hibernate.properties</code> Datei.
 * Diese Klasse kann eine <code>java.sql.Connection</code> oder eine Hibernate
 * <code>Configuration</code> erzeugen.
 *
 * @author Andre Winkler
 */
public class BetofficeHibernateProperties extends HibernateProperties {

    /**
     * @param _properties
     */
    public BetofficeHibernateProperties(Properties _properties) {
        super(_properties);
    }

    /**
     * Erstellt ein Hibernate <code>Configuration</code> Objekt anhand eines
     * Property Objekts.
     *
     * @return Eine Hibernate Configuration.
     */
    public Configuration createConfiguration() {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        classes.add(Game.class);
        classes.add(GameList.class);
        classes.add(GameTipp.class);
        classes.add(Group.class);
        classes.add(GroupType.class);
        classes.add(Season.class);
        classes.add(Team.class);
        classes.add(TeamAlias.class);
        classes.add(User.class);
        classes.add(Session.class);
        return super.createConfiguration(classes);
    }

}
