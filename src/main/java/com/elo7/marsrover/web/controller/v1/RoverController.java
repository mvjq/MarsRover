package com.elo7.marsrover.web.controller.v1;

import com.elo7.marsrover.service.MarsRoverService;
import com.elo7.marsrover.web.controller.v1.request.CommandRequest;
import com.elo7.marsrover.web.controller.v1.request.RoverRequest;
import com.elo7.marsrover.web.controller.v1.response.RoverResponse;
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

    private final MarsRoverService marsRoverService;

    public RoverController(MarsRoverService marsRoverService) {
        this.marsRoverService = marsRoverService;
    }

    @PostMapping
    public ResponseEntity<RoverResponse> createRover(@RequestBody RoverRequest request) {
        try {
            // eu preciso ter um request o rover e o planeta ele quer posar
            log.info("Creating and landing the rover {}",request);
            var saved = marsRoverService.saveRover(request);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{roverName}")
    public ResponseEntity<RoverResponse> getRover(@PathVariable String roverName) {
        try {
            log.info("Getting rover {}", roverName);
            var found = marsRoverService.getRover(roverName);
            return new ResponseEntity<>(found, HttpStatus.FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<RoverResponse>> getAllRover() {
        try {
            var founds = marsRoverService.getAllRovers();
            return new ResponseEntity<>(founds, HttpStatus.FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{roverName}")
    public ResponseEntity<RoverResponse> deleteRover(@PathVariable String roverName) {
        try {
            log.info("Deleting rover {}", roverName);
           var deleted = marsRoverService.deleteRover(roverName);
           return new ResponseEntity<>(deleted, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/move/{roverName}")
    public ResponseEntity<RoverResponse> moveRover(@PathVariable String roverName, @RequestBody  CommandRequest request) {
        try {
            var rover = marsRoverService.sendCommandsToRover(roverName, request);
            return new ResponseEntity<>(rover, HttpStatus.OK);
        } catch (Exception ex) {
            log.info("Exception {} happened when trying to move the rover", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
