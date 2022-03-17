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

//    public static int[] mutate(int[] child, double percentage) {
//        int[] newChild = child.clone();
//        int lenToChange = (int) (newChild.length * percentage);
//        int maxStartInx = newChild.length - lenToChange;
//        int startInx = random.nextInt(maxStartInx);
////        System.out.println(startInx);
////        System.out.println(lenToChange);
////        System.out.println(Arrays.toString(child));
//
//        int first = newChild[startInx];
//        for (int i = 0; i < lenToChange - 1; i++) {
//            newChild[startInx + i] = child[startInx + i + 1];
//        }
//        newChild[startInx + lenToChange - 1] = first;
//        return newChild;
//    }
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
}