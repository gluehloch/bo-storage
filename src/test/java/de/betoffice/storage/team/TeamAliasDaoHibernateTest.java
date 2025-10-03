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

package de.betoffice.storage.team;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.betoffice.dao.hibernate.AbstractDaoTestSupport;
import de.betoffice.storage.team.dao.TeamAliasDaoHibernate;
import de.betoffice.storage.team.entity.Team;

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

    @BeforeEach
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
        Optional<Team> team = null;
        team = teamAliasDao.findByAliasName("Schalke");
        assertEquals("FC Schalke 04", team.get().getName());

        team = teamAliasDao.findByAliasName("RWE");
        assertEquals("RWE", team.get().getName());
        assertEquals("Rot-Weiss-Essen", team.get().getLongName());
    }

}
