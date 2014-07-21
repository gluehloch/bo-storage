/*
 * $Id: PersistenceTestSupport.java 3806 2013-08-26 17:22:53Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2008 by Andre Winkler. All rights reserved.
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

import javax.sql.DataSource;

import org.dbunit.Assertion;
import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultColumnFilter;
import org.dbunit.dataset.xml.FlatXmlDataSet;

/**
 * Utitlity for some test cases.
 * 
 * @todo This is not the best place for a test utility class. Here it is part of
 * the production code. This is more like a utility class for DBUnit.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3806 $ $LastChangedDate: 2013-08-26 19:22:53 +0200 (Mon, 26 Aug 2013) $
 */
public final class PersistenceTestSupport {

	/**
	 * Vergleicht den Tabelleninhalt der Tabelle <code>tableName</code> mit
	 * den Daten aus der DBUnit XML Datei. Die Name der DBUnit XML wird
	 * folgendermaßen zusammen gesetzt:<br>
	 * <code>clazzName-expectedXml.xml</code>
	 *
	 * @param dataSource Die Datenquelle.
	 * @param clazz Die Testklasse.
	 * @param tableName Der Name der Tabelle.
	 * @param expectedXml Die DBUnit XML Datei ohne die Endung '*.xml'.
	 * @throws Exception Da ging was schief.
	 */
	public static void assertTableEquals(final DataSource dataSource,
			final Class<?> clazz, final String tableName,
			final String expectedXml) throws Exception {

		StringBuilder sb = new StringBuilder(clazz.getSimpleName());
		sb.append("-").append(expectedXml).append(".xml");

		DataSourceDatabaseTester dsdt = new DataSourceDatabaseTester(dataSource);
		IDatabaseConnection conn = null;

		try {
    		conn = dsdt.getConnection();
    
    		// Fetch database data after executing your code
    		IDataSet databaseDataSet = conn.createDataSet();
    		ITable actualTable = databaseDataSet.getTable(tableName);
    
    		InputStream is = clazz.getResourceAsStream(sb.toString());
    		IDataSet expectedDataSet = new FlatXmlDataSet(is);
    		ITable expectedTable = expectedDataSet.getTable(tableName);
    
    		ITable filteredTable = DefaultColumnFilter.includedColumnsTable(
    			actualTable, expectedTable.getTableMetaData().getColumns());
    
    		// Assert actual database table match expected table
    		Assertion.assertEquals(expectedTable, filteredTable);
		} finally {
		    if (conn != null) {
		        dsdt.closeConnection(conn);
		    }
		}
	}

}
