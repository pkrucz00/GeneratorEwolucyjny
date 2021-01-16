import org.junit.jupiter.api.Test;
import pl.edu.agh.lab.pkruczkie.animal.Animal;
import pl.edu.agh.lab.pkruczkie.animal.Genome;
import pl.edu.agh.lab.pkruczkie.animal.IAnimal;
import pl.edu.agh.lab.pkruczkie.maps.RandomMapVectors;
import pl.edu.agh.lab.pkruczkie.maps.WorldMap;
import pl.edu.agh.lab.pkruczkie.auxiliary.Vector2d;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class WorldMapTest {
    @Test
    public void jungleTest(){
        WorldMap testMap = new WorldMap(10, 10, 0.64f);
        Vector2d[] jungleBounds = testMap.getJungleBounds();
        assertEquals(new Vector2d(1,8), jungleBounds[0]);
        assertEquals(new Vector2d(8,1), jungleBounds[1]);
    }

    @Test
    public void placeTest(){
        int[] genes = {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 4, 4, 4, 4, 4, 4, 5, 5, 6, 6, 7, 7, 7, 7};
        Genome genome = new Genome(genes);
        Vector2d positionOfTheParrot = new Vector2d(5, 5);
        Animal parrot = new Animal(positionOfTheParrot, genome, 10);
        WorldMap testMap = new WorldMap(10, 10, 1);
        testMap.place(parrot);

        Set<IAnimal> setOfAnimalsOnPosition = testMap.animalsAt(positionOfTheParrot);
        assertTrue(setOfAnimalsOnPosition.contains(parrot));

        int[] otherGenes = {2, 4, 3, 5, 1, 2, 3, 6, 5, 7, 5, 4, 3 ,2 ,0 ,1 ,4 ,5 ,4 ,2, 0, 1, 2, 4, 5, 6, 6, 7, 3, 2, 5, 1};
        Genome genome1 = new Genome(otherGenes);
        Vector2d positionOfAra = new Vector2d(5,5);
        Animal ara = new Animal(positionOfAra, genome1, 10);

        testMap.place(ara);

        setOfAnimalsOnPosition = testMap.animalsAt(positionOfTheParrot);
        assertTrue(setOfAnimalsOnPosition.contains(parrot));
        assertTrue(setOfAnimalsOnPosition.contains(ara));
    }

    @Test
    public void randomUnoccupiedPositionNearbyTest(){
        WorldMap testMap = new WorldMap(10, 10, 1);
        int[] genes = {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 4, 4, 4, 4, 4, 4, 5, 5, 6, 6, 7, 7, 7, 7};
        Genome genome = new Genome(genes);

        //checking normal implementation
        Vector2d centre = new Vector2d(5,5);
        Vector2d unoccupied = new Vector2d(6, 6);
        for (int i = -1; i <= 1; i++){              //filling adjacent positions with animals unless it's centre or "unoccupied" vector
            for (int j= -1; j <= 1; j++){
                Vector2d animalPosition = new Vector2d(centre.x+i, centre.y+j);
                if (!animalPosition.equals(centre) && !animalPosition.equals(unoccupied)){
                    testMap.place(new Animal(animalPosition, genome, 10));
                }
            }
        }
        RandomMapVectors randVecGen = new RandomMapVectors(testMap);
        Vector2d randVec = randVecGen.getRandomUnoccupiedPosition(centre);
        assertEquals(unoccupied, randVec);

        //checking "teleporting" on map boundaries
        centre = new Vector2d(9, 8);
        unoccupied = new Vector2d(0, 8);
        for (int i = -1; i <= 1; i++){
            for (int j= -1; j <= 1; j++){
                Vector2d animalPosition = new Vector2d(StrictMath.floorMod(centre.x+i, 10),
                        StrictMath.floorMod(centre.y+j, 10));
                if (!animalPosition.equals(centre) && !animalPosition.equals(unoccupied)){
                    testMap.place(new Animal(animalPosition, genome, 10));
                }
            }
        }
        randVec = randVecGen.getRandomUnoccupiedPosition(centre);
        assertEquals(unoccupied, randVec);
    }

    @Test
    public void sortedSetAnimalsTest(){
        WorldMap testMap = new WorldMap(10, 10, 1);
        int[] genes = {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 2, 2, 2, 2, 2, 3, 3, 4, 4, 4, 4, 4, 4, 5, 5, 6, 6, 7, 7, 7, 7};
        Genome genome = new Genome(genes);
        Vector2d position = new Vector2d(5,5);
        ArrayList<IAnimal> testAnimals = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            testAnimals.add(new Animal(position, genome, i));
        }
        Collections.shuffle(testAnimals);
        for (IAnimal animal: testAnimals){
            testMap.place(animal);
        }
        SortedSet<IAnimal> animalSet = testMap.animalsAt(position);

        assertEquals(9, animalSet.first().getEnergy());
        assertEquals(0, animalSet.last().getEnergy());

        Iterator iterator = animalSet.iterator();
        IAnimal strongest = (IAnimal) iterator.next();
        IAnimal secondStrongest = (IAnimal) iterator.next();
        assertEquals(8, secondStrongest.getEnergy());

        testMap.place(new Animal(position, genome, 9));
        animalSet = testMap.animalsAt(position);
        assertEquals(11, animalSet.size());
    }

}
