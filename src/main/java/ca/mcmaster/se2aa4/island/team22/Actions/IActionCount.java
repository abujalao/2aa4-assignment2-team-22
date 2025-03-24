package ca.mcmaster.se2aa4.island.team22.Actions;

public interface IActionCount {
    public int getCount(); //check previous actions to determine future move
    public void incrementCount(); //increment count
    public void resetCount(); //reset count
}