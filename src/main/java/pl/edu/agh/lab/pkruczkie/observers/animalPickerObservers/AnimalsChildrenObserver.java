package pl.edu.agh.lab.pkruczkie.observers.animalPickerObservers;

import pl.edu.agh.lab.pkruczkie.animal.IAnimal;
import pl.edu.agh.lab.pkruczkie.simulationEngine.Engine;

public class AnimalsChildrenObserver implements IAnimalPickerObserver{
    private final IAnimal animal;
    private int count = 0;


    public AnimalsChildrenObserver(IAnimal animal, Engine engine){
        this.animal = animal;
        engine.addOffspringObserver(this);
    }

    @Override
    public void add(IAnimal father, IAnimal mother, IAnimal child) {
        if (father.equals(animal) || mother.equals(animal))
            count++;
    }

    @Override
    public int getResult() {
        return count;
    }

}
