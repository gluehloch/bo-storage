/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2014 by Andre Winkler. All
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

package de.winkler.betoffice.mail;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.winkler.betoffice.storage.User;

/**
 * Test für die Klasse {@link MailContentDetails}.
 *
 * @author by Andre Winkler
 */
public class MailContentDetailsTest {

    @Test
    public void testMailContentMapUser() {
        MailContentDetails mailContent = new MailContentDetails();
        mailContent.setUser("Winkler");
        mailContent.setUserCall("Andre");
        mailContent.setNickName("Frosch");
        mailContent.setMail("andre@mail.de");
        mailContent.setPhone("65 65 65");
        mailContent.setPwdA("irgendwas");

        RegistrationParser regiParser = new RegistrationParser();
        User user = regiParser.start(mailContent);

        assertEquals("Winkler", user.getName());
        assertEquals("Andre", user.getSurname());
        assertEquals("Frosch", user.getNickName());
        assertEquals("irgendwas", user.getPassword());
        assertEquals("", user.getTitle());
        assertEquals(false, user.isAutomat());
        assertEquals(false, user.isExcluded());
    }

}
