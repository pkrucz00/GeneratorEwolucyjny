package elements.animal;

import position.Vector2d;

public interface IAnimal {
    void turn(int gene);

    void move();

    IAnimal generateOffspring(IAnimal parent);

    boolean isDead();

    void addObserver();

    void removeObserver();

    Vector2d getPosition();

}
