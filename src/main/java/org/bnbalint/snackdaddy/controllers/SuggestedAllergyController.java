package org.bnbalint.snackdaddy.controllers;

import org.bnbalint.snackdaddy.models.Ingredient;
import org.bnbalint.snackdaddy.models.SuggestedAllergy;
import org.bnbalint.snackdaddy.repositories.IngredientRepository;
import org.bnbalint.snackdaddy.repositories.SuggestedAllergyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;import java.util.List;

@RestController
public class SuggestedAllergyController {

    //-------------------------------------------
    // Constants
    //
    private static final String JSON = "application/json";
    private final Logger log =LoggerFactory.getLogger(SuggestedAllergyController.class);

    //-------------------------------------------
    // Single Autowired Constructor
    //
    private final SuggestedAllergyRepository suggestedAllergyRepo;
    public SuggestedAllergyController(SuggestedAllergyRepository suggestedAllergyRepository){
        this.suggestedAllergyRepo = suggestedAllergyRepository;
    }


    /**
     * Get all suggestedAllergies
     * @return the full list of suggestedAllergies from the database
     */
    @RequestMapping(
        value = "/suggested-allergies",
        method = RequestMethod.GET,
        produces = JSON
    )
    public ResponseEntity<List<SuggestedAllergy>> getAllSuggestedAllergies() {
        log.trace("getAllSuggestedAllergies");

        try {
            List<SuggestedAllergy> suggestions = suggestedAllergyRepo.findAll();
            log.debug("Found suggestions = {}", suggestions);
            return ResponseEntity.ok(suggestions);
        } catch (Exception ex) {
            log.error("Error while querying DB for all suggestions", ex);
            return ResponseEntity.internalServerError().build();
        }
    }


    /**
     * Add a new suggestedAllergy to the database
     * @param suggestedAllergy - the suggestedAllergy to add
     * @return the added suggestedAllergy
     */
    @RequestMapping(
            value = "/suggested-allergies",
            method = RequestMethod.POST,
            consumes = JSON,
            produces = JSON
    )
    public ResponseEntity<SuggestedAllergy> addSuggestedAllergy(@RequestBody SuggestedAllergy suggestedAllergy) {
        log.trace("addSuggestedAllergy - suggestedAllergy = {}", suggestedAllergy);

        try {
            SuggestedAllergy savedSuggestion = suggestedAllergyRepo.save(suggestedAllergy);
            log.debug("saved suggestedAllergy = {}", savedSuggestion);
            return new ResponseEntity<>(savedSuggestion, HttpStatus.CREATED);
        } catch (OptimisticLockingFailureException ex) {
            log.error("Conflict error while saving suggestedAllergy to the database", ex);
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            log.error("Error while saving suggestedAllergy to database", ex);
            return ResponseEntity.internalServerError().build();
        }
    }

}
