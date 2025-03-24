package ca.mcmaster.se2aa4.island.team22.States;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ca.mcmaster.se2aa4.island.team22.Actions.Action;
import ca.mcmaster.se2aa4.island.team22.Actions.Echo;
import ca.mcmaster.se2aa4.island.team22.Actions.Fly;
import ca.mcmaster.se2aa4.island.team22.Actions.Heading;
import ca.mcmaster.se2aa4.island.team22.Actions.Scan;
import ca.mcmaster.se2aa4.island.team22.Actions.Stop;
import ca.mcmaster.se2aa4.island.team22.IActionManage;
import ca.mcmaster.se2aa4.island.team22.IDroneAction;
import ca.mcmaster.se2aa4.island.team22.IStorage;

public abstract class State implements IDroneState {
    protected  final IDroneAction droneInterface;
    protected IStorage storageInterface;
    protected IActionManage actionControlInterface;
    protected final Logger logger = LogManager.getLogger();

    public State(IDroneAction droneInterface,IStorage storageInterface,IActionManage actionControlInterface) {
        this.droneInterface = droneInterface;
        this.storageInterface = storageInterface;
        this.actionControlInterface = actionControlInterface;
    }

    protected IActionManage getActionControlInterface() {
        return actionControlInterface;
    }

    protected IStorage getStorageInterface() {
        return storageInterface;
    }

    public void checkNull() {
        if (getActionControlInterface() == null || storageInterface == null) {
            this.actionControlInterface = droneInterface.getActionManagerInterface();
            this.storageInterface = droneInterface.getStorageInterface();
        }
    }

    @Override
    public final String perform(Action action) { // Template method
        checkNull();
        return dispatchPerformCheck(action);
    }

    private String dispatchPerformCheck(Action action) {
        if (action instanceof Echo) {
            return performCheck((Echo) action);
        } else if (action instanceof Fly) {
            return performCheck((Fly) action);
        } else if (action instanceof Heading) {
            return performCheck((Heading) action);
        } else if (action instanceof Scan) {
            return performCheck((Scan) action);
        } else if (action instanceof Stop) {
            return performCheck((Stop) action);
        }
        throw new UnsupportedOperationException("Action type not supported: " + action.getClass().getSimpleName());
    }
    protected abstract String performCheck(Echo action);
    protected abstract String performCheck(Fly action);
    protected abstract String performCheck(Scan action);
    protected abstract String performCheck(Stop action);
    protected abstract String performCheck(Heading action);

}