/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2025 by Andre Winkler. All
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

package de.betoffice.service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.betoffice.dao.CommunityDao;
import de.betoffice.dao.GameTippDao;
import de.betoffice.dao.GoalDao;
import de.betoffice.dao.GroupDao;
import de.betoffice.dao.GroupTypeDao;
import de.betoffice.dao.MatchDao;
import de.betoffice.dao.PlayerDao;
import de.betoffice.dao.RoundDao;
import de.betoffice.dao.SeasonDao;
import de.betoffice.dao.TeamDao;
import de.betoffice.storage.Game;
import de.betoffice.storage.GameList;
import de.betoffice.storage.GameResult;
import de.betoffice.storage.GameTipp;
import de.betoffice.storage.Goal;
import de.betoffice.storage.Group;
import de.betoffice.storage.GroupType;
import de.betoffice.storage.Player;
import de.betoffice.storage.Season;
import de.betoffice.storage.SeasonReference;
import de.betoffice.storage.Team;
import de.betoffice.storage.TeamResult;
import de.betoffice.storage.User;
import de.betoffice.util.BetofficeValidator;
import de.betoffice.validation.ValidationException;
import de.betoffice.validation.ValidationMessage;
import de.betoffice.validation.ValidationMessage.MessageType;

/**
 * Die Default-Implementierung der Meisterschaftsverwaltung.
 *
 * @author by Andre Winkler
 */
@Service("seasonManagerService")
@Transactional(readOnly = true)
public class DefaultSeasonManagerService extends AbstractManagerService implements SeasonManagerService {

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

    @Autowired
    private CommunityDao communityDao;

    @Override
    public List<TeamResult> calculateTeamRanking(Season season, GroupType groupType) {
        return seasonDao.calculateTeamRanking(season, groupType);
    }

    @Override
    public List<TeamResult> calculateTeamRanking(Season season,
            GroupType groupType, int startIndex, int endIndex) {

        return seasonDao.calculateTeamRanking(season, groupType, startIndex, endIndex);
    }

    @Override
    public List<Season> findAllSeasons() {
        return seasonDao.findAll();
    }

    @Override
    public List<Team> findTeams(Group group) {
        return groupDao.findTeams(group);
    }

    @Override
    public List<Team> findTeams(Season season, GroupType groupType) {
        return teamDao.findTeamsBySeasonAndGroup(season, groupType);
    }

    @Override
    public List<GroupType> findGroupTypesBySeason(Season season) {
        return groupTypeDao.findBySeason(season);
    }

    @Override
    public List<Game> findMatches(Team homeTeam, Team guestTeam, int limit) {
        return matchDao.find(homeTeam, guestTeam, limit);
    }

    @Override
    public List<Game> findMatches(Team homeTeam, Team guestTeam, boolean spin, int limit) {
        List<Game> results = new ArrayList<Game>();
        if (spin) {
            results.addAll(matchDao.findAll(homeTeam, guestTeam, limit));
        } else {
            results.addAll(matchDao.find(homeTeam, guestTeam, limit));
        }
        return results;
    }

    @Override
    public List<Game> findMatches(Team team, int limit) {
        return matchDao.find(team, limit);
    }

    @Override
    public List<Game> findMatchesWithHomeTeam(Team team, int limit) {
        return matchDao.findByHomeTeam(team, limit);
    }

    @Override
    public List<Game> findMatchesWithGuestTeam(Team team, int limit) {
        return matchDao.findByGuestTeam(team, limit);
    }

    @Override
    public List<Goal> findGoalsOfMatch(Game game) {
        return goalDao.find(game);
    }

    @Override
    public List<Game> findMatches(GameList round) {
        return matchDao.find(round);
    }

    @Override
    public Game findMatch(Long gameId) {
        return matchDao.findById(gameId);
    }

    @Override
    public Optional<Game> findMatch(GameList round, Team homeTeam, Team guestTeam) {
        return matchDao.find(round, homeTeam, guestTeam);
    }

    @Override
    public Optional<GameList> findRound(Season season, int index) {
        return roundDao.findRound(season, index);
    }

    @Override
    public Optional<GameList> findLastRound(Season season) {
        return roundDao.findLastRound(season);
    }

    @Override
    public Optional<GameList> findFirstRound(Season season) {
        return roundDao.findFirstRound(season);
    }

    @Override
    public GameList findRound(long id) {
        return roundDao.findById(id);
    }

    @Override
    public Optional<GameList> findRoundGames(long roundId) {
        return roundDao.findRound(roundId);
    }

    @Override
    public Optional<GameList> findNextRound(long id) {
        Optional<Long> nextRoundId = roundDao.findNext(id);
        Optional<GameList> nextGameList = Optional.empty();
        if (nextRoundId.isPresent()) {
            nextGameList = Optional.of(roundDao.findById(nextRoundId.get()));
        }
        return nextGameList;
    }

    @Override
    public Optional<GameList> findPrevRound(long id) {
        Optional<Long> prevRoundId = roundDao.findPrevious(id);
        Optional<GameList> prevGameList = Optional.empty();
        if (prevRoundId.isPresent()) {
            prevGameList = Optional.of(roundDao.findById(prevRoundId.get()));
        }
        return prevGameList;
    }

    @Override
    public List<GameList> findRounds(Season season) {
        return roundDao.findRounds(season);
    }

    @Override
    public List<GameList> findRounds(Group group) {
        return roundDao.findRounds(group);
    }

    @Override
    public List<Group> findGroups(Season season) {
        return groupDao.findBySeason(season);
    }

    @Override
    public List<GroupType> findGroupTypes(Season season) {
        return (groupTypeDao.findBySeason(season));
    }

    @Override
    public Group findGroup(Season season, GroupType groupType) {
        return (groupDao.findBySeasonAndGroupType(season, groupType));
    }

    @Override
    public Optional<Season> findSeasonByName(String name, String year) {
        return seasonDao.find(SeasonReference.of(year, name));
    }

    @Override
    public Season findSeasonById(long id) {
        return seasonDao.findById(id);
    }

    @Override
    @Transactional
    public Game addMatch(GameList round, ZonedDateTime date, Group group, Team homeTeam, Team guestTeam) {
        GameList gamelist = roundDao.findById(round.getId());
        Game match = new Game();
        match.setDateTime(date);
        match.setHomeTeam(homeTeam);
        match.setGuestTeam(guestTeam);
        match.setGroup(group);
        matchDao.persist(match);

        gamelist.addGame(match);
        roundDao.update(gamelist);

        return match;
    }

    @Override
    @Transactional
    public Game addMatch(GameList round, ZonedDateTime date, Group group,
            Team homeTeam, Team guestTeam, int homeGoals, int guestGoals) {

        return addMatch(round, date, group, homeTeam, guestTeam, GameResult.of(homeGoals, guestGoals));
    }

    @Override
    @Transactional
    public Game addMatch(GameList round, ZonedDateTime date, Group group,
            Team homeTeam, Team guestTeam, GameResult result) {

        GameList gamelist = roundDao.findById(round.getId());
        Game match = new Game();
        match.setDateTime(date);
        match.setHomeTeam(homeTeam);
        match.setGuestTeam(guestTeam);
        match.setGroup(group);
        match.setResult(result);
        match.setPlayed(true);
        matchDao.persist(match);

        gamelist.addGame(match);
        roundDao.update(gamelist);

        return match;
    }

    @Override
    @Transactional
    public Game addMatch(Season season, int round, ZonedDateTime date,
            GroupType groupType, Team homeTeam, Team guestTeam) {

        Season persistedSeason = seasonDao.findById(season.getId());
        return (addMatch(persistedSeason.getGamesOfDay(round), date,
                persistedSeason.getGroup(groupType), homeTeam, guestTeam));
    }

    @Override
    @Transactional
    public Game addMatch(Season season, int round, ZonedDateTime date,
            GroupType groupType, Team homeTeam, Team guestTeam, int homeGoals,
            int guestGoals) {

        Season persistedSeason = seasonDao.findById(season.getId());
        return (addMatch(persistedSeason.getGamesOfDay(round), date,
                persistedSeason.getGroup(groupType), homeTeam, guestTeam, homeGoals,
                guestGoals));
    }

    @Override
    @Transactional
    public GameList addRound(Season season, ZonedDateTime date, GroupType groupType) {
        GameList round = new GameList();
        round.setDateTime(date);

        Group group = groupDao.findBySeasonAndGroupType(season, groupType);
        round.setGroup(group);

        Season persistedSeason = seasonDao.findById(season.getId());
        persistedSeason.addGameList(round);
        roundDao.persist(round);
        return round;
    }

    @Override
    @Transactional
    public GameList addRound(Season season, int index, ZonedDateTime data, GroupType groupType) {
        BetofficeValidator.validateRoundIndex(index);

        Season seasonEntity = seasonDao.find(season.getReference())
                .orElseThrow(() -> new IllegalArgumentException(String.format("Season %s not found.", season)));

        Optional<GameList> gameList = roundDao.findRound(seasonEntity, index);
        if (gameList.isPresent()) {
            throw new IllegalArgumentException(String.format("Round '%s, Index: %s' already exists.", season, index));
        }

        GroupType groupTypeEntity = groupTypeDao.findByName(groupType.getName())
                .orElseThrow(() -> new IllegalArgumentException(String.format("GroupTyp %s not found.", groupType)));

        Group group = groupDao.findBySeasonAndGroupType(seasonEntity, groupTypeEntity);
        if (group == null) {
            throw new IllegalArgumentException(
                    String.format("Group '%s, GroupType: %s' not found.", seasonEntity, groupTypeEntity));
        }

        GameList round = new GameList();
        round.setDateTime(data);
        round.setIndex(index);
        round.setGroup(group);
        seasonEntity.addGameList(round);

        return round;
    }

    @Override
    @Transactional
    public GameList updateRound(Season season, int index, ZonedDateTime date, GroupType groupType) {
        BetofficeValidator.validateRoundIndex(index);

        Season seasonEntity = seasonDao.find(season.getReference())
                .orElseThrow(() -> new IllegalArgumentException(String.format("Season %s not found.", season)));

        GroupType groupTypeEntity = groupTypeDao.findByName(groupType.getName())
                .orElseThrow(() -> new IllegalArgumentException(String.format("GroupTyp %s not found.", groupType)));

        Group group = groupDao.findBySeasonAndGroupType(seasonEntity, groupTypeEntity);
        if (group == null) {
            throw new IllegalArgumentException(
                    String.format("Group '%s, GroupType: %s' not found.", seasonEntity, groupTypeEntity));
        }

        GameList round = roundDao.findRound(season, index)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Round '%s, Index: %s' not found.")));

        round.setGroup(group);
        round.setDateTime(date);

        return round;
    }

    @Override
    @Transactional
    public Group addTeam(Season season, GroupType groupType, Team team) {
        List<ValidationMessage> messages = new ArrayList<>();

        if (!season.getTeamType().equals(team.getTeamType())) {
            messages.add(ValidationMessage.error(
                    MessageType.SEASON_DOES_NOT_SUPPORT_THIS_TEAM_TYPE, season, team.getTeamType()));
        }

        if (messages.isEmpty()) {
            Group group = groupDao.findBySeasonAndGroupType(season, groupType);

            List<Team> teams = groupDao.findTeams(group);
            if (teams.contains(team)) {
                throw new ValidationException(ValidationMessage.error(
                        MessageType.SEASON_GROUP_TEAM_IS_ALREADY_A_MEMBER, team, season, groupType));
            }
            // Group group = season.getGroup(groupType);
            // TODO Lazy load exception
            group.addTeam(team);
            groupDao.update(group);
            return group;
        } else {
            throw new ValidationException(messages);
        }
    }

    @Override
    @Transactional
    public Group addTeams(Season season, GroupType groupType,
            Collection<Team> teams) {

        teams.stream().forEach(team -> addTeam(season, groupType, team));
        return groupDao.findBySeasonAndGroupType(season, groupType);
    }

    @Override
    @Transactional
    public Season createSeason(Season season) {
        try {
            seasonDao.persist(season);
            return season;
        } catch (ConstraintViolationException ex) {
            List<ValidationMessage> messages = new ArrayList<>();
            messages.add(ValidationMessage.error(MessageType.TEAM_ALREADY_EXISTS));
            throw new ValidationException(messages);
        }
    }

    @Override
    @Transactional
    public void deleteSeason(Season season) {
        List<ValidationMessage> messages = new ArrayList<>();

        if (!communityDao.find(season.getReference()).isEmpty()) {
            messages.add(ValidationMessage.error(MessageType.SEASON_DELETE_NOT_POSSIBE_COMMUNITIES_EXISTS));
        }

        if (!findRounds(season).isEmpty()) {
            messages.add(ValidationMessage.error(MessageType.SEASON_DELETE_NOT_POSSIBLE_ROUNDS_EXISTS));
        }

        if (!findGroups(season).isEmpty()) {
            messages.add(ValidationMessage.error(MessageType.SEASON_DELETE_NOT_POSSIBLE_GROUPS_EXISTS));
        }

        if (messages.isEmpty()) {
            seasonDao.delete(season);
        } else {
            throw new ValidationException(messages);
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
        Group group = groupDao.findBySeasonAndGroupType(season, groupType);
        group.removeTeam(team);
        // Group group = season.getGroup(groupType);
        // group.removeTeam(team);
        groupDao.update(group);
    }

    @Override
    @Transactional
    public void removeTeams(Season season, GroupType groupType, Collection<Team> teams) {
        teams.stream().forEach(team -> removeTeam(season, groupType, team));
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
    public Season addGroupType(Season season, GroupType groupType) {
        Objects.requireNonNull(season, "season is null");
        Objects.requireNonNull(groupType, "groupType is null");

        Season persistedSeason = seasonDao.findById(season.getId());

        Group group = new Group();
        group.setGroupType(groupType);
        persistedSeason.addGroup(group);
        groupDao.persist(group);

        return persistedSeason;
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
        Group group = groupDao.findBySeasonAndGroupType(season, groupType);
        groupDao.delete(group);
        // TODO season.remove(group) ?
    }

    @Override
    @Transactional
    public void removeGroupType(Season season, Collection<GroupType> groupTypes) {
        for (GroupType groupType : groupTypes) {
            removeGroupType(season, groupType);
        }
    }

    @Override
    public List<GameTipp> findTipps(GameList round, User user) {
        return gameTippDao.find(round, user);
    }

    @Override
    public List<GameTipp> findTippsByMatch(Game match) {
        return gameTippDao.find(match);
    }

    @Override
    public Optional<Player> findGoalsOfPlayer(long id) {
        return playerDao.findAllGoalsOfPlayer(id);
    }

    @Override
    @Transactional
    public void addGoal(Game match, Goal goal) {
        match.addGoal(goal);
        goal.setGame(match);
        matchDao.update(match);
        goalDao.persist(goal);
    }

    @Override
    public List<Goal> findAllGoals() {
        return goalDao.findAll();
    }

}
