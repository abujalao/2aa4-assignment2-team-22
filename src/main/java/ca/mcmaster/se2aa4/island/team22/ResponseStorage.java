package ca.mcmaster.se2aa4.island.team22;

import java.util.*;
import org.json.JSONObject;

public class ResponseStorage implements IStorage {
    private String result = "";
    private int range = -1;
    private int cost = -1;

    JSONObject prevResponse;

    private boolean onOcean = false;

    // public ResponseStorage(){
    // }

    public void clear(){
        result = "";
        range = -1;
        cost = -1;
    }

    public boolean isOnOcean(){
        return onOcean;
    }

    private void setRange(Integer value){
        this.range=value;
    }

    @Override
    public void decrementRange(){ //decrement by one
        setRange(this.range-1);
    }

    public int getCost(){
        return cost;
    }

    @Override
    public int getRange(){
        return range;
    }

    @Override
    public String getResult(){
        return result;
    }

    @Override
    public JSONObject getPrevResponse(){
        return prevResponse;
    }

    public IStorage getStorageInterface(){
        return this;
    }
    
    public void storeResults(String decision, JSONObject prevResponse){
        JSONObject extraInfo = prevResponse.getJSONObject("extras");
        this.prevResponse = prevResponse;
        
        if(decision.equals("echo")){
            if (extraInfo.getString("found").equals("OUT_OF_RANGE")){
                clear();//clear only if its out of range because we are need to keep running echo until ground is found
            }
            range = extraInfo.getInt("range");
            result = extraInfo.getString("found");
        }

        if(decision.equals("scan")){
            if (extraInfo.getJSONArray("biomes").length() == 1 && 
                extraInfo.getJSONArray("biomes").getString(0).equals("OCEAN")){
                onOcean = true;//clear only if its out of range because we are need to keep running echo until ground is found
            }else{
                onOcean = false;
            }
        }


        cost = prevResponse.getInt("cost");
    }
}