import java.util.ArrayList;

interface CSP_PartialSolution<P, D extends P> {
    void removeValue(Integer variableItem);

    CSP_Variable<D> getNextVariable();

    boolean setNewValue(D domainItem, Integer variableIndex);
    boolean areConstraintsNotBrokenAfterLastChange();
    boolean isSatisfied();
    boolean checkConstraintsAfterLastChange();
    ArrayList<P> getPartialSolution();
    boolean updateVariables(Integer variableItem);

    <T extends CSP_PartialSolution<P, D>> T deepClone();
}