package com.elo7.marsrover.model;

import java.util.List;

public class Planet {

    // TODO: mudar isso para hashmap para melhorar a busca
    private List<Rover> rovers;
    private Plateau plateau;

    /*
    Contem uma lista de probes que estao no planeta
    cada probe tem sua direction e sua coordenada
    (o prob nao tem ideia do planeta, assim como direction e coordenadas nao tem ideia
    do probe)
    e tem o planeta tem o plateau

    de logica:
    - Fica responsavel por checar se um probe faz colisao com os outros
    - se iss
    */

    public Planet(List<Rover> rovers, Plateau plateau) {
        this.rovers = rovers;
        this.plateau = plateau;
    }

    private boolean checkForCollision(Rover rover) {
        return false;
    }

    private boolean isValidPointForPlanet(Point point) throws Exception {
        if (point.getX() > plateau.getMaxX() || point.getY() > plateau.getMaxY()) {
            throw new Exception("Point is not valid for Plateau in this planet");
        }
        return false;
    }

    private void landRover(Rover rover) {
        /*
        checa se o probe eh correto (e transfere essa logica para o rover)
        verifica se o rover pode pousar (se ja existe um rover no ponto)
        e ai pousa ele.
         */
    }

    public void sendCommandsToRover() {}
}