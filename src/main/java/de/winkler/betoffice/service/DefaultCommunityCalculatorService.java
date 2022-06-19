/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2022 by Andre Winkler. All
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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.winkler.betoffice.dao.UserDao;
import de.winkler.betoffice.dao.UserSeasonDao;
import de.winkler.betoffice.storage.Community;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.UserResult;

/**
 * Default Implementierung von {@link CommunityCalculatorService}.
 *
 * @author Andre Winkler
 */
@Service("communitySeasonService")
public class DefaultCommunityCalculatorService implements CommunityCalculatorService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserSeasonDao userSeasonDao;

    @Override
    @Transactional(readOnly = true)
    public List<UserResult> calculateUserRanking(Community community, GameList round) {
        List<User> users = userSeasonDao.findUsers(round.getSeason());
        return userDao.calculateUserRanking(users, round);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResult> calculateUserRanking(Community community, int startIndex, int endIndex) {
        List<User> users = userSeasonDao.findUsers(season);
        return userDao.calculateUserRanking(users, season, startIndex, endIndex);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResult> calculateUserRanking(Community community) {
        List<User> users = userSeasonDao.findUsers(season);
        return userDao.calculateUserRanking(users, season);
    }
    
}
