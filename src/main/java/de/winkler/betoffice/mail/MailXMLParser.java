/*
 * $Id: MailXMLParser.java 3782 2013-07-27 08:44:32Z andrewinkler $
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

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;

import de.awtools.basic.LoggerFactory;

/** 
 * Parst eine EMail bzw. deren XML Inhalt.
 *
 * @author  $Author: andrewinkler $
 * @version $Revision: 3782 $ $Date: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
public class MailXMLParser {

    /** Mail Subject(Betreff Zeile): 'Registrierung/Anmeldung'. */
    public static final String REGISTRATION = "Anmeldung";

    /** Mail Subject (Betreff Zeile): 'Tipp'. */
    public static final String TIPP = "Tipp";

    /** Der private Logger der Klasse. */
    private final Logger log = LoggerFactory.make();

    /**
     * Nimmt einen Mailtext entgegen und parst deren XML-Inhalt. Die restlichen
     * Attribute aus <code>MailContent</code> werden aufgefüllt.
     *
     * @param mailContent Die Rohdaten der Mail.
     * @return mail content details
     * @throws MailException Der Text konnte nicht geparst werden!
     */
    public MailContentDetails parse(final MailContent mailContent)
        throws MailException {

        /* @TODO Mit oder ohne Encoding?!?
           StringBuffer buf = new StringBuffer();
           buf.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>");
           buf.append(mailText);
         */
        try {
            MailContentDetails detail = new MailContentDetails();
            String message = MailContent.trimMsgTxt(mailContent.sMessageText);
            Element element = DocumentHelper.parseText(message)
                .getRootElement();

            startParsing(detail, element);
            return detail;
        } catch (DocumentException ex) {
            log.info("Parsing exception!", ex);
            throw new MailException(ex);
        }
    }

    /**
     * Startet den eigentlichen Parse-Vorgang und befüllt
     * {@link MailContentDetails}.
     *
     * @param content Der Mail-Inhalt.
     * @param element Das dom4j Element.
     */
    private void startParsing(final MailContentDetails content,
            final Element element) {

        content.setDate(element.elementTextTrim(MailContent.DATE));
        content.setBrowser(element.elementTextTrim(MailContent.BROWSER));
        content.setMail(element.elementTextTrim(MailContent.EMAIL));
        content.setFormInfo(element.elementTextTrim(MailContent.FORMINFO));
        content.setChampionship(element.elementTextTrim(MailContent.CHAMPIONSHIP));
        content.setRound(element.elementTextTrim(MailContent.ROUND));
        content.setGuestGoals(element.elementTextTrim(MailContent.GUESTGOALS));
        content.setHomeGoals(element.elementTextTrim(MailContent.HOMEGOALS));
        content.setNickName(element.elementTextTrim(MailContent.NICKNAME));
        content.setPhone(element.elementTextTrim(MailContent.PHONE));
        content.setPwdA(element.elementTextTrim(MailContent.PWDA));
        content.setPwdB(element.elementTextTrim(MailContent.PWDB));
        content.setTime(element.elementTextTrim(MailContent.TIME));
        content.setUsing(element.elementTextTrim(MailContent.USE));
        content.setUser(element.elementTextTrim(MailContent.USER));
        content.setUserCall(element.elementTextTrim(MailContent.USERCALL));
        content.setUserIP(element.elementTextTrim(MailContent.USERIP));
    }

}
