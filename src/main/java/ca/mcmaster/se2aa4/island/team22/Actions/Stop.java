package ca.mcmaster.se2aa4.island.team22.Actions;

import ca.mcmaster.se2aa4.island.team22.IDroneAction;
import ca.mcmaster.se2aa4.island.team22.Managers.ActionManager.ActionType;
import ca.mcmaster.se2aa4.island.team22.States.IDroneState;
public class Stop extends Action {

    public Stop(IDroneAction droneInterface) {
        super(droneInterface, ActionType.stop);
    }

    @Override
    public Action createNew() {
        return new Stop(droneInterface);
    }

    @Override
    public String execute(Object... args) {
        droneInterface.getMapInterface().printMap();
        return createAction(ActionType.stop);
    }

    @Override
    public String accept(IDroneState state){
        return state.visit(this);
    }
}