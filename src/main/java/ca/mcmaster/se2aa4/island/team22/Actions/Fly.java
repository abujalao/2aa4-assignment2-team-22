package ca.mcmaster.se2aa4.island.team22.Actions;

import ca.mcmaster.se2aa4.island.team22.ActionManager.ActionType;
import ca.mcmaster.se2aa4.island.team22.DirectionUtil;
import ca.mcmaster.se2aa4.island.team22.IDroneAction;
import ca.mcmaster.se2aa4.island.team22.States.IDroneState;

public class Fly extends Action {

    public Fly(IDroneAction droneInterface) {
        super(droneInterface,ActionType.fly);
    }

    @Override
    public String accept(IDroneState state){
        return state.visit(this);
    }

    @Override
    public String execute(Object... args) {
        String Direction = droneInterface.getDirection(); //ISTORAGE METHOD
        int[] stepAmount = DirectionUtil.Fly_Increment.get(Direction); //amount of steps to add in position (x,y)
        getDroneInterface().moveDrone(stepAmount[0],stepAmount[1]); //DRONE INTERFACE
        return createAction(ActionType.fly);
    }
}