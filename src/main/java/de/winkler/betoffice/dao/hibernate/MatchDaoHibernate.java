/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2020 by Andre Winkler. All
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

package de.winkler.betoffice.dao.hibernate;

import java.util.List;
import java.util.Optional;

import javax.persistence.TypedQuery;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import de.winkler.betoffice.dao.MatchDao;
import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.Team;

/**
 * The DAO class for access on table bo_game.
 *
 * @author Andre Winkler
 */
@Repository("matchDao")
public class MatchDaoHibernate extends AbstractCommonDao<Game> implements MatchDao {

    /**
     * Sucht nach allen bekannten Spielpaarungen mit gesuchter Heimmannschaft.
     */
    private static final String QUERY_MATCHES_BY_HOMETEAM = "from "
            + Game.class.getName() + " as match "
            + "where match.homeTeam.id = :homeTeamId";

    /**
     * Sucht nach allen bekannten Spielpaarungen mit gesuchter Gastmannschaft.
     */
    private static final String QUERY_MATCHES_BY_GUESTTEAM = "from "
            + Game.class.getName() + " as match "
            + "where match.guestTeam.id = :guestTeamId";

    /**
     * Sucht nach allen bekannten Spielpaarungen mit gesuchter Heim- und
     * Gastmannschaft.
     */
    private static final String QUERY_MATCHES_BY_HOME_AND_GUEST_TEAM = "select match from "
            + Game.class.getName() + " as match "
            + "left join fetch match.goals " + "left join fetch match.location "
            + "where match.homeTeam.id = :homeTeamId"
            + " and match.guestTeam.id = :guestTeamId";

    /**
     * Sucht einer Spielpaarung für einen bestimmten Spieltag mit der gegebenen
     * Heim- und Gastmannschaft.
     */
    private static final String QUERY_MATCH_BY_HOME_AND_GUEST_TEAM_AND_ROUND = "select "
            + "    match " + "from " + "    Game as match " + "where "
            + "    match.homeTeam.id = :homeTeamId and "
            + "    match.guestTeam.id = :guestTeamId and "
            + "    match.gameList.id = :gameListId ";

    public MatchDaoHibernate() {
        super(Game.class);
    }

    @Override
    public List<Game> findByHomeTeam(final Team homeTeam) {
        @SuppressWarnings("unchecked")
        List<Game> games = getSessionFactory().getCurrentSession()
                .createQuery(QUERY_MATCHES_BY_HOMETEAM)
                .setParameter("homeTeamId", homeTeam.getId()).getResultList();
        return games;
    }

    @Override
    public List<Game> findByGuestTeam(final Team guestTeam) {
        @SuppressWarnings("unchecked")
        List<Game> games = getSessionFactory().getCurrentSession()
                .createQuery(QUERY_MATCHES_BY_GUESTTEAM)
                .setParameter("guestTeamId", guestTeam.getId()).getResultList();
        return games;
    }

    @Override
    public List<Game> find(final Team homeTeam, final Team guestTeam) {
        @SuppressWarnings("unchecked")
        List<Game> games = getSessionFactory().getCurrentSession()
                .createQuery(QUERY_MATCHES_BY_HOME_AND_GUEST_TEAM)
                .setParameter("homeTeamId", homeTeam.getId())
                .setParameter("guestTeamId", guestTeam.getId()).getResultList();

        return games;
    }

    @Override
    public List<Game> findAll(final Team team1, final Team team2) {
        List<Game> objects = find(team1, team2);
        objects.addAll(find(team2, team1));
        return objects;
    }

    @Override
    public Optional<Game> find(final GameList round, final Team homeTeam, final Team guestTeam) {
        Query<Game> query = getSessionFactory().getCurrentSession()
                .createQuery(QUERY_MATCH_BY_HOME_AND_GUEST_TEAM_AND_ROUND,
                        Game.class)
                .setParameter("homeTeamId", homeTeam.getId())
                .setParameter("guestTeamId", guestTeam.getId())
                .setParameter("gameListId", round.getId());
        return singleResult(query);
    }

    @Override
    public List<Game> find(GameList round) {
        TypedQuery<Game> query = getSessionFactory().getCurrentSession()
                .createQuery("from Game g where g.gameList.id = :roundId", Game.class)
                .setParameter("roundId", round.getId());
        return query.getResultList();
    }

}
