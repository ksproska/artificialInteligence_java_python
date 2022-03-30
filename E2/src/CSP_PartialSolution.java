import java.util.ArrayList;

interface CSP_PartialSolution<P, D extends P> {
    void setNewValue(D domainItem, Integer variableItem);
    boolean areConstraintsNotBrokenAfterLastChange();
    boolean isSatisfied();
    boolean checkConstraintsAfterLastChange();
    D[] getDomain();
    ArrayList<P> getPartialSolution();
}