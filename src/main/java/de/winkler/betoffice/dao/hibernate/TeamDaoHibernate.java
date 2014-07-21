/*
 * $Id: TeamDaoHibernate.java 3782 2013-07-27 08:44:32Z andrewinkler $
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

import de.winkler.betoffice.dao.TeamDao;
import de.winkler.betoffice.storage.GroupType;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.Team;
import de.winkler.betoffice.storage.enums.TeamType;

/**
 * Klasse für den Zugriff auf <code>Team</code> Objekte mit Hibernate.
 *
 * @author  $Author: andrewinkler $
 * @version $Revision: 3782 $ $Date: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
@Repository("teamDao")
public class TeamDaoHibernate extends AbstractCommonDao<Team> implements
        TeamDao {

    private static final String QUERY_TEAMS_BY_SEASON_AND_GROUPTYPE = AbstractCommonDao
            .loadQuery("query_teams_by_season_and_grouptype.sql");

    /** Sucht nach allen Teams mit einem bestimmten Namen. */
    public static final String QUERY_TEAM_BY_NAME = "from Team as team where team.name = :teamName";

    /** Sucht nach allen Teams mit einem bestimmten Typen. */
    public static final String QUERY_TEAM_BY_TYPE = "from Team as team where team.teamType = :teamType order by team.name";

    // ------------------------------------------------------------------------

    public TeamDaoHibernate() {
        super(Team.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Team> findAll() {
        return getSessionFactory().getCurrentSession()
                .createQuery("from Team as team order by team.name").list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Team> findTeams(TeamType teamType) {
        return getSessionFactory().getCurrentSession()
                .createQuery(QUERY_TEAM_BY_TYPE)
                .setParameter("teamType", teamType).list();
    }

    @Override
    public Team findByName(final String name) {
        @SuppressWarnings("unchecked")
        List<Team> teams = getSessionFactory().getCurrentSession()
                .createQuery(QUERY_TEAM_BY_NAME).setParameter("teamName", name)
                .list();

        if (teams.size() == 0) {
            return null;
        } else {
            return teams.get(0);
        }
    }

    @Override
    public List<Team> findTeamsBySeasonAndGroup(final Season season,
            final GroupType groupType) {

        @SuppressWarnings("unchecked")
        List<Team> teams = getSessionFactory().getCurrentSession()
                .createSQLQuery(QUERY_TEAMS_BY_SEASON_AND_GROUPTYPE)
                .addEntity("team", Team.class)
                .setParameter("season_id", season.getId())
                .setParameter("grouptype_id", groupType.getId()).list();
        return teams;
    }

}
