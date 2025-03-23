package ca.mcmaster.se2aa4.island.team22;
import ca.mcmaster.se2aa4.island.team22.Drone.DroneState;

public interface IDroneAction { //methods needed for fly action
    public void moveDrone(int x, int y); //interface given to ActionController to edit the position of drone depending on the action such as fly() and heading()
    public DroneState getCurrentState();
    public IStorage getStorageInterface();
    public IActionManage getActionManagerInterface();
    public String getDirection();
    public String[] availableDirections();
}