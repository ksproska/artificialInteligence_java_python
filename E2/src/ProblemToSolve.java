import java.util.ArrayList;

public interface ProblemToSolve <D, V> {
    void readProblem(String folderPath);
    ArrayList<V> getVariables();
    ProblemSuggestedSolution<D, V> getInitialSolution();
}
