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

import java.util.List;
import java.util.Optional;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import de.winkler.betoffice.dao.UserSeasonDao;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.UserSeason;

/**
 * Implementierung des {@link UserSeasonDao}.
 * 
 * @author Andre Winkler
 */
@Repository("userSasonDao")
public class UserSeasonDaoHibernate extends AbstractCommonDao<UserSeason>
        implements UserSeasonDao {

    /** Sucht nach allen Teilnehmern einer Meisterschaft. */
    private static final String QUERY_USERS_BY_SEASON = "select user "
            + "from User user, UserSeason us "
            + "where us.season.id = :seasonId and us.user.id = user.id "
            + "order by user.nickname";

    public UserSeasonDaoHibernate() {
        super(UserSeason.class);
    }

    @Override
    public List<User> findUsers(Season season) {
        List<User> users = getSessionFactory().getCurrentSession()
                .createQuery(QUERY_USERS_BY_SEASON, User.class)
                .setParameter("seasonId", season.getId()).getResultList();
        return users;
    }

    @Override
    public Optional<UserSeason> findUserSeason(Season season, User user) {
        Query<UserSeason> query = getSessionFactory().getCurrentSession()
                .createQuery(
                        "from UserSeason us where us.season.id = :seasonId and us.user.id = :userId")
                .setParameter("seasonId", season.getId())
                .setParameter("userId", user.getId());
        return singleResult(query);
    }

}
