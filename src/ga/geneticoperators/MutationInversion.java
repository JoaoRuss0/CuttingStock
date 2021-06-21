package ga.geneticoperators;

import algorithms.IntVectorIndividual;
import algorithms.Problem;
import ga.GeneticAlgorithm;

public class MutationInversion<I extends IntVectorIndividual, P extends Problem<I>> extends Mutation<I, P> {

    public MutationInversion(double probability) {
        super(probability);
    }

    @Override
    public void mutate(I ind)
    {
        // INVERSION MUTATION
        int size = ind.getNumGenes();

        // Get 2 random different genome positions
        int pos1 = GeneticAlgorithm.random.nextInt(size);
        int pos2 = GeneticAlgorithm.random.nextInt(size);

        while(pos2 == pos1)
        {
            pos2 = GeneticAlgorithm.random.nextInt(size);
        }

        // Swap values for loop if necessary
        if(pos2 < pos1)
        {
            int aux = pos2;
            pos2 = pos1;
            pos1 = aux;
        }

        // Swap allele values
        for(int i =  0; i < (pos2 - pos1 + 1)/2; i++)
        {
            int aux = ind.getGene(pos1 + i);
            ind.setGene(pos1 + i, ind.getGene(pos2 - i));
            ind.setGene(pos2 - i, aux);
        }
    }

    @Override
    public String toString(){
        return "Inversion";
    }
}