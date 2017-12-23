/*
 * $Id: DummyGroups.java 3782 2013-07-27 08:44:32Z andrewinkler $
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

import de.winkler.betoffice.storage.GroupType;

/**
 * Stellt ein paar Gruppentypen zu Testzwecken zur Verfügung. <br>
 * <strong>Nur zu Testzwecken zu verwenden!</strong>
 *
 * @author $Author: andrewinkler $
 * @version $Revision: 3782 $ $Date: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul
 *          2013) $
 */
public final class DummyGroups {

    /** Eine Sammlung von Mannschaften. */
    public static final String[][] GROUPTYPE_PROPS = {
            { "1. Bundesliga" },
            { "2. Bundesliga" },
            { "Regionalliga Nord" },
            { "Regionalliga Süd" },
            { "Oberliga Nordrhein" },
            { "Bayernliga" },
            { "Finale" },
            { "Halbfinale" },
            { "Viertelfinale" },
            { "Achtelfinale" }
    };

    /** 1. Bundesliga steht an Index 0 in der Ligen. */
    public static final int BULI_1 = 0;

    /** 2. Bundesliga steht an Index q in der Ligen. */
    public static final int BULI_2 = 1;

    /** RegioNord steht an Index w in der Ligen. */
    public static final int REGIO_NORD = 2;

    /** RegioSüd steht an Index 3 in der Ligen. */
    public static final int REGIO_SUED = 3;

    /** OL Nordrhein steht an Index 4 in der Ligen. */
    public static final int OL_NORDRHEIN = 4;

    /** OL Bayern steht an Index 5 in der Ligen. */
    public static final int OL_BAYERN = 5;

    /** Finale steht an Index 6 in der Ligen. */
    public static final int FINALE = 6;

    /** Halbfinale steht an Index 7 in der Ligen. */
    public static final int HALBFINALE = 7;

    /** Viertelfinal steht an Index 8 in der Ligen. */
    public static final int VIERTELFINALE = 8;

    /** Achtelfinale steht an Index 9 in der Ligen. */
    public static final int ACHTELFINALE = 9;

    /** Die Gruppentypen. */
    private List<GroupType> groupTypes = new ArrayList<GroupType>();

    public DummyGroups() {
        createGroups();
    }

    /**
     * Liefert die generierten Gruppentypen.
     *
     * @return Die Gruppentypen.
     */
    public GroupType[] groupTypes() {
        return groupTypes.toArray(new GroupType[groupTypes.size()]);
    }

    /**
     * Liefert die generierten Gruppentypen.
     *
     * @return Die Gruppentypen.
     */
    public List<GroupType> toList() {
        return Collections.unmodifiableList(groupTypes);
    }

    /**
     * Einen neue Testliga den Ligen hinzufügen.
     *
     * @param groupType
     *            Eine neue Testliga.
     */
    public void add(final GroupType groupType) {
        groupTypes.add(groupType);
    }

    /**
     * Entfernt eine Liga.
     *
     * @param groupType
     *            Die zu entfernende Liga.
     */
    public void delete(final GroupType groupType) {
        groupTypes.remove(groupType);
    }

    /**
     * Erzeugt eine Latte an Gruppentypen.
     */
    private void createGroups() {
        for (int i = 0; i < GROUPTYPE_PROPS.length; i++) {
            GroupType groupType = new GroupType();
            groupType.setName(GROUPTYPE_PROPS[i][0]);
            groupTypes.add(groupType);
        }
    }

}
