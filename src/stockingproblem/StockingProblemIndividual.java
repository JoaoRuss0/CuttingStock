package stockingproblem;

import algorithms.Algorithm;
import algorithms.IntVectorIndividual;

import java.util.*;

public class StockingProblemIndividual extends IntVectorIndividual<StockingProblem, StockingProblemIndividual>
{
    private int[][] material;
    private int num_cuts, materialMaxSize;

    public StockingProblemIndividual(StockingProblem problem, int size)
    {
        super(problem, size);

        // Fill genome with pieces from 0 to length - 1
        for (int i = 0; i < genome.length; i++)
        {
            genome[i] = i;
        }

        shuffleGenome();
    }

    public StockingProblemIndividual(StockingProblemIndividual original)
    {
        super(original);
        this.num_cuts = original.num_cuts;
        this.materialMaxSize = original.materialMaxSize;
        this.material = original.material.clone();
    }

    @Override
    public double computeFitness()
    {
        material = new int[problem.getMaterialHeight()][problem.getMaterialWidth()];
        ArrayList<Item> items = problem.getItems();
        Item item_to_place;

        int length = material[0].length;
        materialMaxSize = 0;

        // Fill material matrix
        // Loop through genome and get items
        for (int m = 0; m < genome.length; m++)
        {
            item_to_place = items.get(genome[m]);

            // Loop material matrix from left to right
            for (int i = 0; i < length; i++)
            {
                // Loop material matrix from top to bottom
                for (int j = 0; j < material.length; j++)
                {
                    if(checkValidPlacement(item_to_place, material, j, i))
                    {
                        place_item_in_position(item_to_place, i, j);

                        // Stop searching
                        i = length;
                        break;
                    }
                }
            }
        }

        countCuts();

        this.fitness = problem.getNumColsPer() * num_cuts + problem.getMaxSizePer() * materialMaxSize;
        return fitness;
    }

    private boolean checkValidPlacement(Item item, int[][] material, int lineIndex, int columnIndex)
    {
        int[][] itemMatrix = item.getMatrix();
        for (int i = 0; i < itemMatrix.length; i++) {
            for (int j = 0; j < itemMatrix[i].length; j++) {
                if (itemMatrix[i][j] != 0) {
                    if ((lineIndex + i) >= material.length
                            || (columnIndex + j) >= material[0].length
                            || material[lineIndex + i][columnIndex + j] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Fitness: ");
        sb.append(fitness);
        sb.append("\n");

        sb.append("Genome: ");
        sb.append(Arrays.toString(genome));
        sb.append("\n");

        sb.append("Material: \n");
        for (int i = 0; i < problem.getMaterialHeight(); i++)
        {
            sb.append("[");
            for (int j = 0; j < problem.getMaterialWidth(); j++)
            {
                if(material[i][j] == 0)
                {
                    sb.append("0 ");
                    continue;
                }
                sb.append((char)material[i][j] + " ");
            }
            sb.append("]\n");
        }
        sb.append("\n");

        sb.append("Number of cuts: ");
        sb.append(num_cuts);
        sb.append("\n");

        sb.append("MaterialMaxSize: ");
        sb.append(materialMaxSize);
        sb.append("\n");

        return sb.toString();
    }

    /**
     * @param i
     * @return 1 if this object is BETTER than i, -1 if it is WORST than I and
     * 0, otherwise.
     */
    @Override
    public int compareTo(StockingProblemIndividual i)
    {
        return (this.fitness == i.getFitness()) ? 0 : (this.fitness < i.getFitness()) ? 1 : -1;
    }

    @Override
    public StockingProblemIndividual clone()
    {
        return new StockingProblemIndividual(this);
    }

    private void shuffleGenome()
    {
        int stored_int, random_pos, length = genome.length;

        // Loop through genome
        for (int i = 0; i < length; i++)
        {
            // Get a random position, between 0 <-> length-1
            random_pos = Algorithm.random.nextInt(length);

            // Switch values from i position to random position
            stored_int = genome[i];
            genome[i] = genome[random_pos];
            genome[random_pos] = stored_int;
        }
    }

    private void place_item_in_position(Item item_to_place, int i, int j)
    {
        int[][] item_matrix = item_to_place.getMatrix();
        int right_most_column = i + item_to_place.getColumns();

        if(materialMaxSize < right_most_column)
        {
            materialMaxSize = right_most_column;
        }

        // Loop item lines
        for (int k = 0; k < item_matrix.length; k++)
        {
            // Loop item columns
            for (int l = 0; l < item_matrix[0].length; l++)
                {
                // Only place if value inside item position is 1
                if(item_matrix[k][l] != 0)
                {
                    material[j+k][i+l] = item_to_place.getRepresentation();
                }
            }
        }
    }

    private void countCuts()
    {
        num_cuts = 0;

        // Count horizontal cuts
        // Loop material matrix from top to bottom
        for (int i = 0; i < material.length; i++)
        {
            // Loop material matrix from left to right
            for (int j = 0; j < material[0].length - 1; j++)
            {
                if(material[i][j] != material[i][j + 1])
                {
                    num_cuts++;
                }
            }
        }

        // Count vertical cuts
        // Loop material matrix from left to right
        for (int i = 0; i < material[0].length; i++)
        {
            // Loop material matrix from top to bottom
            for (int j = 0; j < material.length - 1; j++)
            {
                if(material[j][i] != material[j + 1][i])
                {
                    num_cuts++;
                }
            }
        }

        // Check if cut on max size wasn't counted yet
        for (int i = 0; i < problem.getMaterialHeight(); i++)
        {
            if(material[i][materialMaxSize - 1] == 0)
            {
                 num_cuts++;
            }
        }
    }
}