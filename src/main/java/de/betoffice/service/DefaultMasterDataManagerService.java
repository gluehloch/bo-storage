/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2024 by Andre Winkler. All rights reserved.
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.betoffice.storage.season.GroupTypeDao;
import de.betoffice.storage.season.LocationDao;
import de.betoffice.storage.season.PlayerDao;
import de.betoffice.storage.season.SeasonDao;
import de.betoffice.storage.season.entity.GroupType;
import de.betoffice.storage.season.entity.Location;
import de.betoffice.storage.season.entity.Player;
import de.betoffice.storage.season.entity.Season;
import de.betoffice.storage.team.TeamAlias;
import de.betoffice.storage.team.TeamAliasDao;
import de.betoffice.storage.team.TeamDao;
import de.betoffice.storage.team.TeamType;
import de.betoffice.storage.team.entity.Team;
import de.betoffice.validation.ValidationException;
import de.betoffice.validation.ValidationMessage;
import de.betoffice.validation.ValidationMessage.MessageType;

/**
 * Default-Implementierung der Stammdatenverwaltung.
 *
 * @author by Andre Winkler
 */
@Service("masterDataManagerService")
public class DefaultMasterDataManagerService extends AbstractManagerService
        implements MasterDataManagerService {

    @Autowired
    private SeasonDao seasonDao;

    @Autowired
    private GroupTypeDao groupTypeDao;

    @Autowired
    private TeamDao teamDao;

    @Autowired
    private TeamAliasDao teamAliasDao;

    @Autowired
    private LocationDao locationDao;

    @Autowired
    private PlayerDao playerDao;

    @Override
    @Transactional
    public void createSeason(final Season season) {
        seasonDao.persist(season);
    }

    @Override
    @Transactional
    public void updateSeason(final Season season) {
        seasonDao.update(season);
    }

    @Override
    @Transactional
    public void createGroupType(final GroupType groupType) {
        List<ValidationMessage> messages = new ArrayList<ValidationMessage>();

        if (StringUtils.isBlank(groupType.getName())) {
            messages.add(ValidationMessage.error(MessageType.GROUP_TYPE_NAME_IS_NOT_SET));
        }

        if (messages.isEmpty()) {
            groupTypeDao.persist(groupType);
        } else {
            throw new ValidationException(messages);
        }
    }

    @Override
    @Transactional
    public void createTeam(final Team team) {
        List<ValidationMessage> messages = new ArrayList<ValidationMessage>();

        if (StringUtils.isBlank(team.getName())) {
            messages.add(ValidationMessage.error(MessageType.TEAM_NAME_IS_NOT_SET));
        }

        if (messages.isEmpty()) {
            teamDao.persist(team);
        } else {
            throw new ValidationException(messages);
        }
    }

    @Override
    @Transactional
    public TeamAlias createTeamAlias(final Team team,
            final String teamAliasName) {
        List<ValidationMessage> messages = new ArrayList<ValidationMessage>();
        if (StringUtils.isBlank(teamAliasName)) {
            messages.add(ValidationMessage.error(MessageType.TEAM_ALIAS_NAME_IS_NOT_SET));
        }

        TeamAlias teamAlias = null;
        if (messages.isEmpty()) {
            teamAlias = new TeamAlias();
            teamAlias.setAliasName(teamAliasName);
            teamAlias.setTeam(team);
            teamAliasDao.persist(teamAlias);
        } else {
            throw new ValidationException(messages);
        }

        return teamAlias;
    }

    @Override
    @Transactional
    public void deleteGroupType(final GroupType groupType) {
        GroupType persistentGroupType = groupTypeDao.findById(groupType.getId());
        groupTypeDao.delete(persistentGroupType);
    }

    @Override
    @Transactional
    public void deleteTeam(final Team team) {
        Team persistentTeam = teamDao.findById(team.getId());
        teamDao.delete(persistentTeam);
    }

    @Override
    @Transactional
    public void deleteTeamAlias(final TeamAlias teamAlias) {
        TeamAlias persistentTeamAlias = teamAliasDao.findById(teamAlias.getId());
        teamAliasDao.delete(persistentTeamAlias);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupType> findAllGroupTypes() {
        return groupTypeDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Team> findAllTeams() {
        return teamDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Team> findTeams(TeamType teamType) {
        return teamDao.findTeams(teamType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Team> findTeams(Optional<TeamType> teamType, String filter) {
        return teamDao.findTeams(teamType, filter);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamAlias> findAllTeamAlias(Team team) {
        return teamAliasDao.findAliasNames(team);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GroupType> findGroupType(final String name) {
        return groupTypeDao.findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public GroupType findGroupType(long groupTypeId) {
        return (groupTypeDao.findById(groupTypeId));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Team> findTeam(final String name) {
        return teamDao.findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Team findTeamById(final long id) {
        return teamDao.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Team> findTeamByAlias(String aliasName) {
        return teamAliasDao.findByAliasName(aliasName);
    }

    @Override
    @Transactional
    public void updateGroupType(final GroupType groupType) {
        groupTypeDao.update(groupType);
    }

    @Override
    @Transactional
    public void updateTeam(final Team team) {
        teamDao.update(team);
    }

    @Override
    @Transactional
    public void updateTeamAlias(final TeamAlias teamAlias) {
        teamAliasDao.update(teamAlias);
    }

    @Override
    @Transactional(readOnly = false)
    public Optional<Team> findTeamByOpenligaid(long id) {
        return teamDao.findByOpenligaid(id);
    }

    @Override
    @Transactional
    public void createLocation(Location location) {
        locationDao.persist(location);
    }

    @Override
    @Transactional
    public void deleteLocation(Location location) {
        locationDao.delete(location);
    }

    @Override
    @Transactional
    public void updateLocation(Location location) {
        locationDao.update(location);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Location> findAllLocations() {
        return locationDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Location findLocation(long id) {
        return locationDao.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Location> findLocationByOpenligaid(long openligaid) {
        return locationDao.findByOpenligaid(openligaid);
    }

    @Override
    @Transactional
    public void createPlayer(Player player) {
        playerDao.persist(player);
    }

    @Override
    @Transactional
    public void deletePlayer(Player player) {
        playerDao.delete(player);
    }

    @Override
    @Transactional
    public void updatePlayer(Player player) {
        playerDao.update(player);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Player> findAllPlayers() {
        return playerDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Player findPlayer(long id) {
        return playerDao.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Player> findPlayerByOpenligaid(long openligaid) {
        return playerDao.findByOpenligaid(openligaid);
    }

}
