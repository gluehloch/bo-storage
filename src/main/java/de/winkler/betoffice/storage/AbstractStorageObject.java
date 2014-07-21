/*
 * $Id: AbstractStorageObject.java 3782 2013-07-27 08:44:32Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2010 by Andre Winkler. All rights reserved.
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

package de.winkler.betoffice.storage;

import java.io.Serializable;

import org.slf4j.Logger;

import de.awtools.basic.LoggerFactory;
import de.winkler.betoffice.storage.exception.StorageObjectNotValidException;

/**
 * Abstrakte Oberklasse für StorageObjects. Implementiert die Schnittstelle
 * StorageObject.
 * 
 * @author $Author: andrewinkler $
 * @version $Revision: 3782 $ $Date: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
public abstract class AbstractStorageObject implements StorageObject,
        Serializable {

    /** serial version id */
    private static final long serialVersionUID = -6571773252125809193L;

    private final Logger log = LoggerFactory.make();

    /**
     * Defaultkonstruktor. Trägt das StorageObject in den
     * <code>StorageObserver</code> ein.
     */
    AbstractStorageObject() {
        if (log.isDebugEnabled()) {
            log.debug("Construction: " + this.getClass().getName());
        }
    }

    /**
     * Prüft, ob die Eigenschaften dieses Objekts komplett und gültig gefüllt
     * sind. Ist dies nicht der Fall, wird eine StorageObjectNotValidException
     * geworfen.
     * 
     * @throws StorageObjectNotValidException
     */
    public final void validate() throws StorageObjectNotValidException {
        if (!(isValid())) {
            throw new StorageObjectNotValidException(this);
        }
    }

}
