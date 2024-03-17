/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2014 by Andre Winkler. All rights reserved.
 * ============================================================================
 *          GNU GENERAL PUBLIC LICENSE
 *  TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package de.winkler.betoffice.dao.hibernate;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.winkler.betoffice.dao.PlayerDao;
import de.winkler.betoffice.storage.Player;

/**
 * Test for {@link PlayerDaoHibernate}.
 *
 * @author by Andre Winkler
 */
public class PlayerDaoHibernateTest extends AbstractDaoTestSupport {

    @Autowired
    private PlayerDao playerDao;

    @BeforeEach
    public void init() {
        prepareDatabase(PlayerDaoHibernateTest.class);
    }

    @Test
    public void testDataSource() {
        assertThat(getDataSource()).isNotNull();
        assertThat(getEntityManager()).isNotNull();
        assertThat(playerDao).isNotNull();
    }

    @Test
    public void testPlayerFinder() throws Exception {
        List<Player> player = playerDao.findAll();
        assertThat(player.size()).isEqualTo(4);

        Player mill = playerDao.findById(1);
        assertThat(mill.getName()).isEqualTo("Mill");
        assertThat(mill.getVorname()).isEqualTo("Frank");
        assertThat(mill.getOpenligaid()).isEqualTo(1L);

        Player lippens = playerDao.findById(2);
        assertThat(lippens.getName()).isEqualTo("Lippens");
        assertThat(lippens.getVorname()).isEqualTo("Ente");
        assertThat(lippens.getOpenligaid()).isEqualTo(2L);

        Player winkler = playerDao.findById(3);
        assertThat(winkler.getName()).isEqualTo("Winkler");
        assertThat(winkler.getVorname()).isEqualTo("Erwin");
        assertThat(winkler.getOpenligaid()).isEqualTo(3L);

        Player koen = playerDao.findById(4);
        assertThat(koen.getName()).isEqualTo("Koen");
        assertThat(koen.getVorname()).isEqualTo("Erwin");
        assertThat(koen.getOpenligaid()).isEqualTo(4L);
    }

}
