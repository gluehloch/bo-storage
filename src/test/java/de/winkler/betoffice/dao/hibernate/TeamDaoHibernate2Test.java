/*
 * $Id: org.eclipse.jdt.ui.prefs 104 2008-06-08 10:34:41Z awinkler2 $
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
import static org.junit.Assert.assertThat;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.dbload.Dbload;
import de.dbload.DbloadContext;
import de.dbload.impl.DefaultDbloadContext;
import de.dbload.jdbc.DefaultJdbcTypeConverter;
import de.winkler.betoffice.dao.TeamDao;
import de.winkler.betoffice.storage.Team;
import de.winkler.betoffice.storage.enums.TeamType;

/**
 * A test class for {@link TeamDaoHibernate}.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedDate: 2008-06-08 12:34:41 +0200 (So, 08 Jun 2008) $
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/betoffice-datasource.xml",
        "/betoffice-persistence.xml", "/test-mysql-piratestest.xml" })
public class TeamDaoHibernate2Test extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private TeamDao teamDao;

    @Test
    public void testDataSource() {
        assertThat(dataSource, notNullValue());
        assertThat(teamDao, notNullValue());
        assertThat(sessionFactory, notNullValue());
    }

    @Test
    public void testDataLoad() throws Exception {
        sessionFactory.getCurrentSession().doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                DbloadContext dbloadContext = new DefaultDbloadContext(connection,
                        new DefaultJdbcTypeConverter());
                Dbload.start(dbloadContext, TeamDaoHibernate2Test.class);
            }
        });

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
    }

}
