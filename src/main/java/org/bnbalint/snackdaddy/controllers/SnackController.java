package org.bnbalint.snackdaddy.controllers;

import org.bnbalint.snackdaddy.models.Snack;
import org.bnbalint.snackdaddy.repositories.SnackRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SnackController {

    //-------------------------------------------
    // Constants
    //
    private static final String JSON = "application/json";
    private final Logger log = LoggerFactory.getLogger(SnackController.class);

    //-------------------------------------------
    // Single Autowired Constructor
    //
    private final SnackRepository snackRepo;
    public SnackController(SnackRepository snackRepository){
        this.snackRepo = snackRepository;
    }


    /**
     * Get all snacks
     * @return the full list of snacks from the database
     */
    @RequestMapping(
            value = "/snacks",
            method = RequestMethod.GET,
            produces = JSON
    )
    public ResponseEntity<List<Snack>> getAllSnacks() {
        log.trace("getAllSnacks");

        try {
            List<Snack> snacks = snackRepo.findAll();
            log.debug("Found snacks = {}", snacks);
            return ResponseEntity.ok(snacks);
        } catch (Exception ex) {
            log.error("Error while querying DB for all snacks", ex);
            return ResponseEntity.internalServerError().build();
        }
    }


    /**
     * Add a new snack to the database
     * @param snack - the snack to add
     * @return the added snack
     */
    @RequestMapping(
            value = "/snacks",
            method = RequestMethod.POST,
            consumes = JSON,
            produces = JSON
    )
    public ResponseEntity<Snack> addSnack(@RequestBody Snack snack) {
        log.trace("addSnack - snack = {}", snack);

        try {
            Snack savedSnack = snackRepo.save(snack);
            log.debug("saved snack = {}", savedSnack);
            return new ResponseEntity<>(savedSnack, HttpStatus.CREATED);
        } catch (OptimisticLockingFailureException ex) {
            log.error("Conflict error while saving snack to the database", ex);
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            log.error("Error while saving snack to database", ex);
            return ResponseEntity.internalServerError().build();
        }
    }
}
