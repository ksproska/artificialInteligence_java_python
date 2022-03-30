import java.util.ArrayList;

public interface CspPartialSolution<P, D extends P> {
    void setNewValue(D domainItem, Integer variableItem);
    boolean areConstraintsNotBrokenAfterLastChange();
    boolean isSatisfied();
    boolean checkConstraintsAfterLastChange();
    D[] getDomain();
    CspPartialSolution<P, D> copy();
    ArrayList<P> getPartialSolution();
}
