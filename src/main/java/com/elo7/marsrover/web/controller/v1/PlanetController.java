package com.elo7.marsrover.web.controller.v1;

import com.elo7.marsrover.model.Planet;
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
@RequestMapping("/v1/planet")
public class PlanetController {

    /*
    Aqui eu sou vou adicionar informacoes sobre
    o planeta, nada mais que isso
    Eu vou deixar o ProbeController para receber comandos
    e cadastrar novas probes
    */
    private MarsRoverService marsRoverService;
    @PostMapping
    public ResponseEntity<Planet> createPlanet(@RequestBody Planet planet) {
        try {
            var saved = marsRoverService.savePlanet(planet);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (Exception ex)  {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{planetId}")
    public ResponseEntity<Planet> getPlanet(@PathVariable int planetId) {
        try {
            var found = marsRoverService.getPlanet(planetId);
            return new ResponseEntity<>(found, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<Planet>> getAllPlanets() {
        try {
            var founds = marsRoverService.getAllPlanets();
            return new ResponseEntity<>(founds, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{planetId}")
    public ResponseEntity<HttpStatus> deletePlanet(@PathVariable int planetId) {
        try {
            marsRoverService.deletePlanet(planetId);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }
}
