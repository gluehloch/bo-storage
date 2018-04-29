/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2016 by Andre Winkler. All
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

package de.winkler.betoffice.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.hibernate.exception.ConstraintViolationException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.winkler.betoffice.dao.GameTippDao;
import de.winkler.betoffice.dao.GoalDao;
import de.winkler.betoffice.dao.GroupDao;
import de.winkler.betoffice.dao.GroupTypeDao;
import de.winkler.betoffice.dao.MatchDao;
import de.winkler.betoffice.dao.PlayerDao;
import de.winkler.betoffice.dao.RoundDao;
import de.winkler.betoffice.dao.SeasonDao;
import de.winkler.betoffice.dao.TeamDao;
import de.winkler.betoffice.dao.UserDao;
import de.winkler.betoffice.dao.UserSeasonDao;
import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.GameTipp;
import de.winkler.betoffice.storage.Goal;
import de.winkler.betoffice.storage.Group;
import de.winkler.betoffice.storage.GroupType;
import de.winkler.betoffice.storage.Player;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.Team;
import de.winkler.betoffice.storage.TeamResult;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.storage.UserResult;
import de.winkler.betoffice.storage.UserSeason;
import de.winkler.betoffice.storage.enums.RoleType;
import de.winkler.betoffice.validation.BetofficeValidationException;
import de.winkler.betoffice.validation.BetofficeValidationMessage;
import de.winkler.betoffice.validation.BetofficeValidationMessage.Severity;

/**
 * Die Default-Implementierung der Meisterschaftsverwaltung.
 *
 * @author by Andre Winkler
 */
@Service("seasonManagerService")
public class DefaultSeasonManagerService extends AbstractManagerService
        implements SeasonManagerService {

    @Autowired
    private UserSeasonDao userSeasonDao;
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private SeasonDao seasonDao;
    
    @Autowired
    private TeamDao teamDao;
    
    @Autowired
    private GroupDao groupDao;
    
    @Autowired
    private GroupTypeDao groupTypeDao;
    
    @Autowired
    private RoundDao roundDao;
    
    @Autowired
    private MatchDao matchDao;
    
    @Autowired
    private GameTippDao gameTippDao;
    
    @Autowired
    private PlayerDao playerDao;
    
    @Autowired
    private GoalDao goalDao;
    
    @Override
    @Transactional(readOnly = true)
    public List<UserResult> calculateUserRanking(GameList round) {
        List<User> users = userSeasonDao
                .findUsers(round.getSeason());
        return userDao.calculateUserRanking(users, round);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResult> calculateUserRanking(Season season, int startIndex,
            int endIndex) {

        List<User> users = userSeasonDao.findUsers(season);
        return userDao.calculateUserRanking(users, season,
                startIndex, endIndex);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResult> calculateUserRanking(Season season) {
        List<User> users = userSeasonDao.findUsers(season);
        return userDao.calculateUserRanking(users, season);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamResult> calculateTeamRanking(Season season,
            GroupType groupType) {
        return seasonDao.calculateTeamRanking(season,
                groupType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamResult> calculateTeamRanking(Season season,
            GroupType groupType, int startIndex, int endIndex) {

        return seasonDao.calculateTeamRanking(season,
                groupType, startIndex, endIndex);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findActivatedUsers(Season season) {
        return userSeasonDao.findUsers(season);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Season> findAllSeasons() {
        return seasonDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Team> findTeamsByGroup(Group group) {
        return groupDao.findTeams(group);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Team> findTeamsByGroupType(Season season, GroupType groupType) {
        return teamDao.findTeamsBySeasonAndGroup(season,
                groupType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupType> findGroupTypesBySeason(Season season) {
        return groupTypeDao.findBySeason(season);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Game> findMatches(Team homeTeam, Team guestTeam) {
        return matchDao.find(homeTeam, guestTeam);
    }

    @Override
    @Transactional(readOnly = true)
    public Game findMatch(Long gameId) {
        return matchDao.findById(gameId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Game> findMatch(GameList round, Team homeTeam,
            Team guestTeam) {
        return matchDao.find(round, homeTeam, guestTeam);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Game> findMatches(Team homeTeam, Team guestTeam, boolean spin) {
        List<Game> results = new ArrayList<Game>();
        if (spin) {
            results.addAll(
                    matchDao.findAll(homeTeam, guestTeam));
        } else {
            results.addAll(matchDao.find(homeTeam, guestTeam));
        }
        return results;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GameList> findRound(Season season, int index) {
        return (roundDao.findRound(season, index));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GameList> findLastRound(Season season) {
        return (roundDao.findLastRound(season));
    }

    @Override
    @Transactional(readOnly = true)
    public GameList findRound(long id) {
        return (roundDao.findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GameList> findNextRound(long id) {
        Optional<Long> nextRoundId = roundDao.findNext(id);
        Optional<GameList> nextGameList = Optional.empty();
        if (nextRoundId.isPresent()) {
            nextGameList = Optional
                    .of(roundDao.findById(nextRoundId.get()));
        }
        return nextGameList;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GameList> findPrevRound(long id) {
        Optional<Long> prevRoundId = roundDao.findPrevious(id);
        Optional<GameList> prevGameList = Optional.empty();
        if (prevRoundId.isPresent()) {
            prevGameList = Optional
                    .of(roundDao.findById(prevRoundId.get()));
        }
        return prevGameList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GameList> findRounds(Season season) {
        return (roundDao.findRounds(season));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GameList> findRounds(Group group) {
        return (roundDao.findRounds(group));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Group> findGroups(Season season) {
        return (groupDao.findBySeason(season));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupType> findGroupTypes(Season season) {
        return (groupTypeDao.findBySeason(season));
    }

    @Override
    @Transactional(readOnly = true)
    public Group findGroup(Season season, GroupType groupType) {
        return (groupDao.findBySeasonAndGroupType(season,
                groupType));
    }

    @Override
    @Transactional(readOnly = true)
    public Season findRoundGroupTeamUserRelations(Season season) {
        return (seasonDao.findRoundGroupTeamUser(season));
    }

    @Override
    @Transactional(readOnly = true)
    public Season findRoundGroupTeamUserTippRelations(Season season) {
        return (seasonDao.findRoundGroupTeamUserTipp(season));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Season> findSeasonByName(String name, String year) {
        return seasonDao.findByName(name, year);
    }

    @Override
    @Transactional(readOnly = true)
    public Season findSeasonById(long id) {
        return seasonDao.findById(id);
    }

    @Override
    @Transactional
    public Game addMatch(GameList round, DateTime date, Group group,
            Team homeTeam, Team guestTeam) {

        Game match = new Game();
        match.setDateTime(date.toDate());
        match.setHomeTeam(homeTeam);
        match.setGuestTeam(guestTeam);
        match.setGroup(group);
        round.addGame(match);
        matchDao.save(match);
        return match;
    }

    @Override
    @Transactional
    public Game addMatch(GameList round, DateTime date, Group group,
            Team homeTeam, Team guestTeam, int homeGoals, int guestGoals) {

        Game match = new Game();
        match.setDateTime(date.toDate());
        match.setHomeTeam(homeTeam);
        match.setGuestTeam(guestTeam);
        match.setGroup(group);
        match.setResult(homeGoals, guestGoals);
        match.setPlayed(true);
        round.addGame(match);
        matchDao.save(match);
        return match;
    }

    @Override
    @Transactional
    public Game addMatch(Season season, int round, DateTime date,
            GroupType groupType, Team homeTeam, Team guestTeam) {

        return (addMatch(season.getGamesOfDay(round), date,
                season.getGroup(groupType), homeTeam, guestTeam));
    }

    @Override
    @Transactional
    public Game addMatch(Season season, int round, DateTime date,
            GroupType groupType, Team homeTeam, Team guestTeam, int homeGoals,
            int guestGoals) {

        return (addMatch(season.getGamesOfDay(round), date,
                season.getGroup(groupType), homeTeam, guestTeam, homeGoals,
                guestGoals));
    }

    @Override
    @Transactional
    public GameList addRound(Season season, DateTime date,
            GroupType groupType) {
        GameList round = new GameList();
        round.setDateTime(date.toDate());
        round.setGroup(season.getGroup(groupType));
        season.addGameList(round);
        roundDao.save(round);
        return round;
    }

    @Override
    @Transactional
    public void addTeam(Season season, GroupType groupType, Team team) {
        List<BetofficeValidationMessage> messages = new ArrayList<BetofficeValidationMessage>();

        if (!season.getTeamType().equals(team.getTeamType())) {
            messages.add(new BetofficeValidationMessage(
                    "Der Meisterschaft unterstützt diesen Mannschaftstyp nicht.",
                    null, Severity.ERROR));
        }

        if (messages.size() == 0) {
            Group group = season.getGroup(groupType);
            group.addTeam(team);
            groupDao.update(group);
        } else {
            throw new BetofficeValidationException(messages);
        }
    }

    @Override
    @Transactional
    public void addTeams(Season season, GroupType groupType,
            Collection<Team> teams) {

        for (Team team : teams) {
            addTeam(season, groupType, team);
        }
    }

    @Override
    @Transactional
    public void addUsers(Season season, Collection<User> users) {
        List<User> activeUsers = findActivatedUsers(season);
        Season season2 = findRoundGroupTeamUserRelations(season);

        users.stream()
             .filter(user -> !activeUsers.contains(user))
             .forEach(user -> {
                 UserSeason userSeason = new UserSeason();
                 userSeason.setUser(user);
                 userSeason.setRoleType(RoleType.TIPPER);
                 season2.addUser(userSeason);
                 userSeasonDao.save(userSeason);
             });
    }

    @Override
    @Transactional
    public void createSeason(Season season) {
        try {
            seasonDao.save(season);
        } catch (ConstraintViolationException ex) {
            List<BetofficeValidationMessage> messages = new ArrayList<>();
            messages.add(new BetofficeValidationMessage(
                    "Diese Meisterschaft ist bereits vorhanden.", null,
                    Severity.ERROR));
            throw new BetofficeValidationException(messages);
        }
    }

    @Override
    @Transactional
    public void deleteSeason(Season season) {
        List<BetofficeValidationMessage> messages = new ArrayList<>();

        if (findActivatedUsers(season).size() > 0) {
            messages.add(new BetofficeValidationMessage(
                    "Der Meisterschaft sind Teilnehmer zugeordnet.", null,
                    Severity.ERROR));
        }

        if (findRounds(season).size() > 0) {
            messages.add(new BetofficeValidationMessage(
                    "Der Meisterschaft sind Spieltage zugeordnet.", null,
                    Severity.ERROR));
        }

        if (findGroups(season).size() > 0) {
            messages.add(new BetofficeValidationMessage(
                    "Der Meisterschaft sind Gruppen zugordnet.", null,
                    Severity.ERROR));
        }

        if (messages.size() == 0) {
            seasonDao.delete(season);
        } else {
            throw new BetofficeValidationException(messages);
        }
    }

    @Override
    @Transactional
    public void removeMatch(Game match) {
        match.getGameList().removeGame(match);
        matchDao.delete(match);
    }

    @Override
    @Transactional
    public void removeRound(Season season, GameList round) {
        season.removeGameList(round);
        roundDao.delete(round);
    }

    @Override
    @Transactional
    public void removeTeam(Season season, GroupType groupType, Team team) {
        Group group = season.getGroup(groupType);
        group.removeTeam(team);
        groupDao.update(group);
    }

    @Override
    @Transactional
    public void removeTeams(Season season, GroupType groupType,
            Collection<Team> teams) {

        for (Team team : teams) {
            removeTeam(season, groupType, team);
        }
    }

    @Override
    @Transactional
    public void removeUsers(Season season, Collection<User> users) {
        List<User> activeUsers = findActivatedUsers(season);
        Season season2 = findRoundGroupTeamUserRelations(season);

        users.stream()
             .filter(user -> activeUsers.contains(user))
             .forEach(user -> {
                 UserSeason userSeason = season2.removeUser(user);
                 userSeasonDao.delete(userSeason);
             });
    }

    @Override
    @Transactional
    public void updateMatch(Game match) {
        matchDao.update(match);
    }

    @Override
    @Transactional
    public void updateMatch(Collection<Game> modifiedMatches) {
        for (Game match : modifiedMatches) {
            matchDao.update(match);
        }
    }

    @Override
    @Transactional
    public void updateSeason(Season season) {
        seasonDao.update(season);
    }

    @Override
    @Transactional
    public Group addGroupType(Season season, GroupType groupType) {
        if (season == null) {
            throw new IllegalArgumentException("Parameter season is null!");
        }

        if (groupType == null) {
            throw new IllegalArgumentException("Parameter groupType is null!");
        }
        
        // seasonDao.refresh(season);
        Season persistentSeason = findSeasonById(season.getId());

        Group group = season.getGroup(groupType);
        if (group == null) {
            group = new Group();
            group.setGroupType(groupType);
            season.addGroup(group);
            groupDao.save(group);
        }

        return group;
    }

    @Override
    @Transactional
    public void addGroupType(Season season, Collection<GroupType> groupTypes) {
        for (GroupType groupType : groupTypes) {
            addGroupType(season, groupType);
        }
    }

    @Override
    @Transactional
    public void removeGroupType(Season season, GroupType groupType) {
        if (season.getGroup(groupType) != null) {
            Group group = season.removeGroup(groupType);
            groupDao.delete(group);
        }
    }

    @Override
    @Transactional
    public void removeGroupType(Season season,
            Collection<GroupType> groupTypes) {
        for (GroupType groupType : groupTypes) {
            removeGroupType(season, groupType);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<GameTipp> findTippsByRoundAndUser(GameList round, User user) {
        return gameTippDao.findTippsByRoundAndUser(round,
                user);
    }

    @Override
    @Transactional(readOnly = true)
    public GameList findTipp(GameList round, User user) {
        return gameTippDao.findRound(round, user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GameTipp> findTippsByMatch(Game match) {
        return gameTippDao.findByMatch(match);
    }

    @Override
    @Transactional(readOnly = false)
    public Optional<Player> findGoalsOfPlayer(long id) {
        return playerDao.findAllGoalsOfPlayer(id);
    }

    @Override
    @Transactional
    public void addGoal(Game match, Goal goal) {
        match.addGoal(goal);
        goal.setGame(match);
        matchDao.save(match);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Goal> findAllGoals() {
        return goalDao.findAll();
    }

}
