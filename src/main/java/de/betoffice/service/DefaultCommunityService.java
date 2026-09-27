/*
 * =============================================================================
 * Project betoffice-storage Copyright (c) 2000-2026 by Andre Winkler. All
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

package de.betoffice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import jakarta.persistence.NoResultException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.betoffice.mail.SendUserProfileChangeMailNotification;
import de.betoffice.service.request.CommunityCreateCommand;
import de.betoffice.storage.community.CommunityDao;
import de.betoffice.storage.community.CommunityDto;
import de.betoffice.storage.community.CommunityFilter;
import de.betoffice.storage.community.entity.CommunityDtoMapper;
import de.betoffice.storage.community.entity.CommunityEntity;
import de.betoffice.storage.community.entity.CommunityReference;
import de.betoffice.storage.season.SeasonDao;
import de.betoffice.storage.season.entity.SeasonEntity;
import de.betoffice.storage.season.entity.SeasonReference;
import de.betoffice.storage.time.DateTimeProvider;
import de.betoffice.storage.user.UserDao;
import de.betoffice.storage.user.entity.Nickname;
import de.betoffice.storage.user.entity.UserEntity;
import de.betoffice.validation.ServiceResult;
import de.betoffice.validation.ValidationMessage.MessageType;
import de.betoffice.validation.ValidationMessages.ValidationMessagesBuilder;

/**
 * Manages a community.
 * 
 * @author Andre Winkler
 */
@Service
@Transactional(readOnly = true)
public class DefaultCommunityService extends AbstractManagerService implements CommunityService {

    private final CommunityDao communityDao;
    private final UserDao userDao;
    private final SeasonDao seasonDao;

    public DefaultCommunityService(
            final CommunityDao communityDao,
            final UserDao userDao,
            final SeasonDao seasonDao,
            final SendUserProfileChangeMailNotification sendUserProfileChangeMailNotification,
            final DateTimeProvider dateTimeProvider) {
        this.communityDao = communityDao;
        this.userDao = userDao;
        this.seasonDao = seasonDao;
    }

    @Override
    public CommunityDto find(Long communityId) {
        CommunityEntity byId = communityDao.findById(communityId);
        return CommunityDtoMapper.map(byId);
    }

    @Override
    public Optional<CommunityDto> find(CommunityReference communityReference) {
        return CommunityDtoMapper.map(communityDao.find(communityReference));
    }

    @Override
    public List<CommunityDto> find(String communityName) {
        return CommunityDtoMapper.map(communityDao.find(communityName));
    }

    @Override
    public Page<CommunityDto> findCommunities(CommunityFilter communityFilter, Pageable pageable) {
        return communityDao.findAll(communityFilter, pageable).map(CommunityDtoMapper::map);
    }

    @Override
    public Set<UserEntity> findMembers(CommunityReference communityReference) {
        try {
            final CommunityEntity community = communityDao.findMembers(communityReference);
            return community.getUsers();
        } catch (NoResultException ex) {
            return Set.of();
        }
    }

    @Override
    @Transactional
    public ServiceResult<CommunityDto> create(CommunityCreateCommand communityCreateCommand) {
        final ValidationMessagesBuilder vmb = new ValidationMessagesBuilder();
        final Optional<CreateCommunityResolved> createCommunityResolved = validateAndResolveCreateCommunity(
                vmb, communityCreateCommand);

        if (vmb.containsAnError()) {
            return ServiceResult.failure(vmb.build());
        }

        final CommunityEntity community = persistCommunity(createCommunityResolved.orElseThrow());
        return ServiceResult.success(CommunityDtoMapper.map(community));
    }

    @Override
    @Transactional
    public ServiceResult<Void> delete(CommunityReference reference) {
        final ValidationMessagesBuilder vmb = new ValidationMessagesBuilder();
        final Optional<DeleteCommunityResolved> community = validateAndResolveCommunityForDelete(vmb, reference);
        if (vmb.containsAnError()) {
            return ServiceResult.failure(vmb.build());
        }

        communityDao.delete(community.orElseThrow().community());
        return ServiceResult.success();
    }

    private CommunityEntity persistCommunity(CreateCommunityResolved ccr) {
        final CommunityEntity community = new CommunityEntity();
        community.setYear(ccr.communityCreateCommand().communityYear());
        community.setName(ccr.communityCreateCommand().communityName());
        community.setReference(ccr.communityCreateCommand().communityRef());
        community.setCommunityManager(ccr.communityManager);
        community.setSeason(ccr.season());
        communityDao.persist(community);
        return community;
    }

    //
    //    private CreateUserValidationContext validateCreateUserCommand(
    //            final ValidationMessagesBuilder vmb,
    //            final UserCreateCommand cmd) {
    //
    //        return new CreateUserValidationContext(vmb)
    //                .validateNicknameIsNotBlank(cmd.nickname())
    //                .validateNicknameIsUnique(cmd.nickname())
    //                .validateEmail(cmd.email());
    //    }
    //

    @Override
    @Transactional
    public ServiceResult<CommunityDto> addMember(CommunityReference communityReference, Nickname nickname) {
        final ValidationMessagesBuilder vmb = new ValidationMessagesBuilder();
        final Optional<AddMemberResolved> resolved = validateAndResolveAddMember(vmb, communityReference, nickname);

        if (vmb.containsAnError()) {
            return ServiceResult.failure(vmb.build());
        }

        final AddMemberResolved value = resolved.orElseThrow();
        value.community().addMember(value.user());
        communityDao.update(value.community());
        return ServiceResult.success(CommunityDtoMapper.map(value.community()));
    }

    @Override
    @Transactional
    public ServiceResult<CommunityDto> addMembers(CommunityReference communityReference, Set<Nickname> nicknames) {
        final ValidationMessagesBuilder vmb = new ValidationMessagesBuilder();
        final Optional<AddMembersResolved> resolved = validateAndResolveAddMembers(vmb, communityReference, nicknames);

        if (vmb.containsAnError()) {
            return ServiceResult.failure(vmb.build());
        }

        final AddMembersResolved value = resolved.orElseThrow();
        value.users().forEach(value.community()::addMember);
        communityDao.update(value.community());
        return ServiceResult.success(CommunityDtoMapper.map(value.community()));
    }

    @Override
    @Transactional
    public CommunityDto removeMember(CommunityReference reference, Nickname nickname) {
        UserEntity user = userDao.findByNickname(nickname).orElseThrow();
        CommunityEntity community = communityDao.find(reference).orElseThrow();
        community.removeMember(user);
        communityDao.update(community);
        return CommunityDtoMapper.map(community);
    }

    @Override
    @Transactional
    public CommunityDto removeMembers(CommunityReference reference, Set<Nickname> nicknames) {
        nicknames.stream().forEach(nickname -> {
            removeMember(reference, nickname);
        });
        return CommunityDtoMapper.map(communityDao.find(reference).orElseThrow());
    }

    private Optional<CreateCommunityResolved> validateAndResolveCreateCommunity(
            final ValidationMessagesBuilder vmb,
            final CommunityCreateCommand communityCreateCommand) {

        final Optional<SeasonEntity> season = resolveSeason(vmb, communityCreateCommand.seasonRef());
        final Optional<UserEntity> communityManager = resolveUser(vmb, communityCreateCommand.managerNickname());

        if (vmb.containsAnError()) {
            return Optional.empty();
        }
        return Optional.of(new CreateCommunityResolved(communityCreateCommand, season.orElseThrow(),
                communityManager.orElseThrow()));
    }

    private Optional<DeleteCommunityResolved> validateAndResolveCommunityForDelete(
            final ValidationMessagesBuilder vmb,
            final CommunityReference communityReference) {

        final Optional<CommunityEntity> community = resolveCommunity(vmb, communityReference);

        if (communityDao.hasMembers(communityReference)) {
            vmb.addFormattedMessage(MessageType.COMMUNITY_CANNOT_BE_DELETED_CAUSE_OF_MEMBERS, communityReference);
        }
        if (vmb.containsAnError()) {
            return Optional.empty();
        }
        return Optional.of(new DeleteCommunityResolved(community.orElseThrow()));
    }

    private Optional<AddMemberResolved> validateAndResolveAddMember(
            final ValidationMessagesBuilder vmb,
            final CommunityReference communityReference,
            final Nickname nickname) {

        final Optional<CommunityEntity> community = resolveCommunity(vmb, communityReference);
        final Optional<UserEntity> user = resolveUser(vmb, nickname);

        if (vmb.containsAnError()) {
            return Optional.empty();
        }
        return Optional.of(new AddMemberResolved(community.orElseThrow(), user.orElseThrow()));
    }

    private Optional<AddMembersResolved> validateAndResolveAddMembers(
            final ValidationMessagesBuilder vmb,
            final CommunityReference communityReference,
            final Set<Nickname> nicknames) {

        final Optional<CommunityEntity> community = resolveCommunity(vmb, communityReference);
        final List<UserEntity> users = new ArrayList<>();

        nicknames.forEach(nickname -> resolveUser(vmb, nickname).ifPresent(users::add));

        if (vmb.containsAnError()) {
            return Optional.empty();
        }
        return Optional.of(new AddMembersResolved(community.orElseThrow(), users));
    }

    private Optional<CommunityEntity> resolveCommunity(
            final ValidationMessagesBuilder vmb,
            final CommunityReference communityReference) {

        final Optional<CommunityEntity> optionalCommunity = communityDao.find(communityReference);
        if (optionalCommunity.isEmpty()) {
            vmb.addFormattedMessage(MessageType.COMMUNITY_NOT_FOUND, communityReference);
        }
        return optionalCommunity;
    }

    private Optional<UserEntity> resolveUser(
            final ValidationMessagesBuilder vmb,
            final Nickname nickname) {

        final Optional<UserEntity> optionalUser = userDao.findByNickname(nickname);
        if (optionalUser.isEmpty()) {
            vmb.addFormattedMessage(MessageType.USER_NOT_FOUND, nickname);
        }
        return optionalUser;
    }

    private Optional<SeasonEntity> resolveSeason(
            final ValidationMessagesBuilder vmb,
            final SeasonReference seasonReference) {

        final Optional<SeasonEntity> optionalSeason = seasonDao.find(seasonReference);
        if (optionalSeason.isEmpty()) {
            vmb.addFormattedMessage(MessageType.SEASON_REFERENCE_NOT_FOUND, seasonReference);
        }
        return optionalSeason;
    }

    private record CreateCommunityResolved(CommunityCreateCommand communityCreateCommand, SeasonEntity season,
            UserEntity communityManager) {
    }

    private record DeleteCommunityResolved(CommunityEntity community) {
    }

    private record AddMemberResolved(CommunityEntity community, UserEntity user) {
    }

    private record AddMembersResolved(CommunityEntity community, List<UserEntity> users) {
    }

}
