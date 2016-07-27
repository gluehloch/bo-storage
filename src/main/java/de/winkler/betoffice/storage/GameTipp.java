/*
 * $Id: GameTipp.java 3796 2013-08-05 03:58:02Z andrewinkler $
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

import java.util.Comparator;

import de.winkler.betoffice.storage.enums.TippStatusType;
import de.winkler.betoffice.storage.enums.TotoResult;

/**
 * Verwaltet den Spieltipp/GameTipp eines Spiels eines Teilnehmers.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3796 $ $LastChangedDate: 2013-08-05 05:58:02 +0200 (Mon, 05 Aug 2013) $
 *
 * @todo Das implizite berechnen der Tipperpunkte macht keinen Sinn mehr.
 *
 * @hibernate.class table="bo_gametipp"
 */
public class GameTipp extends AbstractStorageObject implements
        Comparator<GameTipp> {

    /** serial version id */
    private static final long serialVersionUID = -3043191976453282242L;

    // -- Construction --------------------------------------------------------

    /**
     * Ein Defaultkonstruktor. GameTipp wird nur von der Storage-Factory
     * instanziert.
     */
    protected GameTipp() {
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

    // -- tipp ----------------------------------------------------------------

    /** Das getippte Ergebnis. */
    private GameResult tipp = new GameResult();

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
     * @param value Der Tipp.
     */
    private void setTipp(final GameResult value) {
        tipp = value;
    }

    // -- user ----------------------------------------------------------------

    /** Der Spieler, der das Spiel getippt hat. */
    private User user;

    /**
     * Liefert den zugehörigen Teilnehmer.
     *
     * @return Der Teilnehmer.
     *
     * @hibernate.many-to-one
     *     column="bo_user_ref"
     *     cascade="none"
     */
    public User getUser() {
        return user;
    }

    /**
     * Setzt den User für diesen GameTipp.
     *
     * @param value Der neue User.
     * @throws IllegalArgumentException value als null-Parameter übergeben.
     */
    public void setUser(final User value) {
        user = value;
    }

    // -- game ----------------------------------------------------------------

    /** Das getippte Spiel. */
    private Game game;

    /**
     * Liefert das zugehörige Spiel.
     *
     * @return Das Spiel.
     *
     * @hibernate.many-to-one
     *     column="bo_game_ref"
     *     cascade="all"
     */
    public Game getGame() {
        return game;
    }

    /**
     * Setzt das Game für diesen GameTipp. Kann auch <code>null</code> sein.
     * Dann wurde dieser Tipp entfernt.
     *
     * @param value Die neue Game Zuordnung.
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

    /** Status des Tipps: Undefined, Invalid, Auto, User. */
    private TippStatusType status = TippStatusType.UNDEFINED;

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
     * @param value Der zu setzende Status.
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
     * @param homeGoals Der Tipp 'Tore der Heimmannschaft'.
     * @param guestGoals Der Tipp 'Tore der Gastmannschaft'.
     * @param theStatus Der Status des Tipps (User, Auto, etc.).
     */
    public void setTipp(final int homeGoals, final int guestGoals,
            final TippStatusType theStatus) {

        setStatus(theStatus);
        setTipp(new GameResult(homeGoals, guestGoals));
    }

    /**
     * Setzt den Spieltipp neu.
     *
     * @param _result Der Tipp.
     * @param _status Der Status des Tipps (User, Auto, etc.).
     */
    public void setTipp(final GameResult _result, final TippStatusType _status) {
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
     * Liefert das geschönte Ergebnis.
     *
     * @return Das geschönte Ergebnis.
     */
    public double getBeautyPoints() {
        return ((double) getPoints()) / ((double) UserResult.nDivisor);
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

        // Prüfen, ob das Spiel beendet ist.
        if (!game.isPlayed()) {
            return TotoResult.UNDEFINED;
        } else {
            // 3 Punkte Gewinn: Ergebnis richtig getippt
            if (game.getResult().equals(tipp)) {
                return TotoResult.EQUAL;
            } // 1 Punkt Gewinn: Toto richtig getippt
            else if (game.getResult().getToto().equals(tipp.getToto())) {
                return TotoResult.TOTO;
            } // nix richtig
            else {
                return TotoResult.ZERO;
            }
        }
    }

    // -- StorageObject -------------------------------------------------------

    /**
     * Prüft, ob die Eigenschaften dieses Objekts komplett und gültig
     * gefüllt sind, damit es evt. Weiterverarbeitungen erfahren kann.
     * Folgende Eigenschaften müssen gesetzt sein:
     * <ul>
     *   <li>user</li>
     *   <li>status</li>
     * </ul>
     *
     * @return true, Objekt in Ordnung; false, es ist was falsch.
     */
    public boolean isValid() {
        if (user == null) {
            return false;
        } else if (TippStatusType.UNDEFINED.equals(status)) {
            return false;
        } else {
            return true;
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

    //    public boolean equals(Object object) {
    //        if (this == object) {
    //            return true;
    //        } else if (object == null) {
    //            return false;
    //        } else if (!(object instanceof GameTipp)) {
    //            return false;
    //        } else {
    //            GameTipp _tipp = (GameTipp) object;
    //            if ((_tipp.getUser().equals(getUser()))
    //                    && (_tipp.getGame().equals(getGame()))
    //                    && (_tipp.getStatus().equals(getStatus()))
    //                    && (_tipp.getTipp().equals(getTipp()))) {
    //                return true;
    //            } else {
    //                return false;
    //            }
    //        }
    //    }
    //
    //    public int hashCode() {
    //        int result = 17;
    //        result = 37 * result + getUser().hashCode();
    //        result = 37 * result + getGame().hashCode();
    //        result = 37 * result + getStatus().hashCode();
    //        result = 37 * result + getTipp().hashCode();
    //        return result;
    //    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("Tipp: ");
        buf.append(tipp);
        buf.append(" Status: ");
        buf.append(status);
        return buf.toString();
    }

}
