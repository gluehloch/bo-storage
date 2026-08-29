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
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.betoffice.service.request.CommunityCreateCommand;
import de.betoffice.storage.community.CommunityDto;
import de.betoffice.storage.community.CommunityFilter;
import de.betoffice.storage.community.entity.CommunityEntity;
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
     * @return                 the create community.
     */
    ServiceResult<CommunityDto> create(CommunityCreateCommand communityCreateCommand);

    /**
     * Delete community.
     * 
     * @param communityRef the community name to delete
     */
    void delete(CommunityReference communityRef);

    /**
     * Add a new community member.
     * 
     * @param  communityRef the community name
     * @param  nickname     the new community member
     * @return              the updated community.
     */
    CommunityDto addMember(CommunityReference communityRef, Nickname nickname);

    /**
     * Add community members.
     * 
     * @param  communityRef
     * @param  nicknames
     * @return
     */
    CommunityDto addMembers(CommunityReference communityRef, Set<Nickname> nicknames);

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

    /**
     * Find all users.
     * 
     * @param  nicknameFilter a nickname filter
     * @param  pageable       paging parameter
     * @return                list of users
     */
    Page<UserEntity> findUsers(String nicknameFilter, Pageable pageable);

    /**
     * Liefert alle bekannten Teilnehmer zurück.
     *
     * @return Die bekannten Teilnehmer.
     */
    List<UserEntity> findAllUsers();

    /**
     * Find a user by nickname.
     *
     * @param  nickname user identified by nickname
     * @return          a user
     */
    Optional<UserEntity> findUser(Nickname nickname);

    /**
     * Neuanlage eines Teilnehmers.
     *
     * @param user Ein Teilnehmer.
     */
    UserEntity createUser(UserEntity user);

    /**
     * Löschen eines Teilnehmers. Ein Teilnehmer kann nur gelöscht werden, wenn dieser keiner Meisterschaft zugeordnet
     * ist.
     * 
     * @param nickname Ein Teilnehmer.
     */
    void deleteUser(Nickname nickname);

    /**
     * Aktualisierung der Daten eines Teilnehmers. Der sogenannte 'Nickname' eines Nutzers kann nicht geändert werden.
     *
     * TODO: Dafür benöitge ich eine seperate Methode. Oder erst gar nicht anbieten!
     *
     * @param adminOperation    Administrator Aktion? Dieser kann die Mail-Adresse beliebig ändern.
     * @param nickname          Nutzerkürzel
     * @param name              Name
     * @param surname           Vorname
     * @param mail              Mail Adresse
     * @param emailNotification Email Benachrichtigung einschalten?
     * @param phone             Telefonnummer
     */
    Optional<UserEntity> updateUser(boolean adminOperation, Nickname nickname, String name, String surname, String mail,
            boolean emailNotification, String phone);

    /**
     * Bestätigt die Änderung der Mail Adresse.
     *
     * @param  nickname    Nutzerkürzel
     * @param  changeToken Das Token für die Änderung der Mail Adresse
     * @return             Der geänderte Nutzer
     */
    ServiceResult<UserEntity> confirmMailAddressChange(Nickname nickname, String changeToken);

    /**
     * Versendet die Bestätigungs Mail noch einmal an das neue Email Postfach.
     * 
     * @param  nickname Nutzerkürzel
     * @return          Der betroffene Nutzer
     */
    Optional<UserEntity> resubmitConfirmationMail(Nickname nickname);

    /**
     * Nimmt den Email-Änderungswunsch wieder zurück.
     * 
     * @param  nickname Nutzerkürzel
     * @return          Der nicht geänderte Nutzer
     */
    Optional<UserEntity> abortMailAddressChange(Nickname nickname);

    /**
     * Sucht nach einem Teilnehmer.
     * 
     * @param  userId Die Teilnehmer ID
     * @return        Ein Teilnehmer
     */
    UserEntity findUser(long userId);

    /**
     * Sucht nach einem Teilnehmer anhand seine Change-Tokens.
     * 
     * @param  changeToken Das Token mit der Nutzer seine Mail-Änderung quittieren kann.
     * @return
     */
    Optional<UserEntity> findUserByChangeToken(String changeToken);

}
