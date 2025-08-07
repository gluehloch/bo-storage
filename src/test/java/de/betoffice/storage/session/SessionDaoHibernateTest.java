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

package de.betoffice.storage.session;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.betoffice.dao.hibernate.AbstractDaoTestSupport;
import de.betoffice.storage.Session;

/**
 * A test for {@link SessionDao}.
 *
 * @author by Andre Winkler
 */
class SessionDaoHibernateTest extends AbstractDaoTestSupport {

    @Autowired
    private SessionDao sessionDao;

    @BeforeEach
    public void init() {
        prepareDatabase(SessionDaoHibernateTest.class);
    }
    
    @Test
    void validateSystemTimeZoneIsEuropeBerlin() {
        ZoneId systemDefault = ZoneId.systemDefault();
        assertThat(systemDefault).isEqualTo(ZoneId.of("Europe/Berlin"));
        
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime now2 = ZonedDateTime.now(ZoneId.systemDefault());
        
        System.out.println(now);
        System.out.println(now2);
    }

    @Test
    void testFindSessionsByNickname() {
        List<Session> sessions = sessionDao.findByNickname("Frosch");
        assertThat(sessions).hasSize(2);
        assertThat(sessions.get(0).getNickname()).isEqualTo("Frosch");
        assertThat(sessions.get(0).getBrowser()).isEqualTo("firefoy");
        assertThat(sessions.get(0).getFailedLogins()).isEqualTo(1);

        ZonedDateTime login = sessions.get(0).getLogin();
        ZonedDateTime expectedLogin = ZonedDateTime.of(LocalDate.of(2015, 11, 14), LocalTime.of(2, 0),
                ZoneId.of("Europe/Berlin"));
        assertThat(login).isEqualTo(expectedLogin);
    }

    @Test
    void testFindSessionBySessionId() {
        List<Session> sessions = sessionDao.findBySessionId("4711");
        assertThat(sessions.size()).isEqualTo(1);
        assertThat(sessions.get(0).getToken()).isEqualTo("4711");
    }

}
