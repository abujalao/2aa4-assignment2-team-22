package ca.mcmaster.se2aa4.island.team22.Util;
import java.util.Map;

public class DirectionUtil {
    public enum ActionType {
        echo, fly, scan, stop, heading;
    }
    public static final Map<String, String> Opposite_Directions = Map.of(
        "N", "S",
        "S", "N",
        "E", "W",
        "W", "E"
    );
    public static final Map<String, String[]> Available_Directions = Map.of( //available directions are directions that are not equal to current direction & opposite direction
        "N", new String[]{"W", "E"},
        "S", new String[]{"W", "E"},
        "E", new String[]{"N", "S"},
        "W", new String[]{"N", "S"}
    );
    public static final Map<String, int[]> Fly_Increment = Map.of( //Position amount to add for each direction on fly() aciton [(x,y) (horizontal,vertical)]
        "N", new int[]{0,-1},  //game take position reference point from top left as (1,1) so as you go right and down, x and y increase 
        "S", new int[]{0,1},
        "E", new int[]{1,0},
        "W", new int[]{-1,0}
    );
}