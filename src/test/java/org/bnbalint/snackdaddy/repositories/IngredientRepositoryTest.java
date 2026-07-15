package org.bnbalint.snackdaddy.repositories;

import org.bnbalint.snackdaddy.models.Ingredient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class IngredientRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private IngredientRepository ingredientRepo;


    @Test
    void test_findAll() {

        //--------------------------------------------------
        // SET VALUES
        Ingredient ingredient = new Ingredient("Pecan");

        //--------------------------------------------------
        // CONFIGURE MOCKS
        // Persist the data to the database
        entityManager.persistAndFlush(ingredient);
        System.out.println("Saved ingredient = " + ingredient);

        //--------------------------------------------------
        // EXECUTE
        var foundIngredients = ingredientRepo.findAll();
        System.out.println("Found ingredients = " + foundIngredients);

        //--------------------------------------------------
        // VERIFY RESULTS
        assertEquals(1, foundIngredients.size());
        var firstIngredient = foundIngredients.get(0);
        assertEquals("Pecan", firstIngredient.getName());
    }

    @Test
    void test_save() {

        //--------------------------------------------------
        // SET VALUES
        Ingredient ingredient = new Ingredient("Almond Milk");

        //--------------------------------------------------
        // EXECUTE
        var savedIngredient = ingredientRepo.save(ingredient);
        System.out.println("Saved ingredient = " + savedIngredient);

        //--------------------------------------------------
        // VERIFY RESULTS
        assert(savedIngredient.getId() > 0);
        assertEquals("Almond Milk", savedIngredient.getName());
        assertNotNull(savedIngredient.getCreatedAt());
        assertNotNull(savedIngredient.getUpdatedAt());
    }
}
