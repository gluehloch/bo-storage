/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2016 by Andre Winkler. All rights reserved.
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

package de.betoffice.storage.session;

import java.util.List;

import de.betoffice.storage.community.CommonDao;
import de.betoffice.storage.session.entity.Session;

/**
 * DAO for table BO_SESSION.
 *
 * @author by Andre Winkler
 */
public interface SessionDao extends CommonDao<Session> {

    /**
     * Returns all logins for user with name 'nickname'.
     * 
     * @param nickname
     *            the nickname of the user
     * @return a list of login sessions
     */
    public List<Session> findByNickname(String nickname);

    /**
     * Returns the session id of an authorized user.
     * 
     * @param sessionId
     *            session id
     * @return The session of the authorized user
     */
    public List<Session> findBySessionId(String sessionId);

}
