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
        suggestion.setNewValue(0, 0);
        suggestion.setNewValue(1, 6);
        suggestion.setNewValue(0, 18);
        suggestion.setNewValue(1, 24);

        suggestion.setNewValue(0, 5);
        suggestion.setNewValue(0, 17);
        suggestion.setNewValue(0, 23);
        suggestion.setNewValue(1, 29);
        suggestion.setNewValue(1, 35);
        Assertions.assertFalse(suggestion.areConstraintsNotBrokenAfterLastChange());
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