/*
 * =============================================================================
 * Project betoffice-storage Copyright (c) 2000-2022 by Andre Winkler. All
 * rights reserved.
 * =============================================================================
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

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import de.winkler.betoffice.dao.CommunityDao;
import de.winkler.betoffice.dao.SeasonDao;
import de.winkler.betoffice.dao.UserDao;
import de.winkler.betoffice.storage.Community;
import de.winkler.betoffice.storage.CommunityFilter;
import de.winkler.betoffice.storage.CommunityReference;
import de.winkler.betoffice.storage.Nickname;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.SeasonReference;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.util.LoggerFactory;

/**
 * Manages a community.
 * 
 * @author Andre Winkler
 */
@Service("communityService")
public class DefaultCommunityService extends AbstractManagerService implements CommunityService {

    private static final Logger LOG = LoggerFactory.make();

    @Autowired
    private CommunityDao communityDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private SeasonDao seasonDao;

    public List<Community> find(String communityName) {
        return communityDao.find(communityName);
    }

    @Override
    public Page<Community> findCommunities(CommunityFilter communityFilter, Pageable pageable) {
        return communityDao.findAll(communityFilter, pageable);
    }

    @Override
    public Page<User> findUsers(String nicknameFilter, Pageable pageable) {
        return userDao.findAll(nicknameFilter, pageable);
    }

    @Override
    public Community create(CommunityReference communityRef, SeasonReference seasonRef, String communityName,
            Nickname managerNickname) {

        Community existingCommunity = communityDao.find(communityRef).orElseThrow();
        Season persistedSeason = seasonDao.findByName(season.getName(), seasonRef.getYear()).orElseThrow();
        User communityManager = userDao.findByNickname(managerNickname).orElseThrow();

        Community community = new Community();
        community.setName(communityName);
        community.setReference(reference);
        community.setCommunityManager(communityManager);
        community.setSeason(persistedSeason);
        communityDao.save(community);

        return community;
    }

    @Override
    public void delete(CommunityReference reference) {
        Community community = communityDao.find(reference).orElseThrow();

        if (communityDao.hasMembers(community)) {
            LOG.warn("Unable to delete community '{}'. The Community has members.", community);
            throw new IllegalArgumentException("Unable to delete community. The Community has members.");
        }

        communityDao.delete(community);
    }

    @Override
    public Community addMember(CommunityReference communityReference, Nickname nickname) {
        User user = userDao.findByNickname(nickname).orElseThrow();
        Community community = communityDao.find(communityReference).orElseThrow();
        community.addMember(user);
        communityDao.save(community);
        return community;
    }

    @Override
    public Community removeMember(CommunityReference reference, Nickname nickname) {
        User user = userDao.findByNickname(nickname).orElseThrow();
        Community community = communityDao.find(reference).orElseThrow();
        community.removeMember(user);
        communityDao.save(community);
        return community;
    }

}
