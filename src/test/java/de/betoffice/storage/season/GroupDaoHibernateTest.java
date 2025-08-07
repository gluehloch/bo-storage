/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2022 by Andre Winkler. All
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
 * this program; if not, write to the Free Software Foundation, Inc., 59 TempleO
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

package de.betoffice.storage.season;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.betoffice.dao.hibernate.AbstractDaoTestSupport;
import de.betoffice.storage.season.dao.GroupDaoHibernate;
import de.betoffice.storage.season.entity.Group;
import de.betoffice.storage.season.entity.Season;
import de.betoffice.storage.season.entity.SeasonReference;

/**
 * Test case for class {@link GroupDaoHibernate}.
 *
 * @author Andre Winkler
 */
class GroupDaoHibernateTest extends AbstractDaoTestSupport {

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private SeasonDao seasonDaoHibernate;

    @BeforeEach
    public void init() {
        prepareDatabase(GroupDaoHibernateTest.class);
    }

    @Test
    void testGroupDaoHibernateFindBySeason() {
        Season season = seasonDaoHibernate.find(SeasonReference.of("1000", "4711")).orElseThrow();
        List<Group> groups = groupDao.findBySeason(season);
        assertThat(groups).hasSize(2);
        assertThat(groups.get(0).getGroupType().getName()).isEqualTo("1. Liga");
        assertThat(groups.get(1).getGroupType().getName()).isEqualTo("2. Liga");
    }

}
