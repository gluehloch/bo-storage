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
package de.winkler.betoffice.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.winkler.betoffice.storage.CommunityReference;
import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.GameResult;
import de.winkler.betoffice.storage.GameTipp;
import de.winkler.betoffice.storage.Group;
import de.winkler.betoffice.storage.GroupType;
import de.winkler.betoffice.storage.Nickname;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.SeasonReference;
import de.winkler.betoffice.storage.Team;
import de.winkler.betoffice.storage.TippDto;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.enums.SeasonType;
import de.winkler.betoffice.storage.enums.TippStatusType;
import de.winkler.betoffice.test.DateTimeDummyProducer;

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

    private GameList round;
    private Game luebeckRwe;
    private Game rweLuebeck;
    
    @Test
    @Transactional
    void sendTippAfterKickOff() {
        GameList roundGames = seasonManagerService.findRoundGames(round.getId()).orElseThrow();
        assertThat(roundGames.size()).isEqualTo(2);

        User user = communityService.findUser(nicknameUserA).orElseThrow();        
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
        List<GameTipp> tipps = tippService.findTipps(round, user);
        assertThat(tipps).hasSize(1);
        assertThat(tipps.get(0).getUser()).isEqualTo(user);
        assertThat(tipps.get(0).getTipp().getHomeGoals()).isEqualTo(1);
        assertThat(tipps.get(0).getTipp().getGuestGoals()).isEqualTo(0);
    }

    @BeforeEach
    void setup() {
        Team luebeck = Team.TeamBuilder
                .team("Vfb Lübeck")
                .longName("Vfb Lübeck")
                .logo("luebeck.gif")
                .build();
        masterDataManagerService.createTeam(luebeck);

        Team rwe = Team.TeamBuilder
                .team("RWE")
                .longName("Rot-Weiss-Essen")
                .logo("rwe.gif")
                .build();
        masterDataManagerService.createTeam(rwe);

        Season season = new Season(seasonReference);
        season.setMode(SeasonType.LEAGUE);
        seasonManagerService.createSeason(season);

        GroupType buli1 = new GroupType();
        buli1.setName("1. Bundesliga");
        masterDataManagerService.createGroupType(buli1);

        season = seasonManagerService.addGroupType(season, buli1);
        Group group = seasonManagerService.findGroup(season, buli1);
        seasonManagerService.addTeam(season, buli1, rwe);
        seasonManagerService.addTeam(season, buli1, luebeck);

        round = seasonManagerService.addRound(season, DateTimeDummyProducer.DATE_1971_03_24, buli1);
        luebeckRwe = seasonManagerService.addMatch(round, DateTimeDummyProducer.DATE_1971_03_24, group, luebeck, rwe);
        rweLuebeck = seasonManagerService.addMatch(round, DateTimeDummyProducer.DATE_1971_03_24, group, rwe, luebeck);

        User userA = new User();
        userA.setNickname(nicknameUserA);
        communityService.createUser(userA);
        
        communityService.create(communityReference, seasonReference, "TDKB Test Community", nicknameUserA);
        communityService.addMember(communityReference, nicknameUserA);
    }

}
