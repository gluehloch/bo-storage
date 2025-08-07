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

package de.betoffice.storage.season.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import de.betoffice.dao.hibernate.AbstractCommonDao;
import de.betoffice.storage.comparator.TeamPointsComparator;
import de.betoffice.storage.group.entity.GroupType;
import de.betoffice.storage.season.SeasonDao;
import de.betoffice.storage.season.entity.Group;
import de.betoffice.storage.season.entity.Season;
import de.betoffice.storage.season.entity.SeasonReference;
import de.betoffice.storage.team.TeamResult;
import de.betoffice.storage.team.entity.Team;

/**
 * Klasse für den Zugriff auf {@link de.betoffice.storage.season.entity.Season}. Zusätzlich liefern die Methoden
 * {@link #calculateTeamRanking(Season, Group)} und {@link #calculateTeamRanking(Season, Group, int, int)} das
 * Tabellenbild zu der Parameterkombination ({@link Season}, {@link Group}).
 *
 * @author by Andre Winkler
 */
@Repository("seasonDao")
public class SeasonDaoHibernate extends AbstractCommonDao<Season> implements SeasonDao {

    /** Sucht nach einer Meisterschaft mit gesuchtem Namen und Jahrgang. */
    private static final String QUERY_SEASON_BY_NAME_AND_YEAR = "select season from "
            + Season.class.getName() + " as season "
            + "where season.reference.name = :name and season.reference.year = :year";

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
        List<Season> seasons = getEntityManager()
                .createQuery("select s from Season s order by s.reference.year", Season.class)
                .getResultList();
        return seasons;
    }

    @Override
    public Optional<Season> find(final SeasonReference seasonRef) {
        TypedQuery<Season> query = getEntityManager()
                .createQuery(QUERY_SEASON_BY_NAME_AND_YEAR, Season.class)
                .setParameter("name", seasonRef.getName())
                .setParameter("year", seasonRef.getYear());

        return singleResult(query);
    }
    
    @Override
    public List<TeamResult> calculateTeamRanking(Season season, GroupType groupType) {
        Map<Team, TeamResult> resultMap = new HashMap<Team, TeamResult>();

        NativeQuery queryTeamGoals = getEntityManager().unwrap(Session.class)
                .createNativeQuery(QUERY_TEAM_SEASON_GOALS)
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

        NativeQuery queryTeamPoints = getEntityManager().unwrap(Session.class)
                .createNativeQuery(QUERY_TEAM_SEASON_POINTS)
                .addEntity("team", Team.class)
                .addScalar("win", org.hibernate.type.StandardBasicTypes.LONG)
                .addScalar("remis", org.hibernate.type.StandardBasicTypes.LONG)
                .addScalar("lost", org.hibernate.type.StandardBasicTypes.LONG)
                .addScalar("points",
                        org.hibernate.type.StandardBasicTypes.LONG);
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
    public List<TeamResult> calculateTeamRanking(Season season, GroupType groupType, int startIndex, int endIndex) {
        Map<Team, TeamResult> resultMap = new HashMap<Team, TeamResult>();

        NativeQuery queryTeamGoals = getEntityManager().unwrap(Session.class)
                .createNativeQuery(QUERY_TEAM_SEASON_RANGE_GOALS)
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

        List<?> resultQueryTeamGoals = queryTeamGoals.getResultList();
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

        NativeQuery queryTeamPoints = getEntityManager().unwrap(Session.class)
                .createNativeQuery(QUERY_TEAM_SEASON_RANGE_POINTS)
                .addEntity("team", Team.class)
                .addScalar("win", org.hibernate.type.StandardBasicTypes.LONG)
                .addScalar("remis", org.hibernate.type.StandardBasicTypes.LONG)
                .addScalar("lost", org.hibernate.type.StandardBasicTypes.LONG)
                .addScalar("points",
                        org.hibernate.type.StandardBasicTypes.LONG);
        queryTeamPoints.setParameter("season_id", season.getId());
        queryTeamPoints.setParameter("grouptype_id", groupType.getId());
        queryTeamPoints.setParameter("start_index", startIndex);
        queryTeamPoints.setParameter("end_index", endIndex);

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

    private List<TeamResult> teamResultToList(
            Map<Team, TeamResult> teamResults) {
        List<TeamResult> tr = new ArrayList<TeamResult>();
        tr.addAll(teamResults.values());

        Collections.sort(tr, new TeamPointsComparator());
        for (int i = 0; i < tr.size(); i++) {
            tr.get(i).setTabPos(i + 1);
        }

        return tr;
    }

}
