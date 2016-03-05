/*
 * $Id: PerformanceMeasurement.java 3782 2013-07-27 08:44:32Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2012 by Andre Winkler. All rights reserved.
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

package de.winkler.betoffice.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;

import de.awtools.basic.LoggerFactory;

/**
 * Messung der Performance einzelner Service Methoden.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3782 $ $LastChangedDate: 2013-07-27 10:44:32 +0200 (Sat, 27 Jul 2013) $
 */
@Aspect
public class PerformanceMeasurement {

    private final Logger log = LoggerFactory.make();

    private long startTime;
    private long endTime;

    public void startMeasurement() {
        startTime = System.currentTimeMillis();
    }

    public void stopMeasurement() {
        endTime = System.currentTimeMillis();
    }

    @Around("execution(* de.winkler.betoffice.service.Default*Service.*(..))")
    public Object doBasicProfiling(final ProceedingJoinPoint pjp)
            throws Throwable {

        startMeasurement();
        Object retVal = pjp.proceed();
        stopMeasurement();

        StringBuilder sb = new StringBuilder("Runtime measurement: ");
        sb.append(pjp.getTarget().getClass()).append("#");
        sb.append(pjp.toShortString());
        sb.append(" (ms): ").append(endTime - startTime);
        log.info(sb.toString());

        return retVal;
    }

}
