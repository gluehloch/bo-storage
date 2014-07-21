/*
 * $Id: ImportDatabase.java 3782 2013-07-27 08:44:32Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2007 by Andre Winkler. All rights reserved.
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

package de.winkler.betoffice.database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.io.IOUtils;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.dataset.DefaultTable;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;

import de.awtools.config.PropertiesGlueConfig;

/**
 * Importiert die DBUnit Daten.
 * 
 * @todo Angabe der Exportdateien. Momentan wird die DTD nach database.dtd und
 *   die Daten nach database.xml ausgespielt.
 * 
 * @author $Author: andrewinkler $
 * @version $Revision: 3782 $ $Date: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
public class ImportDatabase {

	/**
	 * Importiert einen DBUnit Datenbestand.
	 * 
	 * @param jdbcConnection Eine JDBC Connection.
	 * @param xmlFile Die zu importierende XML Datei.
	 * @param dtdFile Die DTD.
	 */
	public void load(final Connection jdbcConnection, final String xmlFile,
			final String dtdFile) {

		FileInputStream xmlFis = null;
		FileInputStream dtdFis = null;
		try {
			xmlFis = new FileInputStream(xmlFile);
			dtdFis = new FileInputStream(dtdFile);
			load(jdbcConnection, xmlFis, dtdFis);
		} catch (FileNotFoundException ex) {
			throw new RuntimeException(ex);
		} finally {
			IOUtils.closeQuietly(xmlFis);
			IOUtils.closeQuietly(dtdFis);
		}
	}

	/**
	 * Imports DBUnit test data. After import the method sends a commit to
	 * the database. The database connection will be left open.
	 * 
	 * @param jdbcConnection Eine JDBC Connection.
	 * @param xmlStream Die zu importierende XML Datei.
	 * @param dtdStream Die DTD.
	 */
	public void load(final Connection jdbcConnection,
			final InputStream xmlStream, final InputStream dtdStream) {

		try {
			IDatabaseConnection connection = new DatabaseConnection(
				jdbcConnection);

			jdbcConnection.setAutoCommit(false);
			Statement stmt = jdbcConnection.createStatement();
			// TODO MySQL spezifischer Code!!
			stmt.execute("SET FOREIGN_KEY_CHECKS = 0;");
			stmt.execute("SET UNIQUE_CHECKS=0;");

			String id = "http://www.dbunit.org/properties/datatypeFactory";
			DatabaseConfig config = connection.getConfig();
			System.out.println("datatypeFactory clazz: "
					+ config.getProperty(id));

			// initialize your dataset here
			FlatXmlDataSet dataSet = new FlatXmlDataSet(xmlStream, dtdStream);

			// Löscht alle Tabellen.
			String[] tables = dataSet.getTableNames();
			int i = tables.length - 1;
			while (i >= 0) {
				String table = tables[i];
				IDataSet dataSetDelete = new DefaultDataSet(new DefaultTable(
					table));
				DatabaseOperation.DELETE_ALL.execute(connection, dataSetDelete);
				i--;
			}

			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
			// TODO MySQL spezifischer Code!!
			stmt.execute("SET FOREIGN_KEY_CHECKS = 1;");
			stmt.execute("SET UNIQUE_CHECKS = 1;");
			jdbcConnection.commit();
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} catch (DataSetException ex) {
			throw new RuntimeException(ex);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		} catch (DatabaseUnitException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Exportiert den Datenbestand mit Hilfe von DBUnit.
	 * 
	 * @param args Kommandozeilenparameter.
	 * @throws Exception Da ging was schief.
	 */
	public static void main(final String[] args) throws Exception {
		URL resource = ImportDatabase.class.getResource(args[0]);
		PropertiesGlueConfig propertyHolder = new PropertiesGlueConfig(resource);
		propertyHolder.load();

		String inputFile = null;
		if (args.length > 1) {
			inputFile = args[1];
		} else {
			inputFile = "database.xml";
		}

		String dtdFile = null;
		if (args.length > 2) {
			dtdFile = args[2];
		} else {
			dtdFile = "database.dtd";
		}

		ImportDatabase importer = new ImportDatabase();
		importer.load(HibernateProperties.createConnection(propertyHolder
			.getProperties()), inputFile, dtdFile);
	}

}
