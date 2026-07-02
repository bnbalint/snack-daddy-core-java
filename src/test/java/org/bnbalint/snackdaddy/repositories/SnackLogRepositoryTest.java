package org.bnbalint.snackdaddy.repositories;

import org.bnbalint.snackdaddy.models.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class SnackLogRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SnackLogRepository snackLogRepo;


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

        Ingredient ingredient1 = new Ingredient("Crisp Rice Cereal");
        Ingredient ingredient2 = new Ingredient("Marshmallow");
        Ingredient ingredient3 = new Ingredient("Margarine");
        Ingredient ingredient4 = new Ingredient("Vanilla");
        Ingredient[] ingredients = { ingredient1, ingredient2, ingredient3, ingredient4 };
        Snack snack = new Snack(
                "Rice Crispie Treat",
                true,
                false,
                2,
                ingredients
        );

        SnackLog snackLogEntry = new SnackLog(snack, team, LocalDate.of(2026, Month.JULY, 2));

        //--------------------------------------------------
        // CONFIGURE MOCKS
        // Persist the data to the database (first the ingredients, then the snack, then the team, then the entry)
        entityManager.persist(ingredient1);
        entityManager.persist(ingredient2);
        entityManager.persist(ingredient3);
        entityManager.persistAndFlush(ingredient4);
        entityManager.persist(snack);
        entityManager.persistAndFlush(team);
        entityManager.persistAndFlush(snackLogEntry);
        System.out.println("Saved snackLogEntry = " + snackLogEntry);

        //--------------------------------------------------
        // EXECUTE
        var foundEntries = snackLogRepo.findAll();
        System.out.println("Found entries = " + foundEntries);

        //--------------------------------------------------
        // VERIFY RESULTS
        assertEquals(1, foundEntries.size());
        var firstEntry = foundEntries.get(0);

        var firstSnack = firstEntry.getSnack();
        assertEquals("Rice Crispie Treat", firstSnack.getName());
        assertEquals(true, firstSnack.getSweet());
        assertEquals(false, firstSnack.getSavory());
        assertEquals(2, firstSnack.getDifficulty());
        assertEquals(4, firstSnack.getIngredients().length);
        assertEquals("", firstSnack.getRecipeUrl());

        var firstTeam = firstEntry.getTeam();
        assertEquals("Mules", firstTeam.getName());
        assertEquals(Rink.BAIREL, firstTeam.getRink());
        assertEquals(Level.D5, firstTeam.getLevel());
        assertEquals("#b88907", firstTeam.getPrimaryColor());
        assertEquals("#000000", firstTeam.getSecondaryColor());
        assertEquals("#c42323", firstTeam.getTernaryColor());
        assertEquals("", firstTeam.getLogoUrl());
    }
}
