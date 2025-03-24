package ca.mcmaster.se2aa4.island.team22.Actions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.mcmaster.se2aa4.island.team22.ActionManager.ActionType;
import ca.mcmaster.se2aa4.island.team22.IActionManage;
import ca.mcmaster.se2aa4.island.team22.IDroneAction;
import ca.mcmaster.se2aa4.island.team22.Position;

public class Scan extends Action {
    private IActionManage actionControlInterface;

    private final Logger logger = LogManager.getLogger();

    public Scan(IDroneAction droneInterface) {
        super(droneInterface);     
        this.actionControlInterface = droneInterface.getActionManagerInterface(); 
    }

    @Override
    public String execute(Object... args) {
        return createAction(ActionType.scan);
    }

    @Override
    public String checkMove() {
        String[] availableDirs = getDroneInterface().availableDirections();

        if (actionControlInterface==null) {
            this.actionControlInterface=droneInterface.getActionManagerInterface();
        }
        droneInterface.getPoiInterface().storeScan(); 
        droneInterface.getMapInterface().addPOI();
        if (droneInterface.canSaveThem()){
            logger.info("Have found save places.");
            return actionControlInterface.getAction(ActionType.stop).execute();
        }
        //if the biome is ocean, tturn...
        // Position targetPosition = new Position(50,30); // Replace with your desired position
        // if (droneInterface.getPos().equals(targetPosition)) {
        //     logger.info("Drone has reached the target position: " + targetPosition);
        //     return actionControlInterface.getAction(ActionType.stop).execute();
        // }
        if(droneInterface.isbiomeOcean()){
            logger.info("Biome is ocean, turning...");
            if (droneInterface.getChangeScanDir()){
                return actionControlInterface.getAction(ActionType.heading).execute(availableDirs[0]);
            }
            return actionControlInterface.getAction(ActionType.heading).execute(availableDirs[1]);
        }
        return actionControlInterface.getAction(ActionType.fly).execute();
        //currentState = DroneState.return_base;//testing return_base state
        // if (droneInterface.getDroneScan() == 0){
        //     droneInterface.incrementScan();
        //     return actionControlInterface.getAction(ActionType.scan).execute();
        // }
        // else{
        //     droneInterface.resetScan();
        //     // if (map.canSaveThem()){
        //     //     logger.info("youve done it.");
        //     //     return actionController.stop();
        //     // }
        //     return actionControlInterface.getAction(ActionType.echo).execute(droneInterface.getDirection());
        // }
    }
}