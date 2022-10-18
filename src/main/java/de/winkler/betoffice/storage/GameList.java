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

package de.winkler.betoffice.storage;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.apache.commons.lang3.Validate;

/**
 * Verwaltet alle Spiele/Games/Matches eines Spieltags/GameList/Round.
 *
 * @author by Andre Winkler
 */
@Entity
@Table(name = "bo_gamelist")
public class GameList extends AbstractStorageObject implements Comparable<GameList> {

    /** serial version */
    private static final long serialVersionUID = -3629753274439214154L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "bo_index")
    private int index;

    @Column(name = "bo_openligaid")
    private Long openligaid;

    @Column(name = "bo_datetime")
    private ZonedDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "bo_season_ref")
    private Season season;

    @ManyToOne
    @JoinColumn(name = "bo_group_ref")
    private Group group;

    @OneToMany(mappedBy = "gameList", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderColumn(name = "bo_index")
    private List<Game> gameList = new ArrayList<>();

    // -- Construction --------------------------------------------------------

    public GameList() {
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

    // -- gameList ------------------------------------------------------------

    /**
     * Liefert die Liste der Spiele.
     *
     * @return Die Liste der Spiele.
     */
    protected List<Game> getGameList() {
        return gameList;
    }

    /**
     * Setzt die Liste der Spiele.
     *
     * @param value Die Liste der Spiele.
     */
    protected void setGameList(final List<Game> value) {
        Validate.notNull(value);
        gameList = value;
    }

    /**
     * Fügt eine neue Spielpaarung dem Spieltag hinzu.
     *
     * @param value Eine Spielpaarung.
     */
    public void addGame(final Game value) {
        Validate.notNull(value);
        if (gameList.contains(value)) {
            throw new IllegalArgumentException("The game '" + value + "' already exists!");
        }

        gameList.add(value);
        value.setIndex(gameList.indexOf(value));
        value.setGameList(this);
    }

    /**
     * Entfernt alle Spielpaarungen. Funktioniert nur, wenn keine Tipps vorhanden sind.
     */
    public void removeAllGame() {
        List<Game> tmp = new ArrayList<Game>(getGameList());
        for (Game game : tmp) {
            removeGame(game);
        }
    }

    /**
     * Entfernt ein Spielpaarung aus dieser Spieltagsliste.
     *
     * @param value Die zu entfernende Spielpaarung.
     */
    public void removeGame(final Game value) {
        Validate.notNull(value);
        if (!gameList.contains(value)) {
            throw new IllegalArgumentException(value + " nicht vorhanden!");
        }

        gameList.remove(value);
        value.setGameList(null);
    }

    /**
     * Liefert eine Liste der Spiele dieses Spieltags, die zu einer bestimmten Gruppe gehören.
     *
     * @param  _group Die Gruppe deren Spiele gefragt sind.
     * @return        Eine Liste aller Spiele der gefragten Gruppe. Diese Liste kann nicht modifiziert werden.
     */
    public List<Game> toList(final Group _group) {
        return toList(game -> game.getGroup().equals(_group));
    }

    /**
     * Liefert eine Liste der Spiele dieses Spieltags, die zu einer bestimmten Gruppe gehören.
     *
     * @param  filter Filterkriterium fuer die in Frage kommenden Spiele.
     * @return        Eine Liste aller Spiele der gefragten Gruppe. Diese Liste kann nicht modifiziert werden.
     */
    public List<Game> toList(Predicate<Game> filter) {
        return gameList.stream().filter(filter).toList();
    }

    /**
     * Liefert eine nicht modifizierbare Liste aller Spiele des Spieltags.
     *
     * @return Liste aller Spiele ({@link Game}) eines Spieltags.
     */
    public List<Game> unmodifiableList() {
        return Collections.unmodifiableList(gameList);
    }

    /**
     * Hole Spiel mit Spielnummer index.
     *
     * @param  _index Index eines Spiels.
     * @return        Das Spiel mit dem gesuchten Index.
     */
    public Game get(final int _index) {
        return (gameList.get(_index));
    }

    /**
     * Liefert ein Spiel anhand der Datenbank ID.
     *
     * @param  id Die Datenbank ID
     * @return    Ein Spiel aus diesem Spieltag.
     */
    public Game getById(final long id) {
        for (Game game : gameList) {
            if (game.getId() == id) {
                return game;
            }
        }
        return null;
    }

    /**
     * Liefert die Anzahl der Spiele des Spieltages.
     *
     * @return Anzahl der Spieltage.
     */
    public int size() {
        return gameList.size();
    }

    /**
     * Abfrage auf leere Liste.
     *
     * @return true=leer, false=nicht leer.
     */
    public boolean isEmpty() {
        return gameList.isEmpty();
    }

    // -- index ---------------------------------------------------------------

    /**
     * Methode liefert den Index des Spieltags. Der Index wird berechnet und spiegelt die Position innerhalb der
     * <code>gameDayList</code> wieder.
     *
     * @return Spieltagindex mit dem Wertebereich <code>(0 .. N-1)</code>.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Setzt den Spieltagindex. Ist ohne Bedeutung, da über die Sortierung in GameDayList gesetzt. Siehe dazu die
     * Methode {@link #getIndex()}.
     *
     * @param value Der zu setzende Spieltagindex.
     *
     * @see         #getIndex()
     */
    public void setIndex(final int value) {
        if (value < 0) {
            throw new IllegalArgumentException("value is smaller than zero.");
        }
        index = value;
    }

    // -- dateTime ------------------------------------------------------------

    /**
     * Returns date and time of the game.
     *
     * @return date and time
     */
    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Ses date and time of the game.
     *
     * @param _dateTime date and time
     */
    public void setDateTime(final ZonedDateTime _dateTime) {
        dateTime = _dateTime;
    }

    // -- season --------------------------------------------------------------

    /**
     * Liefert das zugehörige <code>GameDayList</code> Objekt.
     *
     * @return Die zugehörige Saison.
     */
    public Season getSeason() {
        return season;
    }

    /**
     * Setzt die Saison/Spieltagsliste, die zu diesem Spieltag gehört. Wird gesetzt, wenn die Spieltagsliste einer
     * Saison/GameDayList zugeordnet wird. Der Aufruf erfolgt aus der Methode {@link Season#addGameList(GameList)}.
     *
     * @param value Die zugehörige Saison. Kann <code>null</code> sein. Dann wurde diese Spieltagsliste aus einer Saison
     *                  entfernt.
     *
     * @see         Season#addGameList(GameList)
     */
    public void setSeason(final Season value) {
        season = value;
    }

    // -- group ---------------------------------------------------------------

    /**
     * Liefert die zugehörige Gruppe des Spieltags.
     *
     * @return Die zugehörige Gruppe.
     */
    public Group getGroup() {
        return group;
    }

    /**
     * Setzt die zugehörige Gruppe.
     *
     * @param value Die zugehörige Gruppe. Kann <code>null</code> sein!
     */
    public void setGroup(final Group value) {
        group = value;
    }

    // -- openligaid ----------------------------------------------------------

    /**
     * Get openligadb ID.
     *
     * @return The openligadb ID
     */
    public Long getOpenligaid() {
        return openligaid;
    }

    /**
     * Set openligadb ID
     *
     * @param value The openligadb ID
     */
    public void setOpenligaid(Long value) {
        openligaid = value;
    }

    // ------------------------------------------------------------------------

    /**
     * Überprüft, ob der Spieltag vor dem übergebenen Spieltag liegt.
     *
     * @param  _gameList Der zu vergleichende Spieltag.
     * @return           true, Spieltag liegt vor dem übergebenen Spieltag; false sonst.
     */
    public boolean isBefore(final GameList _gameList) {
        if (getIndex() < _gameList.getIndex()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Überprüft, ob der Spieltag nach dem übergebenen Spieltag liegt.
     *
     * @param  gd Der zu vergleichende Spieltag.
     * @return    true, Spieltag liegt nach dem übergebenen Spieltag; false sonst.
     */
    public boolean isAfter(final GameList gd) {
        if (getIndex() > gd.getIndex()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Find the best round date of an array of matches.
     *
     * @return the best round date
     */
    public LocalDate findBestRoundDate() {
        List<ZonedDateTime> matchDates = new ArrayList<>();
        for (Game match : gameList) {
            matchDates.add(match.getDateTime());
        }

        return findBestDate(matchDates);
    }

    /**
     * Find the best round date of an array of dates.
     *
     * @param  matchDates the dates
     * @return            the best round date
     */
    public static LocalDate findBestDate(List<ZonedDateTime> matchDates) {
        if (matchDates.isEmpty()) {
            return null;
        }

        Map<LocalDate, Integer> dates = new HashMap<>();

        for (ZonedDateTime date : matchDates) {
            if (!dates.containsKey(date.toLocalDate())) {
                dates.put(date.toLocalDate(), Integer.valueOf(0));
            }

            Integer value = dates.get(date.toLocalDate());
            dates.put(date.toLocalDate(),
                    Integer.valueOf(value.intValue() + 1));
        }

        Map.Entry<LocalDate, Integer> bestDate = null;

        Set<Map.Entry<LocalDate, Integer>> values = dates.entrySet();
        for (Map.Entry<LocalDate, Integer> dateCount : values) {
            if (bestDate == null) {
                bestDate = dateCount;
            } else {
                if (dateCount.getValue() > bestDate.getValue()) {
                    bestDate = dateCount;
                }
            }
        }

        return bestDate.getKey();
    }

    // -- StorageObject -------------------------------------------------------

    /**
     * Prüft, ob die Eigenschaften dieses Objekts komplett und gültig gefüllt sind, damit es evt. Weiterverarbeitungen
     * erfahren kann. Folgende Eigenschaften müssen gesetzt sein:
     * <ul>
     * <li>dateTime</li>
     * <li>group</li>
     * </ul>
     *
     * @return true, Objekt in Ordnung; false, es ist was falsch.
     */
    public boolean isValid() {
        if (dateTime == null) {
            return false;
        } else if (group == null) {
            return false;
        } else {
            return true;
        }
    }

    // -- Object --------------------------------------------------------------

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(final GameList _round) {
        if (this.getIndex() < _round.getIndex())
            return -1;
        else if (this.getIndex() > _round.getIndex())
            return 1;
        else
            return 0;
    }

    /**
     * @see Object#toString()
     */
    public String toString() {
        return (this.getClass().getName() + "# ID: " + getId());
    }

    /**
     * @see Object#equals(java.lang.Object)
     */
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        } else if (!(object instanceof GameList)) {
            return false;
        } else {
            GameList other = (GameList) object;
            return other.getId().equals(this.getId());
        }
    }

    /**
     * @see Object#hashCode()
     */
    public int hashCode() {
        int result = 17;
        result = 37 * result + getId().hashCode();
        return result;
    }

}
