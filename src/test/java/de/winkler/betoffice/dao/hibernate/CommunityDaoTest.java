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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.classmate.ResolvedTypeWithMembers;

import de.winkler.betoffice.dao.CommunityDao;
import de.winkler.betoffice.storage.Community;
import de.winkler.betoffice.storage.User;

/**
 * Test for class {@link CommunityDao}.
 * 
 * @author Andre Winkler
 */
class CommunityDaoTest extends AbstractDaoTestSupport {

	@Autowired
	private CommunityDao communityDao;
	
    @BeforeEach
    public void init() {
        prepareDatabase(CommunityDaoTest.class);
    }

    @Test
    void findCommunityByShortName() {
    	assertThat(communityDao).isNotNull();
    	
    	Community community = communityDao.findByShortName("TDKB 2021/2022");
    	assertThat(community).isNotNull();
    	assertThat(community.getName()).isEqualTo("Bundesliga");
    	assertThat(community.getYear()).isEqualTo("2021/2022");    	 
    }
    
    @Test
    void findCommunityUsers() {
    	Community community = communityDao.findByShortName("TDKB 2021/2022");
    	List<User> members = communityDao.findMembers(community);
    	assertThat(members).hasSize(6);
    	
    }
    
}
