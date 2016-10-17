/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2016 by Andre Winkler. All
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
 *
 */

package de.winkler.betoffice.storage;

import de.winkler.betoffice.storage.enums.RoleType;

/**
 * Verwaltet die Beziehung zwischen <code>User</code> und <code>Season</code>.
 *
 * @author Andre Winkler
 */
public class UserSeason extends AbstractStorageObject {

    /** serial version */
    private static final long serialVersionUID = -3145117873775029160L;

    // -- id ------------------------------------------------------------------

    /** Der Primärschlüssel. */
    private Long id;

    /**
     * Liefert den Primärschlüssel.
     *
     * @return Der Primärschlüssel.
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

    // -- season --------------------------------------------------------------

    /** Die zugeordnete Saison. */
    private Season season;

    /**
     * Liefert die zugeordnete Saison.
     *
     * @return Die Saison.
     */
    public Season getSeason() {
        return season;
    }

    /**
     * Setzt die Saison.
     *
     * @param value
     *            Eine Saison.
     */
    protected void setSeason(final Season value) {
        season = value;
    }

    // -- user ----------------------------------------------------------------

    /** Der zugeordnete Teilnehmer. */
    private User user;

    /**
     * Liefert den Teilnehmer.
     *
     * @return Der Teilnehmer.
     */
    public User getUser() {
        return user;
    }

    /**
     * Setzt den Teilnehmer.
     *
     * @param value
     *            Der Teilnehmer.
     */
    public void setUser(final User value) {
        user = value;
    }

    // -- wager ---------------------------------------------------------------

    /** Der Wetteinsatz. */
    private int wager;

    /**
     * Liefert den Wetteinsatz.
     *
     * @return Der Wetteinsatz.
     */
    public int getWager() {
        return wager;
    }

    /**
     * Setzt den Wetteinsatz.
     *
     * @param value
     *            Der Wetteinsatz.
     */
    public void setWager(int value) {
        wager = value;
    }

    // -- roleType ------------------------------------------------------------

    /** The role for an user/season relation. Tipper is the default. */
    private RoleType roleType = RoleType.TIPPER;

    /**
     * Gets the user role for a season.
     * 
     * @return the user role
     */
    public RoleType getRoleType() {
        return roleType;
    }

    /**
     * Set the user role for a season.
     * 
     * @param roleType
     *            role type for a user/season relation.
     */
    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    // -- Object -------------------------------------------------------------

    @Override
    public String toString() {
        return "UserSeason [id=" + id + ", season=" + season + ", user=" + user
                + ", wager=" + wager + ", roleType=" + roleType + "]";
    }

}
