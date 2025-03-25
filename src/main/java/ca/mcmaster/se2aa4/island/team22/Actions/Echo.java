package ca.mcmaster.se2aa4.island.team22.Actions;

import java.util.Map;

import ca.mcmaster.se2aa4.island.team22.IDroneAction;
import ca.mcmaster.se2aa4.island.team22.Managers.ActionManager.ActionType;
import ca.mcmaster.se2aa4.island.team22.States.IDroneState;

public class Echo extends Action {
    public Echo(IDroneAction droneInterface) {
        super(droneInterface,ActionType.ECHO);
    }

    @Override
    public Action createNew() {
        return new Echo(droneInterface);
    }
   
    @Override
    public String accept(IDroneState state){
        return state.visit(this);
    }
    
    @Override
    public String execute(Object... args) {
        if (args.length > 0 && args[0] instanceof String direction) {
            return createAction(ActionType.ECHO,Map.of("direction", direction));
        } else {
            throw new IllegalArgumentException("Echo action requires a direction argument.");
        }
    }
}