import consts.HeuristicEnum;

import java.util.ArrayList;

public interface CSP_Solver <P, D extends P, E extends HeuristicEnum, T extends CSP_Problem<P, D, E>, S extends CSP_PartialSolution<P, D, E>> {
    ArrayList<S> getResults(E chosenHeuristic);
    int getVisitedNodesCounter();
    int getTillFirstVisitedNodesCounter();
    int getReturnsCounter();
    int getTillFirstReturnsCounter();
}
