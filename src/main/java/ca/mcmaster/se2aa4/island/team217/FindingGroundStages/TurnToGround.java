package ca.mcmaster.se2aa4.island.team217.FindingGroundStages;

import ca.mcmaster.se2aa4.island.team217.*;
import ca.mcmaster.se2aa4.island.team217.MapRepresentation.*;

public class TurnToGround implements Phase {

    boolean reachedEnd = false;
    int counter = 0;
    String sideToTurn;

    MapInitializer mapInitializer;

    public TurnToGround(MapInitializer mapInitializer) {
        this.mapInitializer = mapInitializer;
    }

    public Boolean reachedEnd() {
        return reachedEnd;
    }

    public Phase getNextPhase() {
        return new FlyToGround(mapInitializer);
    }

    public Boolean isFinal() {
        return false;
    }

    public String nextDecision(Drone drone, MapRepresenter map) {
        if (counter == 0) {
            if (mapInitializer.directionToEcho == drone.getCurrentHeading().leftSide()) {
                sideToTurn = "left";
            } else {
                sideToTurn = "right";

            }
        }
        if (counter == 7) {
            reachedEnd = true;
            mapInitializer.distanceToGround -= 2;
            return null;
        }
        return turnToGround(sideToTurn, drone);
    }

    private String turnToGround(String sideToTurn, Drone drone) {
        if (sideToTurn.equals("left")) {
            // Only needs one spot above it turn
            if (counter == 0) {
                counter++;
                return drone.heading(drone.getCurrentHeading().leftSide());
            } else if (counter == 1) {
                counter++;
                return drone.fly();
            } else if (counter == 2) {
                counter++;
                return drone.heading(drone.getCurrentHeading().leftSide());
            } else if (counter == 3) {
                counter++;
                return drone.fly();
            } else if (counter == 4) {
                counter++;
                return drone.heading(drone.getCurrentHeading().leftSide());
            } else if (counter == 5) {
                counter++;
                return drone.heading(drone.getCurrentHeading().leftSide());
            } else if (counter == 6) {
                counter++;
                return drone.heading(drone.getCurrentHeading().leftSide());
            }
        } else if (sideToTurn.equals("right")) {
            // needs one spot above it to turn
            if (counter == 0) {
                counter++;
                return drone.heading(drone.getCurrentHeading().rightSide());
            } else if (counter == 1) {
                counter++;
                return drone.fly();
            } else if (counter == 2) {
                counter++;
                return drone.heading(drone.getCurrentHeading().rightSide());
            } else if (counter == 3) {
                counter++;
                return drone.fly();
            } else if (counter == 4) {
                counter++;
                return drone.heading(drone.getCurrentHeading().rightSide());
            } else if (counter == 5) {
                counter++;
                return drone.heading(drone.getCurrentHeading().rightSide());
            } else if (counter == 6) {
                counter++;
                return drone.heading(drone.getCurrentHeading().rightSide());
            }
        }
        return null;
    }
}
