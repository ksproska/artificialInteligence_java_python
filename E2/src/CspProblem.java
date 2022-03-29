import java.util.ArrayList;

public interface CspProblem<D, V> {
    ArrayList<V> getVariables();
    CspPartialSolution<D, V> getInitialSolution();
}
