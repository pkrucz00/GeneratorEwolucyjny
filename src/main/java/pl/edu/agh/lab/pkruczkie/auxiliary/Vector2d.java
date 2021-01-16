package pl.edu.agh.lab.pkruczkie.auxiliary;

import java.util.Objects;

public class Vector2d {
    public final int x;
    public final int y;

    public Vector2d(int x, int y){
        this.x = x;
        this.y = y;
    }

    public String toString(){
        return "(" + (this.x) + "," + this.y + ")";
    }


    public Vector2d add(Vector2d other){
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    public Vector2d addModulo(Vector2d other, int width, int height){
        return new Vector2d(Math.floorMod(this.x + other.x, width),
                Math.floorMod(this.y + other.y, height));
    }
    

    public boolean equals(Object other){
        if (this == other)
            return true;
        if (!(other instanceof Vector2d)){
            return false;
        }
        Vector2d that = (Vector2d) other;
        return this.x == that.x && this.y == that.y;
    }

    public boolean inBetween(Vector2d upperLeft, Vector2d lowerRight){
        return (upperLeft.x <= this.x && this.x <= lowerRight.x && lowerRight.y <= this.y && this.y <= upperLeft.y);
    }

    public Vector2d opposite(){
        return new Vector2d(-this.x, -this.y);
    }

    public int area(Vector2d other){
        int width = Math.abs(this.x - other.x) + 1;
        int height = Math.abs(this.y - other.y) + 1;
        return width*height;
    }

    @Override
    public int hashCode() {
            return Objects.hash(this.x, this.y);
        }
}
