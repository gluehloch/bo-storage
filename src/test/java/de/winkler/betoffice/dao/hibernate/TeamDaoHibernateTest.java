/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2019 by Andre Winkler. All
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

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.hibernate.jdbc.Work;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.dbload.csv.writer.ResourceWriter;
import de.winkler.betoffice.dao.TeamDao;
import de.winkler.betoffice.storage.Team;
import de.winkler.betoffice.storage.enums.TeamType;

/**
 * A test class for {@link TeamDao}.
 *
 * @author by Andre Winkler
 */
public class TeamDaoHibernateTest extends AbstractDaoTestSupport {

    @Autowired
    private TeamDao teamDao;

    @BeforeEach
    public void init() {
        prepareDatabase(TeamDaoHibernateTest.class);
    }

    @Test
    public void testDataSource() {
        assertThat(getDataSource()).isNotNull();
        assertThat(getSessionFactory()).isNotNull();
        assertThat(teamDao).isNotNull();
    }

    @Test
    public void testTeamDaoOpenligaidFinder() {
        Optional<Team> rwe = teamDao.findByOpenligaid(10);
        assertThat(rwe.get().getName()).isEqualTo("RWE");
        Optional<Team> rwo = teamDao.findByOpenligaid(20);
        assertThat(rwo.get().getName()).isEqualTo("RWO");
        Optional<Team> deutschland = teamDao.findByOpenligaid(30);
        assertThat(deutschland.get().getName()).isEqualTo("Deutschland");
        Optional<Team> frankreich = teamDao.findByOpenligaid(40);
        assertThat(frankreich.get().getName()).isEqualTo("Frankreich");
    }

    @Test
    public void testTeamDaoFinder() throws Exception {
        List<Team> teams = teamDao.findAll();
        assertThat(teams).hasSize(4);

        Team rwe = teamDao.findById(1);
        assertThat(rwe.getName()).isEqualTo("RWE");
        assertThat(rwe.getTeamType()).isEqualTo(TeamType.DFB);

        Team rwo = teamDao.findById(2);
        assertThat(rwo.getName()).isEqualTo("RWO");
        assertThat(rwo.getTeamType()).isEqualTo(TeamType.DFB);

        Team deutschland = teamDao.findById(3);
        assertThat(deutschland.getName()).isEqualTo("Deutschland");
        assertThat(deutschland.getTeamType()).isEqualTo(TeamType.FIFA);

        Team frankreich = teamDao.findById(4);
        assertThat(frankreich.getName()).isEqualTo("Frankreich");
        assertThat(frankreich.getTeamType()).isEqualTo(TeamType.FIFA);

        File teamExportFile = File.createTempFile("team", "dat");
        final ResourceWriter rw = new ResourceWriter(teamExportFile, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        getSessionFactory().getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                rw.start(connection, "select * from bo_team", false);
            }
        });

        String fileToString = FileUtils.readFileToString(teamExportFile, "UTF-8");
        System.out.println(fileToString);
    }

    @Test
    public void testTeamDaoFindAll() {
        List<Team> teams = teamDao.findAll();
        assertEquals(4, teams.size());
        assertEquals("Deutschland", teams.get(0).getName());
        assertEquals("Frankreich", teams.get(1).getName());
        assertEquals("RWE", teams.get(2).getName());
        assertEquals("RWO", teams.get(3).getName());
    }

    @Test
    public void testTeamDaoFindTeam() {
        Optional<Team> team = teamDao.findByName("RWE");
        assertEquals("RWE", team.get().getName());
        team = teamDao.findByName("RWO");
        assertEquals("RWO", team.get().getName());
    }

    @Test
    public void testTeamDaoFindTeams() {
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
