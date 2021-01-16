package pl.edu.agh.lab.pkruczkie.observers.animalPickerObservers;

import pl.edu.agh.lab.pkruczkie.animal.IAnimal;
import pl.edu.agh.lab.pkruczkie.frames.SimulationPanel;
import pl.edu.agh.lab.pkruczkie.simulationEngine.Engine;

import javax.swing.*;


public class AnimalPicker {
    private final IAnimal pickedAnimal;
    private final IAnimalPickerObserver childrenObserver;
    private final IAnimalPickerObserver descendantsObserver;
    private final SimulationPanel simulationPanel;
    int daysElapsed;
    int n;

    public AnimalPicker(IAnimal animal, SimulationPanel simulationPanel, int n){
        this.pickedAnimal = animal;
        this.n = n;
        this.simulationPanel = simulationPanel;

        Engine engine = simulationPanel.getEngine();
        childrenObserver = new AnimalsChildrenObserver(pickedAnimal, engine);
        descendantsObserver = new AnimalsDescendantsObserver(pickedAnimal, engine);
    }


    public void update(){
        this.daysElapsed++;
        if (this.shouldShowAlert()) {
            JOptionPane.showMessageDialog(simulationPanel, this.getMessage());
            simulationPanel.setAnimalPicker(null);
        }
    }

    public boolean shouldShowAlert(){
        return pickedAnimal.isDead() || this.daysElapsed == this.n;
    }

    public String getMessage(){
        String message = "";
        if (pickedAnimal.isDead())
            message += "Animal died after " + this.daysElapsed + " days since being picked.\n";
        message += "Animal had %d children and %d descendants";
        return String.format(message, childrenObserver.getResult(), descendantsObserver.getResult());
    }

    public IAnimal getPickedAnimal(){
        return pickedAnimal;
    }
}
