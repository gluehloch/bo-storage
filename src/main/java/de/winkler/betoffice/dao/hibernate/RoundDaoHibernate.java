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

package de.winkler.betoffice.dao.hibernate;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import de.winkler.betoffice.dao.RoundDao;
import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.Group;
import de.winkler.betoffice.storage.Season;

/**
 * Eine Hibernate-DAO Implementierung zur Verwaltung eines Spieltags.
 * 
 * @author by Andre Winkler
 */
@Repository("roundDao")
public class RoundDaoHibernate extends AbstractCommonDao<GameList> implements RoundDao {

    /**
     * Sucht nach allen Spieltagen einer Meisterschaft.
     */
    private static final String QUERY_GAMELIST_BY_SEASON = """
            select
                gl
            from
                GameList gl
            where
                gl.season.id = :seasonId
            order by
                gl.index
            """;

    /**
     * Sucht nach dem letzten Spieltag einer Meisterschaft.
     */
    private static final String QUERY_LAST_GAMELIST_BY_SEASON = """
            select
                gl
            from
                GameList gl
            where
                gl.season.id = :seasonId
                and gl.index =
                (
                    select
                        max(index)
                    from
                        GameList gl2
                    where
                        gl2.season.id = gl.season.id
                )
            """;

    /**
     * Sucht nach dem ersten Spieltag einer Meisterschaft.
     */
    private static final String QUERY_FIRST_GAMELIST_BY_SEASON = """
            select
            	gl
            from
                  	GameList gl
                  where
            	gl.season.id = :seasonId
                and gl.index =
                (
            		select
                  		min(index)
                	from
                  		GameList gl2
                  	where
                  		gl2.season.id = gl.season.id
                )
                  """;

    /**
     * Sucht nach allen Spieltagen einer Meisterschaft fuer eine bestimmte Gruppe.
     */
    private static final String QUERY_GAMELIST_AND_GAMES_BY_SEASON_GROUP = "select "
            + "distinct round "
            + "from "
            + "    GameList as round "
            + "    left join fetch round.gameList game "
            + "where "
            + "    game.group.id = :groupId "
            + "order"
            + "    by round.index";

    /** Sucht nach einem Spieltag zu einen Spiel. */
    private static final String QUERY_GAMELIST_OF_GAME = "select round "
            + "from "
            + "    GameList as round "
            + "    join round.gameList game "
            + "where "
            + "     game.id = :gameId";

    /**
     * Sucht nach allen Gruppen, Spielen, Mannschaften einer Spielrunde/Spieltag.
     */
    private static final String QUERY_ROUND_AND_GAMES_BY_INDEX = "select round "
            + "from "
            + "    GameList as round "
            + "    left join fetch round.gameList game "
            + "    left join fetch game.homeTeam "
            + "    left join fetch game.guestTeam "
            + "    left join fetch game.group "
            + "where "
            + "    round.season.id = :seasonId "
            + "    and round.index = :index";

    /**
     * Sucht nach allen Gruppen, Spielen, Mannschaften einer Spielrunde/Spieltag.
     */
    private static final String QUERY_ROUND_AND_GAMES_BY_ID = "select round "
            + "from "
            + "    GameList as round "
            + "    left join fetch round.gameList game "
            + "    left join fetch game.homeTeam "
            + "    left join fetch game.guestTeam "
            + "    left join fetch game.group "
            + "where "
            + "    round.id = :roundId";

    /** Findet den naechsten zu tippenden Spieltag. */
    private static final String QUERY_NEXT_ROUND_BY_DATE = """
            select
                gl.bo_datetime datetime,
                gl.id last_round_id
            from
                bo_gamelist gl
            where
                gl.bo_datetime =
                (
                    select
                        min(t.bo_datetime) datetime
                    from
                    (
                        select
                            r.bo_datetime,
                            r.id
                        from
                            bo_gamelist r,
                            bo_game m
                        where
                            r.bo_season_ref = :season_id
                            and r.id = m.bo_gamelist_ref
                            and m.bo_datetime >= :date
                    ) as t
                )
            """;

    /** Findet den naechsten zu tippenden Spieltag. Version 2.0 */
    private static final String QUERY_NEXT_GAME_BY_SEASON_AND_DATE = """
            select
                gl.bo_datetime round_datetime,
                gl.id          last_round_id,
                g.bo_datetime  game_datetime
            from
                bo_gamelist gl
                join bo_game g on (g.bo_gamelist_ref = gl.id)
            where
                gl.bo_season_ref = :season_id
                and g.bo_datetime =
                (
                    select
                        min(g2.bo_datetime)
                    from
                        bo_game g2
                    where
                        g2.bo_datetime > :date
                )
            """;

    /** Findet den naechsten zu tippenden Spieltag. Version 2.0 */
    private static final String QUERY_NEXT_GAME_BY_SEASON = """
            select
                gl.bo_datetime round_datetime,
                gl.id          last_round_id,
                g.bo_datetime  game_datetime
            from
                bo_gamelist gl
                join bo_game g on (g.bo_gamelist_ref = gl.id)
            where
                g.bo_datetime =
                (
                    select
                        min(g2.bo_datetime)
                    from
                        bo_game g2
                    where
                        g2.bo_datetime > :date
                )
            """;

    private static final String QUERY_MIN_GAME_BY_DATE = """
            select
                min(g2.bo_datetime)
            from
                bo_game g2
            where
                g2.bo_datetime > :date
            """;

    private static final String QUERY_GAME_BY_DATE = """
            select
                g
            from
                Game g
            where
                g.dateTime = :date
            """;

    /** Findet den letzten zu tippenden Spieltag. */
    private static final String QUERY_LAST_ROUND_BY_DATE = "select gl.bo_datetime datetime, gl.id last_round_id "
            + "from bo_gamelist gl "
            + "where gl.bo_datetime = "
            + "("
            + "    select "
            + "        max(t.bo_datetime)"
            + "    from "
            + "    ("
            + "        select "
            + "            r.bo_datetime, r.id"
            + "        from "
            + "            bo_gamelist r, bo_game m"
            + "        where "
            + "            r.bo_season_ref = :season_id"
            + "            and r.id = m.bo_gamelist_ref "
            + "            and m.bo_datetime < :date"
            + "    ) as t"
            + ")";

    /**
     * Search for the next game day id.
     */
    private static final String QUERY_NEXT_ROUND = AbstractCommonDao.loadQuery("query_next_round.sql");

    /**
     * Search for the last game day id.
     */
    private static final String QUERY_PREV_ROUND = AbstractCommonDao.loadQuery("query_prev_round.sql");

    public RoundDaoHibernate() {
        super(GameList.class);
    }

    @Override
    public List<GameList> findRounds(Season season) {
        List<GameList> objects = getEntityManager()
                .createQuery(QUERY_GAMELIST_BY_SEASON, GameList.class)
                .setParameter("seasonId", season.getId())
                .getResultList();
        return objects;
    }

    @Override
    public List<GameList> findRounds(Group group) {
        List<GameList> objects = getEntityManager()
                .createQuery(QUERY_GAMELIST_AND_GAMES_BY_SEASON_GROUP, GameList.class)
                .setParameter("groupId", group.getId())
                .getResultList();
        return objects;
    }

    @Override
    public Optional<GameList> findRound(Season season, int index) {
        TypedQuery<GameList> query = getEntityManager()
                .createQuery(QUERY_ROUND_AND_GAMES_BY_INDEX, GameList.class)
                .setParameter("seasonId", season.getId())
                .setParameter("index", Integer.valueOf(index));

        return singleResult(query);
    }

    @Override
    public Optional<GameList> findRound(long roundId) {
        TypedQuery<GameList> query = getEntityManager()
                .createQuery(QUERY_ROUND_AND_GAMES_BY_ID, GameList.class)
                .setParameter("roundId", roundId);

        return singleResult(query);
    }

    @Override
    public Optional<GameList> findRoundByGame(Game game) {
        TypedQuery<GameList> query = getEntityManager()
                .createQuery(QUERY_GAMELIST_OF_GAME, GameList.class)
                .setParameter("gameId", game.getId());
        return singleResult(query);
    }

    @Override
    public Optional<Long> findNextTippRound(ZonedDateTime date) {
        NativeQuery<Object> query = getEntityManager().unwrap(Session.class)
                .createNativeQuery(QUERY_NEXT_ROUND_BY_DATE, Object.class);
        query.setParameter("date", date, ZonedDateTime.class);
        return findNextTippRound(query);
    }

    @Override
    public Optional<Long> findNextTippRound(long seasonId, ZonedDateTime date) {
        NativeQuery<Object> query = getEntityManager().unwrap(Session.class)
                .createNativeQuery(QUERY_NEXT_GAME_BY_SEASON_AND_DATE, Object.class);
        query.setParameter("season_id", seasonId);
        query.setParameter("date", date, ZonedDateTime.class);
        return findNextTippRound(query);
    }

    private Optional<Long> findNextTippRound(Query query) {
        try {
            List<Object> resultList = query.getResultList();
            if (resultList.size() == 0) {
                return Optional.empty();
            }
            Object[] object = (Object[]) resultList.get(0);
            Long roundId = (Long) object[1];
            if (roundId == null) {
                return Optional.empty();
            }
            return Optional.of(roundId.longValue());
        } catch (NonUniqueResultException ex) {
            System.out.println("Mehr als ein Ergebnis gefunden!");
            return Optional.empty();
        } catch (NoResultException ex) {
            System.out.println("Kein Ergebnis gefunden!");
            return Optional.empty();
        }
    }

    @Override
    public ZonedDateTime findNearestGame(ZonedDateTime date) {
        NativeQuery<ZonedDateTime> query = getEntityManager().unwrap(Session.class)
                .createNativeQuery(QUERY_MIN_GAME_BY_DATE, ZonedDateTime.class);
        query.setParameter("date", date, ZonedDateTime.class);
        return query.getSingleResult();
    }

    @Override
    public List<Game> findGames(ZonedDateTime date) {
        org.hibernate.query.Query<Game> query = getEntityManager().unwrap(Session.class).createQuery(QUERY_GAME_BY_DATE,
                Game.class);
        query.setParameter("date", date, ZonedDateTime.class);
        return query.getResultList();
    }

    @Override
    public Optional<Long> findLastTippRound(long seasonId, ZonedDateTime date) {
        NativeQuery query = getEntityManager().unwrap(Session.class).createNativeQuery(QUERY_LAST_ROUND_BY_DATE);
        query.setParameter("season_id", seasonId);
        query.setParameter("date", date, ZonedDateTime.class);

        Optional<Long> result = Optional.empty();
        try {
            Object[] object = (Object[]) query.getSingleResult();
            Long roundId = (Long) object[1];

            if (roundId != null) {
                result = Optional.of(roundId.longValue());
            }
        } catch (NoResultException ex) {
            // Return value keeps empty.
        }
        return result;
    }

    @Override
    public Optional<Long> findNext(long id) {
        NativeQuery query = getEntityManager().unwrap(Session.class).createNativeQuery(QUERY_NEXT_ROUND);
        query.setParameter("roundId", id);
        query.addScalar("next_round_id");

        Number uniqueResult = (Number) query.uniqueResult();
        Optional<Long> nextRoundId = Optional.empty();
        if (uniqueResult != null) {
            nextRoundId = Optional.of(uniqueResult.longValue());
        }

        return nextRoundId;
    }

    @Override
    public Optional<Long> findPrevious(long id) {
        NativeQuery<?> query = getEntityManager().unwrap(Session.class).createNativeQuery(QUERY_PREV_ROUND);
        query.setParameter("roundId", id);
        query.addScalar("prev_round_id");

        Number uniqueResult = (Number) query.uniqueResult();
        Optional<Long> prevRoundId = Optional.empty();
        if (uniqueResult != null) {
            prevRoundId = Optional.of(uniqueResult.longValue());
        }

        return prevRoundId;
    }

    @Override
    public Optional<GameList> findLastRound(Season season) {
        TypedQuery<GameList> query = getEntityManager().unwrap(Session.class)
                .createQuery(QUERY_LAST_GAMELIST_BY_SEASON, GameList.class)
                .setParameter("seasonId", season.getId());
        return singleResult(query);
    }

    @Override
    public Optional<GameList> findFirstRound(Season season) {
        TypedQuery<GameList> query = getEntityManager().unwrap(Session.class)
                .createQuery(QUERY_FIRST_GAMELIST_BY_SEASON, GameList.class)
                .setParameter("seasonId", season.getId());
        return singleResult(query);
    }

}
