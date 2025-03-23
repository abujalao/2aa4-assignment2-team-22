package ca.mcmaster.se2aa4.island.team22.Actions;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.mcmaster.se2aa4.island.team22.ActionManager.ActionType;
import ca.mcmaster.se2aa4.island.team22.Drone;
import ca.mcmaster.se2aa4.island.team22.IActionManage;
import ca.mcmaster.se2aa4.island.team22.IDroneAction;
import ca.mcmaster.se2aa4.island.team22.IStorage;

public class Echo extends Action {
    private final Logger logger = LogManager.getLogger();
    private IActionManage actionControlInterface;
    private IStorage storageInterface;
    public Echo(IDroneAction droneInterface) {
        super(droneInterface);
        this.storageInterface = droneInterface.getStorageInterface();
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
        if (args.length > 0 && args[0] instanceof String direction) {
            return createAction(ActionType.echo,Map.of("direction", direction));
        } else {
            throw new IllegalArgumentException("Echo action requires a direction argument.");
        }
    }

    @Override
    public String checkMove() {
        checkNull();
        if (getDroneInterface().getCurrentState() == Drone.DroneState.find_island) {
            logger.info("FINDING2");
            if(storageInterface.getResult().equals("GROUND")){
                logger.info("FOUND GROUND");
                if(getDroneInterface().getDirection().equals(actionControlInterface.getPastParameter(ActionType.echo, "direction"))){
                    storageInterface.decrementRange();
                    return actionControlInterface.getAction(ActionType.fly).execute();
                }
                else{
                    storageInterface.decrementRange();
                    return actionControlInterface.getAction(ActionType.heading).execute(actionControlInterface.getPastParameter(ActionType.echo, "direction"));
                }
            }
            //if its looking at ground then fly towards it...
            else{
                //fly until you reach middle of map...
                logger.info("Im FLYINGGGGG");
                storageInterface.decrementRange();
                return actionControlInterface.getAction(ActionType.fly).execute();
            }
        }
        return actionControlInterface.getAction(ActionType.stop).execute(); //wasnt in the original code
    }
}