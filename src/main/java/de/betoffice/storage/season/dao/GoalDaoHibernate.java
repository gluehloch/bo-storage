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

package de.betoffice.storage.season.dao;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import de.betoffice.storage.hibernate.AbstractCommonDao;
import de.betoffice.storage.season.GoalDao;
import de.betoffice.storage.season.entity.GameEntity;
import de.betoffice.storage.season.entity.GoalEntity;

/**
 * The default implementation of the {@link GoalDao}.
 *
 * @author by Andre Winkler
 */
@Repository("goalDao")
public class GoalDaoHibernate extends AbstractCommonDao<GoalEntity> implements GoalDao {

    public GoalDaoHibernate() {
        super(GoalEntity.class);
    }

    @Override
    public List<GoalEntity> findAll() {
        return getEntityManager()
                .createQuery(
                        "select goal from GoalEntity goal inner join fetch goal.player order by goal.id",
                        GoalEntity.class)
                .getResultList();
    }

    @Override
    public Optional<GoalEntity> findByOpenligaid(long openligaid) {
        TypedQuery<GoalEntity> query = getEntityManager()
                .createQuery(
                        "select goal from GoalEntity goal where goal.openligaid = :openligaid",
                        GoalEntity.class)
                .setParameter("openligaid", openligaid);
        return singleResult(query);
    }

    @Override
    public List<GoalEntity> find(GameEntity match) {
    	return find(match.getId());
    }

    @Override
    public List<GoalEntity> find(long matchId) {
        List<GoalEntity> goals = getEntityManager()
                .createQuery("select goal from GoalEntity goal where goal.game.id = :matchId order by goal.minute",
                        GoalEntity.class)
                .setParameter("matchId", matchId)
                .getResultList();
        return goals;
    }

    @Override
    public void deleteAll(GameEntity game) {
        getEntityManager().createNativeQuery("DELETE bo_goal g WHERE g.bo_game_ref = :gameId").setParameter("gameId", game.getId());
    }

}
