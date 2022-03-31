import consts.BinaryEnum;
import consts.FutoshikiEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static java.util.Arrays.asList;


public class CSP_Solver_Test {
    static Binary_Problem binaryProblem;
    static Futoshiki_Problem futoshikiProblem;
    static CSP_Solver<Integer, Integer, Binary_Problem, Binary_PartialSolution> binaryCSPSolver;
    static CSP_Solver<Object, Integer, Futoshiki_Problem, Futoshiki_PartialSolution> futoshikiCSPSolver;

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
        var solution = futoshikiProblem.getInitialPartialSolution();
        solution.setNewValueAtIndexOf(4, 4);
        solution.setNewValueAtIndexOf(2, 20);
        solution.setNewValueAtIndexOf(2, 32);
        solution.setNewValueAtIndexOf(1, 46);
        solution.setNewValueAtIndexOf(1, 48);
        Assertions.assertFalse(solution.areConstraintsNotBrokenAfterLastChange());
    }

    @Test
    void testBinaryBacktracking6x6() {
        binaryProblem = new Binary_Problem(BinaryEnum.B6x6);
        binaryCSPSolver = new CSP_BacktrackingSolver<>(binaryProblem);
        var results = binaryCSPSolver.getResults();
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
    void testBinaryBacktracking8x8() {
        binaryProblem = new Binary_Problem(BinaryEnum.B8x8);
        binaryCSPSolver = new CSP_BacktrackingSolver<>(binaryProblem);
        var results = binaryCSPSolver.getResults();
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
    void testBinaryBacktracking10x10() {
        binaryProblem = new Binary_Problem(BinaryEnum.B10x10);
        binaryCSPSolver = new CSP_BacktrackingSolver<>(binaryProblem);
        var results = binaryCSPSolver.getResults();
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
    void testFutoshikiBacktracking4x4() {
        futoshikiProblem = new Futoshiki_Problem(FutoshikiEnum.F4x4);
        futoshikiCSPSolver = new CSP_BacktrackingSolver<>(futoshikiProblem);
        var results = futoshikiCSPSolver.getResults();
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
    void testFutoshikiBacktracking5x5() {
        futoshikiProblem = new Futoshiki_Problem(FutoshikiEnum.F5x5);
        futoshikiCSPSolver = new CSP_BacktrackingSolver<>(futoshikiProblem);
        var results = futoshikiCSPSolver.getResults();
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
    void testFutoshikiBacktracking6x6() {
        futoshikiProblem = new Futoshiki_Problem(FutoshikiEnum.F6x6);
        futoshikiCSPSolver = new CSP_BacktrackingSolver<>(futoshikiProblem);
        var results = futoshikiCSPSolver.getResults();
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
