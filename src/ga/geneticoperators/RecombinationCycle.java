package ga.geneticoperators;

import algorithms.IntVectorIndividual;
import algorithms.Problem;

import java.util.Arrays;

public class RecombinationCycle<I extends IntVectorIndividual, P extends Problem<I>> extends Recombination<I, P> {

    public RecombinationCycle(double probability) {
        super(probability);
    }

    @Override
    public void recombine(I ind1, I ind2)
    {
        int size = ind1.getNumGenes();
        int[] child1 = new int[size];
        int[] child2 = new int[size];

        Arrays.fill(child1, -1);
        Arrays.fill(child2, -1);

        cycle(ind1, ind2, 0, child1, child2);
        cycle(ind1, ind2, 1, child2, child1);

        // Fill children
        for (int i = 0; i < size; i++)
        {
            if(child1[i] == -1)
            {
                child1[i] = ind1.getGene(i);
                child2[i] = ind2.getGene(i);
            }
        }

        // Replace parent's genome
        for (int i = 0; i < size; i++)
        {
            ind1.setGene(i, child1[i]);
            ind2.setGene(i, child2[i]);
        }
    }

    private void cycle(I ind1, I ind2, int index, int[] child1, int[] child2)
    {
        int allele = ind1.getGene(index);
        int value1 = allele, value2;

        do
        {
            value2 = ind2.getGene(index);

            child1[index] = value1;
            child2[index] = value2;

            index = ind1.getIndexof(value2);
            value1 = ind1.getGene(index);
        }
        while(value1 != allele);
    }

    @Override
    public String toString() {
        return "Cycle";
    }
}