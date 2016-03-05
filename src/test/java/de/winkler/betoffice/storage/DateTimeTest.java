/*
 * $Id: DateTimeTest.java 3782 2013-07-27 08:44:32Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2012 by Andre Winkler. All rights reserved.
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

package de.winkler.betoffice.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Test;

/**
 * Tests for class {@link DateTime}.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3782 $ $LastChangedDate: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
public class DateTimeTest {

    @Test
    public void testDateTime() {
        DateTime dateTime = new DateTime("1971-03-24T21:15:16");
        Date date = dateTime.toDate();
        
        DateTime dateTimeAfter = new DateTime("1971-03-24T21:16:17");
        Date dateAfter = dateTimeAfter.toDate();
        
        assertTrue(dateTimeAfter.isAfter(dateTime));
        assertTrue(dateAfter.after(date));
        
        DateTime dateTimeWithConstructor = new DateTime(1971, 3, 24, 21, 15, 16);
        assertEquals(dateTime, dateTimeWithConstructor);
    }

}
