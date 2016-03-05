/*
 * $Id: GroupDaoHibernate.java 3782 2013-07-27 08:44:32Z andrewinkler $
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

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import de.winkler.betoffice.dao.GroupDao;
import de.winkler.betoffice.storage.Group;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.Team;

/**
 * Implementierung von {@link GroupDao}.
 * 
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3782 $ $LastChangedDate: 2012-07-27 19:20:45
 *          +0200 (Fri, 27 Jul 2012) $
 */
@Repository("groupDao")
public class GroupDaoHibernate extends AbstractCommonDao<Group> implements
        GroupDao {

    /** Sucht nach allen Gruppen zu einer Meisterschaft. */
    private static final String QUERY_GROUPS_FROM_SEASON = AbstractCommonDao
            .loadQuery("hql_groups_season.sql");

    private static final String QUERY_TEAMS_BY_GROUP = AbstractCommonDao
            .loadQuery("query_teams_group.sql");

    public GroupDaoHibernate() {
        super(Group.class);
    }

    @Override
    public List<Group> findAll() {
        @SuppressWarnings("unchecked")
        List<Group> groups = getSessionFactory().getCurrentSession()
                .createQuery("from group").list();
        return groups;
    }

    @Override
    public List<Group> findBySeason(final Season season) {
        @SuppressWarnings("unchecked")
        List<Group> objects = getSessionFactory().getCurrentSession()
                .createQuery(QUERY_GROUPS_FROM_SEASON)
                .setParameter("seasonId", season.getId()).list();
        return objects;
    }

    @Override
    public List<Team> findTeams(final Group group) {
        SQLQuery query = getSessionFactory().getCurrentSession()
                .createSQLQuery(QUERY_TEAMS_BY_GROUP)
                .addEntity("team", Team.class);
        query.setParameter("group_id", group.getId());

        @SuppressWarnings("unchecked")
        List<Team> teams = query.list();
        return teams;
    }

}
