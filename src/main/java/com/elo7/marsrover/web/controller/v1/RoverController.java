package com.elo7.marsrover.web.controller.v1;

import com.elo7.marsrover.model.Rover;
import com.elo7.marsrover.service.MarsRoverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/rover")
public class RoverController {

    private MarsRoverService marsRoverService;

    @PostMapping
    public ResponseEntity<Rover> createRover(@RequestBody Rover rover) {
        try {
            var saved = marsRoverService.saveRover(rover);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{roverId}")
    public ResponseEntity<Rover> getRover(@PathVariable int roverId) {
        try {
            var found = marsRoverService.getRover(roverId);
            return new ResponseEntity<>(found, HttpStatus.FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Rover>> getAllRover() {
        try {
            var founds = marsRoverService.getAllRovers();
            return new ResponseEntity<>(founds, HttpStatus.FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{roverId}")
    public ResponseEntity<HttpStatus> deleteRover(@PathVariable int roverId) {
        try {
           marsRoverService.deleteRover(roverId);
           return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/move/{roverId}")
    public void moveRover(@PathVariable MarsRoverRequest request) {}
}
