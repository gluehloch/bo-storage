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

import java.util.List;

import org.springframework.stereotype.Repository;

import de.winkler.betoffice.dao.LocationDao;
import de.winkler.betoffice.storage.Location;

/**
 * {@link Location} DAO implementation
 *
 * @author by Andre Winkler
 */
@Repository("locationDao")
public class LocationDaoHibernate extends AbstractCommonDao<Location> implements
        LocationDao {

    public LocationDaoHibernate() {
        super(Location.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Location> findAll() {
        return getSessionFactory()
                .getCurrentSession()
                .createQuery("from Location as location order by location.name")
                .list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Location findByOpenligaid(long openligaid) {
        List<Location> locations = getSessionFactory()
                .getCurrentSession()
                .createQuery(
                        "from Location as location where location.openligaid = :openligaid")
                .setParameter("openligaid", openligaid).list();
        return first(locations);
    }

}
