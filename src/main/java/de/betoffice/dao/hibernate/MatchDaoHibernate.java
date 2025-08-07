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

package de.betoffice.dao.hibernate;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import de.betoffice.dao.MatchDao;
import de.betoffice.storage.season.Game;
import de.betoffice.storage.season.GameList;
import de.betoffice.storage.team.Team;

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
    private static final String QUERY_MATCHES_BY_HOME_TEAM = """
            select
                match
            from
                Game as match
            where
                match.homeTeam.id = :homeTeamId
                and match.played = true
            order by
                match.dateTime desc
            """;

    /**
     * Sucht nach allen bekannten Spielpaarungen mit gesuchter Gastmannschaft.
     */
    private static final String QUERY_MATCHES_BY_GUEST_TEAM = """
            select
                match
            from
                Game as match
            where
                match.guestTeam.id = :guestTeamId
                and match.played = true
            order by
                match.dateTime desc
            """;

    /**
     * Sucht nach allen bekannten Spielpaarungen mit gesuchter Heim- und Gastmannschaft.
     */
    private static final String QUERY_MATCHES_BY_HOME_AND_GUEST_TEAM = """
            select
                match
            from
                Game as match
                left join fetch match.goals
                left join fetch match.location
            where
                match.homeTeam.id = :homeTeamId
                and match.guestTeam.id = :guestTeamId
                and match.played = true
            order by
                match.dateTime desc
            """;

    /**
     * Sucht nach allen bekannten Spielpaarungen mit gesuchter Heim- und Gastmannschaft bzw. umgekehrt.
     */
    private static final String QUERY_MATCHES_BY_HOME_AND_GUEST_TEAM_AND_REVERSE = """
            select
                match
            from
                Game as match
                left join fetch match.goals
                left join fetch match.location
            where
                (
                    (
                        match.homeTeam.id = :homeTeamId
                        and match.guestTeam.id = :guestTeamId
                    )
                    or
                    (
                        match.homeTeam.id = :guestTeamId
                        and match.guestTeam.id = :homeTeamId
                    )
                )
                and match.played = true
            order by
                match.dateTime desc
            """;

    private static final String QUERY_MATCHES_BY_TEAM = """
            select
                match
            from
                Game as match
                left join fetch match.goals
                left join fetch match.location
            where
                (
                    match.homeTeam.id = :teamId
                    or match.guestTeam.id = :teamId
                )
                and match.played = true
            order by
                match.dateTime desc
            """;
    /**
     * Sucht einer Spielpaarung für einen bestimmten Spieltag mit der gegebenen Heim- und Gastmannschaft.
     */
    private static final String QUERY_MATCH_BY_HOME_AND_GUEST_TEAM_AND_ROUND = """
            select
                match
            from
                Game as match
            where
                match.homeTeam.id = :homeTeamId
                and match.guestTeam.id = :guestTeamId
                and match.gameList.id = :gameListId
            """;

    public MatchDaoHibernate() {
        super(Game.class);
    }

    @Override
    public List<Game> findByHomeTeam(final Team homeTeam, final int limit) {
        List<Game> games = getEntityManager()
                .createQuery(QUERY_MATCHES_BY_HOME_TEAM, Game.class)
                .setParameter("homeTeamId", homeTeam.getId())
                .setMaxResults(limit)
                .getResultList();
        return games;
    }

    @Override
    public List<Game> findByGuestTeam(final Team guestTeam, int limit) {
        List<Game> games = getEntityManager()
                .createQuery(QUERY_MATCHES_BY_GUEST_TEAM, Game.class)
                .setParameter("guestTeamId", guestTeam.getId())
                .setMaxResults(limit)
                .getResultList();
        return games;
    }

    @Override
    public List<Game> find(final Team homeTeam, final Team guestTeam, int limit) {
        List<Game> games = getEntityManager()
                .createQuery(QUERY_MATCHES_BY_HOME_AND_GUEST_TEAM, Game.class)
                .setParameter("homeTeamId", homeTeam.getId())
                .setParameter("guestTeamId", guestTeam.getId())
                .setMaxResults(limit)
                .getResultList();
        return games;
    }

    @Override
    public List<Game> findAll(final Team homeTeam, final Team guestTeam, int limit) {
        List<Game> games = getEntityManager()
                .createQuery(QUERY_MATCHES_BY_HOME_AND_GUEST_TEAM_AND_REVERSE, Game.class)
                .setParameter("homeTeamId", homeTeam.getId())
                .setParameter("guestTeamId", guestTeam.getId())
                .setMaxResults(limit)
                .getResultList();
        return games;
    }

    @Override
    public List<Game> find(Team team, int limit) {
        List<Game> games = getEntityManager()
                .createQuery(QUERY_MATCHES_BY_TEAM, Game.class)
                .setParameter("teamId", team.getId())
                .setMaxResults(limit)
                .getResultList();
        return games;
    }

    @Override
    public Optional<Game> find(final GameList round, final Team homeTeam, final Team guestTeam) {
        TypedQuery<Game> query = getEntityManager()
                .createQuery(QUERY_MATCH_BY_HOME_AND_GUEST_TEAM_AND_ROUND, Game.class)
                .setParameter("homeTeamId", homeTeam.getId())
                .setParameter("guestTeamId", guestTeam.getId())
                .setParameter("gameListId", round.getId());
        return singleResult(query);
    }

    @Override
    public List<Game> find(GameList round) {
        TypedQuery<Game> query = getEntityManager()
                .createQuery("from Game g where g.gameList.id = :roundId order by g.dateTime", Game.class)
                .setParameter("roundId", round.getId());
        return query.getResultList();
    }

}
