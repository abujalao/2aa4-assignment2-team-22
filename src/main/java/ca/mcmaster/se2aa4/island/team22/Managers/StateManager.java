package ca.mcmaster.se2aa4.island.team22.Managers;

import java.util.HashMap;
import java.util.Map;

import ca.mcmaster.se2aa4.island.team22.IDroneAction;
import ca.mcmaster.se2aa4.island.team22.States.FindLand;
import ca.mcmaster.se2aa4.island.team22.States.InterlaceScan;
import ca.mcmaster.se2aa4.island.team22.States.State;
public class StateManager {
    public enum DroneState {
        findLand,
        interlaceScan
    }

    private State currentState;
    private final IDroneAction droneInterface;
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
        return currentState.perform(droneInterface.getActionManagerInterface().getAction());
    }

}