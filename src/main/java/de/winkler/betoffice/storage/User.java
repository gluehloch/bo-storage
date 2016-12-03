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

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * Die Klasse verwaltet einen Teilnehmer.
 *
 * @author by Andre Winkler
 */
public class User extends AbstractStorageObject {

    /** serial version id */
    private static final long serialVersionUID = -1806113679051281041L;

    /** Der Leerstring. */
    private static String EMPTY = "";

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
    public User(String _nickname) {
        setNickName(_nickname);
    }

    /**
     * Konstruktor.
     *
     * @param aName
     *            Der Username (Nachname).
     * @param aSurname
     *            Der Vorname des Users.
     * @param aNickname
     *            Der Nickname.
     * @param aMail
     *            Die Mailadresse.
     * @param aPhone
     *            Die Telefonnummer.
     * @param aPwd
     *            Das Password.
     * @param aTitle
     *            Der Titel.
     * @param aIsAuto
     *            Automat?
     * @param aIsExcluded
     *            Ausgeschlossen?
     */
    public User(String aName, String aSurname, String aNickname, String aMail,
            String aPhone, String aPwd, String aTitle, boolean aIsAuto,
            boolean aIsExcluded) {

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
     * @param value
     *            Der Primärschlüssel.
     */
    protected void setId(Long value) {
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
     * @param value
     *            Der Nachname.
     */
    public void setName(String value) {
        name = value;
    }

    // -- surname -------------------------------------------------------------

    /** Der Vorname des Users. */
    private String surname = EMPTY;

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
     * @param value
     *            Der Vorname.
     */
    public void setSurname(String value) {
        surname = value;
    }

    // -- nickName ------------------------------------------------------------

    /** Der Nickname des Users. */
    private String nickName = EMPTY;

    /**
     * Liefert den Nickname des Users.
     *
     * @return Der Nickname.
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * Setzt den Nickname.
     *
     * @param value
     *            Der Nickname.
     */
    public void setNickName(String value) {
        nickName = value;
    }

    // -- email ---------------------------------------------------------------

    /** Die EMailadresse des Users. */
    private String email = EMPTY;

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
     * @param value
     *            Die Mail-Adresse.
     */
    public void setEmail(String value) {
        email = value;
    }

    // -- phone ---------------------------------------------------------------

    /** Die Telefonnummer des Users. */
    private String phone = EMPTY;

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
     * @param value
     *            Die Telefonnummer.
     */
    public void setPhone(String value) {
        phone = value;
    }

    // -- password ------------------------------------------------------------

    /** Das Password des Users. */
    private String password = EMPTY;

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
     * @param value
     *            Das Password.
     */
    public void setPassword(String value) {
        password = value;
    }

    // -- automat -------------------------------------------------------------

    /** Flag ob User ein Automat ist. */
    private boolean automat = false;

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
     * @param value
     *            true Automat; false sonst.
     */
    public void setAutomat(boolean value) {
        automat = value;
    }

    // -- excluded ------------------------------------------------------------

    /** Gültigkeitsflag. */
    private boolean exclude = false;

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
     * @param value
     *            true Ausschluß; false sonst.
     */
    public void setExcluded(boolean value) {
        exclude = value;
    }

    // -- title ---------------------------------------------------------------

    /** Der eventuelle Meistertitel etc. */
    private String title = EMPTY;

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
     * @param value
     *            Der Titel.
     */
    public void setTitle(String value) {
        title = value;
    }

    // -- admin ---------------------------------------------------------------

    /** Adminstrator Flag. */
    private boolean admin = false;

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
     * @param _admin
     *            <code>true</code>, wenn dies ein Administrator werden soll.
     */
    public void setAdmin(boolean _admin) {
        admin = _admin;
    }

    // -- userSeason ----------------------------------------------------------

    /** Die Teilnehmer, die dieser Saison zugeordnet sind. */
    private Set<UserSeason> userSeason = new HashSet<>();

    /**
     * Liefert die Beziehung Teilnehmer/Saison.
     *
     * @return Ein Menge von <code>UserSeason</code>.
     */
    public Set<UserSeason> getUserSeason() {
        return userSeason;
    }

    /**
     * Setzt die Beziehung Teilnehmer/Saison.
     *
     * @param value
     *            Ein Menge von <code>UserSeason</code>.
     */
    protected void setUserSeason(Set<UserSeason> value) {
        userSeason = value;
    }

    // ------------------------------------------------------------------------

    /**
     * Vergleicht ein Password mit dem Password dieses Users.
     *
     * @param value
     *            Das zu vergleichende Password.
     * @return true Passwörter sind gleich; false sonst.
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
        buf.append(nickName);
        buf.append(": ");
        buf.append(email);
        buf.append(": ");
        buf.append(phone);
        buf.append(": ");
        buf.append(password);
        return buf.toString();
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
