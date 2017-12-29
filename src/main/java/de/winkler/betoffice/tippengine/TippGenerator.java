/*
 * $Id: TippGenerator.java 3782 2013-07-27 08:44:32Z andrewinkler $
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

package de.winkler.betoffice.tippengine;

import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.Season;

/**
 * Interface for a tipp generator.
 * 
 * @author $Author: andrewinkler $
 * @version $Revision: 3782 $ $Date: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul
 *          2013) $
 */
public interface TippGenerator {

    /**
     * Erzeugt für die gesamte Saison Min-Tipps für alle Tipper, die ihren Tipp
     * nicht abgegeben haben.
     *
     * @param season
     *            Die auszuwertende Meisterschaft.
     */
    public void generateTipp(final Season season);

    /**
     * Erzeugt für alle User, die ihren Tipp nicht abgegeben haben, einen
     * Min-Tipp. Zudem darf der User nicht gesperrt sein.
     *
     * @param round
     *            Der Spieltag für die Tipp Generierung.
     */
    public void generateTipp(final GameList round);

}
