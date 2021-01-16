package pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers;

import pl.edu.agh.lab.pkruczkie.animal.IAnimal;
import pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers.Interfaces.IFloatStatisticsObserver;
import pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers.Interfaces.IObservable;

public class AvgEnergyObserver implements IFloatStatisticsObserver {
    private int counter;
    private int sum;

    public void add(IObservable object){
        if (!(object instanceof IAnimal)) {
            throw new IllegalArgumentException(object.toString() + " is not an animal.");
        }
        counter++;
        sum += ((IAnimal) object).getEnergy();
    }

    public void remove(IObservable object) {
        if (!(object instanceof IAnimal)) {
            throw new IllegalArgumentException(object.toString() + " is not an animal.");
        }
        counter--;
        sum -= ((IAnimal) object).getEnergy();
    }

    public float getResult(){
        return (float)sum/counter;
    }
}
