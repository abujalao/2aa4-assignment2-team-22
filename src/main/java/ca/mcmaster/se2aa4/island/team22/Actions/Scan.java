package ca.mcmaster.se2aa4.island.team22.Actions;

import ca.mcmaster.se2aa4.island.team22.ActionManager.ActionType;
import ca.mcmaster.se2aa4.island.team22.IDroneAction;
import ca.mcmaster.se2aa4.island.team22.States.IDroneState;
public class Scan extends Action {

    public Scan(IDroneAction droneInterface) {
        super(droneInterface,ActionType.scan);     
    }
    
    @Override
    public String accept(IDroneState state){
        return state.visit(this);
    }
    
    @Override
    public String execute(Object... args) {
        return createAction(ActionType.scan);
    }
}