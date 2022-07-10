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
import java.util.Optional;

import javax.persistence.NoResultException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.winkler.betoffice.dao.CommunityDao;
import de.winkler.betoffice.dao.SeasonDao;
import de.winkler.betoffice.dao.UserDao;
import de.winkler.betoffice.storage.Community;
import de.winkler.betoffice.storage.CommunityReference;
import de.winkler.betoffice.storage.Nickname;
import de.winkler.betoffice.storage.Season;
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
	public List<Community> findAll(String communityNameFilter) {
		return communityDao.findAll(communityNameFilter);
	}

	@Override
	public Community create(CommunityReference reference, Season season, String communityName, Nickname managerNickname) {
		if (StringUtils.isBlank(communityName)) {
			throw new IllegalArgumentException("community name is blank");
		}
		Season persistedSeason = seasonDao.findById(season.getId());

		// Optional chaining?

		Optional<Community> optional = communityDao.find(reference);

		if (optional.isPresent()) {
			throw new IllegalArgumentException("Community '" + reference + "' is already defined.");
		}

		Optional<User> communityManager = userDao.findByNickname(managerNickname);
		communityManager.ifPresent(manager -> {
			Community community = new Community();
			community.setName(communityName);
			community.setReference(CommunityReference.of(communityShortName));
			community.setCommunityManager(manager);
			community.setSeason(persistedSeason);
			communityDao.save(community);
		});

		return community;
	}

	@Override
	public void delete(String communityName) {
		Community community = null;
		try {
			community = communityDao.find(communityName);
		} catch (NoResultException ex) {
			throw new IllegalArgumentException("Unknwon community name '" + communityName + "'.");
		}

		if (communityDao.hasMembers(community)) {
			LOG.warn("Unable to delete community '{}'. The Community has members.", community);
			throw new IllegalArgumentException("Unable to delete community. The Community has members.");
		}

		communityDao.delete(community);
	}

	@Override
	public Community addMember(String communityName, String nickname) {
		validateCommunityName(communityName);
		validateNickname(nickname);

		userDao.findByNickname(nickname).map(user -> {
			Community community = communityDao.find(communityName);
		});

		User user = findUser(nickname);

		try {
			Community community = communityDao.findCommunityMembers(communityName);
			community.addMember(user);
			communityDao.save(community);
			return community;
		} catch (NoResultException ex) {
			throw new IllegalArgumentException("Unknwon community name '" + communityName + "'.");
		}
	}

	@Override
	public Community removeMember(String communityName, String nickname) {
		validateCommunityName(communityName);
		validateNickname(nickname);
		User user = findUser(nickname);

		try {
			Community community = communityDao.findCommunityMembers(communityName);
			community.removeMember(user);
			communityDao.save(community);
			return community;
		} catch (NoResultException ex) {
			throw new IllegalArgumentException("Unknwon community name '" + communityName + "'.");
		}
	}

}
