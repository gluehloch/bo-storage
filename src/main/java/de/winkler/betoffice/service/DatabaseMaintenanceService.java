/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2024 by Andre Winkler. All rights reserved.
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

package de.winkler.betoffice.service;

/**
 * Ausführung von benutzerseitigen HQL Skripten auf der Datenbank.
 *
 * @author by Andre Winkler
 */
public interface DatabaseMaintenanceService {

	/**
	 * Setzt einen HQL Befehl direkt auf der Datenbank ab.
	 *
	 * @param hql Ein HQL Befehl.
	 * @return Rückgabeergebnis.
	 */
	public Object executeHql(String hql);

	/**
	 * Setzt einen nativen SQL Befehl auf die Datenbank ab.
	 *
	 * @param sqlQuery Der SQL Befehl.
	 * @return Rückgabaergbenis.
	 */
	public Object executeSQL(String sqlQuery);

}
