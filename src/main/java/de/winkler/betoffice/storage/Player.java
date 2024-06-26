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

package de.winkler.betoffice.storage;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * The player of a football match.
 *
 * @author by Andre Winkler
 */
@Entity
@Table(name = "bo_player")
public class Player extends AbstractStorageObject {

    private static final long serialVersionUID = 7264908918810836616L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "bo_name")
    private String name;

    @Column(name = "bo_vorname")
    private String vorname;

    @Column(name = "bo_openligaid")
    private Long openligaid;

    @OneToMany
    @JoinColumn(name = "bo_player_ref")
    private Set<Goal> goals = new HashSet<>();

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the vorname
     */
    public String getVorname() {
        return vorname;
    }

    /**
     * @param vorname
     *            the vorname to set
     */
    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    /**
     * @return the goals
     */
    public Set<Goal> getGoals() {
        return goals;
    }

    /**
     * @param goals
     *            the goals to set
     */
    public void setGoals(Set<Goal> goals) {
        this.goals = goals;
    }

    /**
     * @return the openligaid
     */
    public Long getOpenligaid() {
        return openligaid;
    }

    /**
     * @param openligaid
     *            the openligaid to set
     */
    public void setOpenligaid(Long openligaid) {
        this.openligaid = openligaid;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Player [id=" + id + ", name=" + name + ", vorname=" + vorname
                + ", openligaid=" + openligaid + "]";
    }

}
