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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.betoffice.storage.group.entity.GroupTeamDto;

public class SeasonGroupTeamDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private SeasonDto seasonJson;
	private List<GroupTeamDto> groupTeams = new ArrayList<GroupTeamDto>();

	public SeasonDto getSeasonJson() {
		return seasonJson;
	}

	public void setSeasonJson(SeasonDto seasonJson) {
		this.seasonJson = seasonJson;
	}

	public List<GroupTeamDto> getGroupTeams() {
		return groupTeams;
	}

	public void setGroupTeams(List<GroupTeamDto> groupTeams) {
		this.groupTeams = groupTeams;
	}

}
