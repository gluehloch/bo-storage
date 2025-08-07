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

import de.betoffice.dao.PlayerDao;
import de.betoffice.storage.Player;

/**
 * The implementation of {@link PlayerDao}.
 *
 * @author by Andre Winkler
 */
@Repository("playerDao")
public class PlayerDaoHibernate extends AbstractCommonDao<Player>
        implements PlayerDao {

    public PlayerDaoHibernate() {
        super(Player.class);
    }

    @Override
    public List<Player> findAll() {
        return getEntityManager()
                .createQuery("from Player as player order by player.name",
                        Player.class)
                .getResultList();
    }

    @Override
    public Optional<Player> findByOpenligaid(long openligaid) {
        TypedQuery<Player> query = getEntityManager()
                .createQuery(
                        "from Player as player where player.openligaid = :openligaid",
                        Player.class)
                .setParameter("openligaid", openligaid);
        return singleResult(query);
    }

    @Override
    public Optional<Player> findAllGoalsOfPlayer(long id) {
        TypedQuery<Player> query = getEntityManager()
                .createQuery(
                        "from Player as player left join fetch player.goals where player.id = :id",
                        Player.class)
                .setParameter("id", id);
        return singleResult(query);
    }

}
