package com.elo7.marsrover.repository;

import com.elo7.marsrover.model.Planet;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface PlanetRepository extends JpaRepository<Planet, Integer> {
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH,
            value =  "planet-rover-graph")
    Optional<Planet> findPlanetByPlanetName(String planetName);
}
