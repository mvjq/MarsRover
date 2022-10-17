package com.elo7.marsrover.service;

import com.elo7.marsrover.model.Command;
import com.elo7.marsrover.model.Direction;
import com.elo7.marsrover.model.Planet;
import com.elo7.marsrover.model.Plateau;
import com.elo7.marsrover.model.Point;
import com.elo7.marsrover.model.Rover;
import com.elo7.marsrover.repository.PlanetRepository;
import com.elo7.marsrover.repository.RoverRepository;
import com.elo7.marsrover.web.controller.v1.request.CommandRequest;
import com.elo7.marsrover.web.controller.v1.request.PlanetRequest;
import com.elo7.marsrover.web.controller.v1.request.RoverRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

@RunWith(SpringRunner.class)
@DataJpaTest
class MarsRoverServiceTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    PlanetRepository planetRepository;

    @Autowired
    RoverRepository roverRepository;

    MarsRoverService marsRoverService;

    @BeforeEach
    public void init() {
        marsRoverService = new MarsRoverService(
                planetRepository,
                roverRepository
        );
    }


    //TODO: refactor this tests in smaller and reusable functions
    // and reusing constants with @ParametrizedValue
    @Test
    void shouldFoundEmptyPlanetsonEmptyDatabase() {
        var planets = marsRoverService.getAllPlanets();
        assertThat(planets).isEmpty();
    }
    @Test
    void shouldPostValidPlanetandGetFromDatabase() throws Exception {
        var request = new PlanetRequest("Mars", 10, 10);
        var response = marsRoverService.savePlanet(request);
        assertThat(response).isNotNull();
    }

    @Test
    void getAllPlanetsfromDatabase() throws Exception {
        persistPlanets();
        var responses = marsRoverService.getAllPlanets();
        assertThat(responses).isNotEmpty();
        assertThat(responses.size()).isEqualTo(3);
    }

    @Test
    void getPlanetByNameShouldThrow() throws Exception {
        assertThrows(Exception.class , () -> marsRoverService.getRover("Mars"));
    }

    @Test
    void getPlanetByName() throws Exception {
        persistPlanets();
        var response = marsRoverService.getPlanet("Mars");
        assertThat(response).isNotNull();
    }

    @Test
    void shouldThrowInvalidPlanetWithZeroSizePlateau() throws Exception {
        var request = new PlanetRequest("Mars", 0, 0);
        assertThrows(Exception.class, () -> marsRoverService.savePlanet(request));
    }

    @Test
    void shouldThrowInvalidPlanetWithNegativeSizePlateau() {
        var request = new PlanetRequest("Mars", -1, -1);
        assertThrows(Exception.class, () -> marsRoverService.savePlanet(request));
    }

    @Test
    void shouldDelelePlanetFromDatabase() throws Exception {
        var request = new PlanetRequest("Mars", 10, 10);
        entityManager.persist(Planet.getPlanetFromRequest(request));
        marsRoverService.deletePlanet(request.planetName());
        var planets = marsRoverService.getAllPlanets();
        assertThat(planets).isEmpty();
        System.out.println(planets);
    }

    @Test
    void shouldThrowWhenDeletedInexistPlanetFromDatabase() {
        var request = new PlanetRequest("Mars", 10, 10);
        assertThrows(Exception.class, () -> marsRoverService.deletePlanet(request.planetName()));
    }


    // Rover
    @Test
    void shouldFoundEmptyRovers_onEmptyDatabase() {
        var rovers = marsRoverService.getAllRovers();
        assertThat(rovers).isEmpty();
    }

    @Test
    void shouldPostValidRoverAndGetFromDatabase() throws Exception {
        persistPlanets();
        var request = new RoverRequest("MarsRover", "Mars", Direction.N, new Point(0, 0));
        var response = marsRoverService.saveRover(request);
        System.out.println("Response: " + response);
        assertThat(response).isNotNull();

    }

    @Test
    void getAllRoversFromDatabase() throws Exception {
        var planets = persistPlanets();
        persistRovers(planets);
        var response = marsRoverService.getAllRovers();
        System.out.println("Response: " + response);
        assertThat(response).hasSize(3);
    }

    @Test
    void getRoverByNameFromDatabase() throws Exception {
        var planets = persistPlanets();
        var rovers = persistRovers(planets);
        var response = marsRoverService.getRover("MarsRover");
        assertThat(response).isNotNull();
    }

    @Test
    void getInexistentRoverByNameFromDatabaseShouldThrow() throws Exception {
        var planets = persistPlanets();
        var rovers = persistRovers(planets);
        assertThrows(Exception.class, () -> marsRoverService.getRover("MarsRover#1"));
    }

    @Test
    void landRoverOnInexistentPlanet_shouldThrow() throws Exception {
        var planets = persistPlanets();
        var request = new RoverRequest("Rover", "Sun", Direction.N, new Point(0, 0));
        assertThrows(Exception.class, () -> marsRoverService.saveRover(request));
    }

    @Test
    void landRoverOnInexistentPointOnPlanet_shouldThrow() throws Exception {

        entityManager.persistAndFlush(getPlanet("Mars", 10));
        var request = new RoverRequest("Rover", "Sun", Direction.N, new Point(11, 11));
        assertThrows(Exception.class,
                () -> marsRoverService.saveRover(request));
    }

    @Test
    void landRoverOnOtherRover_shouldThrow() throws Exception {
        persistPlanets();
        // rover #1
        var request1 = new RoverRequest("MarsRover #1", "Mars", Direction.N, new Point(0, 0));
        var response1 = marsRoverService.saveRover(request1);
        assertThat(response1).isNotNull();

        var request2 = new RoverRequest("MarsRover #2", "Mars", Direction.N, new Point(0, 0));
        assertThrows(Exception.class,
                () -> marsRoverService.saveRover(request2));
    }
    @Test
    void sendMoveCommandToRoverDirectionNorth_shouldSucceed() throws Exception {
        createAndCheckPlanet("Mars", 10, 10);
        createAndCheckRover("MarsRover", "Mars", Direction.N, 0, 0);
        var command = new CommandRequest(List.of(Command.M));
        var responseCommand = marsRoverService.sendCommandsToRover("MarsRover", command);
        assertNotNull(responseCommand);
        assertThat(responseCommand.rover().getCurrentPoint().getX()).isZero();
        assertThat(responseCommand.rover().getCurrentPoint().getY()).isEqualTo(1);
    }


    @Test
    void sendMoveCommandToRoverDirectionSouth_shouldPositionNegativeY() throws Exception {
        createAndCheckPlanet("Mars", 10, 10);
        createAndCheckRover("MarsRover", "Mars", Direction.S, 0, 0);
        var command = new CommandRequest(List.of(Command.M));
        var responseCommand = marsRoverService.sendCommandsToRover("MarsRover", command);
        assertThat(responseCommand.rover().getCurrentPoint().getX()).isZero();
        assertThat(responseCommand.rover().getCurrentPoint().getY()).isEqualTo(-1);
    }

    @Test
    void sendMoveCommandToRoverDirectionEast_shouldPositionPositiveX() throws Exception {
        createAndCheckPlanet("Mars", 10, 10);
        createAndCheckRover("MarsRover", "Mars", Direction.E, 0, 0);
        sendCommandAndAssertPositionIsValid("MarsRover",1);
    }

    @Test
    void sendMoveCommandToRoverDirectionWest_shouldPositionNegativeX() throws Exception {
        createAndCheckPlanet("Mars", 10, 10);
        createAndCheckRover("MarsRover", "Mars", Direction.W, 0, 0);
        sendCommandAndAssertPositionIsValid("MarsRover", -1);
    }

    private void sendCommandAndAssertPositionIsValid(String roverName, int expected) throws Exception {
        var command = new CommandRequest(List.of(Command.M));
        var responseCommand = marsRoverService.sendCommandsToRover(roverName, command);
        assertThat(responseCommand.rover().getCurrentPoint().getX()).isEqualTo(expected);
        assertThat(responseCommand.rover().getCurrentPoint().getY()).isZero();
    }

    @Test
    void shouldMoveNorthCommandToRoverTillOutsideOfPlateau_shouldThrow() throws Exception {
        createAndCheckPlanet("Mars", 2, 2);
        createAndCheckRover("MarsRover #1", "Mars", Direction.N, 0, 0);
        sendCommandAndThrows(List.of(Command.M, Command.M, Command.M, Command.M, Command.M));
    }

    @Test
    void shouldMoveSouthCommandToRoverTillOutsideOfPlateau_shouldThrow() throws Exception {
        createAndCheckPlanet("Mars", 2, 2);
        createAndCheckRover("MarsRover #1", "Mars", Direction.S, 0, 0);
        sendCommandAndThrows(List.of(Command.M, Command.M, Command.M, Command.M, Command.M));
    }

    @Test
    void shouldMoveEastCommandToRoverTillOutsideOfPlateayu_shouldThrow() throws Exception {
        createAndCheckPlanet("Mars", 2, 2);
        createAndCheckRover("MarsRover #1", "Mars", Direction.E, 0, 0);
        sendCommandAndThrows(List.of(Command.M, Command.M, Command.M, Command.M, Command.M));
    }

    @Test
    void shouldMoveWestCommandToRoverTillOutsideOfPlateau_shouldThrow() throws Exception {
        createAndCheckPlanet("Mars", 2, 2);
        createAndCheckRover("MarsRover #1", "Mars", Direction.W, 0, 0);
        sendCommandAndThrows(List.of(Command.M, Command.M, Command.M, Command.M, Command.M));
    }


    @Test
    void spinRoverOnPosition_shouldSucceed() throws Exception {
        createAndCheckPlanet("Mars", 10, 10);
        createAndCheckRover("MarsRover #1", "Mars", Direction.N, 0, 0);
        var command = new CommandRequest(List.of(Command.L, Command.L, Command.L, Command.L));
        var response =  marsRoverService.sendCommandsToRover("MarsRover #1", command);
        assertNotNull(response);
    }


    @Test
    void sendMoveCommandToRoverDirectionNorthCollidedWithAnotherRover_shouldThrow() throws Exception {
        createAndCheckPlanet("Mars", 10, 10);
        createAndCheckRover("MarsRover #1", "Mars", Direction.N, 0, 0);
        createAndCheckRover("MarsRover #2", "Mars", Direction.N, 0, 1);
        sendCommandAndThrows(List.of(Command.M));
    }

    @Test
    void sendMoveCommandToRoverDirectionSouthCollidedWithAnotherRover_shouldThrow() throws Exception {
        createAndCheckPlanet("Mars", 10, 10);
        createAndCheckRover("MarsRover #1", "Mars", Direction.S, 0, 0);
        createAndCheckRover("MarsRover #2", "Mars", Direction.S, 0, -1);
        sendCommandAndThrows(List.of(Command.M));
    }

    @Test
    void sendMoveCommandToRoverDirectionEastCollidedWithAnotherRover_shouldThrow() throws Exception {
        createAndCheckPlanet("Mars", 10, 10);
        createAndCheckRover("MarsRover #1", "Mars", Direction.E, 0, 0);
        createAndCheckRover("MarsRover #2", "Mars", Direction.E, 1, 0);
        sendCommandAndThrows(List.of(Command.M));
    }

    @Test
    void sendMoveCommandToRoverDirectionWestCollidedWithAnotherRover_shouldThrow() throws Exception {
        createAndCheckPlanet("Mars", 10, 10);
        createAndCheckRover("MarsRover #1", "Mars", Direction.W, 0, 0);
        createAndCheckRover("MarsRover #2", "Mars", Direction.W, -1, 0);
        sendCommandAndThrows(List.of(Command.M));
    }

    private void sendCommandAndThrows(List<Command> M) {
        var command = new CommandRequest(M);
        assertThrows(Exception.class, () -> marsRoverService.sendCommandsToRover("MarsRover #1", command));
    }


    private List<Planet> persistPlanets() throws Exception {
          return Stream.of(
                          getPlanet("Mars", 10),
                getPlanet("Jupiter", 20),
                getPlanet("Neptune", 15))
                  .map(
                          planet ->
                                  entityManager.persistAndFlush(planet)
                  ).toList();
    }

    private static Planet getPlanet(String Mars, int x) throws Exception {
        return Planet.builder()
                .planetName(Mars)
                .roversOnPlanet(new ArrayList<>())
                .plateau(
                        new Plateau(
                                new Point(x, x)
                        )
                ).build();
    }

    private List<Rover> persistRovers(List<Planet> planets) throws Exception {
        var rovers = Stream.of(
                        getRover("MarsRover", planets, 0),
                        getRover("JupiterRover", planets, 1),
                        getRover("NeptuneRover", planets, 2)
                ).map(rover -> entityManager.persistAndFlush(rover))
                .toList();

        planets.forEach(
                planet -> {
                    rovers.forEach(rover ->
                            planet.getRoversOnPlanet().add(rover)
                    );
                    entityManager.persistAndFlush(planet);
                }
        );
        return rovers;
    }

    private static Rover getRover(String MarsRover, List<Planet> planets, int i) {
        return Rover.builder()
                .roverName(MarsRover)
                .currentDirection(Direction.N)
                .currentPoint(new Point(0, 0))
                .planet(planets.get(i))
                .build();
    }

    private void createAndCheckPlanet(String planetName, int x, int y) throws Exception {
        var planet = new PlanetRequest(planetName, x, y);
        var responsePlanet = marsRoverService.savePlanet(planet);
        assertNotNull(responsePlanet);
        var getPlanetResponse = marsRoverService.getPlanet(planetName);
        assertNotNull(getPlanetResponse);
    }

    private void createAndCheckRover(String roverName, String planetName, Direction direction, int x, int y) throws Exception {
        var rover = new RoverRequest(roverName, planetName, direction, new Point(x, y));
        var responseRover = marsRoverService.saveRover(rover);
        assertNotNull(responseRover);
        var getRoveResponse = marsRoverService.getRover(roverName);
        assertNotNull(getRoveResponse);
    }
}
