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

package de.winkler.betoffice.validation;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import de.winkler.betoffice.validation.ValidationMessage.MessageType;

/**
 * A service result.
 * 
 * @author Andre Winkler
 */
public interface ServiceResult<T> {

    Optional<T> result();

    <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X;

    T orElseThrow();

    List<ValidationMessage> messages();

    boolean containsAnError();

    static <T> ServiceResult<T> sucess(final T result) {
        return DefaultServiceResult.sucess(result);
    }

    static <T> ServiceResult<T> failure() {
        return DefaultServiceResult.failure();
    }

    static <T> ServiceResult<T> failure(final ValidationMessage validationMessage) {
        return DefaultServiceResult.failure(validationMessage);
    }

    static <T> ServiceResult<T> failure(MessageType errorType) {
        return DefaultServiceResult.failure(errorType);
    }

    static <T> ServiceResult<T> failureWithFormattedError(final MessageType errorType, final String messageParam) {
        return DefaultServiceResult.failureWithFormattedError(errorType, new Object[] { messageParam });
    }

}
