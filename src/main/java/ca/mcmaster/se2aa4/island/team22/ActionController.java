package ca.mcmaster.se2aa4.island.team22;

import java.util.Map;

import org.json.JSONObject;

public class ActionController {
    String action = "";
    
    private void setAction(String action) {
        this.action = action;
    }

    public String getAction(){
        return action;
    }

    private String createAction(String action) { //if no parameters then no need to provide it (method overloading)
        return createAction(action,null);
    }
    private String createAction(String action, Map<String, Object> parameters) {
        JSONObject decision = new JSONObject(Map.of(
             "action", action,
             "parameters", parameters
        ));
         
        setAction(action);
        return decision.toString();
    }

    public String fly(){
        return createAction("fly");
    }
    
    public String echo(String dir) {
        return createAction("echo",Map.of("direction", dir));
    }
    
    public String scan(){
        return createAction("scan");
    }

    public String changeHeading(String dir){
        return createAction("heading",Map.of("direction", dir));
    }

    public String stop(){
        return createAction("stop");
    }

}