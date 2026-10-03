/*
 * ============================================================================
 * Project betoffice-jweb Copyright (c) 2022 by Andre Winkler. All rights
 * reserved.
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

package de.betoffice.storage.season.entity;

import java.util.List;

import de.betoffice.storage.season.SeasonDto;
import de.betoffice.storage.season.SeasonType;
import de.betoffice.storage.team.TeamType;

/**
 * Mapping for {@link Season} to {@link SeasonDto}.
 * 
 * @author Andre Winkler
 */
public class SeasonDtoMapper {

    public static SeasonDto map(SeasonEntity season, SeasonDto seasonJson) {
        seasonJson.setId(season.getId());
        seasonJson.setName(season.getReference().getName());
        seasonJson.setYear(season.getReference().getYear());
        seasonJson.setSeasonType(season.getMode().toString());
        seasonJson.setTeamType(season.getTeamType().toString());

        if (season.getChampionshipConfiguration() != null) {
            seasonJson.setOpenligaLeagueSeason(
                    season.getChampionshipConfiguration()
                            .getOpenligaLeagueSeason());
            seasonJson.setOpenligaLeagueShortcut(season
                    .getChampionshipConfiguration()
                    .getOpenligaLeagueShortcut());
        }
        return seasonJson;
    }

    public static List<SeasonDto> map(List<SeasonEntity> seasons) {
        return seasons.stream().map(SeasonDtoMapper::map).toList();
    }
    
    private static SeasonDto map(SeasonEntity season) {
    	return map(season, new SeasonDto());
    }

    public static SeasonEntity reverse(SeasonDto seasonJson, SeasonEntity season) {
        season.setMode(SeasonType.valueOf(seasonJson.getSeasonType()));
        season.setTeamType(TeamType.valueOf(seasonJson.getTeamType()));
        season.setReference(SeasonReference.of(seasonJson.getYear(), seasonJson.getName()));
        season.getChampionshipConfiguration().setOpenligaLeagueSeason(seasonJson.getOpenligaLeagueSeason());
        season.getChampionshipConfiguration().setOpenligaLeagueShortcut(seasonJson.getOpenligaLeagueShortcut());
        return season;
    }

}
