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

/**
 * Eine Validierungsnachricht.
 *
 * @author by Andre Winkler
 */
public class BetofficeValidationMessage {

    public enum Severity {
        OK, ERROR, WARNING, INFO
    };

    public enum ErrorType {
        NO_ERROR, COMMUNITY_EXISTS
    };

    private final String message;
    private final Severity severity;

    public static BetofficeValidationMessage error(String message) {
        return new BetofficeValidationMessage(message, Severity.ERROR);
    }

    public static BetofficeValidationMessage error() {
        return new BetofficeValidationMessage("", Severity.ERROR);
    }

    public static BetofficeValidationMessage ok() {
        return new BetofficeValidationMessage("", Severity.OK);
    }

    private BetofficeValidationMessage(final String _message, final Severity _severity) {
        message = _message;
        severity = _severity;
    }

    public String getMessage() {
        return message;
    }

    /**
     * @return the severity
     */
    public Severity getSeverity() {
        return severity;
    }

}
