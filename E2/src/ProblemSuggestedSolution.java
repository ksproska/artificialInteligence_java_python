import java.util.ArrayList;

public interface ProblemSuggestedSolution <D, V> {
    void setValue(D domainItem, V variableItem);
    boolean isCurrentCorrect();
    boolean isCompleted();
    boolean wereNegativelyAffected();
    D[] getDomain();
    ProblemSuggestedSolution<D, V> copy();
    ArrayList<D> getPartialSolution();
}
