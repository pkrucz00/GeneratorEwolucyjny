package elements.animal;

public interface IGenotype {
    boolean isValidGenotype();

    int[] splitGenomes(IGenotype other);

    void repairGenome();
}
