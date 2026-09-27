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
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.betoffice.mail.NotificationType;
import de.betoffice.mail.SendUserProfileChangeMailNotification;
import de.betoffice.service.request.UserCreateCommand;
import de.betoffice.storage.time.DateTimeProvider;
import de.betoffice.storage.user.UserDao;
import de.betoffice.storage.user.entity.Nickname;
import de.betoffice.storage.user.entity.UserEntity;
import de.betoffice.storage.user.entity.UserProfileDto;
import de.betoffice.storage.user.entity.UserProfileDtoMapper;
import de.betoffice.util.LoggerFactory;
import de.betoffice.validation.ServiceResult;
import de.betoffice.validation.ValidationMessage.MessageType;
import de.betoffice.validation.ValidationMessages.ValidationMessagesBuilder;

@Service
@Transactional(readOnly = true)
public class DefaultUserService extends AbstractManagerService implements UserService {

    private static final Logger LOG = LoggerFactory.make();

    private final UserDao userDao;
    private final SendUserProfileChangeMailNotification sendUserProfileChangeMailNotification;
    private final DateTimeProvider dateTimeProvider;

    public DefaultUserService(
            UserDao userDao,
            SendUserProfileChangeMailNotification sendUserProfileChangeMailNotification,
            DateTimeProvider dateTimeProvider) {
        this.userDao = userDao;
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
    public Page<UserEntity> findUsers(String nicknameFilter, Pageable pageable) {
        return userDao.findAll(nicknameFilter, pageable);
    }

    @Override
    @Transactional
    public ServiceResult<UserProfileDto> create(final UserCreateCommand user) {
        final ValidationMessagesBuilder vmb = new ValidationMessagesBuilder();
        validateNickname(vmb, user.nickname());

        if (vmb.containsAnError()) {
            return ServiceResult.failure(vmb.build());
        }

        final UserEntity userEntity = persistUser(user);
        return ServiceResult.success(UserProfileDtoMapper.map(userEntity));
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
        return !Objects.equals(user.getEmail(), newMailAddress);
    }

    @Override
    @Transactional
    public ServiceResult<UserEntity> confirmMailAddressChange(final Nickname nickname, final String changeToken) {
        final Optional<UserEntity> optionalUser = userDao.findByNickname(nickname);
        if (optionalUser.isEmpty()) {
            return ServiceResult.failureWithFormattedError(MessageType.USER_NOT_FOUND, nickname.toString());
        }

        final UserEntity user = optionalUser.get();
        if (Objects.equals(changeToken, user.getChangeToken())) {
            final var changeDateTime = user.getChangeDateTime();
            final ZonedDateTime changeDateTimePlusTenMinutes = changeDateTime.plusMinutes(10);
            // --- mailChange --- +10m --- now
            final var now = dateTimeProvider.currentDateTime();
            if (changeDateTime.isAfter(now)) {
                return ServiceResult.failure(MessageType.EMAIL_CHANGE_DATETIME_IS_IN_THE_FUTURE);
            } else if (now.isBefore(changeDateTimePlusTenMinutes)) {
                user.acceptEmailChange();
                return ServiceResult.success(user);
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

    private UserEntity persistUser(UserCreateCommand userCreateCommand) {
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

    private ValidationMessagesBuilder validateNickname(final ValidationMessagesBuilder vmb, final String nickname) {
        if (StringUtils.isBlank(nickname)) {
            vmb.addError(MessageType.NICKNAME_IS_NOT_SET);
        } else if (!nickname.equals(StringUtils.trim(nickname))) {
            vmb.addFormattedMessage(MessageType.NICKNAME_CONTAINS_WHITESPACES_AT_THE_BEGINNING_OR_END);
        } else {
            final List<UserEntity> lowerCaseNickname = userDao.findLowerCaseNickname(nickname);
            if (!lowerCaseNickname.isEmpty()) {
                vmb.addFormattedMessage(MessageType.NICKNAME_ALREADY_EXISTS, nickname);
            }
        }
        return vmb;
    }

}
