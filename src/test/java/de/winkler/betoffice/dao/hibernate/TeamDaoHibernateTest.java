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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.fest.util.Files;
import org.hibernate.jdbc.Work;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.dbload.csv.ResourceWriter;
import de.winkler.betoffice.dao.TeamDao;
import de.winkler.betoffice.storage.Team;
import de.winkler.betoffice.storage.enums.TeamType;

/**
 * A test class for {@link teamDao}.
 *
 * @author by Andre Winkler
 */
public class TeamDaoHibernateTest extends AbstractDaoTestSupport {

    @Autowired
    private TeamDao teamDao;

    @Before
    public void init() {
        prepareDatabase(TeamDaoHibernateTest.class);
    }

    @After
    public void shutdown() {
        getSessionFactory().getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                Statement stmt = connection.createStatement();
                stmt.execute("DELETE FROM bo_team");
                stmt.close();
            }
        });
    }

    @Test
    public void testDataSource() {
        assertThat(getDataSource(), notNullValue());
        assertThat(getSessionFactory(), notNullValue());
        assertThat(teamDao, notNullValue());
    }

    @Test
    public void testDataLoad() throws Exception {
        List<Team> teams = teamDao.findAll();
        assertThat(teams.size(), equalTo(4));

        Team rwe = teamDao.findById(1);
        assertThat(rwe.getName(), equalTo("RWE"));
        assertThat(rwe.getTeamType(), equalTo(TeamType.DFB));

        Team rwo = teamDao.findById(2);
        assertThat(rwo.getName(), equalTo("RWO"));
        assertThat(rwo.getTeamType(), equalTo(TeamType.DFB));

        Team deutschland = teamDao.findById(3);
        assertThat(deutschland.getName(), equalTo("Deutschland"));
        assertThat(deutschland.getTeamType(), equalTo(TeamType.FIFA));

        Team frankreich = teamDao.findById(4);
        assertThat(frankreich.getName(), equalTo("Frankreich"));
        assertThat(frankreich.getTeamType(), equalTo(TeamType.FIFA));

        Path path = new File("D:/tmp/team.dat").toPath();
        final ResourceWriter rw = new ResourceWriter(path);
        getSessionFactory().getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                rw.start(connection, "select * from bo_team", false);
            }
        });
    }

    @Test
    public void testteamDaoFindAll() {
        List<Team> teams = teamDao.findAll();
        assertEquals(4, teams.size());
        assertEquals("Deutschland", teams.get(0).getName());
        assertEquals("Frankreich", teams.get(1).getName());
        assertEquals("RWE", teams.get(2).getName());
        assertEquals("RWO", teams.get(3).getName());
    }

    @Test
    public void testteamDaoFindTeam() {
        Team team = teamDao.findByName("RWE");
        assertEquals("RWE", team.getName());
        team = teamDao.findByName("RWO");
        assertEquals("RWO", team.getName());
    }

    @Test
    public void testteamDaoFindTeams() {
        List<Team> teams = teamDao.findTeams(TeamType.DFB);
        assertEquals(2, teams.size());
        assertEquals("RWE", teams.get(0).getName());
        assertEquals(TeamType.DFB, teams.get(0).getTeamType());
        assertEquals("RWO", teams.get(1).getName());
        assertEquals(TeamType.DFB, teams.get(1).getTeamType());

        teams = teamDao.findTeams(TeamType.FIFA);
        assertEquals(2, teams.size());
        assertEquals("Deutschland", teams.get(0).getName());
        assertEquals(TeamType.FIFA, teams.get(0).getTeamType());
        assertEquals("Frankreich", teams.get(1).getName());
        assertEquals(TeamType.FIFA, teams.get(1).getTeamType());
    }

}
