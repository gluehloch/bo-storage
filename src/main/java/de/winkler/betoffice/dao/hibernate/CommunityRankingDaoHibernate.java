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

    @Autowired
    public CommunityRankingDaoHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<UserResult> calculateUserRanking(final Collection<User> users, final Season season, final SeasonRange seasonRange) {
        Map<User, UserResult> resultMap = new HashMap<>();
        for (User user : users) {
            resultMap.put(user, new UserResult(user));
        }

        /* count(*) as 'full_points', u.* */
        Query query13 = sessionFactory.getCurrentSession()
                .createSQLQuery(QUERY_SEASON_RANGE_13_POINTS)
                .addEntity("user", User.class).addScalar("full_points",
                        org.hibernate.type.StandardBasicTypes.LONG);
        query13.setParameter("season_id", season.getId());
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
        NativeQuery query10 = sessionFactory.getCurrentSession()
                .createNativeQuery(QUERY_SEASON_RANGE_10_POINTS)
                .addEntity("user", User.class).addScalar("half_points",
                        org.hibernate.type.StandardBasicTypes.LONG);
        query10.setParameter("season_id", season.getId());
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
        NativeQuery queryMatches = sessionFactory.getCurrentSession()
                .createNativeQuery(QUERY_SEASON_RANGE_COUNT_MATCHES)
                .addScalar("count_matches",
                        org.hibernate.type.StandardBasicTypes.LONG);
        queryMatches.setParameter("season_id", season.getId());
        queryMatches.setParameter("start_index", seasonRange.startIndex());
        queryMatches.setParameter("end_index", seasonRange.endIndex());
        Long countMatches = (Long) queryMatches.uniqueResult();

        return userResultMapToList(resultMap, countMatches.intValue());
    }

    @Override
    public List<UserResult> calculateUserRanking(final Collection<User> users, final GameList round) {
        return calculateUserRanking(users, round.getSeason(), SeasonRange.of(round.getIndex(), round.getIndex()));
    }

    @Override
    public List<UserResult> calculateUserRanking(final Collection<User> users, final Season season) {
        Map<User, UserResult> resultMap = new HashMap<>();
        for (User user : users) {
            resultMap.put(user, new UserResult(user));
        }

        /* count(*) as 'full_points', u.* */
        NativeQuery query13 = sessionFactory.getCurrentSession()
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
                UserResult ur = new UserResult(user);
                ur.setUserWin(fullPoints);
                resultMap.put(user, ur);
            }
        }

        /* count(*) as 'half_points', u.* */
        NativeQuery query10 = sessionFactory.getCurrentSession()
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
                UserResult ur = new UserResult(user);
                ur.setUserTotoWin(halfPoints);
                resultMap.put(user, ur);
            }
        }

        /* count(*) as 'count_matches' */
        NativeQuery queryMatches = sessionFactory.getCurrentSession()
                .createNativeQuery(QUERY_SEASON_COUNT_MATCHES)
                .addScalar("count_matches",
                        org.hibernate.type.StandardBasicTypes.LONG);
        queryMatches.setParameter("season_id", season.getId());
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
