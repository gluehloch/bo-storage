/*
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

package de.winkler.betoffice;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import de.winkler.betoffice.service.AutoTippService;
import de.winkler.betoffice.service.MasterDataManagerService;
import de.winkler.betoffice.service.SeasonManagerService;
import de.winkler.betoffice.service.TippService;
import de.winkler.betoffice.util.LoggerFactory;

/**
 * Verwaltet die Betoffice Services.
 * 
 * @author by Andre Winkler
 */
public class BetofficeStore {

    private final Logger log = LoggerFactory.make();

    /** Liefert den Zugriff auf den Spring-Kontext. */
    private final SpringBeans springBeans;

    /** Service für den Zugriff auf die Daten einer Meisterschaft. */
    private final SeasonManagerService sms;

    /** Service zur Verwaltung von Spieltipps. */
    private final TippService tippService;

    /** Service für den Zugriff auf die Stammdaten. */
    private final MasterDataManagerService mdms;

    /** Service für das Anlegen von automatisierten Tipps. */
    private final AutoTippService autoTippService;

    /**
     * Startet das Spring-Wiring. Es wird die Default-Spring-Konfiguration
     * verwendet. Daten werden persistent angelegt.
     *
     * @param appContextUserProperties
     *            Eine Datei mit Benutzereinstellungen.
     */
    public BetofficeStore(final String appContextUserProperties) {
        this(new String[] { appContextUserProperties });
    }

    /**
     * Startet das Spring-Wiring.
     *
     * @param applicationResources
     *            Die Spring-Konfigurationen.
     */
    public BetofficeStore(final String[] applicationResources) {
        List<String> confs = new ArrayList<String>();
        confs.add("classpath:betoffice-datasource.xml");
        confs.add("classpath:betoffice-persistence.xml");
        confs.add("file:hibernate.xml");
        for (String resource : applicationResources) {
            confs.add(resource);
        }

        String[] resources = confs.toArray(new String[confs.size()]);

        if (log.isInfoEnabled()) {
            for (String resource : resources) {
                log.info("Resource: " + resource);
            }
        }

        springBeans = new SpringBeans(resources);

        sms = springBeans.getBean("seasonManagerService");
        mdms = springBeans.getBean("masterDataManagerService");
        autoTippService = springBeans.getBean("autoTippService");
        tippService = springBeans.getBean("tippService");
    }

    /**
     * Liefert die über Spring konfigurierten Objekte.
     *
     * @return Die Spring Beans.
     */
    public final SpringBeans getBeans() {
        return springBeans;
    }

    /**
     * Service für den Zugriff auf die Daten einer Meisterschaft.
     *
     * @return Ein Service.
     */
    public final SeasonManagerService getSeasonManagerService() {
        return sms;
    }

    /**
     * Service fuer die Verwaltung von Spieltipps.
     *
     * @return Ein Service.
     */
    public final TippService getTippService() {
        return tippService;
    }

    /**
     * Service für den Zugriff auf die Stammdaten.
     *
     * @return Ein Service.
     */
    public final MasterDataManagerService getMasterDataManagerService() {
        return mdms;
    }

    /**
     * Service für das automatisierte erstellen von Tipps.
     *
     * @return Ein Service.
     */
    public final AutoTippService getAutoTippService() {
        return autoTippService;
    }

}
