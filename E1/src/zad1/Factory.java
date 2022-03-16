package zad1;
import java.util.Arrays;
import java.util.Random;


public class Factory {
    private static Random random = new Random();
    public final int x, y;
    public final int gridSize;
    public final int numberOfMachines;
    public final InstanceEnum instanceEnum;
    private final int[][] partialMultiplicationForIds;

    public Factory(InstanceEnum instanceEnum, String folderPath) {
        this.instanceEnum = instanceEnum;
        this.numberOfMachines = FactorySetupVals.getNumberOfMachines(instanceEnum);
        var factorySize = FactorySetupVals.getSize(instanceEnum);
        this.x = factorySize[0];
        this.y = factorySize[1];
        gridSize = x * y;

        int[][] flow = ReadJson.getData(folderPath, instanceEnum, DataEnum.FLOW);
        int[][] cost = ReadJson.getData(folderPath, instanceEnum, DataEnum.COST);

        partialMultiplicationForIds = new int[numberOfMachines][numberOfMachines];
        for (int i = 0; i < flow.length; i++) {
            partialMultiplicationForIds[flow[i][0]][flow[i][1]] = flow[i][2] * cost[i][2];
            partialMultiplicationForIds[flow[i][1]][flow[i][0]] = flow[i][2] * cost[i][2];
        }
    }

    public int getX(int position) { return position % x; }
    public int getY(int position) { return position / x; }

    public int evaluateGrid(int[] grid) {
        int sum = 0;
        for (int position1 = 0; position1 < grid.length; position1++) {
            int id1 = grid[position1];
            if(id1 != -1) {
                int x1 = getX(position1), y1 = getY(position1);

                for (int position2 = position1 + 1; position2 < grid.length; position2++) {
                    int id2 = grid[position2];
                    if(id2 != -1) {
                        int x2 = getX(position2), y2 = getY(position2);
                        int D = Math.abs(x1 - x2) + Math.abs(y1 - y2);
                        sum += D * partialMultiplicationForIds[id1][id2];
                    }
                }
            }
        }
        return sum;
    }

    public int[] createInitGrid() {
        int[] flattenedGrid = new int[gridSize];
        for (int i = 0; i < numberOfMachines; i++) { flattenedGrid[i] = i; }
        for (int i = numberOfMachines; i < gridSize; i++) { flattenedGrid[i] = -1; }

        for (int i = gridSize-1; i > 0; i--) {
            int j = random.nextInt(i+1);
            int temp = flattenedGrid[i];
            flattenedGrid[i] = flattenedGrid[j];
            flattenedGrid[j] = temp;
        }
        return flattenedGrid;
    }

    public int[][] getRandomGeneration(int N) {
        int[][] generation = new int[N][];
        for (int i = 0; i < generation.length; i++) {
            generation[i] = createInitGrid();
        }
        return generation;
    }

    @Override
    public String toString() {
        StringBuilder weights = new StringBuilder();
        for (int[] partialMultiplicationForId : partialMultiplicationForIds) {
            weights.append(Arrays.toString(partialMultiplicationForId)).append("\n");
        }
        return "Factory " + instanceEnum + " {grid:" + x + "x" + y + ", machines:" + numberOfMachines +
                ",\n\trelations=\n" + weights + '}';
    }

    public void displayGrid(int[] grid) {
        System.out.print(" | ");
        for (int i = 0; i < grid.length; i++) {
            if (grid[i] == -1) {
                System.out.print("  ");
            }
            else {
                System.out.printf("%2d", grid[i]);
            }
            System.out.print(" | ");
            if((i + 1) % x == 0) {
                System.out.print("\n | ");
            }
        }
    }
}
