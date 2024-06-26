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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import de.winkler.betoffice.dao.CommunityDao;
import de.winkler.betoffice.storage.Community;
import de.winkler.betoffice.storage.CommunityFilter;
import de.winkler.betoffice.storage.CommunityReference;

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

        Community community = communityDao.find(CommunityReference.of("TDKB 2021/2022")).orElseThrow();
        assertThat(community).isNotNull();
        assertThat(community.getName()).isEqualTo("Bundesliga");
        assertThat(community.getYear()).isEqualTo("2021/2022");
    }

    @Test
    void findCommunityUsers() {
        Community community = communityDao.find(CommunityReference.of("TDKB 2021/2022")).orElseThrow();
        assertThat(community.getUsers()).hasSize(6);
        assertThat(community.getUsers())
                .extracting(user -> user.getNickname().value())
                .containsExactlyInAnyOrder("Frosch", "Steffen", "Mao", "Peter", "mrTipp", "Jogi");
    }

    @Test
    void findCommunities() {
        CommunityFilter filter = new CommunityFilter();
        filter.setName("Bundesliga");
        Page<Community> communityPage;

        communityPage = communityDao.findAll(filter, Pageable.unpaged());
        assertThat(communityPage).isNotNull().hasSize(23);

        var pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("name"), Sort.Order.desc("shortName")));
        communityPage = communityDao.findAll(filter, pageRequest);

        assertThat(communityPage).isNotNull().hasSize(10);

    }

}
