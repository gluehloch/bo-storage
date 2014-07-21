/*
 * $Id: RegistrationParser.java 3782 2013-07-27 08:44:32Z andrewinkler $
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.winkler.betoffice.storage.User;

/**
 * Registriert einen neuen Teilnehmer. Die nötigen Daten bekommt er aus
 * <code>MailContent</code>.
 * 
 * @author  $Author: andrewinkler $
 * @version $Revision: 3782 $ $Date: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
class RegistrationParser {

    /** Der private Logger der Klasse. */
    private final Log log = LogFactory.getLog(RegistrationParser.class);

    /**
     * Parst eine EMail vom Typ 'Registrierung' und erstellt einen neuen
     * Teilnehmer.
     *
     * @param mail Die zu parsende Mail.
     * @return Der ermittelte, neue Teilnehmer.
     */
    public User start(final MailContentDetails mail) {
        log.info("Neue Teilnehmer-Anmeldung!");
        User user = new User();
        user.setName(mail.getUser());
        user.setSurname(mail.getUserCall());
        user.setNickName(mail.getNickName());
        user.setEmail(mail.getMail());
        user.setPhone(mail.getPhone());
        user.setPassword(mail.getPwdA());
        user.setTitle("");
        user.setAutomat(false);
        user.setExcluded(false);
        return user;
    }

}
