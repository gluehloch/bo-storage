/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2024 by Andre Winkler. All
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

import javax.sql.DataSource;

import jakarta.persistence.EntityManager;

import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import de.betoffice.database.data.DeleteDatabase;
import de.dbload.Dbload;
import de.winkler.betoffice.conf.PersistenceJPAConfiguration;
import de.winkler.betoffice.conf.TestPropertiesConfiguration;

/**
 * DAO test support. The test method runs in his own transaction. So a lazy-loading exception
 * should not happen here.
 *
 * @author by Andre Winkler
 */
@ActiveProfiles(profiles = "test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { PersistenceJPAConfiguration.class, TestPropertiesConfiguration.class })
@ComponentScan({"de.winkler.betoffice", "de.betoffice"})
@Transactional
public abstract class AbstractDaoTestSupport {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private EntityManager entityManager;

    public void prepareDatabase(final Class<?> clazz) {
        deleteDatabase();
        entityManager.unwrap(Session.class).doWork(connection -> Dbload.read(connection, clazz));
    }

    @AfterEach
    public void deleteDatabase() {
        entityManager.unwrap(Session.class).doWork(connection -> DeleteDatabase.deleteDatabase(connection));
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

}
