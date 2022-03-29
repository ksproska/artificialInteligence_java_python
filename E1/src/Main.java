import zad1.Factory;
import zad1.InstanceEnum;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class Main {
    public static int[] bestGridOutOfFirst(int n, Factory factory) {
        int[] bestOne = factory.createInitGrid();
        int bestEval = factory.evaluateGrid(bestOne);
        for (int i = 0; i < n - 1; i++) {
            int[] nextGrid = factory.createInitGrid();
            int nextEval = factory.evaluateGrid(nextGrid);
            if(bestEval > nextEval) {
                bestOne = nextGrid;
                bestEval = nextEval;
            }
        }
        return bestOne;
    }

    public static void randomMethod() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(
                "F:\\sztuczna_inteligencja\\E1\\src\\experimentOutputs\\"
                        + "random method"
                        + ".csv", false));
        String folderPath = "F:\\sztuczna_inteligencja\\flo_dane_v1.2";
        InstanceEnum[] instanceEnums = new InstanceEnum[] {InstanceEnum.EASY, InstanceEnum.FLAT, InstanceEnum.HARD};
        writer.append("sep=,\n");

        for (var instanceEn : instanceEnums) {
            Factory factory = new Factory(instanceEn, folderPath);
            for (var nubOfGens : new int[]{300 * 50, 600 * 50}) {
                for (int trial = 0; trial < 10; trial++) {
                    int[] bestOne = factory.createInitGrid();
                    int[] worstOne = bestOne.clone();
                    int bestEval = factory.evaluateGrid(bestOne);
                    int worstEval = factory.evaluateGrid(worstOne);
                    var sum = 0;
                    for (int j = 0; j < nubOfGens - 1; j++) {
                        int[] nextGrid = factory.createInitGrid();
                        int nextEval = factory.evaluateGrid(nextGrid);
                        if (bestEval > nextEval) {
                            bestOne = nextGrid;
                            bestEval = nextEval;
                        }
                        if (worstEval < nextEval) {
                            worstOne = nextGrid;
                            worstEval = nextEval;
                        }
                        sum += nextEval;
                    }
                    writer.append(instanceEn + " " + nubOfGens + "," + bestEval + "," + worstEval + "," + ((float) sum / nubOfGens) + "\n");
                }
                writer.append("\n\n");
            }
        }
        writer.close();
    }

    public static void main(String[] args) throws IOException {
        randomMethod();
    }
}
