package ca.mcmaster.se2aa4.island.team22.Actions;

import ca.mcmaster.se2aa4.island.team22.ActionManager.ActionType;
import ca.mcmaster.se2aa4.island.team22.IDroneAction;

public class Stop extends Action {

    public Stop(IDroneAction droneInterface) {
        super(droneInterface, ActionType.stop);
    }

    @Override
    public String execute(Object... args) {
        droneInterface.getMapInterface().printMap();
        return createAction(ActionType.stop);
    }

    @Override
    public String checkMove() {
        return this.execute();
    }
}