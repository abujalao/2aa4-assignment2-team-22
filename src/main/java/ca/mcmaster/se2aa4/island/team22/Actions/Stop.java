package ca.mcmaster.se2aa4.island.team22.Actions;

import ca.mcmaster.se2aa4.island.team22.ActionManager.ActionType;
import ca.mcmaster.se2aa4.island.team22.IDroneAction;

public class Stop extends Action {

    public Stop(IDroneAction droneInterface) {
        super(droneInterface);
    }

    @Override
    public String execute(Object... args) {
        return createAction(ActionType.stop);
    }

    @Override
    public String checkMove(String[] availableDirs) {
        return this.execute();
    }
}