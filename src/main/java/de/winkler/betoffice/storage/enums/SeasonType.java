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

package de.winkler.betoffice.storage.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Beschreibt den Modus der Saison.
 * 
 * @author Andre Winkler
 */
public enum SeasonType {

    /** Championship / Bundesliga */
    LEAGUE("LEAGUE"),

    /** Enumeration POKAL */
    CUP("CUP"),

    /** Enumeration Uefa Cup */
    UEFACUP("UEFACUP"),

    /** Enumeration CL Champions League */
    CL("CL"),

    /** Enumeration WC Worldchampionship */
    WC("WC"),

    /** Enumeration EC Eurochampionchip */
    EC("EC");

    private String name;

    private SeasonType(final String _name) {
        name = _name;
    }

    public String toString() {
        return name;
    }

    /**
     * Returns a list of these enums.
     *
     * @return A list with all enums.
     */
    public static List<SeasonType> toList() {
        SeasonType[] seasonTypes = SeasonType.values();
        List<SeasonType> listOfSeasonTypes = new ArrayList<SeasonType>();
        for (SeasonType seasonType : seasonTypes) {
            listOfSeasonTypes.add(seasonType);
        }
        return listOfSeasonTypes;
    }

}
