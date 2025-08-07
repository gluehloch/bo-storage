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

package de.betoffice.storage;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

import de.betoffice.storage.user.entity.User;

/**
 * Holds user session data.
 *
 * @author by Andre Winkler
 */
@Entity
@Table(name = "bo_session")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "bo_nickname")
    private String nickname;
    
    @Column(name = "bo_token")
    private String token;

    @Column(name = "bo_login")
    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE)
    private ZonedDateTime login;
    
    @Column(name = "bo_logout")
    @TimeZoneStorage(TimeZoneStorageType.NORMALIZE)
    private ZonedDateTime logout;
    
    @Column(name = "bo_remoteaddress")
    private String remoteAddress;
    
    @Column(name = "bo_browser")
    private String browser;

    @Column(name = "bo_failedLogins")
    private int failedLogins;

    /** Der eingeloggte Spieler. Referenziert die Spalte {@code BO_USER#bo_user_ref} */
    @ManyToOne
    @JoinColumn(name = "bo_user_ref")
    private User user;

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

    // -- nickname ------------------------------------------------------------

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String value) {
        nickname = value;
    }

    // -- token ---------------------------------------------------------------

    public String getToken() {
        return token;
    }

    public void setToken(String value) {
        token = value;
    }

    // -- login ---------------------------------------------------------------

    public ZonedDateTime getLogin() {
        return login;
    }

    public void setLogin(ZonedDateTime date) {
        login = date;
    }

    // -- logout --------------------------------------------------------------

    public ZonedDateTime getLogout() {
        return logout;
    }

    public void setLogout(ZonedDateTime value) {
        logout = value;
    }
    
    public boolean isTerminated() {
    	return logout != null;
    }

    // -- remoteAddress -------------------------------------------------------

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String ip) {
        remoteAddress = ip;
    }

    // -- browser -------------------------------------------------------------

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String value) {
        browser = value;
    }

    // -- failedLogins --------------------------------------------------------

    public int getFailedLogins() {
        return failedLogins;
    }

    public void setFailedLogins(int value) {
        failedLogins = value;
    }

    // -- user ----------------------------------------------------------------

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
