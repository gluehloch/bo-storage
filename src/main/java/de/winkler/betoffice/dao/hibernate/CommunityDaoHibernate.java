/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2022 by Andre Winkler. All rights reserved.
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

import java.util.List;

import org.springframework.stereotype.Repository;

import de.winkler.betoffice.dao.CommunityDao;
import de.winkler.betoffice.storage.Community;
import de.winkler.betoffice.storage.User;

@Repository("communityDao")
public class CommunityDaoHibernate extends AbstractCommonDao<Community> implements CommunityDao {

	public CommunityDaoHibernate() {
		super(Community.class);
	}

	@Override
	public List<Community> findAll(String nameFilter) {
		String filter = new StringBuilder("%").append(nameFilter).append("%").toString();
		return getSessionFactory().getCurrentSession()
				.createQuery("from Community c where LOWER(c.name) like LOWER(:filter)", Community.class)
				.setParameter("filter", filter).getResultList();
	}

	@Override
	public Community findByShortName(String shortName) {
		return getSessionFactory().getCurrentSession()
				.createQuery("from Community c where c.shortName = :shortName", Community.class).setParameter("shortName", shortName)
				.getSingleResult();
	}

	@Override
	public Community find(String name) {
		return getSessionFactory().getCurrentSession()
				.createQuery("from Community c where c.name = :name", Community.class).setParameter("name", name)
				.getSingleResult();
	}

	@Override
	public Community findCommunityMembers(String name) {
		return getSessionFactory().getCurrentSession()
				.createQuery("from Community c left join fetch c.users where c.name = :name", Community.class)
				.setParameter("name", name).getSingleResult();
	}

	@Override
	public boolean hasMembers(Community community) {
		List<User> members = getSessionFactory().getCurrentSession()
				.createQuery("from Community c left join fetch c.users where c.id = :id", User.class)
				.setParameter("id", community.getId()).getResultList();
		return members.size() > 0;
	}

	@Override
	public List<User> findMembers(Community community) {
		return getSessionFactory().getCurrentSession()
				.createQuery("from Community c left join fetch c.users where c.id = :id", User.class)
				.setParameter("id", community.getId()).getResultList();
	}

}
