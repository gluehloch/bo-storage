/*
 * $Id: UserResultOfDayComparator.java 3782 2013-07-27 08:44:32Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2009 by Andre Winkler. All rights reserved.
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

package de.betoffice.storage.comparator;

import java.util.Comparator;

import de.betoffice.storage.tip.UserResultOfDay;

/**
 * Vergleicht zwei UserResultOfDay Objekte miteinander.
 * 
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3782 $ $LastChangedDate: 2013-07-27 10:44:32
 *          +0200 (Sat, 27 Jul 2013) $
 */
public class UserResultOfDayComparator implements Comparator<UserResultOfDay> {

    /**
     * Vergleicht zwei UserResults miteinander.
     *
     * @param u1
     *            UserResultOfDay Nr 1.
     * @param u2
     *            UserResultOfDay Nr 2.
     * @return -1, u1 &lt; u2; 0, u1 == u2; +1, u1 &gt; u2.
     */
    public int compare(UserResultOfDay u1, UserResultOfDay u2) {
        if (u1.getUser().isExcluded()) {
            return 1;
        }
        if (u2.getUser().isExcluded()) {
            return -1;
        }

        if (u1.getPoints() > u2.getPoints()) {
            return -1;
        } else {
            return 1;
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else {
            return false;
        }
    }

}
