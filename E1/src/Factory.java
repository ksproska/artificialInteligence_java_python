import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class Factory {
    public final int x, y;
    public final int gridSize;
    public final int numberOfMachines;
    public final String instanceType;
    private final ArrayList<ValuesBetweenMachines> betweenMachinesVals = new ArrayList<>();

    public Factory(String instanceType, String folderPath) {
        this.instanceType = instanceType;
        var factorySize = FactoryValues.sizes.get(instanceType);
        this.x = factorySize[0];
        this.y = factorySize[1];
        gridSize = x * y;
        this.numberOfMachines = FactoryValues.numbOfMachines.get(instanceType);
        this.setMachinesValues(folderPath);
    }

    public int getX(int position) { return position % x; }
    public int getY(int position) { return position / x; }

    private void setMachinesValues(String folderPath) {
        int[][] flow = ReadJson.getData(folderPath, instanceType, FactoryValues.flow);
        int[][] cost = ReadJson.getData(folderPath, instanceType, FactoryValues.cost);
        for (int[] ints : flow) {
            var id1 = ints[0];
            var id2 = ints[1];
            var machinesFlow = ints[2];
            this.betweenMachinesVals.add(new ValuesBetweenMachines(id1, id2, machinesFlow, 0));
        }
        for (int[] ints : cost) {
            var id1 = ints[0];
            var id2 = ints[1];
            var machinesCost = ints[2];
            this.find(id1, id2).setCost(machinesCost);
        }
    }

    public int[] createInitMachinesPositions() {
        // index = machine number
        // value = position in grid, (grid as a flattened matrix)
        int[] array = IntStream.range(0, gridSize).toArray();
        Random rd = new Random();
        for (int i = gridSize-1; i > 0; i--) {
            int j = rd.nextInt(i+1);
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
        return Arrays.copyOfRange(array, 0, numberOfMachines);
    }

    private ValuesBetweenMachines find(int id1, int id2) {
        var foundVal = this.betweenMachinesVals.stream()
                .filter(elem -> elem.id1 == id1 && elem.id2 == id2 || elem.id1 == id2 && elem.id2 == id1)
                .findFirst();
        return foundVal.orElse(null);
    }

    public int evaluateMachines(ValuesBetweenMachines valuesBetweenMachines, int[] population) {
        int position1 = population[valuesBetweenMachines.id1];
        int position2 = population[valuesBetweenMachines.id2];
        int x1 = getX(position1), y1 = getY(position1);
        int x2 = getX(position2), y2 = getY(position2);
        int D = Math.abs(x1 - x2) + Math.abs(y1 - y2);
        return D * valuesBetweenMachines.getCost() * valuesBetweenMachines.getFlow();
    }

    public Stream<Integer> getEvaluationForEachMachine(int[] population) {
        return betweenMachinesVals.stream().map(elem -> evaluateMachines(elem, population));
    }

    public int evaluatePopulation(int[] population) {
        return getEvaluationForEachMachine(population).reduce(0, Integer::sum);
    }

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


