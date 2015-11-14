/*
 * $Id: UserSeason.java 3782 2013-07-27 08:44:32Z andrewinkler $
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

/**
 * Verwaltet die Beziehung zwischen <code>User</code> und <code>Season</code>.
 *
 * @author  $Author: andrewinkler $
 * @version $Revision: 3782 $ $Date: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 *
 * @hibernate.class table="bo_user_season"
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

    // -- season --------------------------------------------------------------

    /** Der Name der Eigenschaft 'saison'. */
    public static final String PROPERTY_SEASON = "season";

    /** Die zugeordnete Saison. */
    private Season season;

    /**
     * Liefert die zugeordnete Saison.
     *
     * @return Die Saison.
     *
     * @hibernate.many-to-one
     *     column="bo_season_ref"
     *     cascade="none"
     *     not-null="true"
     *     class="de.winkler.betoffice.storage.Season"
     */
    public Season getSeason() {
        return season;
    }

    /**
     * Setzt die Saison.
     *
     * @param value Eine Saison.
     */
    protected void setSeason(final Season value) {
        season = value;
    }

    // -- user ----------------------------------------------------------------

    /** Der Name der Eigenschaft 'user'. */
    public static final String PROPERTY_USER = "user";

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
     * @param value Der Teilnehmer.
     *
     * @hibernate.many-to-one
     *     column="bo_user_ref"
     *     cascade="none"
     *     not-null="true"
     *     class="de.winkler.betoffice.storage.User"
     */
    public void setUser(final User value) {
        user = value;
    }

    // -- wager ---------------------------------------------------------------

    /** Der Name der Eigenschaft 'wager'. */
    public static final String PROPERTY_WAGER = "wager";

    /** Der Wetteinsatz. */
    private int wager;

    /**
     * Liefert den Wetteinsatz.
     *
     * @return Der Wetteinsatz.
     *
     * @hibernate.property column="bo_wager"
     */
    public int getWager() {
        return wager;
    }

    /**
     * Setzt den Wetteinsatz.
     *
     * @param value Der Wetteinsatz.
     */
    public void setWager(int value) {
        wager = value;
    }

    // ------------------------------------------------------------------------

    /**
     * @return valid?
     * 
     * @see de.winkler.betoffice.storage.StorageObject#isValid()
     */
    public boolean isValid() {
        if ((user != null) && (season != null)) {
            return true;
        } else {
            return false;
        }
    }

    // -- Object --------------------------------------------------------------

    /**
     * @see Object#toString()
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("User: ").append(getUser());
        buf.append(", Season: ").append(getSeason());
        return buf.toString();
    }

//    /**
//     * @see Object#equals(java.lang.Object)
//     */
//    public final boolean equals(final Object object) {
//        if (object == null) {
//            return false;
//        } else if (!(object instanceof UserSeason)) {
//            return false;
//        } else if ((getSeason().equals(((UserSeason) object).getSeason()))
//                && (getUser().equals(((UserSeason) object).getUser()))) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    /**
//     * @see Object#hashCode()
//     */
//    public final int hashCode() {
//        int result = 17;
//        result = 37 * result + getSeason().hashCode();
//        result = 37 * result + getUser().hashCode();
//        return result;
//    }

}
