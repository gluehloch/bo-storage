/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2016 by Andre Winkler. All
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

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.winkler.betoffice.dao.SessionDao;
import de.winkler.betoffice.dao.UserDao;
import de.winkler.betoffice.storage.Session;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.enums.RoleType;
import de.winkler.betoffice.util.LoggerFactory;

/**
 * Implementation of {@link AuthService}.
 *
 * @author by Andre Winkler
 */
@Service("authService")
public class DefaultAuthService implements AuthService {

    private final Logger log = LoggerFactory.make();

    @Autowired
    private UserDao userDao;

    @Autowired
    private SessionDao sessionDao;

    @Autowired
    private DateTimeProvider dateTimeProvider;

    @Transactional
    @Override
    public SecurityToken login(String name, String password, String sessionId, String address, String browserId) {
        Optional<User> user = userDao.findByNickname(name);

        SecurityToken securityToken = null;
        if (user.isPresent() && user.get().comparePwd(password)) {
            User presentUser = user.get();
            RoleType roleType = presentUser.isAdmin() ? RoleType.ADMIN : RoleType.TIPPER;
            
            var now = dateTimeProvider.currentDateTime();
            securityToken = new SecurityToken(sessionId, presentUser, roleType, now);

            Session session = new Session();
            session.setBrowser(browserId);
            session.setFailedLogins(0);
            session.setLogin(now);
            session.setLogout(null);
            session.setNickname(name);
            session.setRemoteAddress(address);
            session.setToken(securityToken.getToken());
            session.setUser(user.get());

            sessionDao.save(session);
        }

        return securityToken;
    }

    @Transactional
    @Override
    public void logout(SecurityToken securityToken) {
        List<Session> sessions = sessionDao.findBySessionId(securityToken.getToken());

        if (sessions.isEmpty()) {
            log.warn("Trying to logout with an invalid securityToken=[{}]", securityToken);
        } else {
            for (Session session : sessions) {
                session.setLogout(dateTimeProvider.currentDateTime());
                sessionDao.save(session);
            }
        }
    }

    @Override
    public Optional<Session> validateSession(String token) {
        List<Session> sessions = sessionDao.findBySessionId(token);

        if (sessions.isEmpty()) {
            log.warn(
                    "Trying to validate the session with an invalid securityToken=[{}]",
                    token);
            return Optional.empty();
        } else {
            Session session = sessions.get(0);
            session.getUser().getNickname();
            // TODO Hier koennte man noch mehr pruefen, wie z.B. Browser und IP?
            // Dann waeren mehr Parameter an diese Methode zu uebergeben.
            // Vielleicht doch das ganze SecurityToken?
            return Optional.of(session);
        }
    }

	@Override
	public Optional<User> findByNickname(String nickname) {
		return userDao.findByNickname(nickname);
	}

}
