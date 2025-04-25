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
public class ValidationMessage {

    public enum Severity {
        OK,
        ERROR,
        WARNING,
        INFO
    };

    public enum MessageType {
        NO_ERROR(""),
        UNKNOWN_ERROR("Unbekannter Fehler."),
        COMMUNITY_EXISTS("Die Community %s existiert nicht."),
        EMAIL_CHANGE_DATETIME_IS_IN_THE_FUTURE(
                "Der Bestätigungsmail wurde in der Zukunft verschickt."),
        EMAIL_CHANGE_DATETIME_EXPIRED(
                "Die Bestätigungslink für die Änderung der Email-Adresse ist nicht mehr gültig."),
        USER_NOT_FOUND("Der Nutzer %s ist nicht bekannt."),
        NICKNAME_IS_NOT_SET("Nickname ist nicht gesetzt."),
        GROUP_TYPE_NAME_IS_NOT_SET("Gruppentyp Name ist nicht gesetzt."),
        TEAM_NAME_IS_NOT_SET("Mannschaftsname ist nicht gesetzt."),
        TEAM_ALIAS_NAME_IS_NOT_SET("Alias Mannschaftsname ist nicht gesetzt."),
        TEAM_ALREADY_EXISTS("Mannschaftsname ist bereits vorhanden."),
        SEASON_DELETE_NOT_POSSIBE_COMMUNITIES_EXISTS(
                "Die Meisterschaft kann nicht entfernt werden, da Communities zu der Meisterschaft existieren."),
        SEASON_DELETE_NOT_POSSIBLE_ROUNDS_EXISTS(
                "Die Meisterschaft kann nicht entfernt werden, da Spieltage zu der Meisterschaft existieren."),
        SEASON_DELETE_NOT_POSSIBLE_GROUPS_EXISTS(
                "Die Meisterschaft kann nicht entfernt werden, da Gruppen der Meisterschaft zugeordnet sind."),
        SEASON_DOES_NOT_SUPPORT_THIS_TEAM_TYPE("Die Meisterschaft %s unterstützt diesen Mannschaftstyp %s nicht."),
        SEASON_GROUP_TEAM_IS_ALREADY_A_MEMBER(
                "Die Mannschaft '%s' ist bereits Teil der Meisterschaft '%s' für die Gruppe '%s'."),
        ROUND_ID_NOT_FOUND("Eine Spielrunde mit der ID '%d' ist nicht vorhanden.");

        private final String message;

        MessageType(String message) {
            this.message = message;
        }

        public String message() {
            return message;
        }

        public String formattedMessage(Object s) {
            return String.format(message(), s);
        }

        public String formattedMessage(Object... s) {
            return String.format(message(), s);
        }
    };

    private final Object[] messageParams;
    private final Severity severity;
    private final MessageType messageType;

    public static ValidationMessage error() {
        return new ValidationMessage(Severity.ERROR, MessageType.UNKNOWN_ERROR);
    }

    public static ValidationMessage error(final MessageType messageType) {
        return new ValidationMessage(Severity.ERROR, messageType);
    }

    public static ValidationMessage error(final MessageType messageType, final Object... messageParams) {
        return new ValidationMessage(Severity.ERROR, messageType, messageParams);
    }

    public static ValidationMessage ok() {
        return new ValidationMessage(Severity.OK, MessageType.NO_ERROR);
    }

    private ValidationMessage(final Severity severity, final MessageType messageType, final Object... messageParams) {
        this.messageType = messageType;
        this.severity = severity;
        this.messageParams = messageParams;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public Severity getSeverity() {
        return severity;
    }

    public Object[] getMessageParams() {
        return messageParams;
    }

    public boolean isOk() {
        return Severity.OK.equals(this.severity);
    }

    public boolean isAnError() {
        return Severity.ERROR.equals(this.severity);
    }

    public String getMessage() {
        if (messageParams == null) {
            return getMessageType().message();
        } else {
            return getMessageType().formattedMessage(messageParams);
        }
    }
}
