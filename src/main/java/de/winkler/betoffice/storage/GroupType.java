/*
 * $Id: GroupType.java 3782 2013-07-27 08:44:32Z andrewinkler $
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

import org.apache.commons.lang.StringUtils;

/**
 * Beschreibt einen Gruppentyp (1. Liga, 2. Liga, Regionalliga Nord, etc.).
 * 
 * @author $Author: andrewinkler $
 * @version $Revision: 3782 $ $Date: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul
 *          2013) $
 *
 * @hibernate.class table="bo_grouptype"
 */
public class GroupType extends AbstractStorageObject implements
        Comparable<GroupType> {

    /** serial version */
    private static final long serialVersionUID = -8513852915020891940L;

    // -- id ------------------------------------------------------------------

    /** Der Primärschlüssel. */
    private Long id;

    /**
     * Liefert den Primärschlüssel.
     *
     * @return Der Primärschlüssel.
     *
     * @hibernate.id generator-class="native"
     */
    public Long getId() {
        return id;
    }

    /**
     * Setzt den Primärschlüssel.
     *
     * @param value
     *            Der Primärschlüssel.
     */
    protected void setId(final Long value) {
        id = value;
    }

    // -- name ----------------------------------------------------------------

    /** Der Name der Gruppe. */
    private String name;

    /**
     * Liest den Namen der Gruppe.
     *
     * @return Name der Gruppe.
     *
     * @hibernate.property column="bo_name" not-null="true" unique="true"
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen der Gruppe.
     *
     * @param value
     *            Name der Gruppe.
     */
    public void setName(final String value) {
        name = value;
    }

    // -- StorageObject -------------------------------------------------------

    /**
     * Prüft, ob die Eigenschaften dieses Objekts komplett und gültig gefüllt
     * sind, damit es evt. Weiterverarbeitungen erfahren kann. Folgende
     * Eigenschaften müssen gesetzt sein:
     * <ul>
     * <li>name</li>
     * </ul>
     *
     * @return true, Objekt in Ordnung; false, es ist was falsch.
     */
    public boolean isValid() {
        if (StringUtils.isBlank(name)) {
            return false;
        } else {
            return true;
        }
    }

    // -- Comparable ----------------------------------------------------------

    /**
     * Vergleicht den Gruppennamen.
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(final GroupType o) {
        return (getName().compareTo(o.getName()));
    }

    // -- Object --------------------------------------------------------------

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) {
            return false;
        } else if (!(object instanceof GroupType)) {
            return false;
        } else {
            GroupType groupType = (GroupType) object;
            if (groupType.getName().equals(getName())) {
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + getName().hashCode();
        return result;
    }

}
