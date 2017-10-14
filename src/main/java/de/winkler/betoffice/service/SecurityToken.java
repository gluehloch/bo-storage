/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2015 by Andre Winkler. All
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
 *
 */

package de.winkler.betoffice.service;

import org.joda.time.DateTime;

import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.enums.RoleType;

/**
 * I don´t know. But something to hold session data.
 *
 * @author by Andre Winkler
 */
public class SecurityToken {

    private final String token;
    private final User user;
    private final RoleType roleType;
    private final DateTime loginTime;

    /**
     * Constructor
     *
     * @param _token
     *            The session id
     * @param _user
     *            the logged in user
     * @param _roleType
     *            Role of the user
     * @param _loginTime
     *            Zeitpunkt des einloggens.
     */
    public SecurityToken(String _token, User _user, RoleType _roleType,
            DateTime _loginTime) {
        token = _token;
        user = _user;
        roleType = _roleType;
        loginTime = _loginTime;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public DateTime getLoginTime() {
        return loginTime;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    @Override
    public String toString() {
        return "SecurityToken [token=" + token + ", user=" + user
                + ", roleType=" + roleType + ", loginTime=" + loginTime + "]";
    }

}
