package org.bnbalint.snackdaddy.controllers;

import org.bnbalint.snackdaddy.models.User;
import org.bnbalint.snackdaddy.repositories.UserRepository;
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
public class UserController {

    //-------------------------------------------
    // Constants
    //
    private static final String JSON = "application/json";
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    //-------------------------------------------
    // Single Autowired Constructor
    //
    private final UserRepository userRepo;
    public UserController(UserRepository userRepository){
        this.userRepo = userRepository;
    }


    /**
     * Get all users
     * @return the full list of users from the database
     */
    @RequestMapping(
            value = "/users",
            method = RequestMethod.GET,
            produces = JSON
    )
    public ResponseEntity<List<User>> getAllUsers() {
        log.trace("getAllUsers");

        try {
            List<User> users = userRepo.findAll();
            log.debug("Found users = {}", users);
            return ResponseEntity.ok(users);
        } catch (Exception ex) {
            log.error("Error while querying DB for all users", ex);
            return ResponseEntity.internalServerError().build();
        }
    }


    /**
     * Add a new user to the database
     * @param user - the user to add
     * @return the added user
     */
    @RequestMapping(
            value = "/users",
            method = RequestMethod.POST,
            consumes = JSON,
            produces = JSON
    )
    public ResponseEntity<User> addUser(@RequestBody User user) {
        log.trace("addUser - user = {}", user);

        try {
            User savedUser = userRepo.save(user);
            log.debug("saved user = {}", savedUser);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (OptimisticLockingFailureException ex) {
            log.error("Conflict error while saving user to the database", ex);
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            log.error("Error while saving user to database", ex);
            return ResponseEntity.internalServerError().build();
        }
    }
}
