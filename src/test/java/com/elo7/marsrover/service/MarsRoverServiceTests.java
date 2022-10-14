package com.elo7.marsrover.service;

import com.elo7.marsrover.repository.PlanetRepository;
import com.elo7.marsrover.repository.RoverRepository;
import com.elo7.marsrover.web.controller.v1.request.PlanetRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
@Disabled
class MarsRoverServiceTests {

    @Mock
    private PlanetRepository planetRepository;
    @Mock
    private RoverRepository roverRepository;
    @InjectMocks
    private MarsRoverService marsRoverService;

    @Test
    void createValidPlanet_shouldSucceed() throws Exception {
        var request = new PlanetRequest("Mars", 10, 10);
        var response = marsRoverService.savePlanet(request);
        System.out.println("PlanetResponse: " + response);
        Assertions.assertEquals(request.planetName(), response.planet().getPlanetName());
    }

}
