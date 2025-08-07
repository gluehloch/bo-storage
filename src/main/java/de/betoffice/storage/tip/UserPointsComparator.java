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

package de.betoffice.storage.tip;

import java.util.Comparator;

import org.slf4j.Logger;

import de.betoffice.storage.user.UserResult;
import de.betoffice.util.LoggerFactory;

/**
 * Die Klasse legt eine Ordnung über alle Teilnehmer.
 *
 * @author by Andre Winkler
 */
public final class UserPointsComparator implements Comparator<UserResult> {

    private final Logger log = LoggerFactory.make();

    /**
     * Vergleicht zwei UserResults miteinander.
     *
     * @param  u1 User Nr 1.
     * @param  u2 User Nr 2.
     * @return    -1, o1 &lt; o2; 0, o1 == o2; +1, o1 &gt; o2.
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
                retcode = u1.getUser().getNickname().toLowerCase().compareTo(
                        u2.getUser().getNickname().toLowerCase());
            } else {
                throw new IllegalStateException();
            }
        }

        if (log.isDebugEnabled()) {
            if (retcode < 0) {
                log.debug("{} > {}", u1.getUser().getNickname(), u2.getUser().getNickname());
            } else if (retcode > 0) {
                log.debug("{} > {}", u2.getUser().getNickname(), u1.getUser().getNickname());
            } else {
                log.debug("{} == {}", u1.getUser().getNickname(), u2.getUser().getNickname());
            }
        }

        return retcode;
    }

    public boolean equals(Object obj) {
        return obj == this;
    }

}
