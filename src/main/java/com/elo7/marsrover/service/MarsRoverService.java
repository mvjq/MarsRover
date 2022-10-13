package com.elo7.marsrover.service;

import com.elo7.marsrover.model.Planet;
import com.elo7.marsrover.model.Rover;
import com.elo7.marsrover.repository.PlanetRepository;
import com.elo7.marsrover.repository.RoverRepository;
import com.elo7.marsrover.web.controller.v1.request.PlanetRequest;
import com.elo7.marsrover.web.controller.v1.request.RoverRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MarsRoverService {

    private PlanetRepository planetRepository;
    private RoverRepository roverRepository;

    //CRUDS
    public Planet savePlanet(PlanetRequest request) {
        var newPlanet = request.planet();
        log.info("Saving planet: {}", newPlanet);
        return planetRepository.saveAndFlush(newPlanet);
    }

    public Planet getPlanet(String planetName) throws Exception {
        var foundPlanet = planetRepository.findPlanetByPlanetName(planetName);
        return foundPlanet.orElseThrow(
                () -> new Exception("Planet not found"));
    }

    public List<Planet> getAllPlanets() {
        return planetRepository.findAll();
    }

    public void deletePlanet(String planetName) {
        planetRepository.deleteByPlanetName(planetName);
    }

    public Rover saveRover(RoverRequest request) throws Exception {
        try {
            var roverToLand = Rover.getRoverFromRequest(request);
            var planetToLand = getPlanet(request.planetName());
            planetToLand.landRover(roverToLand);
            log.info("Rover {} landed in Planet {}", roverToLand, planetToLand);
            planetRepository.saveAndFlush(planetToLand);
            return roverRepository.saveAndFlush(roverToLand);
        } catch (Exception err) {
            log.error("Exception: {} when landing and saving the rover", err);
            throw err;
        }
    }

    public Rover getRover(String roverName) throws Exception {
        var foundRover = roverRepository.findRoverByRoverName(roverName);
        return foundRover.orElseThrow(
                () -> new Exception("Rover not found"));
    }

    public List<Rover> getAllRovers() {
        return roverRepository.findAll();
    }

    public void deleteRover(String roverName) {
        roverRepository.deleteByRoverName(roverName);
    }

    public void sendCommandsToRover() {
        // get the planet which the rover wants to land
        // ask the planet the list of rovers in it  with the plateua
        // send this list of rovers and plateu to the rover
        // with the commands for him to do the collision and movement logic


    }

}
