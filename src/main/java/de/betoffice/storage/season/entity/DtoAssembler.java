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
 */

package de.betoffice.storage.season.entity;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import de.betoffice.storage.season.GameDto;
import de.betoffice.storage.season.GameResultDto;
import de.betoffice.storage.season.GameTippDto;
import de.betoffice.storage.season.RoundDto;
import de.betoffice.storage.season.SeasonDto;
import de.betoffice.storage.tip.GameTippEntity;

/**
 * Assembles JSON objects with other JSON objects.
 * 
 * @author Andre Winkler
 */
public class DtoAssembler {

    public static class SeasonAssembler {
        private SeasonEntity season;
        private List<GameListEntity> rounds;
        private GameListEntity currentRound;

        private SeasonAssembler(SeasonEntity _season) {
            season = _season;
        }

        public SeasonAssembler rounds() {
            rounds = season.toGameList();
            return this;
        }

        public SeasonAssembler rounds(List<GameListEntity> _rounds) {
            rounds = _rounds;
            return this;
        }

        public SeasonAssembler rounds(Predicate<GameListEntity> filter) {
            rounds = season.toGameList(filter);
            return this;
        }

        public SeasonAssembler currentRound(GameListEntity _currentRound) {
            currentRound = _currentRound;
            return this;
        }

        public SeasonAssembler currentRound(Optional<GameListEntity> _currentRound) {
            return currentRound(_currentRound.orElse(null));
        }

        public SeasonDto assemble() {
            SeasonDto seasonJson = DtoBuilder.toJson(season);
            if (rounds == null || rounds.isEmpty()) {
                seasonJson.getRounds().clear();
            } else {
                List<RoundDto> gameListJson = DtoBuilder.toJsonWithGameList(rounds);
                seasonJson.getRounds().clear();
                seasonJson.getRounds().addAll(gameListJson);
            }

            if (currentRound != null) {
                seasonJson.setCurrentRoundId(currentRound.getId());
            }

            return seasonJson;
        }
    }

    public static class RoundAssembler {
        private GameListEntity round;
        private List<GameEntity> games;
        private List<GameTippEntity> tipps;
        private boolean lastRound = false;

        private boolean hasToAddTipp = false;
        private boolean hasToAddEmptyTipp = false;

        private RoundAssembler(GameListEntity _round) {
            round = _round;
        }

        public RoundAssembler games(Predicate<GameEntity> filter) {
            games = round.toList(filter);
            return this;
        }

        public RoundAssembler games(List<GameEntity> _games) {
            games = _games;
            return this;
        }

        public RoundAssembler games() {
            games = round.unmodifiableList();
            return this;
        }

        public RoundAssembler tipps() {
            hasToAddTipp = true;
            return this;
        }

        public RoundAssembler tipps(List<GameTippEntity> _tipps) {
            tipps = _tipps;
            return this;
        }

        public RoundAssembler emptyTipp() {
            hasToAddEmptyTipp = true;
            return this;
        }

        public RoundAssembler lastRound(boolean _lastRound) {
            lastRound = _lastRound;
            return this;
        }

        public RoundDto assemble() {
            RoundDto roundJson = DtoBuilder.toJson(round);
            if (games == null || games.isEmpty()) {
                roundJson.getGames().clear();
            } else {
                List<GameDto> gameJsons = null;
                if (hasToAddEmptyTipp) {
                    gameJsons = DtoBuilder.toJsonWithGames(games);
                    for (GameDto gj : gameJsons) {
                        GameTippDto gameTippJson = new GameTippDto();
                        gameTippJson.setTipp(new GameResultDto());
                        gj.addTipp(gameTippJson);
                    }
                } else if (tipps != null && !tipps.isEmpty()) {
                    gameJsons = DtoBuilder.toJsonWithGamesAndTipps(games, tipps);
                } else if (hasToAddTipp) {
                    gameJsons = DtoBuilder.toJsonWithGamesAndTipps(games, tipps);
                } else {
                    gameJsons = DtoBuilder.toJsonWithGames(games);
                }

                roundJson.getGames().clear();
                roundJson.getGames().addAll(gameJsons);
            }

            roundJson.setLastRound(lastRound);
            roundJson.setTippable(!isFinished(roundJson));

            return roundJson;
        }

        private boolean isFinished(RoundDto round) {
            boolean finished = true;
            if (games == null || games.isEmpty()) {
                // No games? Finished or what? I guess, it is finished.
                finished = false;
            } else {
                for (GameDto game : round.getGames()) {
                    finished = finished && game.isFinished();
                }
            }
            return finished;
        }
    }

    public SeasonAssembler build(SeasonEntity season) {
        return new SeasonAssembler(season);
    }

    public RoundAssembler build(GameListEntity round) {
        return new RoundAssembler(round);
    }

}
