import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import zad1.Factory;
import zad1.InstanceEnum;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.TreeMap;

public class Selection {
    private static final Random random = new Random();
    private final Factory factory;

    public Selection(Factory factory) {
        this.factory = factory;
    }

    private TreeMap<Float, Integer> getDistributionTreeMap(int[][] generation) {
        var treeMap = new TreeMap<Float, Integer>();
        int[] evaluationForEach = getEvaluationForEach(generation);
        int sumAll = Arrays.stream(evaluationForEach).sum();
        float[] distribution = new float[generation.length];
        distribution[0] = (float) evaluationForEach[0] / sumAll;
        treeMap.put(distribution[0], 0);
        for (int i = 0; i < distribution.length - 1; i++) {
            distribution[i + 1] = ((float) evaluationForEach[i + 1] / sumAll) + distribution[i];
            treeMap.put(distribution[i + 1], i + 1);
        }
        return treeMap;
    }

    private int[] getEvaluationForEach(int[][] generation) {
        int[] evaluationForEach = new int[generation.length];
        for (int i = 0; i < generation.length; i++) {
            evaluationForEach[i] = factory.evaluateGrid(generation[i]);
        }
        return evaluationForEach;
    }

    private float[] getAllDistribution(int[][] generation) {
        int[] evaluationForEach = getEvaluationForEach(generation);
        int sumAll = Arrays.stream(evaluationForEach).sum();
        float[] distribution = new float[generation.length];
        distribution[0] = (float) evaluationForEach[0] / sumAll;
        for (int i = 0; i < distribution.length - 1; i++) {
            distribution[i + 1] = ((float) evaluationForEach[i + 1] / sumAll) + distribution[i];
        }
        return distribution;
    }

    private static HashSet<Integer> getRandomIntsDistribution(float[] probabilities, int numberOfChosenInts) {
        HashSet<Integer> selectedInts = new HashSet<>();
        while (numberOfChosenInts > selectedInts.size()) {
            var randFloat = random.nextFloat();
            int chosenIndex = probabilities.length - 1;
            while (chosenIndex > 0 && randFloat < probabilities[chosenIndex]) {
                chosenIndex ++;
            }
            selectedInts.add(chosenIndex + 1);
        }
        return selectedInts;
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
//        float[] distribution = getAllDistribution(generation);
//        HashSet<Integer> chosenInts =  getRandomIntsDistribution(distribution, N);
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
        throw new IllegalArgumentException("SelectionEnum: " + selectionEnum + " is incorrect.");
    }
}

class SelectionTest {
    static String folderPath = "F:\\sztuczna_inteligencja\\flo_dane_v1.2";
    static Factory factory;
    static Selection selection;
    static int[][] generation;
    @BeforeAll
    public static void beforeAll() {
        factory = new Factory(InstanceEnum.TEST, folderPath); // 5x6
        selection = new Selection(factory);
        generation = factory.getRandomGeneration(1000);
    }

    @Test
    public void testTournament() {
        for (var N : new int[]{10, 50, 100, 500, 1000}){
            var selected = selection.selection(SelectionEnum.TOURNAMENT, generation, N);
            System.out.println(factory.evaluateGrid(selected));
            System.out.println(Arrays.toString(selected));
        }
    }

    @Test
    public void testRoulette() {
        for (var N : new int[]{10, 50, 100, 500, 1000}){
            var selected = selection.selection(SelectionEnum.ROULETTE, generation, N);
            System.out.println(factory.evaluateGrid(selected));
            System.out.println(Arrays.toString(selected));
        }
    }
}
