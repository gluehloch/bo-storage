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

package de.winkler.betoffice.dao.hibernate;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.winkler.betoffice.dao.UserDao;
import de.winkler.betoffice.storage.User;

/**
 * Test for class {@link UserDaoHibernate}.
 *
 * @author by Andre Winkler
 */
public class UserDaoHibernateTest extends AbstractDaoTestSupport {

    @Autowired
    private UserDao userDaoHibernate;

    @BeforeEach
    public void init() {
        prepareDatabase(UserDaoHibernateTest.class);
    }

    @Test
    public void testUserDaoHibernateFindAll() {
        List<User> users = userDaoHibernate.findAll();
        assertThat(users).hasSize(3);
        assertThat(users.get(0).getNickname()).isEqualTo("Frosch");
        assertThat(users.get(1).getNickname()).isEqualTo("Hattwig");
        assertThat(users.get(2).getNickname()).isEqualTo("Peter");
    }

    @Test
    public void testUserDaoHibernateFindByNickname() {
        Optional<User> user = userDaoHibernate.findByNickname("Frosch");
        assertThat(user.get().getSurname()).isEqualTo("Adam");

        user = userDaoHibernate.findByNickname("fehler");
        assertThat(user).isNotPresent();
    }

}
