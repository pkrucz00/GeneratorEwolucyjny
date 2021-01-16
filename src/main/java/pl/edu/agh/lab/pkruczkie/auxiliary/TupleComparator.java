package pl.edu.agh.lab.pkruczkie.auxiliary;

import pl.edu.agh.lab.pkruczkie.animal.Genome;

import java.util.Arrays;
import java.util.Comparator;

public class TupleComparator implements Comparator<Tuple<Genome, Integer>> {


    @Override
    public int compare(Tuple<Genome, Integer> o1, Tuple<Genome, Integer> o2) {
        if (o1.equals(o2))
            return 0;
        Genome genome1 = o1.x;
        Genome genome2 = o2.x;
        int occur1 = o1.y;
        int occur2 = o2.y;

        if (occur1 < occur2){
            return -1;
        } else if (occur1 > occur2){
            return 1;
        } else{
            int[] genes1 = genome1.getListOfGenes();
            int[] genes2 = genome2.getListOfGenes();
            return Arrays.compare(genes1, genes2);
        }
    }
}
