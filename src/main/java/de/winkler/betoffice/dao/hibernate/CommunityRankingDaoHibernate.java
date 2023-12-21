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
 *
 */

package de.winkler.betoffice.dao.hibernate;

import de.winkler.betoffice.dao.CommunityRankingDao;
import de.winkler.betoffice.storage.*;
import de.winkler.betoffice.storage.comparator.UserPointsComparator;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("communityRankingDao")
public class CommunityRankingDaoHibernate implements CommunityRankingDao {

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

    private final SessionFactory sessionFactory;

    private final static String PARAMETER_COMMUNITY_ID = "community_id";
    private final static String PARAMETER_SEASON_ID = "season_id";
    
    @Autowired
    public CommunityRankingDaoHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<UserResult> calculateUserRanking(final Community community, final SeasonRange seasonRange) {
        final Collection<User> users = community.getUsers();
        
        Map<User, UserResult> resultMap = new HashMap<>();
        for (User user : users) {
            resultMap.put(user, new UserResult(user));
        }

        /* count(*) as 'full_points', u.* */
        Query<Object> query13 = sessionFactory.getCurrentSession()
                .createNativeQuery(QUERY_SEASON_RANGE_13_POINTS, Object.class)
                .addEntity("user", User.class).addScalar("full_points",
                        org.hibernate.type.StandardBasicTypes.LONG);
        query13.setParameter(PARAMETER_COMMUNITY_ID, community.getId());
        query13.setParameter("start_index", seasonRange.startIndex());
        query13.setParameter("end_index", seasonRange.endIndex());

        List<?> resultQuery13 = query13.list();
        for (Object object : resultQuery13) {
            Object[] row = (Object[]) object;
            User user = (User) row[0];
            int fullPoints = ((Long) row[1]).intValue();

            if (resultMap.containsKey(user)) {
                resultMap.get(user).setUserWin(fullPoints);
            } else {
                UserResult ur = new UserResult(user);
                ur.setUserWin(fullPoints);
                resultMap.put(user, ur);
            }
        }

        /* count(*) as 'half_points', u.* */
        NativeQuery<Object> query10 = sessionFactory.getCurrentSession()
                .createNativeQuery(QUERY_SEASON_RANGE_10_POINTS, Object.class)
                .addEntity("user", User.class).addScalar("half_points",
                        org.hibernate.type.StandardBasicTypes.LONG);
        query10.setParameter(PARAMETER_COMMUNITY_ID, community.getId());
        query10.setParameter("start_index", seasonRange.startIndex());
        query10.setParameter("end_index", seasonRange.endIndex());

        List<?> resultQuery10 = query10.list();
        for (Object object : resultQuery10) {
            Object[] row = (Object[]) object;
            User user = (User) row[0];
            int halfPoints = ((Long) row[1]).intValue();
            if (resultMap.containsKey(user)) {
                resultMap.get(user).setUserTotoWin(halfPoints);
            } else {
                UserResult ur = new UserResult(user);
                ur.setUserTotoWin(halfPoints);
                resultMap.put(user, ur);
            }
        }

        /* count(*) as 'count_matches' */
        NativeQuery<Object> queryMatches = sessionFactory.getCurrentSession()
                .createNativeQuery(QUERY_SEASON_RANGE_COUNT_MATCHES, Object.class)
                .addScalar("count_matches",
                        org.hibernate.type.StandardBasicTypes.LONG);
        queryMatches.setParameter(PARAMETER_SEASON_ID, community.getSeason().getId());
        queryMatches.setParameter("start_index", seasonRange.startIndex());
        queryMatches.setParameter("end_index", seasonRange.endIndex());
        Long countMatches = (Long) queryMatches.uniqueResult();

        return userResultMapToList(resultMap, countMatches.intValue());
    }

    @Override
    public List<UserResult> calculateUserRanking(final Community community, final GameList round) {
        return calculateUserRanking(community, SeasonRange.of(round.getIndex(), round.getIndex()));
    }

    @Override
    public List<UserResult> calculateUserRanking(final Community community) {
        final Collection<User> users = community.getUsers();

        Map<User, UserResult> resultMap = new HashMap<>();
        for (User user : users) {
            resultMap.put(user, new UserResult(user));
        }

        /* count(*) as 'full_points', u.* */
        NativeQuery<Object> query13 = sessionFactory.getCurrentSession()
                .createNativeQuery(QUERY_SEASON_13_POINTS, Object.class)
                .addEntity("user", User.class).addScalar("full_points",
                        org.hibernate.type.StandardBasicTypes.LONG);
        query13.setParameter(PARAMETER_COMMUNITY_ID, community.getId());
        List<?> resultQuery13 = query13.list();
        for (Object object : resultQuery13) {
            Object[] row = (Object[]) object;
            User user = (User) row[0];
            int fullPoints = ((Long) row[1]).intValue();

            if (resultMap.containsKey(user)) {
                resultMap.get(user).setUserWin(fullPoints);
            } else {
                UserResult ur = new UserResult(user);
                ur.setUserWin(fullPoints);
                resultMap.put(user, ur);
            }
        }

        /* count(*) as 'half_points', u.* */
        NativeQuery<Object> query10 = sessionFactory.getCurrentSession()
                .createNativeQuery(QUERY_SEASON_10_POINTS, Object.class)
                .addEntity("user", User.class).addScalar("half_points",
                        org.hibernate.type.StandardBasicTypes.LONG);
        query10.setParameter(PARAMETER_COMMUNITY_ID, community.getId());
        List<?> resultQuery10 = query10.list();
        for (Object object : resultQuery10) {
            Object[] row = (Object[]) object;
            User user = (User) row[0];
            int halfPoints = ((Long) row[1]).intValue();

            if (resultMap.containsKey(user)) {
                resultMap.get(user).setUserTotoWin(halfPoints);
            } else {
                UserResult ur = new UserResult(user);
                ur.setUserTotoWin(halfPoints);
                resultMap.put(user, ur);
            }
        }

        /* count(*) as 'count_matches' */
        NativeQuery<Object> queryMatches = sessionFactory.getCurrentSession()
                .createNativeQuery(QUERY_SEASON_COUNT_MATCHES, Object.class)
                .addScalar("count_matches",
                        org.hibernate.type.StandardBasicTypes.LONG);
        queryMatches.setParameter(PARAMETER_SEASON_ID, community.getSeason().getId());
        Long countMatches = (Long) queryMatches.uniqueResult();

        return userResultMapToList(resultMap, countMatches.intValue());
    }

    /**
     * @param  resultMap    Eine Map mit {@link UserResult}s.
     * @param  countMatches Anzahl der Spielpaarungen.
     * @return              Eine sortierte Liste der {@link UserResult}s.
     */
    private List<UserResult> userResultMapToList(Map<User, UserResult> resultMap, int countMatches) {
        List<UserResult> userResults = new ArrayList<>();
        userResults.addAll(resultMap.values());
        Collections.sort(userResults, new UserPointsComparator());
        int tabpos = 1;
        for (UserResult userResult : userResults) {
            userResult.setTicket(countMatches - userResult.getUserTotoWin() - userResult.getUserWin());
            userResult.setTabPos(tabpos);
            tabpos++;
        }
        return userResults;
    }

}
