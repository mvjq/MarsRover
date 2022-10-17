package com.elo7.marsrover.controller;


import com.elo7.marsrover.model.Planet;
import com.elo7.marsrover.model.Plateau;
import com.elo7.marsrover.model.Point;
import com.elo7.marsrover.service.MarsRoverService;
import com.elo7.marsrover.web.controller.v1.PlanetController;
import com.elo7.marsrover.web.controller.v1.request.PlanetRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PlanetController.class)
class PlanetControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MarsRoverService marsRoverService;

    @Test
    void postValidPlanet_shouldReturn200() throws Exception {

        var request = new PlanetRequest("Mars", 10, 10);

        mockMvc.perform(post("/v1/planet")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void postEmptyPlanet_shouldThrowClientError() throws Exception {
        mockMvc.perform(post("/v1/planet")
                        .contentType("application/json"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getPlanetByName_shouldReturnValidPlanet() throws Exception {
        var planet = createPlanet("Mars", 10, 10);

        given(marsRoverService.getPlanet(planet.getPlanetName()))
                .willReturn(planet.getResponse());

        mockMvc.perform(
                        get("/v1/planet/Mars"))
                .andExpect(status().isFound());
    }

    @Test
    void getAllPlants_shouldReturnListOfPlanets() throws Exception {
        var planets = List.of(
                createPlanet("Mars", 10, 10),
                createPlanet("Earth", 10, 10),
                createPlanet("Jupiter", 10, 10));


        given(marsRoverService.getAllPlanets())
                .willReturn(planets.stream().map(Planet::getResponse).toList());

        mockMvc.perform(
                        get("/v1/planet/Mars"))
                .andExpect(status().isFound());
    }

    @Test
    void deletePlanet_shouldReturnOk() throws Exception {
        var planet = createPlanet("Mars", 10, 10);
        given(marsRoverService.deletePlanet(planet.getPlanetName()))
                .willReturn(planet.getResponse());

        mockMvc.perform(
                        delete("/v1/planet/Mars"))
                .andExpect(status().isOk());
    }


    Planet createPlanet(String planetName, int x, int y) throws Exception {
        return Planet.builder()
                .planetName(planetName)
                .plateau(new Plateau(
                        new Point(x, y)))
                .roversOnPlanet(List.of())
                .build();
    }
}
