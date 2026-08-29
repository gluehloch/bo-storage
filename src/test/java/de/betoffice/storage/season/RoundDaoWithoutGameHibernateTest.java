package de.betoffice.storage.season;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.betoffice.dao.hibernate.AbstractDaoTestSupport;
import de.betoffice.storage.season.entity.GameListEntity;
import de.betoffice.storage.season.entity.GroupEntity;
import de.betoffice.storage.season.entity.SeasonEntity;

public class RoundDaoWithoutGameHibernateTest extends AbstractDaoTestSupport {

    @Autowired
    private SeasonDao seasonDao;

    @Autowired
    private RoundDao roundDao;

    @Autowired
    private GroupDao groupDao;

    @BeforeEach
    public void init() {
        prepareDatabase(RoundDaoWithoutGameHibernateTest.class);
    }

    @Test
    public void findRoundWithoutGames() {
        // FIXED: Spieltage ohne Spiele werden ausgegeben. Anpassung der Abfrage `roundDao.findRounds(Group)`.
        // Den Spieltagen sind keine Spiele zugeordnet. Die Selektion ueber die Gruppe kann nur funktionieren,
        // wenn es Spiele gibt, die einer Gruppe zugeordnet sind.
        final GroupEntity group = groupDao.findById(1L);
        final List<GameListEntity> rounds1 = roundDao.findRounds(group);
        assertThat(rounds1).hasSize(5);

        // In diesem Fall muessen die Spieltage ueber die Meisterschaft selektiert werden. 
        final SeasonEntity season = seasonDao.findById(1L);
        final List<GameListEntity> rounds2 = roundDao.findRounds(season);
        assertThat(rounds2).hasSize(5);
    }

}
