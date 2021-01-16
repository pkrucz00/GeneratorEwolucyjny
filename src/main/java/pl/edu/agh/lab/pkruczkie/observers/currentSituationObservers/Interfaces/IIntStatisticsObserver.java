package pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers.Interfaces;

public interface IIntStatisticsObserver {

    void add(IObservable object);   //objects, that are sometimes necessary to update data (f.e. animal for his energy)

    void remove(IObservable object); //explanation as above

    int getResult();
}
