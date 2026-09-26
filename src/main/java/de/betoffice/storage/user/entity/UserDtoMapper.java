/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2013-2026 by Andre Winkler. All
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

package de.betoffice.storage.user.entity;

import java.util.List;
import java.util.stream.Collectors;

import de.betoffice.storage.season.UserRankingDto;
import de.betoffice.storage.user.UserResult;

/**
 * Map {@link User} to {@link UserRankingDto}.
 * 
 * @author Andre Winkler
 */
public class UserDtoMapper {

    public static UserRankingDto map(UserResult userResult, UserRankingDto userJson) {
        userJson.setId(userResult.getUser().getId());
        userJson.setNickname(userResult.getUser().getNickname().value());
        userJson.setWin(userResult.getUserWin());
        userJson.setToto(userResult.getUserTotoWin());
        userJson.setTicket(userResult.getTicket());
        userJson.setPoints(userResult.getPoints());
        userJson.setPosition(userResult.getTabPos());
        return userJson;
    }

    public static List<UserRankingDto> map(List<UserResult> userResults) {
        return userResults.stream().map((userResult) -> {
            UserRankingDto json = new UserRankingDto();
            json = map(userResult, json);
            return json;
        }).collect(Collectors.toList());
    }

}
