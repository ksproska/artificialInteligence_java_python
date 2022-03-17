import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import zad1.Factory;
import zad1.InstanceEnum;
import zad2.Selection;


public class TestFactoryTest {
    static String folderPath = "F:\\sztuczna_inteligencja\\flo_dane_v1.2";
    static Factory factory;
    static Selection selection;

    static int[] evaluations = new int[]{7, 5, 5, 9, 11};
    static int[][] grids = new int[][] {
            new int[]{0, 1, 2, 3, 4, 5},
            new int[]{0, 1, 2, 5, 4, 3},
            new int[]{0, 3, 4, 1, 2, 5},
            new int[]{0, 4, 2, 5, 1, 3},
            new int[]{0, 5, 3, 2, 4, 1}
    };

    @BeforeAll
    public static void beforeAll() {
        factory = new Factory(InstanceEnum.TEST, folderPath); // 3x2
        selection = new Selection(factory);
    }

    @Test
    public void evaluateGrid() {
        for (int i = 0; i < evaluations.length; i++) {
            Assertions.assertEquals(evaluations[i], factory.evaluateGrid(grids[i]));
        }
    }

    @Test
    public void evaluateSelection() {
        Assertions.assertArrayEquals(selection.getEvaluationForEach(grids), evaluations);
//        System.out.println(Arrays.toString(selection.getAllDistribution(grids)));
        System.out.println(selection.getDistributionTreeMap(grids));
    }
}
