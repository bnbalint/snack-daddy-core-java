package org.bnbalint.snackdaddy.repositories;

import org.bnbalint.snackdaddy.models.Ingredient;
import org.bnbalint.snackdaddy.models.SuggestedAllergy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class SuggestedAllergyRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SuggestedAllergyRepository suggestedAllergyRepo;



    @Test
    void test_findAll() {

        //--------------------------------------------------
        // SET VALUES
        SuggestedAllergy suggestedAllergy = new SuggestedAllergy("Pine nut");

        //--------------------------------------------------
        // CONFIGURE MOCKS
        // Persist the data to the database
        entityManager.persistAndFlush(suggestedAllergy);
        System.out.println("Saved suggestedAllergy = " + suggestedAllergy);

        //--------------------------------------------------
        // EXECUTE
        var foundSuggestions = suggestedAllergyRepo.findAll();
        System.out.println("Found suggestions = " + foundSuggestions);

        //--------------------------------------------------
        // VERIFY RESULTS
        assertEquals(1, foundSuggestions.size());
        var firstSuggestion = foundSuggestions.get(0);
        assertEquals("Pine nut", firstSuggestion.getName());
    }

    @Test
    void test_save() {

        //--------------------------------------------------
        // SET VALUES
        SuggestedAllergy suggestedAllergy = new SuggestedAllergy("Gluten");

        //--------------------------------------------------
        // EXECUTE
        var savedIngredient = suggestedAllergyRepo.save(suggestedAllergy);
        System.out.println("Saved suggestedAllergy = " + savedIngredient);

        //--------------------------------------------------
        // VERIFY RESULTS
        assert(savedIngredient.getId() > 0);
        assertEquals("Gluten", savedIngredient.getName());
        assertNotNull(savedIngredient.getCreatedAt());
        assertNotNull(savedIngredient.getUpdatedAt());
    }
}
