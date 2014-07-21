/*
 * $Id: SecurityToken.java 3812 2013-09-08 06:45:18Z andrewinkler $
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

import de.winkler.betoffice.storage.User;

/**
 * I don´t know. But something to hold session data.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3812 $
 *          $LastChangedDate: 2013-09-08 08:45:18 +0200 (Sun, 08 Sep 2013) $
 */
public class SecurityToken {

    private final String token;
    
    private final boolean loggedIn;
    
    private final User user;

    /**
     * Constructor
     *
     * @param _token The session id
     * @param _loggedIn <code>true</code> or <code>false</code>
     * @param _user the logged in user
     */
    public SecurityToken(String _token, boolean _loggedIn, User _user) {
        token = _token;
        loggedIn = _loggedIn;
        user = _user;
    }

    public String getToken() {
        return token;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public boolean isNotLoggedIn() {
        return !loggedIn;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "SecurityToken [token=" + token + ", loggedIn=" + loggedIn
                + ", user=" + user + "]";
    }
    
}
