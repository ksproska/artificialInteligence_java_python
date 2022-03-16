import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Crossover {
    static Random random = new Random();
    public static int[] pmxRandom(int[] parent1, int[] parent2) {
        var startInx = random.nextInt(parent1.length);
        var segmentLen = parent1.length/4;
        if(startInx + segmentLen > parent1.length) {
            segmentLen = parent1.length - startInx;
        }
        return partiallyMatchedCrossover(parent1, parent2, startInx, segmentLen);
    }

    public static int[] noRepetitions(int[] parent) {
        int[] newParent = new int[parent.length];
        int zeroCount = 0;
        for (int i = 0; i < parent.length; i++) {
            newParent[i] = parent[i];
            if (parent[i] == -1) {
                zeroCount += 1;
                newParent[i] -= zeroCount;
            }
        }
        return newParent;
    }
    public static int[] toRepetitions(int[] parent) {
        int[] newParent = new int[parent.length];
        for (int i = 0; i < parent.length; i++) {
            if (parent[i] >= 0) {
                newParent[i] = parent[i];
            }
            else {
                newParent[i] = -1;
            }
        }
        return newParent;
    }

    public static int[] partiallyMatchedCrossover(int[] parent1Repetitions, int[] parent2Repetitions, int startInx, int segmentLength) {
        var parent1 = noRepetitions(parent1Repetitions);
        var parent2 = noRepetitions(parent2Repetitions);
        int[] child = new int[parent1.length];
        Arrays.fill(child, -1);
        if (segmentLength >= 0) System.arraycopy(parent1, startInx, child, startInx, segmentLength);
//        var p1c = parent1.clone();
//        Arrays.sort(p1c);
//        System.out.println(Arrays.toString(p1c));
//        System.out.println(Arrays.toString(parent2));
//        System.out.println(startInx);
//        System.out.println(segmentLength);

        for (int i = 0; i < segmentLength; i++) {
            int oldInx = i + startInx;
            int valToAdd = parent2[oldInx];

            if(!Arrays.stream(child).boxed().toList().contains(valToAdd)) {
                while (child[oldInx] != -1) {
                    List<Integer> l2 =  Arrays.stream(parent2).boxed().toList();
                    oldInx = l2.indexOf(parent1[oldInx]);
//                    System.out.println(oldInx);
                }
                child[oldInx] = valToAdd;
            }
        }
        for (int i = 0; i < child.length; i++) {
            if(child[i] == -1) {
                child[i] = parent2[i];
            }
        }
        return toRepetitions(child);
//        return child;
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

    @Test
    public void partiallyMatchedCrossoverTest3() {
        int[] parent1 = new int[]{1, 2, 3, 5, 4, 6};
        int[] parent2 = new int[]{4, 3, 2, 6, 1, 5};
        int[] child =   new int[]{4, 2, 3, 6, 1, 5};
        int startInx = 1, segmentLen = 2;

        Assertions.assertArrayEquals(Crossover.partiallyMatchedCrossover(parent1, parent2, startInx, segmentLen), child);
    }

    @Test
    public void repetitionsReplacer() {
        int[] parent = new int[]{1, 2, -1, 3, 5, -1, 4, 6, -1};
        int[] parentNoReplacement = new int[]{1, 2, -2, 3, 5, -3, 4, 6, -4};

        var replaced = Crossover.noRepetitions(parent);
        System.out.println(Arrays.toString(replaced));
        var normal = Crossover.toRepetitions(replaced);
        System.out.println(Arrays.toString(normal));
    }
}
