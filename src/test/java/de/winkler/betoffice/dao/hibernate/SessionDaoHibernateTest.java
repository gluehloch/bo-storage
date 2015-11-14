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
import static org.junit.Assert.assertThat;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.winkler.betoffice.dao.SessionDao;
import de.winkler.betoffice.storage.Session;

/**
 * A test for {@link SessionDao}.
 *
 * @author by Andre Winkler
 */
public class SessionDaoHibernateTest extends AbstractDaoTestSupport {

    @Autowired
    private SessionDao sessionDao;

    @Before
    public void init() {
        prepareDatabase(SessionDaoHibernateTest.class);
    }

    @Test
    public void testFindLoginSesionsByNickname() {
        List<Session> sessions = sessionDao.findByNickname("Frosch");
        assertThat(sessions.size(), equalTo(2));
        assertThat(sessions.get(0).getNickname(), equalTo("Frosch"));
        assertThat(sessions.get(0).getBrowser(), equalTo("firefoy"));
        assertThat(sessions.get(0).getFailedLogins(), equalTo(1));
        
        DateTime login = new DateTime(sessions.get(0).getLogin());
        DateTime expectedLogin = new DateTime(2015, 11, 14, 0, 0, 0);
        assertThat(login, equalTo(expectedLogin));
    }

}
