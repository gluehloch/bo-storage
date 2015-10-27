/*
 * $Id: DefaultAuthService.java 3782 2013-07-27 08:44:32Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2013 by Andre Winkler. All rights reserved.
 * ============================================================================
 *          GNU GENERAL PUBLIC LICENSE
 *  TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package de.winkler.betoffice.service;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.winkler.betoffice.dao.UserDao;
import de.winkler.betoffice.storage.User;

/**
 * Implementation of {@link AuthService}.
 *
 * @author by Andre Winkler
 */
@Service("authService")
public class DefaultAuthService implements AuthService {

    @Autowired
    private UserDao userDao;

    @Override
    public SecurityToken login(String name, String password) {
        User user = userDao.findByNickname(name);
        DateTime now = DateTime.now();

        if (user == null) {

            return new SecurityToken("securityToken", false, null);

        } else {

            boolean passwordCheckIsOk = user.comparePwd(password);
            return new SecurityToken("securityToken", passwordCheckIsOk, user);

        }
    }

}
