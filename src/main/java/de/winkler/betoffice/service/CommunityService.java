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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.winkler.betoffice.storage.Community;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.User;

/**
 * Community service.
 * 
 * @author Andre Winkler
 */
public interface CommunityService {

    /**
     * Find a community by name.
     * 
     * @param  communityName community name
     * @return               a community.
     */
    Community find(String communityName);

    /**
     * Find all communities.
     * 
     * @param  communityNameFilter a name filter
     * @param  pageable            paging parameter
     * @return                     a list of communities
     */
    Page<Community> findCommunities(String communityNameFilter, Pageable pageable);

    /**
     * Find all users.
     * 
     * @param  nicknameFilter a nickname filter
     * @param  pageable       paging parameter
     * @return                list of users
     */
    Page<User> findUsers(String nicknameFilter, Pageable pageable);

    /**
     * Find a community and all members.
     * 
     * @param  communityName a community name
     * @return               a community with all it´s members.
     */
    Community findCommunityMembers(String communityName);

    /**
     * Create a new community.
     * 
     * @param  communityName      community name
     * @param  communityShortName short name of the community name
     * @param  managerNickname    nickname of the community manager
     * @return                    the create community.
     */
    Community create(Season season, String communityName, String communityShortName, String managerNickname);

    /**
     * Delete community.
     * 
     * @param communityName the community name to delete
     */
    void delete(String communityName);

    /**
     * Add a new community member.
     * 
     * @param  communityName the community name
     * @param  nickname      the new community member
     * @return               the updated community.
     */
    Community addMember(String communityName, String nickname);

    /**
     * Remove a community member.
     * 
     * @param  communityName the community name
     * @param  nickname      the community member to remove
     * @return               the updated community.
     */
    Community removeMember(String communityName, String nickname);

}
