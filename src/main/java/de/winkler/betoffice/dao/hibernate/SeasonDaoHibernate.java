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
 */

package de.winkler.betoffice.dao.hibernate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import de.winkler.betoffice.dao.SeasonDao;
import de.winkler.betoffice.storage.Group;
import de.winkler.betoffice.storage.GroupType;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.Team;
import de.winkler.betoffice.storage.TeamResult;
import de.winkler.betoffice.storage.comparator.TeamPointsComparator;

/**
 * Klasse für den Zugriff auf {@link de.winkler.betoffice.storage.Season}.
 * Zusätzlich liefern die Methoden {@link #calculateTeamRanking(Season, Group)}
 * und {@link #calculateTeamRanking(Season, Group, int, int)} das Tabellenbild
 * zu der Parameterkombination ({@link Season}, {@link Group}).
 *
 * @author by Andre Winkler
 */
@Repository("seasonDao")
public class SeasonDaoHibernate extends AbstractCommonDao<Season> implements
        SeasonDao {

    /** Sucht nach einer Meisterschaft mit gesuchtem Namen und Jahrgang. */
    private static final String QUERY_SEASON_BY_NAME_AND_YEAR = "from "
            + Season.class.getName() + " as season "
            + "where season.name = :name and season.year = :year";

    /**
     * Sucht nach allen Spieltagen, Gruppen, Mannschaften und Tippteilnehmer
     * einer Meisterschaft.
     */
    private static final String QUERY_ALL_SEASON_OBJECTS = "from Season season "
            + "left join fetch season.gameList round "
            + "left join fetch season.groups g "
            + "left join fetch g.groupType gt "
            + "left join fetch season.userSeason us "
            + "left join fetch us.user u "
            + "where season.id = :seasonId "
            + "order by gt.name, round.index";

    /**
     * Sucht nach allen Spieltagen, Gruppen, Mannschaften und Tippteilnehmer
     * einer Meisterschaft inklusive aller Tipps.
     */
    private static final String QUERY_ALL_SEASON_WITH_TIPP_OBJECTS = "from Season season "
            + "left join fetch season.gameList round "
            + "left join fetch round.gameList games "
            + "left join fetch games.tippList "
            + "left join fetch season.groups g "
            + "left join fetch g.groupType gt "
            + "left join fetch season.userSeason us "
            + "left join fetch us.user u "
            + "where season.id = :seasonId "
            + "order by gt.name, round.index";

    /** Sucht nach allen Gruppen einer Meisterschaft. */
    // private static final String QUERY_GROUPS_SEASON = "from Season season "
    // + "left join fetch season.groups g "
    // + "left join fetch g.groupType gt "
    // + "left join fetch season.userSeason us "
    // + "left join fetch us.user u "
    // + "where season.id = ? ";
    private static final String QUERY_TEAM_SEASON_GOALS = AbstractCommonDao
            .loadQuery("query_teamresult_season_goals.sql");

    private static final String QUERY_TEAM_SEASON_POINTS = AbstractCommonDao
            .loadQuery("query_teamresult_season_points.sql");

    private static final String QUERY_TEAM_SEASON_RANGE_GOALS = AbstractCommonDao
            .loadQuery("query_teamresult_season_range_goals.sql");

    private static final String QUERY_TEAM_SEASON_RANGE_POINTS = AbstractCommonDao
            .loadQuery("query_teamresult_season_range_points.sql");

    // ------------------------------------------------------------------------

    public SeasonDaoHibernate() {
        super(Season.class);
    }

    @Override
    public List<Season> findAll() {
        @SuppressWarnings("unchecked")
        List<Season> objects = getSessionFactory().getCurrentSession()
                .createQuery("from Season order by bo_year").list();
        return objects;
    }

    @Override
    public Season findByName(final String name, final String year) {
        @SuppressWarnings("unchecked")
        List<Season> seasons = getSessionFactory().getCurrentSession()
                .createQuery(QUERY_SEASON_BY_NAME_AND_YEAR)
                .setParameter("name", name).setParameter("year", year).getResultList();

        return first(seasons);
    }

    @Override
    public Season findRoundGroupTeamUser(Season season) {
        @SuppressWarnings("unchecked")
        List<Season> seasons = getSessionFactory().getCurrentSession()
                .createQuery(QUERY_ALL_SEASON_OBJECTS)
                .setParameter("seasonId", season.getId()).getResultList();
        return first(seasons);
    }

    @Override
    public Season findRoundGroupTeamUserTipp(Season season) {
        @SuppressWarnings("unchecked")
        List<Season> seasons = getSessionFactory().getCurrentSession()
                .createQuery(QUERY_ALL_SEASON_WITH_TIPP_OBJECTS)
                .setParameter("seasonId", season.getId()).getResultList();
        return first(seasons);
    }

    @Override
    public List<TeamResult> calculateTeamRanking(Season season,
            GroupType groupType) {

        Map<Team, TeamResult> resultMap = new HashMap<Team, TeamResult>();

        SQLQuery queryTeamGoals = getSessionFactory()
                .getCurrentSession()
                .createSQLQuery(QUERY_TEAM_SEASON_GOALS)
                .addEntity("team", Team.class)
                .addScalar("diff", org.hibernate.type.StandardBasicTypes.LONG)
                .addScalar("pos_goals",
                        org.hibernate.type.StandardBasicTypes.LONG)
                .addScalar("neg_goals",
                        org.hibernate.type.StandardBasicTypes.LONG);
        queryTeamGoals.setParameter("season_id", season.getId());
        queryTeamGoals.setParameter("grouptype_id", groupType.getId());

        List<?> resultQueryTeamGoals = queryTeamGoals.list();
        for (Object object : resultQueryTeamGoals) {
            Object[] row = (Object[]) object;
            Team team = (Team) row[0];
            // int diff = ((Long) row[1]).intValue();
            int posGoals = ((Long) row[2]).intValue();
            int negGoals = ((Long) row[3]).intValue();
            TeamResult tr = new TeamResult(season, groupType, team);
            tr.setNegGoals(negGoals);
            tr.setPosGoals(posGoals);
            resultMap.put(team, tr);
        }

        SQLQuery queryTeamPoints = getSessionFactory()
                .getCurrentSession()
                .createSQLQuery(QUERY_TEAM_SEASON_POINTS)
                .addEntity("team", Team.class)
                .addScalar("win", org.hibernate.type.StandardBasicTypes.LONG)
                .addScalar("remis", org.hibernate.type.StandardBasicTypes.LONG)
                .addScalar("lost", org.hibernate.type.StandardBasicTypes.LONG)
                .addScalar("points", org.hibernate.type.StandardBasicTypes.LONG);
        queryTeamPoints.setParameter("season_id", season.getId());
        queryTeamPoints.setParameter("grouptype_id", groupType.getId());

        List<?> resultQueryTeamPoints = queryTeamPoints.getResultList();
        for (Object object : resultQueryTeamPoints) {
            Object[] row = (Object[]) object;
            Team team = (Team) row[0];
            int win = ((Long) row[1]).intValue();
            int remis = ((Long) row[2]).intValue();
            int lost = ((Long) row[3]).intValue();
            // int points = ((Long) row[4]).intValue();

            if (resultMap.containsKey(team)) {
                TeamResult tr = resultMap.get(team);
                tr.setWin(win);
                tr.setLost(lost);
                tr.setRemis(remis);
            } else {
                TeamResult tr = new TeamResult(season, groupType, team);
                tr.setWin(win);
                tr.setLost(lost);
                tr.setRemis(remis);
                resultMap.put(team, tr);
            }
        }

        return teamResultToList(resultMap);
    }

    @Override
    public List<TeamResult> calculateTeamRanking(Season season,
            GroupType groupType, int startIndex, int endIndex) {

        Map<Team, TeamResult> resultMap = new HashMap<Team, TeamResult>();

        SQLQuery queryTeamGoals = getSessionFactory()
                .getCurrentSession()
                .createSQLQuery(QUERY_TEAM_SEASON_RANGE_GOALS)
                .addEntity("team", Team.class)
                .addScalar("diff", org.hibernate.type.StandardBasicTypes.LONG)
                .addScalar("pos_goals",
                        org.hibernate.type.StandardBasicTypes.LONG)
                .addScalar("neg_goals",
                        org.hibernate.type.StandardBasicTypes.LONG);
        queryTeamGoals.setParameter("season_id", season.getId());
        queryTeamGoals.setParameter("grouptype_id", groupType.getId());
        queryTeamGoals.setParameter("start_index", startIndex);
        queryTeamGoals.setParameter("end_index", endIndex);

        List<?> resultQueryTeamGoals = queryTeamGoals.list();
        for (Object object : resultQueryTeamGoals) {
            Object[] row = (Object[]) object;
            Team team = (Team) row[0];
            // int diff = ((Long) row[1]).intValue();
            int posGoals = ((Long) row[2]).intValue();
            int negGoals = ((Long) row[3]).intValue();
            TeamResult tr = new TeamResult(season, groupType, team);
            tr.setNegGoals(negGoals);
            tr.setPosGoals(posGoals);
            resultMap.put(team, tr);
        }

        SQLQuery queryTeamPoints = getSessionFactory()
                .getCurrentSession()
                .createSQLQuery(QUERY_TEAM_SEASON_RANGE_POINTS)
                .addEntity("team", Team.class)
                .addScalar("win", org.hibernate.type.StandardBasicTypes.LONG)
                .addScalar("remis", org.hibernate.type.StandardBasicTypes.LONG)
                .addScalar("lost", org.hibernate.type.StandardBasicTypes.LONG)
                .addScalar("points", org.hibernate.type.StandardBasicTypes.LONG);
        queryTeamPoints.setParameter("season_id", season.getId());
        queryTeamPoints.setParameter("grouptype_id", groupType.getId());
        queryTeamPoints.setParameter("start_index", startIndex);
        queryTeamPoints.setParameter("end_index", endIndex);

        List<?> resultQueryTeamPoints = queryTeamPoints.list();
        for (Object object : resultQueryTeamPoints) {
            Object[] row = (Object[]) object;
            Team team = (Team) row[0];
            int win = ((Long) row[1]).intValue();
            int remis = ((Long) row[2]).intValue();
            int lost = ((Long) row[3]).intValue();
            // int points = ((Long) row[4]).intValue();

            if (resultMap.containsKey(team)) {
                TeamResult tr = resultMap.get(team);
                tr.setWin(win);
                tr.setLost(lost);
                tr.setRemis(remis);
            } else {
                TeamResult tr = new TeamResult(season, groupType, team);
                tr.setWin(win);
                tr.setLost(lost);
                tr.setRemis(remis);
                resultMap.put(team, tr);
            }
        }

        return teamResultToList(resultMap);
    }

    private List<TeamResult> teamResultToList(Map<Team, TeamResult> teamResults) {
        List<TeamResult> tr = new ArrayList<TeamResult>();
        tr.addAll(teamResults.values());

        Collections.sort(tr, new TeamPointsComparator());
        for (int i = 0; i < tr.size(); i++) {
            tr.get(i).setTabPos(i + 1);
        }

        return tr;
    }

}
