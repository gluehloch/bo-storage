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

package de.winkler.betoffice.service;

import java.util.List;

import de.winkler.betoffice.storage.CommunityReference;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.SeasonRange;
import de.winkler.betoffice.storage.UserResult;

/**
 * Berchnet den Spielstand fuer eine Community.
 *
 * @author Andre Winkler
 */
public interface CommunityCalculatorService {

    /**
     * Berechnet das Tipper-Ranking für eine Meisterschaft.
     *
     * @param  community Das User-Ranking dieser Community berechnen.
     * @return           Das Ranking der Tipper.
     */
    List<UserResult> calculateUserRanking(CommunityReference community);

    /**
     * Berechnet das Tipper-Ranking für einen Spieltag.
     *
     * @param  community Das User-Ranking dieser Community berechnen.
     * @param  round     Der betreffende Spieltag.
     * @return           Das Ranking der Tipper.
     */
    List<UserResult> calculateUserRanking(CommunityReference community, GameList round);

    /**
     * Berechnet das Tipper-Ranking für einen Spieltag.
     *
     * @param  community   Das User-Ranking dieser Community berechnen.
     * @param  seasonRange Index des Spieltags ab dem gezählt wird (0..N-1) / Index des Spieltags bis zu dem gezählt
     *                         wird (0..N-1).
     * @return             Das Ranking der Tipper.
     */
    List<UserResult> calculateUserRanking(CommunityReference community, SeasonRange seasonRange);

}
