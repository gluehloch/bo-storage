/*
 * $Id: MySqlDatabasedTestSupport.java 3782 2013-07-27 08:44:32Z andrewinkler $
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

package de.winkler.betoffice.test.database;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import de.awtools.basic.LoggerFactory;
import de.winkler.betoffice.database.ImportDatabase;

/**
 * Bereitet eine MySQL Datenbank für einen oder mehrere Testfälle vor. Geladen
 * wird ein relativ komplexer Datenbestand. Auf diesem Datenbestand sollten so
 * viele Testfälle wie möglich abgehandelt werden.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3782 $ $LastChangedDate: 2013-07-27 10:44:32 +0200 (Sa, 27 Jul 2013) $
 */
public final class MySqlDatabasedTestSupport {

    private final Logger log = LoggerFactory.make();

    public enum DataLoader {

        /** Eine leere Datenbank. */
        EMPTY(""),

        /** Die kompletten Produktionsdaten einladen. */
        FULL("/de/winkler/betoffice/test/database/database.xml"),

        /** Nur die Stammdaten einladen. */
        MASTER_DATA("/de/winkler/betoffice/test/database/masterdata.xml");

        /** Die zu ladende XML Resource. */
        private final String xmlResource;

        private DataLoader(final String resource) {
            xmlResource = resource;
        }

        /**
         * Liefert die XML Resource.
         *
         * @return Die Resource.
         */
        String getResource() {
            return xmlResource;
        }
    };

    /**
     * Erstellt wenn nötig das Schema und spielt die Testdaten ein. Die
     * Testdaten enthalten die Produktionsdaten der Spielzeiten 2000 - 2007.
     *
     * @param _conn A database connection object
     * @param _dataLoader Welche Daten sollen geladen werden?
     * @throws SQLException Datenbank konnte nicht geleert werden.
     */
    public void setUp(final Connection _conn, final DataLoader _dataLoader)
            throws SQLException {

        deleteDatabase(_conn);
        if (!_dataLoader.equals(DataLoader.EMPTY)) {
            ImportDatabase importDatabase = new ImportDatabase();
            InputStream xmlStream = this.getClass().getResourceAsStream(
                    _dataLoader.getResource());
            InputStream dtdStream = this.getClass().getResourceAsStream(
                    "/de/winkler/betoffice/test/database/database.dtd");
            importDatabase.load(_conn, xmlStream, dtdStream);
            _conn.commit();
        }
    }

    /**
     * Löscht alle Tabellen der Testdatenbank.
     *
     * @param _conn A database connection.
     * @throws SQLException Da ging was beim Löschen daneben.
     */
    public void deleteDatabase(final Connection _conn) throws SQLException {
        try {
            SingleConnectionDataSource scds = new SingleConnectionDataSource(
                    _conn, true);
            scds.setAutoCommit(false);
            JdbcTemplate jdbcTemplate = new JdbcTemplate(scds);
            jdbcTemplate.execute("update bo_season set bo_current_ref = null");
            jdbcTemplate.execute("delete from bo_gametipp");
            jdbcTemplate.execute("delete from bo_game");
            jdbcTemplate.execute("delete from bo_gamelist");
            jdbcTemplate.execute("delete from bo_team_group");
            jdbcTemplate.execute("delete from bo_group");
            jdbcTemplate.execute("delete from bo_user_season");
            jdbcTemplate.execute("delete from bo_season");
            jdbcTemplate.execute("delete from bo_teamalias");
            jdbcTemplate.execute("delete from bo_team");
            jdbcTemplate.execute("delete from bo_user");
            jdbcTemplate.execute("delete from bo_grouptype");
            _conn.commit();
        } catch (Exception ex) {
            log.debug("Unable to delete database: ", ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     * Lädt die MySQL Datenbank mit Produktionsdaten für den Integrationstest.
     *
     * @param args Werden ignoriert.
     * @throws Exception Da ging was schief.
     */
    public static void main(String args[]) throws Exception {
        HibernateConnectionFactory factory = new HibernateConnectionFactory();
        Connection conn = factory.getHibernateProperties().createConnection();
        conn.setAutoCommit(false);

        try {
            MySqlDatabasedTestSupport mySqlDatabasedTestSupport = new MySqlDatabasedTestSupport();
            mySqlDatabasedTestSupport.setUp(conn, DataLoader.FULL);
            conn.commit();
        } catch (Exception ex) {
            conn.rollback();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

}
