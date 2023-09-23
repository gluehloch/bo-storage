/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2023 by Andre Winkler. All
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

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import de.winkler.betoffice.dao.TeamAliasDao;
import de.winkler.betoffice.storage.Team;
import de.winkler.betoffice.storage.TeamAlias;

/**
 * Die Hibernate Implementierung des DAO {@link TeamAliasDao}.
 *
 * @author by Andre Winkler
 */
@Repository("teamAliasDao")
public class TeamAliasDaoHibernate extends AbstractCommonDao<TeamAlias>
        implements TeamAliasDao {

    /** Sucht nach allen Teams mit einem bestimmten Namen. */
    private static final String QUERY_TEAMALIAS_BY_NAME = "select "
            + "t.id, t.bo_name, t.bo_longname, t.bo_shortname, t.bo_xshortname, "
            + "t.bo_logo, t.bo_teamtype, t.bo_location_ref, t.bo_openligaid "
            + "from bo_team t, bo_teamalias ta "
            + "where ta.bo_aliasname like :alias_name "
            + "  and ta.bo_team_ref = t.id";

    /** Sucht nach allen Alias Namen einer Mannschaft. */
    private static final String QUERY_TEAMALIAS_BY_TEAM = "select ta from "
            + TeamAlias.class.getName() + " as ta "
            + "where ta.team.id = :teamId";

    /** Sucht nach alle TeamAliase. */
    private static final String QUERY_TEAMALIAS_FINDALL = "select ta from "
            + TeamAlias.class.getName() + " as ta order by bo_aliasName";

    public TeamAliasDaoHibernate() {
        super(TeamAlias.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<TeamAlias> findAll() {
        return (getSessionFactory().getCurrentSession()
                .createQuery(QUERY_TEAMALIAS_FINDALL, TeamAlias.class).getResultList());
    }

    @Override
    public Optional<Team> findByAliasName(final String aliasName) {
        Query<Team> query = getSessionFactory().getCurrentSession()
                .createNativeQuery(QUERY_TEAMALIAS_BY_NAME, Team.class)
                .setParameter("alias_name", aliasName);

        return singleResult(query);
    }

    @Override
    public List<TeamAlias> findAliasNames(final Team team) {
        List<TeamAlias> teams = getSessionFactory().getCurrentSession()
                .createQuery(QUERY_TEAMALIAS_BY_TEAM, TeamAlias.class)
                .setParameter("teamId", team.getId()).getResultList();
        return teams;
    }

}
