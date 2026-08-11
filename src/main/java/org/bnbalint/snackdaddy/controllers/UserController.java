package org.bnbalint.snackdaddy.controllers;

import org.bnbalint.snackdaddy.models.User;
import org.bnbalint.snackdaddy.repositories.UserRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    /**
     * Get single user by userId
     * @return
     *  400 if the userId is null
     *  400 if the userId is less than or equal to 0
     *  404 if the user is not found
     *  500 for all other DB related errors
     *  200 and the user when successful
     */
    @RequestMapping(
            value = "/users/{userId}",
            method = RequestMethod.GET,
            produces = JSON
    )
    public ResponseEntity<?> getUserById(@PathVariable("userId") Long userId) {
        log.trace("getUserById with userId = {}", userId);

        if (userId == null) {
            log.error("UserId cannot be null");
            return ResponseEntity.badRequest().body("UserId cannot be null");
        }

        if (userId <= 0) {
            log.error("UserId must be greater than 0");
            return ResponseEntity.badRequest().body("UserId must be greater than 0");
        }

        try {
            User user = userRepo.findById(userId).orElse(null);

            if (user == null){
                return ResponseEntity.notFound().build();
            }
            log.debug("Found user = {}", user);
            return ResponseEntity.ok(user);
        } catch (Exception ex) {
            log.error("Error while querying DB for user", ex);
            return ResponseEntity.internalServerError().build();
        }
    }



    /**
     * Update a user
     * @param user - the user to update
     * @return
     *  400 if the user is null
     *  400 if the userId is 0
     *  400 if there is a database conflict
     *  500 for all other DB related errors
     *  200 and the user when successful
     */
    @RequestMapping(
            value = "/users",
            method = RequestMethod.PUT,
            consumes = JSON,
            produces = JSON
    )
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        log.trace("updateUser - user = {}", user);

        if (user == null) {
            log.error("User must not be null");
            return ResponseEntity.badRequest().build();
        }

        if (user.getId() == 0){
            log.error("User must have a valid ID to be updated");
            return ResponseEntity.badRequest().build();
        }

        try {
            User savedUser = userRepo.save(user);
            log.debug("updated user = {}", savedUser);
            return new ResponseEntity<>(savedUser, HttpStatus.OK);
        } catch (OptimisticLockingFailureException ex) {
            log.error("Conflict error while saving user to the database", ex);
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            log.error("Error while saving user to database", ex);
            return ResponseEntity.internalServerError().build();
        }
    }
}
