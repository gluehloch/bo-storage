/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2026 by Andre Winkler. All
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

package de.betoffice.storage.season.dao;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import de.betoffice.storage.hibernate.AbstractCommonDao;
import de.betoffice.storage.season.MatchDao;
import de.betoffice.storage.season.entity.GameEntity;
import de.betoffice.storage.season.entity.GameListEntity;
import de.betoffice.storage.team.entity.TeamEntity;

/**
 * The DAO class for access on table bo_game.
 *
 * @author Andre Winkler
 */
@Repository("matchDao")
public class MatchDaoHibernate extends AbstractCommonDao<GameEntity> implements MatchDao {

    /**
     * Sucht nach allen bekannten Spielpaarungen mit gesuchter Heimmannschaft.
     */
    private static final String QUERY_MATCHES_BY_HOME_TEAM = """
            select
                match
            from
                GameEntity as match
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
                GameEntity as match
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
                GameEntity as match
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
                GameEntity as match
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
                GameEntity as match
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
                GameEntity as match
            where
                match.homeTeam.id = :homeTeamId
                and match.guestTeam.id = :guestTeamId
                and match.gameList.id = :gameListId
            """;

    private static final String QUERY_MATCHES_BY_DAY = """
            select
                game
            from
                GameEntity game
                join game.homeTeam
                join game.guestTeam
                join game.group
                join game.group.groupType
            where
                DATE(game.dateTime) = DATE(:date)
            """;

    public MatchDaoHibernate() {
        super(GameEntity.class);
    }

    @Override
    public List<GameEntity> findByDay(final ZonedDateTime date) {
        List<GameEntity> games = getEntityManager()
                .createQuery(QUERY_MATCHES_BY_DAY, GameEntity.class)
                .setParameter("date", date)
                .getResultList();
        return games;
    }

    @Override
    public List<GameEntity> findByHomeTeam(final TeamEntity homeTeam, final int limit) {
        List<GameEntity> games = getEntityManager()
                .createQuery(QUERY_MATCHES_BY_HOME_TEAM, GameEntity.class)
                .setParameter("homeTeamId", homeTeam.getId())
                .setMaxResults(limit)
                .getResultList();
        return games;
    }

    @Override
    public List<GameEntity> findByGuestTeam(final TeamEntity guestTeam, int limit) {
        List<GameEntity> games = getEntityManager()
                .createQuery(QUERY_MATCHES_BY_GUEST_TEAM, GameEntity.class)
                .setParameter("guestTeamId", guestTeam.getId())
                .setMaxResults(limit)
                .getResultList();
        return games;
    }

    @Override
    public List<GameEntity> find(final TeamEntity homeTeam, final TeamEntity guestTeam, int limit) {
        List<GameEntity> games = getEntityManager()
                .createQuery(QUERY_MATCHES_BY_HOME_AND_GUEST_TEAM, GameEntity.class)
                .setParameter("homeTeamId", homeTeam.getId())
                .setParameter("guestTeamId", guestTeam.getId())
                .setMaxResults(limit)
                .getResultList();
        return games;
    }

    @Override
    public List<GameEntity> findAll(final TeamEntity homeTeam, final TeamEntity guestTeam, int limit) {
        List<GameEntity> games = getEntityManager()
                .createQuery(QUERY_MATCHES_BY_HOME_AND_GUEST_TEAM_AND_REVERSE, GameEntity.class)
                .setParameter("homeTeamId", homeTeam.getId())
                .setParameter("guestTeamId", guestTeam.getId())
                .setMaxResults(limit)
                .getResultList();
        return games;
    }

    @Override
    public List<GameEntity> find(TeamEntity team, int limit) {
        List<GameEntity> games = getEntityManager()
                .createQuery(QUERY_MATCHES_BY_TEAM, GameEntity.class)
                .setParameter("teamId", team.getId())
                .setMaxResults(limit)
                .getResultList();
        return games;
    }

    @Override
    public Optional<GameEntity> find(final GameListEntity round, final TeamEntity homeTeam, final TeamEntity guestTeam) {
        TypedQuery<GameEntity> query = getEntityManager()
                .createQuery(QUERY_MATCH_BY_HOME_AND_GUEST_TEAM_AND_ROUND, GameEntity.class)
                .setParameter("homeTeamId", homeTeam.getId())
                .setParameter("guestTeamId", guestTeam.getId())
                .setParameter("gameListId", round.getId());
        return singleResult(query);
    }

    @Override
    public List<GameEntity> find(GameListEntity round) {
        TypedQuery<GameEntity> query = getEntityManager()
                .createQuery("from GameEntity g where g.gameList.id = :roundId order by g.dateTime", GameEntity.class)
                .setParameter("roundId", round.getId());
        return query.getResultList();
    }

    @Override
    public Optional<GameEntity> findByOpenligadbId(long openligadbId) {
        TypedQuery<GameEntity> query = getEntityManager().createQuery(
                """
                        select
                            match
                        from
                            GameEntity as match
                        where
                            match.openligaid = :openligadbId
                        """, GameEntity.class).setParameter("openligadbId", openligadbId);
        return singleResult(query);
    }

}
