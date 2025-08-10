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
 *
 */

package de.betoffice.storage.group.dao;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import de.betoffice.storage.group.GroupTypeDao;
import de.betoffice.storage.group.entity.GroupType;
import de.betoffice.storage.hibernate.AbstractCommonDao;
import de.betoffice.storage.season.entity.Season;

/**
 * Klasse für den Zugriff auf <code>Group</code> Objekte mit Hibernate.
 * 
 * @author Andre Winkler
 */
@Repository("groupTypeDao")
public class GroupTypeDaoHibernate extends AbstractCommonDao<GroupType>
        implements GroupTypeDao {

    /** Sucht nach allen <code>GroupType</code>s mit einem bestimmten Namen. */
    private static final String QUERY_GROUPTYPE_BY_NAME = "from "
            + GroupType.class.getName() + " groupType " + "where "
            + "groupType.name = :groupTypeName order by groupType.name";

    private static final String QUERY_GROUPTYPES_BY_SEASON = AbstractCommonDao
            .loadQuery(GroupTypeDaoHibernate.class, "hql_grouptype_season.sql");

    // ------------------------------------------------------------------------

    public GroupTypeDaoHibernate() {
        super(GroupType.class);
    }

    @Override
    public List<GroupType> findAll() {
        return getEntityManager()
                .createQuery("from GroupType order by name", GroupType.class)
                .getResultList();
    }

    @Override
    public Optional<GroupType> findByName(final String name) {
        TypedQuery<GroupType> query = getEntityManager()
                .createQuery(QUERY_GROUPTYPE_BY_NAME, GroupType.class)
                .setParameter("groupTypeName", name);

        return singleResult(query);
    }

    @Override
    public List<GroupType> findBySeason(final Season season) {
        return getEntityManager()
                .createQuery(QUERY_GROUPTYPES_BY_SEASON, GroupType.class)
                .setParameter("season_id", season.getId()).getResultList();
    }

}
