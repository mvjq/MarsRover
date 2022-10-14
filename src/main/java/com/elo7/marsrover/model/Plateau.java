package com.elo7.marsrover.model;

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

@Slf4j
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "plateau")
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

    public Boolean isValidPointOnPlateau(Point point) {
        if (point.getX() > this.maxPoint.getX()|| point.getY() > this.maxPoint.getY()) {
            log.info("The Point {} are not valid for this plateau", this);
            return false;
        }
        return true;
    }
}
