import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static java.util.Arrays.asList;

public class BinaryConsts {
    private static final HashMap<BinaryEnum, int[]> sizes = new HashMap<>() {
        {
            put(BinaryEnum.B6x6, new int[] {6, 6});
            put(BinaryEnum.B8x8, new int[] {8, 8});
            put(BinaryEnum.B10x10, new int[] {10, 10});
        }
    };

    public static int[] getSize(BinaryEnum binaryEnum) {
        return sizes.get(binaryEnum);
    }
}

class TestFut {
    static FutoshikiProblem futoshikiProblem;
    static FutoshikiCSP futoshikiCSP;

    @Test
    void test6x6() {
        futoshikiProblem = new FutoshikiProblem(FutoshikiEnum.F6x6);
        futoshikiCSP = new FutoshikiCSP(futoshikiProblem);
        System.out.println(futoshikiProblem);
        var results = futoshikiCSP.getResults();
        for (var result : results) {
            System.out.println("------------------------------------------------------");
            System.out.println(result);
        }

        System.out.println(
                results.size()
        );

//        Assertions.assertEquals(1, results.size());
//        var expected = new ArrayList<Integer>(asList(
//                0, 1, 0, 1, 1, 0,
//                1, 0, 0, 1, 0, 1,
//                0, 1, 1, 0, 0, 1,
//                0, 1, 1, 0, 1, 0,
//                1, 0, 0, 1, 1, 0,
//                1, 0, 1, 0, 0, 1
//
//        ));
//        Assertions.assertIterableEquals(expected, results.get(0).getPartialSolution());
    }
}