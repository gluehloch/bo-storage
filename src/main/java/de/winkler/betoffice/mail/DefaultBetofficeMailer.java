/*
 * $Id: DefaultBetofficeMailer.java 3782 2013-07-27 08:44:32Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2013 by Andre Winkler. All rights reserved.
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

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.joda.time.DateTime;
import org.slf4j.Logger;

import de.awtools.basic.LoggerFactory;

/**
 * Sends an email.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3782 $
 *          $LastChangedDate: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $ 
 */
public class DefaultBetofficeMailer implements BetofficeMailer {

    private final Logger log = LoggerFactory.make();

    private static final String HOST = "localhost";

    private static final String FROM = "betoffice@tippdiekistebier.de";

    private final Properties properties;

    public DefaultBetofficeMailer() {
        properties = System.getProperties();
        properties.setProperty("mail.smtp.host", HOST);
    }

    /**
     * Sends an email.
     *
     * @param recipient the receiver of the mail like <code>name@host.de</code>
     * @param tippMail mail subject and content 
     */
    public void sendMail(String recipient, TippMail tippMail)
            throws MessagingException {

        Session session = Session.getDefaultInstance(properties);

        MimeMessage message = new MimeMessage(session);
        message.setHeader("MIME-Version", "1.0");
        message.setHeader("Content-type", "text/plain; charset=utf-8");
        message.setHeader("From", "betoffice@tippdiekistebier.de");
        message.setFrom(new InternetAddress(FROM));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(
                recipient));
        message.setSubject(tippMail.getSubject());
        message.setText(tippMail.getContent(), "utf-8");
        message.setSentDate(DateTime.now().toDate());

        log.info(
                "Sending an email to ->{}<- with subject ->{}<- and content ->{}<-",
                new Object[] { recipient, tippMail.getSubject(),
                        tippMail.getContent() });

        Transport.send(message);
    }

}
