import consts.BinaryEnum;
import consts.BinaryHeuristicEnum;
import consts.FutoshikiEnum;
import consts.FutoshikiHeuristicEnum;
import org.junit.jupiter.api.Test;


public class CSP_Heuristics_Test {
    static Binary_Problem binaryProblem;
    static Futoshiki_Problem futoshikiProblem;
    static CSP_Solver<Integer, Integer, BinaryHeuristicEnum, Binary_Problem, Binary_PartialSolution> binaryCSPSolver;
    static CSP_Solver<Object, Integer, FutoshikiHeuristicEnum, Futoshiki_Problem, Futoshiki_PartialSolution> futoshikiCSPSolver;

    @Test
    void testBinaryForwardChecking6x6() {
        binaryProblem = new Binary_Problem(BinaryEnum.B6x6);
        binaryCSPSolver = new CSP_SolverForwardChecking<>(binaryProblem);
        var resultsInOrder = binaryCSPSolver.getResults(BinaryHeuristicEnum.BH_IN_ORDER);
        System.out.println(binaryCSPSolver);
//        System.out.println(resultsInOrder);
        var resultsBiggestDomain = binaryCSPSolver.getResults(BinaryHeuristicEnum.BH_BIGGEST_DOMAIN);
        System.out.println(binaryCSPSolver);
//        System.out.println(resultsBiggestDomain);
        var resultsMostAround = binaryCSPSolver.getResults(BinaryHeuristicEnum.BH_MOST_AROUND);
        System.out.println(binaryCSPSolver);
//        System.out.println(resultsMostAround);
    }

    @Test
    void testBinaryBacktracking6x6() {
        binaryProblem = new Binary_Problem(BinaryEnum.B6x6);
        binaryCSPSolver = new CSP_SolverBacktracking<>(binaryProblem);
        var resultsInOrder = binaryCSPSolver.getResults(BinaryHeuristicEnum.BH_IN_ORDER);
        System.out.println(binaryCSPSolver);
        var resultsMostAround = binaryCSPSolver.getResults(BinaryHeuristicEnum.BH_MOST_AROUND);
        System.out.println(binaryCSPSolver);
    }

    @Test
    void testFutoshikiBacktracking5x5() {
        futoshikiProblem = new Futoshiki_Problem(FutoshikiEnum.F5x5);
        futoshikiCSPSolver = new CSP_SolverBacktracking<>(futoshikiProblem);
        var resultsInOrder = futoshikiCSPSolver.getResults(FutoshikiHeuristicEnum.FH_IN_ORDER);
        System.out.println(futoshikiCSPSolver);
        var resultsMostAround = futoshikiCSPSolver.getResults(FutoshikiHeuristicEnum.FH_MOST_CONSTRAINTS);
        System.out.println(futoshikiCSPSolver);
    }

    @Test
    void testFutoshikiBacktracking6x6() {
        futoshikiProblem = new Futoshiki_Problem(FutoshikiEnum.F6x6);
        futoshikiCSPSolver = new CSP_SolverBacktracking<>(futoshikiProblem);
        var resultsInOrder = futoshikiCSPSolver.getResults(FutoshikiHeuristicEnum.FH_IN_ORDER);
        System.out.println(futoshikiCSPSolver);
        var resultsMostAround = futoshikiCSPSolver.getResults(FutoshikiHeuristicEnum.FH_MOST_CONSTRAINTS);
        System.out.println(futoshikiCSPSolver);
    }
}
