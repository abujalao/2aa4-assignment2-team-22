package ca.mcmaster.se2aa4.island.team22.Actions;

import ca.mcmaster.se2aa4.island.team22.ActionManager.ActionType;
import ca.mcmaster.se2aa4.island.team22.IActionManage;
import ca.mcmaster.se2aa4.island.team22.IDroneAction;

public class Scan extends Action {
    private IActionManage actionControlInterface;
    public Scan(IDroneAction droneInterface) {
        super(droneInterface);     
        this.actionControlInterface = droneInterface.getActionManagerInterface(); 
    }

    @Override
    public String execute(Object... args) {
        return createAction(ActionType.scan);
    }

    @Override
    public String checkMove() {
        if (actionControlInterface==null) {
            this.actionControlInterface=droneInterface.getActionManagerInterface();
        }
        return actionControlInterface.getAction(ActionType.stop).execute(); //This wasnt in under "fly" case
    }
}