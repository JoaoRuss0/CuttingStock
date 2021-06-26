package algorithms;

import java.util.ArrayList;

public abstract class IntVectorIndividual<P extends Problem, I extends IntVectorIndividual> extends Individual<P, I>
{
    protected int[] genome;
    protected int[] rotations;

    public IntVectorIndividual(P problem, int size) {
        super(problem);
        genome = new int[size];
        rotations = new int[size];
    }

    public IntVectorIndividual(IntVectorIndividual<P, I> original) {
        super(original);
        this.genome = new int[original.genome.length];
        this.rotations = new int[original.rotations.length];
        System.arraycopy(original.genome, 0, genome, 0, genome.length);
        System.arraycopy(original.rotations, 0, rotations, 0, rotations.length);
    }

    @Override
    public int getNumGenes() {
        return genome.length;
    }

    public int getIndexof(int value){
        for (int i = 0; i < genome.length; i++) {
            if (genome[i] == value)
                return i;
        }
        return -1;
    }

    public ArrayList<Integer> subList(int cut1, int cut2) {
        ArrayList<Integer> values = new ArrayList<>();
        for (int i = cut1; i < cut2; i++) {
            values.add(genome[i]);
        }
        return values;
    }

    public int getGene(int index) {
        return genome[index];
    }

    public void setGene(int index, int newValue) {
        genome[index] = newValue;
    }

    public int getRotation(int index) {
        return rotations[index];
    }

    public void setRotation(int index, int newValue) {
        rotations[index] = newValue;
    }

    @Override
    public void swapGenes(IntVectorIndividual other, int index) {
        int aux = genome[index];
        genome[index] = other.genome[index];
        other.genome[index] = aux;
    }

    public int[] getGenome() {
        return genome;
    }
}
