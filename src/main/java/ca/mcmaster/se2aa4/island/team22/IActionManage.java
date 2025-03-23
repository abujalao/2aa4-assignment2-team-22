package ca.mcmaster.se2aa4.island.team22;

import java.util.Map;

import ca.mcmaster.se2aa4.island.team22.ActionManager.ActionType;
import ca.mcmaster.se2aa4.island.team22.Actions.Action;
public interface IActionManage { //methods needed for fly action
    public IActionManage getActionInterface();
    public String getPastParameter(ActionType action, String parameter);
    public String createAction(ActionType action, Map<String, Object> parameters);
    public void putPastParameter(ActionType action, Map<String, Object> parameters);
    public void setAction(Action action);
    public Action getAction(ActionType actionName);
    public Action getAction();
}