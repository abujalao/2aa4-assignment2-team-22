package ca.mcmaster.se2aa4.island.team22.Managers;

import java.util.Map;

import ca.mcmaster.se2aa4.island.team22.Actions.Action;
import ca.mcmaster.se2aa4.island.team22.Managers.ActionManager.ActionType;
public interface IActionManage { //methods needed for fly action
    public IActionManage getActionInterface();
    public String getPastParameter(ActionType action, String parameter);
    public void putPastParameter(ActionType action, Map<String, Object> parameters);
    public void setAction(ActionType action);
    public String execute(ActionType action, Object... parameters);
    public Action getAction();
}