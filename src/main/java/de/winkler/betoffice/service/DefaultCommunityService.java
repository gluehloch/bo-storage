/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2019 by Andre Winkler. All
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

import javax.persistence.NoResultException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.winkler.betoffice.dao.CommunityDao;
import de.winkler.betoffice.dao.UserDao;
import de.winkler.betoffice.storage.Community;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.util.LoggerFactory;

/**
 * Manages a community.
 * 
 * @author Andre Winkler
 */
@Service("communityService")
public class DefaultCommunityService extends AbstractManagerService
        implements CommunityService {

    private final Logger LOG = LoggerFactory.make();

    @Autowired
    private CommunityDao communityDao;

    @Autowired
    private UserDao userDao;

    @Override
    public Community create(String name, String managerNickname) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException(
                    "Community name should be defined.");
        }

        try {
            Community communityDefined = communityDao.findByName(name);
            throw new IllegalArgumentException(
                    "Community '" + name + "' is already defined.");
        } catch (NoResultException ex) {
            // Ok. No community with name is defined.
        }

        if (StringUtils.isBlank(managerNickname)) {
            throw new IllegalArgumentException(
                    "Community manager should be defined.");
        }

        User communityManager = userDao.findByNickname(managerNickname)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No user '" + managerNickname + "' defined."));

        Community community = new Community();
        community.setName(name);
        community.setCommunityManager(communityManager);

        return communityDao.save(community);
    }

    @Override
    public void delete(Community community) {
        if (communityDao.hasMembers(community)) {
            LOG.warn(
                    "Unable to delete community '{}'. The Community has members.",
                    community);
            throw new IllegalArgumentException(
                    "Unable to delete community. The Community has members.");
        }

        return;
    }

    @Override
    public Community addMember(Community community, User member) {
        return null;
    }

    @Override
    public Community removeMember(Community communit, User member) {
        return null;
    }

}
