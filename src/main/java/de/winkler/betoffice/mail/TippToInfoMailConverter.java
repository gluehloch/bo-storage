/*
 * $Id: TippToInfoMailConverter.java 3810 2013-09-02 17:40:15Z andrewinkler $
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

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

/**
 * Creates the info content for a tipp.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3810 $
 *          $LastChangedDate: 2013-09-02 19:40:15 +0200 (Mon, 02 Sep 2013) $
 */
public class TippToInfoMailConverter {

    /**
     * Creates the info mail content for a tipp.
     *
     * @param param the parameters 
     * @return tipp info mail
     */
    public TippMail createTippMail(TippMailParameter param) {

        int anzahl = param.getHomeGoals().length;

        StringBuilder body = new StringBuilder();
        body.append("Guten Tag!\n");
        body.append("Hier ist der Tipp von '").append(param.getNickname())
                .append("' für den ");
        body.append(param.getRoundNr()).append(". Spieltag.\n");
        String timeStamp = DateTime.now().toString("dd.MM.yyyy HH:mm",
                Locale.GERMANY);
        body.append("Der Tipp wurde am ").append(timeStamp);
        body.append(" versendet!\n");
        body.append("\n");
        if (anzahl == 0) {
            body.append("\n");
        } else {
            for (int index = 0; index < anzahl; index++) {
                body.append(StringUtils.rightPad(param.getHomeTeams()[index], 20));
                body.append(" - ");
                body.append(StringUtils.rightPad(param.getGuestTeams()[index], 20));
                body.append(" Tipp: ");
                body.append(StringUtils.rightPad(
                        Integer.toString(param.getHomeGoals()[index]), 2));
                body.append(":");
                body.append(StringUtils.rightPad(
                        Integer.toString(param.getGuestGoals()[index]), 2));
                body.append("\n");
            }
        }
        body.append("\n");
        body.append("Diese EMail wurde von 'Winklers virtuellen Sportwetten'");
        body.append("\n");
        body.append("automatisch generiert. Bitte nicht auf diese EMail antworten!");
        body.append("\n");
        body.append("Für weitere Infos siehe unter http://www.tippdiekistebier.de");
        body.append("\n");

        return new TippMail(createSubject(param), body.toString());
    }

    private String createSubject(TippMailParameter param) {
        String subject = "Tipp-Abgabe Runde #" + param.getRoundNr() + " vom "
                + DateTime.now().toString("dd.MM.yyyy HH:mm", Locale.GERMANY);
        return subject;
    }

}
