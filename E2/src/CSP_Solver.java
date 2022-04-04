import java.util.ArrayList;

public interface CSP_Solver <P, D extends P, T extends CSP_Problem<P, D>, S extends CSP_PartialSolution<P, D>> {
    ArrayList<S> getResults();
    int getVisitedNodesCounter();
    int getTillFirstVisitedNodesCounter();
    int getReturnsCounter();
    int getTillFirstReturnsCounter();
}
