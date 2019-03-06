/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2014 by Andre Winkler. All
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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Holds the location of a footbal stadium
 *
 * @author by Andre Winkler
 */
@Entity
@Table(name = "bo_location")
public class Location extends AbstractStorageObject {

    private static final long serialVersionUID = -5731266845767848350L;

    /** The unknown location or default/undefined location. */
    public static final long UNKNOWN_LOCATION_ID = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "city")
    private String city;

    @Column(name = "geodat")
    private String geodat;

    @Column(name = "openligaid")
    private Long openligaid;

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
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city
     *            the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the geodat
     */
    public String getGeodat() {
        return geodat;
    }

    /**
     * @param geodat
     *            the geodat to set
     */
    public void setGeodat(String geodat) {
        this.geodat = geodat;
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
        return "Location [id=" + id + ", name=" + name + ", city=" + city
                + ", geodat=" + geodat + ", openligaid=" + openligaid + "]";
    }

}
