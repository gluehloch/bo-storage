/*
 * $Id: TippToXmlConverter.java 3810 2013-09-02 17:40:15Z andrewinkler $
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

import java.util.Locale;

import org.joda.time.DateTime;

/**
 * Evaluate a tipp form.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3810 $
 *          $LastChangedDate: 2013-09-02 19:40:15 +0200 (Mon, 02 Sep 2013) $
 */
public class TippToXmlConverter {

    /**
     * Creates the xml document for the betoffice fat client email evaluator.
     *
     * @param param the parameters 
     * @return xml document for betoffice fat client email evaluator
     */
    public TippMail createTippMail(TippMailParameter param) {
        int anzahl = param.getHomeGoals().length;
        StringBuilder body = new StringBuilder();
        body.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        body.append("<FORM>\n");
        body.append("<USE>Tipp-Abgabe Winkler Sportwetten</USE>\n");
        body.append("  <FORMINFO>").append(param.getHttpHost())
                .append("</FORMINFO>\n");
        body.append("  <USERIP>").append(param.getUserIp())
                .append("</USERIP>\n");
        body.append("  <BROWSER>").append(param.getHttpUserAgent())
                .append("</BROWSER>\n");
        String timeStamp = DateTime.now().toString("dd.MM.yyyy HH:mm",
                Locale.GERMANY);
        body.append("  <DATUM>").append(timeStamp).append("</DATUM>\n");
        body.append("  <TIME>").append(timeStamp).append("</TIME>\n");
        body.append("  <NICKNAME>").append(param.getNickname())
                .append("</NICKNAME>\n");
        body.append("  <CHAMPIONSHIP>").append(param.getChampionshipId())
                .append("</CHAMPIONSHIP>\n");
        body.append("  <ROUND>").append(param.getRoundNr())
                .append("</ROUND>\n");
        body.append("  <PWDA>").append(param.getPassword()).append("</PWDA>\n");

        body.append("  <HOMEGOALS>");
        if (anzahl == 0) {
            body.append("");
        } else {
            for (int index = 0; index < anzahl; index++) {
                body.append(param.getHomeGoals()[index]);
                if (index < (anzahl - 1)) {
                    body.append(",");
                }
            }
        }
        body.append("</HOMEGOALS>\n");

        body.append("  <GUESTGOALS>");
        if (anzahl == 0) {
            body.append("");
        } else {
            for (int index = 0; index < anzahl; index++) {
                body.append(param.getGuestGoals()[index]);
                if (index < (anzahl - 1)) {
                    body.append(",");
                }
            }
        }
        body.append("</GUESTGOALS>\n");
        body.append("</FORM>\n");

        return new TippMail(createSubject(param), body.toString());
    }

    private String createSubject(TippMailParameter param) {
        String subject = "Tipp-Abgabe Runde #" + param.getRoundNr() + " vom "
                + DateTime.now().toString("dd.MM.yyyy HH:mm", Locale.GERMANY);
        return subject;
    }

}
