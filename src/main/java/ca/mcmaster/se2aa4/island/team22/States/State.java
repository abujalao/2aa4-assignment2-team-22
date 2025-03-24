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
    public  final IDroneAction droneInterface;
    public IStorage storageInterface;
    public IActionManage actionControlInterface;
    public final Logger logger = LogManager.getLogger();

    public State(IDroneAction droneInterface,IStorage storageInterface,IActionManage actionControlInterface) {
        this.droneInterface = droneInterface;
        this.storageInterface = storageInterface;
        this.actionControlInterface = actionControlInterface;
    }

    public IActionManage getActionControlInterface() {
        return actionControlInterface;
    }

    public IStorage getStorageInterface() {
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
        return action.accept(this);
    }

   @Override
    public String visit(Echo echo) { //Visitor pattern
        return performCheck(echo);
    }

    @Override
    public String visit(Fly fly) {
        return performCheck(fly);
    }

    @Override
    public String visit(Heading heading) {
        return performCheck(heading);
    }

    @Override
    public String visit(Scan scan) {
        return performCheck(scan);
    }

    @Override
    public String visit(Stop stop) {
        return performCheck(stop);
    }

    protected abstract String performCheck(Echo action);
    protected abstract String performCheck(Fly action);
    protected abstract String performCheck(Scan action);
    protected abstract String performCheck(Stop action);
    protected abstract String performCheck(Heading action);

}