package com.elo7.marsrover.web.controller.v1;

import com.elo7.marsrover.service.MarsRoverService;
import com.elo7.marsrover.web.controller.v1.request.PlanetRequest;
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
@RequestMapping("/v1/planet")
public class PlanetController {
    private final MarsRoverService marsRoverService;
    public PlanetController(MarsRoverService marsRoverService) {
        this.marsRoverService = marsRoverService;
    }

    @PostMapping
    public ResponseEntity<PlanetResponse> createPlanet(@RequestBody PlanetRequest request) {
        try {
            log.info("Receiving request {}", request);
            var saved = marsRoverService.savePlanet(request);
            log.info("Saved planet {}", saved);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (Exception ex)  {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{planetName}")
    public ResponseEntity<PlanetResponse> getPlanet(@PathVariable String planetName) {
        try {
            var found = marsRoverService.getPlanet(planetName);
            return new ResponseEntity<>(found, HttpStatus.FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<PlanetResponse>> getAllPlanets() {
        try {
            var founds = marsRoverService.getAllPlanets();
            log.info("Found planets {}", founds);
            return new ResponseEntity<>(founds, HttpStatus.FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{planetName}")
    public ResponseEntity<PlanetResponse> deletePlanet(@PathVariable String planetName) {
        try {
            log.info("Deleting planet: {}", planetName);
            var deleted = marsRoverService.deletePlanet(planetName);
            return new ResponseEntity<>(deleted, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
