package com.elo7.marsrover.service;

import com.elo7.marsrover.model.Direction;
import com.elo7.marsrover.model.Planet;
import com.elo7.marsrover.model.Plateau;
import com.elo7.marsrover.model.Point;
import com.elo7.marsrover.repository.PlanetRepository;
import com.elo7.marsrover.repository.RoverRepository;
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

    /*
    CRUD planet
     */
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

    /*
    ALem dos testss acima eh necssario:
    - cadastrar rover sem planeta -> erro
    - cadastrar rover com planeta certo OK
    - cadastrar rover com posicao invalida no plateau -> erro
    - cadastrar rover #1 e cadastrar rover #2 na mesma posicao ->
    - cadastrar rover #1 e enviar os 3 comandos pra ele (L, R, M);
    - cadastrar rover #1 e enviar os 3 comandos para ele (L, R, M) ate que ele saia do plateau
    - cadastrar rover #1 e #2 e enviar comandos para o #1 andar ate colidir com o rover #2
    - TODO: tirar um rover de um planeta para o outro (POST no createRover)
     */
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

    private List<Planet> persistPlanets() throws Exception {
          return Stream.of(
                Planet.builder()
                        .planetName("Mars")
                        .roversOnPlanet(new ArrayList<>())
                        .plateau(
                                new Plateau(
                                        new Point(10, 10)
                                )
                        )
                        .build(),
                Planet.builder()
                        .planetName("Jupiter")
                        .roversOnPlanet(new ArrayList<>())
                        .plateau(
                                new Plateau(
                                        new Point(20, 20)
                                )
                        )
                        .build(),
                Planet.builder()
                        .planetName("Neptune")
                        .roversOnPlanet(new ArrayList<>())
                        .plateau(
                                new Plateau(
                                        new Point(15, 15)
                                )
                        )
                        .build()
                ).map(
                        planet ->
                                entityManager.persist(planet)
        ).toList();
    }


}
