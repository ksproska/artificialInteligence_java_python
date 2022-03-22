package zad3;

import zad1.Factory;
import zad1.InstanceEnum;
import zad2.Crossover;
import zad2.Mutation;
import zad2.Selection;
import zad2.SelectionEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ExperimentData {
    static Random random = new Random();
    private Factory factory;
    private Selection selection;
    private SelectionEnum selectionEnum;
    private int[] overallBest;
    private int runningGenerationNumber = 1;
    private ArrayList<int[]> currentGeneration;
    private int generationSize;

    public ExperimentData(InstanceEnum instanceEnum, String folderPath, SelectionEnum selectionEnum, int generationSize) {
        factory = new Factory(instanceEnum, folderPath);
        selection = new Selection(factory);
        this.selectionEnum = selectionEnum;
        this.generationSize = generationSize;
        currentGeneration = factory.getRandomGeneration(generationSize);
        overallBest = factory.getBest(currentGeneration);
    }

    public void runNextGeneration() {
        ArrayList<int[]> nextGeneration = new ArrayList<>();
        for (int i = 0; i < generationSize; i++) {
            var p1 = selection.selection(selectionEnum, currentGeneration, 5);
            var p2 = selection.selection(selectionEnum, currentGeneration, 5);

            int[] child;
            if (0.3 < random.nextDouble()) {
                child = Crossover.partiallyMatchedCrossover(p1, p2,  random.nextInt(4), 5);
            } else {
                child = factory.getBest(new ArrayList<>() {
                    {
                        add(p1);
                        add(p2);
                    }
                }).clone();
            }
            Mutation.mutate(child);

            nextGeneration.add(child);
            if (factory.evaluateGrid(child) < factory.evaluateGrid(overallBest)) {
                overallBest = child;
            }
        }
        currentGeneration = nextGeneration;
        runningGenerationNumber += 1;
    }

    public String getSnapshot() {
        String all = "";
        all += runningGenerationNumber + "\t";
        all += factory.evaluateGrid(overallBest) + "\t";
        all += factory.evaluateGrid(factory.getBest(currentGeneration)) + "\t";
        all += factory.evaluateGrid(factory.getWorst(currentGeneration)) + "\t";
        return all;
    }

    public static void main(String[] args) {
        String folderPath = "F:\\sztuczna_inteligencja\\flo_dane_v1.2";
        var experiment = new ExperimentData(InstanceEnum.HARD, folderPath, SelectionEnum.TOURNAMENT, 20);
        for (int i = 0; i < 1000; i++) {
            System.out.println(experiment.getSnapshot());
            experiment.runNextGeneration();
        }
    }
}
