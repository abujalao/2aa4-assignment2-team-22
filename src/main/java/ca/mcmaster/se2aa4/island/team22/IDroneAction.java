package ca.mcmaster.se2aa4.island.team22;
import ca.mcmaster.se2aa4.island.team22.Managers.IActionManage;
import ca.mcmaster.se2aa4.island.team22.Managers.StateManager.DroneState;
public interface IDroneAction {
    public void moveDrone(int x, int y); //interface given to ActionController to edit the position of drone depending on the action such as fly() and heading()

    public String[] availableDirections();
    public boolean canSaveThem();
    
    //interface getters
    public IStorage getStorageInterface(); 
    public IActionManage getActionManagerInterface();
    public IPoi getPoiInterface();
    public IMap getMapInterface();

    //getters
    public String getDirection();
    public String getInitialHeading();
    
    public int[] getDronePosition();
    public void setDroneState(DroneState state);
}