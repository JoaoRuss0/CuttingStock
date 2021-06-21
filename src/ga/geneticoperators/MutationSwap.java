package ga.geneticoperators;

import algorithms.IntVectorIndividual;
import algorithms.Problem;
import ga.GeneticAlgorithm;

public class MutationSwap<I extends IntVectorIndividual, P extends Problem<I>> extends Mutation<I, P> {

    public MutationSwap(double probability) {
        super(probability);
    }

    @Override
    public void mutate(I ind)
    {
        // SWAP MUTATION
        int size = ind.getNumGenes();

        // Get 2 random different genome positions
        int pos1 = GeneticAlgorithm.random.nextInt(size);
        int pos2 = GeneticAlgorithm.random.nextInt(size);

        while(pos2 == pos1)
        {
            pos2 = GeneticAlgorithm.random.nextInt(size);
        }

        // Swap allele values
        int aux = ind.getGene(pos1);
        ind.setGene(pos1, ind.getGene(pos2));
        ind.setGene(pos2, aux);
    }

    @Override
    public String toString(){
        return "Swap";
    }
}