package ca.mcmaster.se2aa4.island.team217;

//import 3.4;

import ca.mcmaster.se2aa4.island.team217.MapRepresenter;
import ca.mcmaster.se2aa4.island.team217.Drone.Heading;
import ca.mcmaster.se2aa4.island.team217.Initializer;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.ArrayList;

public class GridSearcher {
    private final Logger logger = LogManager.getLogger();

    Drone drone;
    MapRepresenter map;

    int counter = 0;
    Boolean enoughSpaceToTurn = true;
    Boolean echoed = false;
    Boolean flyCheck = false;
    Boolean atEdge = false;
    Boolean initializeGridSearch = false;
    Boolean middle;
    Point initialLocation;
    Heading initialHeading; 

    // this is questionable
    Heading gridSearchDirection;

    public GridSearcher(Drone drone, MapRepresenter map) {
        this.drone = drone;
        this.map = map;
        gridSearchDirection = this.drone.initialHeading;
    }

    /*
     * scans through the whole island and will store all important POI's in the map
     * instanstiation
     */
    public String searchGrid(HashMap<String, List<String>> responseStorage) {

        if (initializeGridSearch == false) {
            initializeGridSearch = true;
            initialLocation = drone.currentLocation;
            initialHeading = drone.currentHeading;
            middle = drone.spawnedFacingGround;
        }
        logger.info("BATTERY {}", drone.getBatteryLevel());

        // This is a state in which the drone is above only water as a biome, but we
        // dont know if we are in the middle of the island or past it so, it checks that
        if (echoed) {
            logger.info("WE IN THE ECHOEED");
            logger.info("THIS IS THE STORAGE {} ", responseStorage.get("found").get(0));
            // The drone will have echoed and so now it will check to see if the echo is out
            // of range or if it needs to keep searching, this is done through activating
            // this next state of "atEdge"
            if (counter == 0 &&
                responseStorage.get("found").get(0).equals("OUT_OF_RANGE")) 
            {
                logger.info("WE SETTING atEdge!!!");
                atEdge = true;
            }
            // implement turning around method
            if (atEdge) {
                // The drone will continue to call the turnAroundGridSearch method until it has
                // completed its rotation and then will reset all elements for the next turn in
                // the future
                if (counter == 9) {
                    counter = 0;
                    gridSearchDirection = drone.currentHeading;
                    atEdge = false;
                    return drone.scan();
                } else {
                    logger.info("COUNTERRRR {} , ", counter);
                    //return turnAroundGridSearch();
                }
            }
            // if the turn is now complete or we detected water but it was not the edge of
            // the island, the drone will reset itself by doing a scan and fly and then
            // continue

            if (echoed && !atEdge && counter == 1) {
                if (responseStorage.get("found").get(0).equals("OUT_OF_RANGE")) {
                    logger.info("DONE");
                    return drone.stop();
                }
            }

            if (echoed && !atEdge && counter == 0) {
                logger.info("ECHO AFTER THE TURN TO DETERMINE IF WE ARE DONE");
                counter++;
                return drone.echo(drone.currentHeading);
            } else {
                logger.info("DOING A QUICK SCAN");
                echoed = false;
                counter = 0;
                flyCheck = false;
                return drone.fly();
            }

        }
        // If we are only on OCEAN as a biome, the drone will echo and set echoed to be
        // true, otherwise it will cycle between scanning and then flying forward
        if (!drone.currentLocation.getGround()) {
            echoed = !echoed;
            logger.info("WE BOUTTA ECHOOOOOOOOOOOOOOOOOOOOOO");
            logger.info("CURRENT HEADING {}", drone.currentHeading);
            return drone.echo(drone.currentHeading);
        } else if (!flyCheck) {
            flyCheck = true;
            echoed = false;
            return drone.scan();
        } else if (flyCheck) {
            flyCheck = false;
            echoed = false;
            return drone.fly();
        } else {
            // a pretty useless stop
            return drone.stop();
        }

    }

    private String normalTurnAroundGridSearch(){
        if (counter == 0){
            counter++;
            return drone.heading(drone.currentHeading.leftSide(drone.currentHeading));
        }
        else if (counter == 1){
            counter++;
            return drone.heading(drone.currentHeading.leftSide(drone.currentHeading));
        }
        return null;
    }
    private String turnAroundGridSearch() {
        logger.info("WE TURNING AROUNDDD");
        logger.info(gridSearchDirection);
        // Is the method to turn around using a counter with respect to the heading
        // changes.
        // fix it such that based on the previous echo, if there is enough room it
        // chooses the more cost effective turning method
        if (gridSearchDirection.equals(Heading.S) || gridSearchDirection.equals(Heading.W)) {
            // Only needs one spot above it turn
            if (counter == 0) {
                counter++;
                return drone.heading(drone.currentHeading.leftSide(drone.currentHeading));
            } else if (counter == 1) {
                counter++;
                return drone.fly();
            } else if (counter == 2) {
                counter++;
                return drone.fly();
            } else if (counter == 3) {
                counter++;
                return drone.fly();
            } else if (counter == 4) {
                counter++;
                return drone.heading(drone.currentHeading.leftSide(drone.currentHeading));
            } else if (counter == 5) {
                counter++;
                return drone.heading(drone.currentHeading.leftSide(drone.currentHeading));
            } else if (counter == 6) {
                counter++;
                return drone.heading(drone.currentHeading.leftSide(drone.currentHeading));
            } else if (counter == 7) {
                counter++;
                return drone.heading(drone.currentHeading.rightSide(drone.currentHeading));
            } else if (counter == 8) {
                counter++;
                return drone.heading(drone.currentHeading.rightSide(drone.currentHeading));
            }

            // needs 3 spaces above it to turn
            // if (counter == 0) {
            // counter++;
            // return drone.fly();
            // } else if (counter == 1) {
            // counter++;
            // return drone.fly();
            // } else if (counter == 2) {
            // counter++;
            // logger.info("COUNTER IS A 1 WE TURNIGN LEFTT");
            // logger.info(drone.currentHeading.leftSide(drone.currentHeading));
            // return drone.heading(drone.currentHeading.leftSide(drone.currentHeading));
            // } else if (counter == 3) {
            // counter++;
            // return drone.fly();
            // } else if (counter == 4) {
            // counter++;
            // return drone.heading(drone.currentHeading.leftSide(drone.currentHeading));
            // } else if (counter == 5) {
            // counter++;
            // return drone.heading(drone.currentHeading.leftSide(drone.currentHeading));
            // } else if (counter == 6) {
            // counter++;
            // return drone.heading(drone.currentHeading.rightSide(drone.currentHeading));
            // }
        } else if (gridSearchDirection.equals(Heading.N) || gridSearchDirection.equals(Heading.E)) {
            // needs one spot above it to turn
            if (counter == 0) {
                counter++;
                return drone.heading(drone.currentHeading.rightSide(drone.currentHeading));
            } else if (counter == 1) {
                counter++;
                return drone.fly();
            } else if (counter == 2) {
                counter++;
                return drone.fly();
            } else if (counter == 3) {
                counter++;
                return drone.fly();
            } else if (counter == 4) {
                counter++;
                return drone.heading(drone.currentHeading.rightSide(drone.currentHeading));
            } else if (counter == 5) {
                counter++;
                return drone.heading(drone.currentHeading.rightSide(drone.currentHeading));
            } else if (counter == 6) {
                counter++;
                return drone.heading(drone.currentHeading.rightSide(drone.currentHeading));
            } else if (counter == 7) {
                counter++;
                return drone.heading(drone.currentHeading.leftSide(drone.currentHeading));
            } else if (counter == 8) {
                counter++;
                return drone.heading(drone.currentHeading.leftSide(drone.currentHeading));
            }

            // needs 3 spaces above it to turn
            // if (counter == 0) {
            // counter++;
            // return drone.fly();
            // } else if (counter == 1) {
            // counter++;
            // return drone.fly();
            // } else if (counter == 2) {
            // counter++;
            // return drone.heading(drone.currentHeading.rightSide(drone.currentHeading));
            // } else if (counter == 3) {
            // counter++;
            // return drone.fly();
            // } else if (counter == 4) {
            // counter++;
            // return drone.heading(drone.currentHeading.rightSide(drone.currentHeading));
            // } else if (counter == 5) {
            // counter++;
            // return drone.heading(drone.currentHeading.rightSide(drone.currentHeading));
            // } else if (counter == 6) {
            // counter++;
            // return drone.heading(drone.currentHeading.leftSide(drone.currentHeading));
            // }
        }
        return null;

    }
}
