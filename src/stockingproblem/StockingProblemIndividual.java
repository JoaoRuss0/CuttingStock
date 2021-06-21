package stockingproblem;

import algorithms.Algorithm;
import algorithms.IntVectorIndividual;

import java.util.*;

public class StockingProblemIndividual extends IntVectorIndividual<StockingProblem, StockingProblemIndividual>
{
    private ArrayList<Integer>[] material;
    private int[] rotations;
    private int num_cuts, materialMaxSize;

    public StockingProblemIndividual(StockingProblem problem, int size)
    {
        super(problem, size);

        // Fill genome with pieces from 0 to length - 1
        for (int i = 0; i < genome.length; i++)
        {
            genome[i] = i;
        }

        // Fill rotation array with 0, meaning piece rotated 0 times to the right
        rotations = new int[genome.length];
        Arrays.fill(rotations, 0);

        shuffleGenome();
    }

    public StockingProblemIndividual(StockingProblemIndividual original)
    {
        super(original);
        this.num_cuts = original.num_cuts;
        this.materialMaxSize = original.materialMaxSize;
        this.material = original.material.clone();
        this.rotations = original.rotations.clone();
    }

    @Override
    public double computeFitness()
    {
        // Initialize material
        material = new ArrayList[problem.getMaterialHeight()];

        for (int i = 0; i < material.length; i++)
        {
            material[i] = new ArrayList();
        }

        // Get needed variables
        ArrayList<Item> items = problem.getItems();
        Item item_to_place;
        materialMaxSize = 0;

        // Fill material matrix
        // Loop through genome and get items
        for (int l = 0; l < genome.length; l++)
        {
            item_to_place = items.get(genome[l]);
            int i = 0;

            out:

            while(true)
            {
                // Loop material matrix from top to bottom
                for (int j = 0; j < material.length; j++)
                {
                    // Check if rotations fit
                    for (int k = 0; k < 4; k++)
                    {
                        Item new_item = new Item(item_to_place.getId(), problem.getRotationsIndex(genome[l] * 4 + k));

                        if (checkValidPlacement(new_item, i, j))
                        {
                            place_item_in_position(new_item, i, j);

                            rotations[l] = k;

                            // Stop searching
                            break out;
                        }
                    }
                }
                i++;
            }
        }

        zeroPaddings();
        countCuts();

        this.fitness = problem.getNumColsPer() * num_cuts + problem.getMaxSizePer() * materialMaxSize;
        return fitness;
    }

    private boolean checkValidPlacement(Item item, int i, int j)
    {
        int[][] itemMatrix = item.getMatrix();

        for (int k = 0; k < item.getLines(); k++)
        {
            if(k + j > problem.getMaterialHeight() - 1)
            {
                return false;
            }

            // We do not need to search to see if matrix can be placed in a line if line has no values
            if(material[j+k].size() != 0)
            {
                int last_line_filled_position = material[j+k].size() - 1;

                // if last filled position is inferior to the first position we want to fill
                if(last_line_filled_position < i)
                {
                    continue;
                }

                for (int l = 0; l < item.getColumns(); l++)
                {
                    // if last filled position was bigger than the first position to be filled but now is inferior
                    if(last_line_filled_position < l + i)
                    {
                        break;
                    }

                    if (itemMatrix[k][l] != 0)
                    {
                        if (material[j+k].get(l + i) != 0)
                        {
                            return false;
                        }
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

        sb.append("Genome:    ");
        sb.append(Arrays.toString(genome));
        sb.append("\n");

        sb.append("Rotations: ");
        sb.append(Arrays.toString(rotations));
        sb.append("\n\n");

        sb.append("Material: \n");
        for (int i = 0; i < problem.getMaterialHeight(); i++)
        {
            sb.append("[");
            for (int j = 0; j < material[i].size() ; j++)
            {
                if(material[i].get(j) == 0)
                {
                    sb.append("0 ");
                    continue;
                }
                sb.append((char) (int)material[i].get(j) + " ");
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
                int last_line_filled_position = material[j+k].size() - 1;

                // Only place if value inside item position is 1
                if(item_matrix[k][l] != 0)
                {
                    if(last_line_filled_position < i+l)
                    {
                        //Place missing positons on the left side
                        for (int m = last_line_filled_position; m < i + l - 1; m++)
                        {
                            material[j+k].add(0);
                        }

                        material[j+k].add((int)item_to_place.getRepresentation());
                    }
                    else
                    {
                        material[j+k].set(i+l, (int)item_to_place.getRepresentation());
                    }
                }
            }
        }

    }

    private void countCuts()
    {
        num_cuts = 0;

        // Loop matrix rows and compare each position to the one on its right and below
        for (int i = 0; i < material.length - 1; i++)
        {
            for (int j = 0; j < materialMaxSize - 1; j++)
            {
                if(material[i].get(j) != material[i].get(j + 1))
                {
                    num_cuts++;
                }

                if(material[i].get(j) != material[i + 1].get(j))
                {
                    num_cuts++;
                }
            }

            // Necessary because last column positions are not going to get compared to the positons below
            if(material[i].get(materialMaxSize - 1) != material[i + 1].get(materialMaxSize - 1))
            {
                num_cuts++;
            }
        }

        // Necessary because last row positions are not going to get compared to the positons on the right
        for (int j = 0; j < materialMaxSize - 1; j++)
        {
            if(material[material.length - 1].get(j) != material[material.length - 1].get(j + 1))
            {
                num_cuts++;
            }
        }
    }

    private void zeroPaddings()
    {
        for (int j = 0; j < material.length; j++)
        {
            for (int i = material[j].size(); i < materialMaxSize; i++)
            {
                material[j].add(0);
            }
        }
    }
}