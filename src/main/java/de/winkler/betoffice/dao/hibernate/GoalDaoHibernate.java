/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2014 by Andre Winkler. All
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

package de.winkler.betoffice.dao.hibernate;

import java.util.List;
import java.util.Optional;

import org.hibernate.query.Query;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import de.winkler.betoffice.dao.GoalDao;
import de.winkler.betoffice.storage.Game;
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

    @Override
    public List<Goal> findAll() {
        return getSessionFactory().getCurrentSession()
                .createQuery(
                        "from Goal as goal inner join fetch goal.player order by goal.id",
                        Goal.class)
                .getResultList();
    }

    @Override
    public Optional<Goal> findByOpenligaid(long openligaid) {
        Query<Goal> query = getSessionFactory().getCurrentSession()
                .createQuery(
                        "from Goal as goal where goal.openligaid = :openligaid",
                        Goal.class)
                .setParameter("openligaid", openligaid,
                        StandardBasicTypes.LONG);
        return singleResult(query);
    }

    @Override
    public List<Goal> find(Game match) {
        List<Goal> goals = getSessionFactory().getCurrentSession()
                .createQuery("from Goal as goal where goal.game.id = :matchId",
                        Goal.class)
                .setParameter("matchId", match.getId(), StandardBasicTypes.LONG)
                .getResultList();
        return goals;
    }

    @Override
    public void deleteAll(Game game) {
        getSessionFactory().getCurrentSession()
                .createSQLQuery("delete from goal g where gaol.game = :game");
    }

}
