/*
 * $Id: SeasonDao.java 3782 2013-07-27 08:44:32Z andrewinkler $
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

package de.winkler.betoffice.dao;

import java.util.List;

import de.winkler.betoffice.storage.Group;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.TeamResult;

/**
 * DAO Klasse für den Zugriff auf {@link de.winkler.betoffice.storage.Season}
 * Objekte.
 *
 * @author by Andre Winkler
 */
public interface SeasonDao {

    /**
     * Liefert eine Liste aller Meisterschaften.
     *
     * @return Eine Liste aller Meisterschaften.
     */
    public List<Season> findAll();

    /**
     * Liefert eine Meisterschaft mit gesuchten Namen und Jahrgang.
     *
     * @param name Der gesuchte Name.
     * @param year Der gesuchte Jahrgang
     * @return Eine Meisterschaft.
     */
    public Season findByName(String name, String year);

    /**
     * Liefert eine Meisterschaft mit der gesuchten ID.
     *
     * @param id Datenbank ID für die Meisterschaft.
     * @return Eine Meisterschaft.
     */
    public Season findById(long id);

    /**
     * Sucht nach den Gruppen-, Mannschafts- und Teilnehmerbeziehungen zu der
     * übegebenen Meisterschaft.
     *
     * @param season Die betreffende Meisterschaft.
     * @return Eine Meisterschaft mit gesetzten Referenzen zu allen Spieltagen,
     *     Gruppen, Mannschaften und Teilnehmern. 
     */
    public Season findRoundGroupTeamUser(Season season);

    /**
     * Sucht nach den Gruppen-, Mannschafts- und Teilnehmerbeziehungen zu der
     * übegebenen Meisterschaft, sowie alle Tipps für alle Teilnehmer.
     *
     * @param season Die betreffende Meisterschaft.
     * @return Eine Meisterschaft mit gesetzten Referenzen zu allen Spieltagen,
     *     Gruppen, Mannschaften und Teilnehmern, sowie alle Tipps für alle
     *     Teilnehmer. 
     */
    public Season findRoundGroupTeamUserTipp(Season season);

    /**
     * Legt eine neue Meisterschaft an.
     *
     * @param season Eine Meisterschaft.
     */
    public void save(Season season);

    /**
     * Eine Update-Operation.
     *
     * @param season Eine Season.
     */
    public void update(Season season);

    /**
     * Löscht eine Meisterschaft.
     *
     * @param season Eine Meisterschaft.
     */
    public void delete(Season season);

    /**
     * Startet die Tabellenberechnung der Mannschaften einer Meisterschaft.
     *
     * @param season Die Meisterschaft.
     * @param group Die Liga/Gruppe die berechnet werden soll.
     * @return Eine sortierte Liste der Tabelle.
     */
    public List<TeamResult> calculateTeamRanking(Season season, Group group);

    /**
     * Startet die Tabellenberechnung der Mannschaften einer Meisterschaft über
     * bestimmte Spielrunden. 
     *
     * @param season Die Meisterschaft.
     * @param group Die Liga/Gruppe die berechnet werden soll.
     * @param startIndex Index des Start-Spieltags (0..N-1).
     * @param endIndex Index des End-Spieltags (0..N-1).
     * @return Eine sortierte Liste der Tabelle.
     */
    public List<TeamResult> calculateTeamRanking(Season season, Group group,
            int startIndex, int endIndex);

}
