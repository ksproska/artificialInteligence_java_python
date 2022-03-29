import java.util.ArrayList;

public interface ProblemToSolve <D, V>{
    void readProblem(String folderPath);

//    D[] getDomain();
    ArrayList<V> getVariables();
    <P extends ProblemSuggestedSolution<D, V>> ArrayList<P> getResults();
}
