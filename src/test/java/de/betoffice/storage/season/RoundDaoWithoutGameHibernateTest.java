package de.betoffice.storage.season;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.betoffice.dao.hibernate.AbstractDaoTestSupport;
import de.betoffice.storage.season.entity.GameList;
import de.betoffice.storage.season.entity.Group;
import de.betoffice.storage.season.entity.Season;

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
        // Den Spieltagen sind keine Spiele zugeordnet. Die Selektion ueber die Gruppe kann nur funktionieren,
        // wenn es Spiele gibt, die einer Gruppe zugeordnet sind.
        final Group group = groupDao.findById(1L);
        final List<GameList> rounds1 = roundDao.findRounds(group);
        assertThat(rounds1).hasSize(0);
        
        // In diesem Fall muessen die Spieltage ueber die Meisterschaft selektiert werden. 
        final Season season = seasonDao.findById(1L);       
        final List<GameList> rounds2 = roundDao.findRounds(season);
        assertThat(rounds2).hasSize(5);        
    }

}
