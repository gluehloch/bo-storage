/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2023 by Andre Winkler. All
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

package de.betoffice.storage.season;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Verwaltet die administrativen Daten einer Meisterschaft. Zum Beispiel die
 * Info, in welches Verzeichnis ein Export geht oder welches Export-Templates zu
 * verwenden sind.
 *
 * @author Andre Winkler
 */
@Embeddable
public class ChampionshipConfiguration {

    /** The openligadb league shortcut ('bl1' for 'Fussball Bundesliga'). */
    @Column(name = "bo_openligaleagueshortcut")
    private String openligaLeagueShortcut;

    /** The openligadb league-season value (Something like '2014'). */
    @Column(name = "bo_openligaleagueSeason")
    private String openligaLeagueSeason;

    // -- openligadb leagueShortcut -------------------------------------------

    public String getOpenligaLeagueShortcut() {
        return openligaLeagueShortcut;
    }

    public void setOpenligaLeagueShortcut(String value) {
        openligaLeagueShortcut = value;
    }

    // -- openligadb leagueSeason ---------------------------------------------

    public String getOpenligaLeagueSeason() {
        return openligaLeagueSeason;
    }

    public void setOpenligaLeagueSeason(String value) {
        openligaLeagueSeason = value;
    }

}
