import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class Factory {
    public final int x, y;
    public final int gridSize;
    public final int numberOfMachines;
    public final String instanceType;
    private final ArrayList<ValuesBetweenTwoMachines> betweenMachinesVals = new ArrayList<>();

    public Factory(String instanceType, String folderPath) {
        this.instanceType = instanceType;
        this.numberOfMachines = FactoryValues.numbOfMachines.get(instanceType);
        var factorySize = FactoryValues.sizes.get(instanceType);
        this.x = factorySize[0];
        this.y = factorySize[1];
        gridSize = x * y;
        this.setMachinesValues(folderPath);
    }

    private ValuesBetweenTwoMachines find(int id1, int id2) {
        var foundVal = this.betweenMachinesVals.stream()
                .filter(elem -> elem.id1 == id1 && elem.id2 == id2 || elem.id1 == id2 && elem.id2 == id1)
                .findFirst();
        return foundVal.orElse(null);
    }

    private void setMachinesValues(String folderPath) {
        int[][] flow = ReadJson.getData(folderPath, instanceType, FactoryValues.flow);
        int[][] cost = ReadJson.getData(folderPath, instanceType, FactoryValues.cost);
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

    // -----------------------------------------------------------------------------------------------------------------

    public int[] createInitMachinesPositions() {
        // index = machine id
        // value = position of the machine in grid, (grid as a flattened matrix)

        int[] grid = IntStream.range(0, gridSize).toArray();
        Random rd = new Random();
        for (int i = gridSize-1; i > 0; i--) {
            int j = rd.nextInt(i+1);
            int temp = grid[i];
            grid[i] = grid[j];
            grid[j] = temp;
        }
        return Arrays.copyOfRange(grid, 0, numberOfMachines);
    }

    // -----------------------------------------------------------------------------------------------------------------

    public int getX(int position) { return position % x; }
    public int getY(int position) { return position / x; }

    public int evaluateTwoMachines(ValuesBetweenTwoMachines valuesBetweenTwoMachines, int[] machinesPositions) {
        int position1 = machinesPositions[valuesBetweenTwoMachines.id1];
        int position2 = machinesPositions[valuesBetweenTwoMachines.id2];
        int x1 = getX(position1), y1 = getY(position1);
        int x2 = getX(position2), y2 = getY(position2);
        int D = Math.abs(x1 - x2) + Math.abs(y1 - y2);
        return D * valuesBetweenTwoMachines.getCost() * valuesBetweenTwoMachines.getFlow();
    }

    private Stream<Integer> getEvaluationForEachMachine(int[] machinesPositions) {
        return betweenMachinesVals.stream().map(elem -> evaluateTwoMachines(elem, machinesPositions));
    }

    public int evaluatePopulation(int[] machinesPositions) {
        return getEvaluationForEachMachine(machinesPositions).reduce(0, Integer::sum);
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Factory {grid:" + x + "x" + y + ", machines:" + numberOfMachines +
                ", relations=" + betweenMachinesVals + '}';
    }

    public void displayPopulation(int[] population) {
        Integer[][] grid = new Integer[x][y];
        for (int i = 0; i < population.length; i++) {
            var position = population[i];
            grid[getX(position)][getY(position)] = i;
        }

        for (var row: grid) {
            System.out.print("\n| ");
            for (var element : row) {
                if (element != null) { System.out.printf("%2d", element); }
                else { System.out.print("  "); }
                System.out.print(" | ");
            }
        }
    }
}