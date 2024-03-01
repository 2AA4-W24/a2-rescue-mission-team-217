package ca.mcmaster.se2aa4.island.team217;
import ca.mcmaster.se2aa4.island.team217.Point;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Drone {

    private final Logger logger = LogManager.getLogger(); 

    private Integer batteryLevel;
    Point currentLocation; 
    public Heading currentHeading;
    public final Heading initialHeading;

    public MapRepresenter mapRepresenter;

    //parameters of the next decision
    private String action;
    private Heading direction;

    public enum Heading {
        N, E, S, W;

        // methods to quickly find direction on left, right and back sides
        public Heading leftSide(Heading currentHeading) {
            switch (currentHeading) {
                case N:
                    return W;
                case E:
                    return N;
                case S:
                    return E;
                case W:
                    return S;
                default:
                    throw new NullPointerException("Invalid heading: " + currentHeading);
            }
        }
    
        public Heading rightSide(Heading currentHeading) {
            switch (currentHeading) {
                case N:
                    return E;
                case E:
                    return S;
                case S:
                    return W;
                case W:
                    return N;
                default:
                    throw new NullPointerException("Invalid heading: " + currentHeading);
            }
        }
    
        public Heading backSide(Heading currentHeading) {
            switch (currentHeading) {
                case N:
                    return S;
                case E:
                    return W;
                case S:
                    return N;
                case W:
                    return E;
                default:
                    throw new NullPointerException("Invalid heading: " + currentHeading);
            }
        }
    }

    public Drone(Integer batteryLevel, String initialHeading, MapRepresenter map) {
        this.batteryLevel = batteryLevel;
        this.currentHeading = Heading.valueOf(initialHeading);
        this.initialHeading = Heading.valueOf(initialHeading);
        // initialize it at (100, 100) for now
        this.currentLocation = new Point(100, 100);
        this.mapRepresenter = map;
    }

    // // these methods are based on the actions that the drone can take
    // public String turnLeft() {
    //     currentHeading = currentHeading.leftSide(currentHeading);
    //     return decisionTaken("heading", currentHeading.toString());
    // }
    
    // public String turnRight() {
    //     currentHeading = currentHeading.rightSide(currentHeading);
    //     return decisionTaken("heading", currentHeading.toString());
    // }

    // need to fix this method
    // public String turnAround(){
    //     Heading left = this.currentHeading.leftSide();
    //     switch (this.currentHeading) {
    //         case N:
    //             return decisionTaken("heading", left.toString());
    //         case E:
    //             return decisionTaken("heading", left.toString());
    //         case S:
    //             return decisionTaken("heading", left.toString());
    //         case W:
    //             return decisionTaken("heading", left.toString());
    //         default:
    //             return null;
    //     }
    // }
    

    // this method also updates the current location of the drone
    public String fly(){
        switch (currentHeading){
            case N:
                currentLocation = mapRepresenter.map.get(currentLocation.getX() - 1).get(currentLocation.getY());
                break;
            case E:
                currentLocation = mapRepresenter.map.get(currentLocation.getX()).get(currentLocation.getY() + 1);
                break;
            case S:
                currentLocation = mapRepresenter.map.get(currentLocation.getX() + 1).get(currentLocation.getY());
                break;
            case W:
                currentLocation = mapRepresenter.map.get(currentLocation.getX()).get(currentLocation.getY() - 1);
                break;
            default:
                break;
        }

        logger.info(currentLocation.getX()+ " " + currentLocation.getY());

        return decisionTaken("fly");
    }

    public void initializeCurrentLocation(Integer leftX, Integer topY, Boolean spawnedFacingGround){
        int x;
        int y;

        // we didnt change heading and so leftX and topY are the same as the current location
        if (spawnedFacingGround){
            x = leftX;
            y = topY;
        }
        // since we changed heading, leftX and topY are off by a bit, the 100 we intialized currentLocation at (100, 100)
        else{
            x = leftX + currentLocation.getY() - 100;
            y = topY + currentLocation.getX() - 100;
        }
        logger.info(x);
        logger.info(y);
        currentLocation = mapRepresenter.map.get(x).get(y);
    }

    // this method also updates current location based on current heading and next heading
    public String heading(Heading heading){
        if (heading == currentHeading || heading == currentHeading.backSide(currentHeading)){
            throw new IllegalArgumentException("Invalid heading");
        }
        if (heading == currentHeading.leftSide(currentHeading)){
            switch (currentHeading){
                case N:
                    currentLocation = mapRepresenter.map.get(currentLocation.getX() - 1).get(currentLocation.getY() - 1);
                    break;
                case E:
                    currentLocation = mapRepresenter.map.get(currentLocation.getX() - 1).get(currentLocation.getY() + 1);
                    break;
                case S:
                    currentLocation = mapRepresenter.map.get(currentLocation.getX() + 1).get(currentLocation.getY() + 1);
                    break;
                case W:
                    currentLocation = mapRepresenter.map.get(currentLocation.getX() + 1).get(currentLocation.getY() - 1);
                    break;
                default:
                    return null;
            }
        }

        if (heading == currentHeading.rightSide(currentHeading)){
            switch (currentHeading){
                case N:
                    currentLocation = mapRepresenter.map.get(currentLocation.getX() - 1).get(currentLocation.getY() + 1);
                    break;
                case E:
                    currentLocation = mapRepresenter.map.get(currentLocation.getX() + 1).get(currentLocation.getY() + 1);
                    break;
                case S:
                    currentLocation = mapRepresenter.map.get(currentLocation.getX() + 1).get(currentLocation.getY() - 1);
                    break;
                case W:
                    currentLocation = mapRepresenter.map.get(currentLocation.getX() - 1).get(currentLocation.getY() - 1);
                    break;
                default:
                    return null;
            }
        }
        logger.info(currentLocation.getX()+ " " + currentLocation.getY());
        return decisionTaken("heading", heading.toString());
    }

    public String echo(Heading heading){
        return decisionTaken("echo", heading.toString());
    }

    public String scan(){
        return decisionTaken("scan");
    }

    public String stop(){
        return decisionTaken("stop");
    }

    public Integer getBatteryLevel() {
        return batteryLevel;
    }

    public String getAction() {
        return action;
    }

    public Heading getDirection() {
        return direction;
    }

    public void updateBatteryLevel(Integer cost){
        if (cost < 0){
            throw new IllegalArgumentException("Cost cannot be negative");
        }
        batteryLevel -= cost;
    }

    // these helper methods store the parameters of the next decision in the variables action and direction and provide a string that will be returned to takeDecision
    String decisionTaken(String command){

        //ensures the commands are valid
        if (!command.equals("fly") && !command.equals("scan") && !command.equals("stop")){
            throw new IllegalArgumentException("Invalid command");
        }
        action = command;
        String nextDecision = "{\"action\": \""+ command +"\"}";
        return nextDecision;
    }

    String decisionTaken(String command, String direction){

        // need to make sure that the commands are valid
        if (!command.equals("echo") && !command.equals("heading")){
            throw new IllegalArgumentException("Invalid command");
        }
        //ensures the direction is valid
        if (!direction.equals("N") && !direction.equals("E") && !direction.equals("S") && !direction.equals("W")){
            throw new IllegalArgumentException("Invalid direction");
        }

        // if the command is heading, then the currentDirection is the new heading
        if (command.equals("heading")){
            this.currentHeading = Heading.valueOf(direction);
        }

        //store the parameters of the next decision
        action = command;
        this.direction = Heading.valueOf(direction); 

        String nextDecision = "{\"action\": \""+ command +"\", \"parameters\": { \"direction\": \"" + direction +"\"}}";
        return nextDecision;
    }
    

}
