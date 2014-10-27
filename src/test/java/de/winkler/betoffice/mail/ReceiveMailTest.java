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
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

/**
 * Testet das Empfangen und Parsen von Mails.
 *
 * @author Andre Winkler
 */
public class ReceiveMailTest {

    private void testTechnicalMailPart(final MailContentDetails mailContent) {
        assertEquals("##ENV_HTTP_USER_AGENT##", mailContent.getBrowser());
        assertEquals("##DATUM##", mailContent.getDate());
        assertEquals("##ENV_HTTP_REFERER##", mailContent.getFormInfo());
        assertEquals("##UHRZEIT##", mailContent.getTime());
        assertEquals("##ENV_REMOTE_HOST##", mailContent.getUserIP());
    }

    @Test
    public void testTippMail() throws Exception {
        InputStream in = getClass().getResourceAsStream("tipp-message.txt");

        assertNotNull(in);
        String mailMessageText = IOUtils.toString(in);

        MailContent mailContent = new MailContent("andre.winkler@web.de",
                MailXMLParser.TIPP, mailMessageText, "24.03.1971");
        MailXMLParser parser = new MailXMLParser();
        MailContentDetails details = parser.parse(mailContent);

        assertEquals("##championship##", details.getChampionship());
        assertEquals("##round##", details.getRound());
        assertEquals("##guest_goals##", details.getGuestGoals());
        assertEquals("##home_goals##", details.getHomeGoals());
        assertEquals("##nickname##", details.getNickName());
        assertEquals("##pwd##", details.getPwdA());
        assertEquals("Tipp-Abgabe Winkler Sportwetten", details.getUsing());
    }

    @Test
    public void testRegistrationMail() throws Exception {
        InputStream in = getClass().getResourceAsStream("regi-message.txt");

        assertNotNull(in);
        String mailMessageText = IOUtils.toString(in);

        MailContent mailContent = new MailContent("andre.winkler@web.de",
                MailXMLParser.REGISTRATION, mailMessageText, "24.03.1971");
        MailXMLParser parser = new MailXMLParser();
        MailContentDetails details = parser.parse(mailContent);

        testTechnicalMailPart(details);

        assertEquals("##user_name##", details.getUser());
        assertEquals("##passwordA##", details.getPwdA());
        assertEquals("##passwordB##", details.getPwdB());
        assertEquals("Anmeldung Winkler Sportwetten", details.getUsing());

        assertEquals("##absender##", details.getMail());
        assertEquals("##nickname##", details.getNickName());
        assertEquals("##user_call##", details.getUserCall());
        assertEquals("##phone##", details.getPhone());
    }

}
