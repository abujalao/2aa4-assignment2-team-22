package ca.mcmaster.se2aa4.island.team22;

import java.util.HashMap;
import java.util.Map;

public class Maps implements IMap{
    Drone drone;
    PointsOfInterest poi;
    private Map<Position, PointsOfInterest> map;
    private boolean creekFound;
    private boolean siteFound;

    public Maps(Drone drone, PointsOfInterest poi){
        this.drone = drone;
        this.poi = poi;

        map = new HashMap<>();
    }


    public boolean canSaveThem(){
        if(creekFound && siteFound){
            return true;
        }
        return false;
    }
    

    public void addPOI(){
        poi.storeScan();

         if (!poi.getCreeks().isEmpty()){
             creekFound = true;
         }
         if (!poi.getSites().isEmpty()){
             siteFound = true;
         }
        int[] pos = drone.getDronePosition();
        Position position = new Position(pos[0], pos[1]);
        map.put(position, poi);
    }
}
