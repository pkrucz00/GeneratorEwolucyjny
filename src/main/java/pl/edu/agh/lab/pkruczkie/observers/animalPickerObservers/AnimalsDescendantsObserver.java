package pl.edu.agh.lab.pkruczkie.observers.animalPickerObservers;

import pl.edu.agh.lab.pkruczkie.animal.IAnimal;
import pl.edu.agh.lab.pkruczkie.simulationEngine.Engine;

import java.util.HashSet;
import java.util.Set;

public class AnimalsDescendantsObserver implements IAnimalPickerObserver{
    private final IAnimal headOfTheFamily;
    private final Set<IAnimal> childrenSet = new HashSet<>();

    public AnimalsDescendantsObserver(IAnimal animal, Engine engine){
        this.headOfTheFamily = animal;
        engine.addOffspringObserver(this);
    }

    @Override
    public void add(IAnimal father, IAnimal mother, IAnimal child) {
        if (headOfTheFamily.equals(father) || isADescendant(father)){
            childrenSet.add(child);
        }
        if (headOfTheFamily.equals(mother) || isADescendant(mother)){
            childrenSet.add(child);
        }
    }

    private boolean isADescendant(IAnimal animal){return childrenSet.contains(animal);}

    @Override
    public int getResult() {
        return childrenSet.size();
    }
}
