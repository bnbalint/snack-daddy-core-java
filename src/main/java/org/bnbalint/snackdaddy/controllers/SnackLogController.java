package org.bnbalint.snackdaddy.controllers;

import org.bnbalint.snackdaddy.models.SnackLog;
import org.bnbalint.snackdaddy.repositories.SnackLogRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SnackLogController {

    //-------------------------------------------
    // Constants
    //
    private static final String JSON = "application/json";
    private final Logger log = LoggerFactory.getLogger(SnackLogController.class);

    //-------------------------------------------
    // Single Autowired Constructor
    //
    private final SnackLogRepository snackLogRepo;
    public SnackLogController(SnackLogRepository snackLogRepository){
        this.snackLogRepo = snackLogRepository;
    }


    /**
     * Get the entire snack log
     * @return the full of snack log from the database
     */
    @RequestMapping(
            value = "/snack-log",
            method = RequestMethod.GET,
            produces = JSON
    )
    public ResponseEntity<List<SnackLog>> getAllSnackLogs() {
        log.trace("getAllSnackLogs");

        try {
            List<SnackLog> snackLogs = snackLogRepo.findAll();
            log.debug("Full snack log = {}", snackLogs);
            return ResponseEntity.ok(snackLogs);
        } catch (Exception ex) {
            log.error("Error while querying DB for full snack log", ex);
            return ResponseEntity.internalServerError().build();
        }
    }


    /**
     * Add a new snack log entry to the database
     * @param snackLogEntry - the snackLogEntry entry to add
     * @return the added snackLogEntry entry
     */
    @RequestMapping(
            value = "/snack-log",
            method = RequestMethod.POST,
            consumes = JSON,
            produces = JSON
    )
    public ResponseEntity<SnackLog> addToSnackLog(@RequestBody SnackLog snackLogEntry) {
        log.trace("addToSnackLog - entry = {}", snackLogEntry);

        try {
            SnackLog savedSnackLog = snackLogRepo.save(snackLogEntry);
            log.debug("saved snackLogEntry = {}", savedSnackLog);
            return new ResponseEntity<>(savedSnackLog, HttpStatus.CREATED);
        } catch (ConstraintViolationException ex) {
            log.error("Conflict error while saving snackLogEntry to the database", ex);
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            log.error("Error while saving snackLogEntry to database", ex);
            return ResponseEntity.internalServerError().build();
        }
    }
}
