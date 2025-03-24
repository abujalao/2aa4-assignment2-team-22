package ca.mcmaster.se2aa4.island.team22.Actions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.mcmaster.se2aa4.island.team22.ActionManager.ActionType;
import ca.mcmaster.se2aa4.island.team22.DirectionUtil;
import ca.mcmaster.se2aa4.island.team22.Drone;
import ca.mcmaster.se2aa4.island.team22.IActionManage;
import ca.mcmaster.se2aa4.island.team22.IDroneAction;
import ca.mcmaster.se2aa4.island.team22.IStorage;
import ca.mcmaster.se2aa4.island.team22.Position;

public class Fly extends Action {
    private final Logger logger = LogManager.getLogger();
    private IStorage storageInterface;
    private IActionManage actionControlInterface;
    public Fly(IDroneAction droneInterface) {
        super(droneInterface);
        this.storageInterface = droneInterface.getStorageInterface();
        this.actionControlInterface = droneInterface.getActionManagerInterface();
        
    }

    public void checkNull() {
        if (actionControlInterface==null||storageInterface==null) {
            actionControlInterface = getDroneInterface().getActionManagerInterface();
            storageInterface = getDroneInterface().getStorageInterface();
        }
    }

    @Override
    public String execute(Object... args) {
        checkNull();
        String Direction = actionControlInterface.getPastParameter(ActionType.heading,"direction"); //ISTORAGE METHOD
        int[] stepAmount = DirectionUtil.Fly_Increment.get(Direction); //amount of steps to add in position (x,y)
        getDroneInterface().moveDrone(stepAmount[0],stepAmount[1]); //DRONE INTERFACE
        return createAction(ActionType.fly);
    }

    @Override
    public String checkMove() {
        checkNull();
        String[] availableDirs = getDroneInterface().availableDirections();
        storageInterface.decrementRange();

        if(droneInterface.getIslandFound()){
            logger.info("range is: " + storageInterface.getRange());
            //i need getbiome from previousresponse
            if(storageInterface.getRange() > -1 && storageInterface.getResult().equals("GROUND")){
                return actionControlInterface.getAction(ActionType.fly).execute();
            }
            if (storageInterface.getResult().equals("GROUND")){
                return actionControlInterface.getAction(ActionType.scan).execute();
            }
        }
        else{
            if (getDroneInterface().getCurrentState() == Drone.DroneState.find_island) {
                    // When range is less than 30, echo in one direction at a time
                    if (!storageInterface.getResult().equals("GROUND")) {
                        // Get the directions available for the current position
                        if (droneInterface.getDroneChecks() == 0){
                            droneInterface.incrementDroneChecks();
                            return actionControlInterface.getAction(ActionType.echo).execute(availableDirs[0]);
                        }
                        else{
                            droneInterface.resetDroneChecks();
                            return actionControlInterface.getAction(ActionType.echo).execute(availableDirs[1]);
                        }

                    } else {
                        logger.info("range is: " + storageInterface.getRange());
                        if(storageInterface.getRange() > -2){
                            // If range is greater than 30, just keep flying and decrement range
                            // and if you havent reached ground...
                            return this.execute();
                        }
                        else if (storageInterface.getRange() < 0){
                            droneInterface.setIslandFound(true);
                            
                            return actionControlInterface.getAction(ActionType.scan).execute();
                        }
                    } 
            }
        }
        return this.execute(); //This wasnt in under "fly" case
    }
}