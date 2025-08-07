/*
 * $Id: GroupType.java 3782 2013-07-27 08:44:32Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2022 by Andre Winkler. All rights reserved.
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

package de.betoffice.storage.group.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;

import de.betoffice.storage.AbstractStorageObject;
import de.betoffice.storage.season.GroupTypeEnum;

/**
 * Beschreibt einen Gruppentyp (1. Liga, 2. Liga, Regionalliga Nord, etc.).
 */
@Entity
@Table(name = "bo_grouptype")
public class GroupType extends AbstractStorageObject implements Comparable<GroupType> {

    /** serial version */
    private static final long serialVersionUID = -8513852915020891940L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @NotNull
    @Column(name = "bo_name")
    private String name;

    @NotNull
    @Column(name = "bo_type")
    @Enumerated
    private GroupTypeEnum type = GroupTypeEnum.LEAGUE; 

    // ------------------------------------------------------------------------

    public static GroupType of(String groupTypeName) {
        GroupType groupType = new GroupType();
        groupType.setName(groupTypeName);
        return groupType;
    }

    // -- id ------------------------------------------------------------------

    public Long getId() {
        return id;
    }

    protected void setId(final Long value) {
        id = value;
    }

    // -- name ----------------------------------------------------------------

    public String getName() {
        return name;
    }

    public void setName(final String value) {
        name = value;
    }

    // -- type ----------------------------------------------------------------

    public GroupTypeEnum getType() {
        return type;
    }


    public void setType(GroupTypeEnum type) {
        this.type = type;
    }

    // -- StorageObject -------------------------------------------------------

    /**
     * Prüft, ob die Eigenschaften dieses Objekts komplett und gültig gefüllt sind,
     * damit es evt. Weiterverarbeitungen erfahren kann. Folgende Eigenschaften
     * müssen gesetzt sein:
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
