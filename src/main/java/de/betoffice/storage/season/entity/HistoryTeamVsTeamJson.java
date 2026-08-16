/*
 * ============================================================================
 * Project betoffice-jweb
 * Copyright (c) 2000-2017 by Andre Winkler. All rights reserved.
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

package de.betoffice.storage.season.entity;

import java.util.ArrayList;
import java.util.List;

import de.betoffice.web.json.TeamVsTeamJson;

/**
 * Holds the history of some matches of two teams.
 *
 * @author Andre Winkler
 */
public class HistoryTeamVsTeamJson {

    private List<TeamVsTeamDto> games = new ArrayList<>();

    public List<TeamVsTeamDto> getGames() {
        return games;
    }

    public void addGame(TeamVsTeamDto teamVsTeamJson) {
        games.add(teamVsTeamJson);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("HistoryTeamVsTeamJson [");
        sb.append("]");
        for (TeamVsTeamDto match : games) {
            sb.append("(").append(match).append(")");
        }
        return sb.toString();
    }

}
