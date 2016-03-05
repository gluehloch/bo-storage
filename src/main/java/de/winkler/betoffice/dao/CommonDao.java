/*
 * $Id: CommonDao.java 3782 2013-07-27 08:44:32Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2011 by Andre Winkler. All rights reserved.
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

/**
 * Allgemeine DAO Schnittstelle. Definiert die üblichen CRUD Methoden <code>
 * save(), saveAll(), update(), updateAll(), delete(), deleteAll()</code>.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3782 $ $LastChangedDate: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 *
 * @param <T> Der Typ für den dieses DAO zuständig ist.
 */
public interface CommonDao<T> {
    
    /**
     * Liefert ein T.
     *
     * @param id Identifier.
     * @return An entity with identifier id.
     */
    public T findById(long id);

    /**
     * Legt ein T an.
     *
     * @param t Ein neues T.
     */
    public void save(T t);

    /**
     * Speichert mehrere neue Ts.
     *
     * @param ts Eine Liste von Ts.
     */
    public void saveAll(List<T> ts);

    /**
     * Ein Update.
     *
     * @param t Ein T
     */
    public void update(T t);

    /**
     * Update für alle Ts.
     *
     * @param ts Eine Liste von Ts.
     */
    public void updateAll(List<T> ts);

    /**
     * Löscht ein T.
     *
     * @param t Ein Teilnehmer.
     */
    public void delete(T t);

    /**
     * Löscht alle Ts.
     *
     * @param ts Die Ts.
     */
    public void deleteAll(List<T> ts);

}
