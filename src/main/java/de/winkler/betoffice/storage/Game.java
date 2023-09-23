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

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

/**
 * Kapselt alle Daten eines Fussballspiels.
 *
 * @author by Andre Winkler
 */
@Entity
@Table(name = "bo_game")
public class Game extends AbstractStorageObject implements Comparable<Game> {

    /** serial version id */
    private static final long serialVersionUID = 8861153553430553696L;

    /** Der Leerstring. */
    public static final String EMPTY = "";

    /** Punkte für ein gewonnenes Spiel. */
    public static final int WIN = 3;

    /** Punkte für ein verlorenes Spiel. */
    public static final int LOST = 0;

    /** Punkte für ein Remis. */
    public static final int REMIS = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    /** date and time of game play */
    @Column(name = "bo_datetime")
    private ZonedDateTime dateTime;

    /** Die zugehörige Gruppe. */
    @ManyToOne
    @JoinColumn(name = "bo_group_ref")
    private Group group;

    /** Die Heimmannschaft. */
    @ManyToOne
    @JoinColumn(name = "bo_hometeam_ref")
    private Team homeTeam;

    /** Die Gastmannschaft. */
    @ManyToOne
    @JoinColumn(name = "bo_guestteam_ref")
    private Team guestTeam;

    /** Spiel beendet? */
    @Column(name = "bo_isplayed")
    private boolean played = false;

    /** Das Spielergebnis. */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(column = @Column(name = "bo_homegoals"), name = "homeGoals"),
            @AttributeOverride(column = @Column(name = "bo_guestgoals"), name = "guestGoals")
    })
    private GameResult result = new GameResult();

    /** The half-time goals. */
    @AttributeOverrides({
            @AttributeOverride(column = @Column(name = "bo_halftimehomegoals"), name = "homeGoals"),
            @AttributeOverride(column = @Column(name = "bo_halftimeguestgoals"), name = "guestGoals")
    })
    private GameResult halfTimeGoals = new GameResult();

    /** The over-time goals. */
    @AttributeOverrides({
            @AttributeOverride(column = @Column(name = "bo_overtimehomegoals"), name = "homeGoals"),
            @AttributeOverride(column = @Column(name = "bo_overtimeguestgoals"), name = "guestGoals")
    })
    private GameResult overTimeGoals = new GameResult();

    /** The penalty goals after over-time. */
    @AttributeOverrides({
            @AttributeOverride(column = @Column(name = "bo_penaltyhomegoals"), name = "homeGoals"),
            @AttributeOverride(column = @Column(name = "bo_penaltyguestgoals"), name = "guestGoals")
    })
    private GameResult penaltyGoals = new GameResult();

    @ManyToOne
    @JoinColumn(name = "bo_location_ref")
    private Location location;

    @Column(name = "bo_index")
    private int index;

    @ManyToOne
    @JoinColumn(name = "bo_gamelist_ref")
    private GameList gameList;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    @OrderBy("bo_index")
    private List<Goal> goals = new ArrayList<>();

    /** http://www.openligadb.de */
    @Column(name = "bo_openligaid")
    private Long openligaid;

    /** Ist das ein sogenanntes KO Spiel? Pokalspiel? */
    @Column(name = "bo_ko")
    private boolean ko = false;

    // -- Construction --------------------------------------------------------

    public Game() {
    }

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
     * @param _dateTime
     *            date and time
     */
    public void setDateTime(final ZonedDateTime _dateTime) {
        dateTime = _dateTime;
    }

    // -- group ---------------------------------------------------------------

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

    public GameResult getOverTimeGoals() {
        return overTimeGoals;
    }

    public void setOverTimeGoals(GameResult _overTimeGoals) {
        overTimeGoals = _overTimeGoals;
    }

    public void setOverTimeGoals(int homeGoals, int guestGoals) {
        setOverTimeGoals(new GameResult(homeGoals, guestGoals));
    }

    // -- penaltyGoals --------------------------------------------------------

    public GameResult getPenaltyGoals() {
        return penaltyGoals;
    }

    public void setPenaltyGoals(GameResult _penaltyGoals) {
        penaltyGoals = _penaltyGoals;
    }

    public void setPenaltyGoals(int homeGoals, int guestGoals) {
        setPenaltyGoals(new GameResult(homeGoals, guestGoals));
    }

    // -- location -------------------------------------------------------------

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location _location) {
        location = _location;
    }

    // -- played --------------------------------------------------------------

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
        return gameList;
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
        gameList = value;
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
        return index;
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
    protected void setIndex(final int value) {
        index = value;
    }

    // -- goals ---------------------------------------------------------------

    public List<Goal> getGoals() {
        return goals;
    }

    protected void setGoals(List<Goal> _goals) {
        goals = _goals;
    }

    public void addGoal(Goal goal) {
        goals.add(goal);
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
     * @param value
     *            The openligadb ID
     */
    public void setOpenligaid(Long value) {
        openligaid = value;
    }

    // -- KO Game -------------------------------------------------------------

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

    @Override
    public int hashCode() {
        return 13;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Game other = (Game) obj;
        return id != null && id.equals(other.getId());
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
    
    public String debug() {
        StringBuilder buf = new StringBuilder();
        buf.append("ID=").append(getId()).append(", ");

        if (getDateTime() == null) {
            buf.append("Date: <null>");
        } else {
            buf.append("Date: ");
            buf.append(getDateTime());
        }
        buf.append(", ");

        if (getGroup() == null) {
            buf.append("Group: <null>");
        } else {
            buf.append(getGroup());
        }
        buf.append(", ");

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

        buf.append(", Result=");
        buf.append(result);

        return buf.toString();
    }

}
