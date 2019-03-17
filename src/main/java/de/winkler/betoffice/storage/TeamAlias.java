/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2019 by Andre Winkler. All rights reserved.
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Verwaltet die Alias-Namen von Mannschaften.
 *
 * @author by Andre Winkler
 */
@Entity
@Table(name = "bo_teamalias")
public class TeamAlias extends AbstractStorageObject {

    /** serial version id */
    private static final long serialVersionUID = -1276165156151976261L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "bo_aliasname")
    private String aliasName;

    @ManyToOne
    @JoinColumn(name = "bo_team_ref")
    private Team team;

    // -- Construction --------------------------------------------------------

    /**
     * Defaultkonstruktor.
     */
    public TeamAlias() {
    }

    // -- id ------------------------------------------------------------------

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

    // -- StorageObject -------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see de.winkler.betoffice.storage.StorageObject#isValid()
     */
    public boolean isValid() {
        return true;
    }

    // -- aliasName -----------------------------------------------------------

    /**
     * Liefert den Alias Namen.
     *
     * @return Der Alias Teamname.
     *
     * @hibernate.property column="bo_aliasName" not-null="true" unique="true"
     */
    public String getAliasName() {
        return aliasName;
    }

    /**
     * Setzt den Alias Teamnamen.
     *
     * @param value
     *            Der Alias Teamname.
     */
    public void setAliasName(final String value) {
        aliasName = value;
    }

    // -- team ----------------------------------------------------------------
    
    /**
     * Liefert die zugeordnete Mannschaft.
     *
     * @return Eine Mannschaft.
     */
    public Team getTeam() {
        return team;
    }

    /**
     * Setzt die zugeordnete Mannschaft.
     *
     * @param value
     *            Eine Mannschaft.
     */
    public void setTeam(final Team value) {
        team = value;
    }

    // -- Object --------------------------------------------------------------

    /**
     * @see Object#toString()
     */
    public String toString() {
        return aliasName;
    }

    /**
     * @see Object#equals(java.lang.Object)
     */
    public boolean equals(final Object object) {
        if (object == null) {
            return false;
        } else if (!(object instanceof TeamAlias)) {
            return false;
        } else {
            TeamAlias team = (TeamAlias) object;
            return (team.getAliasName().equals(getAliasName()));
        }
    }

    /**
     * @see Object#hashCode()
     */
    public int hashCode() {
        int result = 17;
        result = 37 * result + getAliasName().hashCode();
        return result;
    }

}
