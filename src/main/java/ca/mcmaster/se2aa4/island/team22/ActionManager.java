package ca.mcmaster.se2aa4.island.team22;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.team22.ActionManager.ActionType;
import ca.mcmaster.se2aa4.island.team22.Actions.*;

public class ActionManager implements IActionManage{

    public enum ActionType {
        echo, fly, scan, stop, heading;
    }
    
    private final Logger logger = LogManager.getLogger();
    final Map<ActionType, Action> actions = new HashMap<>(); //available actions to perform
    private Action action;
    private final Map<ActionType, Map> pastParameters = new HashMap<>(); //save last parameter given in createAction() call
    private final IDroneAction droneMoveInterface;

    public ActionManager(IDroneAction droneInterface) {
        this.droneMoveInterface = droneInterface;
        //initialize all actions manually
        actions.put(ActionType.heading, new Heading(droneInterface));
        actions.put(ActionType.echo, new Echo(droneInterface));
        actions.put(ActionType.fly, new Fly(droneInterface));
        actions.put(ActionType.stop, new Stop(droneInterface));
        actions.put(ActionType.scan, new Scan(droneInterface));
    }

    @Override
    public void setAction(Action action) {
        this.action = action;
    }

    public String executeCurrentAction() {
        return action.execute();
    }
    public String executeCurrentAction(String dir) {
        return action.execute(dir);
    }

    @Override
    public String createAction(ActionType action, Map<String, Object> parameters) {
        JSONObject decision = new JSONObject(Map.of(
             "action", action
        ));

        if (parameters != null && !parameters.isEmpty()) {
            pastParameters.put(action,parameters);
            decision.put("parameters", parameters);
        }
        
        setAction(actions.get(action));
        return decision.toString();
    }

    @Override
    public void putPastParameter(ActionType action, Map<String, Object> parameters) {
        pastParameters.put(action,parameters);
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

    @Override
    public Action getAction(ActionType actionName){ //get action using action enum
        return actions.get(actionName);
    }

    @Override
    public Action getAction(){
        return action;
    }


    @Override
    public IActionManage getActionInterface(){
        return this;
    }

    private Map getParametersMap(ActionType action) {
        return pastParameters.getOrDefault(action, null);
    }
}