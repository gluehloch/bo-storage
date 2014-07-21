/*
 * $Id: DummyTeams.java 3782 2013-07-27 08:44:32Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2010 by Andre Winkler. All rights reserved.
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

import de.winkler.betoffice.storage.Team;

/**
 * Eine Utility Klasse für die Testunterstützung. Diese Klasse generiert
 * Mengen von StorageObjects.
 * <br>
 * <strong>Nur zu Testzwecken zu verwenden!</strong>
 *
 * @author  $author$
 * @version $Revision: 3782 $ $Date: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
public final class DummyTeams {

    /** Eine Sammlung von Mannschaften. */
    public static final String[][] TEAM_PROPS = {
        {"RWE", "Rot-Weiss-Essen", "rwe.gif"},
        {"S04", "Schalke 04", "schalke.gif"},
        {"MGladbach", "Borussia Mönchengladbach", "gladbach.gif"},
        {"BVB", "Borussia Dortmund", "bvb.gif"},
        {"FCB", "Bayern München", "bayern.gif"},
        {"St.Pauli", "St.Pauli", "pauli.gif"},
        {"1860", "1860 München", "loewen.gif"},
        {"HSV", "Hamburger SV", "hsv.gif"},
        {"Bochum", "VfL Bochum", "bochum.gif"},
        {"Luebeck", "VfB Luebeck", "luebeck.gif"}
    };

    /** RWE steht an Index 0 in der Mannschaften. */
    public static final int RWE = 0;

    /** SO4 steht an Index 1 in der Mannschaften. */
    public static final int S04 = 1;

    /** Gladbach steht an Index 2 in der Mannschaften. */
    public static final int MGLADBACH = 2;

    /** BVB steht an Index 3 in der Mannschaften. */
    public static final int BVB = 3;

    /** Baynern München steht an Index 4 in der Mannschaften. */
    public static final int FCB = 4;

    /** St.Pauli steht an Index 5 in der Mannschaften. */
    public static final int STPAULI = 5;

    /** TSV 1860 steht an Index 6 in der Mannschaften. */
    public static final int TSV1860 = 6;

    /** HSV steht an Index 7 in der Mannschaften. */
    public static final int HSV = 7;

    /** Bochum steht an Index 8 in der Mannschaften. */
    public static final int BOCHUM = 8;

    /** VfB Lübeck steht an Index 9 in der Mannschaften. */
    public static final int LUEBECK = 9;

    /** Die Mannschaften. */
    private final List<Team> teams = new ArrayList<Team>();

    /**
     * Konstruktor.
     */
    public DummyTeams() {
        createTeams();
    }

    /**
     * Liefert die generierten Mannschaften.
     *
     * @return Die Mannschaften.
     */
    public Team[] teams() {
        return teams.toArray(new Team[teams.size()]);
    }

    /**
     * Liefert die generierten Mannschaften als nicht modifizierbare Liste.
     *
     * @return Die Mannschaften.
     */
    public List<Team> toList() {
        return Collections.unmodifiableList(teams);
    }

    /**
     * Fügt eine weitere Testmannschaft den Mannschaften hinzu.
     *
     * @param team Eine neue Testmannschaft.
     */
    public void add(final Team team) {
        teams.add(team);
    }

    /**
     * Entfernt eine Testmannschaft.
     *
     * @param team Eine Mannschaft.
     */
    public void delete(final Team team) {
        teams.remove(team);
    }

    /**
     * Erzeugt eine Latte an Mannschaften.
     *
     * @param factory Eine <code>StorageFactory</code>.
     */
    private void createTeams() {
        for (int i = 0; i < TEAM_PROPS.length; i++) {
            Team team = new Team();
            team.setName(TEAM_PROPS[i][0]);
            team.setLongName(TEAM_PROPS[i][1]);
            team.setLogo(TEAM_PROPS[i][2]);
            teams.add(team);
        }
    }

}
