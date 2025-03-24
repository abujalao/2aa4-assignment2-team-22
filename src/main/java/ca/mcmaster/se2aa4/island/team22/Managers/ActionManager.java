package ca.mcmaster.se2aa4.island.team22.Managers;

import java.util.HashMap;
import java.util.Map;

import ca.mcmaster.se2aa4.island.team22.Actions.Action;
import ca.mcmaster.se2aa4.island.team22.Actions.Echo;
import ca.mcmaster.se2aa4.island.team22.Actions.Fly;
import ca.mcmaster.se2aa4.island.team22.Actions.Heading;
import ca.mcmaster.se2aa4.island.team22.Actions.Scan;
import ca.mcmaster.se2aa4.island.team22.Actions.Stop;
import ca.mcmaster.se2aa4.island.team22.IDroneAction;
import ca.mcmaster.se2aa4.island.team22.Managers.ActionManager.ActionType;
public class ActionManager implements IActionManage {

    public enum ActionType {
        echo, fly, scan, stop, heading;
    }
    
    private final Map<ActionType, Action> actions = new HashMap<>();
    private Action action;
    private final Map<ActionType, Map> pastParameters = new HashMap<>(); //save last parameter given in createAction() call

    public ActionManager(IDroneAction droneInterface) {
        //initialize all actions manually
        actions.put(ActionType.heading, new Heading(droneInterface));
        actions.put(ActionType.echo, new Echo(droneInterface));
        actions.put(ActionType.fly, new Fly(droneInterface));
        actions.put(ActionType.stop, new Stop(droneInterface));
        actions.put(ActionType.scan, new Scan(droneInterface));
    }

    @Override
    public void setAction(ActionType action) {
        this.action = getAction(action);
    }

    public void processAction(ActionType action, Map<String, Object> parameters) {
        if (parameters != null && !parameters.isEmpty()) {
            pastParameters.put(action,parameters);
        }
    }

    @Override
    public void putPastParameter(ActionType action, Map<String, Object> parameters) {
         if (parameters != null && !parameters.isEmpty()) {
            pastParameters.put(action,parameters);
        }
    }

    public void setDirectionParameter(String direction){ //When we initialize drone we need to use this method to save the direction in pastParameters
        pastParameters.put(ActionType.heading,Map.of("direction",direction));
    }

    @Override
    public String getPastParameter(ActionType action, String parameter) {
        Map map = getParametersMap(action);
        if (map!=null) {
            return (String)map.getOrDefault(parameter,"");
        }
        return "";
    }

    private Action getAction(ActionType actionName){ 
        return actions.get(actionName);
    }

    @Override
    public Action getAction(){
        return action.createNew();
    }

    public String getCurrentAction(){ //get current action in String format
        return action.getActionString();
    }

    @Override
    public String execute(ActionType action, Object... parameters) {
        String executeResult = getAction(action).execute(parameters);
        setAction(action);
        return executeResult;
    }
    @Override
    public IActionManage getActionInterface(){
        return this;
    }

    private Map getParametersMap(ActionType action) {
        return pastParameters.getOrDefault(action, null);
    }
}