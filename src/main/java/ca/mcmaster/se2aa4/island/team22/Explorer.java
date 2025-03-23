package ca.mcmaster.se2aa4.island.team22;

import java.io.StringReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;

import eu.ace_design.island.bot.IExplorerRaid;

public class Explorer implements IExplorerRaid {

    private final Logger logger = LogManager.getLogger();
    private Drone drone;
    @Override
    public void initialize(String s) {
        logger.info("** Initializing the Exploration Command Center");
        JSONObject info = new JSONObject(new JSONTokener(new StringReader(s))); 
        logger.info("** Initialization info:\n {}",info.toString(2));
        String direction = info.getString("heading"); 
        Integer batteryLevel = info.getInt("budget");

        this.drone = new Drone(batteryLevel,direction);
    }

    @Override
    public String takeDecision() {
        String DecisionResult = drone.makeMove();
        logger.info("DECISION: "+DecisionResult);
        return DecisionResult;
    }

    @Override
    public void acknowledgeResults(String s) {
        drone.updateDrone(s);
    }

    @Override
    public String deliverFinalReport() {
        return "no creek found";
    }

}
