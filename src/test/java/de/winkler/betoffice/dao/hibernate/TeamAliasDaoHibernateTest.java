/*
 * $Id: TeamAliasDaoHibernateTest.java 3782 2013-07-27 08:44:32Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2012 by Andre Winkler. All
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

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.hibernate.jdbc.Work;
import org.junit.After;
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
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3782 $ $LastChangedDate: 2013-07-27 10:44:32
 *          +0200 (Sat, 27 Jul 2013) $
 */
public class TeamAliasDaoHibernateTest extends DaoTestSupport {

    @Autowired
    private TeamAliasDao teamAliasDao;

    @Autowired
    private TeamDao teamDao;

    @Before
    public void init() {
        prepareDatabase(TeamAliasDaoHibernateTest.class);
    }

    @After
    public void shutdown() {
        getSessionFactory().getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                Statement stmt = connection.createStatement();
                stmt.execute("DELETE FROM bo_teamalias");
                stmt.execute("DELETE FROM bo_team");
                stmt.close();
            }
        });
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
