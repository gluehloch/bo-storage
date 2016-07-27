/*
 * $Id: UserPointsComparatorTest.java 3782 2013-07-27 08:44:32Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2011 by Andre Winkler. All rights reserved.
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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.awtools.basic.AWTools;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.UserResult;

/**
 * Testet den {@link Comparator} {@link UserPointsComparator}.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3782 $ $LastChangedDate: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
public class UserPointsComparatorTest {

    @Test
    public void testUserPointsComparator() {
        User frosch = new User();
        frosch.setNickName("Frosch");
        User hattwig = new User();
        hattwig.setNickName("Hattwig");
        User mrTipp = new User();
        mrTipp.setNickName("mrTipp");
        User chris = new User();
        chris.setNickName("chris");
        
        Season season = new Season();
        season.setName("Bundesliga");
        season.setYear("2009/2010");

        UserResult urFrosch = new UserResult(frosch, season);
        UserResult urHattwig = new UserResult(hattwig, season);
        UserResult urMrTipp = new UserResult(mrTipp, season);
        UserResult urChris = new UserResult(chris, season);

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

        List<UserResult> ur = AWTools.arrayList(urMrTipp, urChris, urFrosch, urHattwig);
        Collections.sort(ur, new UserPointsComparator());

        Assert.assertEquals("Frosch", ur.get(0).getUser().getNickName());
        Assert.assertEquals("Hattwig", ur.get(1).getUser().getNickName());
        Assert.assertEquals("mrTipp", ur.get(2).getUser().getNickName());
        Assert.assertEquals("chris", ur.get(3).getUser().getNickName());
    }

}
