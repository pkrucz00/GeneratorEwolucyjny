package pl.edu.agh.lab.pkruczkie.simulationEngine;

public class Parameters {
    public final int width;
    public final int height;
    public final float jungleRatio;
    public final int startEnergy;
    public final int moveEnergy;
    public final int plantEnergy;
    public final int numberOfInitialAnimals;

    public Parameters(int[] params){
        this.width = params[0];
        this.height = params[1];
        this.jungleRatio = (float)params[2]/100;
        this.startEnergy = params[3];
        this.moveEnergy = params[4];
        this.plantEnergy = params[5];
        this.numberOfInitialAnimals = params[6];
    }
}

