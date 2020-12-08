package maps;

import elements.AbstractWorldElement;
import elements.animal.IAnimal;
import position.Vector2d;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.lang.StrictMath.sqrt;

public class WorldMap implements IWorldMap{
    Vector2d upperLeftBound;
    Vector2d lowerRightBound;
    Vector2d jungleLowerLeftBound;
    Vector2d jungleUpperRightBound;
    Map<Vector2d, Set<AbstractWorldElement>> elements = new HashMap<>();


    public WorldMap(int width, int height, int jungleRatio, IAnimal[] initialAnimals){
        //TODO w parserze - dodać ograniczenie width > jungleWidth && height > jungleHeight
        upperLeftBound = new Vector2d(0,height-1);
        lowerRightBound = new Vector2d(width-1, 0);
        Vector2d[] jungleBounds = computeJungleBounds(jungleRatio, width, height);
        jungleLowerLeftBound = jungleBounds[0];
        jungleUpperRightBound = jungleBounds[1];

        for (IAnimal animal: initialAnimals){
//            place(animal);
        }
    }

    private Vector2d[] computeJungleBounds(int jungleRatio, int width, int height){
        Vector2d centre = new Vector2d((int) (width/2), (int)(height/2));
        int worldMapArea = width*height;
        float jungleArea = jungleRatio*(float)worldMapArea;
        int jungleHeight = (int)(sqrt(jungleArea)); //since the field is discrete, we can't always make sure, that the ratio is exact.
                                                    //we take the floor of the square root so the bounds don't come out of the map

        Vector2d upperLeft = new Vector2d(centre.x - (jungleHeight/2), centre.y + (jungleHeight/2));
        Vector2d lowerRight = new Vector2d(centre.x - jungleHeight, upperLeft.y - jungleHeight);
        return new Vector2d[]{upperLeft, lowerRight};
    }

    @Override
    public void place(AbstractWorldElement element) {
        Vector2d position = element.getPosition();
        if (!elements.containsKey(position)){
            elements.put(position, new HashSet<>());
        }
        Set<AbstractWorldElement> positionSet = elements.get(position);
        if (positionSet.contains(element)){
            throw new IllegalArgumentException("Element " + element.toString() + " is already on position " + position.toString());
        }
        positionSet.add(element);
    }

    @Override
    public void remove(AbstractWorldElement element) {
        Vector2d position = element.getPosition();
        String errorMessage = "Element " + element.toString() + " is not on position " + position.toString();

        if (!elements.containsKey(position)){
            throw new IllegalArgumentException(errorMessage);
        }
        Set<AbstractWorldElement> positionSet = elements.get(position);
        if (!positionSet.contains(element)){
            throw new IllegalArgumentException(errorMessage);
        }

        positionSet.remove(element);
        if (positionSet.isEmpty()){
            elements.remove(position);
        }
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return false;
    }

    @Override
    public Set<AbstractWorldElement> objectAt(Vector2d position) {
        return null;
    }

//    @Override
//    public boolean hasPlant(Vector2d position) {
//
//    }


}
