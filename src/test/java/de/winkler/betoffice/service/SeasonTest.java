/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2022 by Andre Winkler. All rights reserved.
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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.betoffice.database.data.DatabaseTestData.DataLoader;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.Team;
import de.winkler.betoffice.storage.enums.SeasonType;
import de.winkler.betoffice.test.DummyTeams;
import de.winkler.betoffice.test.ScenarioBuilder;

/**
 * Testklasse für das Storage-Objekt Season.
 *
 * @author Andre Winkler
 */
public class SeasonTest extends AbstractServiceTest {

    @Autowired
    private ScenarioBuilder sceneBuilder;

    @Autowired
    private SeasonManagerService seasonManagerService;

    @Autowired
    protected DataSource dataSource;

    private DatabaseSetUpAndTearDown dsuatd;

    @BeforeEach
    public void setUp() throws Exception {
        dsuatd = new DatabaseSetUpAndTearDown(dataSource);
        dsuatd.setUp(DataLoader.EMPTY);
        createData();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        dsuatd.tearDown();
    }

    @Test
    public void testSeasonProperties() {
        Season season = seasonManagerService.findSeasonByName("Bundesliga", "1994/1995").orElseThrow();
        assertThat(season.getReference().getName()).isEqualTo("Bundesliga");
        assertThat(season.getReference().getYear()).isEqualTo("1994/1995");
        assertThat(season.getMode()).isEqualTo(SeasonType.LEAGUE);
    }

    @Test
    public void testGetGamesOfDay() {
        Season season = new Season();
        assertThrows(IndexOutOfBoundsException.class, () -> {
            season.getGamesOfDay(0);
        });

        assertThrows(IndexOutOfBoundsException.class, () -> {
            season.getGamesOfDay(-1);
        });

        assertThrows(IndexOutOfBoundsException.class, () -> {
            season.getGamesOfDay(season.toGameList().size());
        });

        assertThat(season.toGameList()).hasSize(0);
    }

    @Test
    public void testTeamSelection() {
        assertThat(sceneBuilder.getRwe()).isNotNull();
        assertThat(sceneBuilder.getS04());
        assertThat(sceneBuilder.getErsteBundesliga().getTeams()).isNotNull();
        assertThat(sceneBuilder.getZweiteBundesliga().getTeams()).isNotNull();

        Team bochum = sceneBuilder.getTeams().teams()[DummyTeams.BOCHUM];
        Team rwe = sceneBuilder.getTeams().teams()[DummyTeams.RWE];
        Team bvb = sceneBuilder.getTeams().teams()[DummyTeams.BVB];

        List<Team> teamsErsteBundesliga = seasonManagerService.findTeams(sceneBuilder.getErsteBundesliga());
        assertThat(teamsErsteBundesliga).hasSize(10);
        assertThat(teamsErsteBundesliga).containsOnlyElementsOf(sceneBuilder.getTeams().toList());

        List<Team> teamsZweiteBundesliga = seasonManagerService.findTeams(sceneBuilder.getZweiteBundesliga());
        assertThat(teamsZweiteBundesliga).hasSize(3);
        assertThat(teamsZweiteBundesliga).contains(bochum, rwe, bvb);

        seasonManagerService.removeTeam(sceneBuilder.getSeason(), sceneBuilder.getZweiteBundesliga().getGroupType(),
                rwe);

        teamsZweiteBundesliga = seasonManagerService.findTeams(sceneBuilder.getZweiteBundesliga());
        assertThat(teamsZweiteBundesliga).hasSize(2);
        assertThat(teamsZweiteBundesliga).contains(bochum, bvb);
    }

    private void createData() throws Exception {
        sceneBuilder.initialize();
    }

}
