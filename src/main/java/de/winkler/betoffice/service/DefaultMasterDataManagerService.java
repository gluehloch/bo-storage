/*
 * ============================================================================
 * Project betoffice-storage
 * Copyright (c) 2000-2017 by Andre Winkler. All rights reserved.
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.winkler.betoffice.storage.GroupType;
import de.winkler.betoffice.storage.Location;
import de.winkler.betoffice.storage.Player;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.Team;
import de.winkler.betoffice.storage.TeamAlias;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.enums.TeamType;
import de.winkler.betoffice.validation.BetofficeValidationException;
import de.winkler.betoffice.validation.BetofficeValidationMessage;
import de.winkler.betoffice.validation.BetofficeValidationMessage.Severity;

/**
 * Default-Implementierung der Stammdatenverwaltung.
 *
 * @author by Andre Winkler
 */
@Service("masterDataManagerService")
public class DefaultMasterDataManagerService extends AbstractManagerService
        implements MasterDataManagerService {


    @Override
    @Transactional
    public void createSeason(final Season season) {
        getConfig().getSeasonDao().save(season);
    }

    @Override
    @Transactional
    public void updateSeason(final Season season) {
        getConfig().getSeasonDao().update(season);
    }

    @Override
    @Transactional
    public void createGroupType(final GroupType groupType) {
        List<BetofficeValidationMessage> messages = new ArrayList<BetofficeValidationMessage>();

        if (StringUtils.isBlank(groupType.getName())) {
            messages.add(new BetofficeValidationMessage(
                "Name ist nicht gesetzt.", "nickName", Severity.ERROR));
        }

        if (messages.isEmpty()) {
            getConfig().getGroupTypeDao().save(groupType);
        } else {
            throw new BetofficeValidationException(messages);
        }
    }

    @Override
    @Transactional
    public void createTeam(final Team team) {
        List<BetofficeValidationMessage> messages = new ArrayList<BetofficeValidationMessage>();

        if (StringUtils.isBlank(team.getName())) {
            messages.add(new BetofficeValidationMessage(
                "Name ist nicht gesetzt.", "nickName", Severity.ERROR));
        }

        if (messages.isEmpty()) {
            getConfig().getTeamDao().save(team);
        } else {
            throw new BetofficeValidationException(messages);
        }
    }

    @Override
    @Transactional
    public TeamAlias createTeamAlias(final Team team, final String teamAliasName) {
        List<BetofficeValidationMessage> messages = new ArrayList<BetofficeValidationMessage>();
        if (StringUtils.isBlank(teamAliasName)) {
            messages.add(new BetofficeValidationMessage(
                "Alias Name nicht gesetzt.", "aliasName", Severity.ERROR));
        }

        TeamAlias teamAlias = null;
        if (messages.isEmpty()) {
            teamAlias = new TeamAlias();
            teamAlias.setAliasName(teamAliasName);
            teamAlias.setTeam(team);
            getConfig().getTeamAliasDao().save(teamAlias);
        } else {
            throw new BetofficeValidationException(messages);
        }

        return teamAlias;
    }

    @Override
    @Transactional
    public User createUser(final User user) {
        List<BetofficeValidationMessage> messages = new ArrayList<BetofficeValidationMessage>();

        if (StringUtils.isBlank(user.getNickName())) {
            messages.add(new BetofficeValidationMessage(
                "Nickname ist nicht gesetzt.", "nickName", Severity.ERROR));
        }

        if (messages.isEmpty()) {
            getConfig().getUserDao().save(user);
        } else {
            throw new BetofficeValidationException(messages);
        }
        
        return user;
    }

    @Override
    @Transactional
    public void deleteGroupType(final GroupType groupType) {
        getConfig().getGroupTypeDao().delete(groupType);
    }

    @Override
    @Transactional
    public void deleteTeam(final Team team) {
        getConfig().getTeamDao().delete(team);
    }

    @Override
    @Transactional
    public void deleteUser(final User user) {
        getConfig().getUserDao().delete(user);
    }


    @Override
    @Transactional
    public void deleteTeamAlias(final TeamAlias teamAlias) {
        getConfig().getTeamAliasDao().delete(teamAlias);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupType> findAllGroupTypes() {
        return getConfig().getGroupTypeDao().findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findUserByNickname(final String nickname) {
        return getConfig().getUserDao().findByNickname(nickname);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Team> findAllTeams() {
        return getConfig().getTeamDao().findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Team> findTeams(TeamType teamType) {
        return getConfig().getTeamDao().findTeams(teamType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamAlias> findAllTeamAlias(Team team) {
        return getConfig().getTeamAliasDao().findAliasNames(team);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return getConfig().getUserDao().findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GroupType> findGroupType(final String name) {
        return getConfig().getGroupTypeDao().findByName(name);
    }

	@Override
    @Transactional(readOnly = true)
	public GroupType findGroupType(long groupTypeId) {
		return (getConfig().getGroupTypeDao().findById(groupTypeId));
	}

    @Override
    @Transactional(readOnly = true)
    public Optional<Team> findTeam(final String name) {
        return getConfig().getTeamDao().findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Team findTeamById(final long id) {
        return getConfig().getTeamDao().findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Team> findTeamByAlias(String aliasName) {
        return getConfig().getTeamAliasDao().findByAliasName(aliasName);
    }

    @Override
    @Transactional
    public void updateGroupType(final GroupType groupType) {
        getConfig().getGroupTypeDao().update(groupType);
    }

    @Override
    @Transactional
    public void updateTeam(final Team team) {
        getConfig().getTeamDao().update(team);
    }

    @Override
    @Transactional
    public void updateTeamAlias(final TeamAlias teamAlias) {
        getConfig().getTeamAliasDao().update(teamAlias);
    }
 
    @Override
    @Transactional
    public void updateUser(final User user) {
        getConfig().getUserDao().update(user);
    }
    
    @Override
    @Transactional
    public User findUser(long userId) {
        return getConfig().getUserDao().findById(userId);
    }

    @Override
    @Transactional(readOnly = false)
    public Optional<Team> findTeamByOpenligaid(long id) {
        return getConfig().getTeamDao().findByOpenligaid(id);
    }

    @Override
    @Transactional
    public void createLocation(Location location) {
        getConfig().getLocationDao().save(location);
    }

    @Override
    @Transactional
    public void deleteLocation(Location location) {
        getConfig().getLocationDao().delete(location);
    }

    @Override
    @Transactional
    public void updateLocation(Location location) {
        getConfig().getLocationDao().update(location);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Location> findAllLocations() {
        return getConfig().getLocationDao().findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Location findLocation(long id) {
        return getConfig().getLocationDao().findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Location> findLocationByOpenligaid(long openligaid) {
        return getConfig().getLocationDao().findByOpenligaid(openligaid);
    }

    @Override
    @Transactional
    public void createPlayer(Player player) {
        getConfig().getPlayerDao().save(player);
    }

    @Override
    @Transactional
    public void deletePlayer(Player player) {
        getConfig().getPlayerDao().delete(player);
    }

    @Override
    @Transactional
    public void updatePlayer(Player player) {
        getConfig().getPlayerDao().update(player);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Player> findAllPlayers() {
        return getConfig().getPlayerDao().findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Player findPlayer(long id) {
        return getConfig().getPlayerDao().findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Player> findPlayerByOpenligaid(long openligaid) {
        return getConfig().getPlayerDao().findByOpenligaid(openligaid);
    }

}
