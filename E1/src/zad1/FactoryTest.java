package zad1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.IntStream;

class FactoryTest {
    static String folderPath = "F:\\sztuczna_inteligencja\\flo_dane_v1.2";

    @Test
    void FactoryGetXYTest() {
        var hardF = new Factory(FactorySetupVals.HARD, folderPath); // 5x6

        Assertions.assertEquals(hardF.getX(13), 3);
        Assertions.assertEquals(hardF.getY(13), 2);

        Assertions.assertEquals(hardF.getX(7), 2);
        Assertions.assertEquals(hardF.getY(7), 1);

        Assertions.assertEquals(hardF.getX(9), 4);
        Assertions.assertEquals(hardF.getY(9), 1);

        Assertions.assertEquals(hardF.getX(4), 4);
        Assertions.assertEquals(hardF.getY(4), 0);

        Assertions.assertEquals(hardF.getX(15), 0);
        Assertions.assertEquals(hardF.getY(15), 3);
    }

    @Test
    void FactoryEvaluateMachinesTest() {
        var easyF = new Factory(FactorySetupVals.EASY, folderPath); // 3x3
        var initial = IntStream.range(0, 10).toArray();

        int calculate1 = easyF.evaluateTwoMachines(new Factory.ValuesBetweenTwoMachines(0, 1, 3, 4), initial);
        Assertions.assertEquals(calculate1, 1 * 3 * 4);

        int calculate2 = easyF.evaluateTwoMachines(new Factory.ValuesBetweenTwoMachines(0, 4, 3, 4), initial);
        Assertions.assertEquals(calculate2, 2 * 3 * 4);

        int calculate3 = easyF.evaluateTwoMachines(new Factory.ValuesBetweenTwoMachines(3, 8, 3, 4), initial);
        Assertions.assertEquals(calculate3, 3 * 3 * 4);

        int calculate4 = easyF.evaluateTwoMachines(new Factory.ValuesBetweenTwoMachines(0, 8, 3, 4), initial);
        Assertions.assertEquals(calculate4, 4 * 3 * 4);
    }

    @Test
    void EasyLoadingTest() {
        var factory = new Factory(FactorySetupVals.EASY, folderPath);
        var machinesVals = factory.getAllMachinesVals();
        var machine0 = machinesVals.get(0);
        var machine5 = machinesVals.get(5);
        var machineLast = machinesVals.get(machinesVals.size() - 1);

        Assertions.assertEquals(machine0.id1, 0);
        Assertions.assertEquals(machine0.id2, 1);
        Assertions.assertEquals(machine0.getFlow(), 100);
        Assertions.assertEquals(machine0.getCost(), 1);

        Assertions.assertEquals(machine5.id1, 2);
        Assertions.assertEquals(machine5.id2, 3);
        Assertions.assertEquals(machine5.getFlow(), 0);
        Assertions.assertEquals(machine5.getCost(), 5);

        Assertions.assertEquals(machineLast.id1, 7);
        Assertions.assertEquals(machineLast.id2, 8);
        Assertions.assertEquals(machineLast.getFlow(), 12);
        Assertions.assertEquals(machineLast.getCost(), 1);
    }

    @Test
    void FlatLoadingTest() {
        var factory = new Factory(FactorySetupVals.FLAT, folderPath);
        var machinesVals = factory.getAllMachinesVals();
        var machine0 = machinesVals.get(0);
        var machine5 = machinesVals.get(5);
        var machineLast = machinesVals.get(machinesVals.size() - 1);

        Assertions.assertEquals(machine0.id1, 0);
        Assertions.assertEquals(machine0.id2, 3);
        Assertions.assertEquals(machine0.getFlow(), 310);
        Assertions.assertEquals(machine0.getCost(), 1);

        Assertions.assertEquals(machine5.id1, 11);
        Assertions.assertEquals(machine5.id2, 9);
        Assertions.assertEquals(machine5.getFlow(), 120);
        Assertions.assertEquals(machine5.getCost(), 1);

        Assertions.assertEquals(machineLast.id1, 1);
        Assertions.assertEquals(machineLast.id2, 7);
        Assertions.assertEquals(machineLast.getFlow(), 90);
        Assertions.assertEquals(machineLast.getCost(), 1);
    }

    @Test
    void HardLoadingTest() {
        var factory = new Factory(FactorySetupVals.HARD, folderPath);
        var machinesVals = factory.getAllMachinesVals();
        var machine0 = machinesVals.get(0);
        var machine5 = machinesVals.get(5);
        var machineLast = machinesVals.get(machinesVals.size() - 1);

        Assertions.assertEquals(machine0.id1, 21);
        Assertions.assertEquals(machine0.id2, 0);
        Assertions.assertEquals(machine0.getFlow(), 440);
        Assertions.assertEquals(machine0.getCost(), 1);

        Assertions.assertEquals(machine5.id1, 13);
        Assertions.assertEquals(machine5.id2, 6);
        Assertions.assertEquals(machine5.getFlow(), 230);
        Assertions.assertEquals(machine5.getCost(), 1);

        Assertions.assertEquals(machineLast.id1, 12);
        Assertions.assertEquals(machineLast.id2, 18);
        Assertions.assertEquals(machineLast.getFlow(), 160);
        Assertions.assertEquals(machineLast.getCost(), 1);
    }

    @Test
    void FactoryEvaluatePopulationTest() {
        var easyF = new Factory(FactorySetupVals.EASY, folderPath);
        var initial = IntStream.range(0, 10).toArray();
        int calculate1 = easyF.evaluatePopulation(initial);
        Assertions.assertEquals(calculate1, 7664);

        var flatF = new Factory(FactorySetupVals.FLAT, folderPath);
        initial = IntStream.range(0, 12).toArray();
        calculate1 = flatF.evaluatePopulation(initial);
        Assertions.assertEquals(calculate1, 15865);

        var hardF = new Factory(FactorySetupVals.HARD, folderPath);
        initial = IntStream.range(0, 24).toArray();
        calculate1 = hardF.evaluatePopulation(initial);
        Assertions.assertEquals(calculate1, 38625);
    }

    @Test
    void InitPopulationTest() {
        var factory = new Factory(FactorySetupVals.HARD, folderPath);
        var initial = factory.createInitMachinesPositions();
        var sorted = Arrays.stream(initial).sorted();
        Assertions.assertTrue(sorted.toArray()[0] >= 0);
        sorted = Arrays.stream(initial).sorted();
        Assertions.assertTrue(sorted.toArray()[initial.length - 1] < factory.gridSize);

        var noDuplicates = new HashSet<Integer>();
        for (int i : initial) {
            noDuplicates.add(i);
        }

        Assertions.assertEquals(noDuplicates.size(), initial.length);
    }
}
