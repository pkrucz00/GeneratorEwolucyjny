package pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers;

import pl.edu.agh.lab.pkruczkie.animal.Genome;
import pl.edu.agh.lab.pkruczkie.maps.IWorldMap;
import pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers.Interfaces.IFloatStatisticsObserver;
import pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers.Interfaces.IGenomeObserver;
import pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers.Interfaces.IIntStatisticsObserver;
import pl.edu.agh.lab.pkruczkie.simulationEngine.Engine;

public class CurrentSituationObserversAggregate {
    private final IIntStatisticsObserver sizeOfAnimalsObserver;
    private final IIntStatisticsObserver sizeOfPlantsObserver;
    private final IGenomeObserver dominantGenomeObserver;
    private final IFloatStatisticsObserver avgEnergyObserver;

    private final IFloatStatisticsObserver avgLifespanObserver;
    private final IFloatStatisticsObserver avgChildrenPerAnimal;

    public CurrentSituationObserversAggregate(Engine engine){
        IWorldMap map = engine.getMap();

        sizeOfAnimalsObserver = map.getSizeOfAnimalsObserver();
        sizeOfPlantsObserver = map.getSizeOfPlantsObserver();
        dominantGenomeObserver = map.getDominantGenomeObserver();
        avgEnergyObserver = map.getAvgEnergyObserver();

        avgLifespanObserver = engine.avgLifespanObserver;
        avgChildrenPerAnimal = engine.avgChildrenPerAnimal;
    }

    public String[] getResults(){
        return new String[]{getSizeOfAnimalsString(),
                            getSizeOfPlantsString(),
                            getDominantGenomeString(),
                            getAvgEnergyString(),
                            getAvgLifespanString(),
                            getAvgChildrenPerAnimalString()};
    }

    public float getAvgLifespan(){
        return avgLifespanObserver.getResult();
    }

    public float getAvgChildrenPerAnimal(){
        return avgChildrenPerAnimal.getResult();
    }

    public float getAvgEnergy(){
        return avgEnergyObserver.getResult();
    }

    public Genome getDominantGenome(){
        return dominantGenomeObserver.getResult();
    }

    public int getSizeOfAnimals(){
        return sizeOfAnimalsObserver.getResult();
    }

    public int getSizeOfPlants() { return sizeOfPlantsObserver.getResult();}


    public String getAvgLifespanString() { return Float.toString(getAvgLifespan()); }

    public String getAvgChildrenPerAnimalString() { return Float.toString(getAvgChildrenPerAnimal()); }

    public String getAvgEnergyString() { return Float.toString(getAvgEnergy()); }

    public String getDominantGenomeString() {
        Genome dominant = getDominantGenome();
        if (dominant != null)
            return dominant.toStatisticsString();
        else
            return "NaN";
    }

    public String getSizeOfAnimalsString() { return Integer.toString(getSizeOfAnimals()); }

    public String getSizeOfPlantsString() { return Integer.toString(getSizeOfPlants()); }





}
