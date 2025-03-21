package ca.mcmaster.se2aa4.island.team22;

import org.json.JSONObject;

public class ResponseStorage {
    private String result = "";
    private int range = -1;
    private int cost = -1;
    private boolean is_out_range;

    // public ResponseStorage(){
    // }

    public void clear(){
        result = "";
        range = -1;
        cost = -1;
    }

    private void setRange(Integer value){
        this.range=value;
    }

    public void decrementRange(){ //decrement by one
        setRange(this.range-1);
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

    public void storeResults(String decision, JSONObject prevResponse){
        JSONObject extraInfo = prevResponse.getJSONObject("extras");
        if(decision.equals("echo")){
            if (extraInfo.getString("found").equals("OUT_OF_RANGE")){
                clear();//clear only if its out of range because we are need to keep running echo until ground is found
            }
            range = extraInfo.getInt("range");
            result = extraInfo.getString("found");
        }
        cost = prevResponse.getInt("cost");
    }
}