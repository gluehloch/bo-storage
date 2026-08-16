package de.betoffice.storage.season.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.betoffice.storage.group.entity.GroupTeamDto;
import de.betoffice.storage.season.SeasonDto;

public class SeasonGroupTeamJson implements Serializable {

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
