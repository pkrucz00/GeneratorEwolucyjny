import org.junit.jupiter.api.Test;
import pl.edu.agh.lab.pkruczkie.animal.Animal;
import pl.edu.agh.lab.pkruczkie.animal.Genome;
import pl.edu.agh.lab.pkruczkie.animal.IAnimal;
import pl.edu.agh.lab.pkruczkie.maps.WorldMap;
import pl.edu.agh.lab.pkruczkie.auxiliary.Vector2d;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class AnimalTest {
    @Test
    void initializationTest(){
        int[] genes = {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 4, 4, 4, 4, 4, 4, 5, 5, 6, 6, 7, 7, 7, 7};
        Genome genome = new Genome(genes);
        Animal testAnimal = new Animal(new Vector2d(1, 1), genome, 10);
        assertEquals(genome, testAnimal.getGenome());
        assertEquals(new Vector2d(1,1), testAnimal.getPosition());
    }

    @Test
    void generatingOffspring(){
        int[] motherGenes = {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 4, 4, 4, 4, 4, 4, 5, 5, 6, 6, 7, 7, 7, 7};
        int[] dadGenes = {2, 4, 3, 5, 1, 2, 3, 6, 5, 7, 5, 4, 3 ,2 ,0 ,1 ,4 ,5 ,4 ,2, 0, 1, 2, 4, 5, 6, 6, 7, 3, 2, 5, 1};
        Genome motherGenome = new Genome(motherGenes);
        Genome dadGenome = new Genome(dadGenes);
        IAnimal mother = new Animal(new Vector2d(0,0), motherGenome, 12);
        IAnimal dad = new Animal(new Vector2d(0,0), dadGenome, 12);
        IAnimal child = mother.generateOffspring(dad, new Vector2d(1,0));

        assertEquals(new Vector2d(1,0), child.getPosition());
        assertNotEquals(mother.getGenome(), child.getGenome());
        assertNotEquals(dad.getGenome(), child.getGenome());
        assertEquals(9, dad.getEnergy());
        assertEquals(9, mother.getEnergy());
        assertEquals(6, child.getEnergy());
    }

    @Test
    void moveTest(){
        int[] genes = {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 4, 4, 4, 4, 4, 4, 5, 5, 6, 6, 7, 7, 7, 7};
        int[] otherGenes = {2, 4, 3, 5, 1, 2, 3, 6, 5, 7, 5, 4, 3 ,2 ,0 ,1 ,4 ,5 ,4 ,2, 0, 1, 2, 4, 5, 6, 6, 7, 3, 2, 5, 1};
        Genome genome1 = new Genome(otherGenes);
        Genome genome = new Genome(genes);

        Animal parrot = new Animal(new Vector2d(5,5), genome, 10);
        Animal ara = new Animal(new Vector2d(5, 5),genome1, 10);
        WorldMap testMap = new WorldMap(10, 10, 1);
        testMap.place(parrot);
        testMap.place(ara);
        parrot.move(10, 10);
        ara.move(10, 10);

        Set<IAnimal> setOfAnimalsWithParrot = testMap.animalsAt(parrot.getPosition());
        Set<IAnimal> setOfAnimalsWithAra = testMap.animalsAt(ara.getPosition());
        Set<IAnimal> emptyField = testMap.animalsAt(new Vector2d(5,5));

        assertTrue(setOfAnimalsWithParrot.contains(parrot));
        assertTrue(setOfAnimalsWithAra.contains(ara));
        assertNull(emptyField);
    }
}
