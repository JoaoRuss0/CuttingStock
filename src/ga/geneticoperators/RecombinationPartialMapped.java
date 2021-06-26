package ga.geneticoperators;

import ga.GeneticAlgorithm;
import algorithms.IntVectorIndividual;
import algorithms.Problem;

public class RecombinationPartialMapped<I extends IntVectorIndividual, P extends Problem<I>> extends Recombination<I, P> {

    public RecombinationPartialMapped(double probability) {
        super(probability);
    }

    private int[][] child1, child2, segment1, segment2;

    private int cut1;
    private int cut2;

    @Override
    public void recombine(I ind1, I ind2) {
        child1 = new int[2][ind1.getNumGenes()];
        child2 = new int[2][ind2.getNumGenes()];

        cut1 = GeneticAlgorithm.random.nextInt(ind1.getNumGenes());

        do {
            cut2 = GeneticAlgorithm.random.nextInt(ind1.getNumGenes());
        } while (cut1 == cut2);

        if (cut1 > cut2) {
            int aux = cut1;
            cut1 = cut2;
            cut2 = aux;
        }

        create_Segments(cut1, cut2, ind1, ind2);
        crossOver(child1, ind1);
        crossOver(child2, ind2);

        for (int i = 0; i < ind1.getNumGenes(); i++) {
            ind1.setGene(i, child1[0][i]);
            ind1.setRotation(i, child1[1][i]);

            ind2.setGene(i, child2[0][i]);
            ind2.setRotation(i, child2[1][i]);
        }

    }

    private boolean check_forDuplicates(int[][] offspring, int indexOfElement) {
        for (int index = 0; index < offspring[0].length; index++) {
            if ((offspring[0][index] == offspring[0][indexOfElement]) &&
                    (indexOfElement != index)) {
                return true;
            }
        }
        return false;
    }

    // If Element is Duplicated, replace it by using its mapping //
    private void sort_Duplicates(int[][] offspring, int indexOfElement) {
        for (int index = 0; index < segment1[0].length; index++) {
            if (segment1[0][index] == offspring[0][indexOfElement]) {
                offspring[0][indexOfElement] = segment2[0][index];
                offspring[1][indexOfElement] = segment2[1][index];
            } else if (segment2[0][index] == offspring[0][indexOfElement]) {
                offspring[0][indexOfElement] = segment1[0][index];
                offspring[1][indexOfElement] = segment1[1][index];
            }
        }
    }

    private void create_Segments(int cutPoint1, int cutPoint2, I ind1, I ind2) {
        int capacity_ofSegments = (cutPoint2 - cutPoint1) + 1;
        segment1 = new int[2][capacity_ofSegments];
        segment2 = new int[2][capacity_ofSegments];
        int segment1and2Index = 0;
        for (int index = 0; index < ind1.getNumGenes(); index++) {
            if ((index >= cutPoint1) && (index <= cutPoint2)) {
                int x = ind1.getGene(index);
                int xRotation = ind1.getRotation(index);
                int y = ind2.getGene(index);
                int yRotation = ind2.getRotation(index);
                ;

                segment1[0][segment1and2Index] = x;
                segment1[1][segment1and2Index] = xRotation;
                segment2[0][segment1and2Index] = y;
                segment2[1][segment1and2Index] = yRotation;
                segment1and2Index++;
            }
        }
    }

    private void insert_Segments(int[][] offspring, int[][] segment) {
        int segmentIndex = 0;
        for (int index = 0; index < offspring[0].length; index++) {
            if ((index >= cut1) && (index <= cut2)) {
                offspring[0][index] = segment[0][segmentIndex];
                offspring[1][index] = segment[1][segmentIndex];
                segmentIndex++;
            }
        }
    }

    // offspring2 gets segment 1, offspring1 gets segment2 //
    public void crossOver(int[][] offspring, I ind) {
        if (offspring == child1) {
            int[][] segment = segment2;
            insert_Segments(offspring, segment);
        } else if (offspring == child2) {
            int[][] segment = segment1;
            insert_Segments(offspring, segment);
        }

        for (int index = 0; index < offspring[0].length; index++) {
            if ((index < cut1) || (index > cut2)) {
                offspring[0][index] = ind.getGene(index);
                offspring[1][index] = ind.getRotation(index);
            }
        }

        for (int index = 0; index < offspring[0].length; index++) {
            if ((index < cut1) || (index > cut2)) {
                while (check_forDuplicates(offspring, index)) {
                    sort_Duplicates(offspring, index);
                }
            }
        }
    }


    @Override
    public String toString() {
        return "PMX";
    }
}