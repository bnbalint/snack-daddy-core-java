package org.bnbalint.snackdaddy.controllers;

import org.bnbalint.snackdaddy.models.Ingredient;
import org.bnbalint.snackdaddy.repositories.IngredientRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;import java.util.List;

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


    /**
     * Add a new ingredient to the database
     * @param ingredient - the ingredient to add
     * @return the added ingredient
     */
    @RequestMapping(
            value = "/ingredients",
            method = RequestMethod.POST,
            consumes = JSON,
            produces = JSON
    )
    public ResponseEntity<Ingredient> addIngredient(@RequestBody Ingredient ingredient) {
        log.trace("addIngredient - ingredient = {}", ingredient);

        try {
            Ingredient savedIngredient = ingredientRepo.save(ingredient);
            log.debug("saved ingredient = {}", savedIngredient);
            return new ResponseEntity<>(savedIngredient, HttpStatus.CREATED);
        } catch (ConstraintViolationException ex) {
            log.error("Conflict error while saving ingredient to the database", ex);
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            log.error("Error while saving ingredient to database", ex);
            return ResponseEntity.internalServerError().build();
        }
    }

}
