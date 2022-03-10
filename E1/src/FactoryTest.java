import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

class FactoryTest {
    static String folderPath = "F:\\sztuczna_inteligencja\\flo_dane_v1.2";

    @Test
    void FactoryGetXYTest() {
        var hardF = new Factory(FactoryValues.HARD, folderPath); // 5x6

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
        var easyF = new Factory(FactoryValues.EASY, folderPath); // 3x3
        var initial = IntStream.range(0, 10).toArray();

        int calculate1 = easyF.evaluateTwoMachines(new ValuesBetweenTwoMachines(0, 1, 3, 4), initial);
        Assertions.assertEquals(calculate1, 1 * 3 * 4);

        int calculate2 = easyF.evaluateTwoMachines(new ValuesBetweenTwoMachines(0, 4, 3, 4), initial);
        Assertions.assertEquals(calculate2, 2 * 3 * 4);

        int calculate3 = easyF.evaluateTwoMachines(new ValuesBetweenTwoMachines(3, 8, 3, 4), initial);
        Assertions.assertEquals(calculate3, 3 * 3 * 4);

        int calculate4 = easyF.evaluateTwoMachines(new ValuesBetweenTwoMachines(0, 8, 3, 4), initial);
        Assertions.assertEquals(calculate4, 4 * 3 * 4);
    }

    @Test
    void FactoryEvaluatePopulationTest() {
        var easyF = new Factory(FactoryValues.EASY, folderPath);
        var initial = IntStream.range(0, 10).toArray();
        int calculate1 = easyF.evaluatePopulation(initial);
        Assertions.assertEquals(calculate1, 7664);

        var flatF = new Factory(FactoryValues.FLAT, folderPath);
        initial = IntStream.range(0, 12).toArray();
        calculate1 = flatF.evaluatePopulation(initial);
        Assertions.assertEquals(calculate1, 15865);

        var hardF = new Factory(FactoryValues.HARD, folderPath);
        initial = IntStream.range(0, 24).toArray();
        calculate1 = hardF.evaluatePopulation(initial);
        Assertions.assertEquals(calculate1, 38625);
    }
}
