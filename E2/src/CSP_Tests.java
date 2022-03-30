import consts.BinaryEnum;
import consts.FutoshikiEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static java.util.Arrays.asList;


public class CSP_Tests {
    static BinaryProblem binaryProblem;
    static BinaryCSP binaryCsp;
    static FutoshikiProblem futoshikiProblem;
    static FutoshikiCSP futoshikiCSP;

    @Test
    void testCorrectionsB6x6() {
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
    void testCorrectionsF4x4() {
        futoshikiProblem = new FutoshikiProblem(FutoshikiEnum.F4x4);
        var solution = futoshikiProblem.getInitialSolution();
        solution.setNewValue(4, 4);
        solution.setNewValue(2, 20);
        solution.setNewValue(2, 32);
        solution.setNewValue(1, 46);
        solution.setNewValue(1, 48);
        Assertions.assertFalse(solution.areConstraintsNotBrokenAfterLastChange());
    }

    @Test
    void testB6x6() {
        binaryProblem = new BinaryProblem(BinaryEnum.B6x6);
        binaryCsp = new BinaryCSP(binaryProblem);
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
        binaryProblem = new BinaryProblem(BinaryEnum.B8x8);
        binaryCsp = new BinaryCSP(binaryProblem);
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
        binaryProblem = new BinaryProblem(BinaryEnum.B10x10);
        binaryCsp = new BinaryCSP(binaryProblem);
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
        futoshikiProblem = new FutoshikiProblem(FutoshikiEnum.F4x4);
        futoshikiCSP = new FutoshikiCSP(futoshikiProblem);
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
        futoshikiProblem = new FutoshikiProblem(FutoshikiEnum.F5x5);
        futoshikiCSP = new FutoshikiCSP(futoshikiProblem);
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
        futoshikiProblem = new FutoshikiProblem(FutoshikiEnum.F6x6);
        futoshikiCSP = new FutoshikiCSP(futoshikiProblem);
        var results = futoshikiCSP.getResults();
        Assertions.assertEquals(133, results.size());
        var expected = """
                1       3       6       2       4   <   5  \s
                                        <                  \s
                5       4       1       3       2       6  \s
                                                        >  \s
                2   >   1       5   <   6       3       4  \s
                <                               <       >  \s
                3       6       4       1       5       2  \s
                <                                          \s
                4       2   <   3       5       6       1  \s
                                        >                  \s
                6       5       2   <   4   >   1       3\s""";

        Assertions.assertEquals(expected, futoshikiProblem.toDisplay(results.get(0).getPartialSolution()));
    }
}
