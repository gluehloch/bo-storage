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
import de.betoffice.storage.group.entity.GroupTypeEntity;
import de.betoffice.storage.season.entity.GameEntity;
import de.betoffice.storage.season.entity.GameListEntity;
import de.betoffice.storage.season.entity.GroupEntity;
import de.betoffice.storage.season.entity.SeasonEntity;
import de.betoffice.storage.session.entity.SessionEntity;
import de.betoffice.storage.team.TeamAlias;
import de.betoffice.storage.team.entity.TeamEntity;
import de.betoffice.storage.tip.GameTippEntity;
import de.betoffice.storage.user.entity.UserEntity;

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
        classes.add(GameEntity.class);
        classes.add(GameListEntity.class);
        classes.add(GameTippEntity.class);
        classes.add(GroupEntity.class);
        classes.add(GroupTypeEntity.class);
        classes.add(SeasonEntity.class);
        classes.add(TeamEntity.class);
        classes.add(TeamAlias.class);
        classes.add(UserEntity.class);
        classes.add(SessionEntity.class);
        return super.createConfiguration(classes);
    }

}
