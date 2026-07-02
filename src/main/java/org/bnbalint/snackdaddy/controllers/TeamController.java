package org.bnbalint.snackdaddy.controllers;

import org.bnbalint.snackdaddy.models.Ingredient;
import org.bnbalint.snackdaddy.models.Team;
import org.bnbalint.snackdaddy.repositories.IngredientRepository;
import org.bnbalint.snackdaddy.repositories.TeamRepository;
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
     * @return the added team
     */
    @RequestMapping(
            value = "/teams",
            method = RequestMethod.POST,
            consumes = JSON,
            produces = JSON
    )
    public ResponseEntity<Team> addTeam(@RequestBody Team team) {
        log.trace("addTeam - team = {}", team);

        try {
            Team savedTeam = teamRepo.save(team);
            log.debug("saved team = {}", savedTeam);
            return new ResponseEntity<>(savedTeam, HttpStatus.CREATED);
        } catch (ConstraintViolationException ex) {
            log.error("Conflict error while saving team to the database", ex);
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            log.error("Error while saving team to database", ex);
            return ResponseEntity.internalServerError().build();
        }
    }
}
