/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2014 by Andre Winkler. All rights reserved.
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

package de.winkler.betoffice.dao.hibernate;

import java.util.List;

import org.springframework.stereotype.Repository;

import de.winkler.betoffice.dao.GoalDao;
import de.winkler.betoffice.storage.Goal;

/**
 * The default implementation of the {@link GoalDao}.
 *
 * @author by Andre Winkler
 */
@Repository("goalDao")
public class GoalDaoHibernate extends AbstractCommonDao<Goal> implements GoalDao {

    public GoalDaoHibernate() {
        super(Goal.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Goal> findAll() {
        return getSessionFactory().getCurrentSession()
                .createQuery("from Goal as goal order by goal.id")
                .list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Goal findByOpenligaid(long openligaid) {
        List<Goal> goals = getSessionFactory()
                .getCurrentSession()
                .createQuery(
                        "from Goal as goal where goal.openligaid = :openligaid")
                .setLong("openligaid", openligaid).list();
        return first(goals);
    }


}
