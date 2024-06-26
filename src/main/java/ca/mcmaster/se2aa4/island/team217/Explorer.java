package ca.mcmaster.se2aa4.island.team217;

import ca.mcmaster.se2aa4.island.team217.MapRepresentation.*;

import java.io.StringReader;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import eu.ace_design.island.bot.IExplorerRaid;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONArray;
import java.util.List;

public class Explorer implements IExplorerRaid {

    private final Logger logger = LogManager.getLogger();

    private JSONObject checker = new JSONObject(); // used by explorer system

    Drone drone;
    MapRepresenter map;
    MissionControl missionControl;

    @Override
    public void initialize(String s) {
        logger.info("** Initializing the Exploration Command Center");
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Initialization info:\n {}", info.toString(2));
        String direction = info.getString("heading");
        Integer batteryLevel = info.getInt("budget");

        map = new MapRepresenter();
        drone = new Drone(batteryLevel, direction, map);
        missionControl = new MissionControl(drone, map);

        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);
    }

    @Override
    public String takeDecision() {
        String decision = missionControl.nextDecision();
        logger.info("** Decision: {}", decision.toString());
        return decision;
    }

    @Override
    public void acknowledgeResults(String s) {
        JSONObject response = new JSONObject(new JSONTokener(new StringReader(s)));
        missionControl.storeResponse(drone.getAction(), response);

        logger.info("** Response received:\n" + response.toString(2));
        Integer cost = response.getInt("cost");
        drone.updateBatteryLevel(cost);
        logger.info("The cost of the action was {}", cost);
        String status = response.getString("status");
        logger.info("The status of the drone is {}", status);
        JSONObject extraInfo = response.getJSONObject("extras");
        logger.info("Additional information received: {}", extraInfo);
        checker.put("check", response.getJSONObject("extras"));
    }

    @Override
    public String deliverFinalReport() {
        List<PointWithCreeks> creeks = map.getCreeks();
        PointWithCreeks closestCreek = map.getClosestCreek();
        PointWithSite site = map.getSite();

        if (closestCreek == null && site == null) {
            return "No creeks found";
        }
        else if (site == null) {
            logger.info("The closest creek is {}", closestCreek.getIdentifiers().get(0));
            return closestCreek.getIdentifiers().get(0);
        }
        else if (closestCreek == null) {
            return site.getIdentifier();
        }
        else{
            logger.info("** The identifier of the closest creek is {}", closestCreek.getIdentifiers().get(0));
            logger.info("** Delivering the final report");
            return closestCreek.getIdentifiers().get(0);
        }
    }

    public static void main(String[] args) {
        Explorer e = new Explorer();
        e.initialize("{\"budget\":1000,\"heading\":\"N\"}");
        e.takeDecision();
        e.acknowledgeResults("{\"cost\":1,\"status\":\"success\",\"extras\":{\"range\":1}}");
        e.deliverFinalReport();
    }

}