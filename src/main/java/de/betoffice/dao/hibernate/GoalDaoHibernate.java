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

package de.betoffice.dao.hibernate;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import de.betoffice.dao.GoalDao;
import de.betoffice.storage.Game;
import de.betoffice.storage.Goal;

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
        return getEntityManager()
                .createQuery(
                        "select goal from Goal goal inner join fetch goal.player order by goal.id",
                        Goal.class)
                .getResultList();
    }

    @Override
    public Optional<Goal> findByOpenligaid(long openligaid) {
        TypedQuery<Goal> query = getEntityManager()
                .createQuery(
                        "select goal from Goal goal where goal.openligaid = :openligaid",
                        Goal.class)
                .setParameter("openligaid", openligaid);
        return singleResult(query);
    }

    @Override
    public List<Goal> find(Game match) {
    	return find(match.getId());
    }

    @Override
    public List<Goal> find(long matchId) {
        List<Goal> goals = getEntityManager()
                .createQuery("select goal from Goal goal where goal.game.id = :matchId order by goal.minute",
                        Goal.class)
                .setParameter("matchId", matchId)
                .getResultList();
        return goals;
    }

    @Override
    public void deleteAll(Game game) {
        getEntityManager().createNativeQuery("DELETE bo_goal g WHERE g.bo_game_ref = :gameId").setParameter("gameId", game.getId());
    }

}
