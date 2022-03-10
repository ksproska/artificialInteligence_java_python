import zad1.Factory;
import zad1.InstanceEnum;

import java.util.Arrays;

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

    public static void main(String[] args) {
        String folderPath = "F:\\sztuczna_inteligencja\\flo_dane_v1.2";
        Factory factory = new Factory(InstanceEnum.HARD, folderPath);
        System.out.println(factory);

        var bestGrid = bestGridOutOfFirst(100000, factory);
        System.out.println("best grid:");
//        System.out.println(Arrays.toString(bestGrid));
        factory.displayGrid(bestGrid);
        int eval = factory.evaluateGrid(bestGrid);
        System.out.printf("\nEvaluated: %s", eval);
    }
}
