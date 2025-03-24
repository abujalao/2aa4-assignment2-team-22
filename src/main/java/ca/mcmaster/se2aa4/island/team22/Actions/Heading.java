package ca.mcmaster.se2aa4.island.team22.Actions;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.mcmaster.se2aa4.island.team22.ActionManager.ActionType;
import ca.mcmaster.se2aa4.island.team22.DirectionUtil;
import ca.mcmaster.se2aa4.island.team22.IActionManage;
import ca.mcmaster.se2aa4.island.team22.IDroneAction;
import ca.mcmaster.se2aa4.island.team22.States.IDroneState;
public class Heading extends Action {
    private final Logger logger = LogManager.getLogger();
    private IActionManage actionControlInterface;

    public Heading(IDroneAction droneInterface) {
        super(droneInterface,ActionType.heading);
        this.actionControlInterface = droneInterface.getActionManagerInterface();
    }
    
    public void checkNull() {
        if (actionControlInterface==null) {
            this.actionControlInterface=droneInterface.getActionManagerInterface();
        }
    }

    @Override
    public String accept(IDroneState state){
        return state.visit(this);
    }

    @Override
    public String execute(Object... args) {
        checkNull();
        if (args.length > 0 && args[0] instanceof String direction) {
            String currentDir = actionControlInterface.getPastParameter(ActionType.heading,"direction"); //get current drone direction
            int[] stepAmountCurrent = DirectionUtil.Fly_Increment.get(currentDir); //amount of steps in current direction 
            int[] stepAmountNew = DirectionUtil.Fly_Increment.get(direction); //amount of steps in new direction
            logger.info("Change direction from {} to {}. Move step is ({},{})",currentDir,direction,stepAmountCurrent[0]+stepAmountNew[0],stepAmountCurrent[1]+stepAmountNew[1]);
            getDroneInterface().moveDrone(stepAmountCurrent[0]+stepAmountNew[0],stepAmountCurrent[1]+stepAmountNew[1]);//When turning around the drone will take a step in the current direction + the direction where it wants to go. If im north and its going east it will take one step north + one step east
            return createAction(ActionType.heading,Map.of("direction", direction));
        } else {
            throw new IllegalArgumentException("Heading action requires a direction argument.");
        }
    }
}