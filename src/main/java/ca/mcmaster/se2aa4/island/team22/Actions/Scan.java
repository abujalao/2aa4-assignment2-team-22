package ca.mcmaster.se2aa4.island.team22.Actions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.mcmaster.se2aa4.island.team22.ActionManager.ActionType;
import ca.mcmaster.se2aa4.island.team22.IActionManage;
import ca.mcmaster.se2aa4.island.team22.IDroneAction;

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
        if (actionControlInterface==null) {
            this.actionControlInterface=droneInterface.getActionManagerInterface();
        }
        droneInterface.getPoiInterface().storeScan(); 
        droneInterface.getMapInterface().addPOI();
        if (droneInterface.canSaveThem()){
            logger.info("Have found save places.");
            return actionControlInterface.getAction(ActionType.stop).execute();
        }
        return actionControlInterface.getAction(ActionType.echo).execute(droneInterface.getDirection());
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