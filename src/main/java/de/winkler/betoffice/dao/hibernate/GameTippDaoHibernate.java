/*
 * $Id: GameTippDaoHibernate.java 3798 2013-08-05 18:58:15Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2013 by Andre Winkler. All rights reserved.
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

import de.winkler.betoffice.dao.GameTippDao;
import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.GameTipp;
import de.winkler.betoffice.storage.User;

/**
 * Hibernate DAO für den Zugriff auf {@link GameTipp}.
 *
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3798 $ $LastChangedDate: 2013-08-05 20:58:15 +0200 (Mon, 05 Aug 2013) $
 */
@Repository("gameTippDao")
public class GameTippDaoHibernate extends AbstractCommonDao<GameTipp> implements
        GameTippDao {

    /**
     * Sucht nach allen Spieltipps zu einem Spiel.
     */
    private static final String QUERY_GAMETIPP_BY_MATCH = "from "
            + "    GameTipp gametipp inner join fetch "
            + "    gametipp.user inner join fetch " + "    gametipp.game "
            + "where " + "    gametipp.game.id = :gameId";

    /**
     * Sucht nach allen Spieltipps zu einem Spieltag von einem Teilnehmer.
     * Hier auch das entsprechende SQL Statement: 
     * <pre>
     * SELECT *
     * FROM
     *     bo_season s,
     *     bo_gamelist gl,
     *     bo_game g,
     *     bo_gametipp t,
     *     bo_user u
     * WHERE
     *         gl.bo_season_ref  = s.id
     *     AND s.id              = :season_id
     *     AND gl.bo_index       = 0
     *     AND g.bo_gamelist_ref = gl.id
     *     ADN g.id              = t.bo_game_ref
     *     AND t.bo_user_ref     = u.id
     *     AND u.bo_nickname     = :nickname;
     * </pre>
     */
    private static final String QUERY_GAMETIPP_BY_SEASON_ROUND_AND_USER = AbstractCommonDao
            .loadQuery("hql_gametipp_season_round_user.sql");

    public GameTippDaoHibernate() {
        super(GameTipp.class);
    }

    @Override
    public List<GameTipp> findByMatch(final Game match) {
        @SuppressWarnings("unchecked")
        List<GameTipp> tipps = getSessionFactory().getCurrentSession()
                .createQuery(QUERY_GAMETIPP_BY_MATCH)
                .setParameter("gameId", match.getId()).list();

        return tipps;
    }

    @Override
    public List<GameTipp> findTippsByRoundAndUser(final GameList round,
            final User user) {

        @SuppressWarnings("unchecked")
        List<GameTipp> objects = getSessionFactory().getCurrentSession()
                .createQuery(QUERY_GAMETIPP_BY_SEASON_ROUND_AND_USER)
                .setParameter("roundId", round.getId())
                .setParameter("userId", user.getId()).list();
        return objects;
    }

    @Override
    public List<GameTipp> findTippsByRoundAndUser(long seasonId, long roundId,
            String nickname) {
        return null;
    }

}
