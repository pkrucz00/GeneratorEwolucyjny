package pl.edu.agh.lab.pkruczkie.maps;

import pl.edu.agh.lab.pkruczkie.animal.Orientation;
import pl.edu.agh.lab.pkruczkie.auxiliary.Vector2d;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class RandomMapVectors {
    private final IWorldMap map;
    private final int width;
    private final int height;

    public RandomMapVectors(IWorldMap map){
        this.map = map;
        this.width = map.getWidth();
        this.height = map.getHeight();
    }

    public Vector2d getRandomUnoccupiedPosition(Vector2d position){
        Orientation[] orientations = Orientation.values();
        ArrayList<Vector2d> possibleVectors = new ArrayList<>();

        for (Orientation orientation: orientations){
            Vector2d possibleVector = position.addModulo(orientation.toUnitVector(), width, height);
            if (!this.map.isOccupied(possibleVector))
                possibleVectors.add(possibleVector);
        }

        Random random = new Random();
        int n = possibleVectors.size();
        if (n == 0){
            Orientation randomOrientation = orientations[random.nextInt(8)];
            return position.addModulo(randomOrientation.toUnitVector(), width, height);
        }
        else
            return possibleVectors.get(random.nextInt(n));
    }

    public int getNumberOfOccupiedPositionsBetweenVectors(Vector2d upperLeft, Vector2d lowerRight){
        Set<Vector2d> animalsPositions = this.map.getAnimalsPositions();
        Set<Vector2d> plantsPositions = this.map.getPlantsPositions();

        int acc = 0;
        for (Vector2d position: animalsPositions){
            if (position.inBetween(upperLeft, lowerRight)){
                acc++;
            }
        }
        for (Vector2d position: plantsPositions){
            if (position.inBetween(upperLeft, lowerRight)){
                acc++;
            }
        }
        return acc;
    }

    public Vector2d getRandomUnoccupiedJungleVector(){
        Vector2d[] jungleBounds = this.map.getJungleBounds();
        Vector2d jungleUpperLeftBound = jungleBounds[0];
        Vector2d jungleLowerRightBound = jungleBounds[1];

        int area = jungleBounds[0].area(jungleBounds[1]);
        if (getNumberOfOccupiedPositionsBetweenVectors(jungleUpperLeftBound, jungleLowerRightBound) < area) {
            Random random = new Random();
            Vector2d result;
            do {
                int x = random.nextInt(jungleLowerRightBound.x - jungleUpperLeftBound.x + 1) + jungleUpperLeftBound.x;
                int y = random.nextInt(jungleUpperLeftBound.y - jungleLowerRightBound.y + 1) + jungleLowerRightBound.y;
                result = new Vector2d(x, y);
            } while (this.map.isOccupied(result));
            return result;
        } else {
            return null;
        }

    }

    public Vector2d getRandomUnoccupiedSteppeVector(){
        Vector2d[] jungleBounds = this.map.getJungleBounds();
        Vector2d jungleUpperLeftBound = jungleBounds[0];
        Vector2d jungleLowerRightBound = jungleBounds[1];

        Vector2d[] mapBounds = this.map.getMapBounds();
        Vector2d upperLeftBound = mapBounds[0];
        Vector2d lowerRightBound = mapBounds[1];

        int area = width*height - jungleLowerRightBound.area(jungleUpperLeftBound);
        int NoAllOccupiedPositions = getNumberOfOccupiedPositionsBetweenVectors(upperLeftBound, lowerRightBound);
        int JungleOccupiedPositions = getNumberOfOccupiedPositionsBetweenVectors(jungleUpperLeftBound, jungleLowerRightBound);
        if (NoAllOccupiedPositions - JungleOccupiedPositions < area) {
            Random random = new Random();
            Vector2d result;
            do {
                int x = random.nextInt(width);
                int y = random.nextInt(height);
                result = new Vector2d(x, y);
            } while (result.inBetween(jungleUpperLeftBound, jungleLowerRightBound) || this.map.isOccupied(result));
            return result;
        }
        return null;
    }
}
