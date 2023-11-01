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
 */

package de.winkler.betoffice.dao.hibernate;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import de.winkler.betoffice.dao.RoundDao;
import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.Group;
import de.winkler.betoffice.storage.Season;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

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
    private static final String QUERY_GAMELIST_BY_SEASON =
            """
            select
                gamelist
            from
                GameList as gamelist
            where
                gamelist.season.id = :seasonId
            order by
                gamelist.index
            """;

    /**
     * Sucht nach dem letzten Spieltag einer Meisterschaft.
     */
    private static final String QUERY_LAST_GAMELIST_BY_SEASON =
            """
            select
                gamelist
            from
                GameList gamelist
            where
                gamelist.season.id = :seasonId
                and gamelist.index =
                (
                    select
                        max(index)
                    from
                        gamelist gl2
                    where
                        gl2.season.id = :seasonId
                )
            """;

    /**
     * Sucht nach dem ersten Spieltag einer Meisterschaft.
     */
    private static final String QUERY_FIRST_GAMELIST_BY_SEASON = "select gamelist from "
            + "GameList as gamelist "
            + "where gamelist.season.id = :seasonId "
            + "and gamelist.index = "
            + "("
            + "    select "
            + "        min(index) "
            + "    from "
            + "        gamelist gl2 "
            + "    where "
            + "        gl2.season.id = :seasonId "
            + ")";

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
    private static final String QUERY_NEXT_ROUND_BY_DATE = "select gl.bo_datetime datetime, gl.id last_round_id "
            + "from bo_gamelist gl "
            + "where gl.bo_datetime = "
            + "( "
            + "    select "
            + "        min(t.bo_datetime) datetime"
            + "    from"
            + "    ("
            + "        select "
            + "            r.bo_datetime, r.id "
            + "        from "
            + "            bo_gamelist r, bo_game m "
            + "        where "
            + "            r.bo_season_ref = :season_id "
            + "            and r.id = m.bo_gamelist_ref "
            + "            and m.bo_datetime >= :date "
            + "    ) as t"
            + ")";

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
    private static final String QUERY_NEXT_ROUND = AbstractCommonDao
            .loadQuery("query_next_round.sql");

    /**
     * Search for the last game day id.
     */
    private static final String QUERY_PREV_ROUND = AbstractCommonDao
            .loadQuery("query_prev_round.sql");

    public RoundDaoHibernate() {
        super(GameList.class);
    }

    @Override
    public List<GameList> findRounds(Season season) {
        List<GameList> objects = getSessionFactory().getCurrentSession()
                .createQuery(QUERY_GAMELIST_BY_SEASON, GameList.class)
                .setParameter("seasonId", season.getId())
                .getResultList();
        return objects;
    }

    @Override
    public List<GameList> findRounds(Group group) {
        List<GameList> objects = getSessionFactory().getCurrentSession()
                .createQuery(QUERY_GAMELIST_AND_GAMES_BY_SEASON_GROUP, GameList.class)
                .setParameter("groupId", group.getId())
                .getResultList();
        return objects;
    }

    @Override
    public Optional<GameList> findRound(Season season, int index) {
        Query<GameList> query = getSessionFactory().getCurrentSession()
                .createQuery(QUERY_ROUND_AND_GAMES_BY_INDEX, GameList.class)
                .setParameter("seasonId", season.getId())
                .setParameter("index", Integer.valueOf(index));

        return singleResult(query);
    }

    @Override
    public Optional<GameList> findRound(long roundId) {
        Query<GameList> query = getSessionFactory().getCurrentSession()
                .createQuery(QUERY_ROUND_AND_GAMES_BY_ID, GameList.class)
                .setParameter("roundId", roundId);

        return singleResult(query);
    }
    
    @Override
    public Optional<GameList> findRoundByGame(Game game) {
        Query<GameList> query = getSessionFactory().getCurrentSession()
                .createQuery(QUERY_GAMELIST_OF_GAME, GameList.class)
                .setParameter("gameId", game.getId());
        return singleResult(query);
    }

    @Override
    public Optional<Long> findNextTippRound(long seasonId, ZonedDateTime date) {
        NativeQuery query = getSessionFactory().getCurrentSession().createNativeQuery(QUERY_NEXT_ROUND_BY_DATE);
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
            // TODO Return value keeps empty
        }

        return result;
    }

    @Override
    public Optional<Long> findLastTippRound(long seasonId, ZonedDateTime date) {
        NativeQuery query = getSessionFactory().getCurrentSession().createNativeQuery(QUERY_LAST_ROUND_BY_DATE);
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

    /*
     * (non-Javadoc)
     * 
     * @see de.winkler.betoffice.dao.RoundDao#findNext(long)
     */
    @Override
    public Optional<Long> findNext(long id) {
        NativeQuery query = getSessionFactory().getCurrentSession().createNativeQuery(QUERY_NEXT_ROUND);
        query.setParameter("roundId", id);
        query.addScalar("next_round_id");

        Number uniqueResult = (Number) query.uniqueResult();
        Optional<Long> nextRoundId = Optional.empty();
        if (uniqueResult != null) {
            nextRoundId = Optional.of(uniqueResult.longValue());
        }

        return nextRoundId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.winkler.betoffice.dao.RoundDao#findPrevious(long)
     */
    @Override
    public Optional<Long> findPrevious(long id) {
        NativeQuery<?> query = getSessionFactory().getCurrentSession().createNativeQuery(QUERY_PREV_ROUND);
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
        TypedQuery<GameList> query = getSessionFactory().getCurrentSession()
                .createQuery(QUERY_LAST_GAMELIST_BY_SEASON, GameList.class)
                .setParameter("seasonId", season.getId());
        return singleResult(query);
    }

    @Override
    public Optional<GameList> findFirstRound(Season season) {
        TypedQuery<GameList> query = getSessionFactory().getCurrentSession()
                .createQuery(QUERY_FIRST_GAMELIST_BY_SEASON, GameList.class)
                .setParameter("seasonId", season.getId());
        return singleResult(query);
    }

}
