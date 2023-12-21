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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import de.winkler.betoffice.validation.BetofficeServiceResult;
import de.winkler.betoffice.validation.BetofficeValidationException;
import de.winkler.betoffice.validation.BetofficeValidationMessage;
import de.winkler.betoffice.validation.BetofficeValidationMessage.ErrorType;
import de.winkler.betoffice.validation.BetofficeValidationMessage.Severity;

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

    public Optional<Community> find(CommunityReference communityReference) {
        return communityDao.find(communityReference);
    }
    
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
    public Set<User> findMembers(CommunityReference communityReference) {
        return communityDao.findMembers(communityReference).getUsers();
    }

    @Override
    public BetofficeServiceResult<Community> create(CommunityReference communityRef, SeasonReference seasonRef, String communityName,
            Nickname managerNickname) {

        Optional<Community> definedCommunity = communityDao.find(communityRef);
        if (definedCommunity.isPresent()) {
        	return BetofficeServiceResult.failure(ErrorType.COMMUNITY_EXISTS);
        }
        
        Season persistedSeason = seasonDao.find(seasonRef).orElseThrow();
        User communityManager = userDao.findByNickname(managerNickname).orElseThrow();

        Community community = new Community();
        community.setName(communityName);
        community.setReference(communityRef);
        community.setCommunityManager(communityManager);

        community.setSeason(persistedSeason);
        communityDao.persist(community);

        return BetofficeServiceResult.sucess(community);
    }

    @Override
    public void delete(CommunityReference reference) {
        Community community = communityDao.find(reference).orElseThrow();

        if (communityDao.hasMembers(reference)) {
            LOG.warn("Unable to delete community '{}'. The Community has members.", community);
            throw new IllegalArgumentException("Unable to delete community. The Community has members.");
        }

        communityDao.delete(community);
    }

    @Override
    public Community addMember(CommunityReference communityReference, Nickname nickname) {
        Community community = communityDao.find(communityReference).orElseThrow();
        User user = userDao.findByNickname(nickname).orElseThrow();
        community.addMember(user);
        communityDao.update(community);
        return community;
    }

    @Override
    public Community addMembers(CommunityReference communityReference, Set<Nickname> nicknames) {
        Community community = communityDao.find(communityReference).orElseThrow();
        nicknames.stream()
            .map(n -> userDao.findByNickname(n))
            .forEach(u -> u.ifPresent(us -> community.addMember(us)));
        communityDao.update(community);

        return community;
    }

    @Override
    public Community removeMember(CommunityReference reference, Nickname nickname) {
        User user = userDao.findByNickname(nickname).orElseThrow();
        Community community = communityDao.find(reference).orElseThrow();
        community.removeMember(user);
        communityDao.update(community);
        return community;
    }

    @Override
    public Community removeMembers(CommunityReference reference, Set<Nickname> nicknames) {
        nicknames.stream().forEach(nickname -> {
            removeMember(reference, nickname);
        });
        return communityDao.find(reference).orElseThrow();
    }
    
    @Override
    public Optional<User> findUser(Nickname nickname) {
        return userDao.findByNickname(nickname);
    }

    @Override
    @Transactional
    public User createUser(final User user) {
        List<BetofficeValidationMessage> messages = new ArrayList<BetofficeValidationMessage>();

        if (user.getNickname() == null || StringUtils.isBlank(user.getNickname().value())) {
            messages.add(new BetofficeValidationMessage(
                    "Nickname ist nicht gesetzt.", "nickName", Severity.ERROR));
        }

        if (messages.isEmpty()) {
            userDao.persist(user);
        } else {
            throw new BetofficeValidationException(messages);
        }

        return user;
    }

    @Override
    @Transactional
    public void deleteUser(Nickname nickname) {
    	userDao.findByNickname(nickname).ifPresent(u -> userDao.delete(u));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return userDao.findAll();
    }    
    
    @Override
    @Transactional
    public void updateUser(final User user) {
        userDao.update(user);
    }

    @Override
    @Transactional
    public User findUser(long userId) {
        return userDao.findById(userId);
    }

}
