/*
 * ============================================================================
 *  Project betoffice-storage Copyright (c) 2000-2020 by Andre Winkler. All
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

package de.betoffice.dao.hibernate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.betoffice.dao.SeasonDao;
import de.betoffice.dao.hibernate.SeasonDaoHibernate;
import de.betoffice.storage.Season;
import de.betoffice.storage.SeasonReference;
import de.betoffice.storage.enums.TeamType;

/**
 * Testet isoliert das DAO {@link SeasonDaoHibernate}.
 *
 * @author by Andre Winkler
 */
class SeasonDaoHibernateTest extends AbstractDaoTestSupport {

    @Autowired
    private SeasonDao seasonDaoHibernate;

    @BeforeEach
    public void init() {
        prepareDatabase(SeasonDaoHibernateTest.class);
    }

    @Test
    void testSeasonFindAll() {
        List<Season> seasons = seasonDaoHibernate.findAll();
        assertEquals(4, seasons.size());
        assertEquals("1. Bundesliga", seasons.get(0).getReference().getName());
        assertEquals("1999/2000", seasons.get(0).getReference().getYear());
        assertEquals("1. Bundesliga", seasons.get(1).getReference().getName());
        assertEquals("2000/2001", seasons.get(1).getReference().getYear());
        assertEquals("1. Bundesliga", seasons.get(2).getReference().getName());
        assertEquals("2001/2002", seasons.get(2).getReference().getYear());
        assertEquals("1. Bundesliga", seasons.get(3).getReference().getName());
        assertEquals("2002/2003", seasons.get(3).getReference().getYear());
    }

    @Test
    void testSeasonFindByName() {
        assertThat(seasonDaoHibernate.find(SeasonReference.of("2000/2001", "1. Bundesliga")))
            .hasValueSatisfying(season -> {
                assertThat(season.getReference().getName()).isEqualTo("1. Bundesliga");
                assertThat(season.getReference().getYear()).isEqualTo("2000/2001");
            });

        assertThat(seasonDaoHibernate.find(SeasonReference.of("2001/2002", "1. Bundesliga")))
            .hasValueSatisfying(season -> {
                assertEquals("1. Bundesliga", season.getReference().getName());
                assertEquals("2001/2002", season.getReference().getYear());
            });
    }

    @Test
    void testSeasonFindByNameGroupTeam() {
        Season season = seasonDaoHibernate.find(SeasonReference.of("2000/2001", "1. Bundesliga")).orElseThrow();
        assertEquals("1. Bundesliga", season.getReference().getName());
        assertEquals("2000/2001", season.getReference().getYear());
        assertEquals(0, season.getGroups().size());
    }

    @Test
    void testSeasonDaoHibernate() {
        Season season = seasonDaoHibernate.find(SeasonReference.of("1999/2000", "1. Bundesliga")).orElseThrow();
        Season season2 = seasonDaoHibernate.findById(season.getId());

        assertEquals("1999/2000", season.getReference().getYear());
        assertEquals("1. Bundesliga", season.getReference().getName());
        assertEquals(TeamType.DFB, season.getTeamType());
        assertEquals("1999/2000", season2.getReference().getYear());
        assertEquals("1. Bundesliga", season2.getReference().getName());
        assertEquals(TeamType.DFB, season2.getTeamType());

        List<Season> seasons = seasonDaoHibernate.findAll();
        assertEquals(4, seasons.size());

        assertThat(seasons.get(0).getReference().getYear()).isEqualTo("1999/2000");
        assertThat(seasons.get(1).getReference().getYear()).isEqualTo("2000/2001");
        assertThat(seasons.get(2).getReference().getYear()).isEqualTo("2001/2002");
        assertThat(seasons.get(3).getReference().getYear()).isEqualTo("2002/2003");
    }

}
