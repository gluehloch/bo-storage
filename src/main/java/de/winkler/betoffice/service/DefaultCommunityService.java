/*
 * =============================================================================
 * Project betoffice-storage Copyright (c) 2000-2025 by Andre Winkler. All
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
import java.util.UUID;

import jakarta.persistence.NoResultException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.winkler.betoffice.dao.CommunityDao;
import de.winkler.betoffice.dao.SeasonDao;
import de.winkler.betoffice.dao.UserDao;
import de.winkler.betoffice.mail.SendUserProfileChangeMailNotification;
import de.winkler.betoffice.storage.Community;
import de.winkler.betoffice.storage.CommunityFilter;
import de.winkler.betoffice.storage.CommunityReference;
import de.winkler.betoffice.storage.Nickname;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.SeasonReference;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.enums.NotificationType;
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
    public Optional<User> findUser(Nickname nickname) {
        return userDao.findByNickname(nickname);
    }

    @Override
    public List<User> findAllUsers() {
        return userDao.findAll();
    }

    @Override
    public User findUser(long userId) {
        return userDao.findById(userId);
    }

    @Override
    public Optional<User> findUserByChangeToken(String changeToken) {
        return userDao.findByChangeToken(changeToken);
    }

    @Override
    public Community find(Long communityId) {
        return communityDao.findById(communityId);
    }

    @Override
    public Optional<Community> find(CommunityReference communityReference) {
        return communityDao.find(communityReference);
    }

    @Override
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
        try {
            final Community community = communityDao.findMembers(communityReference);
            return community.getUsers();
        } catch (NoResultException ex) {
            return Set.of();
        }
    }

    @Override
    @Transactional
    public BetofficeServiceResult<Community> create(
            CommunityReference communityRef,
            SeasonReference seasonRef,
            String communityName,
            String communityYear,
            Nickname managerNickname) {

        Optional<Community> definedCommunity = communityDao.find(communityRef);
        if (definedCommunity.isPresent()) {
            return BetofficeServiceResult.failure(ErrorType.COMMUNITY_EXISTS);
        }

        Season persistedSeason = seasonDao.find(seasonRef).orElseThrow(
                () -> new IllegalArgumentException(String.format("%s does not exist.", seasonRef)));
        User communityManager = userDao.findByNickname(managerNickname).orElseThrow(
                () -> new IllegalArgumentException(String.format("%s does not exist.", managerNickname)));

        Community community = new Community();
        community.setYear(communityYear);
        community.setName(communityName);
        community.setReference(communityRef);
        community.setCommunityManager(communityManager);

        community.setSeason(persistedSeason);
        communityDao.persist(community);

        return BetofficeServiceResult.sucess(community);
    }

    @Override
    @Transactional
    public void delete(CommunityReference reference) {
        Community community = communityDao.find(reference).orElseThrow();

        if (communityDao.hasMembers(reference)) {
            LOG.warn("Unable to delete community '{}'. The Community has members.", community);
            throw new IllegalArgumentException("Unable to delete community. The Community has members.");
        }

        communityDao.delete(community);
    }

    @Override
    @Transactional
    public Community addMember(CommunityReference communityReference, Nickname nickname) {
        Community community = communityDao.find(communityReference).orElseThrow();
        User user = userDao.findByNickname(nickname).orElseThrow();
        community.addMember(user);
        communityDao.update(community);
        return community;
    }

    @Override
    @Transactional
    public Community addMembers(CommunityReference communityReference, Set<Nickname> nicknames) {
        Community community = communityDao.find(communityReference).orElseThrow();
        nicknames.stream()
                .map(n -> userDao.findByNickname(n))
                .forEach(u -> u.ifPresent(us -> community.addMember(us)));
        communityDao.update(community);

        return community;
    }

    @Override
    @Transactional
    public Community removeMember(CommunityReference reference, Nickname nickname) {
        User user = userDao.findByNickname(nickname).orElseThrow();
        Community community = communityDao.find(reference).orElseThrow();
        community.removeMember(user);
        communityDao.update(community);
        return community;
    }

    @Override
    @Transactional
    public Community removeMembers(CommunityReference reference, Set<Nickname> nicknames) {
        nicknames.stream().forEach(nickname -> {
            removeMember(reference, nickname);
        });
        return communityDao.find(reference).orElseThrow();
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
    public void deleteUser(final Nickname nickname) {
        userDao.findByNickname(nickname).ifPresent(u -> userDao.delete(u));
    }

    @Override
    @Transactional
    public Optional<User> updateUser(
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
                sendUserProfileChangeMailNotification.send(u);
                u.incrementChangeSend();
            } else {
                u.setEmail(mail);
                u.abortEmailChange();
            }
            return u;
        });
    }

    private boolean hasUserChangedHisMailAddress(final User user, final String newMailAddress) {
        return !StringUtils.equals(user.getEmail(), newMailAddress);
    }

    @Override
    @Transactional
    public Optional<User> confirmMailAddressChange(final Nickname nickname, final String changeToken) {
        return userDao.findByNickname(nickname).map(u -> {
            if (StringUtils.equals(changeToken, u.getChangeToken())) {
                u.acceptEmailChange();
            } else {
                LOG.warn("Unable to confirm email change. ChangeTokens are different. {} vs {}", changeToken,
                        u.getChangeToken());
                throw new IllegalArgumentException("Unable to confirm email change. ChangeTokens are different.");
            }
            return u;
        });
    }

    @Override
    @Transactional
    public Optional<User> abortMailAddressChange(final Nickname nickname) {
        return userDao.findByNickname(nickname).map(u -> u.abortEmailChange());
    }

    @Override
    @Transactional
    public Optional<User> resubmitConfirmationMail(final Nickname nickname) {
        return userDao.findByNickname(nickname)
                .filter(u -> u.getChangeSend() < 5)
                .map(u -> sendUserProfileChangeMailNotification.send(u));
    }

}
