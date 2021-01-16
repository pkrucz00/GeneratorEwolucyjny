package pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers;

import pl.edu.agh.lab.pkruczkie.animal.Genome;
import pl.edu.agh.lab.pkruczkie.auxiliary.Tuple;
import pl.edu.agh.lab.pkruczkie.auxiliary.TupleComparator;
import pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers.Interfaces.IGenomeObserver;

import java.util.*;

public class DominantGenomeObserver implements IGenomeObserver {
    private final Map<Genome, Integer> genomeOccurrencesMap = new HashMap<>();        //for counting occurrences
    private final TupleComparator tupleComparator = new TupleComparator();
    private final SortedSet<Tuple<Genome, Integer>> genomeOccurrencesSet = new TreeSet<>(tupleComparator);   //for sorting by number of occurrences


    public void add(Genome genome){
        if (!genomeOccurrencesMap.containsKey(genome)){
            genomeOccurrencesMap.put(genome, 1);
        }
        else{
            int newValue = genomeOccurrencesMap.get(genome) + 1;
            genomeOccurrencesMap.put(genome, newValue);
            genomeOccurrencesSet.remove(new Tuple<>(genome, newValue));     //removing from red-black tree
        }
        int numberOfOccurrences = genomeOccurrencesMap.get(genome);
        genomeOccurrencesSet.add(new Tuple<>(genome, numberOfOccurrences));
    }

    @Override
    public void remove(Genome genome) {
        if (!genomeOccurrencesMap.containsKey(genome)){
            throw new IllegalArgumentException(genome.toString() + " not in the genome set");
        }
        else{
            int oldValue = genomeOccurrencesMap.get(genome);
            genomeOccurrencesSet.remove(new Tuple<>(genome, oldValue));

            int newValue = genomeOccurrencesMap.get(genome) - 1;
            if (newValue > 0) {
                genomeOccurrencesMap.put(genome, newValue);
                genomeOccurrencesSet.add(new Tuple<>(genome, newValue));
            }
            else
                genomeOccurrencesMap.remove(genome);
        }
    }

    @Override
    public Genome getResult() {
        if (genomeOccurrencesSet.isEmpty())
            return null;
        return genomeOccurrencesSet.first().x;
    }
}
