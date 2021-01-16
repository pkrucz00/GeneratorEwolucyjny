package pl.edu.agh.lab.pkruczkie.maps;

import pl.edu.agh.lab.pkruczkie.animal.IAnimal;

import java.util.Comparator;

public class EnergyComparator implements Comparator<IAnimal> {

    @Override
    public int compare(IAnimal o1, IAnimal o2) {
        if (o1.equals(o2)){
            return 0;
        }
        int energy1 = o1.getEnergy();
        int energy2 = o2.getEnergy();

        if (energy1 < energy2)     //reversed order - energy is in descending order
            return 1;
        else if (energy1 > energy2)
            return -1;
        else{
            int id1 = o1.getId();
            int id2 = o2.getId();
            if (id1 > id2)
                return 1;
            else
                return -1;
        }
    }
}
