package com.elo7.marsrover.service;

import com.elo7.marsrover.model.Planet;
import com.elo7.marsrover.model.Rover;
import com.elo7.marsrover.repository.PlanetRepository;
import com.elo7.marsrover.repository.RoverRepository;
import com.elo7.marsrover.web.controller.v1.request.CommandRequest;
import com.elo7.marsrover.web.controller.v1.request.PlanetRequest;
import com.elo7.marsrover.web.controller.v1.request.RoverRequest;
import com.elo7.marsrover.web.controller.v1.response.PlanetResponse;
import com.elo7.marsrover.web.controller.v1.response.RoverResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
public class MarsRoverService {

    private final PlanetRepository planetRepository;

    private final RoverRepository roverRepository;

    public MarsRoverService(PlanetRepository planetRepository, RoverRepository roverRepository) {
        this.planetRepository = planetRepository;
        this.roverRepository = roverRepository;
    }

    //CRUDS
    @Transactional
    public PlanetResponse savePlanet(PlanetRequest request) throws Exception {
        var newPlanet =  Planet.getPlanetFromRequest(request);
        log.info("Saving planet: {}", newPlanet);
        return planetRepository.saveAndFlush(newPlanet).getResponse();
    }

    public PlanetResponse getPlanet(String planetName) throws Exception {
        var foundPlanet = planetRepository.findPlanetByPlanetName(planetName);
        log.info("Found Planet: {}", foundPlanet);
        return foundPlanet
                .map(Planet::getResponse)
                .orElseThrow(
                () -> new EntityNotFoundException("Planet not found"));
    }

    public List<PlanetResponse> getAllPlanets() {
        return planetRepository.findAll().stream().map(
                Planet::getResponse)
                .toList();
    }

    public PlanetResponse deletePlanet(String planetName) throws Exception {
        return planetRepository.findPlanetByPlanetName(planetName)
                .map( planet -> {
                    planetRepository.delete(planet);
                    return planet.getResponse();
                        }
                ).orElseThrow(
                        () -> new EntityNotFoundException("Planet not found ")
                );
    }

    @Transactional
    public RoverResponse saveRover(RoverRequest request) throws Exception {
        try {
            var roverToLand = Rover.getRoverFromRequest(request);
            var planetToLand = planetRepository.findPlanetByPlanetName(request.planetName()).get();
            roverToLand = roverRepository.findRoverByRoverName(roverToLand.getRoverName()).map(
                    rover -> {
                        rover.takeOff();
                        return rover;
                    }
            ).orElse(roverToLand);
            planetToLand.landRover(roverToLand);
            log.info("Rover Landed:  {}", roverToLand);
            return roverRepository.saveAndFlush(roverToLand).getResponse();
        } catch (Exception err) {
            log.error("Exception when landing and saving the rover: " + err);
            throw err;
        }
    }

    public RoverResponse getRover(String roverName) throws Exception {
        return roverRepository.findRoverByRoverName(roverName)
                .map(Rover::getResponse)
                .orElseThrow(() -> new EntityNotFoundException("Rover not found"));
    }

    public List<RoverResponse> getAllRovers() {
        return roverRepository.findAll()
                .stream().map(Rover::getResponse)
                .toList();
    }

    public RoverResponse deleteRover(String roverName) throws Exception {
        return roverRepository.findRoverByRoverName(roverName)
                .map( rover -> {
                    rover.takeOff();
                    roverRepository.delete(rover);
                    return rover.getResponse();
                })
                .orElseThrow(
                        () -> new EntityNotFoundException("planet not found")
                );
    }

    public RoverResponse sendCommandsToRover(String roverName, CommandRequest request) throws Exception {
        try {
            var foundRover = roverRepository.findRoverByRoverName(roverName).get();
            foundRover.executeCommands(request);
            return foundRover.getResponse();
        } catch (Exception ex) {
            log.info("Exception found when sending commands to rover: ", ex);
            throw ex;
        }
    }
}
