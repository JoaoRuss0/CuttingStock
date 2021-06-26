package stockingproblem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Item {

    private int id;
    private char representation;
    private int lines;
    private int columns;
    private int[][] matrix;
    private HashMap<Integer, int[][]> computedRotations;
    private int[] possibleRotations;

    public Item(int problemHeight, int id, int[][] matrix) {
        this.id = id;
        this.representation = (id < 26) ? (char) ('A' + id) : (char) ('A' + id + 6);
        this.lines = matrix.length;
        this.columns = matrix[0].length;
        this.matrix = matrix;
        this.computedRotations = new HashMap();

        computeItemRotations(problemHeight);
    }

    private void computeItemRotations(int problemHeight)
    {
        computedRotations.put(0, matrix);

        boolean has_zeros = false;

        check_zeros:
        for (int i = 0; i < lines; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                if(matrix[i][j] == 0)
                {
                    has_zeros = true;
                    break check_zeros;
                }
            }
        }

        // No zeros
        // Square matrix
        if(columns == lines && !has_zeros)
        {
            possibleRotations = new int[]{0};
            return;
        }

        // No zeros
        // Only possible rotations for a matrix filled with 1s is either vertical or horizontal
        if(!has_zeros && columns <= problemHeight)
        {
            possibleRotations = new int[]{0, 1};
            computedRotations.put(1, rotate90DegreesClockwise(matrix));
            return;
        }

        // No zeros
        // Only possible rotations for a matrix filled with 1s is either vertical or horizontal
        // However if the new rotated matrix's height is bigger than the problem height
        if(!has_zeros && columns > problemHeight)
        {
            possibleRotations = new int[]{0};
            return;
        }

        // Has zeros
        // If rotated matrix's  height surpasses problem's height there are only 2 rotations possible
        if(columns > problemHeight)
        {
            possibleRotations = new int[]{0, 2};
            computedRotations.put(2, rotate90DegreesClockwise(rotate90DegreesClockwise(matrix)));
            return;
        }

        // Has zeros
        possibleRotations = new int[]{0, 1, 2, 3};

        for(int i = 0; i < 3; i++)
        {
            computedRotations.put(i + 1, rotate90DegreesClockwise(computedRotations.get(i)));
        }
    }

    private int[][] rotate90DegreesClockwise(int[][] oldMatrix)
    {
        int cols = oldMatrix[0].length, rows = oldMatrix.length;
        int[][] rotated = new int[cols][rows];
        int newCol, newRow = cols - 1;

        for (int oldCol = cols - 1; oldCol >= 0; oldCol--)
        {
            newCol = rows - 1;

            for (int oldRow = 0; oldRow < rows; oldRow++)
            {
                rotated[newRow][newCol] = oldMatrix[oldRow][oldCol];
                newCol--;
            }
            newRow--;
        }

        return rotated;
    }

    public int[][] getRotation(int index)
    {
        return this.computedRotations.get(index);
    }

    public int[] getPossibleRotations()
    {
        return this.possibleRotations;
    }

    public int possibleRotations(int index)
    {
        return this.possibleRotations[index];
    }

    public int getId() {
        return id;
    }

    public char getRepresentation() {
        return representation;
    }

    public int getLines() {
        return lines;
    }

    public int getColumns() {
        return columns;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n");
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < columns; j++) {
                sb.append(matrix[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}
