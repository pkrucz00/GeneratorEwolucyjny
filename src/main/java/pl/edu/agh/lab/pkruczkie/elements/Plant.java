package pl.edu.agh.lab.pkruczkie.elements;

import pl.edu.agh.lab.pkruczkie.maps.IWorldMap;
import pl.edu.agh.lab.pkruczkie.maps.WorldMap;
import pl.edu.agh.lab.pkruczkie.auxiliary.Vector2d;
import pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers.Interfaces.IObservable;

public class Plant implements IAbstractWorldElement, IObservable {
    private final Vector2d position;
    private final int energy;
    private final IWorldMap map;
    public Plant(Vector2d position, int energy, IWorldMap map) {
        this.position = position;
        this.energy = energy;
        this.map = map;
    }

    public Vector2d getPosition(){
        return this.position;
    }

    public int getEaten(){      //function removes plant from the map and returns energy it had
        this.map.removePlant(this);
        return this.energy;
    }

    public String toString() {
        return "*";
    }
}
