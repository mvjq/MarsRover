package com.elo7.marsrover.model;

import com.elo7.marsrover.utils.NotValidPosition;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

//TODO: refactor to have entity classes and serialization classes
// so this jackson annotations and logic
// stay out of the representation of the model/table from the database
@Slf4j
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "plateau")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Plateau {

    @Id
    @GeneratedValue
    @Column(name = "plateau_id")
    private Integer id;

    @JsonUnwrapped
    private Point maxPoint;

    public Plateau (Point point) throws Exception {
        if (point.getX() <= 0 || point.getY() <0) {
            throw new Exception("Point cannot be negative or zero");
        }
        this.maxPoint = point;
    }

    //TODO: do a refactor on this boolean + throw exception
    // this logic is 'weak' because in some way i'm controlling the flow of the program
    // with the exception
    public Boolean isValidPointOnPlateau(Point point) throws NotValidPosition {
        if (point.getX() > maxPoint.getX() ||
                point.getY() > maxPoint.getY() ||
                point.getX() < -maxPoint.getX() ||
                point.getY() < -getMaxPoint().getY()) {
            throw new NotValidPosition("The Point {} are not valid for this plateau: " + this);
        }
        return true;
    }

    public boolean isSamePositionOnPlateau(Point point1, Point point2) {
        if (point1.getX() == point2.getX() && point1.getY() == point2.getY()) {
            log.info("A collision happened in position {}", point1);
            return true;
        }
         return false;
    }

    @Override
    public String toString() {
        return "Plateau{" +
                "id=" + id +
                ", maxPoint=" + maxPoint +
                '}';
    }
}
