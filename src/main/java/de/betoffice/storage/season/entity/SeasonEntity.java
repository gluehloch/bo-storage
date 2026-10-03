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

package de.betoffice.storage.season.entity;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

import de.betoffice.storage.AbstractStorageObject;
import de.betoffice.storage.StorageConst;
import de.betoffice.storage.group.entity.GroupTypeEntity;
import de.betoffice.storage.season.SeasonType;
import de.betoffice.storage.team.TeamType;
import de.betoffice.storage.team.entity.TeamEntity;

/**
 * Verwaltet die Daten einer Saison.
 * 
 * @author by Andre Winkler
 */
@Entity
@Table(name = "bo_season")
public class SeasonEntity extends AbstractStorageObject {

    /** serial version */
    private static final long serialVersionUID = -8992251563826611291L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Embedded
    private SeasonReference reference = new SeasonReference();

    @Embedded
    private ChampionshipConfiguration championshipConfiguration = new ChampionshipConfiguration();

    /** Spielmodus der Saison. (Bundesliga, Pokal, WM,...) ` */
    @Enumerated
    @Column(name = "bo_mode")
    private SeasonType mode = SeasonType.LEAGUE;

    /** Mannschaftstyp. (DFB, FIFA) ` */
    @Enumerated
    @Column(name = "bo_teamtype")
    private TeamType teamType = TeamType.DFB;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "bo_season_ref")
    private Set<GroupEntity> groups = new HashSet<>();

    /** Eine Liste der Spieltage/GameList. */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "bo_season_ref")
    @OrderColumn(name = "bo_index")
    private List<GameListEntity> gameList = new ArrayList<>();

    // -- Construction --------------------------------------------------------

    public SeasonEntity() {
    }
    
    public SeasonEntity(SeasonReference sr) {
        this.reference = sr;
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
    protected void setId(final Long value) {
        id = value;
    }

    // -- reference -----------------------------------------------------------

    public SeasonReference getReference() {
        return reference;
    }

    public void setReference(SeasonReference reference) {
        this.reference = reference;
    }

    // -- championshipConfiguration -------------------------------------------

    /**
     * Liefert die Meisterschaftskonfiguration.
     * 
     * @return Die Meisterschaftskonfiguration.
     */
    public ChampionshipConfiguration getChampionshipConfiguration() {
        if (championshipConfiguration == null) {
            this.championshipConfiguration = new ChampionshipConfiguration();
        }
        return this.championshipConfiguration;
    }

    /**
     * Setzt eine neue Meisterschaftskonfiguration.
     * 
     * @param value Die Meisterschaftskonfiguration.
     */
    protected void setChampionshipConfiguration(final ChampionshipConfiguration value) {
        championshipConfiguration = value;
    }

    // -- mode ----------------------------------------------------------------

    /**
     * Liefert den Modus der Saison.
     * 
     * @return Der Modus.
     */
    public SeasonType getMode() {
        return mode;
    }

    /**
     * Setzt den Modus der Saison.
     * 
     * @param value Der Modus.
     */
    public void setMode(final SeasonType value) {
        mode = value;
    }

    // -- teamType ------------------------------------------------------------

    /**
     * Liefert den Mannschaftstyp, der für diese Meisterschaft akzeptiert wird.
     * 
     * @return Der akzeptierte TeamType.
     */
    public TeamType getTeamType() {
        return teamType;
    }

    /**
     * Setzt den Mannschaftstyp.
     * 
     * @param value Der Mannschaftstyp.
     */
    public void setTeamType(final TeamType value) {
        teamType = value;
    }

    // -- groups --------------------------------------------------------------

    /**
     * Liefert die Gruppen dieser Saison.
     * 
     * @return Eine Menge mit Gruppen der Saison.
     */
    public Set<GroupEntity> getGroups() {
        return groups;
    }

    /**
     * Setzt die Gruppen.
     * 
     * @param value Die Grupppen.
     */
    protected void setGroups(final Set<GroupEntity> value) {
        groups = value;
    }

    /**
     * Liefert die passende Gruppe in dieser Saison.
     * 
     * @param      groupType Ein Gruppentyp.
     * @return               Die passende Gruppe. Kann <code>null</code> liefern, wenn keine passende Gruppe gefunden.
     * @deprecated           Better use a service method.
     */
    public GroupEntity getGroup(final GroupTypeEntity groupType) {
        for (GroupEntity group : getGroups()) {
            if (group.getGroupType().equals(groupType)) {
                return group;
            }
        }
        return null;
    }

    /**
     * Fügt eine weitere Gruppe der Saison hinzu.
     * 
     * @param group Eine weitere Gruppe.
     */
    public void addGroup(final GroupEntity group) {
        Objects.requireNonNull(group);
        group.setSeason(this);
        groups.add(group);
    }

    /**
     * Fügt eine Liste von Gruppen dieser Saison hinzu.
     * 
     * @param values Eine Liste von Gruppen.
     */
    public void addGroups(final Set<GroupEntity> values) {
        Objects.requireNonNull(values);
        for (GroupEntity group : values) {
            addGroup(group);
        }
    }

    /**
     * Entfernt eine Gruppe.
     * 
     * @param group Die zu entfernende Gruppe.
     */
    public void removeGroup(final GroupEntity group) {
        Objects.requireNonNull(group.getSeason());
        Objects.requireNonNull(group.getSeason().equals(this));

        groups.remove(group);
    }

    /**
     * Deaktiviert eine Gruppe.
     * 
     * @param  groupType Der zu deaktivierende Gruppentyp.
     * @return           Das entfernte {@link GroupEntity} Objekt. Kann <code>null</code> sein, wenn kein entsprechendes
     *                   {@link GroupEntity} Objekt gefunden.
     */
    public GroupEntity removeGroup(final GroupTypeEntity groupType) {
        for (Iterator<GroupEntity> i = getGroups().iterator(); i.hasNext();) {
            GroupEntity tmp = i.next();
            if (tmp.getGroupType().equals(groupType)) {
                i.remove();
                return tmp;
            }
        }
        return null;
    }

    /**
     * Liefert alle Mannschaften, die über eine Gruppe an dieser Saison beteiligt sind.
     * 
     * @return Alle Mannschaften, die dieser Saison zugeordnet sind.
     */
    public Set<TeamEntity> getTeams() {
        Set<TeamEntity> teams = new HashSet<TeamEntity>();
        for (GroupEntity group : getGroups()) {
            teams.addAll(group.getTeams());
        }
        return teams;
    }

    /**
     * Liefert alle Gruppentypen, die dieser Saison zugeordnet sind.
     * 
     * @return Eine Liste mit <code>GroupType</code>.
     */
    public List<GroupTypeEntity> getGroupTypes() {
        List<GroupTypeEntity> result = new ArrayList<GroupTypeEntity>();
        for (GroupEntity group : getGroups()) {
            result.add(group.getGroupType());
        }
        return result;
    }

    // -- gameDayList ---------------------------------------------------------

    /**
     * Liefert alle Spieltage. Eine <code>List</code> von {@link GameListEntity} Objekten.
     * 
     * @return Die Spieltage.
     */
    protected List<GameListEntity> getGameList() {
        return gameList;
    }

    /**
     * Setzt die Spieltagsliste.
     * 
     * @param value Eine Spieltagsliste.
     */
    protected void setGameList(final List<GameListEntity> value) {
        Objects.requireNonNull(value);
        gameList = value;
    }

    /**
     * Liefert eine Liste aller Spieltage.
     * 
     * @return Liste aller Spieltage (GameList).
     */
    public List<GameListEntity> toGameList() {
        return Collections.unmodifiableList(gameList);
    }

    /**
     * Liefert eine Liste aller Spieltage.
     * 
     * @return Liste aller Spieltage (GameList).
     */
    public List<GameListEntity> toGameList(Predicate<GameListEntity> filter) {
        return gameList.stream().filter(filter).collect(Collectors.toList());
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
     * @param  object Das gesuchte Objekt.
     * @return        Anzahl der Spieltage.
     */
    public int indexOf(final Object object) {
        return gameList.indexOf(object);
    }

    /**
     * Übergebener GameList/Spieltag bereits in Liste enthalten?
     * 
     * @param  _gameList Die zu prüfende GameList/Spieltag
     * @return           true, vorhanden; false, nicht vorhanden.
     */
    public boolean contains(final GameListEntity _gameList) {
        return (gameList.contains(_gameList));
    }

    /**
     * Diese Methode liefert die Spieltagsliste.
     * 
     * @param      dayNr Nummer des Spieltags ( [0] .. [size() - 1] ).
     * @return           Liste aller Spiele eines Spieltags. Liefert <code>null</code> zurück, wenn keine Spieltage der
     *                   Saison/Meisterschaft zugeordnet!
     * @deprecated       Better use a service method.
     */
    public GameListEntity getGamesOfDay(final int dayNr) {
        if ((dayNr >= gameList.size()) || (dayNr < 0)) {
            throw new IndexOutOfBoundsException("dayNr '" + dayNr
                    + "' out of bounds!");
        } else if (gameList.size() == 0) {
            return null;
        }

        for (GameListEntity currGameList : gameList) {
            if (currGameList.getIndex() == StorageConst.INDEX_UNDEFINED) {
                String dateTime = DateTimeFormatter.ISO_DATE_TIME.format(currGameList.getDateTime());
                throw new IllegalStateException("current round index: "
                        + currGameList.getIndex() + "; dateTime: " + dateTime);
            } else if (currGameList.getIndex() == dayNr) {
                // Spieltag gefunden, Suche beenden und alle Spiele
                // des Spieltages in einer Liste zurückgeben.
                return currGameList;
            }
        }

        throw new IllegalStateException();
    }

    /**
     * Fügt einen neuen Spieltag der Spieltagsliste hinzu. Ein Spieltag wird immer hinten angehängt. Falls eine andere
     * Einsortierung erfolgen soll müsste entsprechend der Index in allen Spieltagen geändert werden.
     * 
     * @param  newRound                 Ein Spieltag.
     * @throws IllegalArgumentException GameListe bereits vorhanden oder ein Attribut von gameList nicht gesetzt.
     */
    public void addGameList(final GameListEntity newRound) {
        Objects.requireNonNull(newRound, "The newRound parameter is null.");
        Objects.requireNonNull(newRound.getDateTime(), "The datetime field of newRound is null.");
        Objects.requireNonNull(newRound.getGroup(), "The group field of newRound is null.");

        for (GameListEntity gl : getGameList()) {
            if (gl.getDateTime() != null
                    && gl.getDateTime().equals(newRound.getDateTime())
                    && gl.getGroup().equals(newRound.getGroup())) {

                String error = String
                        .format("Round [%s] with same date and group is already there.",
                                newRound.toString());
                throw new IllegalArgumentException(error);
            }
        }

        // ... und in Spieltagsliste aufnehmen.
        newRound.setSeason(this);
        if (newRound.getIndex() == 0) {
            newRound.setIndex(gameList.size());
        }

        gameList.add(newRound);
    }

    /**
     * Entfernt alle Spieltage.
     */
    public void removeAllGameList() {
        List<GameListEntity> tmp = new ArrayList<GameListEntity>(getGameList());
        for (GameListEntity gl : tmp) {
            gl.removeAllGame();
            removeGameList(gl);
        }
    }

    /**
     * Entfernt einen Spieltag aus der Spieltagsliste.
     * 
     * @param _gameList Ein Spieltag.
     */
    public void removeGameList(final GameListEntity _gameList) {
        Objects.requireNonNull(_gameList);

        if (!gameList.contains(_gameList)) {
            StringBuilder buf = new StringBuilder(_gameList.toString());
            buf.append(" nicht vorhanden!");
            throw new IllegalArgumentException(buf.toString());
        }

        gameList.remove(_gameList);
        _gameList.setSeason(null);
    }

    // -- Object --------------------------------------------------------------

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("Year: ").append(getReference().getYear());
        buf.append(", Name: ").append(getReference().getName());
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
        } else if (!(object instanceof SeasonEntity)) {
            return false;
        } else {
            SeasonEntity ssn = (SeasonEntity) object;
            return getReference().equals(ssn.getReference());
        }
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public final int hashCode() {
        return 37;
    }

}
