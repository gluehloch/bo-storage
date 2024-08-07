/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2024 by Andre Winkler. All
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
 *
 */

package de.winkler.betoffice.storage.enums;

import java.util.Arrays;
import java.util.List;

/**
 * Klassifizierung einer Mannschaft. Z.B. FIFA oder DFB(Bundesliga) Mannschaft.
 *
 * @author Andre Winkler
 */
public enum TeamType {

    DFB("DFB"), FIFA("FIFA");

    private final String name;

    private TeamType(final String _name) {
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
    public static List<TeamType> toList() {
        return Arrays.asList(TeamType.values());
    }

}
