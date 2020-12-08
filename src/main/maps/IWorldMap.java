package maps;

import elements.AbstractWorldElement;
import elements.animal.Animal;
import elements.animal.IAnimal;
import position.Vector2d;

import java.util.Set;

public interface IWorldMap {

    void place(AbstractWorldElement animal);

    void remove(AbstractWorldElement animal);

    boolean isOccupied(Vector2d position);

    Set<AbstractWorldElement> objectAt(Vector2d position);

}
