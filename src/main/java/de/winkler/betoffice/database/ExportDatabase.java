/*
 * $Id: ExportDatabase.java 3850 2013-11-30 18:24:08Z andrewinkler $
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.filter.ITableFilter;
import org.dbunit.dataset.filter.SequenceTableFilter;
import org.dbunit.dataset.xml.FlatDtdDataSet;
import org.dbunit.dataset.xml.FlatXmlWriter;

import de.awtools.config.PropertiesGlueConfig;

/**
 * Exportiert die Daten einer Datenbank.
 * 
 * @todo Angabe der Exportdateien. Momentan wird die DTD nach database.dtd und
 *       die Daten nach database.xml ausgespielt.
 * 
 * @author $Author: andrewinkler $
 * @version $Revision: 3850 $ $Date: 2013-07-27 10:44:32 +0200 (Sa, 27 Jul 2013)
 *          $
 */
public class ExportDatabase {

	/**
	 * Exportiert eine DTD und XML Datei zur Beschreibung der Datenbank.
	 * 
	 * @param jdbcConnection
	 *            Eine JDBC Connection.
	 * @param dtdFile
	 *            Die DTD Datei.
	 * @param xmlFile
	 *            Die XML Datei.
	 * @throws SQLException
	 *             Problem beim Zugriff auf die Datenbank.
	 */
	public void export(final Connection jdbcConnection, final File dtdFile,
			final File xmlFile) throws SQLException {

		try {
			IDatabaseConnection connection = new DatabaseConnection(
					jdbcConnection);

			FlatDtdDataSet.write(connection.createDataSet(),
					new FileOutputStream(dtdFile));

			// ITableFilter filter = new DatabaseSequenceFilter(connection);
			ITableFilter filter = new SequenceTableFilter(new String[] {
					"bo_season", "bo_team", "bo_teamalias", "bo_user",
					"bo_user_season", "bo_grouptype", "bo_group",
					"bo_team_group", "bo_gamelist", "bo_game", "bo_gametipp" });
			IDataSet dataset = new FilteredDataSet(filter,
					connection.createDataSet());
			FlatXmlWriter writer = new FlatXmlWriter(new FileOutputStream(
					xmlFile));
			writer.setDocType(dtdFile.getName());
			writer.write(dataset);

			// IDataSet fullDataSet = connection.createDataSet();
			// FlatXmlDataSet.write(fullDataSet, new FileOutputStream(
			// xmlFile));
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} catch (DataSetException ex) {
			throw new RuntimeException(ex);
		} catch (FileNotFoundException ex) {
			throw new RuntimeException(ex);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		} finally {
			jdbcConnection.close();
		}
	}

	/**
	 * Dumps the database with DBUnit support. Call this main method with the
	 * following parameter to export the 'test' database:
	 * 
	 * <pre>
	 * /de/winkler/betoffice/test/database/test-mysql-piratestest.properties
	 * </pre>
	 * 
	 * @param args
	 *            Kommandozeilenparameter.
	 * @throws Exception
	 *             Da ging was schief.
	 */
	public static void main(final String[] args) throws Exception {
		URL resource = ExportDatabase.class.getResource(args[0]);
		PropertiesGlueConfig propertyHolder = new PropertiesGlueConfig(resource);
		propertyHolder.load();

		System.out.println("Loading the properties from URL: " + resource);

		String outputFile = null;
		if (args.length > 1) {
			outputFile = args[1];
		} else {
			outputFile = "database.dtd";
		}

		ExportDatabase exporter = new ExportDatabase();
		exporter.export(HibernateProperties.createConnection(propertyHolder
				.getProperties()), new File(outputFile), new File(
				"database.xml"));
	}

}
