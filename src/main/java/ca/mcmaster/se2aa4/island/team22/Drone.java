package ca.mcmaster.se2aa4.island.team22;

import java.io.StringReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;

import ca.mcmaster.se2aa4.island.team22.Drone.StatusType;


public class Drone {
    private final Logger logger = LogManager.getLogger();
    private int batteryLevel;
    private StatusType status;
    
    private final ActionController actionController = new ActionController();

    private final ResponseStorage store = new ResponseStorage();

    public enum StatusType {
        OK,
    }

    public Drone(int batteryLevel,String direction) {
        this.batteryLevel = batteryLevel;
        actionController.setDirectionParameter(direction); //save inital direction in actionController.pastParameters to track drone direction
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

    public StatusType getStatus() {
        return status;
    }

    private String getDirection() {
        return actionController.getPastParameter("heading","direction"); //Last saved heading action parameter is the drone current direction.
    }

    private Boolean canChangeDirection(String direction) { //check if drone can change to given direction
        return (!direction.equals(this.getDirection()) && !direction.equals(DirectionUtil.Opposite_Directions.get(direction))); //if given direction!=drone direction AND given direction!=Opposite direction then true
    }

    private Boolean canEcho(String direction) { //check if drone can change to given direction
        return (!direction.equals(DirectionUtil.Opposite_Directions.get(direction))); //if given echo direction!=Opposite direction then true (We cant echo the opposite direction)
    }
    
    public String makeMove() {
        logger.info("DRONE DIRECTION: "+getDirection());
        if(store.getResult().equals("GROUND")){ //if found ground on last result
                if (actionController.getPastParameter("echo","direction").equals("S") && canChangeDirection("S")){ //If past echo was ground && If drone can change direction (ex. if the drone is already heading south dont change heading to south again, this will stop the game)
                    return actionController.heading("S");
                }
        }
        if(store.getCost() == -1){ //First run when cost is at default amount of -1
            return actionController.echo("E");
        }
        if(actionController.getAction().equals("heading")){ //if last action was "heading"
            return actionController.echo("S");
        }
        if(store.getRange() > 0 && store.getResult().equals("GROUND")){ //keep flying to ground until range is zero
            store.decrementRange(); //update distance instead of echo update (1 fly = 1 range)
            return actionController.fly();
        }
        if(actionController.getAction().equals("fly")){ //if last action was "fly"
            return actionController.echo("S");
        }
        if(store.getRange() > 25 && store.getResult().equals("OUT_OF_RANGE")){ //We dont want to get close to "out of range"
            return actionController.fly();
        } 
        return actionController.stop();
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
        store.storeResults(actionController.getAction(), response); 
    }
}