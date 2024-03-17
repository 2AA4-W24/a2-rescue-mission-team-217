package ca.mcmaster.se2aa4.island.team217;

import ca.mcmaster.se2aa4.island.team217.Drone.Heading;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class MapRepresenter {

    private final Logger logger = LogManager.getLogger();

    // used for map initialization
    int columns = 0;
    int rows = 0;

    public List<PointOfInterest> creeks = new ArrayList<>();
    PointOfInterest closestCreek;
    public PointOfInterest site;
    List<List<Point>> map = new ArrayList<>();
    public Boolean initialized = false;

    public MapRepresenter() {
        // initialize with these dimensions for now, will refactor this later
        for (int i = 0; i < 200; i++) {
            List<Point> row = new ArrayList<>();
            for (int j = 0; j < 200; j++) {
                Point point = new Point(i, j);
                row.add(point);
            }
            map.add(row);
        }
    }

    public void storeScanResults(ResponseStorage scanResults, Point currentLocation) {

        if (!(scanResults.getCreeks().get(0).equals("null"))) {
            // if there are creeks, add them to the POI list
            for (String creekIdentifier : scanResults.getCreeks()) {
                PointOfInterest poi = new PointOfInterest(currentLocation.getX(), currentLocation.getY(),
                        creekIdentifier, "creek");
                currentLocation = poi;
                creeks.add(poi);
            }
        }

        if (!(scanResults.getSite().equals("null"))) {
            // if there are sites, add them to the POI list
            site = new PointOfInterest(currentLocation.getX(), currentLocation.getY(),
                        scanResults.getSite(), "site");
            currentLocation = site;
        }
        currentLocation.addBiomes(scanResults.getBiomes());

    }

    public void initializeMap() {
        // clear the map that we used for intialization purposes
        map.clear();

        // initialize the map with the given dimensions
        logger.info("Initializing map with dimensions: " + columns + "x" + rows);
        for (int i = 0; i < columns; i++) {
            List<Point> row = new ArrayList<>();
            for (int j = 0; j < rows; j++) {
                Point point = new Point(i, j);
                row.add(point);
            }
            map.add(row);
        }

    }

    public double computeMinDistance(){
        if (site == null){
            return 0;
        }
        closestCreek = creeks.get(0);
        double minDistance = 1000000;
        for (PointOfInterest creek : creeks){
            double distance = Math.sqrt(Math.pow((creek.getX() - site.getX()), 2) + Math.pow((creek.getY() - site.getY()), 2));
            if (distance < minDistance){
                closestCreek = creek;
                minDistance = distance;
            }
        }
        return minDistance;
    }
}
