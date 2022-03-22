import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import zad1.Factory;
import zad1.InstanceEnum;
import zad2.Selection;
import zad2.SelectionEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;


public class TestFactoryTest {
    static String folderPath = "F:\\sztuczna_inteligencja\\flo_dane_v1.2";
    static Factory factory;
    static Selection selection;

    static int[] evaluations = new int[]{7, 5, 5, 9, 11};
    static ArrayList<int[]> grids = new ArrayList<>() {
        {
            add(new int[]{0, 1, 2, 3, 4, 5});
            add(new int[]{0, 1, 2, 5, 4, 3});
            add(new int[]{0, 3, 4, 1, 2, 5});
            add(new int[]{0, 4, 2, 5, 1, 3});
            add(new int[]{0, 5, 3, 2, 4, 1});
        }
    };

    @BeforeAll
    public static void beforeAll() {
        factory = new Factory(InstanceEnum.TEST, folderPath); // 3x2
        selection = new Selection(factory);
    }

    @Test
    public void evaluateGrid() {
        for (int i = 0; i < evaluations.length; i++) {
            Assertions.assertEquals(evaluations[i], factory.evaluateGrid(grids.get(i)));
        }
    }

    @Test
    public void evaluateSelection() {
        Assertions.assertArrayEquals(selection.getEvaluationForEach(grids), evaluations);
//        System.out.println(Arrays.toString(selection.getAllDistribution(grids)));
        System.out.println(selection.getDistributionTreeMap(grids));
    }

    @Test
    public void testCompareSelections() {
        ArrayList<Integer> selectedTournament = new ArrayList<>();
        ArrayList<Integer> selectedRoulette = new ArrayList<>();
        int testSize = 100000;

        for (int i = 0; i < testSize; i++) {
            selectedTournament.add(factory.evaluateGrid(selection.selection(SelectionEnum.TOURNAMENT, grids, 1)));
            selectedRoulette.add(factory.evaluateGrid(selection.selection(SelectionEnum.ROULETTE, grids, 1)));
        }
//        selectedTournament.sort(Integer::compareTo);
//        selectedRoulette.sort(Integer::compareTo);
        HashMap<Integer, Integer> mapTournament = new HashMap<>(){
            {
                for (var eval : evaluations) {
                    put(eval, 0);
                }
            }
        };
        HashMap<Integer, Integer> mapRoulette = new HashMap<>(){
            {
                for (var eval : evaluations) {
                    put(eval, 0);
                }
            }
        };
        for (var value : selectedTournament) {
            mapTournament.put(value, mapTournament.get(value) + 1);
        }
        for (var value : selectedRoulette) {
            mapRoulette.put(value, mapRoulette.get(value) + 1);
        }
        mapTournament.put(5, mapTournament.get(5)/2);
        mapRoulette.put(5, mapRoulette.get(5)/2);
//
//
//        for (var value : selectedTournament) {
//            mapTournament.put(value, mapTournament.get(value) / testSize);
//        }
//        for (var value : selectedRoulette) {
//            mapRoulette.put(value, mapRoulette.get(value) / testSize);
//        }

        System.out.println(mapTournament);
        System.out.println(mapRoulette);

        Assertions.assertTrue((float) Math.abs(mapTournament.get(5) - mapTournament.get(7))/testSize < 0.01);
        Assertions.assertTrue((float) Math.abs(mapTournament.get(5) - mapTournament.get(9))/testSize < 0.01);
        Assertions.assertTrue((float) Math.abs(mapTournament.get(5) - mapTournament.get(11))/testSize < 0.01);

        Assertions.assertTrue(mapRoulette.get(5) > mapRoulette.get(7));
        Assertions.assertTrue(mapRoulette.get(7) > mapRoulette.get(9));
        Assertions.assertTrue(mapRoulette.get(9) > mapRoulette.get(11));
    }
}
