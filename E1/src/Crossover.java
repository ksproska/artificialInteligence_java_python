import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

public class Crossover {
    public static int[] partiallyMatchedCrossover(int[] parent1, int[] parent2, int startInx, int segmentLength) {
        int[] child = new int[parent1.length];
        Arrays.fill(child, -1);
        if (segmentLength >= 0) System.arraycopy(parent1, startInx, child, startInx, segmentLength);

        for (int i = 0; i < segmentLength; i++) {
            int oldInx = i + startInx;
            int valToAdd = parent2[oldInx];

            if(!Arrays.stream(child).boxed().toList().contains(valToAdd)) {
                while (child[oldInx] != -1) {
                    List<Integer> l2 =  Arrays.stream(parent2).boxed().toList();
                    oldInx = l2.indexOf(parent1[oldInx]);
                }
                child[oldInx] = valToAdd;
            }
        }
        for (int i = 0; i < child.length; i++) {
            if(child[i] == -1) {
                child[i] = parent2[i];
            }
        }
        return child;
    }
}

class CrossoverTest {
    @Test
    public void partiallyMatchedCrossoverTest() {
        int[] parent1 = new int[]{8, 4, 7, 3, 6, 2, 5, 1, 9, 0};
        int[] parent2 = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] child =   new int[]{0, 7, 4, 3, 6, 2, 5, 1, 8, 9};
        int startInx = 3, segmentLen = 5;

        Assertions.assertArrayEquals(Crossover.partiallyMatchedCrossover(parent1, parent2, startInx, segmentLen), child);
    }

    @Test
    public void partiallyMatchedCrossoverTest2() {
        int[] parent1 = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        int[] parent2 = new int[]{4, 3, 2, 7, 6, 5, 8, 1};
        int[] child =   new int[]{7, 3, 2, 4, 5, 6, 8, 1};
        int startInx = 3, segmentLen = 2;

        Assertions.assertArrayEquals(Crossover.partiallyMatchedCrossover(parent1, parent2, startInx, segmentLen), child);
    }
}
