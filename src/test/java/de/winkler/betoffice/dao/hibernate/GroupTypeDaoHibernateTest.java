/*
 * $Id: GroupTypeDaoHibernateTest.java 3782 2013-07-27 08:44:32Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2012 by Andre Winkler. All rights reserved.
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

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.winkler.betoffice.storage.GroupType;
import de.winkler.betoffice.storage.Season;

/**
 * Test case for class {@link GroupTypeDaoHibernate}.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3782 $ $LastChangedDate: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
public class GroupTypeDaoHibernateTest extends HibernateDaoTestSupport {

    private GroupTypeDaoHibernate groupTypeDaoHibernate;

    private SeasonDaoHibernate seasonDaoHibernate;

    @Test
    public void testGroupTypeDaoHibernateFindAll() {
        List<GroupType> groupTypes = groupTypeDaoHibernate.findAll();
        assertEquals(3, groupTypes.size());
        assertEquals("1. Liga", groupTypes.get(0).getName());
        assertEquals("2. Liga", groupTypes.get(1).getName());
        assertEquals("3. Liga", groupTypes.get(2).getName());
    }

    @Test
    public void testGroupTypeDaoHibernateFindBySeason() {
        Season season = seasonDaoHibernate.findByName("4711", "1000");
        List<GroupType> findBySeason = groupTypeDaoHibernate.findBySeason(season);
        assertEquals("1. Liga", findBySeason.get(0).getName());
        assertEquals("2. Liga", findBySeason.get(1).getName());
        assertEquals(2, findBySeason.size());
        
        season = seasonDaoHibernate.findByName("4712", "1001");
        findBySeason = groupTypeDaoHibernate.findBySeason(season);
        assertEquals("3. Liga", findBySeason.get(0).getName());
        assertEquals(1, findBySeason.size());
    }

    @Before
    public void setup() {
        groupTypeDaoHibernate = new GroupTypeDaoHibernate();
        groupTypeDaoHibernate.setSessionFactory(sessionFactory);
        
        seasonDaoHibernate = new SeasonDaoHibernate();
        seasonDaoHibernate.setSessionFactory(sessionFactory);
    }

}
