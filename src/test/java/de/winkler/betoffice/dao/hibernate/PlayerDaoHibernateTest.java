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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
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

    @Before
    public void init() {
        prepareDatabase(PlayerDaoHibernateTest.class);
    }

    @Test
    public void testDataSource() {
        assertThat(getDataSource(), notNullValue());
        assertThat(getSessionFactory(), notNullValue());
        assertThat(playerDao, notNullValue());
    }

    @Test
    public void testPlayerFinder() throws Exception {
        List<Player> player = playerDao.findAll();
        assertThat(player.size(), equalTo(4));

        Player mill = playerDao.findById(1);
        assertThat(mill.getName(), equalTo("Mill"));
        assertThat(mill.getVorname(), equalTo("Frank"));
        assertThat(mill.getOpenligaid(), equalTo(1L));

        Player lippens = playerDao.findById(2);
        assertThat(lippens.getName(), equalTo("Lippens"));
        assertThat(lippens.getVorname(), equalTo("Ente"));
        assertThat(lippens.getOpenligaid(), equalTo(2L));

        Player winkler = playerDao.findById(3);
        assertThat(winkler.getName(), equalTo("Winkler"));
        assertThat(winkler.getVorname(), equalTo("Erwin"));
        assertThat(winkler.getOpenligaid(), equalTo(3L));

        Player koen = playerDao.findById(4);
        assertThat(koen.getName(), equalTo("Koen"));
        assertThat(koen.getVorname(), equalTo("Erwin"));
        assertThat(koen.getOpenligaid(), equalTo(4L));
    }

}
