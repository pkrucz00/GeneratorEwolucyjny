package pl.edu.agh.lab.pkruczkie.observers.animalPickerObservers;

import pl.edu.agh.lab.pkruczkie.animal.IAnimal;

public interface IAnimalPickerObserver {
    void add(IAnimal father, IAnimal mother, IAnimal child);

    int getResult();
}
