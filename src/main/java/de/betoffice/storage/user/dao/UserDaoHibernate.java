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

package de.betoffice.storage.user.dao;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.TypedQuery;

import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import de.betoffice.storage.hibernate.AbstractCommonDao;
import de.betoffice.storage.user.UserDao;
import de.betoffice.storage.user.entity.Nickname;
import de.betoffice.storage.user.entity.UserEntity;

/**
 * Klasse für den Zugriff auf <code>User</code> Objekte mit Hibernate.
 * 
 * @author Andre Winkler
 */
@Repository("userDao")
public class UserDaoHibernate extends AbstractCommonDao<UserEntity> implements UserDao {

    /** Sucht nach allen Usern mit einem bestimmten Nick-Namen. */
    private static final String QUERY_USER_BY_NICKNAME = """
            from
                UserEntity as user
            where
                user.nickname = :nickname
            """;

    // ------------------------------------------------------------------------

    public UserDaoHibernate() {
        super(UserEntity.class);
    }

    private long countAll() {
        return getEntityManager()
                .createQuery("select count(*) from CommunityEntity c", Long.class)
                .getSingleResult();
    }

    @Override
    public List<UserEntity> findAll() {
        return getEntityManager().createQuery("from UserEntity u", UserEntity.class).getResultList();
    }

    @Override
    public List<UserEntity> findLowerCaseNickname(String nickname) {
        return getEntityManager().createQuery("from UserEntity u where LOWER(u.nickname) = LOWER(:nickname)", UserEntity.class)
                .setParameter("nickname", nickname)
                .getResultList();
    }

    @Override
    public Page<UserEntity> findAll(String nicknameFilter, Pageable pageable) {
        long total = countAll();
        String filter = new StringBuilder("%").append(nicknameFilter).append("%").toString();

        List<UserEntity> users;
        if (pageable.isPaged()) {
            users = getEntityManager().unwrap(Session.class)
                    .createQuery(
                            "from UserEntity u where LOWER(u.nickname) like LOWER(:filter)",
                            UserEntity.class)
                    .setParameter("filter", filter)
                    .setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize())
                    .getResultList();
        } else {
            users = getEntityManager().unwrap(Session.class)
                    .createQuery(
                            "from UserEntity u where LOWER(u.nickname) like LOWER(:filter)",
                            UserEntity.class)
                    .setParameter("filter", filter)
                    .getResultList();
        }

        return new PageImpl<>(users, pageable, total);
    }

    @Override
    public Optional<UserEntity> findByNickname(final Nickname nickname) {
        TypedQuery<UserEntity> user = getEntityManager()
                .createQuery(QUERY_USER_BY_NICKNAME, UserEntity.class)
                .setParameter("nickname", nickname);
        return singleResult(user);
    }

    @Override
    public Optional<UserEntity> findByChangeToken(String changeToken) {
        TypedQuery<UserEntity> user = getEntityManager()
                .createQuery("SELECT u FROM UserEntity u WHERE u.changeToken = :changeToken", UserEntity.class)
                .setParameter("changeToken", changeToken);
        return singleResult(user);
    }

}
