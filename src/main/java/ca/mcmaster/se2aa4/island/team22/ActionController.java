package ca.mcmaster.se2aa4.island.team22;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;


public class ActionController {
    private final Logger logger = LogManager.getLogger();
    private String action = "";
    private final Map<String, Map> pastParameters = new HashMap<>(); //save last parameter given in createAction() call
    private final IDroneMove droneMoveInterface;

    public ActionController(IDroneMove moveInterface) {
        this.droneMoveInterface = moveInterface;
    }
    private void setAction(String action) {
        this.action = action;
    }

    public void setDirectionParameter(String direction){ //When we initialize drone we need to use this method to save the direction in pastParameters
        pastParameters.put("heading",Map.of("direction",direction));
    }

    public String getPastParameter(String action, String parameter) {
        Map map = getParametersMap(action);
        if (map!=null) {
            return (String)map.getOrDefault(parameter,"");
        }
        return "";
    }

    public String getAction(){
        return action;
    }

    private Map getParametersMap(String action) {
        return pastParameters.getOrDefault(action, null);
    }

    private String createAction(String action) { //if no parameters then no need to provide it (method overloading)
        return createAction(action,null);
    }
    private String createAction(String action, Map<String, Object> parameters) {
        JSONObject decision = new JSONObject(Map.of(
             "action", action
        ));

        if (parameters != null && !parameters.isEmpty()) {
            pastParameters.put(action,parameters);
            decision.put("parameters", parameters);
        }
        
        setAction(action);
        return decision.toString();
    }
    
    //ACTIONS:
    public String fly(){
        String Direction = getPastParameter("heading","direction"); //get current drone direction
        int[] stepAmount = DirectionUtil.Fly_Increment.get(Direction); //amount of steps to add in position (x,y)
        droneMoveInterface.moveDrone(stepAmount[0],stepAmount[1]);
        return createAction("fly");
    }
    
    public String heading(String dir){
        String currentDir = getPastParameter("heading","direction"); //get current drone direction
        int[] stepAmountCurrent = DirectionUtil.Fly_Increment.get(currentDir); //amount of steps in current direction 
        int[] stepAmountNew = DirectionUtil.Fly_Increment.get(dir); //amount of steps in new direction
        logger.info("Change direction from {} to {}. Move step is ({},{})",currentDir,dir,stepAmountCurrent[0]+stepAmountNew[0],stepAmountCurrent[1]+stepAmountNew[1]);
        droneMoveInterface.moveDrone(stepAmountCurrent[0]+stepAmountNew[0],stepAmountCurrent[1]+stepAmountNew[1]);//When turning around the drone will take a step in the current direction + the direction where it wants to go. If im north and its going east it will take one step north + one step east
        return createAction("heading",Map.of("direction", dir));
    }

    public String echo(String dir) {
        return createAction("echo",Map.of("direction", dir));
    }
    
    public String scan(){
        return createAction("scan");
    }

    public String stop(){
        return createAction("stop");
    }

    public String land(String creekID, int people) {
        return createAction("land",Map.of(
            "creek", creekID ,
            "people", people
        ));
    }

    public String move_to(String dir) {
        return createAction("move_to",Map.of("direction", dir));
    }

    public String scout(String dir) {
        return createAction("scout",Map.of("direction", dir));
    }

    public String glimpse(String dir, int range) {
        return createAction("glimpse",Map.of(
            "direction", dir ,
            "range", range
        ));
    }

    public String explore(){
        return createAction("explore");
    }

    public String exploit(String resource) {
        return createAction("exploit",Map.of("resource", resource));
    }
}