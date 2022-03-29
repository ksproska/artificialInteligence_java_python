import java.util.ArrayList;

public interface CspPartialSolution<D, V> {
    void setValue(D domainItem, V variableItem);
    boolean isCurrentCorrect();
    boolean isCompleted();
    boolean wereNegativelyAffected();
    D[] getDomain();
    CspPartialSolution<D, V> copy();
    ArrayList<D> getPartialSolution();
}
