/*
 * $Id: TippStatusType.java 3782 2013-07-27 08:44:32Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2011 by Andre Winkler. All rights reserved.
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

package de.winkler.betoffice.storage.enums;

/**
 * Repräsentiert den GameTipp-Zustand eines Tipps. Ist er ungültig
 * (INVALID), ein normaler Tipp eines Spielers (USERTIPP), ein
 * automatisch generierter Tipp für einen Automaten-Tipper (AUTO), ein
 * automatisch generierter MIN-Tipp für einen Mitspieler (MINTIPP) für
 * eine vergessene Tippabgabe oder etwa im Zustand undefiniert
 * (UNDEFINED).
 *
 * @author  $Author: andrewinkler $
 * @version $Revision: 3782 $ $Date: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
public enum TippStatusType {

    /** Enumeration INVALID. Ein ungültiger Tipp. Wird nicht gezählt. */
    INVALID("INVALID"),
    
    /** Enumeration USERTIPP. Eine normaler Tipp. */
    USER("USERTIPP"),
    
    /** Enumeration AUTO. Eine generierter Tipp von einem Automaten. */
    AUTO("AUTOTIPP"),
    
    /** Enumeration MINTIPP. Ein generierter Tipp von einem Automaten */
    MIN("MIN"),
    
    /** Enumeration UNDEFINED */
    UNDEFINED("UNDEFINED");
    
    private final String name;

    private TippStatusType(final String _name) {
        name = _name;
    }

    public String toString() {
        return name;
    }
   
}
