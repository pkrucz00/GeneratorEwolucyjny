package pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers;

import pl.edu.agh.lab.pkruczkie.animal.IAnimal;
import pl.edu.agh.lab.pkruczkie.auxiliary.Tuple;
import pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers.Interfaces.IFloatStatisticsObserver;
import pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers.Interfaces.IObservable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AvgChildrenPerAnimal implements IFloatStatisticsObserver {
    private Map<IAnimal, Set<IAnimal>> animalChildrenMap = new HashMap<>();

    @Override
    public void add(IObservable object) {   //when a child is born
        IAnimal parent;
        IAnimal child;
        try{
            parent = ((Tuple<IAnimal, IAnimal>) object).x;
            child = ((Tuple<IAnimal, IAnimal>) object).y;
        } catch (IllegalArgumentException ex){
            throw new IllegalArgumentException(object + " is not a Tuple of IAnimal and IAnimal");
        }
        if (!animalChildrenMap.containsKey(parent)){
            animalChildrenMap.put(parent, new HashSet<>());
        }
        Set<IAnimal> childrenSet = animalChildrenMap.get(parent);
        if (child != null) {
            childrenSet.add(child);
        }
    }

    @Override
    public void remove(IObservable object) {        //when parent dies
        if (object instanceof IAnimal){
            if (animalChildrenMap.containsKey(object))
                animalChildrenMap.remove(object);
        }
        else {
            throw new IllegalArgumentException("IObservable object " + object + " is not an IAnimal");
        }
    }

    private int countAllChildren(){
        int acc = 0;
        Set<IAnimal> animalKeys = animalChildrenMap.keySet();
        for (IAnimal parent: animalKeys)
            acc += animalChildrenMap.get(parent).size();
        return acc;
    }

    @Override
    public float getResult() {
        long allChildren = countAllChildren();
        long allParents = animalChildrenMap.size();
        if (allParents != 0)
            return (float) allChildren/allParents;
        return 0;               //if there are 0 parents, no one has a child
    }
}
