package org.bnbalint.snackdaddy.repositories;

import org.bnbalint.snackdaddy.models.Level;
import org.bnbalint.snackdaddy.models.Rink;
import org.bnbalint.snackdaddy.models.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class TeamRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TeamRepository teamRepo;


    @Test
    void test_findAll() {

        //--------------------------------------------------
        // SET VALUES
        Team team = new Team(
            "Mules",
            Rink.BAIREL,
            Level.D5,
            "#b88907",
            "#000000",
            "#c42323",
            ""
        );

        //--------------------------------------------------
        // CONFIGURE MOCKS
        // Persist the data to the database
        entityManager.persistAndFlush(team);
        System.out.println("Saved team = " + team);

        //--------------------------------------------------
        // EXECUTE
        var foundTeams = teamRepo.findAll();
        System.out.println("Found teams = " + foundTeams);

        //--------------------------------------------------
        // VERIFY RESULTS
        assertEquals(1, foundTeams.size());
        var firstTeam = foundTeams.get(0);
        assertEquals("Mules", firstTeam.getName());
        assertEquals(Rink.BAIREL, firstTeam.getRink());
        assertEquals(Level.D5, firstTeam.getLevel());
        assertEquals("#b88907", firstTeam.getPrimaryColor());
        assertEquals("#000000", firstTeam.getSecondaryColor());
        assertEquals("#c42323", firstTeam.getTernaryColor());
        assertEquals("", firstTeam.getLogoUrl());
    }


    @Test
    void test_save() {

        //--------------------------------------------------
        // SET VALUES
        Team team = new Team(
                "Mules",
                Rink.BAIREL,
                Level.D5,
                "#b88907",
                "#000000",
                "#c42323",
                ""
        );

        //--------------------------------------------------
        // EXECUTE
        var savedTeam = teamRepo.save(team);
        System.out.println("Saved team = " + savedTeam);

        //--------------------------------------------------
        // VERIFY RESULTS
        assert(savedTeam.getId() > 0);
        assertEquals("Mules", savedTeam.getName());
        assertEquals(Rink.BAIREL, savedTeam.getRink());
        assertEquals(Level.D5, savedTeam.getLevel());
        assertEquals("#b88907", savedTeam.getPrimaryColor());
        assertEquals("#000000", savedTeam.getSecondaryColor());
        assertEquals("#c42323", savedTeam.getTernaryColor());
        assertEquals("", savedTeam.getLogoUrl());
        assertNotNull(savedTeam.getCreatedAt());
        assertNotNull(savedTeam.getUpdatedAt());
    }
}
