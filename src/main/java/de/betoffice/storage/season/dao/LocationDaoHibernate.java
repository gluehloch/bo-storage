/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2024 by Andre Winkler. All
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

package de.betoffice.storage.season.dao;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import de.betoffice.storage.hibernate.AbstractCommonDao;
import de.betoffice.storage.season.LocationDao;
import de.betoffice.storage.season.entity.LocationEntity;

/**
 * {@link LocationEntity} DAO implementation
 *
 * @author by Andre Winkler
 */
@Repository("locationDao")
public class LocationDaoHibernate extends AbstractCommonDao<LocationEntity>
        implements LocationDao {

    public LocationDaoHibernate() {
        super(LocationEntity.class);
    }

    @Override
    public List<LocationEntity> findAll() {
        return getEntityManager()
                .createQuery("from LocationEntity as location order by location.name",
                        LocationEntity.class)
                .getResultList();
    }

    @Override
    public Optional<LocationEntity> findByOpenligaid(long openligaid) {
        TypedQuery<LocationEntity> query = getEntityManager()
                .createQuery(
                        "from LocationEntity as location where location.openligaid = :openligaid",
                        LocationEntity.class)
                .setParameter("openligaid", openligaid);
        return singleResult(query);
    }

}
