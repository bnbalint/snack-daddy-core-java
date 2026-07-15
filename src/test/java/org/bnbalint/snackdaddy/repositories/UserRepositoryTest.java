package org.bnbalint.snackdaddy.repositories;

import org.bnbalint.snackdaddy.models.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepo;


    @Test
    void test_findAll() {

        //--------------------------------------------------
        // SET VALUES
        Ingredient ingredient = new Ingredient("Pecan");
        Team team = new Team(
                "Mules",
                Rink.BAIREL,
                Level.D5,
                "#b88907",
                "#000000",
                "#c42323",
                ""
        );
        Team[] teams = { team };
        Ingredient[] allergies = { ingredient };

        User user = new User("Roger", "Hogwarts", "r.h@gmail.com", teams, allergies);


        //--------------------------------------------------
        // CONFIGURE MOCKS
        // Persist the data to the database (first the ingredient, then the team, then the user)
        entityManager.persist(ingredient);
        entityManager.persistAndFlush(team);
        entityManager.persistAndFlush(user);
        System.out.println("Saved user = " + user);

        //--------------------------------------------------
        // EXECUTE
        var foundUsers = userRepo.findAll();
        System.out.println("Found user = " + foundUsers);

        //--------------------------------------------------
        // VERIFY RESULTS
        assertEquals(1, foundUsers.size());
        var firstUser = foundUsers.get(0);
        assertEquals("Roger", firstUser.getFirstName());
        assertEquals("Hogwarts", firstUser.getLastName());
        assertEquals("r.h@gmail.com", firstUser.getEmail());

        var firstTeams = firstUser.getTeams();
        assertEquals(1, firstTeams.length);
        var firstTeam = firstTeams[0];
        assertEquals("Mules", firstTeam.getName());
        assertEquals(Rink.BAIREL, firstTeam.getRink());
        assertEquals(Level.D5, firstTeam.getLevel());
        assertEquals("#b88907", firstTeam.getPrimaryColor());
        assertEquals("#000000", firstTeam.getSecondaryColor());
        assertEquals("#c42323", firstTeam.getTernaryColor());
        assertEquals("", firstTeam.getLogoUrl());

        var firstAllergies = firstUser.getAllergies();
        assertEquals(1, firstAllergies.length);
        var firstAllergy = firstAllergies[0];
        assertEquals("Pecan", firstAllergy.getName());
    }


    @Test
    void test_save() {

        //--------------------------------------------------
        // SET VALUES
        Ingredient ingredient = new Ingredient("Pecan");
        Team team = new Team(
                "Mules",
                Rink.BAIREL,
                Level.D5,
                "#b88907",
                "#000000",
                "#c42323",
                ""
        );
        Team[] teams = { team };
        Ingredient[] allergies = { ingredient };

        User user = new User("Roger", "Hogwarts", "r.h@gmail.com", teams, allergies);


        //--------------------------------------------------
        // EXECUTE
        var savedUser = userRepo.save(user);
        System.out.println("Saved user = " + savedUser);

        //--------------------------------------------------
        // VERIFY RESULTS
        assert(savedUser.getId() > 0);
        assertEquals("Roger", savedUser.getFirstName());
        assertEquals("Hogwarts", savedUser.getLastName());
        assertEquals("r.h@gmail.com", savedUser.getEmail());
        assertNotNull(savedUser.getCreatedAt());
        assertNotNull(savedUser.getUpdatedAt());

        var firstTeams = savedUser.getTeams();
        assertEquals(1, firstTeams.length);
        var firstTeam = firstTeams[0];
        assertEquals("Mules", firstTeam.getName());
        assertEquals(Rink.BAIREL, firstTeam.getRink());
        assertEquals(Level.D5, firstTeam.getLevel());
        assertEquals("#b88907", firstTeam.getPrimaryColor());
        assertEquals("#000000", firstTeam.getSecondaryColor());
        assertEquals("#c42323", firstTeam.getTernaryColor());
        assertEquals("", firstTeam.getLogoUrl());

        var firstAllergies = savedUser.getAllergies();
        assertEquals(1, firstAllergies.length);
        var firstAllergy = firstAllergies[0];
        assertEquals("Pecan", firstAllergy.getName());
    }
}
