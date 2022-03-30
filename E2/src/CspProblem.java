import java.util.ArrayList;

public interface CspProblem<P, D extends P> {
    ArrayList<Integer> getVariablesIndexes();
    CspPartialSolution<P, D> getInitialSolution();
}
