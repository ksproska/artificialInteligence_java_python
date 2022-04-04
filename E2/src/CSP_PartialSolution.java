import consts.HeuristicEnum;

import javax.net.ssl.HostnameVerifier;
import java.util.ArrayList;


interface CSP_PartialSolution<P, D extends P, H extends HeuristicEnum> {
    boolean isSatisfied();
    <T extends CSP_PartialSolution<P, D, H>> T deepClone();
    Integer getNextVariableIndex(H chosenHeuristic, Integer variableIndex);
    boolean setNewValueAtIndexOf(D domainItem, Integer variableIndex);
    boolean checkConstraintsAfterLastChange();
    void removeValueAtIndexOf(Integer variableItem);

    ArrayList<CSP_Variable<D>> getCspVariables();

    boolean updateVariables(Integer variableItem);

    ArrayList<P> getPartialSolution();
    boolean areConstraintsNotBrokenAfterLastChange();
}