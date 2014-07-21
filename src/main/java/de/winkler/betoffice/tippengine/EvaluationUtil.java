/*
 * $Id: EvaluationUtil.java 3782 2013-07-27 08:44:32Z andrewinkler $
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

package de.winkler.betoffice.tippengine;

import org.apache.commons.lang.Validate;

import de.awtools.basic.NumberUtils;
import de.winkler.betoffice.storage.UserResult;

/**
 * Liefert ein paar statistische Werte zu gemachten Tipps.
 *
 * @author  $Author: andrewinkler $
 * @version $Revision: 3782 $ $Date: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
public class EvaluationUtil {

	/** Das zu bewertende UserResult. */
	private UserResult result;

	/**
	 * Konstruktor.
	 *
	 * @param value Das zu bearbeitende UserResult.
	 */
	public EvaluationUtil(final UserResult value) {
		Validate.notNull(value, "value ist 'null'.");
		result = value;
	}

	/**
	 * Formatiert einen <code>double</code> Wert.
	 *
	 * @param _value Der zu formatierende Wert.
	 * @return Der formatierte Wert.
	 */
	public String format(final double _value) {
	    NumberUtils numberUtils = new NumberUtils();
		return numberUtils.formatDouble(_value);
	}

	/**
	 * Prozentanteil der Tickets aller Tipps einer Saison.
	 *
	 * @return % der Tickets.
	 */
	public double getTicketPercent() {
		if (result.getTippedGames() == 0) {
			return 0;
		} else {
			return ((double) result.getTicket() / (double) result
				.getTippedGames()) * 100;
		}
	}

	/**
	 * Prozentanteil der Toto-Wertung aller Tipps einer Saison.
	 *
	 * @return % mit Toto-Wertung.
	 */
	public double getTotoPercent() {
		if (result.getTippedGames() == 0) {
			return 0;
		} else {
			return ((double) result.getUserTotoWin() / (double) result
				.getTippedGames()) * 100;
		}
	}

	/**
	 * Prozentanteil der BigPoints aller Tipps einer Saison.
	 *
	 * @return % der BigPoints.
	 */
	public double getWinPercent() {
		if (result.getTippedGames() == 0) {
			return 0;
		} else {
			return ((double) result.getUserWin() / (double) result
				.getTippedGames()) * 100;
		}
	}

}
