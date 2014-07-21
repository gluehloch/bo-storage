/*
 * $Id: RoundDaoHibernate.java 3850 2013-11-30 18:24:08Z andrewinkler $
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

import java.math.BigInteger;
import java.util.List;

import org.hibernate.SQLQuery;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

import de.winkler.betoffice.dao.RoundDao;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.Season;

/**
 * Eine Hibernate-DAO Implementierung zur Verwaltung eines Spieltags.
 * 
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3850 $ $LastChangedDate: 2013-11-29 22:36:13
 *          +0100 (Fr, 29 Nov 2013) $
 */
@Repository("roundDao")
public class RoundDaoHibernate extends AbstractCommonDao<GameList> implements
		RoundDao {

	/**
	 * Sucht nach allen Spieltagen einer Meisterschaft.
	 */
	private static final String QUERY_GAMELIST_BY_SEASON = "from "
			+ "GameList as gamelist " + "where gamelist.season.id = :seasonId "
			+ "order by gamelist.index";

	/**
	 * Sucht einem Spieltag einer Meisterschaft.
	 */
	private static final String QUERY_GAMELIST_BY_SEASON_AND_INDEX = "from "
			+ "GameList as gamelist "
			+ "where gamelist.season.id = :seasonId and gamelist.index = :gameListIndex";

	/**
	 * Sucht nach allen Gruppen, Spielen, Mannschaften, Tipps und Tippteilnehmer
	 * einer Spielrunde/Spieltag.
	 */
	private static final String QUERY_ALL_ROUND_OBJECTS = "select round from GameList as round "
			+ "left join fetch round.gameList game "
			+ "left join fetch game.tippList tipp "
			+ "left join fetch tipp.user u "
			+ "left join fetch game.homeTeam "
			+ "left join fetch game.guestTeam "
			+ "left join fetch game.group "
			+ "where round.season.id = :seasonId and round.index = :gameListIndex";

	public RoundDaoHibernate() {
		super(GameList.class);
	}

	@Override
	public List<GameList> findRounds(Season season) {
		@SuppressWarnings("unchecked")
		List<GameList> objects = getSessionFactory().getCurrentSession()
				.createQuery(QUERY_GAMELIST_BY_SEASON)
				.setParameter("seasonId", season.getId()).list();
		return objects;
	}

	@Override
	public GameList findRound(Season season, int index) {
		@SuppressWarnings("unchecked")
		List<GameList> rounds = getSessionFactory().getCurrentSession()
				.createQuery(QUERY_GAMELIST_BY_SEASON_AND_INDEX)
				.setParameter("seasonId", season.getId())
				.setParameter("gameListIndex", Integer.valueOf(index)).list();

		GameList result = null;
		if (!rounds.isEmpty()) {
			result = (GameList) rounds.get(0);
		}
		return result;
	}

	@Override
	public GameList findAllRoundObjects(Season season, int index) {
		@SuppressWarnings("unchecked")
		List<GameList> rounds = getSessionFactory().getCurrentSession()
				.createQuery(QUERY_ALL_ROUND_OBJECTS)
				.setParameter("seasonId", season.getId())
				.setParameter("gameListIndex", Integer.valueOf(index)).list();

		// TODO
		// The source on
		// http://stackoverflow.com/questions/592825/jpa-please-help-understanding-join-fetch
		// says:
		// 'It os ok to get so much objects.'
		//
		// if (objects.size() > 1) {
		// throw new IllegalStateException("Too many result objects.");
		// }

		return ((GameList) rounds.get(0));
	}

	@Override
	public Long findNextTippRound(Season season, DateTime date) {
		String sql = "select min(t.bo_datetime) datetime, t.id next_round_id from "
				+ "(select * from bo_gamelist where bo_season_ref = :season_id and bo_datetime >= :date) as t";
		SQLQuery query = getSessionFactory().getCurrentSession()
				.createSQLQuery(sql);
		query.setParameter("season_id", season.getId());
		query.setDate("date", date.toDate());
		query.addScalar("datetime");
		query.addScalar("next_round_id");

		Object[] uniqueResult = (Object[]) query.uniqueResult();
		Long roundId = null;
		if (uniqueResult != null && uniqueResult[1] != null) {
			roundId = ((BigInteger) uniqueResult[1]).longValue();
		}

		return roundId;
	}

}
