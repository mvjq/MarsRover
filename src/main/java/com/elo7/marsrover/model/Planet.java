package com.elo7.marsrover.model;

import com.elo7.marsrover.utils.NotValidPosition;
import com.elo7.marsrover.web.controller.v1.request.PlanetRequest;
import com.elo7.marsrover.web.controller.v1.response.PlanetResponse;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "planet")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
    private List<Rover> roversOnPlanet;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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


    //TODO: refactor this and centralize in one place (Rover is better,
    // with a semantic of Rover.landsOn(Planet) or something like that
    public void landRover(Rover rover) throws Exception {
        log.info("Trying to land rover {} in planet {}", rover, this);
        rover.landedOn(this);
        if(rover.canMoveOnPlanet(rover.getCurrentPoint())) {
            roversOnPlanet.add(rover);
            log.info("Rover {} landed in Planet {}", rover, this);
        } else {
            throw new Exception("Cannot land in this point on the Plateau because there is another Rover in here");
        }
    }

    public boolean isValidPointOnPlanet(Point point) throws NotValidPosition {
        return plateau.isValidPointOnPlateau(point);
    }

    public boolean checkIfSamePositionOnPlanet(Point pointLanded, Point newPoint) {
        return getPlateau().isSamePositionOnPlateau(pointLanded, newPoint);
    }

    @Override
    public String toString() {
        return "Planet{" +
                "id=" + id +
                ", planetName='" + planetName + '\'' +
                ", roversOnPlanet=" + roversOnPlanet +
                ", plateau=" + plateau +
                '}';
    }
}