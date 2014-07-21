/*
 * $Id: CreateDatabaseSchema.java 3782 2013-07-27 08:44:32Z andrewinkler $
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

package de.winkler.betoffice.database;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.slf4j.Logger;

import de.awtools.basic.LoggerFactory;

/**
 * Klasse für das Anlegen eines Datenbankschemas.
 *
 * @author $Author: andrewinkler $
 * @version $Revision: 3782 $ $Date: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
public class CreateDatabaseSchema {

	/** Logger der Klasse. */
	private final Logger log = LoggerFactory.make();

	/**
	 * Legt das Datenbankschema 'still' in einer Datenbank an. D.h. es werden
	 * keine Ausgaben erzeugt.
	 *
	 * @param config Eine Hibernate Configuration.
	 */
	public void createSilently(final Configuration config) {
		SchemaExport schemaExport = new SchemaExport(config);
		schemaExport.setFormat(true);
		schemaExport.create(false, true);
	}

	/**
	 * Legt das Datenbankschema in einer Datenbank an.
	 *
	 * @param config Eine Hibernate Configuration.
	 * @param outputFile Das generierte Script wird in dieser Datei abgelegt.
	 *     Kann <code>null</code> sein.
	 */
	public void create(final Configuration config, final String outputFile) {
		SchemaExport schemaExport = new SchemaExport(config);
		schemaExport.setFormat(true);
		schemaExport.setOutputFile(outputFile);
		schemaExport.create(true, true);
	}

	/**
	 * Prüft ob alle Tabellen des Datenbankschemas vorhanden sind.
	 *
	 * @param config Eine Hibernate Konfiguration.
	 * @return Liefert <code>true</code> wenn alle Tabellen vorhanden.
	 */
	public boolean validateSchema(final Configuration config) {
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.openSession();

		boolean checkOk = true;
		Iterator<?> iter = config.getClassMappings();
		while (iter.hasNext()) {
			PersistentClass pc = (PersistentClass) iter.next();
			StringBuilder sb = new StringBuilder("from ").append(pc
				.getClassName());
			log.debug(sb.toString());

			try {
				Query query = session.createQuery(sb.toString());
				query.list();
			} catch (HibernateException ex) {
				log.warn("Table " + pc.getTable().getName() + " not found!");
				checkOk = false;
			}
		}
        session.close();
		return checkOk;
	}

	/**
	 *
	 * @param args <code>[0]</code> Verweis auf die database.properties Datei;
	 *     <code>[1]</code> Optional zu erzeugende Skriptdatei.
	 * @throws Exception Da ging was schief.
	 */
	public static void main(final String[] args) throws Exception {
		Properties properties = new Properties();
		properties.load(new FileInputStream(new File(args[0])));
		// TODO properties.loadFromXML(new FileInputStream(new File(args[0])));
		String outputFile = null;
		if (args.length > 1) {
			outputFile = args[1];
		}

		CreateDatabaseSchema script = new CreateDatabaseSchema();
		script.create(HibernateProperties.createConfiguration(properties),
			outputFile);
	}
}
