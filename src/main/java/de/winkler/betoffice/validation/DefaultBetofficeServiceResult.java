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

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;

import de.winkler.betoffice.validation.BetofficeValidationMessage.ErrorType;

/**
 * Service Result.
 *
 * @author Andre Winkler
 */
class DefaultBetofficeServiceResult<T> implements BetofficeServiceResult<T> {

    private final T result;
    private final BetofficeValidationMessage validationMessage;

    private DefaultBetofficeServiceResult() {
        this.result = null;
        this.validationMessage = BetofficeValidationMessage.error();
    }

    private DefaultBetofficeServiceResult(T result) {
        this.result = result;
        this.validationMessage = BetofficeValidationMessage.ok();
    }

    @Override
    public Optional<T> result() {
        return Optional.ofNullable(result);
    }

    @Override
    public BetofficeValidationMessage message() {
        return validationMessage;
    }

    static <T> BetofficeServiceResult<T> sucess(T result) {
        return new DefaultBetofficeServiceResult<>(result);
    }

    static <T> BetofficeServiceResult<T> failure() {
        return new DefaultBetofficeServiceResult<>();
    }

    static <T> BetofficeServiceResult<T> failure(ErrorType errorType) {
        return new DefaultBetofficeServiceResult<>();
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

}
