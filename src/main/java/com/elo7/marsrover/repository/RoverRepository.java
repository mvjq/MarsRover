package com.elo7.marsrover.repository;

import com.elo7.marsrover.model.Rover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoverRepository extends JpaRepository<Rover, Integer> {
    Optional<Rover> findRoverByRoverName(String roverName);
    void deleteByRoverName(String rovername);
}
