import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ReadJsonTest {
    static String folderPath = "F:\\sztuczna_inteligencja\\flo_dane_v1.2";

    @Test
    void easyTest() {
        int[][] flow = ReadJson.getData(folderPath, FactorySetupVals.EASY, FactorySetupVals.flow);
        int[][] cost = ReadJson.getData(folderPath, FactorySetupVals.EASY, FactorySetupVals.cost);

        Assertions.assertArrayEquals(flow[0], new int[]{0, 1, 100});
        Assertions.assertArrayEquals(flow[5], new int[]{2, 3, 0});
        Assertions.assertArrayEquals(flow[flow.length - 1], new int[]{7, 8, 12});

        Assertions.assertArrayEquals(cost[0], new int[]{0, 1, 1});
        Assertions.assertArrayEquals(cost[5], new int[]{2, 3, 5});
        Assertions.assertArrayEquals(cost[cost.length - 1], new int[]{7, 8, 1});
    }

    @Test
    void flatTest() {
        int[][] flow = ReadJson.getData(folderPath, FactorySetupVals.FLAT, FactorySetupVals.flow);
        int[][] cost = ReadJson.getData(folderPath, FactorySetupVals.FLAT, FactorySetupVals.cost);

        Assertions.assertArrayEquals(flow[0], new int[]{0, 3, 310});
        Assertions.assertArrayEquals(flow[5], new int[]{11, 9, 120});
        Assertions.assertArrayEquals(flow[flow.length - 1], new int[]{1, 7, 90});

        Assertions.assertArrayEquals(cost[0], new int[]{0, 3, 1});
        Assertions.assertArrayEquals(cost[5], new int[]{11, 9, 1});
        Assertions.assertArrayEquals(cost[cost.length - 1], new int[]{1, 7, 1});
    }

    @Test
    void hardTest() {
        int[][] flow = ReadJson.getData(folderPath, FactorySetupVals.HARD, FactorySetupVals.flow);
        int[][] cost = ReadJson.getData(folderPath, FactorySetupVals.HARD, FactorySetupVals.cost);

        Assertions.assertArrayEquals(flow[0], new int[]{21, 0, 440});
        Assertions.assertArrayEquals(flow[5], new int[]{13, 6, 230});
        Assertions.assertArrayEquals(flow[flow.length - 1], new int[]{12, 18, 160});

        Assertions.assertArrayEquals(cost[0], new int[]{21, 0, 1});
        Assertions.assertArrayEquals(cost[5], new int[]{13, 6, 1});
        Assertions.assertArrayEquals(cost[cost.length - 1], new int[]{12, 18, 1});
    }
}
