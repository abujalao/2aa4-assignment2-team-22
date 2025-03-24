package ca.mcmaster.se2aa4.island.team22.Actions;
import ca.mcmaster.se2aa4.island.team22.States.IDroneState;
public interface IActionExecute {
    public abstract String execute(Object... parameters);
    public abstract String accept(IDroneState state);
}