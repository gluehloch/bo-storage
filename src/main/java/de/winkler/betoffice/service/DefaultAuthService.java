/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2015 by Andre Winkler. All rights reserved.
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
import de.winkler.betoffice.service.SecurityToken.Role;
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

        SecurityToken securityToken = null;
        if (user != null && user.comparePwd(password)) {
            // TODO Die Rolle muss bestimmt werden
            securityToken = new SecurityToken("securityToken", user, Role.TIPPER, now);
        }
        
        return securityToken;
    }

}
