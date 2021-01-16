package pl.edu.agh.lab.pkruczkie.animal;

import pl.edu.agh.lab.pkruczkie.elements.IAbstractWorldElement;
import pl.edu.agh.lab.pkruczkie.maps.IWorldMap;
import pl.edu.agh.lab.pkruczkie.auxiliary.Vector2d;
import pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers.Interfaces.IObservable;

public interface IAnimal extends IAbstractWorldElement, IObservable {

    void turn();

    void move(int width, int height);

    void changeEnergy(int energyChange);

    IAnimal generateOffspring(IAnimal dad, Vector2d childPosition);

    boolean isDead();

    int getEnergy();

    Genome getGenome();

    int getId();

    void setMap(IWorldMap map);

    boolean equals(Object o);
}
