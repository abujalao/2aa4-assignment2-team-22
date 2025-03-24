package ca.mcmaster.se2aa4.island.team22.Actions;

import java.util.Map;

import ca.mcmaster.se2aa4.island.team22.ActionManager.ActionType;
import ca.mcmaster.se2aa4.island.team22.IActionManage;
import ca.mcmaster.se2aa4.island.team22.IDroneAction;
import ca.mcmaster.se2aa4.island.team22.IStorage;
import ca.mcmaster.se2aa4.island.team22.States.IDroneState;

public class Echo extends Action implements IActionCount {
    //private final Logger logger = LogManager.getLogger();
    private IActionManage actionControlInterface;
    private IStorage storageInterface;
    private int echoCount = 0;
    public Echo(IDroneAction droneInterface) {
        super(droneInterface,ActionType.echo);
        this.storageInterface = droneInterface.getStorageInterface();
    }
    
    @Override
    public int getCount() {
        return echoCount;
    }   

    @Override
    public void incrementCount() {
        echoCount++;
    }  

    @Override
    public String accept(IDroneState state){
        return state.perform(this);
    }

    @Override
    public void resetCount() {
        echoCount = 0;
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
        if (args.length > 0 && args[0] instanceof String direction) {
            return createAction(ActionType.echo,Map.of("direction", direction));
        } else {
            throw new IllegalArgumentException("Echo action requires a direction argument.");
        }
    }
}