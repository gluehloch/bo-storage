/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2022 by Andre Winkler. All rights reserved.
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

package de.winkler.betoffice.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Eine Validierungsnachricht.
 *
 * @author by Andre Winkler
 */
public class BetofficeValidationMessage {

	public static String NO_ERROR_MESSAGE = "";
	public static String NO_PROPERTY_NAME = "";

	public enum ErrorType {
		NO_ERROR, COMMUNITY_EXISTS
	}

	public enum Severity {
		ERROR, WARNING, INFO, OK
	};

	private final ErrorType errorType;
	private final Severity severity;
	private final String message;
	private final List<String> propertyNames = new ArrayList<>();

	/**
	 * Erfolgreiche Validation Message.
	 */
	public BetofficeValidationMessage() {
		this(ErrorType.NO_ERROR, Severity.OK, NO_PROPERTY_NAME, NO_ERROR_MESSAGE);
	}

	/**
	 * Validation Message.
	 *
	 * @param severity Dringlichkeit.
	 */
	public BetofficeValidationMessage(Severity severity) {
		this(ErrorType.NO_ERROR, severity, NO_PROPERTY_NAME, NO_ERROR_MESSAGE);
	}

	/**
	 * Konstruktor.
	 *
	 * @param errorType Fehlertyp
	 * @param severity  Die Dringlichkeit.
	 */
	public BetofficeValidationMessage(ErrorType errorType, Severity severity) {
		this(errorType, severity, NO_PROPERTY_NAME, NO_ERROR_MESSAGE);
	}

	/**
	 * Konstruktor.
	 *
	 * @param errorType    Fehlertyp
	 * @param severity     Die Dringlichkeit.
	 * @param propertyName Die Eigenschaft, die den Fehler verursacht.
	 * @param message      Eine Fehlernachricht
	 */
	public BetofficeValidationMessage(ErrorType errorType, Severity severity, String propertyName, String message) {
		this.errorType = errorType;
		this.severity = severity;
		this.message = message;
		this.propertyNames.add(propertyName);
	}

	public boolean isSuccessful() {
		return Severity.OK.equals(severity);
	}

	public ErrorType errorType() {
		return errorType;
	}

	public Severity severity() {
		return severity;
	}

	public String message() {
		return message;
	}

	public List<String> propertyNames() {
		return Collections.unmodifiableList(propertyNames);
	}

}
