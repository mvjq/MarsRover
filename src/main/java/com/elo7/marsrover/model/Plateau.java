package com.elo7.marsrover.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Slf4j
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "plateau")
public class Plateau {

    @Id
    @GeneratedValue
    @Column(name = "plateau_id")
    private Integer id;
    private Point maxPoint;

    public int getMaxX() {
        return maxPoint.getX();
    }

    public int getMaxY() {
        return maxPoint.getY();
    }

    public Plateau (Point point) throws Exception {
        if (point.getX() <= 0 || point.getY() <0) {
            throw new Exception("Point cannot be negative or zero");
        }
        this.maxPoint = point;
    }

    public Boolean isValidPointOnPlateau(Point point) {
        if (point.getX() > this.getMaxX() || point.getY() > this.getMaxY()) {
            log.info("The Point {} are not valid for this plateau", this);
            return false;
        }
        return true;
    }
}
