package ca.mcmaster.se2aa4.island.team22;

import java.util.HashMap;
import java.util.Map;

import ca.mcmaster.se2aa4.island.team22.States.FindLand;
import ca.mcmaster.se2aa4.island.team22.States.InterlaceScan;
import ca.mcmaster.se2aa4.island.team22.States.State;
public class StateManager {
    public enum DroneState {
        findLand,
        interlaceScan
    }

    private State currentState;
    private IDroneAction droneInterface;
    private final Map<DroneState, State> states = new HashMap<>(); //available actions to perform

    public StateManager(IDroneAction droneInterface) {
        this.droneInterface = droneInterface;
        states.put(DroneState.findLand,new FindLand(droneInterface));
        states.put(DroneState.interlaceScan,new InterlaceScan(droneInterface));
    }


    public void setState(DroneState state) {
        this.currentState = states.get(state);
    }

    public String performMove() {
        return currentState.perform(droneInterface.getActionManagerInterface().getAction()); //FIX THIS I SHOULDNT ACCESS THE CLASS (maybe use getter that give string and try to make it state type)
    }

    public String getState() { //return current state as string
        return states.get(currentState).toString(); 
    }

}