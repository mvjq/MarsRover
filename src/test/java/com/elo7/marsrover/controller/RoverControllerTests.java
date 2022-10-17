package com.elo7.marsrover.controller;

import com.elo7.marsrover.model.Direction;
import com.elo7.marsrover.model.Planet;
import com.elo7.marsrover.model.Plateau;
import com.elo7.marsrover.model.Point;
import com.elo7.marsrover.model.Rover;
import com.elo7.marsrover.service.MarsRoverService;
import com.elo7.marsrover.web.controller.v1.RoverController;
import com.elo7.marsrover.web.controller.v1.request.RoverRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RoverController.class)
class RoverControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MarsRoverService marsRoverService;

    @Test
    void postRover_shouldReturnCreated() throws Exception {
        var request = new RoverRequest("Rover #1", "Mars", Direction.N, new Point(0, 0));
        var rover = Rover.getRoverFromRequest(request);
        rover.landedOn(createPlanet("Mars", 10, 10));
        given(marsRoverService.saveRover(any())).willReturn(rover.getResponse());

        mockMvc.perform(post("/v1/rover")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void getRoverByName() throws Exception {
        var request = new RoverRequest("Rover#1", "Mars", Direction.N, new Point(0, 0));
        var rover = Rover.getRoverFromRequest(request);
        rover.landedOn(createPlanet("Mars", 10, 10));
        given(marsRoverService.getRover("Rover #1")).willReturn(rover.getResponse());

        mockMvc.perform(get("/v1/rover/Rover#1"))
                .andExpect(status().isFound());
    }

    @Test
    void getAllRovers() throws Exception {
        var rovers = List.of(
                createRover("MarsRover", createPlanet("Mars", 10, 10), Direction.N, new Point(0, 0)),
                createRover("EarthRover", createPlanet("Earth", 10, 10), Direction.N, new Point(0, 0)),
                createRover("NeptuneRover", createPlanet("Neptune", 10, 10), Direction.N, new Point(0, 0))
        );

        var responses  = rovers.stream().map(Rover::getResponse).toList();
        given(marsRoverService.getAllRovers())
                .willReturn(responses);

        var response = mockMvc.perform(get("/v1/rover"))
                .andExpect(status().isFound());
    }

    @Test
    void deleteRover() throws Exception {
        var rover = createRover("MarsRover", createPlanet("Mars", 10, 10), Direction.N, new Point(0, 0));
        given(marsRoverService.deleteRover(rover.getRoverName()))
                .willReturn(rover.getResponse());

        mockMvc.perform(
                delete("/v1/rover/MarsRover")
        ).andExpect(status().isOk());
    }

    Rover createRover(String roverName, Planet planetName, Direction direction, Point point) {
        return Rover.builder()
                .roverName(roverName)
                .planet(planetName)
                .currentPoint(point)
                .currentDirection(direction)
                .build();
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
