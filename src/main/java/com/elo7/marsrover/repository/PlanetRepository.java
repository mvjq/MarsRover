package com.elo7.marsrover.repository;

import com.elo7.marsrover.model.Planet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanetRepository extends JpaRepository<Planet, Integer> {
    Optional<Planet> findPlanetByPlanetName(String planetName);
    void deleteByPlanetName(String planetName);
}
