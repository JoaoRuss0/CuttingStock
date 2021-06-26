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
        int[][] child1 = new int[2][size];
        int[][] child2 = new int[2][size];

        Arrays.fill(child1[0], -1);
        Arrays.fill(child1[1], -1);
        Arrays.fill(child2[0], -1);
        Arrays.fill(child2[1], -1);

        cycle(ind1, ind2, 0, child1, child2);
        cycle(ind1, ind2, 1, child2, child1);

        // Fill children
        for (int i = 0; i < size; i++)
        {
            if(child1[0][i] == -1)
            {
                child1[0][i] = ind1.getGene(i);
                child1[1][i] = ind1.getRotation(i);
                child2[0][i] = ind2.getGene(i);
                child2[1][i] = ind2.getRotation(i);
            }
        }

        // Replace parent's genome
        for (int i = 0; i < size; i++)
        {
            ind1.setGene(i, child1[0][i]);
            ind1.setRotation(i, child1[1][i]);
            ind2.setGene(i, child2[0][i]);
            ind2.setRotation(i, child2[1][i]);
        }
    }

    private void cycle(I ind1, I ind2, int index, int[][] child1, int[][] child2)
    {
        int allele = ind1.getGene(index);
        int value1 = allele, value2, value1Rotation = ind1.getRotation(index), value2Rotation;

        do
        {
            value2 = ind2.getGene(index);
            value2Rotation = ind2.getRotation(index);

            child1[0][index] = value1;
            child1[1][index] = value1Rotation;
            child2[0][index] = value2;
            child2[1][index] = value2Rotation;

            index = ind1.getIndexof(value2);
            value1 = ind1.getGene(index);
            value1Rotation = ind1.getRotation(index);
        }
        while(value1 != allele);
    }

    @Override
    public String toString() {
        return "Cycle";
    }
}