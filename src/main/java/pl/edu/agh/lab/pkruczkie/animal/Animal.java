package pl.edu.agh.lab.pkruczkie.animal;

import pl.edu.agh.lab.pkruczkie.maps.IWorldMap;

import pl.edu.agh.lab.pkruczkie.auxiliary.Vector2d;

import java.util.Objects;
import java.util.Random;

public class Animal implements IAnimal{
    private static int animalsMade;

    private final int id;
    private Vector2d position;
    private final Genome genes;
    private Orientation orientation;
    private int energy;
    private IWorldMap map;

    public Animal(Vector2d position, Genome genes, int initialEnergy){
        this.id = animalsMade;
        animalsMade += 1;

        this.position = position;
        this.genes = genes;

        Random rand = new Random();
        Orientation[] orientations = Orientation.values();
        this.orientation = orientations[rand.nextInt(orientations.length)];
        this.energy = initialEnergy;
    }

    @Override
    public void turn() {
        int randGene = genes.getRandomGene();
        for (int i = 0; i < randGene; i++){
            this.orientation = this.orientation.next();
        }
    }

    @Override
    public void move(int width, int height) {
        if (this.isDead())
            throw new IllegalCallerException("MovingDeadAnimalException: Animal on position " + this.position + "'s passed on! This animal is no more! He has ceased to be! 'E's expired and gone to meet 'is maker! 'E's a stiff! Bereft of life, 'e rests in peace! THIS IS AN EX-ANIMAL!");

        this.map.remove(this);

        Vector2d oldPosition = this.position;
        Vector2d change = this.orientation.toUnitVector();
        this.position = oldPosition.addModulo(change, width, height);

        this.map.place(this);

    }


    @Override
    public void changeEnergy(int energyChange) {
        this.map.remove(this);
        this.energy += energyChange;
        this.map.place(this);
    }

    @Override
    public IAnimal generateOffspring(IAnimal dad, Vector2d childPosition) {
        if (!this.position.equals(dad.getPosition()))
            throw new IllegalArgumentException("Animals are not on the same position (Animal1 on " +
                    this.position + " and Animal2 on " + dad.getPosition());

        int energyFromMom = this.getEnergy()/4;
        this.changeEnergy(-energyFromMom);
        int energyFromDad = dad.getEnergy()/4;
        dad.changeEnergy(-energyFromDad);

        int childEnergy = energyFromDad + energyFromMom;

        Genome childsGenome = this.getGenome().copyGenome();  //we need to copy - otherwise the pointer of the genome will be assigned to the child
        childsGenome.insertOtherGenome(dad.getGenome());

        return new Animal(childPosition, childsGenome, childEnergy);

    }

    @Override
    public boolean isDead() {
        return this.energy <= 0;
    }


    public void setMap(IWorldMap map){this.map = map;}


    @Override
    public int getEnergy() {
        return energy;
    }

    @Override
    public Genome getGenome() {
        return this.genes;
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    @Override
    public int getId() {
        return id;
    }

    public String toString(){
        return String.valueOf(this.energy);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return id == animal.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
