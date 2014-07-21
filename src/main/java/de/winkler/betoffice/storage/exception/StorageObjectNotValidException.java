/*
 * $Id: StorageObjectNotValidException.java 3782 2013-07-27 08:44:32Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2007 by Andre Winkler. All rights reserved.
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

package de.winkler.betoffice.storage.exception;

import de.winkler.betoffice.storage.StorageObject;

/**
 * Diese Exception wird im Zusammenhang mit der <code>checkValidity()</code>.
 * der StorageObjects geworfen.
 *
 * @author  $Author: andrewinkler $
 * @version $Revision: 3782 $ $Date: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
public class StorageObjectNotValidException extends StorageRuntimeException {

    /** serial version id */
    private static final long serialVersionUID = -722656677279364482L;

    /** Das bereits ungültige StorageObject. */
    private StorageObject object;

    public StorageObjectNotValidException() {
        super();
    }

    /**
     * Konstruktor.
     *
     * @param obj Das ungültige StorageObject.
     */
    public StorageObjectNotValidException(final StorageObject obj) {
        super("Not valid: " + obj.toString());
        object = obj;
    }

    public StorageObjectNotValidException(final Throwable ex) {
        super(ex);
    }

    /**
     * Liefert das bereits existierende StorageObject.
     *
     * @return Das existierende StorageObject.
     */
    public StorageObject getObject() {
        return object;
    }

}
