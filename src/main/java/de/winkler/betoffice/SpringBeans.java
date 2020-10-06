/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2018 by Andre Winkler. All rights reserved.
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

package de.winkler.betoffice;

import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import de.winkler.betoffice.util.LoggerFactory;

/**
 * Kapselt den Zugriff auf die Beans mit Spring.
 *
 * @author Andre Winkler
 */
public class SpringBeans {

    private final Logger log = LoggerFactory.make();

    /** Die BeanFactory. */
    private final FileSystemXmlApplicationContext applicationContext;

    /**
     * Initialisierung.
     *
     * @param applicationResource
     *            Enthält die Spring-Konfigurationen.
     */
    public SpringBeans(final String applicationResource) {
        this(new String[] { applicationResource });
    }

    /**
     * Initialisierung.
     *
     * @param applicationResources
     *            Enthält die Spring-Konfigurationen.
     */
    public SpringBeans(final String[] applicationResources) {
        if (log.isInfoEnabled()) {
            for (String resource : applicationResources) {
                log.info("ApplicationResource..: {}", resource);
            }
        }

        FileSystemXmlApplicationContext context = null;
        try {
            context = new FileSystemXmlApplicationContext(applicationResources);
        } catch (BeansException ex) {
            log.error("BeanException", ex);
            throw new RuntimeException(ex);
        }

        applicationContext = context;
    }

    /**
     * Liefert die <code>BeanFactory</code>.
     *
     * @return Eine <code>BeanFactory</code>.
     */
    protected final FileSystemXmlApplicationContext getBeanFactory() {
        return applicationContext;
    }

    /**
     * Liefert ein Objekt zu der gesuchten <code>beanId</code>.
     *
     * @param <T>
     *            Der erwartete Typ.
     * @param beanId
     *            Die ID des Objekts.
     * @return Ein Objekt passend zu der gesuchten ID.
     */
    @SuppressWarnings("unchecked")
    public final <T> T getBean(final String beanId) {
        return (T) getBeanFactory().getBean(beanId);
    }

}
