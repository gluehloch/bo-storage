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

package de.betoffice.storage.comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.betoffice.storage.Nickname;
import de.betoffice.storage.User;
import de.betoffice.storage.UserResult;
import de.betoffice.storage.comparator.UserPointsComparator;

/**
 * Testet den {@link Comparator} {@link UserPointsComparator}.
 *
 * @author by Andre Winkler
 */
class UserPointsComparatorTest {

    @Test
    void testUserPointsComparator() {
        User frosch = new User();
        frosch.setNickname(Nickname.of("Frosch"));
        User hattwig = new User();
        hattwig.setNickname(Nickname.of("Hattwig"));
        User mrTipp = new User();
        mrTipp.setNickname(Nickname.of("mrTipp"));
        User chris = new User();
        chris.setNickname(Nickname.of("chris"));

        UserResult urFrosch = new UserResult(frosch);
        UserResult urHattwig = new UserResult(hattwig);
        UserResult urMrTipp = new UserResult(mrTipp);
        UserResult urChris = new UserResult(chris);

        urFrosch.setUserWin(10);
        urFrosch.setUserTotoWin(20);
        urFrosch.setTicket(5);

        urHattwig.setUserWin(9);
        urHattwig.setUserTotoWin(19);
        urHattwig.setTicket(7);

        urMrTipp.setUserWin(9);
        urMrTipp.setUserTotoWin(18);
        urMrTipp.setTicket(8);

        urChris.setUserWin(8);
        urChris.setUserTotoWin(18);
        urChris.setTicket(7);

        List<UserResult> ur = Arrays.asList(urMrTipp, urChris, urFrosch, urHattwig);
        Collections.sort(ur, new UserPointsComparator());

        assertEquals("Frosch", ur.get(0).getUser().getNickname().value());
        assertEquals("Hattwig", ur.get(1).getUser().getNickname().value());
        assertEquals("mrTipp", ur.get(2).getUser().getNickname().value());
        assertEquals("chris", ur.get(3).getUser().getNickname().value());
    }

}
