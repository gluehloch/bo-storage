/*
 * ============================================================================
 * Project betoffice-storage
<<<<<<< HEAD
 * Copyright (c) 2000-2022 by Andre Winkler. All rights reserved.
=======
 * Copyright (c) 2000-2021 by Andre Winkler. All rights reserved.
>>>>>>> work/paging
 * ============================================================================
 *          GNU GENERAL  LICENSE
 *  TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General  License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General  License for more details.
 *
 *   You should have received a copy of the GNU General  License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package de.winkler.betoffice.dao.hibernate;

import static de.winkler.betoffice.dao.hibernate.FilterBuilder.filter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.query.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import de.winkler.betoffice.dao.CommunityDao;
import de.winkler.betoffice.storage.Community;
import de.winkler.betoffice.storage.CommunityFilter;
import de.winkler.betoffice.storage.CommunityReference;
import de.winkler.betoffice.storage.User;

@Repository("communityDao")
public class CommunityDaoHibernate extends AbstractCommonDao<Community> implements CommunityDao {

	public CommunityDaoHibernate() {
		super(Community.class);
	}

	@Override
	public Optional<Community> find(CommunityReference reference) {
		Query<Community> query = getSessionFactory().getCurrentSession()
				.createQuery("from Community c left join fetch c.users where c.reference = :reference", Community.class)
				.setParameter("reference", reference);
		return singleResult(query);
	}

	@Override
	public List<Community> find(String name) {
		return getSessionFactory().getCurrentSession()
				.createQuery("from Community c left join fetch c.users where c.name = :name", Community.class)
				.setParameter("name", name)
				.getResultList();
	}
    
    @Override
    public Page<Community> findAll(CommunityFilter communityFilter, Pageable pageable) {
        long total = countAll();

        Optional<String> sqlsort = Optional.empty();
        if (pageable.getSort().isSorted()) {        
            sqlsort = Optional.of("ORDER BY " + pageable.getSort().stream().map(s -> s.getProperty() + s.getDirection().name()).collect(Collectors.joining(", ")));    
        }

        List<Community> communities = getSessionFactory().getCurrentSession()
                .createQuery(
                        "FROM "
                        + " Community c "
                        + "WHERE "
                        + filter("c", "shortName")
                        + " AND " + filter("c", "name")
                        + sqlsort.orElse(""),
                        Community.class)
                .setParameter("shortName", communityFilter.getShortName())
                .setParameter("name", communityFilter.getName())
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
               
        return new PageImpl<Community>(communities, pageable, total);
    }

    private long countAll() {
        return getSessionFactory().getCurrentSession()
                .createQuery("select count(*) from Community c", Long.class)
                .getSingleResult();        
    }

	@Override
	public boolean hasMembers(Community community) {
		List<User> members = getSessionFactory().getCurrentSession()
				.createQuery("from Community c left join fetch c.users where c.id = :id", User.class)
				.setParameter("id", community.getId())
				.getResultList();
		return members.size() > 0;
	}

}
