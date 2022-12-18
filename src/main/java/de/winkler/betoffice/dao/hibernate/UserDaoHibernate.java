/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2022 by Andre Winkler. All
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

package de.winkler.betoffice.dao.hibernate;

import java.util.List;
import java.util.Optional;

import org.hibernate.query.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import de.winkler.betoffice.dao.UserDao;
import de.winkler.betoffice.storage.Nickname;
import de.winkler.betoffice.storage.User;

/**
 * Klasse für den Zugriff auf <code>User</code> Objekte mit Hibernate.
 * 
 * @author Andre Winkler
 */
@Repository("userDao")
public class UserDaoHibernate extends AbstractCommonDao<User> implements UserDao {

    /** Sucht nach allen Usern mit einem bestimmten Nick-Namen. */
    private static final String QUERY_USER_BY_NICKNAME = "from "
            + User.class.getName() + " as user " + "where "
            + "user.nickname = :nickname";

    // ------------------------------------------------------------------------

    public UserDaoHibernate() {
        super(User.class);
    }

    private long countAll() {
        return getSessionFactory().getCurrentSession()
                .createQuery("select count(*) from Community c", Long.class)
                .getSingleResult();
    }

    public List<User> findAll() {
        return getSessionFactory().getCurrentSession().createQuery("from User u", User.class).getResultList();
    }

    public Page<User> findAll(String nicknameFilter, Pageable pageable) {
        long total = countAll();
        String filter = new StringBuilder("%").append(nicknameFilter).append("%").toString();

        List<User> users;
        if (pageable.isPaged()) {
            users = getSessionFactory().getCurrentSession()
                    .createQuery(
                            "from User u where LOWER(u.nickname) like LOWER(:filter)",
                            User.class)
                    .setParameter("filter", filter)
                    .setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize())
                    .getResultList();
        } else {
            users = getSessionFactory().getCurrentSession()
                    .createQuery(
                            "from User u where LOWER(u.nickname) like LOWER(:filter)",
                            User.class)
                    .setParameter("filter", filter)
                    .getResultList();            
        }
        
        return new PageImpl<>(users, pageable, total);
    }

    @Override
    public Optional<User> findByNickname(final Nickname nickname) {
        Query<User> user = getSessionFactory().getCurrentSession()
                .createQuery(QUERY_USER_BY_NICKNAME, User.class)
                .setParameter("nickname", nickname);
        return singleResult(user);
    }

}
