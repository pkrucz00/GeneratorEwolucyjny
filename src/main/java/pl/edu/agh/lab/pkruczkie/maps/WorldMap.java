package pl.edu.agh.lab.pkruczkie.maps;

import pl.edu.agh.lab.pkruczkie.animal.IAnimal;
import pl.edu.agh.lab.pkruczkie.elements.*;
import pl.edu.agh.lab.pkruczkie.auxiliary.Vector2d;
import pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers.*;
import pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers.Interfaces.IFloatStatisticsObserver;
import pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers.Interfaces.IGenomeObserver;
import pl.edu.agh.lab.pkruczkie.observers.currentSituationObservers.Interfaces.IIntStatisticsObserver;

import java.util.*;

import static java.lang.StrictMath.*;


public class WorldMap implements IWorldMap{
    private final Vector2d upperLeftBound;
    private final Vector2d lowerRightBound;
    private final Vector2d jungleUpperLeftBound;
    private final Vector2d jungleLowerRightBound;
    private final int width, height;

    private final Comparator<IAnimal> energyComp = new EnergyComparator();
    private final Map<Vector2d, SortedSet<IAnimal>> animals = new LinkedHashMap<>();
    private final Map<Vector2d, Plant> plants = new HashMap<>();

    public final IIntStatisticsObserver sizeOfAnimalsObserver = new NoAnimalsObserver();
    public final IIntStatisticsObserver sizeOfPlantsObserver = new NoPlantsObserver();
    public final IGenomeObserver dominantGenomeObserver = new DominantGenomeObserver();
    public final IFloatStatisticsObserver avgEnergyObserver = new AvgEnergyObserver();


    public WorldMap(int width, int height, float jungleRatio) {
        if (jungleRatio >1)
            throw new IllegalArgumentException("jungleRatio is above 1");
        this.width = width;
        this.height = height;
        upperLeftBound = new Vector2d(0, height-1);
        lowerRightBound = new Vector2d(width-1, 0);
        Vector2d[] jungleBounds = computeJungleBounds(jungleRatio, width, height);
        jungleUpperLeftBound = jungleBounds[0];
        jungleLowerRightBound = jungleBounds[1];

    }

    private Vector2d[] computeJungleBounds(float jungleRatio, int width, int height) {
        if (jungleRatio == 1)  //if jungleRation is 1, jungle bounds are exactly the same vectors as map bounds
            return new Vector2d[]{new Vector2d(0, height - 1), new Vector2d(width - 1, 0)};

        Vector2d centre = new Vector2d((width / 2), (height / 2));
        int worldMapArea = width * height;
        float jungleArea = (jungleRatio * (float) worldMapArea);
        int jungleHeight = (int) Math.ceil(sqrt(jungleArea)); //since the field is discrete, we can't always make sure, that the ratio is exact.
        //we take the floor of the square root so the bounds don't come out of the map

        int x_min = max(0, centre.x - (jungleHeight / 2));
        int y_min = max(0, centre.y - (jungleHeight / 2));
        int y_max = min(height-1, y_min + jungleHeight - 1);
        int x_max = min(width-1,  x_min + jungleHeight - 1);

        Vector2d upperLeft = new Vector2d(x_min, y_max);
        Vector2d lowerRight = new Vector2d(x_max, y_min);
        return new Vector2d[]{upperLeft, lowerRight};
    }

    // ------ animals methods ------
    @Override
    public void place(IAnimal animal) {
        Vector2d position = animal.getPosition();

        if (!animals.containsKey(position)) {
            animals.put(position,new TreeSet<>(energyComp));
        }
        Set<IAnimal> positionSet = animals.get(position);
        if (positionSet.contains(animal)) {
            throw new IllegalArgumentException("Animal " + animal.toString() + " is already on position " + position.toString());
        }
        positionSet.add(animal);
        animal.setMap(this);
        this.sizeOfAnimalsObserver.add(animal);
        this.avgEnergyObserver.add(animal);
        this.dominantGenomeObserver.add(animal.getGenome());
    }

    public boolean isInSet(IAnimal animal, SortedSet<IAnimal> sortedSet){
        for (IAnimal animal1: sortedSet){
            if (animal.equals(animal1)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void remove(IAnimal animal) {
        Vector2d position = animal.getPosition();
        String errorMessage = "Element " + animal.toString() + " is not on position " + position.toString();

        if (animals.containsKey(position)) {
            SortedSet<IAnimal> positionSet = animals.get(position);
            if (!isInSet(animal, positionSet)) {
                throw new IllegalArgumentException(errorMessage); }


            positionSet.remove(animal);
            this.sizeOfAnimalsObserver.remove(animal);
            this.avgEnergyObserver.remove(animal);
            this.dominantGenomeObserver.remove(animal.getGenome());

            if (positionSet.isEmpty()) {
                animals.remove(position);
            }
        } else {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    @Override
    public SortedSet<IAnimal> animalsAt(Vector2d position) {
        if (animals.containsKey(position))
            return animals.get(position);
        return null;
    }

    public boolean areAnimalsDead(){
        return this.getAnimalsPositions().size()==0;
    }

    public Set<IAnimal> getEveryAnimal(){
        Set<Vector2d> keys = this.getAnimalsPositions();
        Set<IAnimal> result = new LinkedHashSet<>();
        for (Vector2d key: keys){
            SortedSet<IAnimal> animalsAtPosition = this.animals.get(key);
            result.addAll(animalsAtPosition);
        }
        return result;
    }




    // ----- plants methods ------
    @Override
    public boolean hasPlant(Vector2d position) {
        return plants.containsKey(position);
    }

    @Override
    public void addPlant(Plant newPlant) {
        Vector2d position = newPlant.getPosition();
        if (hasPlant(position))
            throw new IllegalArgumentException("PlantceptionError: Plant planted on another plant on position " + position);
        plants.put(position, newPlant);
        sizeOfPlantsObserver.add(newPlant);
    }

    @Override
    public void removePlant(Plant plant){
        Vector2d position = plant.getPosition();
        if (plants.containsKey(position)){
            plants.remove(position);
            sizeOfPlantsObserver.remove(plant);
        }else{
            throw new IllegalArgumentException("The plant is not on position " + position + " and therefore cannot be removed");
        }
    }

    public Plant getPlant(Vector2d position){
        if (this.plants.containsKey(position))
            return this.plants.get(position);
        return null;
    }

    // --- abstract world element ---
    public boolean isOccupied(Vector2d position){
        return (this.hasPlant(position) || this.animalsAt(position) != null);
    }

    //--  getters --

    public Vector2d[] getMapBounds(){
        return new Vector2d[] {this.upperLeftBound, this.lowerRightBound};
    }
    public Vector2d[] getJungleBounds(){
        return new Vector2d[] { this.jungleUpperLeftBound, this.jungleLowerRightBound};
    }


    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }


    public Set<Vector2d> getAnimalsPositions(){
        return this.animals.keySet();
    }
    public Set<Vector2d> getPlantsPositions() {return this.plants.keySet();}


    //  --getters with observers--

    public IIntStatisticsObserver getSizeOfPlantsObserver() {
        return sizeOfPlantsObserver;
    }

    public IIntStatisticsObserver getSizeOfAnimalsObserver() {
        return sizeOfAnimalsObserver;
    }

    public IGenomeObserver getDominantGenomeObserver() {
        return dominantGenomeObserver;
    }

    public IFloatStatisticsObserver getAvgEnergyObserver() {
        return avgEnergyObserver;
    }
}
