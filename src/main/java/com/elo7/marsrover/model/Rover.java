package com.elo7.marsrover.model;

import com.elo7.marsrover.web.controller.v1.request.CommandRequest;
import com.elo7.marsrover.web.controller.v1.request.RoverRequest;
import com.elo7.marsrover.web.controller.v1.response.RoverResponse;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Entity(name = "rover")
public class Rover {

    @Id
    @GeneratedValue
    @Column(name = "rover_id")
    private int id;

    @Column(name = "rover_name", nullable = false, unique = true)
    private String roverName;

    @Column(name = "current_direction", nullable = false)
    private Direction currentDirection;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "id", nullable = false)
    @JsonIgnore
    private Planet planet;

    private Point currentPoint;

    public Point getCurrentPoint() {
        return this.currentPoint;
    }

    public void landedOn(Planet planet) {
        this.planet = planet;
    }

    public static Rover getRoverFromRequest(RoverRequest request) {
        return Rover.builder()
                .roverName(request.roverName())
                .currentDirection(request.landingDirection())
                .planet(null)
                .currentPoint(request.landingPoint())
                .build();
    }

    @JsonIgnore
    public RoverResponse getResponse() {
        return new RoverResponse(this, this.planet.getPlanetName());
    }

    public boolean checkForCollisionWithNewRover(Rover roverLanded) {
        var pointLanded = this.currentPoint;
        var newPoint = roverLanded.getCurrentPoint();
        if (checkIfSamePositionOnPlateau(pointLanded, newPoint))  {
            return true;
        }
        return false;
    }


    public void executeCommands(CommandRequest request) throws Exception {
        var commands = request.commands();
        log.info("Executing commands {} on rover {} on planet {}", commands, this, this.planet.getPlanetName());
        for (Command command : commands) {
            switch (command) {
                case M -> move();
                case L -> this.currentDirection = this.currentDirection.getLeft();
                case R -> this.currentDirection = this.currentDirection.getRight();
                default -> {
                    log.info("Invalid rover commands {}", command);
                    throw new Exception("Invalid rover command");
                }
            }
        }
    }

    //TODO: (big) refactor this logic to be more clean
    // and break it in smaller and reusable functions

    public void move() throws Exception {
        log.info("Trying to move rover {}", this);
        var newPoint = this.currentPoint.moveNewPoint(this.currentDirection.getMovePoint());
        if (this.planet.isValidPointOnPlanet(newPoint)) {
            for(var r: this.planet.getRoversOnPlanet()) {
                if (checkIfSamePositionOnPlateau(r.getCurrentPoint(), newPoint)) {
                    throw new Exception("Cannot move on this point on the plateau because there is another Rover in here");
                }
            }
            log.info("Moving rover to point {}", newPoint);
            this.currentPoint = newPoint;
        }
    }

    private boolean checkIfSamePositionOnPlateau(Point pointLanded, Point newPointRover) {
        if (pointLanded.getX() == newPointRover.getX() && pointLanded.getY() == newPointRover.getY()) {
            log.info("A collision happened in position {}", pointLanded);
            return true;
        }
        return false;
    }


    @Override
    public String toString() {
            String p = (planet != null) ? planet.getPlanetName() : "";
        return "Rover{" +
                "id=" + id +
                ", roverName='" + roverName + '\'' +
                ", currentDirection=" + currentDirection +
                ", planet=" + p +
                ", currentPoint=" + currentPoint +
                '}';
    }
}
