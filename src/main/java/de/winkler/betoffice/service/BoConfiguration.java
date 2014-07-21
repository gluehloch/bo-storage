/*
 * $Id: BoConfiguration.java 3782 2013-07-27 08:44:32Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2010 by Andre Winkler. All rights reserved.
 * ============================================================================
 *          GNU GENERAL PUBLIC LICENSE
 *  TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package de.winkler.betoffice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.winkler.betoffice.dao.GameTippDao;
import de.winkler.betoffice.dao.GroupDao;
import de.winkler.betoffice.dao.GroupTypeDao;
import de.winkler.betoffice.dao.MaintenanceDao;
import de.winkler.betoffice.dao.MatchDao;
import de.winkler.betoffice.dao.RoundDao;
import de.winkler.betoffice.dao.SeasonDao;
import de.winkler.betoffice.dao.TeamAliasDao;
import de.winkler.betoffice.dao.TeamDao;
import de.winkler.betoffice.dao.UserDao;
import de.winkler.betoffice.dao.UserSeasonDao;

/**
 * Takes all DAOs.
 *
 * @author  $Author: andrewinkler $
 * @version $Revision: 3782 $ $Date: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
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

    /** Das UserSeasonDao. */
    private UserSeasonDao userSeasonDao;

    /**
     * Liefert das DAO.
     *
     * @return Das UserSeasonDao.
     */
    public UserSeasonDao getUserSeasonDao() {
        return userSeasonDao;
    }

    /**
     * Setzt das DAO.
     *
     * @param userSeasonDao Das UserSeasonDao.
     */
    @Autowired
    public void setUserSeasonDao(final UserSeasonDao userSeasonDao) {
        this.userSeasonDao = userSeasonDao;
    }

    // -- seasonDao -----------------------------------------------------------

    /** Das Season-DAO. */
    private SeasonDao seasonDao;

    /**
     * Setzt das DAO.
     *
     * @param _seasonDao Das Season-DAO.
     */
    @Autowired
    public final void setSeasonDao(final SeasonDao _seasonDao) {
        seasonDao = _seasonDao;
    }

    /**
     * Liefert das Season-DAO.
     *
     * @return Ein Season-DAO.
     */
    public final SeasonDao getSeasonDao() {
        return seasonDao;
    }

    // -- teamDao -------------------------------------------------------------

    /** Das Team-DAO. */
    private TeamDao teamDao;

    /**
     * Setzt das Team-DAO.
     *
     * @param _teamDao Das Team-DAO.
     */
    @Autowired
    public final void setTeamDao(final TeamDao _teamDao) {
        teamDao = _teamDao;
    }

    /**
     * Liefert das Team-DAO.
     *
     * @return Ein Team-DAO.
     */
    public final TeamDao getTeamDao() {
        return teamDao;
    }

    // -- teamDao -------------------------------------------------------------

    /** Das TeamAlias-DAO. */
    private TeamAliasDao teamAliasDao;

    /**
     * Setzt das TeamAlias-DAO.
     *
     * @param _teamAliasDao Das TeamAlias-DAO.
     */
    @Autowired
    public final void setTeamAliasDao(final TeamAliasDao _teamAliasDao) {
        teamAliasDao = _teamAliasDao;
    }

    /**
     * Liefert das TeamAlias-DAO.
     *
     * @return Ein TeamAlias-DAO.
     */
    public final TeamAliasDao getTeamAliasDao() {
        return teamAliasDao;
    }

    // -- userDao -------------------------------------------------------------

    /** Das User-DAO. */
    private UserDao userDao;

    /**
     * Setzt das User-DAO.
     *
     * @param _userDao Ein User-DAO.
     */
    @Autowired
    public final void setUserDao(final UserDao _userDao) {
        userDao = _userDao;
    }

    /**
     * Liefert das User-DAO.
     *
     * @return Ein User-DAO.
     */
    public final UserDao getUserDao() {
        return userDao;
    }

    // -- groupTypeDao --------------------------------------------------------

    /** Das GroupType-DAO. */
    private GroupTypeDao groupTypeDao;

    /**
     * Setzt das GroupType-DAO.
     *
     * @param _gtDao Ein GroupType-DAO.
     */
    @Autowired
    public final void setGroupTypeDao(final GroupTypeDao _gtDao) {
        groupTypeDao = _gtDao;
    }

    /**
     * Liefert das GroupType-DAO.
     *
     * @return Ein GroupType-DAO.
     */
    public final GroupTypeDao getGroupTypeDao() {
        return groupTypeDao;
    }

    // -- roundDao ------------------------------------------------------------

    /** Round DAO. */
    private RoundDao roundDao;

    /**
     * Liefert das RoundDAO.
     *
     * @return Das Round-DAO.
     */
    public final RoundDao getRoundDao() {
        return roundDao;
    }

    /**
     * Setzt das Round-DAO.
     *
     * @param _roundDao Das Round-DAO.
     */
    @Autowired
    public final void setRoundDao(final RoundDao _roundDao) {
        roundDao = _roundDao;
    }

    // -- matchDao ------------------------------------------------------------

    /** Match DAO. */
    private MatchDao matchDao;

    /**
     * Liefert das Match-DAO.
     *
     * @return Das Match-DAO.
     */
    public final MatchDao getMatchDao() {
        return matchDao;
    }

    /**
     * Setzt das Match-DAO
     *
     * @param _matchDao Das Match-DAO.
     */
    @Autowired
    public final void setMatchDao(final MatchDao _matchDao) {
        matchDao = _matchDao;
    }

    // -- gameTipp ------------------------------------------------------------

    /** Das DAO zur Verwaltung der Spiel-Tipps. */
    private GameTippDao gameTippDao;

    /**
     * Liefert das DAO zur Verwaltung der Spieltipps.
     *
     * @return Das DAO.
     */
    public final GameTippDao getGameTippDao() {
        return gameTippDao;
    }

    /**
     * Setzt das DAO zur Verwaltung der Spiel-Tipps.
     *
     * @param _gameTippDao Das DAO.
     */
    @Autowired
    public final void setGameTippDao(final GameTippDao _gameTippDao) {
        gameTippDao = _gameTippDao;
    }

    // -- maintenance ---------------------------------------------------------

    /** Ein DAO für Wartungsaufgaben. */
    private MaintenanceDao maintenanceDao;

    /**
     * @return Das DAO.
     */
    public final MaintenanceDao getMaintenanceDao() {
        return maintenanceDao;
    }

    /**
     * Setzt das DAO für Wartungsaufgaben.
     *
     * @param _maintenanceDao Das DAO.
     */
    @Autowired
    public final void setMaintenanceDao(final MaintenanceDao _maintenanceDao) {
        maintenanceDao = _maintenanceDao;
    }

}
