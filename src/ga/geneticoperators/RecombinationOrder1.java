package ga.geneticoperators;

import algorithms.IntVectorIndividual;
import algorithms.Problem;
import ga.GeneticAlgorithm;

import java.util.Arrays;

public class RecombinationOrder1<I extends IntVectorIndividual, P extends Problem<I>> extends Recombination<I, P> {

    public RecombinationOrder1(double probability) {
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

        int cut1 = GeneticAlgorithm.random.nextInt(size);
        int cut2 = GeneticAlgorithm.random.nextInt(size);

        while(cut1 == cut2)
        {
            cut2 = GeneticAlgorithm.random.nextInt(size);
        }

        if(cut1 > cut2)
        {
            int aux = cut1;
            cut1 = cut2;
            cut2 = aux;
        }

        getChild(ind1, ind2, cut1, cut2, child1, size);
        getChild(ind2, ind1, cut1, cut2, child2, size);

        // Replace parent's genome
        for (int i = 0; i < size; i++)
        {
            ind1.setGene(i, child1[0][i]);
            ind1.setRotation(i, child1[1][i]);
            ind2.setGene(i, child2[0][i]);
            ind2.setRotation(i, child2[1][i]);
        }
    }

    private boolean arrayContains(int[] array, int value)
    {
        for (int i = 0; i < array.length; i++)
        {
            if(array[i] == value)
            {
                return true;
            }
        }

        return false;
    }

    private void getChild(I mainParent, I secondParent, int cut1, int cut2, int[][] child, int size)
    {
        // Fill child with main parent's random segment
        for (int i = cut1; i <= cut2; i++)
        {
            child[0][i] = mainParent.getGene(i);
            child[1][i] = mainParent.getRotation(i);
        }

        int[][] notUsed = new int[2][size - (cut2 - cut1 + 1)];
        int j = 0;
        int index;

        // Get not yet used alleles
        for (int i = 0; i < size; i++)
        {
            index = (cut2 + 1 + i) % size;

            if(!arrayContains(child[0], secondParent.getGene(index)))
            {
                notUsed[0][j] = secondParent.getGene(index);
                notUsed[1][j] = secondParent.getRotation(index);
                j++;
            }
        }

        // Fill child with not yet used alleles
        for (int i = 0; i < notUsed[0].length; i++)
        {
            index = (cut2 + 1 + i) % size;
            child[0][index] = notUsed[0][i];
            child[1][index] = notUsed[1][i];
        }
    }

    @Override
    public String toString(){
        return "Order1";
    }    
}