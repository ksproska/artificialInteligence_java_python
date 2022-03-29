import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static java.util.Arrays.asList;

class BinaryTests {
    static BinaryProblem binaryProblem;
    static CSP<Integer, Integer> csp;

    @Test
    void testCorrections() {
        binaryProblem = new BinaryProblem(BinaryEnum.B6x6);
        var suggestion = new BinaryPartialSolution(binaryProblem);
        suggestion.setValue(0, 0);
        suggestion.setValue(1, 6);
        suggestion.setValue(0, 18);
        suggestion.setValue(1, 24);

        suggestion.setValue(0, 5);
        suggestion.setValue(0, 17);
        suggestion.setValue(0, 23);
        suggestion.setValue(1, 29);
        suggestion.setValue(1, 35);
        Assertions.assertFalse(suggestion.isCurrentCorrect());
    }

    @Test
    void test6x6() {
        binaryProblem = new BinaryProblem(BinaryEnum.B6x6);
        csp = new CSP<>(binaryProblem);
        var results = csp.getResults();
        Assertions.assertEquals(1, results.size());
        var expected = new ArrayList<Integer>(asList(
                0, 1, 0, 1, 1, 0,
                1, 0, 0, 1, 0, 1,
                0, 1, 1, 0, 0, 1,
                0, 1, 1, 0, 1, 0,
                1, 0, 0, 1, 1, 0,
                1, 0, 1, 0, 0, 1

        ));
        Assertions.assertIterableEquals(expected, results.get(0).getPartialSolution());
    }

    @Test
    void test8x8() {
        binaryProblem = new BinaryProblem(BinaryEnum.B8x8);
        csp = new CSP<>(binaryProblem);
        var results = csp.getResults();
        Assertions.assertEquals(1, results.size());
        var expected = new ArrayList<Integer>(asList(
                1, 1, 0, 0, 1, 0, 1, 0,
                1, 0, 1, 1, 0, 0, 1, 0,
                0, 1, 0, 0, 1, 1, 0, 1,
                0, 0, 1, 1, 0, 1, 1, 0,
                1, 0, 1, 0, 1, 0, 0, 1,
                0, 1, 0, 1, 0, 1, 0, 1,
                0, 1, 1, 0, 1, 0, 1, 0,
                1, 0, 0, 1, 0, 1, 0, 1

        ));
        Assertions.assertIterableEquals(expected, results.get(0).getPartialSolution());
    }

    @Test
    void test10x10() {
        binaryProblem = new BinaryProblem(BinaryEnum.B10x10);
        csp = new CSP<>(binaryProblem);
        var results = csp.getResults();
        Assertions.assertEquals(1, results.size());
        var expected = new ArrayList<Integer>(asList(
                0, 1, 1, 0, 0, 1, 0, 1, 0, 1,
                1, 0, 0, 1, 1, 0, 0, 1, 1, 0,
                1, 0, 0, 1, 1, 0, 1, 0, 1, 0,
                0, 1, 1, 0, 0, 1, 1, 0, 0, 1,
                1, 0, 1, 0, 1, 0, 0, 1, 0, 1,
                0, 1, 0, 1, 0, 1, 0, 1, 1, 0,
                1, 0, 0, 1, 1, 0, 1, 0, 0, 1,
                0, 1, 1, 0, 1, 1, 0, 1, 0, 0,
                1, 0, 1, 0, 0, 1, 1, 0, 1, 0,
                0, 1, 0, 1, 0, 0, 1, 0, 1, 1
        ));
        Assertions.assertIterableEquals(expected, results.get(0).getPartialSolution());
    }
}