/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2014 by Andre Winkler. All
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

package de.winkler.betoffice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.winkler.betoffice.dao.GameTippDao;
import de.winkler.betoffice.dao.GoalDao;
import de.winkler.betoffice.dao.GroupDao;
import de.winkler.betoffice.dao.GroupTypeDao;
import de.winkler.betoffice.dao.LocationDao;
import de.winkler.betoffice.dao.MaintenanceDao;
import de.winkler.betoffice.dao.MatchDao;
import de.winkler.betoffice.dao.PlayerDao;
import de.winkler.betoffice.dao.RoundDao;
import de.winkler.betoffice.dao.SeasonDao;
import de.winkler.betoffice.dao.TeamAliasDao;
import de.winkler.betoffice.dao.TeamDao;
import de.winkler.betoffice.dao.UserDao;
import de.winkler.betoffice.dao.UserSeasonDao;

/**
 * Takes all DAOs.
 *
 * @author Andre Winkler
 */
@Component
public class BoConfiguration {

    // -- groupDao ------------------------------------------------------------

    private GroupDao groupDao;

    public GroupDao getGroupDao() {
        return groupDao;
    }

    @Autowired
    public void setGroupDao(final GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    // -- userSeasonDao -------------------------------------------------------

    private UserSeasonDao userSeasonDao;

    public UserSeasonDao getUserSeasonDao() {
        return userSeasonDao;
    }

    @Autowired
    public void setUserSeasonDao(final UserSeasonDao userSeasonDao) {
        this.userSeasonDao = userSeasonDao;
    }

    // -- seasonDao -----------------------------------------------------------

    private SeasonDao seasonDao;

    @Autowired
    public final void setSeasonDao(final SeasonDao _seasonDao) {
        seasonDao = _seasonDao;
    }

    public final SeasonDao getSeasonDao() {
        return seasonDao;
    }

    // -- teamDao -------------------------------------------------------------

    private TeamDao teamDao;

    @Autowired
    public final void setTeamDao(final TeamDao _teamDao) {
        teamDao = _teamDao;
    }

    public final TeamDao getTeamDao() {
        return teamDao;
    }

    // -- teamDao -------------------------------------------------------------

    private TeamAliasDao teamAliasDao;

    @Autowired
    public final void setTeamAliasDao(final TeamAliasDao _teamAliasDao) {
        teamAliasDao = _teamAliasDao;
    }

    public final TeamAliasDao getTeamAliasDao() {
        return teamAliasDao;
    }

    // -- userDao -------------------------------------------------------------

    private UserDao userDao;

    @Autowired
    public final void setUserDao(final UserDao _userDao) {
        userDao = _userDao;
    }

    public final UserDao getUserDao() {
        return userDao;
    }

    // -- groupTypeDao --------------------------------------------------------

    private GroupTypeDao groupTypeDao;

    @Autowired
    public final void setGroupTypeDao(final GroupTypeDao _gtDao) {
        groupTypeDao = _gtDao;
    }

    public final GroupTypeDao getGroupTypeDao() {
        return groupTypeDao;
    }

    // -- roundDao ------------------------------------------------------------

    private RoundDao roundDao;

    public final RoundDao getRoundDao() {
        return roundDao;
    }

    @Autowired
    public final void setRoundDao(final RoundDao _roundDao) {
        roundDao = _roundDao;
    }

    // -- matchDao ------------------------------------------------------------

    private MatchDao matchDao;

    public final MatchDao getMatchDao() {
        return matchDao;
    }

    @Autowired
    public final void setMatchDao(final MatchDao _matchDao) {
        matchDao = _matchDao;
    }

    // -- gameTipp ------------------------------------------------------------

    private GameTippDao gameTippDao;

    public final GameTippDao getGameTippDao() {
        return gameTippDao;
    }

    @Autowired
    public final void setGameTippDao(final GameTippDao _gameTippDao) {
        gameTippDao = _gameTippDao;
    }

    // -- location ------------------------------------------------------------

    private LocationDao locationDao;

    public LocationDao getLocationDao() {
        return locationDao;
    }

    @Autowired
    public void setLocationDao(LocationDao _locationDao) {
        locationDao = _locationDao;
    }

    // -- player --------------------------------------------------------------

    private PlayerDao playerDao;

    public PlayerDao getPlayerDao() {
        return playerDao;
    }

    @Autowired
    public void setPlayerDao(PlayerDao _playerDao) {
        playerDao = _playerDao;
    }

    // -- goal ----------------------------------------------------------------

    private GoalDao goalDao;

    public GoalDao getGoalDao() {
        return goalDao;
    }

    @Autowired
    public void setGoalDao(GoalDao _goalDao) {
        goalDao = _goalDao;
    }

    // -- maintenance ---------------------------------------------------------

    private MaintenanceDao maintenanceDao;

    public final MaintenanceDao getMaintenanceDao() {
        return maintenanceDao;
    }

    @Autowired
    public final void setMaintenanceDao(final MaintenanceDao _maintenanceDao) {
        maintenanceDao = _maintenanceDao;
    }

}
