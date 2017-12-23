/*
 * $Id: DummyUsers.java 3782 2013-07-27 08:44:32Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2008 by Andre Winkler. All rights reserved.
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

package de.winkler.betoffice.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.winkler.betoffice.storage.User;

/**
 * Verwaltet die Properties von betoffice. <br>
 * <strong>Nur zu Testzwecken zu verwenden!</strong>
 *
 * @author $Author: andrewinkler $
 * @version $Revision: 3782 $ $Date: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul
 *          2013) $
 */
public final class DummyUsers {

    /** Die vordefinierten Teilnehmereigenschaften. */
    public static final Object[][] USER_PROPS = {
            { "Frosch", "Andre", "Winkler", "awi", "040-76751624",
                    Boolean.FALSE },
            { "MrTipp", "Markus", "Rohloff", "tipper", "040-76751624",
                    Boolean.FALSE },
            { "Peter", "Peter", "Groth", "peter", "0201-30 86 10",
                    Boolean.FALSE },
            { "Hattwig", "Lars", "Hattwig", "eisbär", "030-66 55 44",
                    Boolean.FALSE },
            { "Automat", "Automat", "Automat", "Automat", "Automat",
                    Boolean.TRUE }
    };

    /** Frosch steht an Index 0 in der Teilnehmerliste. */
    public static final int FROSCH = 0;

    /** MrTipp steht an Index 0 in der Teilnehmerliste. */
    public static final int MRTIPP = 1;

    /** Peter steht an Index 0 in der Teilnehmerliste. */
    public static final int PETER = 2;

    /** Hattwig steht an Index 0 in der Teilnehmerliste. */
    public static final int HATTWIG = 3;

    /** Automat steht an Index 0 in der Teilnehmerliste. */
    public static final int AUTOMAT = 4;

    /** Die Teilnehmer. */
    private final List<User> users = new ArrayList<User>();

    public DummyUsers() {
        createUsers();
    }

    /**
     * Liefert die generierten Teilnehmer.
     *
     * @return Die Teilnehmer.
     */
    public User[] users() {
        return users.toArray(new User[users.size()]);
    }

    /**
     * Liefert die generierten Teilnehmer.
     *
     * @return Die Teilnehmer.
     */
    public List<User> toList() {
        return Collections.unmodifiableList(users);
    }

    /**
     * Fügt einen weiteren Testteilnehmer den Teilnehmern hinzu.
     *
     * @param user
     *            Ein neuer Testteilnehmer.
     */
    public void add(final User user) {
        users.add(user);
    }

    /**
     * Entfernt einen Teilnehmer.
     *
     * @param user
     *            Der zu entfernende Teilnehmer.
     */
    public void delete(final User user) {
        users.remove(user);
    }

    /**
     * Erzeugt eine Latte an Teilnehmern.
     */
    private void createUsers() {
        for (int i = 0; i < USER_PROPS.length; i++) {
            User user = new User();
            user.setNickName(USER_PROPS[i][0].toString());
            user.setSurname(USER_PROPS[i][1].toString());
            user.setName(USER_PROPS[i][2].toString());
            user.setPassword(USER_PROPS[i][3].toString());
            user.setPhone(USER_PROPS[i][4].toString());
            user.setAutomat(((Boolean) USER_PROPS[i][5]).booleanValue());
            users.add(user);
        }
    }

}
