package com.elo7.marsrover.model;

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
    public Boolean isValidPointOnPlateau(Point point) throws Exception {
        if (point.getX() > this.maxPoint.getX() ||
                point.getY() > this.maxPoint.getY() ||
                point.getX() < -this.maxPoint.getX() ||
                point.getY() < -this.getMaxPoint().getY()) {
            throw new Exception("The Point {} are not valid for this plateau: " + this);
        }
        return true;
    }

    @Override
    public String toString() {
        return "Plateau{" +
                "id=" + id +
                ", maxPoint=" + maxPoint +
                '}';
    }
}
