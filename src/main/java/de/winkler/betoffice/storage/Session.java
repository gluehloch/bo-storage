/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2015 by Andre Winkler. All rights reserved.
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

import java.util.Date;

/**
 * Holds user session data.
 *
 * @author by Andre Winkler
 */
public class Session {

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
     * @param value
     *            Der Primärschlüssel.
     */
    protected void setId(final Long value) {
        id = value;
    }

    // -- nickname ------------------------------------------------------------

    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String value) {
        nickname = value;
    }

    // -- token ---------------------------------------------------------------

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String value) {
        token = value;
    }

    // -- login ---------------------------------------------------------------

    private Date login;

    public Date getLogin() {
        return login;
    }

    public void setLogin(Date date) {
        login = date;
    }

    // -- logout --------------------------------------------------------------

    private Date logout;

    public Date getLogout() {
        return logout;
    }

    public void setLogout(Date value) {
        logout = value;
    }

    // -- remoteAddress -------------------------------------------------------

    private String remoteAddress;

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String ip) {
        remoteAddress = ip;
    }

    // -- browser -------------------------------------------------------------

    private String browser;

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String value) {
        browser = value;
    }

    // -- failedLogins --------------------------------------------------------

    private int failedLogins;

    public int getFailedLogins() {
        return failedLogins;
    }

    public void setFailedLogins(int value) {
        failedLogins = value;
    }

    // -- user ----------------------------------------------------------------

    /** Der eingeloggte Spieler. */
    private User user;

    /**
     * Liefert den zugehoerigen Teilnehmer.
     *
     * @return Der Teilnehmer.
     *
     * @hibernate.many-to-one column="bo_user_ref" cascade="none"
     */
    public User getUser() {
        return user;
    }

    /**
     * Setzt den User für diesen GameTipp.
     *
     * @param value
     *            Der neue User.
     * @throws IllegalArgumentException
     *             value als null-Parameter übergeben.
     */
    public void setUser(final User value) {
        user = value;
    }

}
