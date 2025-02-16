/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2022 by Andre Winkler. All rights reserved.
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

package de.winkler.betoffice.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.winkler.betoffice.storage.Nickname;
import de.winkler.betoffice.storage.User;

/**
 * DAO Klasse für den Zugriff auf {@link de.winkler.betoffice.storage.User}.
 *
 * @author by Andre Winkler
 */
public interface UserDao extends CommonDao<User> {

    /**
     * Find all users. But you should better use {@link #findAll(String, Pageable)}.
     *
     * @return all uses
     */
    List<User> findAll();

    /**
     * Find all users.
     * 
     * @param  nicknameFilter nickname filter
     * @param  pageable       paging params
     * @return                all users
     */
    Page<User> findAll(String nicknameFilter, Pageable pageable);

    /**
     * Liefert einen Teilnehmer mit gesuchten Nickname.
     *
     * @param  nickname Der gesuchte Nickname.
     * @return          Ein Teilnehmer.
     */
    Optional<User> findByNickname(Nickname nickname);

    /**
     * Liefert einen Teilnehmer mit dem gesuchten ChangeToken.
     * 
     * @param  changeToken Das Change-Token
     * @return             Ein Teilnehmer
     */
    Optional<User> findByChangeToken(String changeToken);

}
