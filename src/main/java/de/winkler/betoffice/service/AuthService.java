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
 */

package de.winkler.betoffice.service;

import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.enums.RoleType;

/**
 * Authentication and authorization service.
 *
 * @author by Andre Winkler
 */
public interface AuthService {

    /**
     * Login to betoffice. On success you get a {@link SecurityToken}.
     *
     * @param name
     *            user name
     * @param password
     *            user password
     * @param sessionId
     *            SessionId
     * @param address
     *            ip address
     * @param browserId
     *            browser id
     * @return a security token
     */
    SecurityToken login(String name, String password, String sessionId,
            String address, String browserId);

    /**
     * Logout of betoffice.
     * 
     * @param securityToken
     *            a security token
     */
    void logout(SecurityToken securityToken);

    /**
     * Vaidates the user session.
     * 
     * @param token
     *            a security token
     * @param season
     *            the championship
     * @return <code>true</code>, if it is a valid user session
     */
    boolean validateSession(SecurityToken token);

    /**
     * Find the role for a user session.
     * 
     * @param token
     *            a security token
     * @return the role
     */
    RoleType findRole(SecurityToken token);

    /**
     * Find the role for a user session.
     * 
     * @param token
     *            a security token
     * @param season
     *            a championship
     * @return the role for the specific championship
     */
    RoleType findRole(SecurityToken token, Season season);

}
