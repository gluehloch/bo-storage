/*
 * $Id: MailContent.java 3782 2013-07-27 08:44:32Z andrewinkler $
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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;

import de.awtools.basic.LoggerFactory;

/** 
 * Verwaltet eine Betoffice-EMail, d.h. die Daten einer geparste EMail
 * werden hier abgelegt.
 *
 * TODO @todo Dies ist vermutlich ein klassisches GUI Modell.
 * 
 * @author  $Author: andrewinkler $
 * @version $Revision: 3782 $ $Date: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
public class MailContent {

    public static final String MAILS = "MAILS";
    public static final String MAIL = "MAIL";
    public static final String FROM = "FROM";
    public static final String SUBJECT = "SUBJECT";
    public static final String TEXT = "MESSAGETEXT";
    public static final String DATE = "DATUM";
    public static final String EVAL = "EVALUATED";
    public static final String USE = "USE";
    public static final String FORMINFO = "FORMINFO";
    public static final String USERIP = "USERIP";
    public static final String USER = "USER";
    public static final String USERCALL = "USERCALL";
    public static final String NICKNAME = "NICKNAME";
    public static final String PWDA = "PWDA";
    public static final String CHAMPIONSHIP = "CHAMPIONSHIP";
    public static final String ROUND = "ROUND";
    public static final String HOMEGOALS = "HOMEGOALS";
    public static final String GUESTGOALS = "GUESTGOALS";
    public static final String PWDB = "PWDB";
    public static final String EMAIL = "EMAIL";
    public static final String PHONE = "PHONE";
    public static final String BROWSER = "BROWSER";
    public static final String TIME = "TIME";

    public static final String REQUEST = "Anmeldung Winkler Sportwetten";

    public static final String FORM = "FORM";
    public static final String MESSAGETEXT = "MESSAGETEXT";
    public static final String DELIM = ",";

    private static final Logger LOG = LoggerFactory.make();

    //
    // EMail-Header Daten
    //
    public final String sFrom;
    public final String sSubject;
    public final String sMessageText; // Unausgewertete Nachricht.
    public final String sDate;

    /**
     * Erzeugt neue MailMessage.
     *
     * @param _sFrom Absender
     * @param _sSubject Betreff
     * @param _sMsgTxt Message-Text
     * @param _sDate Absendedatum
     */
    public MailContent(
            final String _sFrom, final String _sSubject, final String _sMsgTxt,
            final String _sDate) {

        sFrom = _sFrom;
        sSubject = _sSubject;
        sMessageText = _sMsgTxt;
        sDate = _sDate;
        evaluated = false;
    }

    // ------------------------------------------------------------------------

    private boolean checkMe = false;

    public void setCheckMe(final boolean _checkMe) {
        checkMe = _checkMe;
    }

    /**
     * Liefert <code>true</code>, wenn diese Mail ausgewertet werden soll.
     *
     * @return Liefert <code>true</code>, wenn diese Mail ausgewertet werden
     *     soll.
     */
    public boolean checkMe() {
        return checkMe;
    }

    // ------------------------------------------------------------------------

    private boolean evaluated;

    /**
     * Wurde diese Mail bereits ausgewertet?
     *
     * @return Liefert <code>true</code>, wenn diese Mail bereits ausgewertet.
     */
    public boolean isEvaluated() {
        return evaluated;
    }

    public void setEvaluated() {
        evaluated = true;
    }

    public String toString() {
    	return new ToStringBuilder(this).
    	    append("checkMe", checkMe).
    	    append("evaluated", evaluated).
        	append("from", sFrom).
        	append("subject", sSubject).
            append("date", sDate).
            append("messageText", sMessageText).toString();
    }

    // ------------------------------------------------------------------------

    /**
     * Extrahiert aus der Nachricht den XML Text &lt;FORM&gt;...&lt;/FORM&gt;.
     *
     * @todo Entitäten erlauben?
     *
     * @param text Der zu beschneidende Text.
     * @return Der beschnittene Text.
     */
    public static String trimMsgTxt(final String text) {
        try {
            String copy = text;
            int start = copy.indexOf("<FORM>");
            int end = copy.indexOf("</FORM>");
      
            // check index
            if (start == -1 || end == -1) return text;
            // index Ok
            int _nEnd2 = end + "</FORM>".length();
            String s1 = copy.substring(start, _nEnd2);
            
            // Nun noch die lästigen '&' entfernen. '&' leitet
            // eine Entität ein und ist hier nicht gewünscht.
            s1 = s1.replace('&', '?');            
            return s1;
        } catch (Exception ex) {
        	LOG.info("Catched an exception:", ex);
            return text;
        }
    }
	
}
