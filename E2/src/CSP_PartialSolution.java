import java.util.ArrayList;

interface CSP_PartialSolution<P, D extends P> {
    void removeNewValue(Integer variableItem);
    void setNewValue(D domainItem, Integer variableItem);
    boolean areConstraintsNotBrokenAfterLastChange();
    boolean isSatisfied();
    boolean checkConstraintsAfterLastChange();
    ArrayList<P> getPartialSolution();
    boolean updateVariables(Integer variableItem);
}