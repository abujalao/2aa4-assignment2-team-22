package ca.mcmaster.se2aa4.island.team22;

import java.io.StringReader;
import java.util.HashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;

import ca.mcmaster.se2aa4.island.team22.Drone.StatusType;


public class Drone implements IDroneMove {
    private final Logger logger = LogManager.getLogger();
    private int batteryLevel;
    private StatusType status;

    private final int[] DEFAULT_POSITION = new int[] {1,1};//The reference starting point will always be (1,1) make sure we return to this position after we are done
    private final Position dronePosition = new Position(DEFAULT_POSITION[0],DEFAULT_POSITION[1]); 
    private final ActionController actionController = new ActionController(getMoveInterface());

    private enum DroneState {
        find_island,
        return_base,
    }
    private DroneState currentState = DroneState.find_island; //first thing to do is to find island
    private final HashSet<String> requiredDirections = new HashSet<>(); //Directions we need to take to reach to the base. 
    private final ResponseStorage store = new ResponseStorage();

    static int checks = 0;

    public enum StatusType {
        OK,
    }

    public Drone(int batteryLevel,String direction) {
        this.batteryLevel = batteryLevel;
        actionController.setDirectionParameter(direction); //save inital direction in actionController.pastParameters to track drone direction
        logger.info("The drone is facing {}", direction);
        logger.info("Battery level is {}", batteryLevel);
        
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

    private IDroneMove getMoveInterface(){
        return this;
    }

    private String[] availableDirections(){
        return DirectionUtil.Available_Directions.get(getDirection());
    }
    
    private String flyBackMove(){ //controls what action to take to get back to the base
        if (currentState != DroneState.return_base) { //make sure we are in return_base state when we are at this point
            this.currentState = DroneState.return_base;
        }

        int offset = 1; //offset for changing direction 

        //check if there will be direction changes (when requiredDirections>1)
        if(requiredDirections.size()==1) {
            offset=0;
        }

        //check x
        if (dronePosition.getX()>DEFAULT_POSITION[0]+offset) {
            requiredDirections.add("W");
        } else if (dronePosition.getX()<DEFAULT_POSITION[0]-offset) {
            requiredDirections.add("E");
        } else {
            requiredDirections.remove("W");
            requiredDirections.remove("E");
            logger.info("REACHED X TARGET BASE");
        }

        //check y
        if (dronePosition.getY()<DEFAULT_POSITION[0]-offset) { 
            requiredDirections.add("S");
        } else if (dronePosition.getY()>DEFAULT_POSITION[0]+offset) {
            requiredDirections.add("N");
        } else {
            requiredDirections.remove("S");
            requiredDirections.remove("N");
            logger.info("REACHED Y TARGET BASE");
        }

        //check direction
        if (!requiredDirections.isEmpty()) { //If its empty that means both conditions above are met
            if (requiredDirections.contains(getDirection())) {
                return actionController.fly();
            } 
            
            for (String direction : availableDirections()) {
                if (requiredDirections.contains(direction)) {
                    return actionController.heading(direction);
                }
            }
        }
        return actionController.stop();//once reached base we can stop
    }

    public String makeMove() {
        logger.info("DRONE DIRECTION: "+getDirection());
        logger.info("DRONE POSITION: "+dronePosition.getStringPosition());
        logger.info("Available Directions: {}", String.join(", ", availableDirections()));

        String[] availableDirs = availableDirections();
        switch (actionController.getAction()) {
            case "":
                return actionController.echo(getDirection());
            case "echo":

                if (currentState == DroneState.find_island) {
                    if(store.getResult().equals("GROUND")){
                        logger.info("FOUND GROUND");
                        if(getDirection().equals(actionController.getPastParameter("echo", "direction"))){
                            store.decrementRange();
                            return actionController.fly();
                        }
                        else{
                            store.decrementRange();
                            return actionController.heading(actionController.getPastParameter("echo", "direction"));
                        }
                    }
                    //if its looking at ground then fly towards it...
                    else{
                        //fly until you reach middle of map...
                        logger.info("Im FLYINGGGGG");
                        store.decrementRange();
                        return actionController.fly();
                    }
                }
                

            case "fly":
                if (currentState == DroneState.find_island) {
                    // When range is less than 30, echo in one direction at a time
                    if (store.getRange() < 30 && !store.getResult().equals("GROUND")) {
                        // Get the directions available for the current position
                        if (checks == 0){
                            checks++;
                            return actionController.echo(availableDirs[0]);
                        }
                        else{
                            checks = 0;
                            return actionController.echo(availableDirs[1]);
                        }

                    } else {
                        if(store.getRange() != 0){
                            // If range is greater than 30, just keep flying and decrement range
                            // and if you havent reached ground...
                            store.decrementRange();
                            return actionController.fly();
                        }
                        if (store.getRange() == 0){
                            return actionController.scan();
                        }
                    } 
                } else if (currentState == DroneState.return_base) {
                    return flyBackMove();
                }
            case "heading":
                return actionController.fly();
            case "scan":
                currentState = DroneState.return_base;//testing return_base state
            default:
                return flyBackMove();
        }
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