/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2017 by Andre Winkler. All rights reserved.
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

import de.winkler.betoffice.storage.enums.TippStatusType;

import org.apache.commons.lang.Validate;

/**
 * Verwaltet die Tippstatistik eines Teilnehmers für einen
 * Spieltag.
 * 
 * @author Andre Winkler
 */
public class UserResultOfDay {

    /** Spieltag wurde vom Tipper getippt? */
    private boolean isTipped = false;
    
    /** Anzahl der richtigen Tipps (ohne Toto-Tipps). */
    private int win = 0;
    
    /** Anzahl der richtigen Toto-Tipps. */
    private int toto = 0;
    
    /** Anzahl der zu tippende Spiele des Spieltags. */
    private int tipps = 0;
    
    /** Der Tipper. */
    private User user;

    /** Was für ein Tipp? */
    private TippStatusType status;
       
    /** Erzeugt ein UserResultOfDay. */
    protected UserResultOfDay() {
    }
    
    /**
     * Prüft, ob die Eigenschaften dieses Objekts komplett und gültig
     * gefüllt sind, damit es evt. Weiterverarbeitungen erfahren kann.
     * Folgende Eigenschaften müssen gesetzt sein:
     * <ul>
     *  <li>user</li>
     * </ul>
     * @return true, Objekt in Ordnung; false, es ist was falsch.
     */
    public boolean isValid() {
        if (user == null) {
            return false;
        } else {
            return true;
        }
    }
            
    public boolean getIsTipped() {
        return isTipped;
    }
    
    public int getWin() {
        return win;
    }
    
    public int getToto() {
        return toto;
    }
    
    public int getTipps() {
        return tipps;
    }
    
    public int getLost() {
        return tipps - (win + toto);
    }
    
    public User getUser() {
        return user;
    }
    
    public long getPoints() {
        return win * UserResult.nEqualValue + 
               toto * UserResult.nTotoValue;
    }
    
    public double getBeautyPoints() {
        return ((double)getPoints()) / ((double)UserResult.nDivisor);
    }
    
    protected void setWin(int value) {
        isTipped = true;
        win = value;
    }
    
    protected void setToto(int value) {
        isTipped = true;
        toto = value;
    }
    
    protected void setTipps(int value) {
        isTipped = true;
        tipps = value;
    }
    
    protected void setUser(User value) {
        Validate.notNull(value);
        user = value;
    }

    /**
     * Setzt den <code>GameTppStatus</code> für diesen Spieltag-Tipp.
     *
     * @param value Ein <code>GameTippStatus</code>.
     */
    public final void setStatus(final TippStatusType value) {
        Validate.notNull(value);
        status = value;
    }

    /**
     * Liefert den <code>GameTippStatus</code> für diesen Spieltags-Tipp.
     *
     * @return Ein <code>GameTippStatus</code>.
     */
    public final TippStatusType getStatus() {
        return status;
    }

    /**
     * @see Object#toString()
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("UROD: ");
        buf.append(user);
        buf.append(" Pts: ");
        buf.append(getPoints());
        buf.append(" Equal: ");
        buf.append(getWin());
        buf.append(" Toto: ");
        buf.append(getToto());
        buf.append(" Status: ");
        buf.append(getStatus().toString());
        return buf.toString();
    }
    
}
