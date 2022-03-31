import consts.BinaryEnum;
import consts.FutoshikiEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static java.util.Arrays.asList;


public class CSP_Test {
    static Binary_Problem binaryProblem;
    static Futoshiki_Problem futoshikiProblem;
    static CSP<Integer, Integer, Binary_Problem, Binary_PartialSolution> binaryCsp;
    static CSP<Object, Integer, Futoshiki_Problem, Futoshiki_PartialSolution> futoshikiCSP;

    @Test
    void testCorrectionsB6x6() {
        binaryProblem = new Binary_Problem(BinaryEnum.B6x6);
        var suggestion = new Binary_PartialSolution(binaryProblem);
        suggestion.setNewValueAtIndexOf(0, 0);
        suggestion.setNewValueAtIndexOf(1, 6);
        suggestion.setNewValueAtIndexOf(0, 18);
        suggestion.setNewValueAtIndexOf(1, 24);

        suggestion.setNewValueAtIndexOf(0, 5);
        suggestion.setNewValueAtIndexOf(0, 17);
        suggestion.setNewValueAtIndexOf(0, 23);
        suggestion.setNewValueAtIndexOf(1, 29);
        suggestion.setNewValueAtIndexOf(1, 35);
        Assertions.assertFalse(suggestion.areConstraintsNotBrokenAfterLastChange());
    }

    @Test
    void testCorrectionsF4x4() {
        futoshikiProblem = new Futoshiki_Problem(FutoshikiEnum.F4x4);
        var solution = futoshikiProblem.getInitialSolution();
        solution.setNewValueAtIndexOf(4, 4);
        solution.setNewValueAtIndexOf(2, 20);
        solution.setNewValueAtIndexOf(2, 32);
        solution.setNewValueAtIndexOf(1, 46);
        solution.setNewValueAtIndexOf(1, 48);
        Assertions.assertFalse(solution.areConstraintsNotBrokenAfterLastChange());
    }

    @Test
    void testB6x6() {
        binaryProblem = new Binary_Problem(BinaryEnum.B6x6);
        binaryCsp = new CSP<>(binaryProblem);
        var results = binaryCsp.getResults();
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
    void testB8x8() {
        binaryProblem = new Binary_Problem(BinaryEnum.B8x8);
        binaryCsp = new CSP<>(binaryProblem);
        var results = binaryCsp.getResults();
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
    void testB10x10() {
        binaryProblem = new Binary_Problem(BinaryEnum.B10x10);
        binaryCsp = new CSP<>(binaryProblem);
        var results = binaryCsp.getResults();
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

    @Test
    void testF4x4() {
        futoshikiProblem = new Futoshiki_Problem(FutoshikiEnum.F4x4);
        futoshikiCSP = new CSP<>(futoshikiProblem);
        var results = futoshikiCSP.getResults();
        Assertions.assertEquals(1, results.size());
        var expected = """
                3   >   1       4       2  \s
                                           \s
                2   <   4       3   >   1  \s
                                >          \s
                1       3       2       4  \s
                <                          \s
                4       2   >   1   <   3\s""";

        Assertions.assertEquals(expected, futoshikiProblem.toDisplay(results.get(0).getPartialSolution()));
    }

    @Test
    void testF5x5() {
        futoshikiProblem = new Futoshiki_Problem(FutoshikiEnum.F5x5);
        futoshikiCSP = new CSP<>(futoshikiProblem);
        var results = futoshikiCSP.getResults();
        Assertions.assertEquals(1, results.size());
        var expected = """
               2       3       1       4   <   5  \s
                                                  \s
               4   >   1       3   <   5       2  \s
                                                  \s
               1       5       4       2       3  \s
               <                       <          \s
               5       4   >   2       3       1  \s
                                                  \s
               3       2   <   5       1       4\s""";

        Assertions.assertEquals(expected, futoshikiProblem.toDisplay(results.get(0).getPartialSolution()));
    }

    @Test
    void testF6x6() {
        futoshikiProblem = new Futoshiki_Problem(FutoshikiEnum.F6x6);
        futoshikiCSP = new CSP<>(futoshikiProblem);
        var results = futoshikiCSP.getResults();
        Assertions.assertEquals(133, results.size());
        var expected = """
                5       2       6       1       3   <   4  \s
                                        <                  \s
                3       4       1       2       6       5  \s
                                                        >  \s
                2   >   1       5   <   6       4       3  \s
                <                               <       >  \s
                4       6       2       3       5       1  \s
                <                                          \s
                6       3   <   4       5       1       2  \s
                                        >                  \s
                1       5       3   <   4   >   2       6\s""";

        Assertions.assertEquals(expected, futoshikiProblem.toDisplay(results.get(0).getPartialSolution()));
    }
}
