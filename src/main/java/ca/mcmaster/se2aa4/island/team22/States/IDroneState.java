package ca.mcmaster.se2aa4.island.team22.States;


import ca.mcmaster.se2aa4.island.team22.Actions.Action;
public interface IDroneState {
    public String perform(Action action);
}