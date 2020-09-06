/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2016 by Andre Winkler. All
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
 *
 */

package de.winkler.betoffice.dao.hibernate;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import de.winkler.betoffice.dao.MaintenanceDao;

/**
 * Hibernate Implementierung für das DAO {@link MaintenanceDao}.
 *
 * @author Andre Winklers
 */
@Repository("maintenanceDao")
public class MaintenanceDaoHibernate extends HibernateDaoSupport implements MaintenanceDao {

    @Override
    public Object executeHql(String hql) {
        return getHibernateTemplate().find(hql);
    }

    @Override
    public Object executeSql(String sqlQuery) {
        return getSessionFactory().openSession().createNativeQuery(sqlQuery).getResultList();
    }

    @Autowired
    public void wrapTheSetSessionFactory(final SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }

}
