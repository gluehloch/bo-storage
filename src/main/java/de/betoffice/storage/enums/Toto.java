/*
 * $Id: Toto.java 3782 2013-07-27 08:44:32Z andrewinkler $
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

package de.betoffice.storage.enums;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * Enumeration Toto.
 * 
 * @author $Author: andrewinkler $
 * @version $Revision: 3782 $ $Date: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul
 *          2013) $
 */
public final class Toto implements Comparable<Toto>, Serializable {

    /** serial version id */
    private static final long serialVersionUID = 2041903623274561703L;

    /** Der Name der Enumeration. */
    private transient String name;

    /** Der Toto-Wert. */
    private transient int toto;

    /** Erzeuge Ordinalzahl der nächsten Enumeration. */
    private static int nextOrdinal = 0;

    /** Ordinalwert dieser Enumeration. */
    private final int ordinal = nextOrdinal++;

    /** Enumeration EQUAL */
    public static final Toto HOME_WIN = new Toto("HOME_WIN", 1);

    /** Enumeration TOTO */
    public static final Toto HOME_LOST = new Toto("HOME_LOST", 2);

    /** Enumeration ZERO */
    public static final Toto REMIS = new Toto("REMIS", 0);

    /** Enumeration UNDEFINED */
    public static final Toto UNDEFINED = new Toto("UNDEFINED", -1);

    /** Ein Array mit allen Enumerations. */
    private static final Toto[] PRIVATE_VALUES = { HOME_WIN, HOME_LOST, REMIS,
            UNDEFINED };

    /**
     * Erzeugt ein Toto.
     *
     * @param name
     *            Name der Aufzählung.
     * @param toto
     *            Der Toto-Wert der Aufzählung.
     */
    private Toto(String name, int toto) {
        this.name = name;
        this.toto = toto;
    }

    /**
     * Der Name der Enumeration.
     *
     * @return Der Name.
     */
    public String getName() {
        return name;
    }

    /**
     * Liefert den Ordinalwert. Nicht zu verwechseln mit den Toto Werten 1,0 und
     * 2.
     *
     * @return Ordinalwert.
     */
    public int getIntValue() {
        return ordinal;
    }

    /**
     * Vergleicht zwei TotoResults miteinander.
     *
     * @param obj
     *            Der zu vergleichende TotoResult.
     * @return Siehe {@link Comparable#compareTo(java.lang.Object)}.
     */
    public int compareTo(Toto obj) {
        return (ordinal - obj.ordinal);
    }

    /**
     * Methode für die Deserialisierung dieser Klasse. Verhindert das nach der
     * Deserialisierung doppelte Konstanten nebeneinander existieren.
     *
     * @return Ein Objekt.
     * @throws ObjectStreamException
     *             Da ging was schief.
     */
    private Object readResolve() throws ObjectStreamException {
        return PRIVATE_VALUES[ordinal];
    }

    /**
     * Liefert den Toto-Wert der Aufzählung.
     *
     * @return Der Toto-Wert.
     */
    public int intValue() {
        return toto;
    }

    // -- Object --------------------------------------------------------------

    public String toString() {
        return Integer.toString(toto);
    }

    /**
     * Liefert eine Kopie des internen Arrays.
     *
     * @return Ein Kopie des internen Arrays.
     */
    public static Object[] toArray() {
        Toto[] result = new Toto[PRIVATE_VALUES.length];
        System.arraycopy(PRIVATE_VALUES, 0, result, 0, result.length);
        return result;
    }

}
