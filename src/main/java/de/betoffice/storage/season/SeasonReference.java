/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2022 by Andre Winkler. All
 * rights reserved.
 * ============================================================================
 * GNU GENERAL PUBLIC LICENSE TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND
 * MODIFICATION
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

package de.betoffice.storage.season;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

@Embeddable
public class SeasonReference {

    /** Bezeichner Jahrgang/Datum der Saison. */
    @NotNull
    @Column(name = "bo_year")
    private String year;

    /** Bezeichner der Saison. */
    @NotNull
    @Column(name = "bo_name")
    private String name;

    protected SeasonReference() {
    }
    
    private SeasonReference(String year, String name) {
        this.year = year;
        this.name = name;
    }
    
    public static SeasonReference of(String year, String name) {
        return new SeasonReference(year, name);
    }
    
    // -- year ----------------------------------------------------------------

    /**
     * Jahrgang der Spielzeit (Format: 2001/2002).
     * 
     * @return Jahrgang der Spielzeit.
     */
    public String getYear() {
        return year;
    }

    /**
     * Setzt den Jahrgang.
     * 
     * @param value Der Jahrgang.
     */
    public void setYear(final String value) {
        Objects.nonNull(value);
        year = value.trim();
    }

    // -- name ----------------------------------------------------------------

    /**
     * Bezeichnung der Spielzeit (z.B. 1. Bundesliga).
     * 
     * @return Bezeichnung der Spielzeit.
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen der Spielzeit.
     * 
     * @param value Name der Spielzeit.
     */
    public void setName(final String value) {
        Objects.nonNull(value);
        name = value.trim();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, year);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SeasonReference other = (SeasonReference) obj;
        return Objects.equals(name, other.name) && Objects.equals(year, other.year);
    }

    @Override
    public String toString() {
        return "SeasonReference [year=" + year + ", name=" + name + "]";
    }

}
