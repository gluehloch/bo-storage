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

package de.betoffice.storage.season.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.betoffice.storage.group.GroupTypeDto;
import de.betoffice.storage.group.entity.GroupTypeDtoMapper;
import de.betoffice.storage.group.entity.GroupTypeEntity;
import de.betoffice.storage.season.GameDto;
import de.betoffice.storage.season.GameResultDto;
import de.betoffice.storage.season.GameTippDto;
import de.betoffice.storage.season.RoundDto;
import de.betoffice.storage.season.SeasonDto;
import de.betoffice.storage.season.TeamResultDto;
import de.betoffice.storage.season.UserRankingDto;
import de.betoffice.storage.team.TeamDto;
import de.betoffice.storage.team.TeamResult;
import de.betoffice.storage.team.entity.TeamDtoMapper;
import de.betoffice.storage.team.entity.TeamEntity;
import de.betoffice.storage.tip.GameTippEntity;
import de.betoffice.storage.user.UserResult;
import de.betoffice.storage.user.entity.UserDtoMapper;

/**
 * Create JSON objects.
 *
 * @author Andre Winkler
 */
public class DtoBuilder {

    public static SeasonDto toJson(SeasonEntity  season) {
        return SeasonDtoMapper.map(season, new SeasonDto());
    }

    public static List<SeasonDto> toJsonWithSeasons(List<SeasonEntity> seasons) {
        return SeasonDtoMapper.map(seasons);
    }

    public static RoundDto toJson(GameListEntity gameList) {
        return RoundDtoMapper.map(gameList, new RoundDto());
    }

    public static List<RoundDto> toJsonWithGameList(List<GameListEntity> rounds) {
        return RoundDtoMapper.map(rounds);
    }

    public static RoundDto toJsonWithGames(GameListEntity gameList) {
        RoundDto roundJson = DtoBuilder.toJson(gameList);
        List<GameDto> gameJson = DtoBuilder.toJsonWithGames(gameList.unmodifiableList());
        roundJson.getGames().addAll(gameJson);
        return roundJson;
    }

    public static GroupTypeDto toJson(GroupTypeEntity groupType) {
        return GroupTypeDtoMapper.map(groupType, new GroupTypeDto());
    }

    public static List<GroupTypeDto> toJsonWithGroupTypes(List<GroupTypeEntity> groupTypes) {
        return GroupTypeDtoMapper.map(groupTypes);
    }

    public static TeamResultDto toJson(TeamResult teamResult) {
        return TeamResultDtoMapper.map(teamResult, new TeamResultDto());
    }

    public static TeamDto toJson(TeamEntity team) {
        return TeamDtoMapper.map(team, new TeamDto());
    }

    public static List<TeamDto> toJsonWithTeams(List<TeamEntity> teams) {
        return TeamDtoMapper.map(teams);
    }

    public static UserRankingDto toJson(UserResult userResult) {
        return UserDtoMapper.map(userResult, new UserRankingDto());
    }

    public static GameResultDto toJson(GameResult gameResult) {
        return GameResultDtoMapper.map(gameResult, new GameResultDto());
    }

    public static GameTippDto toJson(GameTippEntity tipp) {
        return GameTippJsonMapper.map(tipp, new GameTippDto());
    }

    public static GameDto toJson(GameEntity game) {
        GameDto gameJson = GameDtoMapper.map(game, new GameDto());
        return gameJson;
    }

    public static GameDto toGameWithGoalsJson(GameEntity game) {
        GameDto gameJson = GameDtoMapper.map(game, new GameDto());
        return gameJson;
    }

    public static List<GameDto> toJsonWithGames(List<GameEntity> games) {
        List<GameDto> gameJsons = new ArrayList<>();
        for (GameEntity game : games) {
            gameJsons.add(DtoBuilder.toJson(game));
        }
        return gameJsons;
    }

    public static List<GameDto> toJsonWithGamesAndTipps(List<GameEntity> games, List<GameTippEntity> tipps) {
        List<GameDto> gameJsons = games.stream().map(game -> DtoBuilder.toJson(game)).collect(Collectors.toList());

        gameJsons.stream().forEach(gameJson -> {
            tipps.stream().filter(t -> {
                if (t.getGame().getId() != null && gameJson.getId() != null) {
                    return t.getGame().getId().equals(gameJson.getId());
                } else {
                    return false;
                }
            }).forEach((tipp) -> {
                GameTippDto tippJson = toJson(tipp);
                gameJson.addTipp(tippJson);
            });
        });

        return gameJsons;
    }

    public static List<GameDto> toJsonWithGamesAndTipps(List<GameEntity> games, Set<GameTippEntity> tipps) {
        List<GameDto> gameJsons = new ArrayList<>();
        for (GameEntity game : games) {
            GameDto gameJson = DtoBuilder.toJson(game);
            gameJsons.add(gameJson);
            for (GameTippEntity tipp : tipps) {
                GameTippDto tippJson = toJson(tipp);
                gameJson.addTipp(tippJson);
            }
        }
        return gameJsons;
    }

    public static List<GameTippDto> toJsonWithGameTipp(List<GameTippEntity> tipps) {
        List<GameTippDto> gameJsons = new ArrayList<>();
        for (GameTippEntity tipp : tipps) {
            GameTippDto gameTippJson = toJson(tipp);
            gameJsons.add(gameTippJson);
        }
        return gameJsons;
    }

}
