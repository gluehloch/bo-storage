/*
 * $Id: User.java 3782 2013-07-27 08:44:32Z andrewinkler $
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

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * Die Klasse verwaltet einen Teilnehmer.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3782 $ $LastChangedDate: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 *
 * @hibernate.class table="bo_user"
 */
public class User extends AbstractStorageObject {

    /** serial version id */
    private static final long serialVersionUID = -1806113679051281041L;

    /** Der Leerstring. */
    private static final String EMPTY = "";

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
    public User(final String _nickname) {
        setNickName(_nickname);
    }

    /**
     * Konstruktor.
     *
     * @param aName Der Username (Nachname).
     * @param aSurname Der Vorname des Users.
     * @param aNickname Der Nickname.
     * @param aMail Die Mailadresse.
     * @param aPhone Die Telefonnummer.
     * @param aPwd Das Password.
     * @param aTitle Der Titel.
     * @param aIsAuto Automat?
     * @param aIsExcluded Ausgeschlossen?
     */
    public User(final String aName, final String aSurname,
            final String aNickname, final String aMail, final String aPhone,
            final String aPwd, final String aTitle, final boolean aIsAuto,
            final boolean aIsExcluded) {

        setName(aName);
        setSurname(aSurname);
        setNickName(aNickname);
        setEmail(aMail);
        setPhone(aPhone);
        setPassword(aPwd);
        setTitle(aTitle);
        setAutomat(aIsAuto);
        setExcluded(aIsExcluded);
    }

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

    // -- name ----------------------------------------------------------------

    /** Der Nachname des Users. */
    private String name = EMPTY;

    /**
     * Liefert den Nachnamen des Users.
     *
     * @return Der Nachname.
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
    public void setName(final String value) {
        name = value;
    }

    // -- surname -------------------------------------------------------------

    /** Der Vorname des Users. */
    private String surname = EMPTY;

    /**
     * Liefert den Vornamen des Users.
     *
     * @return Der Vorname.
     *
     * @hibernate.property column="bo_surname"
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Setzt den Vornamen des Users.
     * 
     * @param value Der Vorname.
     */
    public void setSurname(final String value) {
        surname = value;
    }

    // -- nickName ------------------------------------------------------------

    /** Der Nickname des Users. */
    private String nickName = EMPTY;

    /**
     * Liefert den Nickname des Users.
     *
     * @return Der Nickname.
     *
     * @hibernate.property
     *     column="bo_nickname"
     *     not-null="true"
     *     unique="true"
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * Setzt den Nickname.
     *
     * @param value Der Nickname.
     */
    public void setNickName(final String value) {
        nickName = value;
    }

    // -- email ---------------------------------------------------------------

    /** Die EMailadresse des Users. */
    private String email = EMPTY;

    /**
     * Liefert die Mail Adresse.
     * 
     * @return Die Mail-Adresse.
     *
     * @hibernate.property column="bo_email"
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setzt die EMail Adresse.
     *
     * @param value Die Mail-Adresse.
     */
    public void setEmail(final String value) {
        email = value;
    }

    // -- phone ---------------------------------------------------------------

    /** Die Telefonnummer des Users. */
    private String phone = EMPTY;

    /**
     * Liefert die Telefonnummer.
     *
     * @return Die Telefonnummer.
     *
     * @hibernate.property column="bo_phone"
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Setzt die Telefonnummer.
     *
     * @param value Die Telefonnummer.
     */
    public void setPhone(final String value) {
        phone = value;
    }

    // -- password ------------------------------------------------------------

    /** Das Password des Users. */
    private String password = EMPTY;

    /**
     * Liefert das Password.
     *
     * @return Das Password.
     *
     * @hibernate.property column="bo_password"
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setzt das Password.
     *
     * @param value Das Password.
     */
    public void setPassword(final String value) {
        password = value;
    }

    // -- automat -------------------------------------------------------------

    /** Flag ob User ein Automat ist. */
    private boolean automat = false;

    /**
     * Automat oder kein Automat.
     *
     * @return true, ein Automat; false sonst.
     *
     * @hibernate.property column="bo_automat"
     */
    public boolean isAutomat() {
        return automat;
    }

    /**
     * Setzt das Automaten-Flag.
     *
     * @param value true Automat; false sonst.
     */
    public void setAutomat(final boolean value) {
        automat = value;
    }

    // -- excluded ------------------------------------------------------------

    /** Gültigkeitsflag. */
    private boolean exclude = false;

    /**
     * User von Teilnahme ausgeschlossen?
     *
     * @return true ausgeschlossen; false sonst.
     *
     * @hibernate.property column="bo_excluded"
     */
    public boolean isExcluded() {
        return exclude;
    }

    /**
     * Setzt das Flag 'Ausschluß'.
     *
     * @param value true Ausschluß; false sonst.
     */
    public void setExcluded(final boolean value) {
        exclude = value;
    }

    // -- title ---------------------------------------------------------------

    /** Der eventuelle Meistertitel etc. */
    private String title = EMPTY;

    /**
     * Liefert den Titel des Users.
     *
     * @return Der Titel.
     *
     * @hibernate.property column="bo_title"
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setzt den Titel des Users.
     *
     * @param value Der Titel.
     */
    public void setTitle(final String value) {
        title = value;
    }

    // -- usersChampionship----------------------------------------------------

    /** Die Teilnehmer, die dieser Saison zugeordnet sind. */
    private Set<UserSeason> userSeason = new HashSet<UserSeason>();

    /**
     * Liefert die Beziehung Teilnehmer/Saison.
     *
     * @return Ein Menge von <code>UserSeason</code>.
     *
     * @hibernate.set
     *     cascade="none"
     *     inverse="true"
     * @hibernate.collection-one-to-many
     *     class="de.winkler.betoffice.storage.UserSeason"
     * @hibernate.collection-key
     *     column="bo_user_ref"
     */
    public Set<UserSeason> getUserSeason() {
        return userSeason;
    }

    /**
     * Setzt die Beziehung Teilnehmer/Saison.
     *
     * @param value Ein Menge von <code>UserSeason</code>.
     */
    protected void setUserSeason(final Set<UserSeason> value) {
        userSeason = value;
    }

    // ------------------------------------------------------------------------

    /**
     * Vergleicht ein Password mit dem Password dieses Users.
     *
     * @param value Das zu vergleichende Password.
     * @return true Passwörter sind gleich; false sonst.
     */
    public boolean comparePwd(final String value) {
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
        buf.append(nickName);
        buf.append(": ");
        buf.append(email);
        buf.append(": ");
        buf.append(phone);
        buf.append(": ");
        buf.append(password);
        return buf.toString();
    }

    // -- StorageObject -------------------------------------------------------    

    /**
     * Prüft, ob die Eigenschaften dieses Objekts komplett und gültig
     * gefüllt sind, damit es evt. Weiterverarbeitungen erfahren kann.
     * Folgende Eigenschaften müssen gesetzt sein:
     * <ul>
     *  <li>nickName</li>
     * </ul>
     *
     * @return true, Objekt in Ordnung; false, es ist was falsch.
     */
    public boolean isValid() {
        if (StringUtils.isBlank(nickName)) {
            return false;
        } else {
            return true;
        }
    }

    // -- Object --------------------------------------------------------------

    /**
     * @see Object#toString()
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(nickName);
        return buf.toString();
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
            return (user.getNickName().equalsIgnoreCase(getNickName()));
        }
    }

    /**
     * @see Object#hashCode()
     */
    public int hashCode() {
        int result = 17;
        result = 37 * result + getNickName().hashCode();
        return result;
    }

}
