package ca.mcmaster.se2aa4.island.team22.Actions;

import ca.mcmaster.se2aa4.island.team22.ActionManager.ActionType;
import ca.mcmaster.se2aa4.island.team22.DirectionUtil;
import ca.mcmaster.se2aa4.island.team22.Drone;
import ca.mcmaster.se2aa4.island.team22.IActionManage;
import ca.mcmaster.se2aa4.island.team22.IDroneAction;
import ca.mcmaster.se2aa4.island.team22.IStorage;

public class Fly extends Action {
    private IStorage storageInterface;
    private IActionManage actionControlInterface;
    int checks=0;
    public Fly(IDroneAction droneInterface) {
        super(droneInterface);
        this.storageInterface = droneInterface.getStorageInterface();
        this.actionControlInterface = droneInterface.getActionManagerInterface();
        
    }

    public void checkNull() {
        if (actionControlInterface==null||storageInterface==null) {
            actionControlInterface = getDroneInterface().getActionManagerInterface();
            storageInterface = getDroneInterface().getStorageInterface();
        }
    }

    @Override
    public String execute(Object... args) {
        checkNull();
        String Direction = actionControlInterface.getPastParameter(ActionType.heading,"direction"); //ISTORAGE METHOD
        int[] stepAmount = DirectionUtil.Fly_Increment.get(Direction); //amount of steps to add in position (x,y)
        getDroneInterface().moveDrone(stepAmount[0],stepAmount[1]); //DRONE INTERFACE
        return createAction(ActionType.fly);
    }

    @Override
    public String checkMove(String[] availableDirs) {
        checkNull();
        if (getDroneInterface().getCurrentState() == Drone.DroneState.find_island) {
                // When range is less than 30, echo in one direction at a time
                if (storageInterface.getRange() < 30 && !storageInterface.getResult().equals("GROUND")) {
                    // Get the directions available for the current position
                    if (checks == 0){
                        checks++;
                        return actionControlInterface.getAction(ActionType.echo).execute(availableDirs[0]);
                    }
                    else{
                        checks = 0;
                        return actionControlInterface.getAction(ActionType.echo).execute(availableDirs[1]);
                    }

                } else {
                    if(storageInterface.getRange() != 0){
                        // If range is greater than 30, just keep flying and decrement range
                        // and if you havent reached ground...
                        storageInterface.decrementRange();
                        return this.execute();
                    }
                    if (storageInterface.getRange() == 0){
                        return actionControlInterface.getAction(ActionType.scan).execute();
                    }
                } 
        }  
        return this.execute(); //This wasnt in under "fly" case
    }
}