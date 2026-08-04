package org.bnbalint.snackdaddy.controllers;

import org.bnbalint.snackdaddy.models.User;
import org.bnbalint.snackdaddy.models.UserSnackRanking;
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

import java.util.List;

@RestController
public class UserSnackRankingController {

    //-------------------------------------------
    // Constants
    //
    private static final String JSON = "application/json";
    private final Logger log = LoggerFactory.getLogger(UserSnackRankingController.class);

    //-------------------------------------------
    // Single Autowired Constructor
    //
    private final UserSnackRankingRepository userSnackRankingRepo;
    public UserSnackRankingController(UserSnackRankingRepository userSnackRankingRepository){
        this.userSnackRankingRepo = userSnackRankingRepository;
    }


    /**
     * Get all rankings, for all users
     * @return the full list of rankings from the database
     */
    @RequestMapping(
            value = "/snack-rankings",
            method = RequestMethod.GET,
            produces = JSON
    )
    public ResponseEntity<List<UserSnackRanking>> getAllRankings() {
        log.trace("getAllRankings");

        try {
            List<UserSnackRanking> rankings = userSnackRankingRepo.findAll();
            log.debug("Found rankings = {}", rankings);
            return ResponseEntity.ok(rankings);
        } catch (Exception ex) {
            log.error("Error while querying DB for all rankings", ex);
            return ResponseEntity.internalServerError().build();
        }
    }


    /**
     * Add a new UserSnackRanking to the database
     * @param ranking - the UserSnackRanking to add
     * @return the added UserSnackRanking
     */
    @RequestMapping(
            value = "/snack-rankings",
            method = RequestMethod.POST,
            consumes = JSON,
            produces = JSON
    )
    public ResponseEntity<UserSnackRanking> addUserSnackRanking(@RequestBody UserSnackRanking ranking) {
        log.trace("addUserSnackRanking - ranking = {}", ranking);

        try {
            UserSnackRanking savedRanking = userSnackRankingRepo.save(ranking);
            log.debug("saved ranking = {}", savedRanking);
            return new ResponseEntity<>(savedRanking, HttpStatus.CREATED);
        } catch (OptimisticLockingFailureException ex) {
            log.error("Conflict error while saving ranking to the database", ex);
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            log.error("Error while saving ranking to database", ex);
            return ResponseEntity.internalServerError().build();
        }
    }
}
