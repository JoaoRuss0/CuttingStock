package random;

import algorithms.Algorithm;
import algorithms.AlgorithmEvent;
import algorithms.Individual;
import algorithms.Problem;

import java.util.Random;

public class RandomAlgorithm<I extends Individual, P extends Problem<I>> extends Algorithm<I, P> {

    public RandomAlgorithm(int maxIterations, Random rand) {
        super(maxIterations, rand);
    }

    @Override
    public I run(P problem)
    {
        t = 0;
        globalBest = problem.getNewIndividual();
        globalBest.computeFitness();
        fireIterationEnded(new AlgorithmEvent(this));

        while (t < maxIterations && !stopped)
        {
            I new_individual = problem.getNewIndividual();
            new_individual.computeFitness();

            if(new_individual.getFitness() < globalBest.getFitness())
            {
                globalBest = new_individual;
            }

            t++;
            fireIterationEnded(new AlgorithmEvent(this));
        }
        fireRunEnded(new AlgorithmEvent(this));
        return globalBest;
    }
}
