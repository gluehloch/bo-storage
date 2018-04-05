/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2017 by Andre Winkler. All
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
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import de.winkler.betoffice.storage.enums.TippStatusType;
import de.winkler.betoffice.storage.exception.StorageObjectExistsException;
import de.winkler.betoffice.storage.exception.StorageObjectNotFoundException;
import de.winkler.betoffice.storage.exception.StorageObjectNotValidException;
import de.winkler.betoffice.storage.exception.StorageRuntimeException;

/**
 * Kapselt alle Daten eines Fussballspiels.
 *
 * @author by Andre Winkler
 *
 * @hibernate.class table="bo_game"
 */
public class Game extends AbstractStorageObject implements Comparable<Game> {

    /** serial version id */
    private static final long serialVersionUID = 8861153553430553696L;

    /** Der private Logger der Klasse. */
    private static Log log = LogFactory.getLog(Game.class);

    /** Der Leerstring. */
    public static final String EMPTY = "";

    /** Punkte für ein gewonnenes Spiel. */
    public static final int WIN = 3;

    /** Punkte für ein verlorenes Spiel. */
    public static final int LOST = 0;

    /** Punkte für ein Remis. */
    public static final int REMIS = 1;

    // -- Construction --------------------------------------------------------

    public Game() {
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

    // -- group ---------------------------------------------------------------

    /** Die zugehörige Gruppe. */
    private Group group;

    /**
     * Liefert die Gruppe zu diesem Spiel.
     *
     * @return Die Gruppe.
     *
     * @hibernate.many-to-one column="bo_group_ref" cascade="none"
     */
    public Group getGroup() {
        return group;
    }

    /**
     * Setzt die Gruppe zu diesem Spiel.
     *
     * @param value
     *            Die Gruppe.
     */
    public void setGroup(final Group value) {
        group = value;
    }

    // -- homeTeam ------------------------------------------------------------

    /** Die Heimmannschaft. */
    private Team homeTeam;

    /**
     * Liefert die Heimmannschaft.
     *
     * @return Die Heimmannschaft.
     *
     * @hibernate.many-to-one column="bo_hometeam_ref" cascade="none"
     */
    public Team getHomeTeam() {
        return homeTeam;
    }

    /**
     * Setzt die Heimmannschaft.
     *
     * @param value
     *            Die Heimmannschaft.
     */
    public void setHomeTeam(final Team value) {
        homeTeam = value;
    }

    // -- guestTeam -----------------------------------------------------------

    /** Die Gastmannschaft. */
    private Team guestTeam;

    /**
     * Liefert die Gastmannschaft.
     *
     * @return Die Gastmannschaft.
     *
     * @hibernate.many-to-one column="bo_guestteam_ref" cascade="none"
     */
    public Team getGuestTeam() {
        return guestTeam;
    }

    /**
     * Setzt die Gastmannschaft.
     *
     * @param value
     *            Die Gastmannschaft.
     */
    public void setGuestTeam(final Team value) {
        guestTeam = value;
    }

    // -- result --------------------------------------------------------------

    /** Das Spielergebnis. */
    private GameResult result = new GameResult();

    /**
     * Liefert das Spielergebniss.
     *
     * @return Das Spielergebniss.
     *
     * @hibernate.component
     */
    public GameResult getResult() {
        return result;
    }

    /**
     * Setzt das Spielergebnis.
     *
     * @param value
     *            Ein Spielergebnis.
     */
    public void setResult(final GameResult value) {
        result = value;
    }

    /**
     * Zuweisung eines Ergebnisses zu diesem Spiel.
     *
     * @param homeGoals
     *            Die Tore der Heimmannschaft.
     * @param guestGoals
     *            Die Tore der Gastmannschaft.
     *
     * @see #setResult(GameResult)
     */
    public void setResult(final int homeGoals, final int guestGoals) {
        setResult(new GameResult(homeGoals, guestGoals));
    }

    /**
     * Zuweisung eines Ergebnisses zu diesem Spiel.
     *
     * @param homeGoals
     *            Die Tore der Heimmannschaft.
     * @param guestGoals
     *            Die Tore der Gastmannschaft.
     * @param finished
     *            Spiel beendet?
     *
     * @see #setResult(GameResult)
     */
    public void setResult(final int homeGoals, final int guestGoals,
            final boolean finished) {

        GameResult newValue = new GameResult(homeGoals, guestGoals);
        setResult(newValue);
        setPlayed(finished);
    }

    // -- halfTimeGoals -------------------------------------------------------

    /** The half-time goals. */
    private GameResult halfTimeGoals = new GameResult();

    public GameResult getHalfTimeGoals() {
        return halfTimeGoals;
    }

    public void setHalfTimeGoals(GameResult _halfTimeGoals) {
        halfTimeGoals = _halfTimeGoals;
    }

    public void setHalfTimeGoals(int homeGoals, int guestGoals) {
        setHalfTimeGoals(new GameResult(homeGoals, guestGoals));
    }

    // -- overTimeGoals -------------------------------------------------------

    /** The over-time goals. */
    private GameResult overTimeGoals = new GameResult();

    public GameResult getOverTimeGoals() {
        return overTimeGoals;
    }

    public void setOverTimeGoals(GameResult _overTimeGoals) {
        overTimeGoals = _overTimeGoals;
    }

    // -- penaltyGoals --------------------------------------------------------

    /** The penalty goals after over-time. */
    private GameResult penaltyGoals = new GameResult();

    public GameResult getPenaltyGoals() {
        return penaltyGoals;
    }

    public void setPenaltyGoals(GameResult _penaltyGoals) {
        penaltyGoals = _penaltyGoals;
    }

    // -- location -------------------------------------------------------------

    /** The location of the match. */
    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location _location) {
        location = _location;
    }

    // -- played --------------------------------------------------------------

    /** Spiel beendet? */
    private boolean played = false;

    /**
     * 'Spiel beendet'.
     *
     * @return true, Spiel beendet; false, sonst.
     *
     * @hibernate.property column="bo_isplayed"
     */
    public boolean isPlayed() {
        return played;
    }

    /**
     * Setzt 'Spiel beendet'.
     *
     * @param value
     *            true, Spiel beendet; false, sonst.
     */
    public void setPlayed(final boolean value) {
        played = value;
    }

    // -- ofGameList ----------------------------------------------------------

    /** Zugehöriger Spieltag. */
    private GameList ofGameList;

    /**
     * Liefert den Spieltag für dieses Spiel.
     *
     * @return Der Spieltag.
     *
     * @hibernate.many-to-one column="bo_gamelist_ref" cascade="none"
     *                        not-null="false"
     *                        class="de.winkler.betoffice.storage.GameList"
     */
    public GameList getGameList() {
        return ofGameList;
    }

    /**
     * Setzt den Spieltag für dieses Spiel. Wird gesetzt, wenn Game einer
     * GameList zugeordnet wird, d.h. der Aufruf erfolgt aus der Methode
     * {@link GameList#addGame(Game)}.
     *
     * @param value
     *            Der Spieltag. Der Parameter kann <code>null</code> sein. In
     *            diesem Fall wird eine Spielpaarung aus einem Spieltag
     *            gelöscht.
     *
     * @see GameList#addGame(Game)
     */
    protected void setGameList(final GameList value) {
        ofGameList = value;
    }

    // -- index ---------------------------------------------------------------

    /**
     * Liefert die Ordnungsnummer dieses Spiels für den Spieltag.
     *
     * @return Die Ordnungsnummer.
     *
     * @hibernate.property column="bo_index"
     */
    public int getIndex() {
        if (getGameList() == null) {
            return StorageConst.INDEX_UNDEFINED;
        } else if (!(getGameList().getGameList().contains(this))) {
            return StorageConst.INDEX_UNDEFINED;
        } else {
            return getGameList().getGameList().indexOf(this);
        }
    }

    /**
     * Setzt die Ordnungsnummer dieses Spiels für einen Spieltag.
     * <strong>Achtung:</strong> Diese Methode sollte nicht aufgerufen werden,
     * da sich dieser Wert aus der Position innerhalb der <code>GameList</code>
     * ermittelt.
     *
     * @param value
     *            Die Ordnungsnummer.
     */
    public void setIndex(final int value) {
        // not used, calculated value, see getIndex() method
    }

    // -- goals ---------------------------------------------------------------

    private List<Goal> goals = new ArrayList<Goal>();

    public List<Goal> getGoals() {
        return goals;
    }

    protected void setGoals(List<Goal> _goals) {
        goals = _goals;
    }

    public void addGoal(Goal goal) {
        goals.add(goal);
    }

    public List<Goal> removeAllGoals() {
        List<Goal> removedGoals = new ArrayList<>();
        for (Goal goal : goals) {
            goal.getPlayer().getGoals().remove(goal);
            removedGoals.add(goal);
        }
        goals.clear();
        return removedGoals;
    }

    // -- tippList ------------------------------------------------------------

    /** Liste mit den Tipps der User zu diesem Spiel. */
    private List<GameTipp> tippList = new ArrayList<GameTipp>();

    /**
     * Liefert die Liste der Tipps.
     *
     * @return Die Tipp-Liste.
     *
     * @hibernate.list cascade="all" lazy="false"
     * @hibernate.collection-index column="bo_tipps_index"
     * @hibernate.collection-key column="bo_game_ref"
     * @hibernate.collection-one-to-many class="de.winkler.betoffice.storage.GameTipp"
     */
    protected List<GameTipp> getTippList() {
        return tippList;
    }

    /**
     * Setzt die Tipp-Liste.
     *
     * @param value
     *            Die Tipp-Liste.
     */
    protected void setTippList(final List<GameTipp> value) {
        tippList = value;
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

    // -- KO Game -------------------------------------------------------------

    /** Ist das ein sogenanntes KO Spiel? Pokalspiel? */
    private boolean ko = false;

    /**
     * Ist das ein KO Spiel?
     *
     * @return true, dann ist das ein KO Spiel
     *
     * @hibernate.property column="bo_ko"
     */
    public boolean isKo() {
        return ko;
    }

    /**
     * Setzt das Flag fuer ein KO Spiel.
     *
     * @param value
     *            true, dann ist das ein KO Spiel.
     */
    public void setKo(final boolean value) {
        ko = value;
    }

    // ------------------------------------------------------------------------

    /**
     * Fügt dem Spiel einen Tipp hinzu. Bestehende Tipps werden überschrieben.
     *
     * @param token
     *            Das Anmeldetoken mit dem dieser Spieltipp angelegt wird.
     * @param user
     *            Der Spieler von dem der Tipp kommt.
     * @param gr
     *            Der Tipp des Spielers.
     * @param status
     *            Der Status des Tipps.
     * @return Ein neu erzeugter Tipp oder ein bereits abgegebener Tipp.
     */
    public GameTipp addTipp(String token, User user, GameResult gr,
            TippStatusType status) {

        Validate.notNull(token);
        Validate.notNull(user);
        Validate.notNull(gr);
        Validate.notNull(status);

        GameTipp tipp = null;
        if (containsTipp(user)) {
            try {
                tipp = getGameTipp(user);
                tipp.setToken(token);
                tipp.setTipp(gr, status);
                tipp.setLastUpdateTime(DateTime.now().toDate());
            } catch (StorageObjectNotFoundException ex) {
                // Nach Abfrage nicht möglich!
                log.error("storage object not found exception", ex);
                throw new StorageRuntimeException(ex);
            }
        } else {
            tipp = new GameTipp();
            tipp.setToken(token);
            tipp.setUser(user);
            tipp.setGame(this);
            tipp.setTipp(gr, status);
            Date now = DateTime.now().toDate();
            tipp.setLastUpdateTime(now);
            tipp.setCreationTime(now);

            try {
                addTipp(tipp);
            } catch (StorageObjectExistsException ex) {
                // Nach Abfrage nicht möglich!
                log.error("storage object exists exception", ex);
                throw new StorageRuntimeException(ex);
            } catch (StorageObjectNotValidException ex) {
                // Selbst zusammen gebaut!
                log.error("storage object not valid exception", ex);
                throw new StorageRuntimeException(ex);
            }
        }
        return tipp;
    }

    /**
     * Fügt diesem Spiel einen neuen Tipp hinzu.
     *
     * @param tipp
     *            Der neue Tipp.
     * @throws StorageObjectExistsException
     *             Tipp bereits vorhanden.
     */
    public void addTipp(final GameTipp tipp)
            throws StorageObjectExistsException {
        Validate.notNull(tipp);

        if (containsTipp(tipp.getUser())) {
            StringBuilder buf = new StringBuilder("'");
            buf.append(tipp.toString()).append("' already exists!");
            log.error(buf);
            throw new StorageObjectExistsException(buf.toString());
        } else if (tipp.getGame() != this) { // TODO equals() wäre besser?
            StringBuilder buf = new StringBuilder("'");
            buf.append(tipp.toString()).append("' game property failure!");
            log.error(buf);
            throw new IllegalArgumentException(buf.toString());
        } else {
            tipp.setGame(this);
            tippList.add(tipp);
        }
    }

    /**
     * Entfernt alle Tipps zu diesem Spiel.
     */
    public void removeAllTipp() {
        List<GameTipp> tmp = new ArrayList<GameTipp>(getTippList());
        for (GameTipp tipp : tmp) {
            try {
                removeTipp(tipp);
            } catch (StorageObjectNotFoundException ex) {
                log.fatal("Error", ex);
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Entfernt aus Game einen Tipp.
     *
     * @param tipp
     *            Der zu entfernende Tipp.
     * @throws StorageObjectNotFoundException
     *             Tipp nicht vorhanden.
     */
    public void removeTipp(final GameTipp tipp)
            throws StorageObjectNotFoundException {

        Validate.notNull(tipp, "tipp als null-Parameter");

        if (!containsTipp(tipp.getUser())) {
            StringBuffer buf = new StringBuffer();
            buf.append(tipp.toString());
            buf.append(" nicht vorhanden.");
            log.error(buf);
            throw new StorageObjectNotFoundException(buf.toString());
        } else {
            tippList.remove(tipp);
            tipp.setGame(null);
        }
    }

    /**
     * Ermittelt für einen Spieler den für dieses Spiel abgegebenen Tipp.
     *
     * @param user
     *            Der gesuchte Tipp von diesem Spieler.
     * @return Der abgegebene Tipp des Spielers.
     * @throws StorageObjectNotFoundException
     *             Keinen Tipp für gesuchten User gefunden.
     */
    public GameTipp getGameTipp(final User user)
            throws StorageObjectNotFoundException {
        Validate.notNull(user, "user als null Parameter");

        Optional<GameTipp> userTipp = tippList.stream()
                .filter(tipp -> tipp != null && tipp.getUser().equals(user))
                .findFirst();

        if (userTipp.isPresent()) {
            return userTipp.get();
        } else {
            throw new StorageObjectNotFoundException(
                    "Der Teilnehmer [" + user.getNickName()
                            + "] hat keinen Spieltipp.");
        }
    }

    /**
     * Liefert einen Spieltipp oder, wenn kein Tipp gefunden werden konnte,
     * einen ungültigen Spieltipp.
     *
     * @param user
     *            Den Tipp von diesem Teilnehmer suchen.
     * @return Der Tipp.
     */
    public GameTipp getGameTippOrInvalid(final User user) {
        GameTipp gameTipp = null;
        try {
            gameTipp = getGameTipp(user);
        } catch (StorageObjectNotFoundException ex) {
            gameTipp = new GameTipp();
            gameTipp.setGame(this);
            gameTipp.setUser(user);
            gameTipp.setTipp(0, 0, TippStatusType.INVALID);
        }
        return gameTipp;
    }

    /**
     * Prüft, ob der Teilnehmer user einen Tipp abgegeben hat.
     *
     * @param user
     *            Der zu prüfende User.
     * @return true, Tipp vorhanden; false, kein Tipp vorhanden.
     */
    public boolean containsTipp(User user) {
        return tippList.stream().anyMatch(tipp -> tipp.getUser().equals(user));
    }

    /**
     * Gibt den Tipp an Position <code>index</code> zurück.
     *
     * @param index
     *            Index des Tipps.
     * @return Der Tipp an Position <code>index</code>.
     */
    public GameTipp getGameTipp(final int index) {
        return (tippList.get(index));
    }

    /**
     * Liefert die Anzahl der abgegebenen Tipps für dieses Spiel.
     *
     * @return Anzahl der Tipps.
     */
    public int tippSize() {
        return tippList.size();
    }

    /**
     * Liefert ein nicht-modifizierbare Liste aller Tipps dieses Spiels.
     *
     * @return Eine nicht modifizierbare Kopie der internen Tipp-Liste.
     */
    public List<GameTipp> getTipps() {
        return Collections.unmodifiableList(tippList);
    }

    // -- Comparable ----------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(final Game _game) {
        if (this.getIndex() < _game.getIndex())
            return -1;
        else if (this.getIndex() > _game.getIndex())
            return 1;
        else
            return 0;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();

        if (getDateTime() == null) {
            buf.append("Date: <null>");
        } else {
            buf.append("Date: ");
            buf.append(getDateTime());
        }

        buf.append("; ");
        if (getGroup() == null) {
            buf.append("Group: <null>");
        } else {
            buf.append(getGroup());
        }

        buf.append("; ");
        if (homeTeam == null) {
            buf.append("HTeam: <null>");
        } else {
            buf.append(homeTeam.getName());
        }

        buf.append(':');

        if (guestTeam == null) {
            buf.append("GTeam: <null>");
        } else {
            buf.append(guestTeam.getName());
        }

        buf.append("; ");
        buf.append(result);

        return buf.toString();
    }

}
