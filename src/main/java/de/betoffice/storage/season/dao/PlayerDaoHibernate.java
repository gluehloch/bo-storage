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
import de.betoffice.storage.season.PlayerDao;
import de.betoffice.storage.season.entity.PlayerEntity;

/**
 * The implementation of {@link PlayerDao}.
 *
 * @author by Andre Winkler
 */
@Repository("playerDao")
public class PlayerDaoHibernate extends AbstractCommonDao<PlayerEntity>
        implements PlayerDao {

    public PlayerDaoHibernate() {
        super(PlayerEntity.class);
    }

    @Override
    public List<PlayerEntity> findAll() {
        return getEntityManager()
                .createQuery("from PlayerEntity as player order by player.name",
                        PlayerEntity.class)
                .getResultList();
    }

    @Override
    public Optional<PlayerEntity> findByOpenligaid(long openligaid) {
        TypedQuery<PlayerEntity> query = getEntityManager()
                .createQuery(
                        "from PlayerEntity as player where player.openligaid = :openligaid",
                        PlayerEntity.class)
                .setParameter("openligaid", openligaid);
        return singleResult(query);
    }

    @Override
    public Optional<PlayerEntity> findAllGoalsOfPlayer(long id) {
        TypedQuery<PlayerEntity> query = getEntityManager()
                .createQuery(
                        "from PlayerEntity as player left join fetch player.goals where player.id = :id",
                        PlayerEntity.class)
                .setParameter("id", id);
        return singleResult(query);
    }

}
