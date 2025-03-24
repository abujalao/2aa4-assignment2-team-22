package ca.mcmaster.se2aa4.island.team22;
import ca.mcmaster.se2aa4.island.team22.Drone.DroneState;

public interface IDroneAction { //methods needed for fly action (!!bloated interface)
    public void moveDrone(int x, int y); //interface given to ActionController to edit the position of drone depending on the action such as fly() and heading()
    public DroneState getCurrentState();
    public IStorage getStorageInterface();
    public IActionManage getActionManagerInterface();
    public String getDirection();
    public String getInitialHeading();
    public String[] availableDirections();
    public int getDroneChecks();
    public void incrementDroneChecks();
    public void resetDroneChecks();
    public boolean getIslandFound();
    public void setIslandFound(boolean value);
    public boolean canSaveThem();
    public int[] getDronePosition();
    public int getDroneScan();
    public void incrementScan();
    public void resetScan();
    public IPoi getPoiInterface();
    public IMap getMapInterface();
    public Position getPos();
    public boolean isbiomeOcean();
    public void resetIslandFound();
    public boolean hasDroneScanned();

    //refactor these
    public boolean getStartOppositeScanning();
    public void setStartOppositeScanning(boolean state);
    public void setChangeScanDir(boolean state);
    public boolean getChangeScanDir();
}