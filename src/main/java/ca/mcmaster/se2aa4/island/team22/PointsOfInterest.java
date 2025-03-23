package ca.mcmaster.se2aa4.island.team22;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class PointsOfInterest {
    //reference for the storage class
    private final ResponseStorage store;
    private List<String> creeks;
    private List<String> biomes;
    private List<String> sites;

    public PointsOfInterest(ResponseStorage storage) {
        // Set the storage
        this.store = storage;
        // Initialize lists
        this.creeks = new ArrayList<>();
        this.biomes = new ArrayList<>();
        this.sites = new ArrayList<>();
    }

    // Method to copy data from JSONArray to List
    private void copyArrayToList(JSONArray thisArray, List<String> list) {
        for (int i = 0; i < thisArray.length(); i++) {
            list.add(thisArray.getString(i));
        }
    }

    public List<String> getSites(){
        return sites;
    }
    public List<String> getCreeks(){
        return creeks;
    }

    // Store the scan data into lists
    public void storeScan() {
        JSONObject response = store.getPrevResponse();
        JSONObject extraInfo = response.getJSONObject("extras");
        // Clear the lists to reset any previous data
        creeks.clear();
        biomes.clear();
        sites.clear();
        // Add the elements to the corresponding lists
        copyArrayToList(extraInfo.getJSONArray("biomes"), biomes);
        copyArrayToList(extraInfo.getJSONArray("sites"), sites);
        copyArrayToList(extraInfo.getJSONArray("creeks"), creeks);
    }

}
