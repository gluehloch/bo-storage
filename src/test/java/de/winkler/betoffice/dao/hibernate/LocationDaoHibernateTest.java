/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2014 by Andre Winkler. All
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

package de.winkler.betoffice.dao.hibernate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.winkler.betoffice.dao.LocationDao;
import de.winkler.betoffice.storage.Location;

/**
 * Test for {@link LocationDaoHibernate}.
 *
 * @author by Andre Winkler
 */
public class LocationDaoHibernateTest extends AbstractDaoTestSupport {

    @Autowired
    private LocationDao locationDao;

    @Before
    public void init() {
        prepareDatabase(LocationDaoHibernateTest.class);
    }

    @After
    public void shutdown() {
        deleteDatabase();
    }

    @Test
    public void testDataSource() {
        assertThat(getDataSource(), notNullValue());
        assertThat(getSessionFactory(), notNullValue());
        assertThat(locationDao, notNullValue());
    }

    @Test
    public void testLocationFinder() throws Exception {
        List<Location> location = locationDao.findAll();
        assertThat(location.size(), equalTo(3));

        Location rwe = locationDao.findById(1);
        assertThat(rwe.getName(), equalTo("Stadion Essen"));
        assertThat(rwe.getCity(), equalTo("Essen"));
        assertThat(rwe.getGeodat(), equalTo("10.10.10.10"));
        assertThat(rwe.getOpenligaid(), equalTo(1L));

        Location bochum = locationDao.findById(2);
        assertThat(bochum.getName(), equalTo("Ruhrstadion"));
        assertThat(bochum.getCity(), equalTo("Bochum"));
        assertThat(bochum.getGeodat(), equalTo("20.20.20.20"));
        assertThat(bochum.getOpenligaid(), equalTo(2L));

        Location dortmund = locationDao.findById(3);
        assertThat(dortmund.getName(), equalTo("Westfalenstadion"));
        assertThat(dortmund.getCity(), equalTo("Dortmund"));
        assertThat(dortmund.getGeodat(), equalTo("30.30.30.30"));
        assertThat(dortmund.getOpenligaid(), equalTo(3L));

        //
        // Team frankreich = teamDao.findById(4);
        // assertThat(frankreich.getName(), equalTo("Frankreich"));
        // assertThat(frankreich.getTeamType(), equalTo(TeamType.FIFA));
        //
        // final ResourceWriter rw = new ResourceWriter(File.createTempFile(
        // "team", "dat"));
        // getSessionFactory().getCurrentSession().doWork(new Work() {
        // @Override
        // public void execute(Connection connection) throws SQLException {
        // rw.start(connection, "select * from bo_team", false);
        // }
        // });
    }

}
