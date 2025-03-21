package ca.mcmaster.se2aa4.island.team22;

import org.json.JSONObject;

public class ResponseStorage {
    private String result;
    private int range;
    private int cost;
    private String echoDir;

    private String prevDecision;

    public ResponseStorage(){
        clear();
    }

    public void clear(){
        result = "";
        range = -1;
        cost = -1;
        prevDecision = "";
        echoDir = "";
    }

    public int getCost(){
        return cost;
    }

    public int getRange(){
        return range;
    }

    public String getResult(){
        return result;
    }

    public String getPrevAction(){
        return prevDecision;
    }

    public String getechoDir(){
        return echoDir;
    }

    public void storeResults(String decision, JSONObject prevResponse){
        clear();
        cost = prevResponse.getInt("cost");

        if(decision.equals("echo")){
            range = prevResponse.getJSONObject("extras").getInt("range");
            result = prevResponse.getJSONObject("extras").getString("found");
            echoDir = prevResponse.getJSONObject("parameters").getString("direction");
        }

        
        prevDecision = decision;

    }
}
