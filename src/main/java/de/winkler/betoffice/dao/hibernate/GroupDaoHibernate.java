/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2016 by Andre Winkler. All
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

package de.winkler.betoffice.dao.hibernate;

import java.util.List;
import java.util.Optional;

import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean;

import de.winkler.betoffice.dao.GroupDao;
import de.winkler.betoffice.storage.Group;
import de.winkler.betoffice.storage.GroupType;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.Team;

/**
 * Implementierung von {@link GroupDao}.
 * 
 * @author by Andre Winkler
 */
@Repository("groupDao")
public class GroupDaoHibernate extends AbstractCommonDao<Group> implements
        GroupDao {

    /** Sucht nach allen Gruppen zu einer Meisterschaft. */
    private static final String QUERY_GROUPS_FROM_SEASON = AbstractCommonDao
            .loadQuery("hql_groups_season.sql");

    private static final String QUERY_TEAMS_BY_GROUP = AbstractCommonDao
            .loadQuery("query_teams_group.sql");

    /**
     * Sucht nach einer <code>Group</code>s anhand Meisterschaft und Gruppentyp.
     */
    private static final String QUERY_GROUP_BY_SEASON_AND_GROUPTYPE =
            "select grp from Group as grp inner join grp.season as season inner join grp.groupType as gt"
            + " where season.id = :seasonId"
            + " and gt.id = :groupTypeId";

    public GroupDaoHibernate() {
        super(Group.class);
    }

    @Override
    public List<Group> findAll() {
        @SuppressWarnings("unchecked")
        List<Group> groups = getSessionFactory().getCurrentSession()
                .createQuery("from group").getResultList();
        return groups;
    }

    @Override
    public List<Group> findBySeason(final Season season) {
        @SuppressWarnings("unchecked")
        List<Group> objects = getSessionFactory().getCurrentSession()
                .createQuery(QUERY_GROUPS_FROM_SEASON)
                .setParameter("seasonId", season.getId()).getResultList();
        return objects;
    }

    @Override
    public List<Team> findTeams(final Group group) {
        NativeQuery<Team> query = getSessionFactory().getCurrentSession()
                .createNativeQuery(QUERY_TEAMS_BY_GROUP, Team.class);
        query.setParameter("group_id", group.getId());

        List<Team> teams = query.getResultList();
        return teams;
    }

    @Override
    public Group findBySeasonAndGroupType(Season season, GroupType groupType) {
        Query<Group> query = getSessionFactory().getCurrentSession().createQuery(
                QUERY_GROUP_BY_SEASON_AND_GROUPTYPE, Group.class);
        query.setParameter("seasonId", season.getId());
        query.setParameter("groupTypeId", groupType.getId());
        return query.getSingleResult();
    }

}
