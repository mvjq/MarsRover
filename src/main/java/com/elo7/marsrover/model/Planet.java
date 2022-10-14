package com.elo7.marsrover.model;

import com.elo7.marsrover.web.controller.v1.request.PlanetRequest;
import com.elo7.marsrover.web.controller.v1.response.PlanetResponse;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;

@Builder
@Getter
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "planet")
@NamedEntityGraph(name = "planet-rover-graph",
        attributeNodes = @NamedAttributeNode(value = "roversOnPlanet")
)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class Planet {

    @Id
    @GeneratedValue
    @Column(name = "planet_id")
    private int id;

    @Column(name = "planet_name", length = 99, nullable = false, unique = true)
    private String planetName;
    @OneToMany(mappedBy = "planet", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonUnwrapped
    private ArrayList<Rover> roversOnPlanet;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "plateau_id")
    private Plateau plateau;

    public static Planet getPlanetFromRequest(PlanetRequest request) throws Exception {
        return Planet.builder()
                .planetName(request.planetName())
                .roversOnPlanet(new ArrayList<>())
                .plateau(
                        new Plateau(
                                new Point(request.maxX(), request.maxY())))
                .build();
    }

    @JsonIgnore
    public PlanetResponse getResponse() {
        return new PlanetResponse(this);
    }

    public void landRover(Rover rover) throws Exception {
        log.info("Trying to land rover {} in planet {}", rover, this);
        if (isValidPointOnPlanet(rover.getCurrentPoint())) {
            log.info("The Rover position {} is valid for the plateau of planet", rover.getCurrentPoint());
            for(var r : roversOnPlanet) {
                if (rover.checkForCollisionWithOtherRover(r)) {
                    throw new Exception("Cannot land in this point on the Planet Plateau because there is another Rover in here");
                }
            }
            log.info("Rover {} landed in Planet {}", rover, this);

            rover.landedOn(this);
            this.roversOnPlanet.add(rover);
        }
    }

    public boolean isValidPointOnPlanet(Point point) {
        return this.plateau.isValidPointOnPlateau(point);
    }
}