import java.util.ArrayList;

public interface CSP_Problem<P, D extends P> {
    ArrayList<Integer> getVariablesIndexes();
    CSP_PartialSolution<P, D> getInitialSolution();
}
