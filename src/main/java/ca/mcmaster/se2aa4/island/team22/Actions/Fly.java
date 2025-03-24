package ca.mcmaster.se2aa4.island.team22.Actions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.mcmaster.se2aa4.island.team22.ActionManager.ActionType;
import ca.mcmaster.se2aa4.island.team22.DirectionUtil;
import ca.mcmaster.se2aa4.island.team22.IActionManage;
import ca.mcmaster.se2aa4.island.team22.IDroneAction;
import ca.mcmaster.se2aa4.island.team22.IStorage;
import ca.mcmaster.se2aa4.island.team22.States.IDroneState;

public class Fly extends Action {
    private final Logger logger = LogManager.getLogger();
    private IStorage storageInterface;
    private IActionManage actionControlInterface;

    public Fly(IDroneAction droneInterface) {
        super(droneInterface,ActionType.fly);
        this.storageInterface = droneInterface.getStorageInterface();
        this.actionControlInterface = droneInterface.getActionManagerInterface();
        
    }

    @Override
    public String accept(IDroneState state){
        return state.perform(this);
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
}