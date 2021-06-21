package ga.geneticoperators;

import ga.GeneticAlgorithm;
import algorithms.IntVectorIndividual;
import algorithms.Problem;

public class MutationInsert<I extends IntVectorIndividual, P extends Problem<I>> extends Mutation<I, P> {

    public MutationInsert(double probability) {
        super(probability);
    }

    @Override
    public void mutate(I ind) {
        //Get 2 random different genome positions
        int cut1 = GeneticAlgorithm.random.nextInt(ind.getNumGenes());
        int cut2;
        do {
            cut2 = GeneticAlgorithm.random.nextInt(ind.getNumGenes());
        }while (cut1==cut2);

        //If cut1 is bigger than cut2, switch values for the if loop
        if (cut1 > cut2) {
            int aux = cut1;
            cut1 = cut2;
            cut2 = aux;
        }

        // Repeatedly swap allele values from i to i + 1
        // Allele values will be shifted to the right by 1 and starting allele value will go all the way to the left
        for(int i = cut2-1; i > cut1 ; i--)
        {
            int aux = ind.getGene(i + 1);
            ind.setGene(i + 1, ind.getGene(i));
            ind.setGene(i, aux);
        }

    }


    @Override
    public String toString() {
        return "Insert";
    }
}