package ca.mcmaster.se2aa4.island.team22.States;

import ca.mcmaster.se2aa4.island.team22.ActionManager.ActionType;
import ca.mcmaster.se2aa4.island.team22.Actions.Echo;
import ca.mcmaster.se2aa4.island.team22.Actions.Fly;
import ca.mcmaster.se2aa4.island.team22.Actions.Heading;
import ca.mcmaster.se2aa4.island.team22.Actions.Scan;
import ca.mcmaster.se2aa4.island.team22.Actions.Stop;
import ca.mcmaster.se2aa4.island.team22.DirectionUtil;
import ca.mcmaster.se2aa4.island.team22.IDroneAction;

public class InterlaceScan extends State {
    int scanCounter=0;
    String oppositeDir = "";
    public InterlaceScan(IDroneAction droneInterface) {
        super(droneInterface, droneInterface.getStorageInterface(), droneInterface.getActionManagerInterface());
    }

    @Override
    protected String performCheck(Echo action) {
        return actionControlInterface.execute(ActionType.fly);
    }

    @Override
    protected String performCheck(Fly action) {
        String[] availableDirs = droneInterface.availableDirections();
        this.oppositeDir = "";
        storageInterface.decrementRange();
        if(droneInterface.getStartOppositeScanning() && !oppositeDir.equals("")){
            return actionControlInterface.execute(ActionType.heading,oppositeDir);
        }
        //i need getbiome from previousresponse
        if(storageInterface.getRange() > -1 && storageInterface.getResult().equals("GROUND")){
            return actionControlInterface.execute(ActionType.fly);
        }
        if(storageInterface.getRange() > -1 && !storageInterface.getResult().equals("GROUND")){
            droneInterface.setStartOppositeScanning(true);
            droneInterface.setChangeScanDir(true);
            this.oppositeDir = DirectionUtil.Opposite_Directions.get(droneInterface.getDirection());
            return actionControlInterface.execute(ActionType.heading,availableDirs[0]);
        }
        if (storageInterface.getResult().equals("GROUND")){
            if(droneInterface.getMapInterface().isInMap(droneInterface.getDronePosition())){
                logger.info("Already scanned, skipping scan");
                return actionControlInterface.execute(ActionType.fly);
            }
            return actionControlInterface.execute(ActionType.scan);
        }
        return action.execute();
    }

    @Override
    protected String performCheck(Scan action) {
        String[] availableDirs = droneInterface.availableDirections();

        if (actionControlInterface==null) {
            this.actionControlInterface=droneInterface.getActionManagerInterface();
        }
        droneInterface.getPoiInterface().storeScan(); 
        droneInterface.getMapInterface().addPOI();
        if (droneInterface.canSaveThem()){
            logger.info("Have found save places.");
            return actionControlInterface.execute(ActionType.stop); 
        }
        if(droneInterface.isbiomeOcean()){
            logger.info("Biome is ocean, turning...");
            this.oppositeDir = DirectionUtil.Opposite_Directions.get(droneInterface.getDirection());
            if (droneInterface.getChangeScanDir()){
                return actionControlInterface.execute(ActionType.heading,availableDirs[0]);
            }
            return actionControlInterface.execute(ActionType.heading,availableDirs[1]);
        }
        return actionControlInterface.execute(ActionType.fly);
    }
    @Override
    protected String performCheck(Heading action) {
        Scan scanAction = (Scan) actionControlInterface.getCountInterface(ActionType.scan);
        if (scanCounter == 0){
                scanCounter++;
                if(droneInterface.getStartOppositeScanning()){
                    return actionControlInterface.execute(ActionType.fly);
                }
                return actionControlInterface.execute(ActionType.heading,oppositeDir);
            }
            else{
                scanCounter=0;
                droneInterface.setStartOppositeScanning(false);
                return actionControlInterface.execute(ActionType.echo,droneInterface.getDirection());
            }
    }
    @Override
    protected String performCheck(Stop action) {
        return action.execute();
    }
}