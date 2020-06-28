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
 */

package de.winkler.betoffice.storage;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.apache.commons.lang3.Validate;

import de.winkler.betoffice.storage.enums.Toto;

/**
 * Verwaltet das Ergebnis eines Fussballspiels.
 *
 * @author Andre Winkler
 */
@Embeddable
public class GameResult implements Serializable, Cloneable {

    /** serial version id */
    private static final long serialVersionUID = -4527677284167591700L;

    // -- Construction --------------------------------------------------------

    /**
     * Defaultkonstruktor. Das sogenannte Default-Ergebnis ist ein 0:0.
     */
    public GameResult() {
    }

    /**
     * Konstruktor. Wird ein unmögliches Ergebnis übergeben, wird eine
     * <code>IllegalArgumentException</code> geworfen.
     *
     * @param home
     *            Tore der Heimmannschaft.
     * @param guest
     *            Tore der Gastmannschaft.
     */
    public GameResult(final int home, final int guest) {
        Validate.isTrue(home >= 0);
        Validate.isTrue(guest >= 0);

        setHomeGoals(home);
        setGuestGoals(guest);
    }

    /**
     * Copy-Konstruktor.
     *
     * @param gameResult
     *            Das zu kopierende GameResult.
     */
    public GameResult(final GameResult gameResult) {
        this(gameResult.getHomeGoals(), gameResult.getGuestGoals());
    }

    public static GameResult of(final int home, final int guest) {
        return new GameResult(home, guest);
    }
    
    public static GameResult of(final GameResult gameResult) {
        return new GameResult(gameResult);
    }
    
    // -- homeGoals -----------------------------------------------------------

    /** Tore der Heimmannschaft. */
    private int homeGoals = 0;

    /**
     * Liefert die Tore der Heimmnannschaft.
     *
     * @return Die Tore der Heimmnannschaft.
     *
     * @hibernate.property column="bo_homegoals"
     */
    public int getHomeGoals() {
        return homeGoals;
    }

    /**
     * Setzt die Tore der Heimmannschaft.
     *
     * @param home
     *            Die Tore der Heimmannschaft.
     */
    private void setHomeGoals(final int home) {
        homeGoals = home;
    }

    // -- guestGoals ----------------------------------------------------------

    /** Tore der Gastmannschaft. */
    private int guestGoals = 0;

    /**
     * Liefert die Tore der Gastmannschaft.
     *
     * @return Die Tore der Gastmannschaft.
     *
     * @hibernate.property column="bo_guestgoals"
     */
    public int getGuestGoals() {
        return guestGoals;
    }

    /**
     * Setzt die Tore der Gastmannschaft.
     *
     * @param guest
     *            Die Tore der Gastmannschaft.
     */
    private void setGuestGoals(final int guest) {
        guestGoals = guest;
    }

    // -- toto ----------------------------------------------------------------

    /** Das Toto-Ergebnis. Dieser Wert wird berechnet. */
    @Transient
    private Toto toto = Toto.REMIS;
    @Transient
    private boolean calculated = false;

    /**
     * Liefert den Toto-Wert von diesem Spielergebnis.
     *
     * @return Der Toto-Wert.
     */
    public Toto getToto() {
        if (!calculated) {
            setToto();
            calculated = true;
        }
        return toto;
    }

    /**
     * Ermittelt den Toto-Wert für das vorliegende Ergebnis.
     */
    private void setToto() {
        if (homeGoals > guestGoals) {
            toto = Toto.HOME_WIN;
        } else if (homeGoals == guestGoals) {
            toto = Toto.REMIS;
        } else if (homeGoals < guestGoals) {
            toto = Toto.HOME_LOST;
        }
    }

    public boolean isRemis() {
        return homeGoals == guestGoals;
    }

    // -- Object --------------------------------------------------------------

    /**
     * @param result
     *            Das zu vergleichende GameResult.
     * @return <code>true</code>, wenn beide GameResult gleich;
     *         <code>false</code> bei GameResult ungleich.
     */
    public boolean compare(final GameResult result) {
        if (result == null) {
            return false;
        }

        if ((result.getHomeGoals() == homeGoals)
                && (result.getGuestGoals() == guestGoals)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        GameResult res = new GameResult();
        res.homeGoals = homeGoals;
        res.guestGoals = guestGoals;
        res.toto = toto;
        return res;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        } else if (object == null) {
            return false;
        } else if (!(object instanceof GameResult)) {
            return false;
        } else {
            GameResult gr = (GameResult) object;
            if ((getHomeGoals() == gr.getHomeGoals())
                    && (getGuestGoals() == gr.getGuestGoals())) {
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + getHomeGoals();
        result = 37 * result + getGuestGoals();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(homeGoals);
        buf.append(':');
        buf.append(guestGoals);
        return buf.toString();
    }

}
