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

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import de.betoffice.storage.season.entity.GameListEntity;
import de.betoffice.storage.season.entity.SeasonEntity;

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
            SeasonDto seasonJson = JsonBuilder.toJson(season);
            if (rounds == null || rounds.isEmpty()) {
                seasonJson.getRounds().clear();
            } else {
                List<RoundDto> gameListJson = JsonBuilder
                        .toJsonWithGameList(rounds);
                seasonJson.getRounds().clear();
                seasonJson.getRounds().addAll(gameListJson);
            }

            if (currentRound != null) {
                seasonJson.setCurrentRoundId(currentRound.getId());
            }

            return seasonJson;
        }
    }

}
