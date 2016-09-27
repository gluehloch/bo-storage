/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2016 by Andre Winkler. All
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.NoResultException;

import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import de.winkler.betoffice.dao.UserDao;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.UserResult;
import de.winkler.betoffice.storage.comparator.UserPointsComparator;

/**
 * Klasse für den Zugriff auf <code>User</code> Objekte mit Hibernate.
 * 
 * TODO: Die SQL Abfragen zur Bestimmung der Tipper-Punkte erfolgen mit zwei SQL
 * Abfragen. Eventuell könnte man diese zu einer zusammen fassen.
 * 
 * @author Andre Winkler
 */
@Repository("userDao")
public class UserDaoHibernate extends AbstractCommonDao<User>
        implements UserDao {

    /** Sucht nach allen Usern mit einem bestimmten Nick-Namen. */
    private static final String QUERY_USER_BY_NICKNAME = "from "
            + User.class.getName() + " as user " + "where "
            + "user.nickName = :nickName";

    private static final String QUERY_SEASON_13_POINTS = AbstractCommonDao
            .loadQuery("query_userresult_season_13.sql");

    private static final String QUERY_SEASON_10_POINTS = AbstractCommonDao
            .loadQuery("query_userresult_season_10.sql");

    private static final String QUERY_SEASON_RANGE_13_POINTS = AbstractCommonDao
            .loadQuery("query_userresult_season_range_13.sql");

    private static final String QUERY_SEASON_RANGE_10_POINTS = AbstractCommonDao
            .loadQuery("query_userresult_season_range_10.sql");

    private static final String QUERY_SEASON_RANGE_COUNT_MATCHES = AbstractCommonDao
            .loadQuery("query_countmatches_season_range.sql");

    private static final String QUERY_SEASON_COUNT_MATCHES = AbstractCommonDao
            .loadQuery("query_countmatches_season.sql");

    // ------------------------------------------------------------------------

    public UserDaoHibernate() {
        super(User.class);
    }

    @Override
    public List<User> findAll() {
        List<User> users = getSessionFactory().getCurrentSession()
                .createQuery("from User order by nickName", User.class)
                .getResultList();
        return users;
    }

    @Override
    public Optional<User> findByNickname(final String nickname) {
        User user = getSessionFactory().getCurrentSession()
                .createQuery(QUERY_USER_BY_NICKNAME, User.class)
                .setParameter("nickName", nickname).getSingleResult();
        return Optional.of(user);
    }

    @Override
    public List<UserResult> calculateUserRanking(final List<User> users,
            final Season season, final int startIndex, final int endIndex) {

        Map<User, UserResult> resultMap = new HashMap<User, UserResult>();
        for (User user : users) {
            resultMap.put(user, new UserResult(user, season));
        }

        /* count(*) as 'full_points', u.* */
        Query query13 = getSessionFactory().getCurrentSession()
                .createSQLQuery(QUERY_SEASON_RANGE_13_POINTS)
                .addEntity("user", User.class).addScalar("full_points",
                        org.hibernate.type.StandardBasicTypes.LONG);
        query13.setParameter("season_id", season.getId());
        query13.setParameter("start_index", startIndex);
        query13.setParameter("end_index", endIndex);

        List<?> resultQuery13 = query13.list();
        for (Object object : resultQuery13) {
            Object[] row = (Object[]) object;
            User user = (User) row[0];
            int fullPoints = ((Long) row[1]).intValue();

            if (resultMap.containsKey(user)) {
                resultMap.get(user).setUserWin(fullPoints);
            } else {
                UserResult ur = new UserResult(user, season);
                ur.setUserWin(fullPoints);
                resultMap.put(user, ur);
            }
        }

        /* count(*) as 'half_points', u.* */
        NativeQuery query10 = getSessionFactory().getCurrentSession()
                .createNativeQuery(QUERY_SEASON_RANGE_10_POINTS)
                .addEntity("user", User.class).addScalar("half_points",
                        org.hibernate.type.StandardBasicTypes.LONG);
        query10.setParameter("season_id", season.getId());
        query10.setParameter("start_index", startIndex);
        query10.setParameter("end_index", endIndex);

        List<?> resultQuery10 = query10.list();
        for (Object object : resultQuery10) {
            Object[] row = (Object[]) object;
            User user = (User) row[0];
            int halfPoints = ((Long) row[1]).intValue();
            if (resultMap.containsKey(user)) {
                resultMap.get(user).setUserTotoWin(halfPoints);
            } else {
                UserResult ur = new UserResult(user, season);
                ur.setUserTotoWin(halfPoints);
                resultMap.put(user, ur);
            }
        }

        /* count(*) as 'count_matches' */
        NativeQuery queryMatches = getSessionFactory().getCurrentSession()
                .createNativeQuery(QUERY_SEASON_RANGE_COUNT_MATCHES)
                .addScalar("count_matches",
                        org.hibernate.type.StandardBasicTypes.LONG);
        queryMatches.setParameter("season_id", season.getId());
        queryMatches.setParameter("start_index", startIndex);
        queryMatches.setParameter("end_index", endIndex);
        Long countMatches = (Long) queryMatches.uniqueResult();

        return userResultMapToList(resultMap, countMatches.intValue());
    }

    @Override
    public List<UserResult> calculateUserRanking(final List<User> users,
            final GameList round) {

        return calculateUserRanking(users, round.getSeason(), round.getIndex(),
                round.getIndex());
    }

    @Override
    public List<UserResult> calculateUserRanking(final List<User> users,
            final Season season) {

        Map<User, UserResult> resultMap = new HashMap<User, UserResult>();
        for (User user : users) {
            resultMap.put(user, new UserResult(user, season));
        }

        /* count(*) as 'full_points', u.* */
        NativeQuery query13 = getSessionFactory().getCurrentSession()
                .createNativeQuery(QUERY_SEASON_13_POINTS)
                .addEntity("user", User.class).addScalar("full_points",
                        org.hibernate.type.StandardBasicTypes.LONG);
        query13.setParameter("season_id", season.getId());
        List<?> resultQuery13 = query13.list();
        for (Object object : resultQuery13) {
            Object[] row = (Object[]) object;
            User user = (User) row[0];
            int fullPoints = ((Long) row[1]).intValue();

            if (resultMap.containsKey(user)) {
                resultMap.get(user).setUserWin(fullPoints);
            } else {
                UserResult ur = new UserResult(user, season);
                ur.setUserWin(fullPoints);
                resultMap.put(user, ur);
            }
        }

        /* count(*) as 'half_points', u.* */
        NativeQuery query10 = getSessionFactory().getCurrentSession()
                .createNativeQuery(QUERY_SEASON_10_POINTS)
                .addEntity("user", User.class).addScalar("half_points",
                        org.hibernate.type.StandardBasicTypes.LONG);
        query10.setParameter("season_id", season.getId());
        List<?> resultQuery10 = query10.list();
        for (Object object : resultQuery10) {
            Object[] row = (Object[]) object;
            User user = (User) row[0];
            int halfPoints = ((Long) row[1]).intValue();

            if (resultMap.containsKey(user)) {
                resultMap.get(user).setUserTotoWin(halfPoints);
            } else {
                UserResult ur = new UserResult(user, season);
                ur.setUserTotoWin(halfPoints);
                resultMap.put(user, ur);
            }
        }

        /* count(*) as 'count_matches' */
        NativeQuery queryMatches = getSessionFactory().getCurrentSession()
                .createNativeQuery(QUERY_SEASON_COUNT_MATCHES)
                .addScalar("count_matches",
                        org.hibernate.type.StandardBasicTypes.LONG);
        queryMatches.setParameter("season_id", season.getId());
        Long countMatches = (Long) queryMatches.uniqueResult();

        return userResultMapToList(resultMap, countMatches.intValue());
    }

    /**
     * @param resultMap
     *            Eine Map mit {@link UserResult}s.
     * @param countMatches
     *            Anzahl der Spielpaarungen.
     * @return Eine sortierte Liste der {@link UserResult}s.
     */
    private List<UserResult> userResultMapToList(
            Map<User, UserResult> resultMap, int countMatches) {

        List<UserResult> userResults = new ArrayList<UserResult>();
        userResults.addAll(resultMap.values());
        Collections.sort(userResults, new UserPointsComparator());
        int tabpos = 1;
        for (UserResult userResult : userResults) {
            userResult.setTicket(countMatches - userResult.getUserTotoWin()
                    - userResult.getUserWin());
            userResult.setTabPos(tabpos);
            tabpos++;
        }
        return userResults;
    }

}
