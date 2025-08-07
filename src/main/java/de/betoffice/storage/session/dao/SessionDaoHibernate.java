/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2024 by Andre Winkler. All
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

package de.betoffice.storage.session.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import de.betoffice.dao.hibernate.AbstractCommonDao;
import de.betoffice.storage.Session;
import de.betoffice.storage.session.SessionDao;

/**
 * DAO for BO_SESSION
 *
 * @author by Andre Winkler
 */
@Repository("sessionDao")
public class SessionDaoHibernate extends AbstractCommonDao<Session>
        implements SessionDao {

    SessionDaoHibernate() {
        super(Session.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.betoffice.dao.SessionDao#findByNickname(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Session> findByNickname(String nickname) {
        return getEntityManager()
                .createQuery(
                        "from Session as session where session.nickname = :nickname order by session.login desc")
                .setParameter("nickname", nickname).getResultList();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.betoffice.dao.SessionDao#findBySessionId(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Session> findBySessionId(String sessionId) {
        return getEntityManager()
                .createQuery("from Session as session "
                        + "left join fetch session.user "
                        + "where session.token = :sessionId order by session.login desc")
                .setParameter("sessionId", sessionId).getResultList();
    }

}
