package ca.mcmaster.se2aa4.island.team22.Actions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.mcmaster.se2aa4.island.team22.ActionManager.ActionType;
import ca.mcmaster.se2aa4.island.team22.IActionManage;
import ca.mcmaster.se2aa4.island.team22.IDroneAction;
import ca.mcmaster.se2aa4.island.team22.States.IDroneState;
public class Scan extends Action implements IActionCount {
    private IActionManage actionControlInterface;
    private int scanCount = 0; //monitor scans
    private final Logger logger = LogManager.getLogger();

    public Scan(IDroneAction droneInterface) {
        super(droneInterface,ActionType.scan);     
        this.actionControlInterface = droneInterface.getActionManagerInterface(); 
    }

    @Override
    public int getCount() {
        return scanCount;
    }   
    @Override
    public void incrementCount() {
        scanCount++;
    }  
    @Override
    public void resetCount() {
        scanCount = 0;
    }  

    @Override
    public String accept(IDroneState state){
        return state.perform(this);
    }
    
    @Override
    public String execute(Object... args) {
        return createAction(ActionType.scan);
    }
}