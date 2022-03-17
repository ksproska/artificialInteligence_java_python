package zad2;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import zad1.Factory;
import zad1.InstanceEnum;

import java.util.*;

public class Selection {
    private static final Random random = new Random();
    private final Factory factory;

    public Selection(Factory factory) {
        this.factory = factory;
    }

    public double[] reverseEvaluation(int[][] generation) {
        double[] evaluationForEach = Arrays.stream(getEvaluationForEach(generation)).asDoubleStream().toArray();
        var maxVal = Arrays.stream(evaluationForEach).max().getAsDouble();
        for (int i = 0; i < evaluationForEach.length; i++) {
            evaluationForEach[i] = (-evaluationForEach[i]) + maxVal + 1;
        }
        return evaluationForEach;
    }

    public TreeMap<Float, Integer> getDistributionTreeMap(int[][] generation) {
        var treeMap = new TreeMap<Float, Integer>();
        var evaluationForEach = reverseEvaluation(generation);

        double sumAll = Arrays.stream(evaluationForEach).sum();
        double[] distribution = new double[generation.length];
        distribution[0] = evaluationForEach[0] / sumAll;
        treeMap.put((float) distribution[0], 0);
        for (int i = 0; i < distribution.length - 1; i++) {
            distribution[i + 1] = ((float) evaluationForEach[i + 1] / sumAll) + distribution[i];
            treeMap.put((float) distribution[i + 1], i + 1);
        }
        return treeMap;
    }

    public int[] getEvaluationForEach(int[][] generation) {
        int[] evaluationForEach = new int[generation.length];
        for (int i = 0; i < generation.length; i++) {
            evaluationForEach[i] = factory.evaluateGrid(generation[i]);
        }
        return evaluationForEach;
    }

    private static HashSet<Integer> getRandomIntsDistributionTreeMap(TreeMap<Float, Integer> treeMap, int numberOfChosenInts) {
        HashSet<Integer> selectedInts = new HashSet<>();
        while (numberOfChosenInts > selectedInts.size()) {
            var randFloat = random.nextFloat();
            float chosenKey = treeMap.tailMap(randFloat).firstKey();
            selectedInts.add(treeMap.get(chosenKey));
        }
        return selectedInts;
    }

    private static HashSet<Integer> getRandomInts(int maxIntRange, int numberOfChosenInts) {
        HashSet<Integer> selectedInts = new HashSet<>();
        while (numberOfChosenInts > selectedInts.size()) {
            selectedInts.add(random.nextInt(maxIntRange));
        }
        return selectedInts;
    }

    private int[] getBestForIndexes(int[][] generation, HashSet<Integer> chosenIndexes) {
        int[] selected = null;
        int selectedEval = -1;
        for (var value : chosenIndexes) {
            if (selected == null) {
                selected = generation[value];
                selectedEval = factory.evaluateGrid(selected);
            }
            else {
                var newSelected = generation[value];
                var newSelectedEval = factory.evaluateGrid(newSelected);
                if(newSelectedEval < selectedEval) {
                    selected = newSelected;
                    selectedEval = newSelectedEval;
                }
            }
        }
        return selected;
    }

    private int[] selectionTournament(int[][] generation, int N) {
        var chosenInts = getRandomInts(generation.length, N);
        return getBestForIndexes(generation, chosenInts);
    }

    private int[] selectionRoulette(int[][] generation, int N) {
        var distributionTreeMap = getDistributionTreeMap(generation);
        var chosenInts = getRandomIntsDistributionTreeMap(distributionTreeMap, N);
        return getBestForIndexes(generation, chosenInts);
    }

    public int[] selection(SelectionEnum selectionEnum, int[][] generation, int N) {
        if(selectionEnum == SelectionEnum.ROULETTE) {
            return selectionRoulette(generation, N);
        }
        if(selectionEnum == SelectionEnum.TOURNAMENT) {
            return selectionTournament(generation, N);
        }
        throw new IllegalArgumentException("zad2.SelectionEnum: " + selectionEnum + " is incorrect.");
    }
}

//class SelectionTest {
//    static String folderPath = "F:\\sztuczna_inteligencja\\flo_dane_v1.2";
//    static Factory factory;
//    static Selection selection;
//    static int[][] generation;
//    @BeforeAll
//    public static void beforeAll() {
//        factory = new Factory(InstanceEnum.HARD, folderPath); // 5x6
//        selection = new Selection(factory);
//        generation = factory.getRandomGeneration(1000);
//    }
//
//    @Test
//    public void testTournament() {
//        for (var N : new int[]{10, 50, 100, 500, 1000}){
//            var selected = selection.selection(SelectionEnum.TOURNAMENT, generation, N);
//            System.out.println(factory.evaluateGrid(selected));
////            System.out.println(Arrays.toString(selected));
//        }
//    }
//
//    @Test
//    public void testRoulette() {
//        for (var N : new int[]{10, 50, 100, 500, 1000}){
//            var selected = selection.selection(SelectionEnum.ROULETTE, generation, N);
//            System.out.println(factory.evaluateGrid(selected));
////            System.out.println(Arrays.toString(selected));
//        }
//    }
//}
