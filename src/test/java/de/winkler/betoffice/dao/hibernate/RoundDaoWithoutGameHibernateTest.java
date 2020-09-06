package de.winkler.betoffice.dao.hibernate;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.winkler.betoffice.dao.GroupDao;
import de.winkler.betoffice.dao.RoundDao;
import de.winkler.betoffice.storage.GameList;
import de.winkler.betoffice.storage.Group;

public class RoundDaoWithoutGameHibernateTest extends AbstractDaoTestSupport {

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
        final Group group = groupDao.findById(1L);
        final List<GameList> rounds = roundDao.findRounds(group);

        assertThat(rounds).hasSize(5);
    }

}
