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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import de.awtools.basic.LoggerFactory;
import de.winkler.betoffice.storage.enums.SeasonType;
import de.winkler.betoffice.storage.enums.TeamType;

/**
 * Verwaltet die Daten einer Saison.
 * 
 * @author by Andre Winkler
 * @hibernate.class table="bo_season"
 */
public class Season extends AbstractStorageObject {

    /** serial version */
    private static final long serialVersionUID = -8992251563826611291L;

    /** Der Logger der Klasse. */
    private static Logger log = LoggerFactory.make();

    // -- Construction --------------------------------------------------------

    /**
     * Defaulkonstruktor
     */
    public Season() {
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
    protected void setId(final Long value) {
        id = value;
    }

    // -- championshipConfiguration -------------------------------------------

    /** Die Meisterschaftskonfiguration. */
    private ChampionshipConfiguration championshipConfiguration = new ChampionshipConfiguration();

    /**
     * Liefert die Meisterschaftskonfiguration.
     * 
     * @return Die Meisterschaftskonfiguration.
     * 
     * @hibernate.component
     */
    public ChampionshipConfiguration getChampionshipConfiguration() {
        return championshipConfiguration;
    }

    /**
     * Setzt eine neue Meisterschaftskonfiguration.
     * 
     * @param value
     *            Die Meisterschaftskonfiguration.
     */
    protected void setChampionshipConfiguration(
            final ChampionshipConfiguration value) {

        championshipConfiguration = value;
    }

    // -- year ----------------------------------------------------------------

    /** Bezeichner Jahrgang/Datum der Saison. */
    private String year;

    /**
     * Jahrgang der Spielzeit (Format: 2001/2002).
     * 
     * @return Jahrgang der Spielzeit.
     * 
     * @hibernate.property column="bo_year"
     */
    public String getYear() {
        return year;
    }

    /**
     * Setzt den Jahrgang.
     * 
     * @param value
     *            Der Jahrgang.
     */
    public void setYear(final String value) {
        Validate.notNull(value);
        year = value.trim();
    }

    // -- name ----------------------------------------------------------------

    /** Bezeichner der Saison. */
    private String name;

    /**
     * Bezeichnung der Spielzeit (z.B. 1. Bundesliga).
     * 
     * @return Bezeichnung der Spielzeit.
     * 
     * @hibernate.property column="bo_name"
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen der Spielzeit.
     * 
     * @param value
     *            Name der Spielzeit.
     */
    public void setName(final String value) {
        Validate.notNull(value);
        name = value.trim();
    }

    // -- mode ----------------------------------------------------------------

    /** Spielmodus der Saison. (Bundesliga, Pokal, WM,...) ` */
    private SeasonType mode = SeasonType.LEAGUE;

    /**
     * Liefert den Modus der Saison.
     * 
     * @return Der Modus.
     * 
     * @hibernate.property column="bo_mode"
     *                     type="de.winkler.betoffice.storage.enums.SeasonType"
     *                     not-null="true"
     */
    public SeasonType getMode() {
        return mode;
    }

    /**
     * Setzt den Modus der Saison.
     * 
     * @param value
     *            Der Modus.
     */
    public void setMode(final SeasonType value) {
        mode = value;
    }

    // -- teamType ------------------------------------------------------------

    /** Mannschaftstyp. (DFB, FIFA) ` */
    private TeamType teamType = TeamType.DFB;

    /**
     * Liefert den Mannschaftstyp, der für diese Meisterschaft akzeptiert wird.
     * 
     * @return Der akzeptierte TeamType.
     * 
     * @hibernate.property column="bo_teamtype"
     *                     type="de.winkler.betoffice.storage.enums.TeamType"
     *                     not-null="true"
     */
    public TeamType getTeamType() {
        return teamType;
    }

    /**
     * Setzt den Mannschaftstyp.
     * 
     * @param value
     *            Der Mannschaftstyp.
     */
    public void setTeamType(final TeamType value) {
        teamType = value;
    }

    // -- groups --------------------------------------------------------------

    /** Die Gruppen, die dieser Saison zugeordnet sind. */
    private Set<Group> groups = new HashSet<Group>();

    /**
     * Liefert die Gruppen dieser Saison.
     * 
     * @return Eine Menge mit Gruppen der Saison.
     * 
     * @hibernate.set cascade="all" inverse="true"
     * @hibernate.collection-one-to-many 
     *                                   class="de.winkler.betoffice.storage.Group"
     * @hibernate.collection-key column="bo_season_ref"
     */
    public Set<Group> getGroups() {
        return groups;
    }

    /**
     * Setzt die Gruppen.
     * 
     * @param value
     *            Die Grupppen.
     */
    protected void setGroups(final Set<Group> value) {
        groups = value;
    }

    /**
     * Liefert die passende Gruppe in dieser Saison.
     * 
     * @param groupType
     *            Ein Gruppentyp.
     * @return Die passende Gruppe. Kann <code>null</code> liefern, wenn keine
     *         passende Gruppe gefunden.
     */
    public Group getGroup(final GroupType groupType) {
        for (Group group : getGroups()) {
            if (group.getGroupType().equals(groupType)) {
                return group;
            }
        }
        return null;
    }

    /**
     * Fügt eine weitere Gruppe der Saison hinzu.
     * 
     * @param group
     *            Eine weitere Gruppe.
     */
    public void addGroup(final Group group) {
        Validate.notNull(group);
        group.setSeason(this);
        groups.add(group);
    }

    /**
     * Fügt eine Liste von Gruppen dieser Saison hinzu.
     * 
     * @param values
     *            Eine Liste von Gruppen.
     */
    public void addGroups(final Set<Group> values) {
        Validate.notNull(values);
        for (Group group : values) {
            addGroup(group);
        }
    }

    /**
     * Prüft, ob eine Gruppe mit gesuchten Namen bereits existiert.
     * 
     * @param _groupType
     *            Der gesuchte Gruppentyp.
     * @return Der Gruppentyp, wenn bereits vorhanden; <code>null</code>, sonst.
     */
    public Group isActivated(final GroupType _groupType) {
        for (Group group : getGroups()) {
            if (group.getGroupType().equals(_groupType)) {
                return group;
            }
        }
        return null;
    }

    /**
     * Entfernt eine Gruppe.
     * 
     * @param group
     *            Die zu entfernende Gruppe.
     */
    public void removeGroup(final Group group) {
        Validate.notNull(group.getSeason());
        Validate.isTrue(group.getSeason().equals(this));

        groups.remove(group);
    }

    /**
     * Deaktiviert eine Gruppe.
     * 
     * @param groupType
     *            Der zu deaktivierende Gruppentyp.
     * @return Das entfernte {@link Group} Objekt. Kann <code>null</code> sein,
     *         wenn kein entsprechendes {@link Group} Objekt gefunden.
     */
    public Group removeGroup(final GroupType groupType) {
        for (Iterator<Group> i = getGroups().iterator(); i.hasNext();) {
            Group tmp = i.next();
            if (tmp.getGroupType().equals(groupType)) {
                i.remove();
                return tmp;
            }
        }
        return null;
    }

    /**
     * Liefert alle Mannschaften, die über eine Gruppe an dieser Saison
     * beteiligt sind.
     * 
     * @return Alle Mannschaften, die dieser Saison zugeordnet sind.
     */
    public Set<Team> getTeams() {
        Set<Team> teams = new HashSet<Team>();
        for (Group group : getGroups()) {
            teams.addAll(group.getTeams());
        }
        return teams;
    }

    /**
     * Liefert alle Gruppentypen, die dieser Saison zugeordnet sind.
     * 
     * @return Eine Liste mit <code>GroupType</code>.
     */
    public List<GroupType> getGroupTypes() {
        List<GroupType> result = new ArrayList<GroupType>();
        for (Group group : getGroups()) {
            result.add(group.getGroupType());
        }
        return result;
    }

    // -- userSeason ----------------------------------------------------------

    /** Die Teilnehmer, die dieser Saison zugeordnet sind. */
    private Set<UserSeason> userSeason = new HashSet<UserSeason>();

    /**
     * Liefert die Beziehung Teilnehmer/Saison.
     * 
     * @return Ein Menge von <code>UserSeason</code>.
     * 
     * @hibernate.set cascade="all" inverse="true"
     * @hibernate.collection-one-to-many 
     *                                   class="de.winkler.betoffice.storage.UserSeason"
     * @hibernate.collection-key column="bo_season_ref"
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
    protected void setUserSeason(final Set<UserSeason> value) {
        userSeason = value;
    }

    /**
     * Einen Teilnehmer der Saison hinzufügen.
     * 
     * @param user
     *            Der neue Teilnehmer.
     */
    public void addUser(final UserSeason user) {
        Validate.notNull(user.getUser());

        if (user.getSeason() != null) {
            user.getSeason().removeUser(user);
        }
        user.setSeason(this);
        userSeason.add(user);
    }

    /**
     * Entfernt einen Teilnehmer aus der Saison.
     * 
     * @param user
     *            Der zu entfernende Teilnehmer. Ist der Teilnehmer nicht dieser
     *            Saison zugeordnet, wird eine <code>RuntimeException</code>
     *            geworfen.
     */
    public void removeUser(final UserSeason user) {
        Validate.notNull(user.getUser());
        Validate.isTrue(user.getSeason().equals(this));

        userSeason.remove(user);
    }

    /**
     * Deaktiviert einen Teilnehmer.
     * 
     * @param user
     *            Der zu deaktivierende Teilnehmer.
     * @return Das entfernte {@link UserSeason} Objekt. Kann <code>null</code>
     *         sein, wenn kein entsprechendes {@link UserSeason} Objekt
     *         gefunden.
     */
    public UserSeason removeUser(final User user) {
        for (Iterator<UserSeason> i = getUserSeason().iterator(); i.hasNext();) {
            UserSeason tmp = i.next();
            if (tmp.getUser().equals(user)) {
                // Darf hier nicht geschehen: Sonst geht das DAO Delete nicht!
                // tmp.setSeason(null);
                // tmp.setUser(null);
                i.remove();
                return tmp;
            }
        }
        return null;
    }

    /**
     * Liefert alle Teilnehmer, die dieser Saison zugeordnet sind.
     * 
     * @return Alle Teilnehmer dieser Saison.
     */
    public List<User> getUsers() {
        List<User> users = new ArrayList<User>();
        for (UserSeason tmp : getUserSeason()) {
            users.add(tmp.getUser());
        }
        return users;
    }

    // -- gameDayList ---------------------------------------------------------

    /** Eine Liste der Spieltage/GameList. */
    private List<GameList> gameList = new ArrayList<GameList>();

    /**
     * Liefert alle Spieltage. Eine <code>List</code> von {@link GameList}
     * Objekten.
     * 
     * @return Die Spieltage.
     * 
     * @hibernate.list cascade="all" lazy="true"
     * @hibernate.collection-index column="bo_index"
     * @hibernate.collection-key column="bo_gamedaylist_ref"
     * @hibernate.collection-one-to-many 
     *                                   class="de.winkler.betoffice.storage.GameList"
     */
    protected List<GameList> getGameList() {
        return gameList;
    }

    /**
     * Setzt die Spieltagsliste.
     * 
     * @param value
     *            Eine Spieltagsliste.
     */
    protected void setGameList(final List<GameList> value) {
        Validate.notNull(value);
        gameList = value;
    }

    /**
     * Liefert eine nicht-modifizierbare Liste aller Spieltage.
     * 
     * @return Liste aller Spieltage (GameList).
     */
    public List<GameList> unmodifiableGameList() {
        return Collections.unmodifiableList(gameList);
    }

    /**
     * Liefert die Anzahl der Spieltage.
     * 
     * @return Anzahl der Spieltage.
     */
    public int size() {
        return gameList.size();
    }

    /**
     * Liefert den Index eines Objekts in dieser Liste.
     * 
     * @param object
     *            Das gesuchte Objekt.
     * @return Anzahl der Spieltage.
     */
    public int indexOf(final Object object) {
        return gameList.indexOf(object);
    }

    /**
     * Übergebener GameList/Spieltag bereits in Liste enthalten?
     * 
     * @param _gameList
     *            Die zu prüfende GameList/Spieltag
     * @return true, vorhanden; false, nicht vorhanden.
     */
    public boolean contains(final GameList _gameList) {
        return (gameList.contains(_gameList));
    }

    /**
     * Diese Methode liefert die Spieltagsliste.
     * 
     * @param dayNr
     *            Nummer des Spieltags ( [0] .. [size() - 1] ).
     * @return Liste aller Spiele eines Spieltags. Liefert <code>null</code>
     *         zurück, wenn keine Spieltage der Saison/Meisterschaft zugeordnet!
     */
    public GameList getGamesOfDay(final int dayNr) {
        if ((dayNr >= gameList.size()) || (dayNr < 0)) {
            throw new IndexOutOfBoundsException("dayNr '" + dayNr
                    + "' out of bounds!");
        } else if (gameList.size() == 0) {
            return null;
        }

        for (GameList currGameList : gameList) {
            if (log.isDebugEnabled()) {
                StringBuffer buf = new StringBuffer();
                buf.append("Untersuche Spieltag '");
                buf.append(currGameList);
                buf.append("' ");
                log.debug(buf.toString());
            }

            if (currGameList.getIndex() == StorageConst.INDEX_UNDEFINED) {
                String dateTime = new DateTime(currGameList.getDateTime())
                        .toString("dd.MM.yyyy HH:mm", Locale.GERMANY);
                throw new IllegalStateException("current round index: "
                        + currGameList.getIndex() + "; dateTime: " + dateTime);
            } else if (currGameList.getIndex() == dayNr) {
                // Spieltag gefunden, Suche beenden und alle Spiele
                // des Spieltages in einer Liste zurückgeben.
                return currGameList;
            }
        }

        log.error("Round number " + dayNr + " not found!");
        throw new IllegalStateException("Fatal coding error. See your logs!");
    }

    /**
     * Fügt einen neuen Spieltag der Spieltagsliste hinzu. Ein Spieltag wird
     * immer hinten angehängt. Falls eine andere Einsortierung erfolgen soll
     * müsste entsprechend der Index in allen Spieltagen geändert werden.
     * 
     * @param newRound
     *            Ein Spieltag.
     * @throws IllegalArgumentException
     *             GameListe bereits vorhanden oder ein Attribut von gameList
     *             nicht gesetzt.
     */
    public void addGameList(final GameList newRound) {
        Validate.notNull(newRound, "The newRound parameter is null.");
        Validate.notNull(newRound.getDateTime(),
                "The datetime field of newRound is null.");
        Validate.notNull(newRound.getGroup(),
                "The group field of newRound is null.");

        for (GameList gl : gameList) {
            if (gl.getDateTime() != null
                    && gl.getDateTime().equals(newRound.getDateTime())
                    && gl.getGroup().equals(newRound.getGroup())) {

                String error = String
                        .format("Round [%s] with same date and group is already there.",
                                newRound.toString());
                log.error(error);
                throw new IllegalArgumentException(error);
            }
        }

        // ... und in Spieltagsliste aufnehmen.
        newRound.setSeason(this);
        newRound.setIndex(gameList.size());
        gameList.add(newRound);

        if (log.isDebugEnabled()) {
            log.debug("Neuen Spieltag '" + newRound + "' hinzugefügt.");
        }
    }

    /**
     * Entfernt alle Spieltage.
     */
    public void removeAllGameList() {
        List<GameList> tmp = new ArrayList<GameList>(getGameList());
        for (GameList gl : tmp) {
            gl.removeAllGame();
            removeGameList(gl);
        }
    }

    /**
     * Entfernt einen Spieltag aus der Spieltagsliste.
     * 
     * @param _gameList
     *            Ein Spieltag.
     */
    public void removeGameList(final GameList _gameList) {
        Validate.notNull(_gameList);

        if (!gameList.contains(_gameList)) {
            StringBuilder buf = new StringBuilder(_gameList.toString());
            buf.append(" nicht vorhanden!");
            log.error(buf.toString());
            throw new IllegalArgumentException(buf.toString());
        }

        // ... und aus Spieltagliste nehmen.
        gameList.remove(_gameList);
        _gameList.setSeason(null);

        if (log.isDebugEnabled()) {
            log.debug("Spieltag '" + _gameList + "' entfernt.");
        }
    }

    // -- StorageObject -------------------------------------------------------

    /**
     * Prüft, ob die Eigenschaften dieses Objekts komplett und gültig gefüllt
     * sind, damit es evt. Weiterverarbeitungen erfahren kann. Folgende
     * Eigenschaften müssen gesetzt sein:
     * <ul>
     * <li>year</li>
     * <li>name</li>
     * </ul>
     * Der Spielmodus ist per default auf 'Liga' eingestellt.
     * 
     * @return true, Objekt in Ordnung; false, es ist was falsch.
     */
    public boolean isValid() {
        if (StringUtils.isBlank(year)) {
            return false;
        } else if (StringUtils.isBlank(name)) {
            return false;
        } else {
            return true;
        }
    }

    // -- Object --------------------------------------------------------------

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("Year: ").append(getYear());
        buf.append(", Name: ").append(getName());
        if (getMode() == null) {
            buf.append(", Mode:").append("<null>");
        } else {
            buf.append(", Mode:").append(getMode().toString());
        }
        return buf.toString();
    }

    /**
     * @see Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals(final Object object) {
        if (object == null) {
            return false;
        } else if (!(object instanceof Season)) {
            return false;
        } else if ((getYear().equals(((Season) object).getYear()))
                && (getName().equals(((Season) object).getName()))
                && (getMode().equals(((Season) object).getMode()))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public final int hashCode() {
        int result = 17;
        result = 37 * result + getName().hashCode();
        result = 37 * result + getYear().hashCode();
        result = 37 * result + getMode().hashCode();
        return result;
    }

}
