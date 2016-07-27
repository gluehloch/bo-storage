/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2015 by Andre Winkler. All
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

import java.math.BigInteger;
import java.util.List;

import org.hibernate.SQLQuery;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import de.winkler.betoffice.dao.RoundDao;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.Group;
import de.winkler.betoffice.storage.Season;

/**
 * Eine Hibernate-DAO Implementierung zur Verwaltung eines Spieltags.
 * 
 * @author by Andre Winkler
 */
@Repository("roundDao")
public class RoundDaoHibernate extends AbstractCommonDao<GameList> implements
        RoundDao {

    /**
     * Sucht nach allen Spieltagen einer Meisterschaft.
     */
    private static final String QUERY_GAMELIST_BY_SEASON = "from "
            + "GameList as gamelist " + "where gamelist.season.id = :seasonId "
            + "order by gamelist.index";

    /**
     * Sucht nach dem letzten Spieltag einer Meisterschaft.
     */
    private static final String QUERY_LAST_GAMELIST_BY_SEASON = "from "
            + "GameList as gamelist "
            + "where gamelist.season.id = :seasonId "
            + "and gamelist.index = "
            + "( "
            + "select max(index) from gamelist gl2 where  gl2.season.id = :seasonId "
            + ")";

    /**
     * Sucht nach allen Spieltagen einer Meisterschaft fuer eine bestimmte
     * Gruppe.
     */
    private static final String QUERY_GAMELIST_BY_SEASON_GROUP = "from "
            + "GameList as gamelist " + "where gamelist.season.id = :seasonId "
            + "and gamelist.group.id = :groupId" + " order by gamelist.index";

    /**
     * Sucht einen Spieltag einer Meisterschaft.
     */
    private static final String QUERY_GAMELIST_BY_SEASON_AND_INDEX = "from "
            + "GameList as gamelist "
            + "where gamelist.season.id = :seasonId and gamelist.index = :gameListIndex";

    /**
     * Sucht nach allen Gruppen, Spielen, Mannschaften, Tipps und Tippteilnehmer
     * einer Spielrunde/Spieltag.
     */
    private static final String QUERY_ALL_ROUND_OBJECTS = "select round from GameList as round "
            + "left join fetch round.gameList game "
            + "left join fetch game.tippList tipp "
            + "left join fetch tipp.user u "
            + "left join fetch game.homeTeam "
            + "left join fetch game.guestTeam "
            + "left join fetch game.group "
            + "where round.season.id = :seasonId and round.index = :gameListIndex";

    private static final String QUERY_NEXT_ROUND_BY_DATE = "select min(t.bo_datetime) datetime, t.id next_round_id "
            + "from (select r.bo_datetime, r.id from bo_gamelist r, bo_game m "
            + "where r.bo_season_ref = :season_id "
            + "and r.id = m.bo_gamelist_ref "
            + "and m.bo_datetime >= :date) "
            + "as t";
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
        @SuppressWarnings("unchecked")
        List<GameList> objects = getSessionFactory().getCurrentSession()
                .createQuery(QUERY_GAMELIST_BY_SEASON)
                .setParameter("seasonId", season.getId()).list();
        return objects;
    }

    @Override
    public List<GameList> findRounds(Season season, Group group) {
        @SuppressWarnings("unchecked")
        List<GameList> objects = getSessionFactory().getCurrentSession()
                .createQuery(QUERY_GAMELIST_BY_SEASON_GROUP)
                .setParameter("seasonId", season.getId())
                .setParameter("groupId", group.getId()).list();
        return objects;
    }

    @Override
    public GameList findRound(Season season, int index) {
        @SuppressWarnings("unchecked")
        List<GameList> rounds = getSessionFactory().getCurrentSession()
                .createQuery(QUERY_GAMELIST_BY_SEASON_AND_INDEX)
                .setParameter("seasonId", season.getId())
                .setParameter("gameListIndex", Integer.valueOf(index)).list();

        GameList result = null;
        if (!rounds.isEmpty()) {
            result = (GameList) rounds.get(0);
        }
        return result;
    }

    @Override
    public GameList findAllRoundObjects(Season season, int index) {
        @SuppressWarnings("unchecked")
        List<GameList> rounds = getSessionFactory().getCurrentSession()
                .createQuery(QUERY_ALL_ROUND_OBJECTS)
                .setParameter("seasonId", season.getId())
                .setParameter("gameListIndex", Integer.valueOf(index)).list();

        // TODO
        // The source on
        // http://stackoverflow.com/questions/592825/jpa-please-help-understanding-join-fetch
        // says:
        // 'It os ok to get so much objects.'
        //
        // if (objects.size() > 1) {
        // throw new IllegalStateException("Too many result objects.");
        // }

        return ((GameList) rounds.get(0));
    }

    @Override
    public Long findNextTippRound(long seasonId, DateTime date) {
        SQLQuery query = getSessionFactory().getCurrentSession()
                .createSQLQuery(QUERY_NEXT_ROUND_BY_DATE);
        query.setParameter("season_id", seasonId);
        query.setDate("date", date.toDate());
        query.addScalar("datetime");
        query.addScalar("next_round_id");

        Object[] uniqueResult = (Object[]) query.uniqueResult();
        Long roundId = null;
        if (uniqueResult != null && uniqueResult[1] != null) {
            roundId = ((BigInteger) uniqueResult[1]).longValue();
        }

        return roundId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.winkler.betoffice.dao.RoundDao#findNext(long)
     */
    @Override
    public Long findNext(long id) {
        SQLQuery query = getSessionFactory().getCurrentSession()
                .createSQLQuery(QUERY_NEXT_ROUND);
        query.setParameter("roundId", id);
        query.addScalar("next_round_id");

        BigInteger uniqueResult = (BigInteger) query.uniqueResult();
        Long nextRoundId = null;
        if (uniqueResult != null) {
            nextRoundId = uniqueResult.longValue();
        }

        return nextRoundId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.winkler.betoffice.dao.RoundDao#findPrevious(long)
     */
    @Override
    public Long findPrevious(long id) {
        SQLQuery query = getSessionFactory().getCurrentSession()
                .createSQLQuery(QUERY_PREV_ROUND);
        query.setParameter("roundId", id);
        query.addScalar("prev_round_id");

        BigInteger uniqueResult = (BigInteger) query.uniqueResult();
        Long prevRoundId = null;
        if (uniqueResult != null) {
            prevRoundId = uniqueResult.longValue();
        }

        return prevRoundId;
    }

    @Override
    public GameList findLastRound(Season season) {
        @SuppressWarnings("unchecked")
        List<GameList> rounds = getSessionFactory().getCurrentSession()
                .createQuery(QUERY_LAST_GAMELIST_BY_SEASON)
                .setParameter("seasonId", season.getId()).list();

        GameList result = null;
        if (!rounds.isEmpty()) {
            result = (GameList) rounds.get(0);
        }
        return result;
    }

}
