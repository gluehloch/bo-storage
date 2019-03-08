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

import java.util.Comparator;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.winkler.betoffice.storage.enums.TippStatusType;
import de.winkler.betoffice.storage.enums.TotoResult;

/**
 * Verwaltet den Spieltipp/GameTipp eines Spiels eines Teilnehmers.
 *
 * @author by Andre Winkler
 *
 * @todo Das implizite berechnen der Tipperpunkte macht keinen Sinn mehr.
 */
@Entity
@Table(name = "bo_gametipp")
public class GameTipp extends AbstractStorageObject
        implements Comparator<GameTipp> {

    /** serial version id */
    private static final long serialVersionUID = -3043191976453282242L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(column = @Column(name = "bo_homegoals"), name = "homeGoals"),
            @AttributeOverride(column = @Column(name = "bo_guestgoals"), name = "guestGoals")
    })
    private GameResult tipp = new GameResult();

    @Column(name = "bo_create")
    private Date creationTime;

    @Column(name = "bo_update")
    private Date lastUpdateTime;

    @ManyToOne
    @JoinColumn(name = "bo_user_ref")
    private User user;

    @Column(name = "bo_token")
    private String token;

    @ManyToOne
    @JoinColumn(name = "bo_game_ref")
    private Game game;

    @Enumerated
    @Column(name = "bo_status")
    private TippStatusType status = TippStatusType.UNDEFINED;

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

    // -- tipp ----------------------------------------------------------------

    /**
     * Liefert den Tipp.
     *
     * @return Der Tipp.
     *
     * @hibernate.component
     */
    public GameResult getTipp() {
        return tipp;
    }

    /**
     * Setzt den Spieltipp.
     *
     * @param value
     *            Der Tipp.
     */
    private void setTipp(final GameResult value) {
        tipp = value;
    }

    // -- createTime ----------------------------------------------------------

    /**
     * Date and time of the creation time point.
     * 
     * @return Date and time of the creation time point.
     */
    public Date getCreationTime() {
        return creationTime;
    }

    /**
     * The creation time point.
     * 
     * @param date
     *            the creation time point
     */
    public void setCreationTime(Date date) {
        creationTime = date;
    }

    // -- updateTime ----------------------------------------------------------

    /**
     * Date and time of the last update
     * 
     * @return date and time of the last update
     */
    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * Set a new update time point
     * 
     * @param _lastUpdateTime
     *            the last update time point
     */
    public void setLastUpdateTime(Date _lastUpdateTime) {
        lastUpdateTime = _lastUpdateTime;
    }

    // -- user ----------------------------------------------------------------

    /**
     * Liefert den zugehörigen Teilnehmer.
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

    // -- token ---------------------------------------------------------------

    /**
     * Liefert das Anmelde-Token mit dem dieser Tipp angelegt wurde.
     * 
     * @return Anmeldetoken. Verknüpft mit Session/bo_session.
     */
    public String getToken() {
        return token;
    }

    /**
     * Setzt das Token mit dem dieser Spieltipp angelegt wurde.
     * 
     * @param _token
     *            Anmeldetoken
     */
    public void setToken(String _token) {
        token = _token;
    }

    // -- game ----------------------------------------------------------------

    /**
     * Liefert das zugehörige Spiel.
     *
     * @return Das Spiel.
     *
     * @hibernate.many-to-one column="bo_game_ref" cascade="all"
     */
    public Game getGame() {
        return game;
    }

    /**
     * Setzt das Game für diesen GameTipp. Kann auch <code>null</code> sein.
     * Dann wurde dieser Tipp entfernt.
     *
     * @param value
     *            Die neue Game Zuordnung.
     */
    protected void setGame(final Game value) {
        game = value;
    }

    // -- totoResult ----------------------------------------------------------

    /**
     * Liefert den Wert-Zustand des Tipps.
     *
     * @return Der Wert-Zustand der Tipps.
     */
    public TotoResult getTotoResult() {
        return calcTippResult();
    }

    // -- status --------------------------------------------------------------

    /**
     * Liefert den Status des Tipps (User, Auto, etc.).
     *
     * @return Der Status.
     *
     * @hibernate.property column="bo_status"
     */
    public TippStatusType getStatus() {
        return status;
    }

    /**
     * Setzt den Status des Tipps (User, Auto, etc.).
     *
     * @param value
     *            Der zu setzende Status.
     */
    private void setStatus(final TippStatusType value) {
        status = value;
    }

    /**
     * Ungültiger Spieltipp?
     *
     * @return <code>true</code>, wenn GameTippStatus == INVALID.
     */
    public boolean isInvalid() {
        return (TippStatusType.INVALID.equals(getStatus()));
    }

    // ------------------------------------------------------------------------

    /**
     * Setzt den Spieltipp neu.
     *
     * @param homeGoals
     *            Der Tipp 'Tore der Heimmannschaft'.
     * @param guestGoals
     *            Der Tipp 'Tore der Gastmannschaft'.
     * @param theStatus
     *            Der Status des Tipps (User, Auto, etc.).
     */
    public void setTipp(final int homeGoals, final int guestGoals,
            final TippStatusType theStatus) {

        setStatus(theStatus);
        setTipp(new GameResult(homeGoals, guestGoals));
    }

    /**
     * Setzt den Spieltipp neu.
     *
     * @param _result
     *            Der Tipp.
     * @param _status
     *            Der Status des Tipps (User, Auto, etc.).
     */
    public void setTipp(final GameResult _result,
            final TippStatusType _status) {
        setStatus(_status);
        setTipp(_result);
    }

    /**
     * Rückgabe der erzielten Punkte des Teilnehmers für das getippte Spiel.
     *
     * @return Punktestand für dieses Spiel.
     */
    public long getPoints() {
        if (getTotoResult().equals(TotoResult.EQUAL)) {
            return UserResult.nEqualValue;
        } else if (getTotoResult().equals(TotoResult.TOTO)) {
            return UserResult.nTotoValue;
        } else if (getTotoResult().equals(TotoResult.ZERO)) {
            return UserResult.nZeroValue;
        } else if (getTotoResult().equals(TotoResult.UNDEFINED)) {
            return UserResult.nZeroValue;
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Berechnet den Toto-Wert des Tipps.
     *
     * @return Der Toto-Wert für diesen Tipp.
     */
    private TotoResult calcTippResult() {
        if (game == null || tipp == null) {
            return TotoResult.UNDEFINED;
        }

        if (!game.isPlayed()) {
            return TotoResult.UNDEFINED;
        }

        if (game.isKo()) {
            if (!game.getResult().isRemis()) {
                // In der normalen Spielzeit beendet.
                return calcTotoResult(game.getResult());
            } else if (game.getResult().isRemis()
                    && !game.getOverTimeGoals().isRemis()) {
                // Verlaengerung
                return calcTotoResult(game.getOverTimeGoals());
            } else if (game.getResult().isRemis()
                    && game.getOverTimeGoals().isRemis()) {
                // Elfmeterschiessen
                return calcTotoResult(game.getPenaltyGoals());
            }
        }

        return calcTotoResult(game.getResult());
    }

    private TotoResult calcTotoResult(GameResult gameResult) {
        if (gameResult.equals(tipp)) {
            return TotoResult.EQUAL; // 13 Punkte
        } else if (gameResult.getToto().equals(tipp.getToto())) {
            return TotoResult.TOTO; // 10 Punkte
        } else {
            return TotoResult.ZERO; // 0 Punkte
        }
    }

    // -- Comparator ----------------------------------------------------------

    @Override
    public int compare(GameTipp o1, GameTipp o2) {
        if (o1.getPoints() > o2.getPoints()) {
            return 1;
        } else if (o1.getPoints() == o2.getPoints()) {
            return 0;
        } else {
            return -1;
        }
    }

    // -- Object --------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "GameTipp [id=" + id + ", tipp=" + tipp + ", creationTime="
                + creationTime + ", lastUpdateTime="
                + lastUpdateTime + ", user=" + user + ", game=" + game
                + ", status=" + status + "]";
    }

}
