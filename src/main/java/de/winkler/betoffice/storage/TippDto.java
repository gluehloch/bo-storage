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
 */

package de.winkler.betoffice.storage;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds the tipp data.
 *
 * @author by Andre Winkler
 */
public class TippDto {

    private ZonedDateTime submitTime;

    private long roundId;
    private String nickname;
    private String token;

    private List<GameTippDto> gameTipps = new ArrayList<>();

    /**
     * @return the submitTime
     */
    public ZonedDateTime getSubmitTime() {
        return submitTime;
    }

    /**
     * @param submitTime
     *            the submitTime to set
     */
    public void setSubmitTime(ZonedDateTime submitTime) {
        this.submitTime = submitTime;
    }

    /**
     * @return the roundId
     */
    public long getRoundId() {
        return roundId;
    }

    /**
     * @param roundId
     *            the roundId to set
     */
    public void setRoundId(long roundId) {
        this.roundId = roundId;
    }

    /**
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname
     *            the nickname to set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token
     *            the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Add a game tipp
     * 
     * @param tipp
     *            a tipp
     */
    public void addGameTipp(GameTippDto tipp) {
        gameTipps.add(tipp);
    }

    /**
     * Get all game tipps
     * 
     * @return all game tipps
     */
    public List<GameTippDto> getGameTipps() {
        return gameTipps;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TippDto [submitTime=" + submitTime + ", roundId=" + roundId
                + ", nickname=" + nickname + ", token=" + token + ", gameTipps="
                + gameTipps + "]";
    }
    
    public GameTippDto addTipp(long gameId, int homeGoals, int guestGoals) {
        GameTippDto dto = new GameTippDto();
        dto.setGameId(gameId);
        dto.setHomeGoals(homeGoals);
        dto.setGuestGoals(guestGoals);
        gameTipps.add(dto);
        return dto;
    }

    public static class GameTippDto {
        private long gameId;
        private int homeGoals;
        private int guestGoals;

        /**
         * @return the gameId
         */
        public long getGameId() {
            return gameId;
        }

        /**
         * @param gameId
         *            the gameId to set
         */
        public void setGameId(long gameId) {
            this.gameId = gameId;
        }

        /**
         * @return the homeGoals
         */
        public int getHomeGoals() {
            return homeGoals;
        }

        /**
         * @param homeGoals
         *            the homeGoals to set
         */
        public void setHomeGoals(int homeGoals) {
            this.homeGoals = homeGoals;
        }

        /**
         * @return the guestGoals
         */
        public int getGuestGoals() {
            return guestGoals;
        }

        /**
         * @param guestGoals
         *            the guestGoals to set
         */
        public void setGuestGoals(int guestGoals) {
            this.guestGoals = guestGoals;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "GameTippDto [gameId=" + gameId + ", homeGoals=" + homeGoals
                    + ", guestGoals=" + guestGoals + "]";
        }
    }

}
