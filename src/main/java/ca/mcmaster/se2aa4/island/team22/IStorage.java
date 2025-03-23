package ca.mcmaster.se2aa4.island.team22;

import org.json.JSONObject;

public interface IStorage {
    public JSONObject getPrevResponse(); //interface given to ActionController to edit the position of drone depending on the action such as fly() and heading()
    public int getRange();
    public void decrementRange();
    public String getResult();
}