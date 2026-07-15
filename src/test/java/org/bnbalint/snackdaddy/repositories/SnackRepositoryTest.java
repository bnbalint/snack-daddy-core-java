package org.bnbalint.snackdaddy.repositories;

import org.bnbalint.snackdaddy.models.Ingredient;
import org.bnbalint.snackdaddy.models.Snack;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class SnackRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SnackRepository snackRepo;


    @Test
    void test_findAll() {

        //--------------------------------------------------
        // SET VALUES
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

        //--------------------------------------------------
        // CONFIGURE MOCKS
        // Persist the data to the database (first the ingredients, then the snack)
        entityManager.persist(ingredient1);
        entityManager.persist(ingredient2);
        entityManager.persist(ingredient3);
        entityManager.persistAndFlush(ingredient4);
        entityManager.persistAndFlush(snack);
        System.out.println("Saved snack = " + snack);

        //--------------------------------------------------
        // EXECUTE
        var foundSnacks = snackRepo.findAll();
        System.out.println("Found snacks = " + foundSnacks);

        //--------------------------------------------------
        // VERIFY RESULTS
        assertEquals(1, foundSnacks.size());
        var firstSnack = foundSnacks.get(0);
        assertEquals("Rice Crispie Treat", firstSnack.getName());
        assertEquals(true, firstSnack.getSweet());
        assertEquals(false, firstSnack.getSavory());
        assertEquals(2, firstSnack.getDifficulty());
        assertEquals(4, firstSnack.getIngredients().length);
        assertEquals("", firstSnack.getRecipeUrl());
    }

    @Test
    void test_save() {

        //--------------------------------------------------
        // SET VALUES
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


        //--------------------------------------------------
        // EXECUTE
        var savedSnack = snackRepo.save(snack);
        System.out.println("Saved snack = " + savedSnack);

        //--------------------------------------------------
        // VERIFY RESULTS
        assert(savedSnack.getId() > 0);
        assertEquals("Rice Crispie Treat", savedSnack.getName());
        assertEquals(true, savedSnack.getSweet());
        assertEquals(false, savedSnack.getSavory());
        assertEquals(2, savedSnack.getDifficulty());
        assertEquals(4, savedSnack.getIngredients().length);
        assertEquals("", savedSnack.getRecipeUrl());
        assertNotNull(savedSnack.getCreatedAt());
        assertNotNull(savedSnack.getUpdatedAt());
    }


    @Test
    void test_saveAll() {

        //--------------------------------------------------
        // SET VALUES
        Ingredient ingredient1 = new Ingredient("Crisp Rice Cereal");
        Ingredient ingredient2 = new Ingredient("Marshmallow");
        Ingredient ingredient3 = new Ingredient("Margarine");
        Ingredient ingredient4 = new Ingredient("Vanilla");
        Ingredient[] ingredients = { ingredient1, ingredient2, ingredient3, ingredient4 };
        Snack snack1 = new Snack(
                "Rice Crispie Treat",
                true,
                false,
                2,
                ingredients
        );

        Snack snack2 = new Snack(
                "Better Rice Crispie Treat",
                true,
                true,
                5,
                ingredients
        );


        //--------------------------------------------------
        // EXECUTE
        var savedSnacks = snackRepo.saveAll(List.of(snack1, snack2));
        System.out.println("Saved snacks = " + savedSnacks);

        //--------------------------------------------------
        // VERIFY RESULTS
        assert(savedSnacks.size() == 2);

        var savedSnack1 = savedSnacks.get(0);
        assert(savedSnack1.getId() > 0);
        assertEquals("Rice Crispie Treat", savedSnack1.getName());
        assertEquals(true, savedSnack1.getSweet());
        assertEquals(false, savedSnack1.getSavory());
        assertEquals(2, savedSnack1.getDifficulty());
        assertEquals(4, savedSnack1.getIngredients().length);
        assertEquals("", savedSnack1.getRecipeUrl());
        assertNotNull(savedSnack1.getCreatedAt());
        assertNotNull(savedSnack1.getUpdatedAt());

        var savedSnack2 = savedSnacks.get(1);
        assert(savedSnack2.getId() > 0);
        assertEquals("Better Rice Crispie Treat", savedSnack2.getName());
        assertEquals(true, savedSnack2.getSweet());
        assertEquals(true, savedSnack2.getSavory());
        assertEquals(5, savedSnack2.getDifficulty());
        assertEquals(4, savedSnack2.getIngredients().length);
        assertEquals("", savedSnack2.getRecipeUrl());
        assertNotNull(savedSnack2.getCreatedAt());
        assertNotNull(savedSnack2.getUpdatedAt());
    }
}
