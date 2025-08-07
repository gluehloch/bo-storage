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
 */

package de.betoffice.storage.season.dao;

import java.util.List;

import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import de.betoffice.dao.hibernate.AbstractCommonDao;
import de.betoffice.storage.season.GroupDao;
import de.betoffice.storage.season.entity.Group;
import de.betoffice.storage.season.entity.GroupType;
import de.betoffice.storage.season.entity.Season;
import de.betoffice.storage.team.entity.Team;

/**
 * Implementierung von {@link GroupDao}.
 * 
 * @author by Andre Winkler
 */
@Repository("groupDao")
public class GroupDaoHibernate extends AbstractCommonDao<Group> implements GroupDao {

    /** Sucht nach allen Gruppen zu einer Meisterschaft. */
    private static final String QUERY_GROUPS_FROM_SEASON = AbstractCommonDao
            .loadQuery("hql_groups_season.sql");

    private static final String QUERY_TEAMS_BY_GROUP = AbstractCommonDao
            .loadQuery("query_teams_group.sql");

    /**
     * Sucht nach einer <code>Group</code>s anhand Meisterschaft und Gruppentyp.
     */
    private static final String QUERY_GROUP_BY_SEASON_AND_GROUPTYPE =
            "select grp "
            + " from Group as grp "
            + "   inner join fetch grp.season as season "
            + "   inner join fetch grp.groupType as gt "
            + "   left join fetch grp.teams teams "
            + " where "
            + "   season.id = :seasonId"
            + "   and gt.id = :groupTypeId";

    public GroupDaoHibernate() {
        super(Group.class);
    }

    @Override
    public List<Group> findAll() {
        List<Group> groups = getEntityManager()
                .createQuery("select g from group g", Group.class).getResultList();
        return groups;
    }

    @Override
    public List<Group> findBySeason(final Season season) {
        List<Group> objects = getEntityManager()
                .createQuery(QUERY_GROUPS_FROM_SEASON, Group.class)
                .setParameter("seasonId", season.getId()).getResultList();
        return objects;
    }

    @Override
    public List<Team> findTeams(final Group group) {
        Query query = getEntityManager()
                .createNativeQuery(QUERY_TEAMS_BY_GROUP, Team.class)
                .setParameter("group_id", group.getId());

        List<Team> teams = query.getResultList();
        return teams;
    }

    @Override
    public Group findBySeasonAndGroupType(Season season, GroupType groupType) {
        TypedQuery<Group> query = getEntityManager()
                .createQuery(QUERY_GROUP_BY_SEASON_AND_GROUPTYPE, Group.class)
                .setParameter("seasonId", season.getId())
                .setParameter("groupTypeId", groupType.getId());
        return query.getSingleResult();
    }

}
