/*
 * $Id: SpringBeanTest.java 3850 2013-11-30 18:24:08Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2010 by Andre Winkler. All rights reserved.
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

package de.winkler.betoffice.service;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.betoffice.database.data.MySqlDatabasedTestSupport.DataLoader;
import de.winkler.betoffice.service.SeasonManagerService;
import de.winkler.betoffice.storage.Season;

/**
 * Test for Spring´s JUnit and annotation support.
 * 
 * @author $Author: andrewinkler $
 * @version $Revision: 3850 $ $Date: 2013-11-30 19:24:08 +0100 (Sa, 30 Nov 2013) $
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/betoffice-datasource.xml",
		"/betoffice-persistence.xml", "/test-mysql-piratestest.xml" })
public class SpringBeanTest {

    @Autowired
    protected DataSource dataSource;

	@Autowired
	private SeasonManagerService seasonManagerService;

    private DatabaseSetUpAndTearDown dsuatd;

    @Before
    public void setUp() throws Exception {
        dsuatd = new DatabaseSetUpAndTearDown(dataSource);
        dsuatd.setUp(DataLoader.FULL);
    }

    @After
    public void tearDown() throws SQLException {
        dsuatd.tearDown();
    }

	@Test
	public void testSwingBetofficeStore() {
		List<Season> championships = seasonManagerService.findAllSeasons();
		assertEquals(22, championships.size());
	}

}
