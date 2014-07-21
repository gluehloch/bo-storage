/*
 * $Id: UserResult.java 3782 2013-07-27 08:44:32Z andrewinkler $
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

import org.slf4j.Logger;

import de.awtools.basic.LoggerFactory;

/**
 * UserResult verwaltet den Punktestand eines Teilnehmers
 * für eine Saison.
 *
 * @author $Author: andrewinkler $
 * @version $Revision: 3782 $ $Date: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
public class UserResult extends AbstractStorageObject {

    /** serial version id */
    private static final long serialVersionUID = 3018999724329773561L;

    /** Der private Log4j Logger. */
    public static Logger log = LoggerFactory.make();

    /** Punkte für einen komplett richtigen Tipp. */
    public static long nEqualValue = 13;

    /** Punkte für einen richtigen Toto-Tipp. */
    public static long nTotoValue = 10;

    /** Punkte für einen falschen Tipp. */
    public static long nZeroValue = 0;

    /** Der Wert, mit dem das Endergebnis eines Users geteilt wird. */
    public static long nDivisor = 10;

    /** Der zugeordnete Teilnehmer/Tipper/User */
    private final User user;

    /** Die zugeordnete Meisterschaft. */
    private final Season season;

    /** Anzahl der 'Toto Tipp richtig'. */
    private int totoWin = 0;

    /** Anzahl der '100%igen'. */
    private int win = 0;

    /** Anzahl der Fahrkarten. */
    private int ticket = 0;

    /** Aktuelle Tabellenposition im Tipper-Ranking. */
    private int tabPos = 0;

    /**
     * Konstruktor.
     *
     * @param _user Der Teilnehmer.
     * @param _season Die Saison.
     */
    public UserResult(final User _user, final Season _season) {
        user = _user;
        season = _season;
    }

    /**
     * Prüft, ob die Eigenschaften dieses Objekts komplett und gültig
     * gefüllt sind, damit es evt. Weiterverarbeitungen erfahren kann.
     * Folgende Eigenschaften müssen gesetzt sein:
     * <ul>
     *  <li>user</li>
     *  <li>season</li>
     * </ul>
     * @return true, Objekt in Ordnung; false, es ist was falsch.
     */
    public boolean isValid() {
        if (user == null) {
            return false;
        } else if (season == null) {
            return false;
        } else {
            return true;
        }
    }

    public void setUserWin(final int _n) {
        win = _n;
    }

    public void setUserTotoWin(final int _n) {
        totoWin = _n;
    }

    public void setTicket(final int _n) {
        ticket = _n;
    }

    public void setTabPos(final int _n) {
        tabPos = _n;
    }

    public void setReset() {
        totoWin = 0;
        win = 0;
        ticket = 0;
        tabPos = 0;
    }

    public User getUser() {
        return user;
    }

    public Season getSeason() {
        return season;
    }

    public int getUserWin() {
        return win;
    }

    public int getUserTotoWin() {
        return totoWin;
    }

    public int getTicket() {
        return ticket;
    }

    public long getPoints() {
        return (nEqualValue * win) + (nTotoValue * totoWin);
    }

    public double getBeautyPoints() {
        return ((double) getPoints()) / ((double) nDivisor);
    }

    public int getTippedGames() {
        return win + totoWin + ticket;
    }

    public int getTabPos() {
        return tabPos;
    }

    /**
     * @see Object#toString()
     */
    public String toString() {
        StringBuilder buf = new StringBuilder("[class=UserResult; ");
        buf.append(", Season: ").append(getSeason().toString());
        buf.append(", User: ").append(user);
        buf.append(", #10 Pts: ").append(getUserTotoWin());
        buf.append(", #13 Pts: ").append(getUserWin());
        buf.append(", #Tickets: ").append(getTicket());
        buf.append(", SUM(points): ").append(getPoints());
        buf.append(", #Tipps: ").append(getTippedGames());
        buf.append(", Tabpos: ").append(getTabPos());
        buf.append("]");
        return buf.toString();
    }

    /**
     * @see Object#equals(java.lang.Object)
     */
    public boolean equals(final Object object) {
        if (object == null) {
            return false;
        } else if (!(object instanceof User)) {
            return false;
        } else {
            User user = (User) object;
            return (user.getNickName().equals(getUser().getNickName()));
        }
    }

    /**
     * @see Object#hashCode()
     */
    public int hashCode() {
        int result = 17;
        result = 37 * result + getUser().getNickName().hashCode();
        return result;
    }

}
