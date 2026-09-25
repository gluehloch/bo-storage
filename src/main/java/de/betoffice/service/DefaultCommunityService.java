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

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.NoResultException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.betoffice.mail.NotificationType;
import de.betoffice.mail.SendUserProfileChangeMailNotification;
import de.betoffice.service.request.CommunityCreateCommand;
import de.betoffice.service.request.UserCreateCommand;
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
import de.betoffice.storage.user.entity.UserProfileDto;
import de.betoffice.storage.user.entity.UserProfileDtoMapper;
import de.betoffice.util.LoggerFactory;
import de.betoffice.validation.ServiceResult;
import de.betoffice.validation.ValidationMessage;
import de.betoffice.validation.ValidationMessage.MessageType;
import de.betoffice.validation.ValidationMessages;
import de.betoffice.validation.ValidationMessages.ValidationMessagesBuilder;

/**
 * Manages a community.
 * 
 * @author Andre Winkler
 */
@Service
@Transactional(readOnly = true)
public class DefaultCommunityService extends AbstractManagerService implements CommunityService {

    private static final Logger LOG = LoggerFactory.make();

    private final CommunityDao communityDao;
    private final UserDao userDao;
    private final SeasonDao seasonDao;
    private final SendUserProfileChangeMailNotification sendUserProfileChangeMailNotification;
    private final DateTimeProvider dateTimeProvider;

    public DefaultCommunityService(
            final CommunityDao communityDao,
            final UserDao userDao,
            final SeasonDao seasonDao,
            final SendUserProfileChangeMailNotification sendUserProfileChangeMailNotification,
            final DateTimeProvider dateTimeProvider) {
        this.communityDao = communityDao;
        this.userDao = userDao;
        this.seasonDao = seasonDao;
        this.sendUserProfileChangeMailNotification = sendUserProfileChangeMailNotification;
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Optional<UserEntity> findUser(Nickname nickname) {
        return userDao.findByNickname(nickname);
    }

    @Override
    public List<UserEntity> findAllUsers() {
        return userDao.findAll();
    }

    @Override
    public UserEntity findUser(long userId) {
        return userDao.findById(userId);
    }

    @Override
    public Optional<UserEntity> findUserByChangeToken(String changeToken) {
        return userDao.findByChangeToken(changeToken);
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
    public Page<UserEntity> findUsers(String nicknameFilter, Pageable pageable) {
        return userDao.findAll(nicknameFilter, pageable);
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
        final CreateCommunityValidationContext vc = validateCreateCommunityCommand(new ValidationMessagesBuilder(),
                communityCreateCommand);
        if (vc.getValidationMessages().containsAnError()) {
            return ServiceResult.failure(vc.getValidationMessages());
        } else {
            final CommunityEntity community = persistCommunity(communityCreateCommand, vc);
            return ServiceResult.sucess(CommunityDtoMapper.map(community));
        }
    }

    private CommunityEntity persistCommunity(CommunityCreateCommand communityCreateCommand,
            final CreateCommunityValidationContext vc) {
        final CommunityEntity community = new CommunityEntity();
        community.setYear(communityCreateCommand.communityYear());
        community.setName(communityCreateCommand.communityName());
        community.setReference(communityCreateCommand.communityRef());
        community.setCommunityManager(vc.getCommunityManager());
        community.setSeason(vc.getSeason());
        communityDao.persist(community);
        return community;
    }

    private UserEntity persistUser(UserCreateCommand userCreateCommand, final CreateUserValidationContext vc) {
        final UserEntity user = new UserEntity();
        user.setNickname(Nickname.of(userCreateCommand.nickname()));
        user.setEmail(userCreateCommand.email());
        user.setName(userCreateCommand.lastName());
        user.setSurname(userCreateCommand.firstName());
        user.setAdmin(false);
        user.setAutomat(false);
        user.setNotification(NotificationType.TIPP);
        user.setPassword(userCreateCommand.password());
        user.setPhone(userCreateCommand.phone());
        user.setTitle(null);
        userDao.persist(user);
        return user;
    }

    private CreateCommunityValidationContext validateCreateCommunityCommand(
            final ValidationMessagesBuilder vmb,
            final CommunityCreateCommand cmd) {

        return new CreateCommunityValidationContext(vmb)
                .validateCommunityReferenceDoesNotExist(cmd.communityRef())
                .validateSeason(cmd.seasonRef())
                .validateCommunityManager(cmd.managerNickname());
    }

    private CreateUserValidationContext validateCreateUserCommand(
            final ValidationMessagesBuilder vmb,
            final UserCreateCommand cmd) {

        return new CreateUserValidationContext(vmb)
                .validateNicknameIsNotBlank(cmd.nickname())
                .validateNicknameIsUnique(cmd.nickname())
                .validateEmail(cmd.email());
    }

    private AddMemberValidationContext validateAddMemberCommand(
            final ValidationMessagesBuilder vmb,
            final CommunityReference communityReference,
            final Nickname nickname) {

        return new AddMemberValidationContext(vmb)
                .validateCommunityExists(communityReference)
                .validateUserExists(nickname);
    }

    private AddMemberValidationContext validateAddMembersCommand(
            final ValidationMessagesBuilder vmb,
            final CommunityReference communityReference,
            final Set<Nickname> nicknames) {

        return new AddMemberValidationContext(vmb)
                .validateCommunityExists(communityReference)
                .validateUsersExists(nicknames);
    }

    @Override
    @Transactional
    public void delete(CommunityReference reference) {
        CommunityEntity community = communityDao.find(reference).orElseThrow();

        if (communityDao.hasMembers(reference)) {
            LOG.warn("Unable to delete community '{}'. The Community has members.", community);
            throw new IllegalArgumentException("Unable to delete community. The Community has members.");
        }

        communityDao.delete(community);
    }

    @Override
    @Transactional
    public ServiceResult<CommunityDto> addMember(CommunityReference communityReference, Nickname nickname) {
        final AddMemberValidationContext vc = validateAddMemberCommand(new ValidationMessagesBuilder(),
                communityReference, nickname);
        if (vc.getValidationMessages().containsAnError()) {
            return ServiceResult.failure(vc.getValidationMessages());
        } else {
            vc.getCommunity().addMember(vc.getUser());
            communityDao.update(vc.getCommunity());
            return ServiceResult.sucess(CommunityDtoMapper.map(vc.getCommunity()));
        }
    }

    @Override
    @Transactional
    public ServiceResult<CommunityDto> addMembers(CommunityReference communityReference, Set<Nickname> nicknames) {
        final AddMemberValidationContext vc = validateAddMembersCommand(new ValidationMessagesBuilder(),
                communityReference, nicknames);
        if (vc.getValidationMessages().containsAnError()) {
            return ServiceResult.failure(vc.getValidationMessages());
        } else {
            nicknames.stream()
                    .map(n -> userDao.findByNickname(n)).filter( Optional::isPresent)
                    .map(us -> vc.getCommunity().addMember(us));
            communityDao.update(vc.getCommunity());
            return ServiceResult.sucess(CommunityDtoMapper.map(vc.getCommunity()));
        }
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

    @Override
    @Transactional
    public ServiceResult<UserProfileDto> create(final UserCreateCommand user) {
        final CreateUserValidationContext vc = validateCreateUserCommand(new ValidationMessagesBuilder(), user);
        if (vc.getValidationMessages().containsAnError()) {
            return ServiceResult.failure(vc.getValidationMessages());
        } else {
            final UserEntity userEntity = persistUser(user, vc);
            return ServiceResult.sucess(UserProfileDtoMapper.map(userEntity));
        }
    }

    @Override
    @Transactional
    public void deleteUser(final Nickname nickname) {
        userDao.findByNickname(nickname).ifPresent(u -> userDao.delete(u));
    }

    @Override
    @Transactional
    public Optional<UserEntity> updateUser(
            final boolean adminOperation,
            final Nickname nickname,
            final String name,
            final String surname,
            final String mail,
            final boolean emailNotification,
            final String phone) {

        return userDao.findByNickname(nickname).map(u -> {
            u.setName(name);
            u.setSurname(surname);
            u.setPhone(phone);
            u.setNotification(emailNotification ? NotificationType.TIPP : NotificationType.NONE);
            if (!adminOperation && hasUserChangedHisMailAddress(u, mail) && u.getChangeSend() < 5) {
                u.setChangeEmail(mail);
                u.setChangeToken(UUID.randomUUID().toString());
                u.setChangeDateTime(dateTimeProvider.currentDateTime());
                u.setChangeDateTime(dateTimeProvider.currentDateTime());
                sendUserProfileChangeMailNotification.send(u);
                u.incrementChangeSend();
            } else {
                u.setEmail(mail);
                u.abortEmailChange();
            }
            return u;
        });
    }

    private boolean hasUserChangedHisMailAddress(final UserEntity user, final String newMailAddress) {
        return !StringUtils.equals(user.getEmail(), newMailAddress);
    }

    @Override
    @Transactional
    public ServiceResult<UserEntity> confirmMailAddressChange(final Nickname nickname, final String changeToken) {
        final Optional<UserEntity> optionalUser = userDao.findByNickname(nickname);
        if (optionalUser.isEmpty()) {
            return ServiceResult.failureWithFormattedError(MessageType.USER_NOT_FOUND, nickname.toString());
        }

        final UserEntity user = optionalUser.get();
        if (StringUtils.equals(changeToken, user.getChangeToken())) {
            final var changeDateTime = user.getChangeDateTime();
            final ZonedDateTime changeDateTimePlusTenMinutes = changeDateTime.plusMinutes(10);
            // --- mailChange --- +10m --- now
            final var now = dateTimeProvider.currentDateTime();
            if (changeDateTime.isAfter(now)) {
                return ServiceResult.failure(MessageType.EMAIL_CHANGE_DATETIME_IS_IN_THE_FUTURE);
            } else if (now.isBefore(changeDateTimePlusTenMinutes)) {
                user.acceptEmailChange();
                return ServiceResult.sucess(user);
            } else {
                return ServiceResult.failure(MessageType.EMAIL_CHANGE_DATETIME_EXPIRED);
            }
        } else {
            LOG.warn("Unable to confirm email change. ChangeTokens are different. {} vs {}", changeToken,
                    user.getChangeToken());
            throw new IllegalArgumentException("Unable to confirm email change. ChangeTokens are different.");
        }
    }

    @Override
    @Transactional
    public Optional<UserEntity> abortMailAddressChange(final Nickname nickname) {
        return userDao.findByNickname(nickname).map(u -> u.abortEmailChange());
    }

    @Override
    @Transactional
    public Optional<UserEntity> resubmitConfirmationMail(final Nickname nickname) {
        return userDao.findByNickname(nickname)
                .filter(u -> u.getChangeSend() < 5)
                .map(u -> sendUserProfileChangeMailNotification.send(u));
    }

    private class AddMemberValidationContext {
        private ValidationMessagesBuilder validationMessagesBuilder;
        private CommunityEntity community;
        private UserEntity user;

        public AddMemberValidationContext(ValidationMessagesBuilder validationMessagesBuilder) {
            this.validationMessagesBuilder = validationMessagesBuilder;
        }

        ValidationMessages getValidationMessages() {
            return validationMessagesBuilder.build();
        }

        public CommunityEntity getCommunity() {
            return community;
        }

        public UserEntity getUser() {
            return user;
        }

        public AddMemberValidationContext validateCommunityExists(CommunityReference communityReference) {
            final Optional<CommunityEntity> optionalCommunity = communityDao.find(communityReference);
            if (optionalCommunity.isEmpty()) {
                validationMessagesBuilder.addFormattedMessage(MessageType.COMMUNITY_NOT_FOUND, communityReference);
            } else {
                this.community = optionalCommunity.get();
            }
            return this;
        }

        public AddMemberValidationContext validateUserExists(Nickname nickname) {
            if (userDao.findByNickname(nickname).isEmpty()) {
                validationMessagesBuilder.addFormattedMessage(MessageType.USER_NOT_FOUND, nickname);
            }
            return this;
        }

        public AddMemberValidationContext validateUsersExists(Set<Nickname> nickname) {
            nickname.forEach(n -> validateUserExists(n));
            return this;
        }
    }

    private class CreateUserValidationContext {
        private ValidationMessagesBuilder validationMessagesBuilder;

        public CreateUserValidationContext(ValidationMessagesBuilder validationMessagesBuilder) {
            this.validationMessagesBuilder = validationMessagesBuilder;
        }

        ValidationMessages getValidationMessages() {
            return validationMessagesBuilder.build();
        }

        public CreateUserValidationContext validateNicknameIsNotBlank(String nickname) {
            if (nickname == null || StringUtils.isBlank(nickname)) {
                validationMessagesBuilder.add(ValidationMessage.error(MessageType.NICKNAME_IS_NOT_SET));
            }
            return this;
        }

        public CreateUserValidationContext validateNicknameIsUnique(String nickname) {
            if (validationMessagesBuilder.containsAnError()) {
                return this;
            }

            final List<UserEntity> lowerCaseNickname = userDao.findLowerCaseNickname(nickname);
            if (!lowerCaseNickname.isEmpty()) {
                validationMessagesBuilder.addFormattedMessage(MessageType.NICKNAME_ALREADY_EXISTS, nickname);
            }
            return this;
        }

        public CreateUserValidationContext validateEmail(String email) {
            if (StringUtils.isBlank(email)) {
                validationMessagesBuilder.add(ValidationMessage.error(MessageType.MAIL_IS_NOT_SET));
            }
            return this;
        }
    }

    private class CreateCommunityValidationContext {
        private ValidationMessagesBuilder validationMessagesBuilder;
        private SeasonEntity season;
        private UserEntity communityManager;

        public CreateCommunityValidationContext(ValidationMessagesBuilder validationMessagesBuilder) {
            this.validationMessagesBuilder = validationMessagesBuilder;
        }

        ValidationMessages getValidationMessages() {
            return validationMessagesBuilder.build();
        }

        SeasonEntity getSeason() {
            return season;
        }

        UserEntity getCommunityManager() {
            return communityManager;
        }

        public CreateCommunityValidationContext validateSeason(SeasonReference seasonRef) {
            final var season = DefaultCommunityService.this.seasonDao.find(seasonRef);
            if (!season.isPresent()) {
                validationMessagesBuilder.addFormattedMessage(MessageType.SEASON_REFERENCE_NOT_FOUND, seasonRef);
            } else {
                this.season = season.get();
            }
            return this;
        }

        public CreateCommunityValidationContext validateCommunityManager(Nickname managerNickname) {
            final var user = DefaultCommunityService.this.userDao.findByNickname(managerNickname);
            if (!user.isPresent()) {
                validationMessagesBuilder.addFormattedMessage(MessageType.USER_NOT_FOUND, managerNickname);
            } else {
                this.communityManager = user.get();
            }
            return this;
        }

        public CreateCommunityValidationContext validateCommunityReferenceDoesNotExist(
                CommunityReference communityRef) {
            if (DefaultCommunityService.this.communityDao.find(communityRef).isPresent()) {
                validationMessagesBuilder.addFormattedMessage(MessageType.COMMUNITY_EXISTS, communityRef);
            }
            return this;
        }
    }

}
