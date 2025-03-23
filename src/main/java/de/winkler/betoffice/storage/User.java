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
 *
 */

package de.winkler.betoffice.storage;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import de.winkler.betoffice.storage.enums.NotificationType;
import de.winkler.betoffice.storage.enums.RoleType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Die Klasse verwaltet einen Teilnehmer.
 *
 * @author by Andre Winkler
 */
@Entity
@Table(name = "bo_user")
public class User extends AbstractStorageObject {

    /** serial version id */
    private static final long serialVersionUID = -1806113679051281041L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    /** Der Nachname des Users. */
    @Column(name = "bo_name")
    private String name;

    /** Der Vorname des Users. */
    @Column(name = "bo_surname")
    private String surname;

    /** Der Nickname des Users. */
    @Embedded
    private Nickname nickname;

    /** Die EMailadresse des Users. */
    @Column(name = "bo_email")
    private String email;

    /** Die Telefonnummer des Users. */
    @Column(name = "bo_phone")
    private String phone;

    /** Das Password des Users. */
    @Column(name = "bo_password")
    private String password;

    /** Flag ob User ein Automat ist. */
    @Column(name = "bo_automat")
    private boolean automat = false;

    /** Gültigkeitsflag. */
    @Column(name = "bo_excluded")
    private boolean exclude = false;

    /** Adminstrator Flag. */
    @Column(name = "bo_admin")
    private boolean admin = false;

    /** Der eventuelle Meistertitel etc. */
    @Column(name = "bo_title")
    private String title;

    @Enumerated
    @Column(name = "bo_notification")
    private NotificationType notification = NotificationType.NONE;

    @Column(name = "bo_change_token")
    private String changeToken;

    @Column(name = "bo_change_email")
    private String changeEmail;

    @Column(name = "bo_change_send")
    private Integer changeSend;

    // -- Construction --------------------------------------------------------

    /**
     * Defaultkonstruktor.
     */
    public User() {
    }

    /**
     * Konstruktor. Erstellt ein minimales User Objekt.
     *
     * @param _nickname
     */
    public User(Nickname _nickname) {
        nickname = _nickname;
    }

    /**
     * Konstruktor.
     *
     * @param nickname    Der Nickname.
     * @param aName       Der Username (Nachname).
     * @param aSurname    Der Vorname des Users.
     * @param aMail       Die Mailadresse.
     * @param aPhone      Die Telefonnummer.
     * @param aPwd        Das Password.
     * @param aTitle      Der Titel.
     * @param aIsAuto     Automat?
     * @param aIsExcluded Ausgeschlossen?
     */
    public User(Nickname nickname, String aName, String aSurname, String aMail, String aPhone, String aPwd,
            String aTitle, boolean aIsAuto, boolean aIsExcluded) {

        setName(aName);
        setSurname(aSurname);
        setNickname(nickname);
        setEmail(aMail);
        setPhone(aPhone);
        setPassword(aPwd);
        setTitle(aTitle);
        setAutomat(aIsAuto);
        setExcluded(aIsExcluded);
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
    protected void setId(Long value) {
        id = value;
    }

    // -- name ----------------------------------------------------------------

    /**
     * Liefert den Nachnamen des Users.
     *
     * @return             Der Nachname.
     *
     * @hibernate.property column="bo_name"
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Nachnamen des Users.
     *
     * @param value Der Nachname.
     */
    public void setName(String value) {
        name = value;
    }

    // -- surname -------------------------------------------------------------

    /**
     * Liefert den Vornamen des Users.
     *
     * @return Der Vorname.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Setzt den Vornamen des Users.
     * 
     * @param value Der Vorname.
     */
    public void setSurname(String value) {
        surname = value;
    }

    // -- nickName ------------------------------------------------------------

    /**
     * Liefert den Nickname des Users.
     *
     * @return Der Nickname.
     */
    public Nickname getNickname() {
        return nickname;
    }

    /**
     * Setzt den Nickname.
     *
     * @param value Der Nickname.
     */
    public void setNickname(Nickname value) {
        nickname = value;
    }

    // -- email ---------------------------------------------------------------

    /**
     * Liefert die Mail Adresse.
     * 
     * @return Die Mail-Adresse.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setzt die EMail Adresse.
     *
     * @param value Die Mail-Adresse.
     */
    public void setEmail(String value) {
        email = value;
    }

    // -- phone ---------------------------------------------------------------

    /**
     * Liefert die Telefonnummer.
     *
     * @return Die Telefonnummer.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Setzt die Telefonnummer.
     *
     * @param value Die Telefonnummer.
     */
    public void setPhone(String value) {
        phone = value;
    }

    // -- password ------------------------------------------------------------

    /**
     * Liefert das Password.
     *
     * @return Das Password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setzt das Password.
     *
     * @param value Das Password.
     */
    public void setPassword(String value) {
        password = value;
    }

    // -- automat -------------------------------------------------------------

    /**
     * Automat oder kein Automat.
     *
     * @return true, ein Automat; false sonst.
     */
    public boolean isAutomat() {
        return automat;
    }

    /**
     * Setzt das Automaten-Flag.
     *
     * @param value true Automat; false sonst.
     */
    public void setAutomat(boolean value) {
        automat = value;
    }

    // -- excluded ------------------------------------------------------------

    /**
     * User von Teilnahme ausgeschlossen?
     *
     * @return true ausgeschlossen; false sonst.
     */
    public boolean isExcluded() {
        return exclude;
    }

    /**
     * Setzt das Flag 'Ausschluß'.
     *
     * @param value true Ausschluß; false sonst.
     */
    public void setExcluded(boolean value) {
        exclude = value;
    }

    // -- title ---------------------------------------------------------------

    /**
     * Liefert den Titel des Users.
     *
     * @return Der Titel.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setzt den Titel des Users.
     *
     * @param value Der Titel.
     */
    public void setTitle(String value) {
        title = value;
    }

    public NotificationType getNotification() {
        return notification;
    }

    public void setNotification(NotificationType notification) {
        this.notification = notification;
    }

    // -- admin ---------------------------------------------------------------

    /**
     * Haben wir es hier mit einem Administrator zu tun?
     * 
     * @return <code>true</code>, wenn dies der Fall ist.
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * Setzt das Administrator Flag.
     * 
     * @param _admin <code>true</code>, wenn dies ein Administrator werden soll.
     */
    public void setAdmin(boolean _admin) {
        admin = _admin;
    }

    // ------------------------------------------------------------------------

    public String getChangeToken() {
        return changeToken;
    }

    public void setChangeToken(String changeToken) {
        this.changeToken = changeToken;
    }

    // ------------------------------------------------------------------------

    public String getChangeEmail() {
        return changeEmail;
    }

    public void setChangeEmail(String changeEmail) {
        this.changeEmail = changeEmail;
    }

    // ------------------------------------------------------------------------

    public Integer getChangeSend() {
        return changeSend;
    }

    public void setChangeSend(Integer changeSend) {
        this.changeSend = changeSend;
    }

    public void incrementChangeSend() {
        this.changeSend = this.changeSend == null
                ? Integer.valueOf(1)
                : Integer.valueOf(this.changeSend.intValue() + 1);
    }

    public void resetChangeSend() {
        this.changeSend = 0;
    }

    public User abortEmailChange() {
        this.changeToken = null;
        this.changeEmail = null;
        this.changeSend = 0;
        return this;
    }

    public User acceptEmailChange() {
        this.email = this.changeEmail;
        this.changeToken = null;
        this.changeEmail = null;
        this.changeSend = 0;
        return this;
    }

    // ------------------------------------------------------------------------

    /**
     * Vergleicht ein Password mit dem Password dieses Users.
     *
     * @param  value Das zu vergleichende Password.
     * @return       true Passwörter sind gleich; false sonst.
     */
    public boolean comparePwd(String value) {
        return (StringUtils.equals(value, password));
    }

    /**
     * Liefert Detailinformationen zu diesem Objekt.
     *
     * @return Ein String.
     */
    public String getDebugInfo() {
        StringBuilder buf = new StringBuilder();
        buf.append(name);
        buf.append(": ");
        buf.append(surname);
        buf.append(": ");
        buf.append(nickname);
        buf.append(": ");
        buf.append(email);
        buf.append(": ");
        buf.append(phone);
        return buf.toString();
    }

    // -- Role ----------------------------------------------------------------

    public List<RoleType> getRoleTypes() {
        List<RoleType> roleTypes = new ArrayList<>();
        if (isAdmin()) {
            roleTypes.add(RoleType.ADMIN);
        }
        if (!isExcluded()) {
            roleTypes.add(RoleType.TIPPER);
        }
        return roleTypes;
    }

    // -- Object --------------------------------------------------------------

    /**
     * @see Object#toString()
     */
    public String toString() {
        return getNickname().toString();
    }

    /**
     * @see Object#equals(java.lang.Object)
     */
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        } else if (!(object instanceof User)) {
            return false;
        } else {
            User user = (User) object;
            return (user.getNickname().equals(getNickname()));
        }
    }

    /**
     * @see Object#hashCode()
     */
    public int hashCode() {
        int result = 17;
        result = 37 * result + getNickname().hashCode();
        return result;
    }

}
