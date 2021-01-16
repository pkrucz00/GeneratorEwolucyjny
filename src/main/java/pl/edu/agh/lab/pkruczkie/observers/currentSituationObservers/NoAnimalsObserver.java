package pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers;

import pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers.Interfaces.IIntStatisticsObserver;
import pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers.Interfaces.IObservable;

public class NoAnimalsObserver implements IIntStatisticsObserver {
    private int numberOfAnimals;

    @Override
    public void add(IObservable object) {
        this.numberOfAnimals++;
    }

    @Override
    public void remove(IObservable object) {
        this.numberOfAnimals--;
    }

    @Override
    public int getResult() {
        return this.numberOfAnimals;
    }
}
