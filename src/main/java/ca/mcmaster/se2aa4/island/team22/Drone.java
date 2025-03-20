package ca.mcmaster.se2aa4.island.team22;

import java.io.StringReader;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;

import ca.mcmaster.se2aa4.island.team22.Drone.StatusType;


public class Drone {
    private final Logger logger = LogManager.getLogger();
    private int batteryLevel;
    private String direction;
    private StatusType status;
    Direction heading = Direction.E;
    String action = "";

    private ResponseStorage store = new ResponseStorage();

    public enum StatusType {
        OK,
    }
    public enum Direction{
        N,E,W,S;
    }

    public Drone(int batteryLevel,String direction) {
        this.batteryLevel = batteryLevel;
        this.direction = direction;
        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);
        
    }

    private void setBattery(int value) {
        if (batteryLevel-value <0) value = batteryLevel;
        this.batteryLevel = value;
        logger.info("The battery of the drone is {}", getBattery());   
    }

    private void setStatus(StatusType status) {
        this.status = status;
    }
    
    private void decrementBattery(int value){
        if (value > 0){
            setBattery(batteryLevel-value);
        }
    }

    private void updateStatus(String status) {
        switch (status) {
            case "OK" -> setStatus(StatusType.OK);
        }
        logger.info("The status of the drone is {}", getStatus());
    }

    public int getBattery() {
        return batteryLevel;
    }

    public String getDirection() {
        return direction;
    }

    public StatusType getStatus() {
        return status;
    }

    public String fly(){
        JSONObject decision = new JSONObject(Map.of(
             "action", "fly"
         ));

        action = "fly";
        return decision.toString();
        
    }

    public String echo(String dir) {
        // JSONObject decision = new JSONObject(Map.of(
        //     "action", "echo",
        //     "parameters", Map.of("direction", "E", "range", 2)
        // ));
        JSONObject decision = new JSONObject(Map.of(
             "action", "echo",
             "parameters", Map.of("direction", dir)
         ));
        // JSONObject decision = new JSONObject(Map.of(
        //      "action", "scan"
             
        //  ));
        action ="echo";
        return decision.toString();
    }

    public String scan(){
        JSONObject decision = new JSONObject(Map.of(
             "action", "scan"
             
         ));
         action ="scan";
        return decision.toString();
    }

    public String changeHeading(String dir){
        JSONObject decision = new JSONObject(Map.of(
             "action", "heading",
             "parameters", Map.of("direction", dir)
         ));
         action ="heading";
        return decision.toString();
    }

    public String stop(){
        JSONObject decision = new JSONObject(Map.of(
             "action", "stop"
         ));
         action ="stop";
        return decision.toString();
    }

    public String makeMove() {
        if(!(store.getResult().equals(""))){
            if(store.getResult().equals("GROUND")){
                if (store.getechoDir().equals("S")){
                    return changeHeading("S");
                }
            }
        }
        if(store.getCost() == -1){
            return echo("E");
        }
        if(store.getPrevAction().equals("heading")){
            return echo("S");
        }
        if(store.getRange() > 25){
            return fly();
        }
        if(store.getPrevAction().equals("fly")){
            return echo("S");
        }
        return stop();

    }

    public void updateDrone(String s) {
        JSONObject response = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Response received:\n"+response.toString(2));
        Integer cost = response.getInt("cost");
        logger.info("The cost of the action was {}", cost);
        decrementBattery(cost);
        updateStatus(response.getString("status"));
        JSONObject extraInfo = response.getJSONObject("extras");
        logger.info("Additional information received: {}", extraInfo);
        store.storeResults(action, response);
        
        
    }
}