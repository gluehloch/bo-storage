/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2020 by Andre Winkler. All rights reserved.
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
package de.betoffice.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.betoffice.service.request.CommunityCreateCommand;
import de.betoffice.storage.community.entity.CommunityReference;
import de.betoffice.storage.group.entity.GroupTypeEntity;
import de.betoffice.storage.season.SeasonType;
import de.betoffice.storage.season.entity.GameEntity;
import de.betoffice.storage.season.entity.GameListEntity;
import de.betoffice.storage.season.entity.GameResult;
import de.betoffice.storage.season.entity.GroupEntity;
import de.betoffice.storage.season.entity.SeasonEntity;
import de.betoffice.storage.season.entity.SeasonReference;
import de.betoffice.storage.team.entity.TeamEntity;
import de.betoffice.storage.tip.GameTippEntity;
import de.betoffice.storage.tip.TippDto;
import de.betoffice.storage.tip.TippStatusType;
import de.betoffice.storage.user.entity.Nickname;
import de.betoffice.storage.user.entity.UserEntity;
import de.betoffice.test.DateTimeDummyProducer;

public class IncompleteTippTest extends AbstractServiceTest {

    @Autowired
    private SeasonManagerService seasonManagerService;

    @Autowired
    private CommunityService communityService;
    
    @Autowired
    private TippService tippService;

    @Autowired
    private MasterDataManagerService masterDataManagerService;

    private static final CommunityReference communityReference = CommunityReference.of("TDKB Test");
    private static final SeasonReference seasonReference = SeasonReference.of("1999/2000", "Bundesliga");
    private static final Nickname nicknameUserA = Nickname.of("User A");

    private GameListEntity round;
    private GameEntity luebeckRwe;
    private GameEntity rweLuebeck;
    
    @Test
    @Transactional
    void sendTippAfterKickOff() {
        GameListEntity roundGames = seasonManagerService.findRoundGames(round.getId()).orElseThrow();
        assertThat(roundGames.size()).isEqualTo(2);

        UserEntity user = communityService.findUser(nicknameUserA).orElseThrow();        
        tippService.createOrUpdateTipp("1", luebeckRwe, user, GameResult.of(1, 0), TippStatusType.USER);
        
        //
        // Tipp für Lübeck-RWE vorhanden: 1:0. Es wird versucht den Tipp nachträglich zu ändern.
        //
        TippDto tippDto = new TippDto();
        tippDto.setNickname("User A");
        tippDto.setRoundId(round.getId());
        tippDto.addGameTipp(tippDto.addTipp(luebeckRwe.getId(), 2, 0));
        tippDto.setSubmitTime(DateTimeDummyProducer.DATE_1971_03_24.plusDays(1));
        tippService.validateKickOffTimeAndAddTipp(tippDto);
        
        assertThat(tippService.findTipps(round.getId())).hasSize(1);
        List<GameTippEntity> tipps = tippService.findTipps(round, user);
        assertThat(tipps).hasSize(1);
        assertThat(tipps.get(0).getUser()).isEqualTo(user);
        assertThat(tipps.get(0).getTipp().getHomeGoals()).isEqualTo(1);
        assertThat(tipps.get(0).getTipp().getGuestGoals()).isEqualTo(0);
    }

    @BeforeEach
    void setup() {
        TeamEntity luebeck = TeamEntity.TeamBuilder
                .team("Vfb Lübeck")
                .longName("Vfb Lübeck")
                .logo("luebeck.gif")
                .build();
        masterDataManagerService.createTeam(luebeck);

        TeamEntity rwe = TeamEntity.TeamBuilder
                .team("RWE")
                .longName("Rot-Weiss-Essen")
                .logo("rwe.gif")
                .build();
        masterDataManagerService.createTeam(rwe);

        SeasonEntity season = new SeasonEntity(seasonReference);
        season.setMode(SeasonType.LEAGUE);
        seasonManagerService.createSeason(season);

        GroupTypeEntity buli1 = new GroupTypeEntity();
        buli1.setName("1. Bundesliga");
        masterDataManagerService.createGroupType(buli1);

        season = seasonManagerService.addGroupType(season, buli1);
        GroupEntity group = seasonManagerService.findGroup(season, buli1);
        seasonManagerService.addTeam(season, buli1, rwe);
        seasonManagerService.addTeam(season, buli1, luebeck);

        round = seasonManagerService.addRound(season, DateTimeDummyProducer.DATE_1971_03_24, buli1);
        luebeckRwe = seasonManagerService.addMatch(round, DateTimeDummyProducer.DATE_1971_03_24, group, luebeck, rwe);
        rweLuebeck = seasonManagerService.addMatch(round, DateTimeDummyProducer.DATE_1971_03_24, group, rwe, luebeck);

        UserEntity userA = new UserEntity();
        userA.setNickname(nicknameUserA);
        communityService.createUser(userA);
        
        communityService.create(new CommunityCreateCommand(communityReference, seasonReference, "TDKB Test Community", "2024", nicknameUserA));
        communityService.addMember(communityReference, nicknameUserA);
    }

}
