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

package de.betoffice.storage.season;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.betoffice.dao.hibernate.AbstractDaoTestSupport;
import de.betoffice.storage.season.dao.LocationDaoHibernate;
import de.betoffice.storage.season.entity.Location;

/**
 * Test for {@link LocationDaoHibernate}.
 *
 * @author by Andre Winkler
 */
public class LocationDaoHibernateTest extends AbstractDaoTestSupport {

    @Autowired
    private LocationDao locationDao;

    @BeforeEach
    public void init() {
        prepareDatabase(LocationDaoHibernateTest.class);
    }

    @Test
    public void testDataSource() {
        assertThat(getDataSource()).isNotNull();
        assertThat(getEntityManager()).isNotNull();
        assertThat(locationDao).isNotNull();
    }

    @Test
    public void testLocationFinderByOpenligaid() {
        Optional<Location> essen = locationDao.findByOpenligaid(1L);
        assertThat(essen.get().getName()).isEqualTo("Stadion Essen");
        assertThat(essen.get().getCity()).isEqualTo("Essen");
        assertThat(essen.get().getGeodat()).isEqualTo("10.10.10.10");

        Optional<Location> bochum = locationDao.findByOpenligaid(2);
        assertThat(bochum.get().getName()).isEqualTo("Ruhrstadion");
        assertThat(bochum.get().getCity()).isEqualTo("Bochum");
        assertThat(bochum.get().getGeodat()).isEqualTo("20.20.20.20");
        assertThat(bochum.get().getOpenligaid()).isEqualTo(2L);
    }

    @Test
    public void testLocationFinder() {
        List<Location> location = locationDao.findAll();
        assertThat(location).hasSize(3);

        Location rwe = locationDao.findById(1);
        assertThat(rwe.getName()).isEqualTo("Stadion Essen");
        assertThat(rwe.getCity()).isEqualTo("Essen");
        assertThat(rwe.getGeodat()).isEqualTo("10.10.10.10");
        assertThat(rwe.getOpenligaid()).isEqualTo(1L);

        Location bochum = locationDao.findById(2);
        assertThat(bochum.getName()).isEqualTo("Ruhrstadion");
        assertThat(bochum.getCity()).isEqualTo("Bochum");
        assertThat(bochum.getGeodat()).isEqualTo("20.20.20.20");
        assertThat(bochum.getOpenligaid()).isEqualTo(2L);

        Location dortmund = locationDao.findById(3);
        assertThat(dortmund.getName()).isEqualTo("Westfalenstadion");
        assertThat(dortmund.getCity()).isEqualTo("Dortmund");
        assertThat(dortmund.getGeodat()).isEqualTo("30.30.30.30");
        assertThat(dortmund.getOpenligaid()).isEqualTo(3L);
    }

}
