package ca.mcmaster.se2aa4.island.team22;
import ca.mcmaster.se2aa4.island.team22.Drone.DroneState;

public interface IDroneAction { //methods needed for fly action (!!bloated interface)
    public void moveDrone(int x, int y); //interface given to ActionController to edit the position of drone depending on the action such as fly() and heading()

    public String[] availableDirections();
    
    public void setFoundLand(boolean value); //X related to drone state
    public boolean canSaveThem();
    
    public boolean isbiomeOcean();
    public void resetDroneLand(); //X related to drone state
    
    //interface getters
    public IStorage getStorageInterface(); 
    public IActionManage getActionManagerInterface();
    public IPoi getPoiInterface();
    public IMap getMapInterface();

    //getters
    public DroneState getCurrentState(); //X related to drone state
    public String getDirection();
    public String getInitialHeading();
    public boolean getFoundLand(); //X related to drone state
    public int[] getDronePosition();
    //refactor below
    public boolean getStartOppositeScanning();
    public boolean getChangeScanDir();
    public void setStartOppositeScanning(boolean state);
    public void setChangeScanDir(boolean state);
}