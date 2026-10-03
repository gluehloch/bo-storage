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

import de.betoffice.storage.community.CommunityDao;
import de.betoffice.storage.group.GroupTypeDao;
import de.betoffice.storage.group.entity.GroupTypeEntity;
import de.betoffice.storage.season.GameTippDao;
import de.betoffice.storage.season.GoalDao;
import de.betoffice.storage.season.GroupDao;
import de.betoffice.storage.season.MatchDao;
import de.betoffice.storage.season.PlayerDao;
import de.betoffice.storage.season.RoundDao;
import de.betoffice.storage.season.SeasonDao;
import de.betoffice.storage.season.entity.GameEntity;
import de.betoffice.storage.season.entity.GameListEntity;
import de.betoffice.storage.season.entity.GameResult;
import de.betoffice.storage.season.entity.GoalEntity;
import de.betoffice.storage.season.entity.GroupEntity;
import de.betoffice.storage.season.entity.PlayerEntity;
import de.betoffice.storage.season.entity.SeasonEntity;
import de.betoffice.storage.season.entity.SeasonReference;
import de.betoffice.storage.team.TeamDao;
import de.betoffice.storage.team.TeamResult;
import de.betoffice.storage.team.entity.TeamEntity;
import de.betoffice.storage.tip.GameTippEntity;
import de.betoffice.storage.user.entity.UserEntity;
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
    public List<TeamResult> calculateTeamRanking(SeasonEntity season, GroupTypeEntity groupType) {
        return seasonDao.calculateTeamRanking(season, groupType);
    }

    @Override
    public List<TeamResult> calculateTeamRanking(SeasonEntity season,
            GroupTypeEntity groupType, int startIndex, int endIndex) {

        return seasonDao.calculateTeamRanking(season, groupType, startIndex, endIndex);
    }

    @Override
    public List<SeasonEntity> findAllSeasons() {
        return seasonDao.findAll();
    }

    @Override
    public List<TeamEntity> findTeams(GroupEntity group) {
        return groupDao.findTeams(group);
    }

    @Override
    public List<TeamEntity> findTeams(SeasonEntity season, GroupTypeEntity groupType) {
        return teamDao.findTeamsBySeasonAndGroup(season, groupType);
    }

    @Override
    public List<GroupTypeEntity> findGroupTypes(SeasonEntity season) {
        return (groupTypeDao.findBySeason(season));
    }

    @Override
    public List<GameEntity> findMatches(TeamEntity homeTeam, TeamEntity guestTeam, int limit) {
        return matchDao.find(homeTeam, guestTeam, limit);
    }

    @Override
    public List<GameEntity> findMatches(TeamEntity homeTeam, TeamEntity guestTeam, boolean spin, int limit) {
        List<GameEntity> results = new ArrayList<GameEntity>();
        if (spin) {
            results.addAll(matchDao.findAll(homeTeam, guestTeam, limit));
        } else {
            results.addAll(matchDao.find(homeTeam, guestTeam, limit));
        }
        return results;
    }

    @Override
    public List<GameEntity> findMatches(TeamEntity team, int limit) {
        return matchDao.find(team, limit);
    }

    @Override
    public List<GameEntity> findMatchesWithHomeTeam(TeamEntity team, int limit) {
        return matchDao.findByHomeTeam(team, limit);
    }

    @Override
    public List<GameEntity> findMatchesWithGuestTeam(TeamEntity team, int limit) {
        return matchDao.findByGuestTeam(team, limit);
    }

    @Override
    public List<GoalEntity> findGoalsOfMatch(GameEntity game) {
        return goalDao.find(game);
    }

    @Override
    public List<GameEntity> findMatches(GameListEntity round) {
        return matchDao.find(round);
    }

    @Override
    public List<GameEntity> findMatches(ZonedDateTime dateTime) {
        return matchDao.findByDay(dateTime);
    }

    @Override
    public GameEntity findMatch(Long gameId) {
        return matchDao.findById(gameId);
    }

    @Override
    public Optional<GameEntity> findMatch(GameListEntity round, TeamEntity homeTeam, TeamEntity guestTeam) {
        return matchDao.find(round, homeTeam, guestTeam);
    }

    @Override
    public Optional<GameListEntity> findRound(SeasonEntity season, int index) {
        return roundDao.findRound(season, index);
    }

    @Override
    public Optional<GameListEntity> findLastRound(SeasonEntity season) {
        return roundDao.findLastRound(season);
    }

    @Override
    public Optional<GameListEntity> findFirstRound(SeasonEntity season) {
        return roundDao.findFirstRound(season);
    }

    @Override
    public GameListEntity findRound(long id) {
        return roundDao.findById(id);
    }

    @Override
    public Optional<GameListEntity> findRoundGames(long roundId) {
        return roundDao.findRound(roundId);
    }

    @Override
    public Optional<GameListEntity> findNextRound(long id) {
        Optional<Long> nextRoundId = roundDao.findNext(id);
        Optional<GameListEntity> nextGameList = Optional.empty();
        if (nextRoundId.isPresent()) {
            nextGameList = Optional.of(roundDao.findById(nextRoundId.get()));
        }
        return nextGameList;
    }

    @Override
    public Optional<GameListEntity> findPrevRound(long id) {
        Optional<Long> prevRoundId = roundDao.findPrevious(id);
        Optional<GameListEntity> prevGameList = Optional.empty();
        if (prevRoundId.isPresent()) {
            prevGameList = Optional.of(roundDao.findById(prevRoundId.get()));
        }
        return prevGameList;
    }

    @Override
    public List<GameListEntity> findRounds(SeasonEntity season) {
        return roundDao.findRounds(season);
    }

    @Override
    public List<GameListEntity> findRounds(GroupEntity group) {
        return roundDao.findRounds(group);
    }

    @Override
    public List<GroupEntity> findGroups(SeasonEntity season) {
        return groupDao.findBySeason(season);
    }

    @Override
    public GroupEntity findGroup(SeasonEntity season, GroupTypeEntity groupType) {
        return (groupDao.findBySeasonAndGroupType(season, groupType));
    }

    @Override
    public Optional<SeasonEntity> findSeasonByName(String name, String year) {
        return seasonDao.find(SeasonReference.of(year, name));
    }

    @Override
    public SeasonEntity findSeasonById(long id) {
        return seasonDao.findById(id);
    }

    @Override
    @Transactional
    public GameEntity addMatch(GameListEntity round, ZonedDateTime date, GroupEntity group, TeamEntity homeTeam, TeamEntity guestTeam) {
        GameListEntity gamelist = roundDao.findById(round.getId());
        GameEntity match = new GameEntity();
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
    public GameEntity addMatch(GameListEntity round, ZonedDateTime date, GroupEntity group,
            TeamEntity homeTeam, TeamEntity guestTeam, int homeGoals, int guestGoals) {

        return addMatch(round, date, group, homeTeam, guestTeam, GameResult.of(homeGoals, guestGoals));
    }

    @Override
    @Transactional
    public GameEntity addMatch(GameListEntity round, ZonedDateTime date, GroupEntity group,
            TeamEntity homeTeam, TeamEntity guestTeam, GameResult result) {

        GameListEntity gamelist = roundDao.findById(round.getId());
        GameEntity match = new GameEntity();
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
    public GameEntity addMatch(SeasonEntity season, int round, ZonedDateTime date,
            GroupTypeEntity groupType, TeamEntity homeTeam, TeamEntity guestTeam) {

        SeasonEntity persistedSeason = seasonDao.findById(season.getId());
        return (addMatch(persistedSeason.getGamesOfDay(round), date,
                persistedSeason.getGroup(groupType), homeTeam, guestTeam));
    }

    @Override
    @Transactional
    public GameEntity addMatch(SeasonEntity season, int round, ZonedDateTime date,
            GroupTypeEntity groupType, TeamEntity homeTeam, TeamEntity guestTeam, int homeGoals,
            int guestGoals) {

        SeasonEntity persistedSeason = seasonDao.findById(season.getId());
        return (addMatch(persistedSeason.getGamesOfDay(round), date,
                persistedSeason.getGroup(groupType), homeTeam, guestTeam, homeGoals,
                guestGoals));
    }

    @Override
    @Transactional
    public GameListEntity addRound(SeasonEntity season, ZonedDateTime date, GroupTypeEntity groupType) {
        GameListEntity round = new GameListEntity();
        round.setDateTime(date);

        GroupEntity group = groupDao.findBySeasonAndGroupType(season, groupType);
        round.setGroup(group);

        SeasonEntity persistedSeason = seasonDao.findById(season.getId());
        persistedSeason.addGameList(round);
        roundDao.persist(round);
        return round;
    }

    @Override
    @Transactional
    public GameListEntity addRound(SeasonEntity season, int index, ZonedDateTime data, GroupTypeEntity groupType) {
        BetofficeValidator.validateRoundIndex(index);

        SeasonEntity seasonEntity = seasonDao.find(season.getReference())
                .orElseThrow(() -> new IllegalArgumentException(String.format("Season %s not found.", season)));

        Optional<GameListEntity> gameList = roundDao.findRound(seasonEntity, index);
        if (gameList.isPresent()) {
            throw new IllegalArgumentException(String.format("Round '%s, Index: %s' already exists.", season, index));
        }

        GroupTypeEntity groupTypeEntity = groupTypeDao.findByName(groupType.getName())
                .orElseThrow(() -> new IllegalArgumentException(String.format("GroupTyp %s not found.", groupType)));

        GroupEntity group = groupDao.findBySeasonAndGroupType(seasonEntity, groupTypeEntity);
        if (group == null) {
            throw new IllegalArgumentException(
                    String.format("Group '%s, GroupType: %s' not found.", seasonEntity, groupTypeEntity));
        }

        GameListEntity round = new GameListEntity();
        round.setDateTime(data);
        round.setIndex(index);
        round.setGroup(group);
        seasonEntity.addGameList(round);

        return round;
    }

    @Override
    @Transactional
    public GameListEntity updateRound(SeasonEntity season, int index, ZonedDateTime date, GroupTypeEntity groupType) {
        BetofficeValidator.validateRoundIndex(index);

        SeasonEntity seasonEntity = seasonDao.find(season.getReference())
                .orElseThrow(() -> new IllegalArgumentException(String.format("Season %s not found.", season)));

        GroupTypeEntity groupTypeEntity = groupTypeDao.findByName(groupType.getName())
                .orElseThrow(() -> new IllegalArgumentException(String.format("GroupTyp %s not found.", groupType)));

        GroupEntity group = groupDao.findBySeasonAndGroupType(seasonEntity, groupTypeEntity);
        if (group == null) {
            throw new IllegalArgumentException(
                    String.format("Group '%s, GroupType: %s' not found.", seasonEntity, groupTypeEntity));
        }

        GameListEntity round = roundDao.findRound(season, index)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Round '%s, Index: %s' not found.")));

        round.setGroup(group);
        round.setDateTime(date);

        return round;
    }

    @Override
    @Transactional
    public GroupEntity addTeam(SeasonEntity season, GroupTypeEntity groupType, TeamEntity team) {
        List<ValidationMessage> messages = new ArrayList<>();

        if (!season.getTeamType().equals(team.getTeamType())) {
            messages.add(ValidationMessage.error(
                    MessageType.SEASON_DOES_NOT_SUPPORT_THIS_TEAM_TYPE, season, team.getTeamType()));
        }

        if (messages.isEmpty()) {
            GroupEntity group = groupDao.findBySeasonAndGroupType(season, groupType);

            List<TeamEntity> teams = groupDao.findTeams(group);
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
    public GroupEntity addTeams(SeasonEntity season, GroupTypeEntity groupType,
            Collection<TeamEntity> teams) {

        teams.stream().forEach(team -> addTeam(season, groupType, team));
        return groupDao.findBySeasonAndGroupType(season, groupType);
    }

    @Override
    @Transactional
    public SeasonEntity createSeason(SeasonEntity season) {
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
    public void deleteSeason(SeasonEntity season) {
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
    public void removeMatch(GameEntity match) {
        match.getGameList().removeGame(match);
        matchDao.delete(match);
    }

    @Override
    @Transactional
    public void removeRound(SeasonEntity season, GameListEntity round) {
        SeasonEntity season2 = seasonDao.findById(season.getId());
        season2.removeGameList(round);
        roundDao.delete(round);

        List<GameListEntity> rounds = roundDao.findRounds(season);
        for (int i = 0; i < rounds.size(); i++) {
            GameListEntity r = rounds.get(i);
            r.setIndex(i);
        }
    }

    @Override
    @Transactional
    public void removeTeam(SeasonEntity season, GroupTypeEntity groupType, TeamEntity team) {
        GroupEntity group = groupDao.findBySeasonAndGroupType(season, groupType);
        group.removeTeam(team);
        // Group group = season.getGroup(groupType);
        // group.removeTeam(team);
        groupDao.update(group);
    }

    @Override
    @Transactional
    public void removeTeams(SeasonEntity season, GroupTypeEntity groupType, Collection<TeamEntity> teams) {
        teams.stream().forEach(team -> removeTeam(season, groupType, team));
    }

    @Override
    @Transactional
    public void updateMatch(GameEntity match) {
        matchDao.update(match);
    }

    @Override
    @Transactional
    public void updateMatch(Collection<GameEntity> modifiedMatches) {
        for (GameEntity match : modifiedMatches) {
            matchDao.update(match);
        }
    }

    @Override
    @Transactional
    public void updateSeason(SeasonEntity season) {
        seasonDao.update(season);
    }

    @Override
    @Transactional
    public SeasonEntity addGroupType(SeasonEntity season, GroupTypeEntity groupType) {
        Objects.requireNonNull(season, "season is null");
        Objects.requireNonNull(groupType, "groupType is null");

        SeasonEntity persistedSeason = seasonDao.findById(season.getId());

        GroupEntity group = new GroupEntity();
        group.setGroupType(groupType);
        persistedSeason.addGroup(group);
        groupDao.persist(group);

        return persistedSeason;
    }

    @Override
    @Transactional
    public void addGroupType(SeasonEntity season, Collection<GroupTypeEntity> groupTypes) {
        for (GroupTypeEntity groupType : groupTypes) {
            addGroupType(season, groupType);
        }
    }

    @Override
    @Transactional
    public void removeGroupType(SeasonEntity season, GroupTypeEntity groupType) {
        GroupEntity group = groupDao.findBySeasonAndGroupType(season, groupType);
        groupDao.delete(group);
        // TODO season.remove(group) ?
    }

    @Override
    @Transactional
    public void removeGroupType(SeasonEntity season, Collection<GroupTypeEntity> groupTypes) {
        for (GroupTypeEntity groupType : groupTypes) {
            removeGroupType(season, groupType);
        }
    }

    @Override
    public List<GameTippEntity> findTipps(GameListEntity round, UserEntity user) {
        return gameTippDao.find(round, user);
    }

    @Override
    public List<GameTippEntity> findTippsByMatch(GameEntity match) {
        return gameTippDao.find(match);
    }

    @Override
    public Optional<PlayerEntity> findGoalsOfPlayer(long id) {
        return playerDao.findAllGoalsOfPlayer(id);
    }

    @Override
    @Transactional
    public void addGoal(GameEntity match, GoalEntity goal) {
        match.addGoal(goal);
        goal.setGame(match);
        matchDao.update(match);
        goalDao.persist(goal);
    }

    @Override
    public List<GoalEntity> findAllGoals() {
        return goalDao.findAll();
    }

}
