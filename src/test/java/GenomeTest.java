import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import pl.edu.agh.lab.pkruczkie.animal.Genome;
import pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers.Interfaces.IGenomeObserver;
import pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers.DominantGenomeObserver;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


public class GenomeTest {
    @Test
    public void genomeValidationTest(){
        int[] inputGenes = {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 4, 4, 4, 4, 4, 4, 5, 5, 6, 6, 7, 7, 7, 7};
        Genome testGenome = new Genome(inputGenes);
        assertTrue(testGenome.isGenomeValid());
        assertTrue(Arrays.equals(inputGenes, testGenome.getListOfGenes()));
    }

    @Test
    public void repairTest(){
        int[] inputGenes = {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 4, 4, 4, 4, 4, 4, 5, 5, 7, 7, 7, 7, 7, 7};
        Genome testGenome = new Genome(inputGenes);
        assertTrue(testGenome.isGenomeValid());
        assertFalse(Arrays.equals(inputGenes, testGenome.getListOfGenes()));
    }

    @Test
    @Timeout(1000)
    public void mergingGenomesTest(){
        int[] inputGenes1 = {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 4, 4, 4, 4, 4, 4, 5, 5, 6, 6, 7, 7, 7, 7};
        int[] inputGenes2 = {2, 4, 3, 5, 1, 2, 3, 6, 5, 7, 5, 4, 3 ,2 ,0 ,1 ,4 ,5 ,4 ,2, 0, 1, 2, 4, 5, 6, 6, 7, 3, 2, 5, 1};
        Genome daddyGenome = new Genome(inputGenes1);
        Genome mommyGenome = new Genome(inputGenes2);
        Genome babyGenome = new Genome(daddyGenome.getListOfGenes());

        assertTrue(babyGenome.isGenomeValid());
        assertTrue(Arrays.equals(daddyGenome.getListOfGenes(), babyGenome.getListOfGenes()));
        assertNotEquals(babyGenome.getListOfGenes(), daddyGenome.getListOfGenes());   //two distinct arrays with the same values

        babyGenome.insertOtherGenome(mommyGenome);

        assertTrue(babyGenome.isGenomeValid());
        assertFalse(Arrays.equals(daddyGenome.getListOfGenes(), babyGenome.getListOfGenes()));
        assertFalse(Arrays.equals(mommyGenome.getListOfGenes(), babyGenome.getListOfGenes()));
    }

    @Test
    public void genomeObserverTest(){
        int[] inputGenes1 = {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 4, 4, 4, 4, 4, 4, 5, 5, 6, 6, 7, 7, 7, 7};
        int[] inputGenes2 = {2, 4, 3, 5, 1, 2, 3, 6, 5, 7, 5, 4, 3 ,2 ,0 ,1 ,4 ,5 ,4 ,2, 0, 1, 2, 4, 5, 6, 6, 7, 3, 2, 5, 1};
        int[] inputGenes3 = {1, 2, 3, 1, 4, 3, 5, 4, 6, 5, 7, 3, 6, 0, 7, 0, 6, 0, 5, 4, 6, 3, 4, 2, 1, 0, 4, 5, 3, 2, 3, 4};
        Genome[] threeSimilarGenomes = new Genome[3];
        for (int i = 0; i < 3; i++){
            threeSimilarGenomes[i] = new Genome(inputGenes1);
        }
        Genome[] twoSimilarGenomes = new Genome[2];
        for (int i = 0; i < 2; i++){
            twoSimilarGenomes[i] = new Genome(inputGenes2);
        }
        Genome oneSimilarGenome = new Genome(inputGenes3);

        IGenomeObserver observer = new DominantGenomeObserver();
        observer.add(threeSimilarGenomes[1]);
        observer.add(twoSimilarGenomes[0]);
        observer.add(oneSimilarGenome);
        observer.add(threeSimilarGenomes[2]);
        observer.add(twoSimilarGenomes[1]);
        observer.add(threeSimilarGenomes[0]);

        assertEquals(threeSimilarGenomes[0], observer.getResult());
    }
}