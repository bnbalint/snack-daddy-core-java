package org.bnbalint.snackdaddy.controllers;

import org.bnbalint.snackdaddy.models.Team;
import org.bnbalint.snackdaddy.repositories.TeamRepository;
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
public class TeamController {

    //-------------------------------------------
    // Constants
    //
    private static final String JSON = "application/json";
    private final Logger log = LoggerFactory.getLogger(TeamController.class);

    //-------------------------------------------
    // Single Autowired Constructor
    //
    private final TeamRepository teamRepo;
    public TeamController(TeamRepository teamRepository){
        this.teamRepo = teamRepository;
    }


    /**
     * Get all teams
     * @return the full list of teams from the database
     */
    @RequestMapping(
            value = "/teams",
            method = RequestMethod.GET,
            produces = JSON
    )
    public ResponseEntity<List<Team>> getAllTeams() {
        log.trace("getAllTeams");

        try {
            List<Team> teams = teamRepo.findAll();
            log.debug("Found teams = {}", teams);
            return ResponseEntity.ok(teams);
        } catch (Exception ex) {
            log.error("Error while querying DB for all teams", ex);
            return ResponseEntity.internalServerError().build();
        }
    }


    /**
     * Add a new team to the database
     * @param team - the team to add
     * @return
     * 400 if the request body is null
     * 400 if the request body already has an ID
     * 400 if there is a database conflict
     * 500 for all other DB related errors
     * 201 and the team when successful
     */
    @RequestMapping(
            value = "/teams",
            method = RequestMethod.POST,
            consumes = JSON,
            produces = JSON
    )
    public ResponseEntity<?> addTeam(@RequestBody Team team) {
        log.trace("addTeam - team = {}", team);

        // check incoming parameter for validity
        if (team == null){
            log.error("Request body (team) must not be null");
            return ResponseEntity.badRequest().body("Request body (team) must not be null");
        }

        if (team.getId() != null){
            log.error("Request body (team) must not already have an id");
            return ResponseEntity.badRequest().body("Request body (team) must not already have an id");
        }

        try {
            Team savedTeam = teamRepo.save(team);
            log.debug("saved team = {}", savedTeam);
            return new ResponseEntity<>(savedTeam, HttpStatus.CREATED);
        } catch (OptimisticLockingFailureException ex) {
            log.error("Conflict error while saving team to the database", ex);
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            log.error("Error while saving team to database", ex);
            return ResponseEntity.internalServerError().build();
        }
    }


    /**
     * Update a team
     * @param team - the team to update
     * @return
     * 400 if the request body is null
     * 400 if the request body does not have a valid id
     * 400 if the team does not already exist in the database
     * 400 if there is a database conflict
     * 500 for all other DB related errors
     * 200 and the team when successful
     */
    @RequestMapping(
            value = "/teams",
            method = RequestMethod.PUT,
            consumes = JSON,
            produces = JSON
    )
    public ResponseEntity<?> updateTeam(@RequestBody Team team) {
        log.trace("updateTeam - team = {}", team);

        // check incoming parameter for validity
        if (team == null){
            log.error("Request body (team) must not be null");
            return ResponseEntity.badRequest().body("Request body (team) must not be null");
        }

        if (team.getId() == null){
            log.error("Request body (team) must have a valid id");
            return ResponseEntity.badRequest().body("Request body (team) must have a valid id");
        }

        // get the existingTeam, if it exists
        var existingTeam = teamRepo.findById(team.getId()).orElse(null);

        // team must already exist for an update to be made
        if (existingTeam == null){
            log.error("Team does not already exist - cannot update");
            return ResponseEntity.badRequest().body("Team does not already exist - cannot update");
        }

        try {
            Team updatedTeam = teamRepo.save(team);
            log.debug("updated team = {}", updatedTeam);
            return new ResponseEntity<>(updatedTeam, HttpStatus.OK);
        } catch (OptimisticLockingFailureException ex) {
            log.error("Conflict error while updating team in the database", ex);
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            log.error("Error while updating team in the database", ex);
            return ResponseEntity.internalServerError().build();
        }
    }
}
