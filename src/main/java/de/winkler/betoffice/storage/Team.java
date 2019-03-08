/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2017 by Andre Winkler. All
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

package de.winkler.betoffice.storage;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.winkler.betoffice.storage.enums.TeamType;

/**
 * Kapselt die Teamdaten.
 *
 * @author by Andre Winkler
 *
 * @hibernate.class table="bo_team"
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

    // @formatter:off
    // Die N:M Mittlertabelle bo_team(id) <-> bo_team_group(bo_team_ref, bo_group_ref) <-> bo_group(id)
    @ManyToMany
    @JoinTable(name = "bo_team_group",
        joinColumns = @JoinColumn(name = "bo_team_ref"), // FK column which references bo_team#id
        inverseJoinColumns = @JoinColumn(name = "bo_group_ref")) // FK column reverse side bo_group#id
    private Set<Group> groups = new HashSet<>();
    // @formatter:on

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
     * @param value
     *            Der Mannschaftsname
     */
    public Team(final String value) {
        this(value, null, null);
    }

    /**
     * Konstruktor.
     *
     * @param _name
     *            Der Mannschaftsname.
     * @param _longName
     *            Der Mannschaftsname komplett/in Langform.
     * @param _logo
     *            Das Logo/Bezeichner der Logo-Datei.
     */
    public Team(final String _name, final String _longName,
            final String _logo) {
        setName(_name);
        setLongName(_longName);
        setLogo(_logo);
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

    // -- name ----------------------------------------------------------------

    /**
     * Liefert den Teamnamen.
     *
     * @return Der Teamname.
     *
     * @hibernate.property column="bo_name" not-null="true" unique="true"
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Teamnamen.
     *
     * @param value
     *            Der Teamname.
     */
    public void setName(final String value) {
        name = value;
    }

    // -- longName ------------------------------------------------------------

    /**
     * Liefert den Teamnamen in der Langbezeichnung.
     *
     * @return Der lange Teamname.
     *
     * @hibernate.property column="bo_longname"
     */
    public String getLongName() {
        return longName;
    }

    /**
     * Setzt den Teamnamen in der Langbezeichnung.
     *
     * @param value
     *            Der lange Teamname.
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
     * @return Das Logo.
     *
     * @hibernate.property column="bo_logo"
     */
    public String getLogo() {
        return logo;
    }

    /**
     * Setzt das Logo.
     *
     * @param value
     *            Das Logo.
     */
    public void setLogo(final String value) {
        logo = value;
    }

    // -- groups --------------------------------------------------------------

    /**
     * Liefert die Gruppen, denen diese Mannschaft angehört.
     *
     * @return Die Gruppen.
     *
     * @hibernate.set role="groups" table="bo_team_group" cascade="none"
     *                inverse="true"
     * @hibernate.collection-key column="bo_team_ref"
     * @hibernate.collection-many-to-many class="de.winkler.betoffice.storage.Group"
     *                                    column="bo_group_ref"
     */
    public Set<Group> getGroups() {
        return groups;
    }

    /**
     * Setzt die Gruppen, denen diese Mannschaft angehört neu.
     *
     * @param value
     *            Die Gruppen.
     */
    protected void setGroups(final Set<Group> value) {
        groups = value;
    }

    /**
     * Ordnet die Mannschaft einer bestimmten Gruppe zu. Wird aus der Methode
     * {@link Group#addTeam(Team)} aufgerufen.
     *
     * @param value
     *            Die zugeordnete Gruppe.
     */
    protected void addGroup(final Group value) {
        groups.add(value);
    }

    /**
     * Entfernt eine Mannschaft aus einer bestimmten Gruppe. Wird aus der
     * Methode {@link Group#removeTeam(Team)} aufgerufen.
     *
     * @param value
     *            Die zugeordnete Gruppe.
     */
    protected void removeGroup(final Group value) {
        groups.remove(value);
    }

    // -- teamType ------------------------------------------------------------

    /**
     * Liefert den Mannschaftstyp.
     *
     * @return Der Modus.
     *
     * @hibernate.property column="bo_teamtype"
     *                     type="de.winkler.betoffice.storage.enums.TeamType"
     *                     not-null="true"
     */
    public TeamType getTeamType() {
        return teamType;
    }

    /**
     * Setzt den Mannschaftstyp.
     *
     * @param value
     *            Der Mannschaftstyp.
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
     * @param value
     *            Das Heimspielstadion.
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
     * @param value
     *            The openligadb ID
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
