package ca.mcmaster.se2aa4.island.team22.States;

import ca.mcmaster.se2aa4.island.team22.Actions.Echo;
import ca.mcmaster.se2aa4.island.team22.Actions.Fly;
import ca.mcmaster.se2aa4.island.team22.Actions.Heading;
import ca.mcmaster.se2aa4.island.team22.Actions.Scan;
import ca.mcmaster.se2aa4.island.team22.Actions.Stop;
import ca.mcmaster.se2aa4.island.team22.IDroneAction;
import ca.mcmaster.se2aa4.island.team22.Managers.ActionManager.ActionType;
import ca.mcmaster.se2aa4.island.team22.Util.DirectionUtil;


public class InterlaceScan extends State {
    private int scanCounter=0;
    private String oppositeDir = "";
    private boolean oppositeScan = false;
    private boolean ChangeScanDir= false;
    public InterlaceScan(IDroneAction droneInterface) {
        super(droneInterface, droneInterface.getStorageInterface(), droneInterface.getActionManagerInterface());
    }

    @Override
    protected String performCheck(Echo action) {
        if (storageInterface.getResult().equals("OUT_OF_RANGE") && droneInterface.getDirection().equals(actionControlInterface.getPastParameter(ActionType.ECHO, "direction")) && storageInterface.getRange() <=2) {
            return actionControlInterface.execute(ActionType.STOP);
        }
        return actionControlInterface.execute(ActionType.FLY);
    }

    @Override
    protected String performCheck(Fly action) {
        String[] availableDirs = droneInterface.availableDirections();
        this.oppositeDir = "";
        storageInterface.decrementRange();
        if(oppositeScan && !oppositeDir.equals("")){
            return actionControlInterface.execute(ActionType.HEADING,oppositeDir);
        }
        //i need getbiome from previousresponse
        if(storageInterface.getRange() > -1 && storageInterface.getResult().equals("GROUND")){
            return actionControlInterface.execute(ActionType.FLY);
        }
        if(storageInterface.getRange() > -1 && !storageInterface.getResult().equals("GROUND")){
            this.oppositeScan=true;
            this.ChangeScanDir=true;
            this.oppositeDir = DirectionUtil.Opposite_Directions.get(droneInterface.getDirection());
            return actionControlInterface.execute(ActionType.HEADING,availableDirs[0]);
        }
        if (storageInterface.getResult().equals("GROUND")){
            if(droneInterface.getMapInterface().isInMap(droneInterface.getDronePosition())){
                logger.info("Already scanned, skipping scan");
                return actionControlInterface.execute(ActionType.FLY);
            }
            return actionControlInterface.execute(ActionType.SCAN);
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
            return actionControlInterface.execute(ActionType.STOP); 
        }
        if(storageInterface.isOnOcean()){
            logger.info("Biome is ocean, turning...");
            this.oppositeDir = DirectionUtil.Opposite_Directions.get(droneInterface.getDirection());
            if (ChangeScanDir){
                return actionControlInterface.execute(ActionType.HEADING,availableDirs[0]);
            }
            return actionControlInterface.execute(ActionType.HEADING,availableDirs[1]);
        }
        return actionControlInterface.execute(ActionType.FLY);
    }
    @Override
    protected String performCheck(Heading action) {
        if (scanCounter == 0){
                scanCounter++;
                if(oppositeScan){
                    return actionControlInterface.execute(ActionType.FLY);
                }
                return actionControlInterface.execute(ActionType.HEADING,oppositeDir);
            }
            else{
                scanCounter=0;
                this.oppositeScan=false;
                return actionControlInterface.execute(ActionType.ECHO,droneInterface.getDirection());
            }
    }
    @Override
    protected String performCheck(Stop action) {
        return action.execute();
    }
}