/*
 * ============================================================================
 *  Project betoffice-storage Copyright (c) 2000-2014 by Andre Winkler. All
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.winkler.betoffice.dao.SeasonDao;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.enums.TeamType;

/**
 * Testet isoliert das DAO {@link SeasonDaoHibernate}.
 *
 * @author by Andre Winkler
 */
public class SeasonDaoHibernateTest extends AbstractDaoTestSupport {

    @Autowired
    private SeasonDao seasonDaoHibernate;

    @BeforeEach
    public void init() {
        prepareDatabase(SeasonDaoHibernateTest.class);
    }

    @Test
    public void testSeasonFindAll() {
        List<Season> seasons = seasonDaoHibernate.findAll();
        assertEquals(4, seasons.size());
        assertEquals("1. Bundesliga", seasons.get(0).getName());
        assertEquals("1999/2000", seasons.get(0).getYear());
        assertEquals("1. Bundesliga", seasons.get(1).getName());
        assertEquals("2000/2001", seasons.get(1).getYear());
        assertEquals("1. Bundesliga", seasons.get(2).getName());
        assertEquals("2001/2002", seasons.get(2).getYear());
        assertEquals("1. Bundesliga", seasons.get(3).getName());
        assertEquals("2002/2003", seasons.get(3).getYear());
    }

    @Test
    public void testSeasonFindByName() {
        Optional<Season> season = null;

        season = seasonDaoHibernate.findByName("1. Bundesliga", "2000/2001");
        assertEquals("1. Bundesliga", season.get().getName());
        assertEquals("2000/2001", season.get().getYear());

        season = seasonDaoHibernate.findByName("1. Bundesliga", "2001/2002");
        assertEquals("1. Bundesliga", season.get().getName());
        assertEquals("2001/2002", season.get().getYear());
    }

    @Test
    public void testSeasonFindRoundGroupTeamUser() {
        Optional<Season> season = seasonDaoHibernate.findByName("1. Bundesliga",
                "2000/2001");
        Season result = seasonDaoHibernate.findRoundGroupTeamUser(season.get());
        assertEquals("1. Bundesliga", result.getName());
        assertEquals("2000/2001", result.getYear());
        assertEquals(0, result.getGroups().size());
        assertEquals(0, result.getUsers().size());
    }

    @Test
    public void testSeasonFindRoundGroupTeamUserTipp() {
        Optional<Season> season = seasonDaoHibernate.findByName("1. Bundesliga",
                "2000/2001");
        Season result = seasonDaoHibernate
                .findRoundGroupTeamUserTipp(season.get());
        assertEquals("1. Bundesliga", result.getName());
        assertEquals("2000/2001", result.getYear());
        assertEquals(0, result.getGroups().size());
        assertEquals(0, result.getUsers().size());
    }

    @Test
    public void testSeasonDaoHibernate() {
        Optional<Season> season = seasonDaoHibernate.findByName("1. Bundesliga",
                "1999/2000");
        Season season2 = seasonDaoHibernate.findById(season.get().getId());

        assertEquals("1999/2000", season.get().getYear());
        assertEquals("1. Bundesliga", season.get().getName());
        assertEquals(TeamType.DFB, season.get().getTeamType());
        assertEquals("1999/2000", season2.getYear());
        assertEquals("1. Bundesliga", season2.getName());
        assertEquals(TeamType.DFB, season2.getTeamType());

        List<Season> seasons = seasonDaoHibernate.findAll();
        assertEquals(4, seasons.size());

        assertThat(seasons.get(0).getYear()).isEqualTo("1999/2000");
        assertThat(seasons.get(1).getYear()).isEqualTo("2000/2001");
        assertThat(seasons.get(2).getYear()).isEqualTo("2001/2002");
        assertThat(seasons.get(3).getYear()).isEqualTo("2002/2003");
    }

}
