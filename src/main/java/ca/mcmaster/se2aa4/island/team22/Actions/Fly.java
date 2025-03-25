package ca.mcmaster.se2aa4.island.team22.Actions;


import ca.mcmaster.se2aa4.island.team22.IDroneAction;
import ca.mcmaster.se2aa4.island.team22.Managers.ActionManager.ActionType;
import ca.mcmaster.se2aa4.island.team22.States.IDroneState;
import ca.mcmaster.se2aa4.island.team22.Util.DirectionUtil;
public class Fly extends Action {

    public Fly(IDroneAction droneInterface) {
        super(droneInterface,ActionType.FLY);
    }

    @Override
    public Action createNew() {
        return new Fly(droneInterface);
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
        return createAction(ActionType.FLY);
    }
}