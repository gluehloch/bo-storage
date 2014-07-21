/*
 * $Id: MailDownloadManager.java 3782 2013-07-27 08:44:32Z andrewinkler $
 * ============================================================================
 * Project betoffice-swing
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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.Validate;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import de.awtools.basic.LoggerFactory;
import de.awtools.mail.MailUtils;
import de.awtools.mail.POP3Receiver;

/**
 * Die Klasse MailManager organisiert das Runterladen der Tipp-Mails und 
 * startet ggf. auch deren Auswertung.
 *
 * @author  $Author: andrewinkler $
 * @version $Revision: 3782 $ $Date: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
public class MailDownloadManager {

    /** Der private Logger der Klasse. */
    private final Logger log = LoggerFactory.make();

    /** Der Name der Eigenschaft 'mailParsing'. */
    public static final String PROPERTY_MAIL_PARSING = "mailParsing";

    /** Der Name der Eigenschaft 'mailCount'. */
    public static final String PROPERTY_MAIL_COUNT = "mailCount";

    /** Der Name der Eigenschaft 'mailFailure'. */
    public static final String PROPERTY_MAIL_FAILURE = "mailFailure";

    /** Empfängt die Mails. */
    private final POP3Receiver receiver;

    private PropertyChangeSupport propertyChangeSupport;

    /**
     * Konstruktor.
     *
     * @param _receiver Utility Klasse für das Empfangen von Mails.
     */
    public MailDownloadManager(final POP3Receiver _receiver) {
        Validate.notNull(_receiver);
        receiver = _receiver;
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    /**
     * Alle Mails lesen und auswerten.
     *
     * @return Eine Liste mit Objekten vom Typ <code>MainContent</code>.
     */
    public List<MailContent> readMails() {
        List<MailContent> mails = new ArrayList<MailContent>();
        receiver.download();
        Message[] messages = receiver.messagesToArray();
        propertyChangeSupport.firePropertyChange(PROPERTY_MAIL_COUNT, 0,
                messages.length);

        for (int i = 0; i < messages.length; i++) {
            try {
                Message message = messages[i];
                
                InternetAddress[] inetAdress = (InternetAddress[]) message.getFrom();

                String from = null;
                if (inetAdress.length > 0) {
                    from = inetAdress[0].getAddress();
                } else {
                    from = "unknwon";
                }

                String subject = message.getSubject();
                String msgTxt = extractMailBody(message, i);

                // hole Datum
                String date;
                if (message.getSentDate() == null) {
                    date = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
                } else {
                    DateFormat dateFormatter = DateFormat.getDateInstance(
                            DateFormat.DEFAULT, new Locale("de", "DE"));
                    date = dateFormatter.format(message.getSentDate());
                }

                // Protokoll schreiben
                if (log.isDebugEnabled()) {
                    StringBuilder debug = new StringBuilder();
                    debug.append("+++++Neue Mail: \n\t");
                    debug.append("Address: ");
                    debug.append(from);
                    debug.append("\n\t");
                    debug.append("Text: ");
                    debug.append(msgTxt);
                    debug.append("\n\t");
                    debug.append("Date: ");
                    debug.append(date);
                    debug.append("\n\t");

                    log.debug(debug.toString());
                }

                MailContent mailContent = new MailContent(from.trim(),
                        subject.trim(), MailContent.trimMsgTxt(msgTxt).trim(),
                        date.trim());

                mails.add(mailContent);

                propertyChangeSupport.firePropertyChange(PROPERTY_MAIL_PARSING,
                        (i - 1), i);
            } catch (IOException ex) {
                log.warn("Mail parsing failed. Check your POP3 account!", ex);
                propertyChangeSupport.firePropertyChange(PROPERTY_MAIL_FAILURE,
                        -1, i);
            } catch (MessagingException ex) {
                log.warn("Mail parsing failed. Check your POP3 account!", ex);
                propertyChangeSupport.firePropertyChange(PROPERTY_MAIL_FAILURE,
                        -1, i);
            }
        }

        return mails;
    }

    private String extractMailBody(final Message message, final int i)
            throws MessagingException, IOException {

        String msgTxt = null;
        try {
            msgTxt = MailUtils.getBody(message);
        } catch (IOException ex) {
            log.warn("Mail body extraction failed with an exception. "
                    + " The mail will be ignored.", ex);
            msgTxt = "Unknown body type. Mail will be ignored.";
        }
        return msgTxt;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

}
