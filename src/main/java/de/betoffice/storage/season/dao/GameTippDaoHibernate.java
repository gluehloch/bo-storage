/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2017 by Andre Winkler. All
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

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import de.betoffice.storage.hibernate.AbstractCommonDao;
import de.betoffice.storage.season.GameTippDao;
import de.betoffice.storage.season.entity.Game;
import de.betoffice.storage.tip.GameTipp;
import de.betoffice.storage.user.entity.User;

/**
 * Hibernate DAO fuer den Zugriff auf {@link GameTipp}.
 *
 * @author by Andre Winkler
 */
@Repository("gameTippDao")
public class GameTippDaoHibernate extends AbstractCommonDao<GameTipp> implements GameTippDao {

    /**
     * Sucht nach allen Spieltipps zu einem Spieltag.
     */
    private static final String QUERY_GAMETIPP_BY_MATCH = "from "
            + "    GameTipp gametipp"
            + "    join fetch gametipp.user "
            + "    join fetch gametipp.game "
            + "where "
            + "    gametipp.game.id = :gameId";

    /**
     * Sucht nach einem Spieltipp zu Spiel und Teilnehmer.
     */
    private static final String QUERY_GAMETIPP_BY_MATCH_AND_USER = "from "
            + "    GameTipp gametipp"
            + "    join fetch gametipp.user "
            + "    join fetch gametipp.game "
            + "where "
            + "    gametipp.game.id = :gameId "
            + "    and gametipp.user.id = :userId";

    private static final String QUERY_ROUND_AND_TIPPS_BY_USER_AND_ROUND = "select "
            + "    tipp "
            + "from "
            + "    GameTipp as tipp "
            + "    join fetch tipp.game game "
            + "    join fetch game.homeTeam "
            + "    join fetch game.guestTeam "
            + "    join fetch game.group "
            + "    join fetch game.gameList round "
            + "    join fetch tipp.user user "
            + "where "
            + "    round.id = :roundId "
            + "    and user.id = :userId";

    private static final String QUERY_ROUND_AND_TIPPS_BY_ROUND = "select "
            + "    tipp "
            + "from "
            + "    GameTipp as tipp "
            + "    join fetch tipp.game game "
            + "    join fetch game.homeTeam "
            + "    join fetch game.guestTeam "
            + "    join fetch game.group "
            + "    join fetch game.gameList round "
            + "    join fetch tipp.user user "
            + "where "
            + "    round.id = :roundId";

    public GameTippDaoHibernate() {
        super(GameTipp.class);
    }

    @Override
    public List<GameTipp> find(Game match) {
        List<GameTipp> tipps = getEntityManager()
                .createQuery(QUERY_GAMETIPP_BY_MATCH, GameTipp.class)
                .setParameter("gameId", match.getId())
                .getResultList();
        return tipps;
    }

    @Override
    public Optional<GameTipp> find(Game game, User user) {
        Optional<GameTipp> tipp = getEntityManager().unwrap(Session.class)
                .createQuery(QUERY_GAMETIPP_BY_MATCH_AND_USER, GameTipp.class)
                .setParameter("gameId", game.getId())
                .setParameter("userId", user.getId())
                .uniqueResultOptional();
        return tipp;
    }

    @Override
    public List<GameTipp> find(long roundId, long userId) {
        List<GameTipp> tipps = getEntityManager()
                .createQuery(QUERY_ROUND_AND_TIPPS_BY_USER_AND_ROUND, GameTipp.class)
                .setParameter("roundId", roundId)
                .setParameter("userId", userId)
                .getResultList();
        return tipps;
    }

    @Override
    public List<GameTipp> find(long roundId) {
        List<GameTipp> tipps = getEntityManager()
                .createQuery(QUERY_ROUND_AND_TIPPS_BY_ROUND, GameTipp.class)
                .setParameter("roundId", roundId)
                .getResultList();
        return tipps;
    }

}
