package org.bnbalint.snackdaddy.repositories;

import org.bnbalint.snackdaddy.models.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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




    @Test
    void test_save() {

        //--------------------------------------------------
        // SET VALUES
        Team team = new Team(
                "Monsters",
                Rink.BAIREL,
                Level.D5,
                "#b88907",
                "#000000",
                "#c42324",
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
        // EXECUTE
        var savedEntry = snackLogRepo.save(snackLogEntry);
        System.out.println("Saved entry = " + savedEntry);

        //--------------------------------------------------
        // VERIFY RESULTS
        assert(savedEntry.getId() > 0);
        assertEquals(savedEntry.getDateMade(), LocalDate.of(2026, Month.JULY, 2));
        assertNotNull(savedEntry.getCreatedAt());
        assertNotNull(savedEntry.getUpdatedAt());

        var savedEntrySnack = savedEntry.getSnack();
        assertEquals("Rice Crispie Treat", savedEntrySnack.getName());
        assertEquals(true, savedEntrySnack.getSweet());
        assertEquals(false, savedEntrySnack.getSavory());
        assertEquals(2, savedEntrySnack.getDifficulty());
        assertEquals(4, savedEntrySnack.getIngredients().length);
        assertEquals("", savedEntrySnack.getRecipeUrl());

        var savedEntryTeam = savedEntry.getTeam();
        assertEquals("Monsters", savedEntryTeam.getName());
        assertEquals(Rink.BAIREL, savedEntryTeam.getRink());
        assertEquals(Level.D5, savedEntryTeam.getLevel());
        assertEquals("#b88907", savedEntryTeam.getPrimaryColor());
        assertEquals("#000000", savedEntryTeam.getSecondaryColor());
        assertEquals("#c42324", savedEntryTeam.getTernaryColor());
        assertEquals("", savedEntryTeam.getLogoUrl());
    }
}
