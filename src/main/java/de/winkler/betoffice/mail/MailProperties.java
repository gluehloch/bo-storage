/*
 * $Id: MailProperties.java 3782 2013-07-27 08:44:32Z andrewinkler $
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

/**
 * Die Eigenschaften für das Mail/POP3-Konto.
 * 
 * @author  $Author: andrewinkler $
 * @version $Revision: 3782 $ $Date: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
public class MailProperties {

    // -- host ----------------------------------------------------------------

    /** Der Mail-Host. */
    private String host;

    /**
     * Liefert den Host.
     *
     * @return Der Host.
     */
    public final String getHost() {
        return host;
    }

    /**
     * Setzt den Host.
     *
     * @param _host Der Host.
     */
    public final void setHost(final String _host) {
        host = _host;
    }

    // -- port ----------------------------------------------------------------

    /** Der Mail-Port. */
    private int port;

    /**
     * Liefert den Port.
     *
     * @return Der Port.
     */
    public final int getPort() {
        return port;
    }

    /**
     * Setzt den Port.
     *
     * @param _port Der Port.
     */
    public final void setPort(final int _port) {
        port = _port;
    }

    // -- user ----------------------------------------------------------------

    /** Der Mail-Konto Besitzer. */
    private String user;

    /**
     * Liefert den User.
     *
     * @return Der User.
     */
    public final String getUser() {
        return user;
    }

    /**
     * Setzt den User.
     *
     * @param _user Der User.
     */
    public final void setUser(final String _user) {
        user = _user;
    }

    // -- password ------------------------------------------------------------

    /** Das Mail-Konto Password. */
    private String password;

    /**
     * Liefert das Password.
     *
     * @return Das Password.
     */
    public final String getPassword() {
        return password;
    }

    /**
     * Setzt das Password.
     *
     * @param _password Das Password.
     */
    public final void setPassword(final String _password) {
        password = _password;
    }

}
