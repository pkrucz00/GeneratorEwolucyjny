package pl.edu.agh.lab.pkruczkie.simulationEngine;


import pl.edu.agh.lab.pkruczkie.animal.*;
import pl.edu.agh.lab.pkruczkie.auxiliary.*;
import pl.edu.agh.lab.pkruczkie.elements.Plant;
import pl.edu.agh.lab.pkruczkie.maps.IWorldMap;
import pl.edu.agh.lab.pkruczkie.maps.RandomMapVectors;
import pl.edu.agh.lab.pkruczkie.maps.WorldMap;
import pl.edu.agh.lab.pkruczkie.observers.animalPickerObservers.IAnimalPickerObserver;
import pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers.AvgChildrenPerAnimal;
import pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers.AvgLifespanObserver;
import pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers.Interfaces.IFloatStatisticsObserver;

import java.util.*;

public class Engine {
    private final Parameters params;
    private final IWorldMap map;
    private final RandomMapVectors randomMapVectors;

    public final IFloatStatisticsObserver avgLifespanObserver = new AvgLifespanObserver();
    public final IFloatStatisticsObserver avgChildrenPerAnimal = new AvgChildrenPerAnimal();

    private final ArrayList<IAnimalPickerObserver> offspringObservers = new ArrayList<>();
    private int daysElapsed;

    public Engine(Parameters params) {
        this.params = params;
        if (params.numberOfInitialAnimals > params.width*params.height)
            throw new IllegalArgumentException("Too many animals! You can put at most " + params.width*params.height + "animal on the map");

        this.map = new WorldMap(params.width, params.height, params.jungleRatio);
        this.randomMapVectors = new RandomMapVectors(map);


        for (int i = 0; i < params.numberOfInitialAnimals; i++){
            Vector2d position = this.generateRandomVector(params.width, params.height);
            while (this.map.isOccupied(position)){
                position = this.randomMapVectors.getRandomUnoccupiedPosition(position);
            }
            Genome genome = this.generateRandomGenome();
            Animal animal = new Animal(position, genome, params.startEnergy);
            this.map.place(animal);

            this.avgLifespanObserver.add(new Tuple<>(animal, daysElapsed));
            this.avgChildrenPerAnimal.add(new Tuple<>(animal, null));
        }
    }


    public void simulateDay(){
        Set<Vector2d> positionsCopy = new HashSet<>(this.map.getAnimalsPositions());
        SortedSet<IAnimal> animalsAtPosition;

        for (Vector2d position: positionsCopy){
            animalsAtPosition = this.map.animalsAt(position);
            removeDeadAnimals(animalsAtPosition);
        }

        turnAndMoveAllAnimals(this.map.getEveryAnimal());

        positionsCopy = new HashSet<>(this.map.getAnimalsPositions());
        for (Vector2d position: positionsCopy){
            animalsAtPosition = this.map.animalsAt(position);

            letAnimalsEat(position, animalsAtPosition);
            letAnimalsGenerateOffspring(position, animalsAtPosition);
        }
        plantPlants();

        daysElapsed++;
    }

    private void removeDeadAnimals(SortedSet<IAnimal> animals){
        SortedSet<IAnimal> animalsCopy = new TreeSet<>(animals);
        for (IAnimal animal: animalsCopy){
            if (animal.isDead()) {
                this.map.remove(animal);
                this.avgLifespanObserver.remove(new Tuple<>(animal, daysElapsed));
                this.avgChildrenPerAnimal.remove(animal);
            }
        }
    }


    private void turnAndMoveAllAnimals(Set<IAnimal> animals){
        for (IAnimal animal: animals){
            animal.turn();

            animal.move(this.map.getWidth(), this.map.getHeight());
            animal.changeEnergy(-params.moveEnergy);
        }
    }

    private void letAnimalsEat(Vector2d position, SortedSet<IAnimal> animals){
        int biggestEnergy = animals.first().getEnergy();
        int acc = 0;            //how many animals have the biggest energy
        for (IAnimal animal: animals) {
            if (animal.getEnergy() == biggestEnergy)
                acc++;
            else
                break;
        }

        if (this.map.hasPlant(position)) {
            Plant plant = this.map.getPlant(position);
            int energyFromPlant = plant.getEaten();
            int energyPerAnimal = energyFromPlant / acc;
            int animalsWithOneExtraEnergyPoint = energyFromPlant % acc; //older animals will get more energy
            int iter = 0;
            SortedSet<IAnimal> animalCopy = new TreeSet<>(animals);
            for (IAnimal animal: animalCopy) {
                if (acc < animalsWithOneExtraEnergyPoint)
                    animal.changeEnergy(energyPerAnimal + 1);
                else
                    animal.changeEnergy(energyPerAnimal);
                iter++;
                if (iter == acc)    break;
            }
        }
    }

    private void letAnimalsGenerateOffspring(Vector2d position, SortedSet<IAnimal> animals) {
        if (animals.size() > 1) {
            Iterator iterator = animals.iterator();
            IAnimal mother = (IAnimal) iterator.next();     // two strongest animals on given position
            IAnimal father = (IAnimal) iterator.next();

            if (father.getEnergy() > params.startEnergy/2) {
                Vector2d childsPosition = this.randomMapVectors.getRandomUnoccupiedPosition(position);
                IAnimal child = mother.generateOffspring(father, childsPosition);
                this.map.place(child);

                this.avgLifespanObserver.add(new Tuple<>(child, daysElapsed));
                this.updateAvgChildrenPerAnimal(father, mother, child);
                this.updateOffspringObservers(mother, father, child);
            }
        }
    }

    private void plantPlants() {
        Vector2d junglePlantVector = this.randomMapVectors.getRandomUnoccupiedJungleVector();
        Vector2d steppePlantVector = this.randomMapVectors.getRandomUnoccupiedSteppeVector();
        if (junglePlantVector != null){
            Plant junglePlant = new Plant(junglePlantVector, params.plantEnergy, this.map);
            this.map.addPlant(junglePlant);
        }
        if (steppePlantVector != null) {
            Plant steppePlant = new Plant(steppePlantVector, params.plantEnergy, this.map);
            this.map.addPlant(steppePlant);
        }
    }

    private Vector2d generateRandomVector(int width, int height){
        Random random = new Random();
        return new Vector2d(random.nextInt(width), random.nextInt(height));
    }

    private Genome generateRandomGenome(){    //this function can't be in Genome because making a new genome requires int[32]
        Random random = new Random();
        int[] genes = new int[32];
        for (int i = 0; i < 32; i++){
            genes[i] = random.nextInt(8);
        }
        return new Genome(genes);
    }


    private void updateOffspringObservers(IAnimal mother, IAnimal father, IAnimal child){
        for (IAnimalPickerObserver observer: this.offspringObservers){
            observer.add(mother, father, child);
        }
    }

    private void updateAvgChildrenPerAnimal(IAnimal father, IAnimal mother, IAnimal child){
        this.avgChildrenPerAnimal.add(new Tuple<>(mother, child));
        this.avgChildrenPerAnimal.add(new Tuple<>(father, child));
        this.avgChildrenPerAnimal.add(new Tuple<>(child, null));    //child can produce children immediately, so we count him too
    }
    public void addOffspringObserver(IAnimalPickerObserver observer){
        this.offspringObservers.add(observer);
    }

    public IWorldMap getMap(){
        return this.map;
    }

    public Parameters getParams(){return this.params;}
}
