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

package de.betoffice.validation;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;

import de.betoffice.validation.ValidationMessage.MessageType;

/**
 * Service Result.
 *
 * @author Andre Winkler
 */
class DefaultServiceResult<T> implements ServiceResult<T> {

    private final T result;
    private final ValidationMessages validationMessages;

    private DefaultServiceResult(T result) {
        this.result = result;
        this.validationMessages = ValidationMessages.empty();
    }

    private DefaultServiceResult(T result, ValidationMessages validationMessages) {
        this.result = result;
        this.validationMessages = validationMessages;
    }

    private DefaultServiceResult(ValidationMessage validationMessage) {
        this.result = null;
        this.validationMessages = ValidationMessages.of(validationMessage);
    }

    private DefaultServiceResult(List<ValidationMessage> validationMessages) {
        this.result = null;
        this.validationMessages = ValidationMessages.of(validationMessages);
    }

    @Override
    public Optional<T> result() {
        return Optional.ofNullable(result);
    }

    @Override
    public ValidationMessages messages() {
        return validationMessages;
    }

    static <T> ServiceResult<T> sucess(T result) {
        return new DefaultServiceResult<T>(result);
    }

    static <T> ServiceResult<T> failure() {
        return new DefaultServiceResult<T>(ValidationMessage.error());
    }

    static <T> ServiceResult<T> failure(final ValidationMessage validationMessage) {
        if (validationMessage.isOk()) {
            throw new IllegalArgumentException(
                    "The validationMessage has severity OK, but you want to create a failure.");
        }
        return new DefaultServiceResult<T>(validationMessage);
    }

    static <T> ServiceResult<T> failure(MessageType errorType) {
        return new DefaultServiceResult<T>(ValidationMessage.error(errorType));
    }

    static <T> ServiceResult<T> failureWithFormattedError(MessageType errorType, Object... messageParams) {
        return new DefaultServiceResult<T>(ValidationMessage.error(errorType, messageParams));
    }

    @Override
    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (result != null) {
            return result;
        } else {
            throw exceptionSupplier.get();
        }
    }

    @Override
    public T orElseThrow() {
        if (result != null) {
            return result;
        }
        throw new NoSuchElementException("No value present");
    }

    @Override
    public boolean containsAnError() {
        return this.validationMessages.containsAnError();
    }

    @Override
    public boolean isSuccessful() {
        return this.result != null && !this.containsAnError();
    }

}
