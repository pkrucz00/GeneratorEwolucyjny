package pl.edu.agh.lab.pkruczkie.animal;

import pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers.Interfaces.IObservable;

public interface IGenotype extends IObservable {
    boolean isGenomeValid();

    void insertOtherGenome(Genome other);

    int getRandomGene();

    int[] getListOfGenes();
}
