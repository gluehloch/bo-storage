/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2020 by Andre Winkler. All rights reserved.
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

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Verwaltet die Daten einer Gruppe.
 *
 * @author Andre Winkler
 *
 * @hibernate.class table="bo_group"
 */
@Entity
@Table(name = "bo_group")
public class Group extends AbstractStorageObject {

    /** serial version */
    private static final long serialVersionUID = 2621079132943084772L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bo_season_ref")
    private Season season;

    // Die N:M Mittlertabelle bo_team(id) <-> bo_team_group(bo_team_ref, bo_group_ref) <-> bo_group(id)
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "bo_team_group", 
        joinColumns = @JoinColumn(name = "bo_group_ref"), // FK column which references bo_group#id
        inverseJoinColumns = @JoinColumn(name = "bo_team_ref")) // FK column reverse side bo_team#id
    private Set<Team> teams = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "bo_grouptype_ref")
    private GroupType groupType;

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

    // -- seasons -------------------------------------------------------------

    /**
     * Liefert die Saison zu dieser Gruppe.
     *
     * @return Die Saison zu dieser Gruppe.
     */
    public Season getSeason() {
        return season;
    }

    /**
     * Setzt die Saison. Wird von {@link Season#addGroup(Group)} aufgerufen.
     *
     * @param value
     *            Eine Saison.
     */
    protected void setSeason(final Season value) {
        Objects.requireNonNull(value);
        season = value;
    }

    // -- teams ---------------------------------------------------------------

    /**
     * Liefert die Mannschaften dieser Gruppe.
     *
     * @return Die Mannschaften dieser Gruppe.
     */
    public Set<Team> getTeams() {
        return teams;
    }

    /**
     * Setzt die Mannschaften dieser Gruppe neu.
     *
     * @param value
     *            Die Mannschaften dieser Gruppe.
     */
    protected void setTeams(final Set<Team> value) {
        teams = value;
    }

    /**
     * Fügt eine Mannschaft der Gruppe hinzu.
     *
     * @param value
     *            Eine Mannschaft.
     */
    public void addTeam(final Team value) {
        Objects.requireNonNull(value);
        teams.add(value);
    }

    /**
     * Entfernt eine Mannschaft aus der Gruppe.
     *
     * @param value
     *            Eine Mannschaft.
     */
    public void removeTeam(final Team value) {
        Objects.requireNonNull(value);
        teams.remove(value);
    }

    /**
     * Prüft, ob das angegebene Team in dieser Saison zu dieser Gruppe gehört.
     *
     * @param team
     *            Das zu untersuchende Team.
     * @return true, gehört zu Gruppe; false sonst.
     */
    public boolean isGroupMember(final Team team) {
        return teams.contains(team);
    }

    // -- type ----------------------------------------------------------------

    /**
     * Liefert den Gruppentyp.
     *
     * @return Der Gruppentyp.
     */
    public GroupType getGroupType() {
        return groupType;
    }

    /**
     * Setzt den Gruppentyp.
     *
     * @param value
     *            Der Gruppentyp.
     */
    public void setGroupType(final GroupType value) {
        groupType = value;
    }

    // -- StorageObject -------------------------------------------------------

    /**
     * Prüft, ob die Eigenschaften dieses Objekts komplett und gültig gefüllt sind,
     * damit es evt. Weiterverarbeitungen erfahren kann. Folgende Eigenschaften
     * müssen gesetzt sein:
     * <ul>
     * <li>groupType</li>
     * <li>season</li>
     * </ul>
     *
     * @return true, Objekt in Ordnung; false, es ist was falsch.
     */
    public boolean isValid() {
        if (getGroupType() == null) {
            return false;
        } else if (getSeason() == null) {
            return false;
        } else {
            return true;
        }
    }

    // -- Object --------------------------------------------------------------

    public String debug() {
        return getGroupType().getName();
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) {
            return false;
        } else if (!(object instanceof Group)) {
            return false;
        } else {
            Group group = (Group) object;
            return Objects.equals(getSeason(), group.getSeason())
                    && Objects.equals(getGroupType(), group.getGroupType());
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGroupType(), getSeason());
    }

}
