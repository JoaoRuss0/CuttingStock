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
        int[] child1 = new int[size];
        int[] child2 = new int[size];

        Arrays.fill(child1, -1);
        Arrays.fill(child2, -1);

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
            ind1.setGene(i, child1[i]);
            ind2.setGene(i, child2[i]);
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

    private void getChild(I mainParent, I secondParent, int cut1, int cut2, int[] child, int size)
    {
        // Fill child with main parent's random segment
        for (int i = cut1; i <= cut2; i++)
        {
            child[i] = mainParent.getGene(i);
        }

        int[] notUsed = new int[size - (cut2 - cut1 + 1)];
        int j = 0;

        // Get not yet used alleles
        for (int i = 0; i < size; i++)
        {
            if(!arrayContains(child, secondParent.getGene((cut2 + 1 + i) % size)))
            {
                notUsed[j] = secondParent.getGene((cut2 + 1 + i) % size);
                j++;
            }
        }

        // Fill child with not yet used alleles
        for (int i = 0; i < notUsed.length; i++)
        {
            child[(cut2 + 1 + i) % size] = notUsed[i];
        }
    }

    @Override
    public String toString(){
        return "Order1";
    }    
}