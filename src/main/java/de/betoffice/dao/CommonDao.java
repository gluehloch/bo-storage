/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2024 by Andre Winkler. All
 * rights reserved.
 * ============================================================================
 * GNU GENERAL PUBLIC LICENSE TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND
 * MODIFICATION
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

package de.betoffice.dao;

/**
 * Allgemeine DAO Schnittstelle. Definiert die üblichen CRUD Methoden <code>
 * save(), saveAll(), update(), updateAll(), delete(), deleteAll()</code>.
 *
 * @author     by Andre Winkler
 *
 * @param  <T> Der Typ für den dieses DAO zuständig ist.
 */
public interface CommonDao<T> {

    /**
     * Liefert ein T.
     *
     * @param  id Identifier.
     * @return    An entity with identifier id.
     */
    T findById(long id);

    /**
     * Legt ein T an.
     *
     * @param t Ein neues T.
     */
    T persist(T t);

    /**
     * Ein Update.
     *
     * @param t Ein T
     */
    void update(T t);

    /**
     * Löscht ein T.
     *
     * @param t Ein Teilnehmer.
     */
    void delete(T t);

    /**
     * Re-associate an entity with the session.
     * 
     * Copied from the Hibernate documentation:
     * 
     * Re-read the state of the given instance from the underlying database. It is inadvisable to use this to implement
     * long-running sessions that span many business tasks. This method is, however, useful in certain special
     * circumstances. For example where a database trigger alters the object state upon insert or update after executing
     * direct SQL (eg. a mass update) in the same session after inserting a Blob or Clob
     * 
     * @param t T
     */
    void refresh(T t);

}
