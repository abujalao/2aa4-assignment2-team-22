package ca.mcmaster.se2aa4.island.team22.Actions;

import java.util.Map;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team22.IDroneAction;
import ca.mcmaster.se2aa4.island.team22.Managers.ActionManager.ActionType;
import ca.mcmaster.se2aa4.island.team22.States.IDroneState;

public abstract class Action implements IActionExecute {
    protected final IDroneAction droneInterface;
    private final ActionType actionType; 
    //!!Want to add past parameter for each action
    public Action(IDroneAction droneInterface, ActionType actionType) {
        this.droneInterface = droneInterface;
        this.actionType = actionType;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public String createAction(ActionType action) { //if no parameters then no need to provide it (method overloading)
        return createAction(action,null);
    }
    public String createAction(ActionType action, Map<String, Object> parameters) {
        droneInterface.getActionManagerInterface().putPastParameter(action,parameters);
        JSONObject decision = new JSONObject(Map.of(
             "action", action.toString().toLowerCase()
        ));
        
        if (parameters != null && !parameters.isEmpty()) {
            decision.put("parameters", parameters);
        }
        return decision.toString();
    }

    public String getActionString(){
        return this.getClass().getSimpleName().toLowerCase();
    }

    public IDroneAction getDroneInterface() {
        return droneInterface;
    }
    @Override
    public abstract Action createNew();
    @Override
    public abstract String execute(Object... parameters);
    @Override
    public abstract String accept(IDroneState state);
}