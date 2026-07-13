package org.bnbalint.snackdaddy.controllers;

import org.bnbalint.snackdaddy.models.Level;
import org.bnbalint.snackdaddy.models.Rink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LevelController {

    //-------------------------------------------
    // Constants
    //
    private static final String JSON = "application/json";
    private final Logger log = LoggerFactory.getLogger(LevelController.class);


    /**
     * Get all levels (from the enum)
     */
    @RequestMapping(
            value = "/levels",
            method = RequestMethod.GET,
            produces = JSON
    )
    public ResponseEntity<List<Level>> getAllLevels() {
        log.trace("getAllLevels");

        List<Level> levels = List.of(Level.values());
        log.debug("Found levels = {}", levels);
        return ResponseEntity.ok(levels);
    }
}
