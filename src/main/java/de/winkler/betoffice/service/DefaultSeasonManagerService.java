/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2024 by Andre Winkler. All
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

import de.winkler.betoffice.dao.CommunityDao;
import de.winkler.betoffice.dao.GameTippDao;
import de.winkler.betoffice.dao.GoalDao;
import de.winkler.betoffice.dao.GroupDao;
import de.winkler.betoffice.dao.GroupTypeDao;
import de.winkler.betoffice.dao.MatchDao;
import de.winkler.betoffice.dao.PlayerDao;
import de.winkler.betoffice.dao.RoundDao;
import de.winkler.betoffice.dao.SeasonDao;
import de.winkler.betoffice.dao.TeamDao;
import de.winkler.betoffice.storage.Game;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.GameResult;
import de.winkler.betoffice.storage.GameTipp;
import de.winkler.betoffice.storage.Goal;
import de.winkler.betoffice.storage.Group;
import de.winkler.betoffice.storage.GroupType;
import de.winkler.betoffice.storage.Player;
import de.winkler.betoffice.storage.Season;
import de.winkler.betoffice.storage.SeasonReference;
import de.winkler.betoffice.storage.Team;
import de.winkler.betoffice.storage.TeamResult;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.util.BetofficeValidator;
import de.winkler.betoffice.validation.BetofficeValidationException;
import de.winkler.betoffice.validation.BetofficeValidationMessage;
import de.winkler.betoffice.validation.BetofficeValidationMessage.Severity;

/**
 * Die Default-Implementierung der Meisterschaftsverwaltung.
 *
 * @author by Andre Winkler
 */
@Service("seasonManagerService")
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
    @Transactional(readOnly = true)
    public List<TeamResult> calculateTeamRanking(Season season, GroupType groupType) {
        return seasonDao.calculateTeamRanking(season, groupType);
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
    public List<Season> findAllSeasons() {
        return seasonDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Team> findTeams(Group group) {
        return groupDao.findTeams(group);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Team> findTeams(Season season, GroupType groupType) {
        return teamDao.findTeamsBySeasonAndGroup(season, groupType);
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
    public List<Game> findMatches(Team homeTeam, Team guestTeam, boolean spin) {
        List<Game> results = new ArrayList<Game>();
        if (spin) {
            results.addAll(matchDao.findAll(homeTeam, guestTeam));
        } else {
            results.addAll(matchDao.find(homeTeam, guestTeam));
        }
        return results;
    }

    @Override
    public List<Game> findMatches(Team team) {
        return matchDao.find(team);
    }

    @Override
    public List<Game> findMatchesWithHomeTeam(Team team) {
        return matchDao.findByHomeTeam(team);
    }

    @Override
    public List<Game> findMatchesWithGuestTeam(Team team) {
        return matchDao.findByGuestTeam(team);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Goal> findGoalsOfMatch(Game game) {
        return goalDao.find(game);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Game> findMatches(GameList round) {
        return matchDao.find(round);
    }

    @Override
    @Transactional(readOnly = true)
    public Game findMatch(Long gameId) {
        return matchDao.findById(gameId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Game> findMatch(GameList round, Team homeTeam, Team guestTeam) {
        return matchDao.find(round, homeTeam, guestTeam);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GameList> findRound(Season season, int index) {
        return roundDao.findRound(season, index);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GameList> findLastRound(Season season) {
        return roundDao.findLastRound(season);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GameList> findFirstRound(Season season) {
        return roundDao.findFirstRound(season);
    }

    @Override
    @Transactional(readOnly = true)
    public GameList findRound(long id) {
        return roundDao.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GameList> findRoundGames(long roundId) {
        return roundDao.findRound(roundId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GameList> findNextRound(long id) {
        Optional<Long> nextRoundId = roundDao.findNext(id);
        Optional<GameList> nextGameList = Optional.empty();
        if (nextRoundId.isPresent()) {
            nextGameList = Optional.of(roundDao.findById(nextRoundId.get()));
        }
        return nextGameList;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GameList> findPrevRound(long id) {
        Optional<Long> prevRoundId = roundDao.findPrevious(id);
        Optional<GameList> prevGameList = Optional.empty();
        if (prevRoundId.isPresent()) {
            prevGameList = Optional.of(roundDao.findById(prevRoundId.get()));
        }
        return prevGameList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GameList> findRounds(Season season) {
        return roundDao.findRounds(season);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GameList> findRounds(Group group) {
        return roundDao.findRounds(group);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Group> findGroups(Season season) {
        return groupDao.findBySeason(season);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupType> findGroupTypes(Season season) {
        return (groupTypeDao.findBySeason(season));
    }

    @Override
    @Transactional(readOnly = true)
    public Group findGroup(Season season, GroupType groupType) {
        return (groupDao.findBySeasonAndGroupType(season, groupType));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Season> findSeasonByName(String name, String year) {
        return seasonDao.find(SeasonReference.of(year, name));
    }

    @Override
    @Transactional(readOnly = true)
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
        List<BetofficeValidationMessage> messages = new ArrayList<>();

        if (!season.getTeamType().equals(team.getTeamType())) {
            messages.add(new BetofficeValidationMessage(
                    String.format("Die Meisterschaft %s unterstützt diesen Mannschaftstyp %s nicht.", season,
                            team.getTeamType()),
                    null, Severity.ERROR));
        }

        if (messages.isEmpty()) {
            Group group = groupDao.findBySeasonAndGroupType(season, groupType);

            List<Team> teams = groupDao.findTeams(group);
            if (teams.contains(team)) {
                throw new BetofficeValidationException(
                        BetofficeValidationMessage.error("team is already member of group", "team"));
            }
            // Group group = season.getGroup(groupType);
            // TODO Lazy load exception
            group.addTeam(team);
            groupDao.update(group);
            return group;
        } else {
            throw new BetofficeValidationException(messages);
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

        if (!communityDao.find(season.getReference()).isEmpty()) {
            messages.add(new BetofficeValidationMessage(
                    "Der Meisterschaft sind Communities zugeordnet.", null,
                    Severity.ERROR));
        }

        if (!findRounds(season).isEmpty()) {
            messages.add(new BetofficeValidationMessage(
                    "Der Meisterschaft sind Spieltage zugeordnet.", null,
                    Severity.ERROR));
        }

        if (!findGroups(season).isEmpty()) {
            messages.add(new BetofficeValidationMessage(
                    "Der Meisterschaft sind Gruppen zugordnet.", null,
                    Severity.ERROR));
        }

        if (messages.isEmpty()) {
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
    @Transactional(readOnly = true)
    public List<GameTipp> findTipps(GameList round, User user) {
        return gameTippDao.find(round, user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GameTipp> findTippsByMatch(Game match) {
        return gameTippDao.find(match);
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
        matchDao.update(match);
        goalDao.persist(goal);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Goal> findAllGoals() {
        return goalDao.findAll();
    }

}
