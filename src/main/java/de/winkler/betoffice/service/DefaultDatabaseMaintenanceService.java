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

package de.winkler.betoffice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.winkler.betoffice.dao.MaintenanceDao;

/**
 * Default-Implementierung für den {@link DatabaseMaintenanceService}.
 *
 * @author by Andre Winkler
 */
@Service("databaseMaintenanceService")
public class DefaultDatabaseMaintenanceService extends AbstractManagerService
        implements DatabaseMaintenanceService {

    @Autowired
    private MaintenanceDao maintenanceDao;
    
    @Transactional(readOnly = true)
    public Object executeHql(String hql) {
        return maintenanceDao.executeHql(hql);
    }

    @Transactional(readOnly = true)
    public Object executeSQL(String sqlQuery) {
        return maintenanceDao.executeSql(sqlQuery);
    }

}
