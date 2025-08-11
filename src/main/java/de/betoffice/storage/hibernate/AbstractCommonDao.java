/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2023 by Andre Winkler. All
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

package de.betoffice.storage.hibernate;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import de.betoffice.storage.community.CommonDao;

/**
 * Utility Implementierung von {@link CommonDao}.
 *
 * @author     by Andre Winkler
 *
 * @param  <T> Der Typ den dieses DAO unterstützt.
 */
public abstract class AbstractCommonDao<T> implements CommonDao<T> {

    private final Class<T> t;

    // -- entityManager ------------------------------------------------------

    private EntityManager entityManager;

    @Autowired
    public void setEntityManager(final EntityManager _entityManager) {
        entityManager = _entityManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    // ------------------------------------------------------------------------

    protected AbstractCommonDao(final Class<T> _t) {
        t = _t;
    }

    public T findById(final long id) {
        return (T) getEntityManager().find(t, id);
    }

    @Override
    public void delete(final T t) {
        getEntityManager().remove(t);
    }

    @Override
    public T persist(final T t) {
        getEntityManager().persist(t);
        return t;
    }

    @Override
    public void update(final T t) {
        getEntityManager().merge(t);
    }

    @Override
    public void refresh(final T t) {
        getEntityManager().refresh(t);
    }

    /**
     * Liefert das erste Element aus einer Liste. Ist die Liste leer, liefert die Methode <code>null</code> zurück. Wird
     * <code>null</code> übergeben, wirft die Methode eine {@link NullPointerException}.
     *
     * @param  objects
     * @return         Das erste Objekt aus der Liste.
     */
    protected T first(final List<T> objects) {
        if (objects.size() == 0) {
            return null;
        } else {
            return objects.get(0);
        }
    }

    /**
     * Lädt eine SQL Query aus den Java Resourcen.
     *
     * @param  clazz Die Klasse, die die Query Resourcen enthält.
     * @param  query Query Resourcen Name.
     * @return       Die Query oder eine {@link RuntimeException} falls nichts gefunden werden konnte.
     */
    public static final String loadQuery(final Class<?> clazz, final String query) {
        try {
            final InputStream resourceAsStream = clazz.getResourceAsStream(query);
            if (resourceAsStream == null) {
                throw new RuntimeException(
                        "Could not find query resource: " + query + " for class: " + clazz.getName());
            }
            return IOUtils.toString(resourceAsStream, Charset.forName("UTF-8"));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static <T> Optional<T> singleResult(TypedQuery<T> query) {
        Optional<T> optionalResult;
        try {
            T result = query.getSingleResult();
            optionalResult = Optional.of(result);
        } catch (NoResultException ex) {
            optionalResult = Optional.empty();
        }
        return optionalResult;
    }

    /**
     * Extract from a query result list the first element.
     * 
     * @param  list A Hibernate query result list
     * @return      The query single result
     */
    public static <T> Optional<T> singleResult(List<T> list) {
        Optional<T> optionalResult = Optional.empty();
        if (list.size() > 0) {
            optionalResult = Optional.of(list.get(0));
        }
        return optionalResult;
    }

}
