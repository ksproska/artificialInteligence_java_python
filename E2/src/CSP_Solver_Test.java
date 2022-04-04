import consts.BinaryEnum;
import consts.BinaryHeuristicEnum;
import consts.FutoshikiEnum;
import consts.FutoshikiHeuristicEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static java.util.Arrays.asList;


public class CSP_Solver_Test {
    static Binary_Problem binaryProblem;
    static Futoshiki_Problem futoshikiProblem;
    static CSP_Solver<Integer, Integer, BinaryHeuristicEnum, Binary_Problem, Binary_PartialSolution> binaryCSPSolver;
    static CSP_Solver<Object, Integer, FutoshikiHeuristicEnum, Futoshiki_Problem, Futoshiki_PartialSolution> futoshikiCSPSolver;

    @Test
    void testCorrectionsB6x6() {
        binaryProblem = new Binary_Problem(BinaryEnum.B6x6);
        var partialSolution = new Binary_PartialSolution(binaryProblem);
        partialSolution.setNewValueAtIndexOf(0, 0);
        Assertions.assertTrue(partialSolution.areConstraintsNotBrokenAfterLastChange());
        partialSolution.setNewValueAtIndexOf(1, 6);
        Assertions.assertTrue(partialSolution.areConstraintsNotBrokenAfterLastChange());
        partialSolution.setNewValueAtIndexOf(0, 18);
        Assertions.assertTrue(partialSolution.areConstraintsNotBrokenAfterLastChange());
        partialSolution.setNewValueAtIndexOf(1, 24);
        Assertions.assertTrue(partialSolution.areConstraintsNotBrokenAfterLastChange());
        partialSolution.setNewValueAtIndexOf(0, 5);
        Assertions.assertTrue(partialSolution.areConstraintsNotBrokenAfterLastChange());
        partialSolution.setNewValueAtIndexOf(0, 17);
        Assertions.assertTrue(partialSolution.areConstraintsNotBrokenAfterLastChange());
        partialSolution.setNewValueAtIndexOf(0, 23);
        Assertions.assertTrue(partialSolution.areConstraintsNotBrokenAfterLastChange());
        partialSolution.setNewValueAtIndexOf(1, 29);
        Assertions.assertTrue(partialSolution.areConstraintsNotBrokenAfterLastChange());
        partialSolution.setNewValueAtIndexOf(1, 35);
        Assertions.assertFalse(partialSolution.areConstraintsNotBrokenAfterLastChange());
    }

    @Test
    void testCorrectionsF4x4() {
        futoshikiProblem = new Futoshiki_Problem(FutoshikiEnum.F4x4);
        var partialSolution = futoshikiProblem.getInitialPartialSolution();
        partialSolution.setNewValueAtIndexOf(4, 4);
        Assertions.assertTrue(partialSolution.areConstraintsNotBrokenAfterLastChange());
        partialSolution.setNewValueAtIndexOf(2, 20);
        Assertions.assertTrue(partialSolution.areConstraintsNotBrokenAfterLastChange());
        partialSolution.setNewValueAtIndexOf(2, 32);
        Assertions.assertTrue(partialSolution.areConstraintsNotBrokenAfterLastChange());
        partialSolution.setNewValueAtIndexOf(1, 46);
        Assertions.assertTrue(partialSolution.areConstraintsNotBrokenAfterLastChange());
        partialSolution.setNewValueAtIndexOf(1, 48);
        Assertions.assertFalse(partialSolution.areConstraintsNotBrokenAfterLastChange());
    }

    @Test
    void testBinaryBacktracking6x6() {
        binaryProblem = new Binary_Problem(BinaryEnum.B6x6);
        binaryCSPSolver = new CSP_SolverBacktracking<>(binaryProblem);
        var results = binaryCSPSolver.getResults(BinaryHeuristicEnum.BH_IN_ORDER);
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
        Assertions.assertEquals(256, binaryCSPSolver.getVisitedNodesCounter());
        Assertions.assertEquals(76, binaryCSPSolver.getTillFirstVisitedNodesCounter());
        Assertions.assertEquals(129, binaryCSPSolver.getReturnsCounter());
        Assertions.assertEquals(18, binaryCSPSolver.getTillFirstReturnsCounter());
    }

    @Test
    void testBinaryBacktracking8x8() {
        binaryProblem = new Binary_Problem(BinaryEnum.B8x8);
        binaryCSPSolver = new CSP_SolverBacktracking<>(binaryProblem);
        var results = binaryCSPSolver.getResults(BinaryHeuristicEnum.BH_IN_ORDER);
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
        Assertions.assertEquals(1496, binaryCSPSolver.getVisitedNodesCounter());
        Assertions.assertEquals(1095, binaryCSPSolver.getTillFirstVisitedNodesCounter());
        Assertions.assertEquals(749, binaryCSPSolver.getReturnsCounter());
        Assertions.assertEquals(511, binaryCSPSolver.getTillFirstReturnsCounter());
    }

    @Test
    void testBinaryBacktracking10x10() {
        binaryProblem = new Binary_Problem(BinaryEnum.B10x10);
        binaryCSPSolver = new CSP_SolverBacktracking<>(binaryProblem);
        var results = binaryCSPSolver.getResults(BinaryHeuristicEnum.BH_IN_ORDER);
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
        Assertions.assertEquals(896, binaryCSPSolver.getVisitedNodesCounter());
        Assertions.assertEquals(452, binaryCSPSolver.getTillFirstVisitedNodesCounter());
        Assertions.assertEquals(449, binaryCSPSolver.getReturnsCounter());
        Assertions.assertEquals(172, binaryCSPSolver.getTillFirstReturnsCounter());
    }

    @Test
    void testFutoshikiBacktracking4x4() {
        futoshikiProblem = new Futoshiki_Problem(FutoshikiEnum.F4x4);
        futoshikiCSPSolver = new CSP_SolverBacktracking<>(futoshikiProblem);
        var results = futoshikiCSPSolver.getResults(FutoshikiHeuristicEnum.FH_IN_ORDER);
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
        Assertions.assertEquals(864, futoshikiCSPSolver.getVisitedNodesCounter());
        Assertions.assertEquals(265, futoshikiCSPSolver.getTillFirstVisitedNodesCounter());
        Assertions.assertEquals(217, futoshikiCSPSolver.getReturnsCounter());
        Assertions.assertEquals(57, futoshikiCSPSolver.getTillFirstReturnsCounter());
    }

    @Test
    void testFutoshikiBacktracking5x5() {
        futoshikiProblem = new Futoshiki_Problem(FutoshikiEnum.F5x5);
        futoshikiCSPSolver = new CSP_SolverBacktracking<>(futoshikiProblem);
        var results = futoshikiCSPSolver.getResults(FutoshikiHeuristicEnum.FH_IN_ORDER);
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
        Assertions.assertEquals(1190, futoshikiCSPSolver.getVisitedNodesCounter());
        Assertions.assertEquals(296, futoshikiCSPSolver.getTillFirstVisitedNodesCounter());
        Assertions.assertEquals(239, futoshikiCSPSolver.getReturnsCounter());
        Assertions.assertEquals(46, futoshikiCSPSolver.getTillFirstReturnsCounter());
    }

    @Test
    void testFutoshikiBacktracking6x6() {
        futoshikiProblem = new Futoshiki_Problem(FutoshikiEnum.F6x6);
        futoshikiCSPSolver = new CSP_SolverBacktracking<>(futoshikiProblem);
        var results = futoshikiCSPSolver.getResults(FutoshikiHeuristicEnum.FH_IN_ORDER);
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
        Assertions.assertEquals(20837934, futoshikiCSPSolver.getVisitedNodesCounter());
        Assertions.assertEquals(1025184, futoshikiCSPSolver.getTillFirstVisitedNodesCounter());
        Assertions.assertEquals(3473122, futoshikiCSPSolver.getReturnsCounter());
        Assertions.assertEquals(170844, futoshikiCSPSolver.getTillFirstReturnsCounter());
    }
}
