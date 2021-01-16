package pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers;

import pl.edu.agh.lab.pkruczkie.animal.IAnimal;
import pl.edu.agh.lab.pkruczkie.auxiliary.Tuple;
import pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers.Interfaces.IFloatStatisticsObserver;
import pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers.Interfaces.IObservable;


import java.util.HashMap;
import java.util.Map;


public class AvgLifespanObserver implements IFloatStatisticsObserver {
    private long sum;
    private int count;
    Map<IAnimal, Integer> aliveAnimals = new HashMap<>();

    @Override
    public void add(IObservable object) {
        IAnimal animal;
        Integer birthAge;
        try{
            animal = ((Tuple<IAnimal, Integer>) object).x;
            birthAge = ((Tuple<IAnimal, Integer>) object).y;
        } catch (IllegalArgumentException ex){
            throw new IllegalArgumentException(object + " is not a Tuple of Animal and Integer");
        }
        aliveAnimals.put(animal, birthAge);
    }

    @Override
    public void remove(IObservable object) {
        IAnimal animal;
        Integer deathAge;
        try {
            animal = ((Tuple<IAnimal, Integer>) object).x;
            deathAge = ((Tuple<IAnimal, Integer>) object).y;
        }catch (IllegalArgumentException exception){
            throw new IllegalArgumentException(object + " is not a Tuple of Animal and Integer");
        }
        Integer birthAge = aliveAnimals.get(animal);

        this.sum += deathAge - birthAge;
        this.count++;
    }

    @Override
    public float getResult() {
        return (float) sum/count;
    }
}
