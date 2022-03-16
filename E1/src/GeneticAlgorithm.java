import zad1.Factory;
import zad1.InstanceEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GeneticAlgorithm {
    static Random random = new Random();
    static void genethic() throws Exception {
        String folderPath = "F:\\sztuczna_inteligencja\\flo_dane_v1.2";
        Factory factory = new Factory(InstanceEnum.HARD, folderPath);
        Selection selection = new Selection(factory);

        var initialGeneration = factory.getRandomGeneration(20);
        var best = factory.getBest(initialGeneration);

        while (factory.evaluateGrid(best) > 2000) {
            ArrayList<int[]> nextGeneration = new ArrayList<>();
            for (int i = 0; i < initialGeneration.length; i++) {
                var p1 = selection.selection(SelectionEnum.ROULETTE, initialGeneration, 5);
                var p2 = selection.selection(SelectionEnum.ROULETTE, initialGeneration, 5);
//                System.out.println("p1: " + Arrays.toString(p1));
//                System.out.println("p2: " + Arrays.toString(p2));

                int[] child;
                if (0.3 < random.nextDouble()) {
                    child = Crossover.partiallyMatchedCrossover(p1, p2,  random.nextInt(4), 5);
                } else {
                    child = factory.getBest(new int[][]{p1, p2}).clone();
                }

//                System.out.println("cross: "  + Arrays.toString(child));
//                if(Arrays.stream(child).sum() != 270) {
//                    throw new Exception();
//                }
                child = Mutation.mutate(child);
//                System.out.println("mut: " + Arrays.toString(child));

                nextGeneration.add(child);
//                if(Arrays.stream(child).sum() != 270) {
//                    throw new Exception();
//                }
                if (factory.evaluateGrid(child) < factory.evaluateGrid(best)) {
                    best = child;
                }
            }
            initialGeneration = nextGeneration.toArray(new int[][]{});
            System.out.println("Best: " + factory.evaluateGrid(best));
        }
        System.out.println(factory.evaluateGrid(best));
    }

    public static void main(String[] args) throws Exception {
        genethic();
        // easy: 4818
        // flat: 9800
        // heard:
    }
}
