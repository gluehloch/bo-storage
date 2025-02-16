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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import de.winkler.betoffice.dao.UserDao;
import de.winkler.betoffice.storage.Nickname;
import de.winkler.betoffice.storage.User;

/**
 * Test for class {@link UserDaoHibernate}.
 *
 * @author by Andre Winkler
 */
class UserDaoHibernateTest extends AbstractDaoTestSupport {

    @Autowired
    private UserDao userDaoHibernate;

    @BeforeEach
    public void init() {
        prepareDatabase(UserDaoHibernateTest.class);
    }

    @Test
    void userDaoHibernateFindAll() {
        Page<User> users = userDaoHibernate.findAll("", Pageable.unpaged());
        assertThat(users).hasSize(3);
        assertThat(users.getContent().get(0).getNickname().value()).isEqualTo("Frosch");
        assertThat(users.getContent().get(1).getNickname().value()).isEqualTo("Hattwig");
        assertThat(users.getContent().get(2).getNickname().value()).isEqualTo("Peter");
    }

    @Test
    void userDaoHibernateFindByNickname() {
        Optional<User> user = userDaoHibernate.findByNickname(Nickname.of("Frosch"));
        assertThat(user.get().getSurname()).isEqualTo("Adam");

        user = userDaoHibernate.findByNickname(Nickname.of("fehler"));
        assertThat(user).isNotPresent();
    }

    @Test
    void userFindAll() {
        Page<User> users = userDaoHibernate.findAll("Frosch", PageRequest.of(0, 5));
        assertThat(users.getContent()).hasSize(1);
        assertThat(users.getPageable().getOffset()).isZero();
        assertThat(users.getPageable().getPageSize()).isEqualTo(5);
        assertThat(users.getContent().get(0).getNickname()).isEqualTo(Nickname.of("Frosch"));
    }

    @Test
    void userFindByChangeToken() {
        User user = userDaoHibernate.findByNickname(Nickname.of("Frosch")).orElseThrow();
        user.setChangeToken("testToken");
        userDaoHibernate.persist(user);
        Optional<User> u2 = userDaoHibernate.findByChangeToken("testToken");
        assertThat(u2).isPresent().contains(user);
    }

}
