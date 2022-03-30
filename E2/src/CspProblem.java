import java.util.ArrayList;

public interface CspProblem<P, D extends P> {
    interface CspPartialSolution<P, D extends P> {
        void setNewValue(D domainItem, Integer variableItem);
        boolean areConstraintsNotBrokenAfterLastChange();
        boolean isSatisfied();
        boolean checkConstraintsAfterLastChange();
        D[] getDomain();
        ArrayList<P> getPartialSolution();
    }

    ArrayList<Integer> getVariablesIndexes();
    CspPartialSolution<P, D> getInitialSolution();
}
