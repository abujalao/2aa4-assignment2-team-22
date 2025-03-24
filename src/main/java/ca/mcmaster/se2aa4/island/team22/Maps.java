package ca.mcmaster.se2aa4.island.team22;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Maps implements IMap{
    private final Logger logger = LogManager.getLogger();
    private IDroneAction drone;
    private IPoi poi;
    private final HashSet<Position> scannedPositions = new HashSet();
    private final Map<String, Position> mapCreeks = new HashMap();
    private final Map<String, Position> mapSites= new HashMap();
    private boolean creekFound;
    private boolean siteFound;

    public Maps(IDroneAction drone, IPoi poi){
        this.drone = drone;
        this.poi = poi;
    }


    public boolean canSaveThem(){
        if(creekFound && siteFound){
            return true;
        }
        return false;
    }

    public boolean isInMap(int[] pos){ //check if the position 2d array is in the map
        Position position = new Position(pos[0], pos[1]);
        return scannedPositions.contains(position);
    }
    
    @Override
    public void addPOI(){
        int[] pos = drone.getDronePosition();
        Position position = new Position(pos[0], pos[1]);
        poi.storeScan();

        if (!poi.getCreeks().isEmpty()){
            creekFound = true;
            for (String creek : poi.getCreeks()){
                mapCreeks.put(creek, position);
            }
        }
        if (!poi.getSites().isEmpty()){
            siteFound = true;
            for (String site : poi.getSites()){
                mapSites.put(site, position);
            }
        }
        
        scannedPositions.add(position);
    }

    @Override
    public void printMap(){
        StringBuilder Positions = new StringBuilder();
        for (Position pos : scannedPositions) {
            if (Positions.length() > 0) {
                Positions.append(", ");
            }
            Positions.append(pos.getStringPosition());
        }

        logger.info("Scanned positions: {}",Positions);
        logger.info("Scanned Creeks: ");
        for (Map.Entry<String, Position> creek : mapCreeks.entrySet()){
            logger.info("Creek: {} at position ({}, {})", creek.getKey(), creek.getValue().getX(), creek.getValue().getY());
        }
        logger.info("Scanned Sites: ");
        for (Map.Entry<String, Position> site : mapSites.entrySet()){
            logger.info("Site: {} at position ({}, {})", site.getKey(), site.getValue().getX(), site.getValue().getY());
        }
        getClosestCreekToSite();
    }

    public String getClosestCreekToSite() { //use scanned creeks and sites to find the closest creek to the site
        if (mapCreeks.isEmpty() || mapSites.isEmpty()) {
            logger.info("No creeks or sites found.");
            return "";
        }
        double minDistance = Double.MAX_VALUE; // Initialize a large value to compare 
        String closestCreek = "";
        String closestSite = "";
        for (Map.Entry<String, Position> site : mapSites.entrySet()) {
            for (Map.Entry<String, Position> creek : mapCreeks.entrySet()) {
                double distance = site.getValue().getDistanceBetween(creek.getValue()); //get the distance between the site and creek
                if (distance < minDistance) {
                    minDistance = distance;
                    closestCreek = creek.getKey();
                    closestSite = site.getKey();
                }
            }
        }
        logger.info("The closest scanned creek to site: {} is creek: {} with distance of {}", closestSite, closestCreek,minDistance);
        return closestCreek;
}
    


}
