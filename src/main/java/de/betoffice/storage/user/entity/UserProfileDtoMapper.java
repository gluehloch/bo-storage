/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2013-2026 by Andre Winkler. All
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

package de.betoffice.storage.user.entity;

import java.util.List;
import java.util.Optional;

import de.betoffice.mail.NotificationType;
import de.betoffice.storage.user.PartyDto;

/**
 * Map {@link User} to {@link PartyDto}.
 * 
 * @author Andre Winkler
 */
public class UserProfileDtoMapper {

    public static Optional<UserProfileDto> map(Optional<UserEntity> user) {
        return user.map(UserProfileDtoMapper::map);
    }

    public static UserProfileDto map(UserEntity user) {
        return map(user, new UserProfileDto());
    }

    public static UserProfileDto map(UserEntity user, UserProfileDto partyJson) {
        partyJson.setName(user.getName());
        partyJson.setSurname(user.getSurname());
        partyJson.setMail(user.getEmail());
        partyJson.setEmailNotificationEnabled(NotificationType.TIPP.equals(user.getNotification()));
        partyJson.setAlternativeMail(user.getChangeEmail());
        partyJson.setNickname(user.getNickname().value());
        partyJson.setPhone(user.getPhone());
        return partyJson;
    }

    public static List<UserProfileDto> map(List<UserEntity> users) {
        return users.stream().map(UserProfileDtoMapper::map).toList();
    }

    public static UserEntity reverse(UserProfileDto partyJson, UserEntity user) {
        user.setName(partyJson.getName());
        user.setSurname(partyJson.getSurname());
        user.setEmail(partyJson.getMail());
        user.setNotification(partyJson.isEmailNotificationEnabled() ? NotificationType.TIPP : NotificationType.NONE);
        user.setNickname(Nickname.of(partyJson.getNickname()));
        user.setPhone(partyJson.getPhone());
        return user;
    }

}
