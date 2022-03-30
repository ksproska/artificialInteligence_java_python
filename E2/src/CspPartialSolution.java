import java.util.ArrayList;

public interface CspPartialSolution<D, V> {
    void setNewValue(D domainItem, V variableItem);
    boolean areConstraintsNotBrokenAfterLastChange();
    boolean isSatisfied();
    boolean checkConstraintsAfterLastChange();
    D[] getDomain();
    CspPartialSolution<D, V> copy();
    ArrayList<D> getPartialSolution();
}
