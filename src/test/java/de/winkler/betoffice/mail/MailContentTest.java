/*
 * $Id: MailContentTest.java 3782 2013-07-27 08:44:32Z andrewinkler $
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

package de.winkler.betoffice.mail;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Testet die Klasse {@link MailContent}.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3782 $ $LastChangedDate: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
public class MailContentTest {

    private static final String MESSAGE_TEXT_1 = "Irgendwas davor .asdf.<FORM>Andre Winkler was here.</FORM> und irgendwas danach .asdfasdf";
    private static final String MESSAGE_TEXT_2 = "Irgendwas davor .asdf.<FORM>Andre Winkler was here.</FORM> und irgendwas </FORM>danach .asdfasdf";

    private String text;

    @Test
    public void testMailContentTrimMessage() {
        text = MailContent.trimMsgTxt(MESSAGE_TEXT_1);
        assertEquals("<FORM>Andre Winkler was here.</FORM>", text);

        text = MailContent.trimMsgTxt(MESSAGE_TEXT_2);
        assertEquals("<FORM>Andre Winkler was here.</FORM>", text);
    }

}
