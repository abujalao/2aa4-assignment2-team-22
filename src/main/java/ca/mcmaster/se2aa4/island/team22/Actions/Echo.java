package ca.mcmaster.se2aa4.island.team22.Actions;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.mcmaster.se2aa4.island.team22.ActionManager.ActionType;
import ca.mcmaster.se2aa4.island.team22.DirectionUtil;
import ca.mcmaster.se2aa4.island.team22.Drone;
import ca.mcmaster.se2aa4.island.team22.IActionManage;
import ca.mcmaster.se2aa4.island.team22.IDroneAction;
import ca.mcmaster.se2aa4.island.team22.IStorage;

public class Echo extends Action implements IActionCount {
    private final Logger logger = LogManager.getLogger();
    private IActionManage actionControlInterface;
    private IStorage storageInterface;
    private int echoCount = 0;
    public Echo(IDroneAction droneInterface) {
        super(droneInterface,ActionType.echo);
        this.storageInterface = droneInterface.getStorageInterface();
    }
    
    @Override
    public int getCount() {
        return echoCount;
    }   

    @Override
    public void incrementCount() {
        echoCount++;
    }  

    @Override
    public void resetCount() {
        echoCount = 0;
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
        String[] availableDirs = getDroneInterface().availableDirections();

        if(droneInterface.getFoundLand()){
            //i need getbiome from previousresponse
            return actionControlInterface.execute(ActionType.fly);
        }

        if (getDroneInterface().getCurrentState() == Drone.DroneState.find_island) {
            if(storageInterface.getResult().equals("GROUND")){
                logger.info("FOUND GROUND");
                if(getDroneInterface().getDirection().equals(actionControlInterface.getPastParameter(ActionType.echo, "direction"))){
                    storageInterface.decrementRange();
                    return actionControlInterface.execute(ActionType.fly);
                }
                else{
                    storageInterface.decrementRange();
                    String oppositeDir = DirectionUtil.Opposite_Directions.get(droneInterface.getDirection());
                    logger.info("dir is:" + droneInterface.getDirection() + "Opposite is: " + oppositeDir);
                    return actionControlInterface.execute(ActionType.heading,actionControlInterface.getPastParameter(ActionType.echo, "direction"));
                }
            }
            //if its looking at ground then fly towards it...
            else{
                //fly until you reach middle of map...
                logger.info("Im FLYINGGGGG");
                
                if (getCount() > 1){
                    resetCount();
                    if(!droneInterface.getFoundLand()){
                        return actionControlInterface.execute(ActionType.fly);
                    }
                    else{
                        String oppositeDir = DirectionUtil.Opposite_Directions.get(droneInterface.getDirection());
                        return actionControlInterface.execute(ActionType.heading, availableDirs[1]);
                    }
                }
                int count = getCount();
                incrementCount();
                return actionControlInterface.execute(ActionType.echo, availableDirs[count]);
            }
        }
        return actionControlInterface.execute(ActionType.stop);
    }
}