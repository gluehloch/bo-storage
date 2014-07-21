/*
 * $Id: GroupTypeDaoHibernate.java 3782 2013-07-27 08:44:32Z andrewinkler $
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

package de.winkler.betoffice.dao.hibernate;

import java.util.List;

import org.springframework.stereotype.Repository;

import de.winkler.betoffice.dao.GroupTypeDao;
import de.winkler.betoffice.storage.GroupType;
import de.winkler.betoffice.storage.Season;

/**
 * Klasse für den Zugriff auf <code>Group</code> Objekte mit Hibernate.
 * 
 * @author $Author: andrewinkler $
 * @version $Revision: 3782 $ $Date: 2012-07-24 06:07:32 +0200 (Tue, 24 Jul
 *          2012) $
 */
@Repository("groupTypeDao")
public class GroupTypeDaoHibernate extends AbstractCommonDao<GroupType>
        implements GroupTypeDao {

    /** Sucht nach allen <code>GroupType</code>s mit einem bestimmten Namen. */
    private static final String QUERY_GROUPTYPE_BY_NAME = "from "
            + GroupType.class.getName() + " groupType " + "where "
            + "groupType.name = :groupTypeName order by groupType.name";

    private static final String QUERY_GROUPTYPES_BY_SEASON = AbstractCommonDao
            .loadQuery("hql_grouptype_season.sql");

    // ------------------------------------------------------------------------

    public GroupTypeDaoHibernate() {
        super(GroupType.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<GroupType> findAll() {
        return (getSessionFactory().getCurrentSession().createQuery(
                "from GroupType order by name").list());
    }

    @Override
    public GroupType findByName(final String name) {
        @SuppressWarnings("unchecked")
        List<GroupType> objects = getSessionFactory().getCurrentSession()
                .createQuery(QUERY_GROUPTYPE_BY_NAME)
                .setParameter("groupTypeName", name).list();

        if (objects.size() == 0) {
            return null;
        } else {
            return objects.get(0);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<GroupType> findBySeason(final Season season) {
        return getSessionFactory().getCurrentSession()
                .createQuery(QUERY_GROUPTYPES_BY_SEASON)
                .setParameter("season_id", season.getId()).list();
    }

}
