package org.bnbalint.snackdaddy.controllers;

import org.bnbalint.snackdaddy.models.Ingredient;
import org.bnbalint.snackdaddy.repositories.IngredientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IngredientController {

    //-------------------------------------------
    // Constants
    //
    private static final String JSON = "application/json";
    private final Logger log =LoggerFactory.getLogger(IngredientController.class);

    //-------------------------------------------
    // Single Autowired Constructor
    //
    private final IngredientRepository ingredientRepo;
    public IngredientController(IngredientRepository ingredientRepository){
        this.ingredientRepo = ingredientRepository;
    }


    /**
     * Get all ingredients
     * @return the full list of ingredients from the database
     */
    @RequestMapping(
        value = "/ingredients",
        method = RequestMethod.GET,
        produces = JSON
    )
    public ResponseEntity<List<Ingredient>> getAllIngredients() {
        log.trace("getAllIngredients");

        try {
            List<Ingredient> ingredients = ingredientRepo.findAll();
            log.debug("Found ingredients = {}", ingredients);
            return ResponseEntity.ok(ingredients);
        } catch (Exception ex) {
            log.error("Error while querying DB for all ingredients", ex);
            return ResponseEntity.internalServerError().build();
        }
    }

}
