package pl.edu.agh.lab.pkruczkie.maps;

import pl.edu.agh.lab.pkruczkie.animal.IAnimal;
import pl.edu.agh.lab.pkruczkie.elements.Plant;
import pl.edu.agh.lab.pkruczkie.auxiliary.Vector2d;
import pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers.Interfaces.IFloatStatisticsObserver;
import pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers.Interfaces.IGenomeObserver;
import pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers.Interfaces.IIntStatisticsObserver;

import java.util.Set;
import java.util.SortedSet;

public interface IWorldMap {

    void place(IAnimal animal);

    void remove(IAnimal animal);

    SortedSet<IAnimal> animalsAt(Vector2d position);

    //return true if plant is on the given vector
    boolean hasPlant(Vector2d position);

    void addPlant(Plant newPlant);

    void removePlant(Plant newPlant);

    Plant getPlant(Vector2d position);

    boolean isOccupied(Vector2d position);

    boolean areAnimalsDead();


    int getWidth();

    int getHeight();

    Vector2d[] getMapBounds();

    Vector2d[] getJungleBounds();

    Set<Vector2d> getAnimalsPositions();

    Set<Vector2d> getPlantsPositions();

    Set<IAnimal> getEveryAnimal();


    IIntStatisticsObserver getSizeOfPlantsObserver();

    IIntStatisticsObserver getSizeOfAnimalsObserver();

    IGenomeObserver getDominantGenomeObserver();

    IFloatStatisticsObserver getAvgEnergyObserver();
}
