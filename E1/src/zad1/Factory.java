package zad1;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;


public class Factory {
    private static Random random = new Random();
    public final int x, y;
    public final int gridSize;
    public final int numberOfMachines;
    public final InstanceEnum instanceEnum;
    private final ArrayList<ValuesBetweenTwoMachines> betweenMachinesVals = new ArrayList<>();
    private final int[][] partialMultiplicationForIds;

    public Factory(InstanceEnum instanceEnum, String folderPath) {
        this.instanceEnum = instanceEnum;
        this.numberOfMachines = FactorySetupVals.getNumberOfMachines(instanceEnum);
        var factorySize = FactorySetupVals.getSize(instanceEnum);
        this.x = factorySize[0];
        this.y = factorySize[1];
        gridSize = x * y;
        this.setMachinesValues(folderPath);
        partialMultiplicationForIds = new int[numberOfMachines][numberOfMachines];
        for (var vals : this.betweenMachinesVals) {
            partialMultiplicationForIds[vals.id1][vals.id2] = vals.getCostTimesFlow();
            partialMultiplicationForIds[vals.id2][vals.id1] = vals.getCostTimesFlow();
        }
    }

    private ValuesBetweenTwoMachines find(int id1, int id2) {
        var foundVal = this.betweenMachinesVals.stream()
                .filter(elem -> elem.id1 == id1 && elem.id2 == id2 || elem.id1 == id2 && elem.id2 == id1)
                .findFirst();
        return foundVal.orElse(null);
    }

    private void setMachinesValues(String folderPath) {
        int[][] flow = ReadJson.getData(folderPath, instanceEnum, DataEnum.FLOW);
        int[][] cost = ReadJson.getData(folderPath, instanceEnum, DataEnum.COST);
        for (int[] ints : flow) {
            var id1 = ints[0];
            var id2 = ints[1];
            var machinesFlow = ints[2];
            this.betweenMachinesVals.add(new ValuesBetweenTwoMachines(id1, id2, machinesFlow, 0));
        }
        for (int[] ints : cost) {
            var id1 = ints[0];
            var id2 = ints[1];
            var machinesCost = ints[2];
            this.find(id1, id2).setCost(machinesCost);
        }
    }

    public static <T> T[] shuffle(T[] array) {
        for (int i = array.length-1; i > 0; i--) {
            int j = random.nextInt(i+1);
            T temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
        return array;
    }

    public int[] createInitGrid() {
        // index = machine id
        // value = position of the machine in flattenedGrid, (flattenedGrid as a flattened matrix)

        int[] flattenedGrid = new int[gridSize];
//        Arrays.fill(flattenedGrid, -1);
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

    @Override
    public String toString() {
        return "Factory " + instanceEnum + " {grid:" + x + "x" + y + ", machines:" + numberOfMachines +
                ",\n\trelations=" + betweenMachinesVals + '}';
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

    public static class ValuesBetweenTwoMachines {
        public final int id1, id2;
        private int flow, cost;
        private int costTimesFlow;

        public ValuesBetweenTwoMachines(int id1, int id2, int flow, int cost) {
            this.id1 = id1;
            this.id2 = id2;
            this.flow = flow;
            this.cost = cost;
            costTimesFlow = cost * flow;
        }

        public void setCost(int cost) {
            this.cost = cost;
            costTimesFlow = cost * flow;
        }

        public void setFlow(int flow) {
            this.flow = flow;
            costTimesFlow = cost * flow;
        }

        public int getCost() {
            return cost;
        }

        public int getFlow() {
            return flow;
        }

        @Override
        public String toString() {
            return "<" + id1 + "," + id2 + "> => D*" + flow + "*" + cost + "";
        }

        public int getCostTimesFlow() {
            return costTimesFlow;
        }
    }
}
