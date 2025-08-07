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

package de.betoffice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.betoffice.dao.CommunityDao;
import de.betoffice.dao.CommunityRankingDao;
import de.betoffice.storage.Community;
import de.betoffice.storage.CommunityReference;
import de.betoffice.storage.GameList;
import de.betoffice.storage.SeasonRange;
import de.betoffice.storage.UserResult;

/**
 * Default Implementierung von {@link CommunityCalculatorService}.
 *
 * @author Andre Winkler
 */
@Service("communitySeasonService")
public class DefaultCommunityCalculatorService implements CommunityCalculatorService {

    @Autowired
    private CommunityDao communityDao;
    
    @Autowired
    private CommunityRankingDao communityRankingDao;

    @Override
    @Transactional(readOnly = true)
    public List<UserResult> calculateRanking(CommunityReference communityReference, GameList round) {
        Community community = communityDao.find(communityReference).orElseThrow();
        return communityRankingDao.calculateUserRanking(community, round);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResult> calculateRanking(CommunityReference communityReference) {
        Community community = communityDao.find(communityReference).orElseThrow();
        return communityRankingDao.calculateUserRanking(community);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserResult> calculateRanking(CommunityReference communityReference, SeasonRange seasonRange) {
        Community community = communityDao.find(communityReference).orElseThrow();
        return communityRankingDao.calculateUserRanking(community, seasonRange);
    }

}
