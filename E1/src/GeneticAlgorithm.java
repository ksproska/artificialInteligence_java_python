import zad1.Factory;
import zad1.InstanceEnum;
import zad2.Crossover;
import zad2.Mutation;
import zad2.Selection;
import zad2.SelectionEnum;

import java.util.ArrayList;
import java.util.Random;

public class GeneticAlgorithm {
    static Random random = new Random();
    static void geneticAlgorithm() throws Exception {
        String folderPath = "F:\\sztuczna_inteligencja\\flo_dane_v1.2";
        Factory factory = new Factory(InstanceEnum.HARD, folderPath);
        Selection selection = new Selection(factory);
        SelectionEnum selectionEnum = SelectionEnum.TOURNAMENT;

        var initialGeneration = factory.getRandomGeneration(20);
        var best = factory.getBest(initialGeneration);

        while (true) {
            ArrayList<int[]> nextGeneration = new ArrayList<>();
            for (int i = 0; i < initialGeneration.size(); i++) {
                var p1 = selection.selection(selectionEnum, initialGeneration, 5);
                var p2 = selection.selection(selectionEnum, initialGeneration, 5);

                int[] child;
                if (0.3 < random.nextDouble()) {
                    child = Crossover.partiallyMatchedCrossover(p1, p2,  random.nextInt(4), 5);
                } else {
                    child = factory.getBest(new ArrayList<>(){
                        {
                            add(p1);
                            add(p2);
                        }
                    }).clone();
                }
                child = Mutation.mutate(child);

                nextGeneration.add(child);
                if (factory.evaluateGrid(child) < factory.evaluateGrid(best)) {
                    best = child;
                    System.out.println("Best: " + factory.evaluateGrid(best));
                }
            }
            initialGeneration = nextGeneration;
        }
    }

    public static void main(String[] args) throws Exception {
        geneticAlgorithm();
        // easy: 4818
        // flat: 9800
        // heard: 11329
    }
}
