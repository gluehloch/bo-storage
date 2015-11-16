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
     * @return a security token
     */
    public SecurityToken login(String name, String password);

    /**
     * Logout of betoffice.
     * 
     * @param securityToken
     *            a security token
     */
    public void logout(SecurityToken securityToken);

}
