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

package de.betoffice.storage.team.dao;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.TypedQuery;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import de.betoffice.dao.hibernate.AbstractCommonDao;
import de.betoffice.storage.season.entity.GroupType;
import de.betoffice.storage.season.entity.Season;
import de.betoffice.storage.team.TeamDao;
import de.betoffice.storage.team.TeamType;
import de.betoffice.storage.team.entity.Team;

/**
 * Klasse für den Zugriff auf <code>Team</code> Objekte mit Hibernate.
 *
 * @author Andre Winkler
 */
@Repository("teamDao")
public class TeamDaoHibernate extends AbstractCommonDao<Team>
        implements TeamDao {

    private static final String QUERY_TEAMS_BY_SEASON_AND_GROUPTYPE = AbstractCommonDao
            .loadQuery("query_teams_by_season_and_grouptype.sql");

    /** Sucht nach allen Teams mit einem bestimmten Namen. */
    public static final String QUERY_TEAM_BY_NAME = "select team from Team as team where team.name = :teamName";

    /** Sucht nach allen Teams mit einem bestimmten Typen. */
    public static final String QUERY_TEAM_BY_TYPE = "select team from Team as team where team.teamType = :teamType order by team.name";

    /** Sucht nach einer Mannschaft anhand der openligadb ID. */
    public static final String QUERY_TEAM_BY_OPENLIGAID = "select team from Team as team where team.openligaid = :openligaid";

    // ------------------------------------------------------------------------

    public TeamDaoHibernate() {
        super(Team.class);
    }

    @Override
    public List<Team> findAll() {
        return getEntityManager()
                .createQuery("select team from Team as team order by team.name", Team.class)
                .getResultList();
    }

    @Override
    public List<Team> findTeams(TeamType teamType) {
        return getEntityManager()
                .createQuery(QUERY_TEAM_BY_TYPE, Team.class)
                .setParameter("teamType", teamType)
                .getResultList();
    }

    /* TODO Removed the round about search...
                        OR LOWER(team.longName) LIKE '%' || :filter || '%'
                        OR LOWER(team.shortName) LIKE '%' || :filter || '%'
                        OR LOWER(team.xshortName) LIKE '%' || :filter || '%'
                        OR LOWER(team.logo) LIKE '%' || :filter || '%' 
     */
    @Override
    public List<Team> findTeams(Optional<TeamType> teamType, String filter) {
        final String query = """
                SELECT
                    team
                FROM
                    Team as team
                WHERE
                    (
                        :filter IS NULL
                        OR LOWER(team.name) LIKE '%' || :filter || '%'
                    )
                    AND
                    (
                        :teamType IS NULL OR team.teamType = :teamType
                    )
                ORDER BY
                    team.name
                """;
        return getEntityManager()
                .createQuery(query, Team.class)
                .setParameter("teamType", teamType.orElse(null))
                .setParameter("filter", filter)
                .getResultList();
    }

    @Override
    public Optional<Team> findByName(final String name) {
        TypedQuery<Team> query = getEntityManager()
                .createQuery(QUERY_TEAM_BY_NAME, Team.class)
                .setParameter("teamName", name);
        return singleResult(query);
    }

    @Override
    public List<Team> findTeamsBySeasonAndGroup(final Season season, final GroupType groupType) {
        List<Team> teams = getEntityManager().unwrap(Session.class)
                .createNativeQuery(QUERY_TEAMS_BY_SEASON_AND_GROUPTYPE, Team.class)
                .setParameter("season_id", season.getId())
                .setParameter("grouptype_id", groupType.getId())
                .list();
        return teams;
    }

    @Override
    public Optional<Team> findByOpenligaid(long openligaid) {
        TypedQuery<Team> query = getEntityManager()
                .createQuery(QUERY_TEAM_BY_OPENLIGAID, Team.class)
                .setParameter("openligaid", openligaid);
        return singleResult(query);
    }

}
