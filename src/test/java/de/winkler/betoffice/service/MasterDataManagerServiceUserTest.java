/*
 * ============================================================================
 * Project betoffice-storage Copyright (c) 2000-2022 by Andre Winkler. All
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
 *
 */

package de.winkler.betoffice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.betoffice.database.data.DatabaseTestData.DataLoader;
import de.winkler.betoffice.storage.Nickname;
import de.winkler.betoffice.storage.User;
import de.winkler.betoffice.validation.BetofficeValidationException;

/**
 * Test CRUD operations on storage object {@link User}.
 * 
 * TODO: It would be interesting to test a more complex scenario. A user with
 * tips and other related informations. Does MySQL allow delete statements here?
 *
 * @author Andre Winkler
 */
public class MasterDataManagerServiceUserTest extends AbstractServiceTest {

    @Autowired
    protected DataSource dataSource;

    @Autowired
    protected CommunityService communityService;
    
    @Autowired
    protected SeasonManagerService seasonManagerService;

    @Autowired
    protected MasterDataManagerService masterDataManagerService;

    @Autowired
    protected DatabaseMaintenanceService databaseMaintenanceService;

    private DatabaseSetUpAndTearDown dsuatd;

    @BeforeEach
    public void setUp() throws Exception {
        dsuatd = new DatabaseSetUpAndTearDown(dataSource);
        dsuatd.setUp(DataLoader.EMPTY);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        dsuatd.tearDown();
    }

    // ------------------------------------------------------------------------

    @Test
    void testCreateUser() {
        createUser("Frosch", "Andre", "Winkler");
        createUser("Peter", "Peter", "Groth");

        List<User> users = communityService.findAllUsers();

        assertThat(users.size()).isEqualTo(2);
        assertThat(users.get(0).getNickname()).isEqualTo("Frosch");
        assertThat(users.get(1).getNickname()).isEqualTo("Peter");
    }

    @Test
    void testCreateInvalidUser() {
        User invalidUser = new User();
        
        BetofficeValidationException ex = assertThrows(BetofficeValidationException.class, () -> {
            communityService.create(co, null, null, null)
            masterDataManagerService.createUser(invalidUser);
        });
        assertThat(ex.getMessages()).isNotEmpty();
    }

    @Test
    void testUpdateUser() {
        User frosch = createUser("Frosch", "Andre", "Winkler");
        createUser("Peter", "Peter", "Groth");

        frosch.setNickname("Darkside");
        masterDataManagerService.updateUser(frosch);

        Optional<User> darkside = masterDataManagerService.findUserByNickname("Darkside");
        assertThat(darkside.isPresent()).isTrue();
        assertThat(darkside.get().getNickname()).isEqualTo("Darkside");
    }

    @Test
    void testDeleteUser() {
        User frosch = createUser("Frosch", "Andre", "Winkler");
        User peter = createUser("Peter", "Peter", "Groth");

        masterDataManagerService.deleteUser(frosch);
        List<User> users = masterDataManagerService.findAllUsers();

        assertThat(users.size()).isEqualTo(1);
        assertThat(users.get(0).getNickname()).isEqualTo("Peter");

        masterDataManagerService.deleteUser(peter);
        users = masterDataManagerService.findAllUsers();

        assertThat(users.size()).isEqualTo(0);
    }

    private User createUser(String nickname, String surname, String name) {
        Nickname nick = Nickname.of(nickname);
        User user = new User();
        user.setNickname(nick);
        user.setName(name);
        user.setSurname(surname);
        communityService.createUser(user);
        return user;
    }

}
