package pl.edu.agh.lab.pkruczkie.observers;

import com.google.gson.Gson;
import pl.edu.agh.lab.pkruczkie.animal.Genome;
import pl.edu.agh.lab.pkruczkie.frames.SimulationPanel;
import pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers.CurrentSituationObserversAggregate;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AllAverageObserver {
    private final CurrentSituationObserversAggregate oneDayStats;
    private final Map<Genome, Integer> genomeIntegerHashMap = new HashMap<>();
    private int numberOfDaysElapsed;
    private int dayOfFirstDeath;

    private float avgAnimalsOnMapSum;
    private float avgPlantsOnMapSum;
    private float avgLifespanSum;
    private float avgEnergySum;
    private float avgChildrenSum;


    public AllAverageObserver(CurrentSituationObserversAggregate oneDayStats){
        this.oneDayStats = oneDayStats;
    }

    private class Data{
        private final float avgAnimalsOnMap;
        private final float avgPlantsOnMap;
        private final float avgLifespan;
        private final float avgEnergy;
        private final Genome dominantGenomeMode;
        private final float avgChildren;

        public Data(float avgAnimalsOnMap,
                    float avgPlantsOnMap,
                    float avgLifespan,
                    float avgEnergy,
                    Genome dominantGenomeMode,  //genome that was dominant for longest time
                    float avgChildren){
            this.avgAnimalsOnMap = avgAnimalsOnMap;
            this.avgPlantsOnMap = avgPlantsOnMap;
            this.avgLifespan = avgLifespan;
            this.avgEnergy = avgEnergy;
            this.dominantGenomeMode = dominantGenomeMode;
            this.avgChildren = avgChildren;
        }
    }

    public void saveJson(String fileLocation, SimulationPanel simulationPanel){
        float avgAnimalsOnMap = this.avgAnimalsOnMapSum/numberOfDaysElapsed;
        float avgPlantsOnMap = this.avgPlantsOnMapSum/numberOfDaysElapsed;
        float avgLifespan = this.avgLifespanSum/numberOfDaysElapsed;
        float avgEnergy = this.avgEnergySum/(numberOfDaysElapsed-1);    //isNan only once
        Genome dominantGenomeMode = findModeGenome();
        float avgChildren = this.avgChildrenSum/numberOfDaysElapsed;

        Data data = new Data(avgAnimalsOnMap, avgPlantsOnMap, avgLifespan, avgEnergy, dominantGenomeMode, avgChildren);
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(fileLocation)) {
            gson.toJson(data, writer);
            JOptionPane.showMessageDialog(simulationPanel, "File saved succesfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(){
        avgAnimalsOnMapSum += oneDayStats.getSizeOfAnimals();
        avgPlantsOnMapSum += oneDayStats.getSizeOfPlants();
        avgChildrenSum += oneDayStats.getAvgChildrenPerAnimal();
        updateAvgLifeSpan();
        if (!Float.isNaN(oneDayStats.getAvgEnergy()))   // isNan only at the very end, when all animals died
            avgEnergySum += oneDayStats.getAvgEnergy();
        if (oneDayStats.getDominantGenome() != null)
            updateGenome(oneDayStats.getDominantGenome());

        numberOfDaysElapsed++;
    }


    private void updateAvgLifeSpan(){       //we need to subtract day when first animal died from total of days elapsed
        float value = oneDayStats.getAvgLifespan();
        if (!Float.isNaN(value)) {
            if (dayOfFirstDeath == 0)   //first death
                this.dayOfFirstDeath = this.numberOfDaysElapsed;

            avgLifespanSum += oneDayStats.getAvgLifespan();
        }
    }

    private void updateGenome(Genome genome){
        int newValue;
        newValue = this.genomeIntegerHashMap.getOrDefault(genome, 1);
        this.genomeIntegerHashMap.put(genome, newValue);
    }

    private Genome findModeGenome(){        //sortedset would be faster, but this is simpler
        Set<Genome> keys = this.genomeIntegerHashMap.keySet();
        Genome result = null;
        int numberOfOccurrences = 0;
        for (Genome key: keys){
            int tmp = this.genomeIntegerHashMap.get(key);
            if (tmp > numberOfOccurrences){
                result = key;
                numberOfOccurrences = tmp;
            }
        }
        return result;
    }



}


