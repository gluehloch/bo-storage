/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2025 by Andre Winkler. All rights reserved.
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

package de.betoffice.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ValidationMessages {

    private final List<ValidationMessage> messages = new ArrayList<>();

    public static ValidationMessages empty() {
        return new ValidationMessages(List.of());
    }

    public static ValidationMessages of(ValidationMessage validationMessage) {
        return new ValidationMessages(List.of(validationMessage));
    }

    public static ValidationMessages of(List<ValidationMessage> validationMessages) {
        return new ValidationMessages(validationMessages);
    }

    private ValidationMessages(final List<ValidationMessage> messages) {
        this.messages.addAll(Objects.requireNonNull(messages));
    }

    public List<ValidationMessage> getMessages() {
        return messages;
    }

    public boolean containsAnError() {
        return this.messages.stream().anyMatch(i -> i.isAnError());
    }

}
