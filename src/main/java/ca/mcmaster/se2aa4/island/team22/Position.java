package ca.mcmaster.se2aa4.island.team22;

import java.util.Objects;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setPosition(int x){
        this.x = x;
    }
    public void setPosition(Integer x, Integer y) {
    if (x != null) this.x = x;
    if (y != null) this.y = y;
    }

    public int[] getPosition(){ //get position by returning 2d list
        return new int[] {x, y};
    }

    public int getX(){ 
        return x;
    }

    public int getY(){ 
        return y;
    }

    public String getStringPosition(){
        return "("+String.valueOf(x) +","+ String.valueOf(y)+")";
    }

    @Override
    public boolean equals(Object object) { //Custom .equals()
        if (this == object) return true; //compare object to it self in memory to check if its identical
        if (object == null || getClass() != object.getClass()) //if object doesnt exist or not same class
            return false; 
        Position position = (Position) object; //object casting
        return x == position.x && y == position.y; //compare the current object position with the other object position 
    }
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}