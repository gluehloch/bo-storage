/*
 * $Id: SeasonDaoHibernateTest.java 3782 2013-07-27 08:44:32Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2008 by Andre Winkler. All rights reserved.
 * ============================================================================
 *          GNU GENERAL PUBLIC LICENSE
 *  TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package de.winkler.betoffice.dao.hibernate;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.unitils.reflectionassert.ReflectionAssert;

import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.enums.TeamType;

/**
 * Testet isoliert das DAO {@link SeasonDaoHibernate}.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3782 $ $LastChangedDate: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
public class SeasonDaoHibernateTest extends HibernateDaoTestSupport {

    private SeasonDaoHibernate seasonDaoHibernate;

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
        Season season = null;

        season = seasonDaoHibernate.findByName("1. Bundesliga", "2000/2001");
        assertEquals("1. Bundesliga", season.getName());
        assertEquals("2000/2001", season.getYear());

        season = seasonDaoHibernate.findByName("1. Bundesliga", "2001/2002");
        assertEquals("1. Bundesliga", season.getName());
        assertEquals("2001/2002", season.getYear());
    }

    @Test
    public void testSeasonFindRoundGroupTeamUser() {
        Season season = seasonDaoHibernate.findByName("1. Bundesliga", "2000/2001");
        season = seasonDaoHibernate.findRoundGroupTeamUser(season);
        assertEquals("1. Bundesliga", season.getName());
        assertEquals("2000/2001", season.getYear());
        assertEquals(0, season.getGroups().size());
        assertEquals(0, season.getUsers().size());
    }

    @Test
    public void testSeasonFindRoundGroupTeamUserTipp() {
        Season season = seasonDaoHibernate.findByName("1. Bundesliga", "2000/2001");
        season = seasonDaoHibernate.findRoundGroupTeamUserTipp(season);
        assertEquals("1. Bundesliga", season.getName());
        assertEquals("2000/2001", season.getYear());
        assertEquals(0, season.getGroups().size());
        assertEquals(0, season.getUsers().size());        
    }

    @Test
    public void testSeasonDaoHibernate() {
        Season season = seasonDaoHibernate.findByName("1. Bundesliga",
                "1999/2000");
        Season season2 = seasonDaoHibernate.findById(season.getId());

        assertEquals("1999/2000", season.getYear());
        assertEquals("1. Bundesliga", season.getName());
        assertEquals(TeamType.DFB, season.getTeamType());
        assertEquals("1999/2000", season2.getYear());
        assertEquals("1. Bundesliga", season2.getName());
        assertEquals(TeamType.DFB, season2.getTeamType());

        List<Season> seasons = seasonDaoHibernate.findAll();
        assertEquals(4, seasons.size());

        ReflectionAssert.assertPropertyLenientEquals("year", Arrays.asList(
                "1999/2000", "2000/2001", "2001/2002", "2002/2003"), seasons);
    }

    @Before
    public void setUp() throws IOException {
        seasonDaoHibernate = new SeasonDaoHibernate();
        seasonDaoHibernate.setSessionFactory(sessionFactory);
    }

}
