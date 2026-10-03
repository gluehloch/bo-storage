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

package de.betoffice.service.resolver;

import java.util.Optional;

import org.springframework.stereotype.Component;

import de.betoffice.storage.user.UserDao;
import de.betoffice.storage.user.entity.Nickname;
import de.betoffice.storage.user.entity.UserEntity;
import de.betoffice.validation.ValidationMessage.MessageType;
import de.betoffice.validation.ValidationMessages.ValidationMessagesBuilder;

@Component
public class UserResolver {

    private final UserDao userDao;

    public UserResolver(UserDao userDao) {
        this.userDao = userDao;
    }

    public Optional<UserEntity> resolveUser(ValidationMessagesBuilder vmb, Nickname nickname) {
        Optional<UserEntity> optionalUser = userDao.findByNickname(nickname);
        if (optionalUser.isEmpty()) {
            vmb.addFormattedMessage(MessageType.USER_NOT_FOUND, nickname);
        }
        return optionalUser;
    }

}
