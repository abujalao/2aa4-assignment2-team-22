package ca.mcmaster.se2aa4.island.team22;

import java.util.*;

public class Maps {
    Drone drone;
    PointsOfInterest poi;
    private Map<Position, PointsOfInterest> map;

    public Maps(Drone drone, PointsOfInterest poi){
        this.drone = drone;
        this.poi = poi;

        map = new HashMap<>();
    }

    public void addPOI(){
        poi.storeScan();
        map.put(drone.getDronePosition(), poi);
    }
}
