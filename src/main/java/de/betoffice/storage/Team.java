/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2023 by Andre Winkler. All
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

package de.betoffice.storage;

import de.betoffice.storage.enums.TeamType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Kapselt die Teamdaten.
 *
 * @author by Andre Winkler
 */
@Entity
@Table(name = "bo_team")
public class Team extends AbstractStorageObject {

    /** serial version id */
    private static final long serialVersionUID = -3181346057831881080L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "bo_name")
    private String name;

    @Column(name = "bo_longname")
    private String longName;

    @Column(name = "bo_shortname")
    private String shortName;

    @Column(name = "bo_xshortname")
    private String xshortName;

    /** Die ID / der Name des Logo-Ressource. */
    @Column(name = "bo_logo")
    private String logo;

    @Column(name = "bo_teamtype")
    @Enumerated
    private TeamType teamType = TeamType.DFB;

    @Column(name = "bo_openligaid")
    private Long openligaid;

    /** Heimspiel Stadion */
    @ManyToOne
    @JoinColumn(name = "bo_location_ref")
    private Location location;

    // -- Construction --------------------------------------------------------

    /**
     * Defaultkonstruktor.
     */
    public Team() {
    }

    /**
     * Konstruktor. Erstellt ein minimales Team Objekt.
     *
     * @param value Der Mannschaftsname
     */
    public Team(String value) {
        this(value, null, null);
    }

    public Team(String _name, String _longName, String _logo) {
        setName(_name);
        setLongName(_longName);
        setLogo(_logo);
    }

    public static class TeamBuilder {
        private String name;
        private String longName;
        private String logoResource;
        private TeamType teamType;

        public static TeamBuilder team(String name) {
            var tb = new TeamBuilder();
            tb.name = name;
            return tb;
        }

        public TeamBuilder longName(String longName) {
            this.longName = longName;
            return this;
        }

        public TeamBuilder logo(String logoResource) {
            this.logoResource = logoResource;
            return this;
        }

        public TeamBuilder type(TeamType teamType) {
            this.teamType = teamType;
            return this;
        }

        public Team build() {
            var team = new Team();
            team.setName(name);
            team.setLongName(longName);
            team.setLogo(logoResource);
            if (teamType != null) {
                team.setTeamType(teamType);
            }
            return team;
        }
    }

    // -- id ------------------------------------------------------------------

    /**
     * Liefert den Primärschlüssel.
     *
     * @return       Der Primärschlüssel.
     *
     * @hibernate.id generator-class="native"
     */
    public Long getId() {
        return id;
    }

    /**
     * Setzt den Primärschlüssel.
     *
     * @param value Der Primärschlüssel.
     */
    protected void setId(final Long value) {
        id = value;
    }

    // -- name ----------------------------------------------------------------

    /**
     * Liefert den Teamnamen.
     *
     * @return             Der Teamname.
     *
     * @hibernate.property column="bo_name" not-null="true" unique="true"
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Teamnamen.
     *
     * @param value Der Teamname.
     */
    public void setName(final String value) {
        name = value;
    }

    // -- longName ------------------------------------------------------------

    /**
     * Liefert den Teamnamen in der Langbezeichnung.
     *
     * @return             Der lange Teamname.
     *
     * @hibernate.property column="bo_longname"
     */
    public String getLongName() {
        return longName;
    }

    /**
     * Setzt den Teamnamen in der Langbezeichnung.
     *
     * @param value Der lange Teamname.
     */
    public void setLongName(final String value) {
        longName = value;
    }

    // -- shortName -----------------------------------------------------------

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String value) {
        shortName = value;
    }

    // -- xshortName -----------------------------------------------------

    public String getXshortName() {
        return xshortName;
    }

    public void setXshortName(String value) {
        xshortName = value;
    }

    // -- logo ----------------------------------------------------------------

    /**
     * Liefert das Logo.
     *
     * @return             Das Logo.
     */
    public String getLogo() {
        return logo;
    }

    /**
     * Setzt das Logo.
     *
     * @param value Das Logo.
     */
    public void setLogo(final String value) {
        logo = value;
    }

    // -- teamType ------------------------------------------------------------

    /**
     * Liefert den Mannschaftstyp.
     *
     * @return             Der Modus.
     */
    public TeamType getTeamType() {
        return teamType;
    }

    /**
     * Setzt den Mannschaftstyp.
     *
     * @param value Der Mannschaftstyp.
     */
    public void setTeamType(final TeamType value) {
        teamType = value;
    }

    // -- location ------------------------------------------------------------

    /**
     * Liefert das Heimstadion.
     * 
     * @return Das Heimspielstadion.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Setzt das Heimspielstadion.
     * 
     * @param value Das Heimspielstadion.
     */
    public void setLocation(Location value) {
        location = value;
    }

    // -- openligaid ----------------------------------------------------------

    /**
     * Get openligadb ID.
     *
     * @return The openligadb ID
     */
    public Long getOpenligaid() {
        return openligaid;
    }

    /**
     * Set openligadb ID
     *
     * @param value The openligadb ID
     */
    public void setOpenligaid(Long value) {
        openligaid = value;
    }

    // -- Object --------------------------------------------------------------

    /**
     * @see Object#toString()
     */
    public String toString() {
        return longName;
    }

    /**
     * @see Object#equals(java.lang.Object)
     */
    public boolean equals(final Object object) {
        if (object == null) {
            return false;
        } else if (!(object instanceof Team)) {
            return false;
        } else {
            Team team = (Team) object;
            return (team.getName().equals(getName()));
        }
    }

    /**
     * @see Object#hashCode()
     */
    public int hashCode() {
        int result = 17;
        result = 37 * result + getName().hashCode();
        return result;
    }

}
