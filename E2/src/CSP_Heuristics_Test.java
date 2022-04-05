import consts.BinaryEnum;
import consts.BinaryHeuristicEnum;
import consts.FutoshikiEnum;
import consts.FutoshikiHeuristicEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class CSP_Heuristics_Test {
    static Binary_Problem binaryProblem;
    static Futoshiki_Problem futoshikiProblem;
    static CSP_Solver<Integer, Integer, BinaryHeuristicEnum, Binary_Problem, Binary_PartialSolution> binaryCSPSolver;
    static CSP_Solver<Object, Integer, FutoshikiHeuristicEnum, Futoshiki_Problem, Futoshiki_PartialSolution> futoshikiCSPSolver;

    @Test
    void testBinary6x6() {
    }

    @Test
    void testBinary8x8() {
    }

    @Test
    void testBinary10x10() {
        binaryProblem = new Binary_Problem(BinaryEnum.B10x10);
        System.out.println(binaryProblem);
        binaryCSPSolver = new CSP_SolverForwardChecking<>(binaryProblem);
        var resultsInOrder = binaryCSPSolver.getResults(BinaryHeuristicEnum.BH_IN_ORDER);
        System.out.println(binaryCSPSolver);
        var resultsInOrderAndDomain = binaryCSPSolver.getResults(BinaryHeuristicEnum.BH_IN_ORDER_AND_DOMAIN_ORDER);
        System.out.println(binaryCSPSolver);
        var resultsBiggestDomain = binaryCSPSolver.getResults(BinaryHeuristicEnum.BH_SMALLEST_DOMAIN);
        System.out.println(binaryCSPSolver);
        var resultsBiggestDomainAndDomainOrder = binaryCSPSolver.getResults(BinaryHeuristicEnum.BH_SMALLEST_DOMAIN_AND_DOMAIN_ORDER);
        System.out.println(binaryCSPSolver);
        var resultsMostAround = binaryCSPSolver.getResults(BinaryHeuristicEnum.BH_MOST_IN_ROWS_COLS);
        System.out.println(binaryCSPSolver);
        var resultsMostAroundAndDomain = binaryCSPSolver.getResults(BinaryHeuristicEnum.BH_MOST_IN_ROWS_COLS_AND_DOMAIN_ORDER);
        System.out.println(binaryCSPSolver);
//
//        Assertions.assertIterableEquals(resultsInOrder.get(0).partialSolution, resultsBiggestDomain.get(0).partialSolution);
//        Assertions.assertIterableEquals(resultsInOrder.get(0).partialSolution, resultsMostAround.get(0).partialSolution);
//        Assertions.assertEquals(resultsInOrder.size(), resultsBiggestDomain.size());
//        Assertions.assertEquals(resultsInOrder.size(), resultsMostAround.size());
//
        System.out.println("--------------------------------");

        binaryCSPSolver = new CSP_SolverBacktracking<>(binaryProblem);
        resultsInOrder = binaryCSPSolver.getResults(BinaryHeuristicEnum.BH_IN_ORDER);
        System.out.println(binaryCSPSolver);
        resultsInOrderAndDomain = binaryCSPSolver.getResults(BinaryHeuristicEnum.BH_IN_ORDER_AND_DOMAIN_ORDER);
        System.out.println(binaryCSPSolver);
        resultsMostAround = binaryCSPSolver.getResults(BinaryHeuristicEnum.BH_MOST_IN_ROWS_COLS);
        System.out.println(binaryCSPSolver);
        resultsMostAroundAndDomain = binaryCSPSolver.getResults(BinaryHeuristicEnum.BH_MOST_IN_ROWS_COLS_AND_DOMAIN_ORDER);
        System.out.println(binaryCSPSolver);
//
//        Assertions.assertIterableEquals(resultsInOrder.get(0).partialSolution, resultsMostAround.get(0).partialSolution);
//        Assertions.assertEquals(resultsInOrder.size(), resultsMostAround.size());
    }

    @Test
    void testFutoshikiForwardChecking6x6() {
        futoshikiProblem = new Futoshiki_Problem(FutoshikiEnum.F6x6);
        futoshikiCSPSolver = new CSP_SolverForwardChecking<>(futoshikiProblem);
        var resultsInOrder = futoshikiCSPSolver.getResults(FutoshikiHeuristicEnum.FH_IN_ORDER);
        System.out.println(futoshikiCSPSolver);
        var resultsMostAround = futoshikiCSPSolver.getResults(FutoshikiHeuristicEnum.FH_MOST_CONSTRAINTS);
        System.out.println(futoshikiCSPSolver);
        var resultsMostConstraintsAndDomain = futoshikiCSPSolver.getResults(FutoshikiHeuristicEnum.FH_MOST_CONSTRAINTS_AND_CHANGE_DOMAIN_ORDER);
        System.out.println(futoshikiCSPSolver);
        var resultsSmallestDomain = futoshikiCSPSolver.getResults(FutoshikiHeuristicEnum.FH_SMALLEST_DOMAIN);
        System.out.println(futoshikiCSPSolver);
        var resultsSmallestDomainAndDomain = futoshikiCSPSolver.getResults(FutoshikiHeuristicEnum.FH_SMALLEST_DOMAIN_AND_CHANGE_DOMAIN_ORDER);
        System.out.println(futoshikiCSPSolver);

//        Assertions.assertIterableEquals(resultsInOrder.get(0).partialSolution, resultsSmallestDomain.get(0).partialSolution);
//        Assertions.assertIterableEquals(resultsInOrder.get(0).partialSolution, resultsMostAround.get(0).partialSolution);
        Assertions.assertEquals(resultsInOrder.size(), resultsSmallestDomain.size());
        Assertions.assertEquals(resultsInOrder.size(), resultsMostAround.size());
    }

    @Test
    void testFutoshiki6x6() {
        futoshikiProblem = new Futoshiki_Problem(FutoshikiEnum.F6x6);
        futoshikiCSPSolver = new CSP_SolverForwardChecking<>(futoshikiProblem);
//        var resultsInOrder = futoshikiCSPSolver.getResults(FutoshikiHeuristicEnum.FH_IN_ORDER);
//        System.out.println(futoshikiCSPSolver);
//        var resultsMostAround = futoshikiCSPSolver.getResults(FutoshikiHeuristicEnum.FH_MOST_CONSTRAINTS);
//        System.out.println(futoshikiCSPSolver);
        var resultsSmallestDomain = futoshikiCSPSolver.getResults(FutoshikiHeuristicEnum.FH_SMALLEST_DOMAIN);
        System.out.println(futoshikiCSPSolver);
    }
}
