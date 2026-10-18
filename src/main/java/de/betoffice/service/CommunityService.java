/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2026 by Andre Winkler. All
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
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.betoffice.service.request.CommunityCreateCommand;
import de.betoffice.storage.community.CommunityDto;
import de.betoffice.storage.community.CommunityFilter;
import de.betoffice.storage.community.entity.CommunityReference;
import de.betoffice.storage.season.entity.SeasonReference;
import de.betoffice.storage.user.entity.Nickname;
import de.betoffice.storage.user.entity.UserEntity;
import de.betoffice.validation.ServiceResult;

/**
 * Community service.
 * 
 * @author Andre Winkler
 */
public interface CommunityService {

    public static final String DEFAULT_PLAYER_GROUP = "TDKB";

    static CommunityReference defaultPlayerGroup(SeasonReference seasonReference) {
        return CommunityReference.of(String.format("%s %s", DEFAULT_PLAYER_GROUP, seasonReference.getYear()));
    }

    /**
     * Find a community by its id.
     * 
     * @param  communityId community id
     * @return             a community
     */
    CommunityDto find(Long communityId);

    /**
     * Find a community by its reference.
     *
     * @param  communityReference a community reference
     * @return                    a community
     */
    Optional<CommunityDto> find(CommunityReference communityReference);

    /**
     * Find a community by name.
     * 
     * @param  communityName community name
     * @return               a community.
     */
    List<CommunityDto> find(String communityName);

    /**
     * All users of a community
     * 
     * @param  communityReference reference of a community
     * @return                    the users of a community.
     */
    Set<UserEntity> findMembers(CommunityReference communityReference);

    /**
     * Find all communities.
     *
     * @param  communityFilter a community filter
     * @param  pageable        paging parameter
     * @return                 a list of communities
     */
    Page<CommunityDto> findCommunities(CommunityFilter communityFilter, Pageable pageable);

    /**
     * Create a new community.
     *
     * @param  communityCreateCommand everything needed to create a new community
     * @return                        the create community.
     */
    ServiceResult<CommunityDto> create(CommunityCreateCommand communityCreateCommand);

    /**
     * Delete community.
     * 
     * @param communityRef the community name to delete
     * @return service result and the deleted community
     */
    ServiceResult<Void> delete(CommunityReference communityRef);

    /**
     * Add a new community member.
     * 
     * @param  communityRef the community name
     * @param  nickname     the new community member
     * @return              the updated community.
     */
    ServiceResult<CommunityDto> addMember(CommunityReference communityRef, Nickname nickname);

    /**
     * Add community members.
     * 
     * @param  communityRef
     * @param  nicknames
     * @return
     */
    ServiceResult<CommunityDto> addMembers(CommunityReference communityRef, Set<Nickname> nicknames);

    /**
     * Remove a community member.
     * 
     * @param  communityRef the community name
     * @param  nickname     the community member to remove
     * @return              the updated community.
     */
    CommunityDto removeMember(CommunityReference communityRef, Nickname nickname);

    /**
     * Remove community members.
     * 
     * @param  communityRef the community name
     * @param  nicknames    the community members to remove
     * @return              the updated community.
     */
    CommunityDto removeMembers(CommunityReference communityRef, Set<Nickname> nicknames);

}
