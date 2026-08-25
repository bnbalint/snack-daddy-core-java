package org.bnbalint.snackdaddy.controllers;

import org.bnbalint.snackdaddy.models.SnackLog;
import org.bnbalint.snackdaddy.models.SnackRank;
import org.bnbalint.snackdaddy.models.User;
import org.bnbalint.snackdaddy.models.UserSnackRanking;
import org.bnbalint.snackdaddy.repositories.SnackLogRepository;
import org.bnbalint.snackdaddy.repositories.UserRepository;
import org.bnbalint.snackdaddy.repositories.UserSnackRankingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
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
    private final UserRepository userRepo;
    private final UserSnackRankingRepository userSnackRankingRepo;
    public SnackLogController(SnackLogRepository snackLogRepository, UserRepository userRepository, UserSnackRankingRepository userSnackRankingRepository){
        this.snackLogRepo = snackLogRepository;
        this.userRepo = userRepository;
        this.userSnackRankingRepo = userSnackRankingRepository;
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
    public ResponseEntity<List<SnackLog>> getAllSnackLogEntries() {
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
     * @return
     *  201 and the added snackLogEntry entry
     *  400 if a Team is not provided on the SnackLog entry
     *  400 if a TeamId is not provided on the SnackLog entry
     *  400 if a Snack is not provided in the SnackLog entry
     *
     * Save the new SnackLog entry to the database
     * Get all users on the team
     * For each user, create an UNRANKED UserSnackRanking
     * Save the UserSnackRanking (on conflict of a ranking already existing for the snack for the user, do nothing)
     */
    @RequestMapping(
            value = "/snack-log",
            method = RequestMethod.POST,
            consumes = JSON,
            produces = JSON
    )
    public ResponseEntity<?> addToSnackLog(@RequestBody SnackLog snackLogEntry) {
        log.trace("addToSnackLog - entry = {}", snackLogEntry);

        // check for validity of Team and TeamId in the request
        if (snackLogEntry.getTeam() == null) {
            log.error("SnackLog must contain a valid Team");
            return ResponseEntity.badRequest().body("SnackLog must contain a valid Team");
        }
        if (snackLogEntry.getTeam().getId() == null) {
            log.error("SnackLog must contain a valid teamId");
            return ResponseEntity.badRequest().body("SnackLog must contain a valid teamId");
        }

        // check for validity of Snack
        if (snackLogEntry.getSnack() == null) {
            log.error("SnackLog must contain a valid Snack");
            return ResponseEntity.badRequest().body("SnackLog must contain a valid Snack");
        }

        SnackLog savedSnackLog;
        try {
            savedSnackLog = snackLogRepo.save(snackLogEntry);
            log.debug("saved snackLogEntry = {}", savedSnackLog);
        } catch (OptimisticLockingFailureException ex) {
            log.error("Conflict error while saving snackLogEntry to the database", ex);
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            log.error("Error while saving snackLogEntry to database", ex);
            return ResponseEntity.internalServerError().build();
        }

        // get all users on the team
        List<User> users = Collections.emptyList();
        try {
            users = userRepo.findUsersByTeamId(snackLogEntry.getTeam().getId()); // we checked that this was not null above
        } catch (Exception ex){
            log.error("Error when querying for all users on team");
        }

        // for each user, save an UNRANKED UserSnackRanking (if a ranking already exists for this user/snack, do nothing)
        for (User user : users) {
            var updatedRow = userSnackRankingRepo.saveOnConflictDoNothing(user, snackLogEntry.getSnack(), SnackRank.UNRANKED);

            if (updatedRow != 0) {
                log.debug("Added UNRANKED entry for user{}", user.getId());
            }
        }


        // final return
        return new ResponseEntity<>(savedSnackLog, HttpStatus.CREATED);

    }
}
