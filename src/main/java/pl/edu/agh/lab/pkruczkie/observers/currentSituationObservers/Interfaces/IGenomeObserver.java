package pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers.Interfaces;

import pl.edu.agh.lab.pkruczkie.animal.Genome;

public interface IGenomeObserver {
    void add(Genome genome);

    void remove(Genome genome);

    Genome getResult();
}
