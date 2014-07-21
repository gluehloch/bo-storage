/*
 * $Id: TeamDaoHibernateTest.java 3782 2013-07-27 08:44:32Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2012 by Andre Winkler. All rights reserved.
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

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.winkler.betoffice.storage.Team;
import de.winkler.betoffice.storage.enums.TeamType;

/**
 * Testet das DAO {@link TeamDaoHibernate}.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3782 $ $LastChangedDate: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
public class TeamDaoHibernateTest extends HibernateDaoTestSupport {

    private TeamDaoHibernate teamDaoHibernate;

    @Test
    public void testTeamDaoHibernateFindAll() {
        List<Team> teams = teamDaoHibernate.findAll();
        assertEquals(4, teams.size());
        assertEquals("Deutschland", teams.get(0).getName());
        assertEquals("Frankreich", teams.get(1).getName());
        assertEquals("RWE", teams.get(2).getName());
        assertEquals("RWO", teams.get(3).getName());
    }

    @Test
    public void testTeamDaoHibernateFindTeam() {
        Team team = teamDaoHibernate.findByName("RWE");
        assertEquals("RWE", team.getName());
        team = teamDaoHibernate.findByName("RWO");
        assertEquals("RWO", team.getName());
    }

    @Test
    public void testTeamDaoHibernateFindTeams() {
        List<Team> teams = teamDaoHibernate.findTeams(TeamType.DFB);
        assertEquals(2, teams.size());
        assertEquals("RWE", teams.get(0).getName());
        assertEquals(TeamType.DFB, teams.get(0).getTeamType());
        assertEquals("RWO", teams.get(1).getName());
        assertEquals(TeamType.DFB, teams.get(1).getTeamType());

        teams = teamDaoHibernate.findTeams(TeamType.FIFA);
        assertEquals(2, teams.size());
        assertEquals("Deutschland", teams.get(0).getName());
        assertEquals(TeamType.FIFA, teams.get(0).getTeamType());
        assertEquals("Frankreich", teams.get(1).getName());
        assertEquals(TeamType.FIFA, teams.get(1).getTeamType());
    }

    @Before
    public void setUp() {
        teamDaoHibernate = new TeamDaoHibernate();
        teamDaoHibernate.setSessionFactory(sessionFactory);
    }

}
