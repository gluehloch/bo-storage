/*
 * $Id: StorageObjectNotFoundException.java 3782 2013-07-27 08:44:32Z andrewinkler $
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

/**
 * Ein Storage-Objekt konnte nicht gefunden werden. In solchen Fällen sollte
 * diese Exception geworfen werden.
 *
 * @author $Author: andrewinkler $
 * @version $Revision: 3782 $ $Date: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul
 *          2013) $
 */
public class StorageObjectNotFoundException extends StorageException {

    /** serial version id */
    private static final long serialVersionUID = -6163154649752002457L;

    public StorageObjectNotFoundException() {
        super();
    }

    public StorageObjectNotFoundException(Throwable ex) {
        super(ex);
    }

    public StorageObjectNotFoundException(String message) {
        super(message);
    }

    public StorageObjectNotFoundException(String message, Throwable ex) {
        super(message, ex);
    }

}
