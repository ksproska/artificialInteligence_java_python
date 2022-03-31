import java.util.ArrayList;


interface CSP_PartialSolution<P, D extends P> {
    boolean isSatisfied();
    <T extends CSP_PartialSolution<P, D>> T deepClone();
    CSP_Variable<D> getNextVariable();
    boolean setNewValueAtIndexOf(D domainItem, Integer variableIndex);
    boolean checkConstraintsAfterLastChange();
    void removeValueAtIndexOf(Integer variableItem);
    boolean updateVariables(Integer variableItem);

    ArrayList<P> getPartialSolution();
    boolean areConstraintsNotBrokenAfterLastChange();
}