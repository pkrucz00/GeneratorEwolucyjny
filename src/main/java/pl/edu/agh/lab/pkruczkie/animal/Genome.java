package pl.edu.agh.lab.pkruczkie.animal;

import java.util.Arrays;
import java.util.Random;

public class Genome implements IGenotype{
    int[] listOfGenes;
    short[] numberOfGenesOfAGivenType = new short[8];  //initialized with 0s

    public Genome(int[] genes){
        int n = genes.length;
        this.listOfGenes = Arrays.copyOf(genes, n);

        this.countGenes();
        if (!this.isGenomeValid())
            this.repairGenome();
    }

    private void countGenes(){
        for (int gene: this.listOfGenes){
            this.numberOfGenesOfAGivenType[gene]++;
        }
    }

     public boolean isGenomeValid(){
        for (int geneNumber: this.numberOfGenesOfAGivenType){
            if (geneNumber == 0) {
                return false;
            }
        }
        return true;
    }


    private void repairGenome(){
        for (int gene = 0; gene < 8; gene++){
            if (this.numberOfGenesOfAGivenType[gene] == 0){
                Random random = new Random();
                int randGeneInd = random.nextInt(this.listOfGenes.length); //we take index not the value of the gene - that will increase our chance of finding the right gene right away
                int randGeneValue = this.listOfGenes[randGeneInd];
                while (!(this.numberOfGenesOfAGivenType[randGeneValue] > 1)){
                    randGeneInd = random.nextInt(listOfGenes.length);
                    randGeneValue = this.listOfGenes[randGeneInd];
                }
                this.listOfGenes[randGeneInd] = gene;
                this.numberOfGenesOfAGivenType[gene]++;
                this.numberOfGenesOfAGivenType[randGeneValue]--;
            }
        }
    }

    @Override
    public int getRandomGene() {
        Random random = new Random();
        int n = this.listOfGenes.length;
        return this.listOfGenes[random.nextInt(n)];
    }

    public void insertOtherGenome(Genome other){
        int[] otherGenes = other.getListOfGenes();
        int n = this.listOfGenes.length;
        if (n != otherGenes.length){
            throw new IllegalArgumentException("Genes are of different sizes: " + n + " and " + otherGenes.length);
        }

        Random random = new Random();
        int lowerInd = random.nextInt(n-2) + 1;
        int higherInd = random.nextInt(n - lowerInd - 1) + lowerInd + 1;  //the off-by-1 offset is so we have three NONZERO blocks of genes

        for (int i = lowerInd; i <= higherInd; i++){
            int oldGene = this.listOfGenes[i];
            int newGene = otherGenes[i];
            this.listOfGenes[i] = newGene;
            this.numberOfGenesOfAGivenType[oldGene]--;
            this.numberOfGenesOfAGivenType[newGene]++;
        }

        if (!this.isGenomeValid()){
            this.repairGenome();
        }
    }

    public int[] getListOfGenes() {
        return listOfGenes;
    }

    public Genome copyGenome(){
        return new Genome(this.getListOfGenes());
    }

    public String toStatisticsString(){
        StringBuilder result = new StringBuilder("<html><body>");
        for (int i = 0; i < 8; i++){
            String tmp = "%d: %d&nbsp;&nbsp;&nbsp;&nbsp;";     //there are no tabs in html. I know it looks awful, but I'couldn'd find another way:(
            result.append(String.format(tmp, i, this.numberOfGenesOfAGivenType[i]));
            if (i==3) result.append("<br>");
        }
        result.append("</body></html>");
        return result.toString();
    }

    @Override
    public String toString() {
        Arrays.sort(listOfGenes);
        return Arrays.toString(listOfGenes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genome genome = (Genome) o;
        return Arrays.equals(numberOfGenesOfAGivenType, genome.numberOfGenesOfAGivenType);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(numberOfGenesOfAGivenType);
    }
}
