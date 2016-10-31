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
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;

import de.awtools.basic.LoggerFactory;
import de.winkler.betoffice.storage.enums.TotoResult;
import de.winkler.betoffice.storage.exception.StorageObjectNotFoundException;
import de.winkler.betoffice.storage.exception.StorageRuntimeException;

/**
 * Verwaltet alle Spiele/Games/Matches eines Spieltags/GameList/Round.
 *
 * @author by Andre Winkler
 *
 * @hibernate.class table="bo_gamelist"
 */
public class GameList extends AbstractStorageObject
        implements Comparable<GameList> {

    /** serial version */
    private static final long serialVersionUID = -3629753274439214154L;

    /** Der private Logger der Klasse. */
    private static Logger log = LoggerFactory.make();

    // -- Construction --------------------------------------------------------

    public GameList() {
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

    // -- gameList ------------------------------------------------------------

    /** Der Name der Eigenschaft 'gameList'. */
    public static final String PROPERTY_GAMELIST = "gameList";

    /** Die Liste der Spiele. Enthält {@link Game} Objekte. */
    private List<Game> gameList = new ArrayList<Game>();

    /**
     * Liefert die Liste der Spiele.
     *
     * @return Die Liste der Spiele.
     *
     * @hibernate.list cascade="all" lazy="false"
     * @hibernate.collection-index column="bo_index"
     * @hibernate.collection-key column="bo_gamelist_ref"
     * @hibernate.collection-one-to-many class="de.winkler.betoffice.storage.Game"
     */
    protected List<Game> getGameList() {
        return gameList;
    }

    /**
     * Setzt die Liste der Spiele.
     *
     * @param value
     *            Die Liste der Spiele.
     */
    protected void setGameList(final List<Game> value) {
        Validate.notNull(value);
        gameList = value;
    }

    /**
     * Fügt eine neue Spielpaarung dem Spieltag hinzu.
     *
     * @param value
     *            Eine Spielpaarung.
     */
    public void addGame(final Game value) {
        Validate.notNull(value);
        if (gameList.contains(value)) {
            log.error("The match '" + value + "' already exists!");
            throw new IllegalArgumentException(value + " already exists!");
        }

        gameList.add(value);
        value.setGameList(this);
    }

    /**
     * Entfernt alle Spielpaarungen.
     */
    public void removeAllGame() {
        List<Game> tmp = new ArrayList<Game>(getGameList());
        for (Game game : tmp) {
            game.removeAllTipp();
            removeGame(game);
        }
    }

    /**
     * Entfernt ein Spielpaarung aus dieser Spieltagsliste.
     *
     * @param value
     *            Die zu entfernende Spielpaarung.
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
     * Liefert eine Liste der Spiele dieses Spieltags, die zu einer bestimmten
     * Gruppe gehören.
     *
     * @param _group
     *            Die Gruppe deren Spiele gefragt sind.
     * @return Eine Liste aller Spiele der gefragten Gruppe. Diese Liste kann
     *         nicht modifiziert werden.
     */
    public List<Game> unmodifiableList(final Group _group) {
        List<Game> list = new LinkedList<Game>();

        for (Game game : gameList) {
            if (game.getGroup().equals(_group)) {
                list.add(game);
            }
        }
        return Collections.unmodifiableList(list);
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
     * @param _index
     *            Index eines Spiels.
     * @return Das Spiel mit dem gesuchten Index.
     */
    public Game get(final int _index) {
        return (gameList.get(_index));
    }

    /**
     * Liefert ein Spiel anhand der Datenbank ID.
     *
     * @param id
     *            Die Datenbank ID
     * @return Ein Spiel aus diesem Spieltag.
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

    private int index;

    /**
     * Methode liefert den Index des Spieltags. Der Index wird berechnet und
     * spiegelt die Position innerhalb der <code>gameDayList</code> wieder.
     *
     * @return Spieltagindex mit dem Wertebereich <code>(0 .. N-1)</code>.
     *
     * @hibernate.property column="bo_index" insert="true" update="true"
     */
    public int getIndex() {
        return index;
    }

    /**
     * Setzt den Spieltagindex. Ist ohne Bedeutung, da über die Sortierung in
     * GameDayList gesetzt. Siehe dazu die Methode {@link #getIndex()}.
     *
     * @param value
     *            Der zu setzende Spieltagindex.
     *
     * @see #getIndex()
     */
    public void setIndex(final int value) {
        index = value;
    }

    // -- dateTime ------------------------------------------------------------

    /** date and time of game play */
    private Date dateTime;

    /**
     * Returns date and time of the game.
     *
     * @return date and time
     */
    public Date getDateTime() {
        return dateTime;
    }

    /**
     * Ses date and time of the game.
     *
     * @param _dateTime
     *            date and time
     */
    public void setDateTime(final Date _dateTime) {
        dateTime = _dateTime;
    }

    // -- season --------------------------------------------------------------

    /** Der Name der Eigenschatft 'season'. */
    public static final String PROPERTY_SEASON = "season";

    /** Die Saison des Spieltags. */
    private Season season;

    /**
     * Liefert das zugehörige <code>GameDayList</code> Objekt.
     *
     * @return Die zugehörige Saison.
     *
     * @hibernate.many-to-one column="bo_gamedaylist_ref" cascade="none"
     */
    public Season getSeason() {
        return season;
    }

    /**
     * Setzt die Saison/Spieltagsliste, die zu diesem Spieltag gehört. Wird
     * gesetzt, wenn die Spieltagsliste einer Saison/GameDayList zugeordnet
     * wird. Der Aufruf erfolgt aus der Methode
     * {@link Season#addGameList(GameList)}.
     *
     * @param value
     *            Die zugehörige Saison. Kann <code>null</code> sein. Dann wurde
     *            diese Spieltagsliste aus einer Saison entfernt.
     *
     * @see Season#addGameList(GameList)
     */
    public void setSeason(final Season value) {
        season = value;
    }

    // -- group ---------------------------------------------------------------

    /** Der Name der Eigenschaft 'group'. */
    public static final String PROPERTY_GROUP = "group";

    /** Die Gruppe des Spieltags. */
    private Group group;

    /**
     * Liefert die zugehörige Gruppe des Spieltags.
     *
     * @return Die zugehörige Gruppe.
     *
     * @hibernate.many-to-one column="bo_group_ref" cascade="none"
     */
    public Group getGroup() {
        return group;
    }

    /**
     * Setzt die zugehörige Gruppe.
     *
     * @param value
     *            Die zugehörige Gruppe. Kann <code>null</code> sein!
     */
    public void setGroup(final Group value) {
        group = value;
    }

    // -- openligaid ----------------------------------------------------------

    /** http://www.openligadb.de */
    private Long openligaid;

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
     * @param value
     *            The openligadb ID
     */
    public void setOpenligaid(Long value) {
        openligaid = value;
    }

    // ------------------------------------------------------------------------

    /**
     * Berechnet die erzielten Punkte eines Spielers für diesen Spieltag.
     *
     * @param user
     *            Der Teilnehmer für den die Punktzahl ermittelt wird.
     * @return Ein <code>UserResultOfDay</code>
     */
    public UserResultOfDay getUserPoints(User user) {
        UserResultOfDay urod = new UserResultOfDay();

        urod.setUser(user);
        for (Game game : gameList) {
            if (game.isPlayed()) {
                try {
                    GameTipp tipp = game.getGameTipp(user);
                    //
                    // Zu bemerken ist, dass alle Tipps eines Spieltags den
                    // gleichen Status besitzen müssen. Ist dies nicht der
                    // Fall, wird eine RuntimeException geworfen.
                    //
                    // TODO Dieser Fall kann aber trotzdem auftreten:
                    // Automatische MinTipp-Generierung und anschliessendes
                    // teilweises, manuelles Ändern der Tipps.
                    //
                    if ((urod.getStatus() != null)
                            && !(tipp.getStatus().equals(urod.getStatus()))) {

                        log.error("Der Tipp " + tipp + " ist fehlerhaft!");
                        throw new StorageRuntimeException(
                                "Ein Tipp wurde automatisch generiert. "
                                        + "Ein anderer Tipp wurde per Teilnehmer "
                                        + "generiert. Dieser Zustand sollte nicht "
                                        + "auftreten!");
                    }

                    urod.setStatus(tipp.getStatus());
                    urod.setTipps(urod.getTipps() + 1);

                    if (tipp.getTotoResult() == TotoResult.EQUAL) {
                        urod.setWin(urod.getWin() + 1);
                    } else if (tipp.getTotoResult() == TotoResult.TOTO) {
                        urod.setToto(urod.getToto() + 1);
                    }
                } catch (StorageObjectNotFoundException ex) {
                    // Ist Ok, dann müssen auch keine Punkte addiert werden.
                    log.info("Kein Tipp für game: " + game + " vorhanden");
                }
            }
        }
        return urod;
    }

    /**
     * Liefert alle Tipps dieses Spieltags für den gefordeten User.
     *
     * @param user
     *            Die Tipps des gesuchten Users.
     * @return Eine Liste mit allen Tipps für diesen Spieltag vom gesuchten
     *         User.
     */
    public List<GameTipp> getTippsOfUser(final User user) {
        List<GameTipp> tippList = new ArrayList<GameTipp>();
        for (Game game : gameList) {
            try {
                GameTipp tipp = game.getGameTipp(user);
                tippList.add(tipp);
            } catch (StorageObjectNotFoundException ex) {
                log.info(new StringBuffer("Für User ").append(user)
                        .append(" keinen Tipp gefunden.").toString());
            }
        }
        return tippList;
    }

    /**
     * Überprüft, ob der Spieltag vor dem übergebenen Spieltag liegt.
     *
     * @param _gameList
     *            Der zu vergleichende Spieltag.
     * @return true, Spieltag liegt vor dem übergebenen Spieltag; false sonst.
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
     * @param gd
     *            Der zu vergleichende Spieltag.
     * @return true, Spieltag liegt nach dem übergebenen Spieltag; false sonst.
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
    public Date findBestRoundDate() {
        List<Date> matchDates = new ArrayList<>();
        for (Game match : gameList) {
            matchDates.add(match.getDateTime());
        }

        return findBestDate(matchDates);
    }

    /**
     * Find the best round date of an array of dates.
     *
     * @param matchDates
     *            the dates
     * @return the best round date
     */
    public static Date findBestDate(List<Date> matchDates) {
        if (matchDates.isEmpty()) {
            return null;
        }

        Map<Date, Integer> dates = new HashMap<>();

        for (Date date : matchDates) {
            if (!dates.containsKey(date)) {
                dates.put(date, Integer.valueOf(0));
            }

            Integer value = dates.get(date);
            dates.put(date, value + 1);
        }

        Map.Entry<Date, Integer> bestDate = null;

        Set<Map.Entry<Date, Integer>> values = dates.entrySet();
        for (Map.Entry<Date, Integer> dateCount : values) {
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
     * Prüft, ob die Eigenschaften dieses Objekts komplett und gültig gefüllt
     * sind, damit es evt. Weiterverarbeitungen erfahren kann. Folgende
     * Eigenschaften müssen gesetzt sein:
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
