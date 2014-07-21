/*
 * $Id: TippMailParameter.java 3810 2013-09-02 17:40:15Z andrewinkler $
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

package de.winkler.betoffice.mail;

import org.apache.commons.lang.StringUtils;

/**
 * Parameter class.
 * 
 * @author  by Andre Winkler, $LastChangedBy: andrewinkler $
 * @version  $LastChangedRevision: 3810 $ $LastChangedDate: 2013-09-02 19:40:15 +0200 (Mon, 02 Sep 2013) $
 */
public class TippMailParameter {

    private String nickname;
    private String password;
    private int[] homeGoals;
    private int[] guestGoals;
    private String[] homeTeams;
    private String[] guestTeams;
    private int roundNr;
    private long championshipId;
    private String httpHost;
    private String userIp;
    private String httpUserAgent;

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return StringUtils.defaultString(password);
    }

    public int[] getHomeGoals() {
        return homeGoals;
    }

    public int[] getGuestGoals() {
        return guestGoals;
    }

    public String[] getHomeTeams() {
        return homeTeams;
    }

    public String[] getGuestTeams() {
        return guestTeams;
    }

    public int getRoundNr() {
        return roundNr;
    }

    public long getChampionshipId() {
        return championshipId;
    }

    public String getHttpHost() {
        return httpHost;
    }

    public String getUserIp() {
        return userIp;
    }

    public String getHttpUserAgent() {
        return httpUserAgent;
    }

    public TippMailParameter(String nickname, String password, int[] homeGoals,
            int[] guestGoals, String[] homeTeams, String[] guestTeams,
            int roundNr, long championshipId, String httpHost, String userIp,
            String httpUserAgent) {

        this.nickname = nickname;
        this.password = password;
        this.homeGoals = homeGoals;
        this.guestGoals = guestGoals;
        this.homeTeams = homeTeams;
        this.guestTeams = guestTeams;
        this.roundNr = roundNr;
        this.password = password;
        this.championshipId = championshipId;
        this.httpHost = httpHost;
        this.userIp = userIp;
        this.httpUserAgent = httpUserAgent;
    }

}