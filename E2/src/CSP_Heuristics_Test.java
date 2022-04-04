import consts.BinaryEnum;
import consts.BinaryHeuristicEnum;
import consts.FutoshikiHeuristicEnum;
import org.junit.jupiter.api.Test;


public class CSP_Heuristics_Test {
    static Binary_Problem binaryProblem;
    static Futoshiki_Problem futoshikiProblem;
    static CSP_Solver<Integer, Integer, BinaryHeuristicEnum, Binary_Problem, Binary_PartialSolution> binaryCSPSolver;
    static CSP_Solver<Object, Integer, FutoshikiHeuristicEnum, Futoshiki_Problem, Futoshiki_PartialSolution> futoshikiCSPSolver;

    @Test
    void testBinaryBacktracking6x6() {
        binaryProblem = new Binary_Problem(BinaryEnum.B6x6);
        binaryCSPSolver = new CSP_SolverBacktracking<>(binaryProblem);
        var resultsInOrder = binaryCSPSolver.getResults(BinaryHeuristicEnum.BH_IN_ORDER);
        System.out.println(binaryCSPSolver);
        var resultsMostAround = binaryCSPSolver.getResults(BinaryHeuristicEnum.BH_MOST_AROUND);
        System.out.println(binaryCSPSolver);
    }
}
