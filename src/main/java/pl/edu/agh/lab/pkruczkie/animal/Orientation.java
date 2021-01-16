package pl.edu.agh.lab.pkruczkie.animal;

import pl.edu.agh.lab.pkruczkie.auxiliary.Vector2d;

public enum Orientation {
    N,
    NE,
    E,
    SE,
    S,
    SW,
    W,
    NW;

    private static Orientation[] list = Orientation.values();

    public String toString() {
        return switch (this) {
            case N -> "North";
            case NE -> "North East";
            case E -> "East";
            case SE -> "South East";
            case S -> "South";
            case SW -> "South West";
            case W -> "West";
            case NW -> "North West";
        };
    }

    public Orientation next(){  //next clockwise
        return switch (this) {
            case N -> NE;
            case NE -> E;
            case E -> SE;
            case SE -> S;
            case S -> SW;
            case SW -> W;
            case W -> NW;
            case NW -> N;
        };
    }

    public Vector2d toUnitVector(){
        return switch (this) {
            case N -> new Vector2d(0,1);
            case NE -> new Vector2d(1, 1);
            case E -> new Vector2d(1,0);
            case SE -> new Vector2d(1,-1);
            case S -> new Vector2d(0,-1);
            case SW -> new Vector2d(-1, -1);
            case W -> new Vector2d(-1,0);
            case NW -> new Vector2d(-1, 1);
        };
    }


}
