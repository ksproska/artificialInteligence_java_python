package zad3;

import zad1.Factory;
import zad1.InstanceEnum;
import zad2.Crossover;
import zad2.Mutation;
import zad2.Selection;
import zad2.SelectionEnum;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class ExperimentData {
    static Random random = new Random();
    private final Factory factory;
    private final Selection selection;
    private final SelectionEnum selectionEnum;
    private int[] overallBest;
    private int runningGenerationNumber = 1;
    private ArrayList<int[]> currentGeneration;
    private final int generationSize;
    private final int tournamentSize;
    private final double crossoverProbability;
    private final double mutationProbability;
    public final int numberOfGenerationsAim;
    public final int lenToMutate;
    public final char fileSeparator = ',';

    public ExperimentData(InstanceEnum instanceEnum, String folderPath, SelectionEnum selectionEnum, int generationSize, int tournamentSize, double crossoverProbability, double mutationProbability, int numberOfGenerationsAim, int lenToMutate) {
        this.numberOfGenerationsAim = numberOfGenerationsAim;
        factory = new Factory(instanceEnum, folderPath);
        selection = new Selection(factory);
        this.selectionEnum = selectionEnum;
        this.generationSize = generationSize;
        currentGeneration = factory.getRandomGeneration(generationSize);
        overallBest = factory.getBest(currentGeneration);
        this.tournamentSize = tournamentSize;
        this.crossoverProbability = crossoverProbability;
        this.mutationProbability = mutationProbability;
        this.lenToMutate = lenToMutate;
        if (tournamentSize > generationSize) {
            throw new IllegalArgumentException("Tournament size to big.");
        }
        if (lenToMutate > factory.gridSize) {
            throw new IllegalArgumentException("Mutated length to big.");
        }
    }

    @Override
    public String toString() {
        var toStr = "data type\t" + factory.instanceEnum + " (" + factory.gridSize + ")" + "\t\t" +
                "\ncrossover probability\t" + crossoverProbability + "\t\t" +
                "\nmutation probability\t" + mutationProbability + "\t\t" +
                "\nmutated length\t" + lenToMutate + '\t' + Math.round((100 * (double)lenToMutate/factory.gridSize)) + '%' + "\t" +
                "\ngeneration size\t" + generationSize + "\t\t" +
                "\nnumber of generations\t" + numberOfGenerationsAim + "\t\t" +
                "\nselection type\t" + selectionEnum + "\t\t";
        if(selectionEnum == SelectionEnum.TOURNAMENT) {
            toStr += "\ntournament size\t" + tournamentSize + '\t' + Math.round((100 * (double)tournamentSize/generationSize)) + '%' + "\t";
        }
        else {
            toStr += "\n\t\t\t\t";
        }
        toStr += "\n\t\t\t\t";
        return toStr.replace('\t', fileSeparator);
    }

    public String getFileName() {
        var toStr = factory.instanceEnum.toString().charAt(0) + "" + factory.gridSize
                + "_" + crossoverProbability
                + "_" + mutationProbability
                + "_" + lenToMutate
                + "_" + generationSize
                + "_" + numberOfGenerationsAim
                + "_" + selectionEnum.toString().charAt(0);
        if(selectionEnum == SelectionEnum.TOURNAMENT) {
            toStr += tournamentSize;
        }
        return toStr;
    }

    public void runNextGeneration() {
        ArrayList<int[]> nextGeneration = new ArrayList<>();
        for (int i = 0; i < generationSize; i++) {
            var p1 = selection.selection(selectionEnum, currentGeneration, tournamentSize);
            var p2 = selection.selection(selectionEnum, currentGeneration, tournamentSize);

            int[] child;
            if (random.nextDouble() < crossoverProbability) {
                child = Crossover.partiallyMatchedCrossover(p1, p2,  random.nextInt(4), 5);
            } else {
                child = factory.getBest(new ArrayList<>() {
                    {
                        add(p1);
                        add(p2);
                    }
                }).clone();
            }
            if(random.nextDouble() < mutationProbability) {
                Mutation.mutate(child);
            }

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
        return all.replace('\t', fileSeparator);
    }

    public static void main(String[] args) throws IOException {
        String folderPath = "F:\\sztuczna_inteligencja\\flo_dane_v1.2";
        InstanceEnum[] instanceEnums = new InstanceEnum[] {InstanceEnum.EASY, InstanceEnum.FLAT, InstanceEnum.HARD};
        SelectionEnum[] selectionEnums = new SelectionEnum[] {SelectionEnum.ROULETTE, SelectionEnum.TOURNAMENT};
        int[] generationSizes = new int[] {20, 100};
        int[] numberOfGenerations = new int[] {50, 100};

        var experiment = new ExperimentData(
                InstanceEnum.HARD, folderPath,
                SelectionEnum.TOURNAMENT,
                50,
                3,
                0.8,
                0.8,
                1000,
                7);
        BufferedWriter writer = new BufferedWriter(new FileWriter(
                "F:\\sztuczna_inteligencja\\E1\\src\\experimentOutputs\\"
                        + experiment.getFileName()
                        + ".csv", false));
        writer.append("sep=,\n");
        writer.append(experiment.toString());
        writer.append(String.valueOf('\n'));
        for (int i = 0; i < experiment.numberOfGenerationsAim; i++) {
            writer.append(experiment.getSnapshot()).append(String.valueOf('\n'));;
            experiment.runNextGeneration();
        }
        writer.close();
    }
}
