package pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers;

import pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers.Interfaces.IIntStatisticsObserver;
import pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers.Interfaces.IObservable;

public class NoPlantsObserver implements IIntStatisticsObserver {
    private int numberOfPlants;

    @Override
    public void add(IObservable object) {
        this.numberOfPlants++;
    }

    @Override
    public void remove(IObservable object) {
        this.numberOfPlants--;
    }

    @Override
    public int getResult() {
        return this.numberOfPlants;
    }
}
