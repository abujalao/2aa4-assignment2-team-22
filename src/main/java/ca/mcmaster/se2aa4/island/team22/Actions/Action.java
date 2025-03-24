package ca.mcmaster.se2aa4.island.team22.Actions;

import java.util.Map;

import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team22.ActionManager.ActionType;
import ca.mcmaster.se2aa4.island.team22.IDroneAction;

public abstract class Action implements IActionExecute, IActionCheck {
    protected final IDroneAction droneInterface;
    //!!Want to add past parameter for each action
    public Action(IDroneAction droneInterface) {
        this.droneInterface = droneInterface;
    }

    public String createAction(ActionType action) { //if no parameters then no need to provide it (method overloading)
        return createAction(action,null);
    }
    public String createAction(ActionType action, Map<String, Object> parameters) {
        droneInterface.getActionManagerInterface().createAction(action,parameters); //temporary
        JSONObject decision = new JSONObject(Map.of(
             "action", action
        ));
        
        if (parameters != null && !parameters.isEmpty()) {
            decision.put("parameters", parameters);
        }
        droneInterface.getActionManagerInterface().setAction(this);//!!want to set it for each action

        return decision.toString();
    }

    public String getActionString(){
        return this.getClass().getSimpleName().toLowerCase();
    }

    public IDroneAction getDroneInterface() {
        return droneInterface;
    }
    @Override
    public abstract String execute(Object... parameters);
    @Override
    public abstract String checkMove();
}