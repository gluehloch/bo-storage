/*
 * $Id: UserSeasonDaoHibernate.java 3782 2013-07-27 08:44:32Z andrewinkler $
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2012 by Andre Winkler. All rights reserved.
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

import de.winkler.betoffice.dao.UserSeasonDao;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.UserSeason;

/**
 * Implementierung des {@link UserSeasonDao}.
 * 
 * @author by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version $LastChangedRevision: 3782 $ $LastChangedDate: 2012-07-24 06:07:32
 *          +0200 (Tue, 24 Jul 2012) $
 */
@Repository("userSasonDao")
public class UserSeasonDaoHibernate extends AbstractCommonDao<UserSeason>
        implements UserSeasonDao {

    /** Sucht nach allen Teilnehmern einer Meisterschaft. */
    private static final String QUERY_USERS_BY_SEASON = "select user "
            + "from User user, UserSeason us "
            + "where us.season.id = :seasonId and us.user.id = user.id "
            + "order by user.nickName";

    public UserSeasonDaoHibernate() {
        super(UserSeason.class);
    }

    @Override
    public List<User> findUsers(final Season season) {
        @SuppressWarnings("unchecked")
        List<User> users = getSessionFactory().getCurrentSession()
                .createQuery(QUERY_USERS_BY_SEASON)
                .setParameter("seasonId", season.getId()).list();
        return users;
    }

}
