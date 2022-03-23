package zad2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

public class Mutation {
    private static final Random random = new Random();

    public static int[] mutate(int[] child) {
        int first = random.nextInt(child.length), second = random.nextInt(child.length);
        while(first == second) {
            second = random.nextInt(child.length);
        }
        int temp = child[first];
        child[first] = child[second];
        child[second] = temp;
        return child;
    }

    public static int[] mutate(int[] child, int lenToChange) {
        int maxStartInx = child.length - lenToChange;
        int startInx = random.nextInt(maxStartInx);
        for (int i = lenToChange - 1; i > 0; i--) {
            int j = random.nextInt(i+1);
            int temp = child[i + startInx];
            child[i + startInx] = child[j + startInx];
            child[j + startInx] = temp;
        }

        return child;
    }
}

class MutationTest {
    @Test
    public void mutationTest1() {
        var child = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        var childCopy = child.clone();
        var mutated = Mutation.mutate(child);
        int sumChange = 0;
        int sumChangeCopy = 0;

        for (int i = 0; i < child.length; i++) {
            if(child[i] != mutated[i]) {
                sumChange += 1;
            }
            if(childCopy[i] != mutated[i]) {
                sumChangeCopy += 1;
            }
        }
        Assertions.assertEquals(0, sumChange);
        Assertions.assertEquals(2, sumChangeCopy);
    }
    @Test
    public void mutationTest2() {
        var child = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        var childCopy = child.clone();
        var mutated = Mutation.mutate(child, 4);
        int sumChange = 0;
        int sumChangeCopy = 0;

        for (int i = 0; i < child.length; i++) {
            if(child[i] != mutated[i]) {
                sumChange += 1;
            }
            if(childCopy[i] != mutated[i]) {
                sumChangeCopy += 1;
            }
        }
        System.out.println(Arrays.toString(childCopy));
        System.out.println(Arrays.toString(child));
        System.out.println(Arrays.toString(mutated));
        Assertions.assertEquals(0, sumChange);
        Assertions.assertTrue(sumChangeCopy <= 4);
    }
}