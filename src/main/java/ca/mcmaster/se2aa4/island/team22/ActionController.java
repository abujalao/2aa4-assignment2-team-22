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
    String parameter = "";
    
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
        return createAction("fly");
    }
    
    public String heading(String dir){
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