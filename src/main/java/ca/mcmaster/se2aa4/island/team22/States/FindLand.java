package ca.mcmaster.se2aa4.island.team22.States;


import ca.mcmaster.se2aa4.island.team22.Actions.Echo;
import ca.mcmaster.se2aa4.island.team22.Actions.Fly;
import ca.mcmaster.se2aa4.island.team22.Actions.Heading;
import ca.mcmaster.se2aa4.island.team22.Actions.Scan;
import ca.mcmaster.se2aa4.island.team22.Actions.Stop;
import ca.mcmaster.se2aa4.island.team22.IDroneAction;
import ca.mcmaster.se2aa4.island.team22.Managers.ActionManager.ActionType;
import ca.mcmaster.se2aa4.island.team22.Managers.StateManager.DroneState;
import ca.mcmaster.se2aa4.island.team22.Util.DirectionUtil;
public class FindLand extends State {
    private int count=0;
    public FindLand(IDroneAction droneInterface) {
        super(droneInterface, droneInterface.getStorageInterface(), droneInterface.getActionManagerInterface());
    }

    @Override
    protected String performCheck(Echo action) {
        logger.info("HERE IN ECHO CHECKKER");
        String[] availableDirs = droneInterface.availableDirections();

        if(storageInterface.getResult().equals("GROUND")){
            logger.info("FOUND GROUND");
            if(droneInterface.getDirection().equals(actionControlInterface.getPastParameter(ActionType.ECHO, "direction"))){
                storageInterface.decrementRange();
                return actionControlInterface.execute(ActionType.FLY);
            }
            else{
                storageInterface.decrementRange();
                String oppositeDir = DirectionUtil.Opposite_Directions.get(droneInterface.getDirection());
                logger.info("dir is:" + droneInterface.getDirection() + "Opposite is: " + oppositeDir);
                return actionControlInterface.execute(ActionType.HEADING,actionControlInterface.getPastParameter(ActionType.ECHO, "direction"));
            }
        }
        //if its looking at ground then fly towards it...
        else{
            //fly until you reach middle of map...
            logger.info("Im FLYINGGGGG");
            
            if (this.count > 1){
                count=0;
                return actionControlInterface.execute(ActionType.FLY);
            }
            int oldCount = count;
            count++;
            return actionControlInterface.execute(ActionType.ECHO, availableDirs[oldCount]);
        }
    }

    @Override
    protected String performCheck(Fly action) {
        String[] availableDirs = droneInterface.availableDirections();
        storageInterface.decrementRange();
        if (!storageInterface.getResult().equals("GROUND")) {
            // Get the directions available for the current position
            if (count == 0){ 
                count++;
                return actionControlInterface.execute(ActionType.ECHO,availableDirs[0]);
            }
            else{
                count = 0;
                return actionControlInterface.execute(ActionType.ECHO,availableDirs[1]);
            }

        } else {
            logger.info("range is: " + storageInterface.getRange());
            if(storageInterface.getRange() > -2){
                // If range is greater than 30, just keep flying and decrement range
                // and if you havent reached ground...
                return action.execute();
            }
            else if (storageInterface.getRange() < 0){ //You are in land now
                droneInterface.setDroneState(DroneState.interlaceScan); //change drone state to start scanning
                return actionControlInterface.execute(ActionType.SCAN);
            }
        } 
        return action.execute(); //test to make it stop
    }

    @Override
    protected String performCheck(Scan action) {
        return actionControlInterface.execute(ActionType.FLY);
    }
    @Override
    protected String performCheck(Heading action) {
        return actionControlInterface.execute(ActionType.FLY);
    }
    @Override
    protected String performCheck(Stop action) {
        return action.execute();
    }
}