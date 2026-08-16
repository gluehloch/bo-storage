/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2026 by Andre Winkler. All
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

package de.betoffice.storage.season;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import de.betoffice.storage.AbstractOpenligaid;
import de.betoffice.storage.JsonDateTimeFormat;
import de.betoffice.storage.group.GroupTypeDto;
import de.betoffice.storage.team.TeamDto;

/**
 * The game data as JSON.
 * 
 * @author Andre Winkler
 */
@JsonInclude(Include.NON_NULL)
public class GameDto extends AbstractOpenligaid {

    private int index;
    private Long roundId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JsonDateTimeFormat.DATETIME_PATTERN, timezone = JsonDateTimeFormat.TIMZONE)
    private ZonedDateTime dateTime;
    private TeamDto homeTeam;
    private TeamDto guestTeam;
    private GroupTypeDto groupType;
    private GameResultDto halfTimeResult;
    private GameResultDto result;
    private GameResultDto overtimeResult;
    private GameResultDto penaltyResult;
    private boolean finished;
    private boolean ko;

    private List<GoalDto> goals = new ArrayList<>();
    private List<GameTippDto> tipps = new ArrayList<>();

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


    public Long getRoundId() {
        return roundId;
    }

    public void setRoundId(Long roundId) {
        this.roundId = roundId;
    }

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public TeamDto getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(TeamDto homeTeam) {
        this.homeTeam = homeTeam;
    }

    public TeamDto getGuestTeam() {
        return guestTeam;
    }

    public GroupTypeDto getGroupType() {
        return groupType;
    }

    public void setGroupType(GroupTypeDto groupType) {
        this.groupType = groupType;
    }

    public void setGuestTeam(TeamDto guestTeam) {
        this.guestTeam = guestTeam;
    }

    public GameResultDto getHalfTimeResult() {
        return halfTimeResult;
    }

    public void setHalfTimeResult(GameResultDto halfTimeResult) {
        this.halfTimeResult = halfTimeResult;
    }

    public GameResultDto getResult() {
        return result;
    }

    public void setResult(GameResultDto result) {
        this.result = result;
    }

    public GameResultDto getOvertimeResult() {
        return overtimeResult;
    }

    public void setOvertimeResult(GameResultDto overtimeResult) {
        this.overtimeResult = overtimeResult;
    }

    public GameResultDto getPenaltyResult() {
        return penaltyResult;
    }

    public void setPenaltyResult(GameResultDto penaltyResult) {
        this.penaltyResult = penaltyResult;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isKo() {
        return ko;
    }

    public void setKo(boolean ko) {
        this.ko = ko;
    }

    public List<GameTippDto> getTipps() {
        return tipps;
    }

    public void setTipps(List<GameTippDto> tipps) {
        this.tipps.clear();
        this.tipps.addAll(tipps);
    }

    public void addTipp(GameTippDto tipp) {
        this.tipps.add(tipp);
    }

    public List<GoalDto> getGoals() {
        return goals;
    }

    public void setGoals(List<GoalDto> goals) {
        this.goals = goals;
    }
    
    @Override
    public String toString() {
        return "GameDto [index=" + index + ", roundId=" + roundId
                + ", dateTime=" + dateTime + ", homeTeam=" + homeTeam
                + ", guestTeam=" + guestTeam + ", halfTimeResult="
                + halfTimeResult + ", result=" + result + ", overtimeResult="
                + overtimeResult + ", penaltyResult=" + penaltyResult
                + ", finished=" + finished + ", ko=" + ko + ", tipps=" + tipps
                + "]";
    }

}
