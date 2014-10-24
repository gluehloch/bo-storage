/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2014 by Andre Winkler. All
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

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.winkler.betoffice.dao.TeamAliasDao;
import de.winkler.betoffice.dao.TeamDao;
import de.winkler.betoffice.storage.Team;
import de.winkler.betoffice.storage.TeamAlias;

/**
 * Test for class {@link TeamAliasDaoHibernate}.
 *
 * @author by Andre Winkler
 */
public class TeamAliasDaoHibernateTest extends AbstractDaoTestSupport {

    @Autowired
    private TeamAliasDao teamAliasDao;

    @Autowired
    private TeamDao teamDao;

    @Before
    public void init() {
        prepareDatabase(TeamAliasDaoHibernateTest.class);
    }

    @Test
    public void testTeamAliasDaoHibernateFindAll() {
        List<TeamAlias> teamAlias = teamAliasDao.findAll();
        assertEquals(4, teamAlias.size());
        assertEquals("RWE", teamAlias.get(0).getAliasName());
        assertEquals("RWO", teamAlias.get(1).getAliasName());
        assertEquals("S04", teamAlias.get(2).getAliasName());
        assertEquals("Schalke", teamAlias.get(3).getAliasName());
    }

    @Test
    public void testTeamAliasDaoHibernateFindAliasNames() {
        Team team = teamDao.findById(3);
        List<TeamAlias> findAliasNames = teamAliasDao
                .findAliasNames(team);
        assertEquals(2, findAliasNames.size());
        assertEquals("S04", findAliasNames.get(0).getAliasName());
        assertEquals("Schalke", findAliasNames.get(1).getAliasName());
    }

    @Test
    public void testTeamAliasDaoHibernateFindByAliasName() {
        Team team = null;
        team = teamAliasDao.findByAliasName("Schalke");
        assertEquals("FC Schalke 04", team.getName());

        team = teamAliasDao.findByAliasName("RWE");
        assertEquals("RWE", team.getName());
        assertEquals("Rot-Weiss-Essen", team.getLongName());
    }

}
