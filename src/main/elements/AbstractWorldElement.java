package elements;

import position.Vector2d;

public abstract class AbstractWorldElement {
    private Vector2d position;

    public Vector2d getPosition(){
        return this.position;
    }

}
