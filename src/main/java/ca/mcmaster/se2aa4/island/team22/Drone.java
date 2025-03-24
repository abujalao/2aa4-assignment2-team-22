package ca.mcmaster.se2aa4.island.team22;

import java.io.StringReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;

import ca.mcmaster.se2aa4.island.team22.ActionManager.ActionType;
import ca.mcmaster.se2aa4.island.team22.StateManager.DroneState;

public class Drone implements IDroneAction {
    private final Logger logger = LogManager.getLogger();
    private int batteryLevel;
    private final int[] DEFAULT_POSITION = new int[] {1,1};//The reference starting point will always be (1,1) make sure we return to this position after we are done
    private final Position dronePosition = new Position(DEFAULT_POSITION[0],DEFAULT_POSITION[1]); 
    private final String initialHeading;

    private final ResponseStorage store = new ResponseStorage();
    private final PointsOfInterest pois = new PointsOfInterest(store);
    private final Maps map = new Maps(this, pois);
    private final ActionManager actionManager;
    private final StateManager stateManager;


    public Drone(int batteryLevel,String direction) {
        this.actionManager = new ActionManager(getDroneInterface());
        this.stateManager = new StateManager(getDroneInterface());
        this.batteryLevel = batteryLevel;
        actionManager.setDirectionParameter(direction); //save inital direction in actionManager.pastParameters to track drone direction

        actionManager.setAction(ActionType.echo); //default action is echo
        stateManager.setState(DroneState.findLand); //default state is find island


        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);
        this.initialHeading = direction;
        
    }

    @Override
    public void setDroneState(DroneState state){
        stateManager.setState(state);
    }

    @Override
    public int[] getDronePosition(){
        return this.dronePosition.getPosition();
    }
    
    @Override
    public void moveDrone(int x, int y) { //change drone position by taking x and y values and adding to the current drone position. (ex. if drone position is (30,41) and given moveDrone (5,6) new location is (35,47)
        int[] currentPosition = this.dronePosition.getPosition();
        this.dronePosition.setPosition(currentPosition[0]+x,currentPosition[1]+y);
    }

    private void setBattery(int value) {
        if (batteryLevel-value <0) value = batteryLevel;
        this.batteryLevel = value;
        logger.info("The battery of the drone is {}", getBattery());   
    }


    @Override
    public boolean canSaveThem(){
        return map.canSaveThem();
    }


    private void decrementBattery(int value){
        if (value > 0){
            setBattery(batteryLevel-value);
        }
    }

    public int getBattery() {
        return batteryLevel;
    }

    @Override
    public String getDirection() {
        return actionManager.getPastParameter(ActionType.heading,"direction"); //Last saved heading action parameter is the drone current direction.
    }

    @Override
    public String getInitialHeading(){
        return initialHeading;
    }

    @Override
    public boolean isbiomeOcean(){
        return store.isOnOcean();
    }

    private IDroneAction getDroneInterface(){
        return this;
    }

    @Override
    public IStorage getStorageInterface(){
        return store;
    }

    @Override
    public IActionManage getActionManagerInterface(){
        return actionManager;
    }

    @Override
    public IPoi getPoiInterface(){
        return pois;
    }

    @Override
    public IMap getMapInterface(){
        return map;
    }

    @Override
    public String[] availableDirections(){
        return DirectionUtil.Available_Directions.get(getDirection());
    }

    public String makeMove() {
        logger.info("DRONE DIRECTION: "+getDirection());
        logger.info("DRONE POSITION: "+dronePosition.getStringPosition());
        logger.info("Available Directions: {}", String.join(", ", availableDirections()));

        //base case: if battery level is low, just return to base to avoid losing the drone
        if(batteryLevel < 30){ 
            return actionManager.execute(ActionType.stop);
        }
        return stateManager.performMove();
    }

    public void updateDrone(String s) {
        JSONObject response = new JSONObject(new JSONTokener(new StringReader(s)));
        logger.info("** Response received:\n"+response.toString(2));
        Integer cost = response.getInt("cost");
        logger.info("The cost of the action was {}", cost);
        decrementBattery(cost);
        JSONObject extraInfo = response.getJSONObject("extras");
        logger.info("Additional information received: {}", extraInfo);
        store.storeResults(actionManager.getCurrentAction(), response); 
    }
}