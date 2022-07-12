/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2022 by Andre Winkler. All
 * rights reserved.
 * ============================================================================
 * GNU GENERAL  LICENSE TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND
 * MODIFICATION
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General  License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General  License for more
 * details.
 * 
 * You should have received a copy of the GNU General  License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

package de.winkler.betoffice.dao;

import java.util.List;
import java.util.Optional;

import de.winkler.betoffice.storage.GroupType;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.SeasonReference;
import de.winkler.betoffice.storage.TeamResult;

/**
 * DAO Klasse für den Zugriff auf {@link de.winkler.betoffice.storage.Season} Objekte.
 *
 * @author by Andre Winkler
 */
public interface SeasonDao extends CommonDao<Season> {

    /**
     * Find all seasons ordered by year.
     * 
     * @return All seasons.
     */
    List<Season> findAll();

    /**
     * Liefert eine Meisterschaft mit gesuchten Namen und Jahrgang.
     *
     * @param  seasonRef Referenz zu einer Meisterschaft.
     * @return           Eine Meisterschaft.
     */
    Optional<Season> find(SeasonReference seasonRef);

    /**
     * Startet die Tabellenberechnung der Mannschaften einer Meisterschaft.
     *
     * @param  season    Die Meisterschaft.
     * @param  groupType Die Liga/Gruppe die berechnet werden soll.
     * @return           Eine sortierte Liste der Tabelle.
     */
    List<TeamResult> calculateTeamRanking(Season season, GroupType groupType);

    /**
     * Startet die Tabellenberechnung der Mannschaften einer Meisterschaft über bestimmte Spielrunden.
     *
     * @param  season     Die Meisterschaft.
     * @param  groupType  Die Liga/Gruppe die berechnet werden soll.
     * @param  startIndex Index des Start-Spieltags (0..N-1).
     * @param  endIndex   Index des End-Spieltags (0..N-1).
     * @return            Eine sortierte Liste der Tabelle.
     */
    List<TeamResult> calculateTeamRanking(Season season, GroupType groupType,
            int startIndex, int endIndex);

}
