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
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.winkler.betoffice.storage.Community;
import de.winkler.betoffice.storage.CommunityFilter;
import de.winkler.betoffice.storage.CommunityReference;
import de.winkler.betoffice.storage.Nickname;
import de.winkler.betoffice.storage.SeasonReference;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.validation.BetofficeServiceResult;

/**
 * Community service.
 * 
 * @author Andre Winkler
 */
public interface CommunityService {

    /**
     * Find a community by its reference.
     *
     * @param communityReference a community reference
     * @return a community
     */
    Optional<Community> find(CommunityReference communityReference);

    /**
     * Find a community by name.
     * 
     * @param communityName community name
     * @return a community.
     */
    List<Community> find(String communityName);

    /**
     * All users of a community
     * 
     * @param communityReference reference of a community
     * @return the users of a community.
     */
    List<User> findMembers(CommunityReference communityReference);

    /**
     * Find all communities.
     *
     * @param communityFilter a community filter
     * @param pageable        paging parameter
     * @return a list of communities
     */
    Page<Community> findCommunities(CommunityFilter communityFilter, Pageable pageable);

    /**
     * Create a new community.
     *
     * @param communityRef    reference of a community.
     * @param seasonRef       reference of a season.
     * @param communityName   community name
     * @param managerNickname nickname of the community manager
     * @return the create community.
     */
    BetofficeServiceResult<Community> create(CommunityReference communityRef, SeasonReference seasonRef,
            String communityName, Nickname managerNickname);

    /**
     * Delete community.
     * 
     * @param communityRef the community name to delete
     */
    void delete(CommunityReference communityRef);

    /**
     * Add a new community member.
     * 
     * @param communityRef the community name
     * @param nickname     the new community member
     * @return the updated community.
     */
    Community addMember(CommunityReference communityRef, Nickname nickname);

    /**
     * Add community members.
     * 
     * @param communityRef
     * @param nicknames
     * @return
     */
    Community addMembers(CommunityReference communityRef, Set<Nickname> nicknames);

    /**
     * Remove a community member.
     * 
     * @param communityRef the community name
     * @param nickname     the community member to remove
     * @return the updated community.
     */
    Community removeMember(CommunityReference communityRef, Nickname nickname);

    /**
     * Find all users.
     * 
     * @param nicknameFilter a nickname filter
     * @param pageable       paging parameter
     * @return list of users
     */
    Page<User> findUsers(String nicknameFilter, Pageable pageable);

    /**
     * Liefert alle bekannten Teilnehmer zurück.
     *
     * @return Die bekannten Teilnehmer.
     */
    List<User> findAllUsers();

    /**
     * Find a user by nickname.
     *
     * @param nickname user identified by nickname
     * @return a user
     */
    Optional<User> findUser(Nickname nickname);

    /**
     * Neuanlage eines Teilnehmers.
     *
     * @param user Ein Teilnehmer.
     */
    User createUser(User user);

    /**
     * Löschen eines Teilnehmers. Ein Teilnehmer kann nur gelöscht werden, wenn
     * dieser keiner Meisterschaft zugeordnet ist.
     * 
     * @param nickname Ein Teilnehmer.
     */
    void deleteUser(Nickname nickname);

    /**
     * Aktualisierung der Daten eines Teilnehmers.
     *
     * @param user Ein Teilnehmer.
     */
    void updateUser(User user);

    /**
     * Sucht nach einem Teilnehmer.
     * 
     * @param userId Die Teilnehmer ID
     * @return Ein Teilnehmer
     */
    User findUser(long userId);

}
