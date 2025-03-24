package ca.mcmaster.se2aa4.island.team22;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class PointsOfInterest implements IPoi{
    //reference for the storage class
    private final IStorage store;
    private final List<String> creeks= new ArrayList<>();
    private final List<String> biomes= new ArrayList<>();
    private final List<String> sites= new ArrayList<>();

    public PointsOfInterest(IStorage storage) {
        this.store = storage;
    }

    // Method to copy data from JSONArray to List
    private void copyArrayToList(JSONArray thisArray, List<String> list) {
        for (int i = 0; i < thisArray.length(); i++) {
            list.add(thisArray.getString(i));
        }
    }
    @Override
    public List<String> getSites(){
        return sites;
    }
    @Override
    public List<String> getCreeks(){
        return creeks;
    }
    // Store the scan data into lists
    @Override
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

    @Override
    public PointsOfInterest getPoi() {
        return this;
    }

}
