package ca.mcmaster.se2aa4.island.team22.Actions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.mcmaster.se2aa4.island.team22.ActionManager.ActionType;
import ca.mcmaster.se2aa4.island.team22.DirectionUtil;
import ca.mcmaster.se2aa4.island.team22.Drone;
import ca.mcmaster.se2aa4.island.team22.IActionManage;
import ca.mcmaster.se2aa4.island.team22.IDroneAction;
import ca.mcmaster.se2aa4.island.team22.IStorage;

public class Fly extends Action {
    private final Logger logger = LogManager.getLogger();
    private IStorage storageInterface;
    private IActionManage actionControlInterface;

    public Fly(IDroneAction droneInterface) {
        super(droneInterface,ActionType.fly);
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

        if(droneInterface.getFoundLand()){
            String oppositeDir = "";
            if(droneInterface.getStartOppositeScanning() && !oppositeDir.equals("")){
                return actionControlInterface.execute(ActionType.heading,oppositeDir);
            }
            //i need getbiome from previousresponse
            if(storageInterface.getRange() > -1 && storageInterface.getResult().equals("GROUND")){
                return actionControlInterface.execute(ActionType.fly);
            }
            if(storageInterface.getRange() > -1 && !storageInterface.getResult().equals("GROUND")){
                droneInterface.setStartOppositeScanning(true);
                droneInterface.setChangeScanDir(true);
                oppositeDir = DirectionUtil.Opposite_Directions.get(droneInterface.getDirection());
                return actionControlInterface.execute(ActionType.heading,availableDirs[0]);
            }
            if (storageInterface.getResult().equals("GROUND")){
                if(droneInterface.getMapInterface().isInMap(droneInterface.getDronePosition())){
                    logger.info("Already scanned, skipping scan");
                    return actionControlInterface.execute(ActionType.fly);
                }
                return actionControlInterface.execute(ActionType.scan);
            }
        }
        else{
            Echo echoAction = (Echo) actionControlInterface.getCountInterface(ActionType.echo);
            if (getDroneInterface().getCurrentState() == Drone.DroneState.find_island) {
                    // When range is less than 30, echo in one direction at a time
                    if (!storageInterface.getResult().equals("GROUND")) {
                        // Get the directions available for the current position
                        if (echoAction.getCount() == 0){ // same comeon availableDirs[getCount()]
                            echoAction.incrementCount();
                            return actionControlInterface.execute(ActionType.echo,availableDirs[0]);
                        }
                        else{
                            echoAction.resetCount();
                            return actionControlInterface.execute(ActionType.echo,availableDirs[1]);
                        }

                    } else {
                        logger.info("range is: " + storageInterface.getRange());
                        if(storageInterface.getRange() > -2){
                            // If range is greater than 30, just keep flying and decrement range
                            // and if you havent reached ground...
                            return this.execute();
                        }
                        else if (storageInterface.getRange() < 0){
                            droneInterface.setFoundLand(true);
                            return actionControlInterface.execute(ActionType.scan);
                        }
                    } 
            }
        }
        return this.execute(); //This wasnt in under "fly" case
    }
}