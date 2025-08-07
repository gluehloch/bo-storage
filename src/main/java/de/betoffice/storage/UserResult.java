/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2022 by Andre Winkler. All rights reserved.
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

package de.betoffice.storage;

import org.slf4j.Logger;

import de.betoffice.util.LoggerFactory;

/**
 * UserResult verwaltet den Punktestand eines Teilnehmers für eine Saison.
 *
 * @author Andre Winkler
 */
public class UserResult {

    /** Der private Log4j Logger. */
    public static Logger log = LoggerFactory.make();

    /** Punkte für einen komplett richtigen Tipp. */
    public static final long nEqualValue = 13;

    /** Punkte für einen richtigen Toto-Tipp. */
    public static final long nTotoValue = 10;

    /** Punkte für einen falschen Tipp. */
    public static final long nZeroValue = 0;

    /** Der zugeordnete Teilnehmer/Tipper/User */
    private final User user;

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
     */
    public UserResult(final User _user) {
        user = _user;
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
        buf.append("User: ").append(user);
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
            return (user.getNickname().equals(getUser().getNickname()));
        }
    }

    /**
     * @see Object#hashCode()
     */
    public int hashCode() {
        int result = 17;
        result = 37 * result + getUser().getNickname().hashCode();
        return result;
    }

}
