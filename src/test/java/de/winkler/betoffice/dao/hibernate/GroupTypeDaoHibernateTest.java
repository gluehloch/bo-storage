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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.winkler.betoffice.dao.GroupTypeDao;
import de.winkler.betoffice.dao.SeasonDao;
import de.winkler.betoffice.storage.GroupType;
import de.winkler.betoffice.storage.Season;

/**
 * Test case for class {@link GroupTypeDaoHibernate}.
 *
 * @author Andre Winkler
 */
public class GroupTypeDaoHibernateTest extends AbstractDaoTestSupport {

    @Autowired
    private GroupTypeDao groupTypeDao;

    @Autowired
    private SeasonDao seasonDao;

    @BeforeEach
    public void init() {
        prepareDatabase(GroupTypeDaoHibernateTest.class);
    }

    @Test
    public void testGroupTypeDaoHibernateFindAll() {
        List<GroupType> groupTypes = groupTypeDao.findAll();
        assertEquals(3, groupTypes.size());
        assertEquals("1. Liga", groupTypes.get(0).getName());
        assertEquals("2. Liga", groupTypes.get(1).getName());
        assertEquals("3. Liga", groupTypes.get(2).getName());
    }

    @Test
    public void testGroupTypeDaoHibernateFindBySeason() {
        Optional<Season> season = seasonDao.findByName("4711", "1000");
        List<GroupType> findBySeason = groupTypeDao.findBySeason(season.get());
        assertEquals("1. Liga", findBySeason.get(0).getName());
        assertEquals("2. Liga", findBySeason.get(1).getName());
        assertEquals(2, findBySeason.size());

        season = seasonDao.findByName("4712", "1001");
        findBySeason = groupTypeDao.findBySeason(season.get());
        assertEquals("3. Liga", findBySeason.get(0).getName());
        assertEquals(1, findBySeason.size());
    }

}
