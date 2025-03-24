package ca.mcmaster.se2aa4.island.team22.States;


import ca.mcmaster.se2aa4.island.team22.Actions.Action;
import ca.mcmaster.se2aa4.island.team22.Actions.Echo;
import ca.mcmaster.se2aa4.island.team22.Actions.Fly;
import ca.mcmaster.se2aa4.island.team22.Actions.Heading;
import ca.mcmaster.se2aa4.island.team22.Actions.Scan;
import ca.mcmaster.se2aa4.island.team22.Actions.Stop;
public interface IDroneState {
    public String perform(Action action);
    String visit(Echo echo);
    String visit(Fly fly);
    String visit(Heading heading);
    String visit(Scan scan);
    String visit(Stop stop);
}