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

package de.betoffice.storage.season;

import java.util.ArrayList;
import java.util.List;

/**
 * User ranking
 * 
 * @author Andre Winkler
 */
public class UserRankingTableDto {

    private SeasonDto season;
    private RoundDto round;

    private final List<UserRankingDto> users = new ArrayList<>();

    public void addUser(UserRankingDto user) {
        users.add(user);
    }

    /**
     * @return the seasonJson
     */
    public SeasonDto getSeason() {
        return season;
    }

    /**
     * @param season
     *            the seasonJson to set
     */
    public void setSeason(SeasonDto season) {
        this.season = season;
    }

    public List<UserRankingDto> getUsers() {
        return users;
    }

    /**
     * @return the roundJson
     */
    public RoundDto getRound() {
        return round;
    }

    /**
     * @param round
     *            the roundJson to set
     */
    public void setRound(RoundDto round) {
        this.round = round;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "UserTableJson [season=" + season + ", round=" + round
                + ", users=" + users + "]";
    }

}
