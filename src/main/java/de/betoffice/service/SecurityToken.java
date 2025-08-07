/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2021 by Andre Winkler. All
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

package de.betoffice.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

import de.betoffice.storage.User;
import de.betoffice.storage.enums.RoleType;

/**
 * I don´t know. But something to hold session data.
 *
 * @author by Andre Winkler
 */
public class SecurityToken {

	private final String token;
	private final User user;
	private final List<RoleType> roleTypes;
	private final ZonedDateTime loginTime;

	/**
	 * Constructor
	 *
	 * @param _token     The session id
	 * @param _user      the logged in user
	 * @param _roleTypes Role of the user
	 * @param _loginTime Zeitpunkt des einloggens.
	 */
	public SecurityToken(String _token, User _user, List<RoleType> _roleTypes, ZonedDateTime _loginTime) {
		Objects.nonNull(_token);
		Objects.nonNull(_user);
		Objects.nonNull(_roleTypes);
		Objects.nonNull(_loginTime);

		token = _token;
		user = _user;
		roleTypes = _roleTypes;
		loginTime = _loginTime;
	}

	public String getToken() {
		return token;
	}

	public User getUser() {
		return user;
	}

	public ZonedDateTime getLoginTime() {
		return loginTime;
	}

	public List<RoleType> getRoleTypes() {
		return roleTypes;
	}

	public boolean hasRole(RoleType roleType) {
		return roleTypes.contains(roleType);
	}

	@Override
	public String toString() {
		return "SecurityToken [token=" + token + ", user=" + user + ", roleTypes=" + roleTypes + ", loginTime="
				+ loginTime + "]";
	}

}
