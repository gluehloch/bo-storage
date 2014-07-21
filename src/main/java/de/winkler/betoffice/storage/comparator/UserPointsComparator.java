/*
 * $Id: UserPointsComparator.java 3782 2013-07-27 08:44:32Z andrewinkler $
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

package de.winkler.betoffice.storage.comparator;

import java.util.Comparator;

import org.slf4j.Logger;

import de.awtools.basic.LoggerFactory;
import de.winkler.betoffice.storage.UserResult;

/**
 * Die Klasse legt eine Ordnung über alle Teilnehmer.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3782 $ $LastChangedDate: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
public final class UserPointsComparator implements Comparator<UserResult> {

    private final Logger log = LoggerFactory.make();

    /**
     * Vergleicht zwei UserResults miteinander.
     *
     * @param u1 User Nr 1.
     * @param u2 User Nr 2.
     * @return -1, o1 &lt; o2; 0, o1 == o2; +1, o1 &gt; o2.
     */
    public int compare(final UserResult u1, final UserResult u2) {
        int retcode = 0;
        if (u1.getPoints() > u2.getPoints()) {
            retcode = -1;
        } else if (u1.getPoints() < u2.getPoints()) {
            retcode = 1;
        } else if (u1.getPoints() == u2.getPoints()) {
            if (u1.getUserWin() > u2.getUserWin()) {
                retcode = -1;
            } else if (u1.getUserWin() < u2.getUserWin()) {
                retcode = 1;
            } else if (u1.getUserWin() == u2.getUserWin()) {
                retcode = u1.getUser().getNickName().toLowerCase().compareTo(
                    u2.getUser().getNickName().toLowerCase());
            } else {
                throw new IllegalStateException();
            }
        }

        if (log.isDebugEnabled()) {
        	if (retcode < 0) {
	            log.debug(u1.getUser().getNickName() + " > " + u2.getUser().getNickName());
        	} else if (retcode > 0) {
        		log.debug(u2.getUser().getNickName() + " > " + u1.getUser().getNickName());
        	} else {
        		log.debug(u1.getUser().getNickName() + " == " + u2.getUser().getNickName());
        	}
        }

        return retcode;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else {
            return false;
        }
    }

}
